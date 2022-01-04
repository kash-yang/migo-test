package tv.migo.test


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tv.migo.test.databinding.FragmentMainBinding
import tv.migo.test.db.data.PassType
import tv.migo.test.db.entity.PassInfo
import tv.migo.test.ui.adapter.PassItemAdapter
import tv.migo.test.ui.adapter.PassItemClickListener
import tv.migo.test.ui.viewmodel.PassViewModel
import tv.migo.test.utils.Resource
import tv.migo.test.utils.Status
import android.text.method.PasswordTransformationMethod
import android.util.Log


@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel by viewModels<PassViewModel>()
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val passItemAdapter = PassItemAdapter(requireContext(), object : PassItemClickListener {
            override fun onPassItemClicked(passInfo: PassInfo) {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(passInfo))
            }

            override fun onBuyPassClicked(passInfo: PassInfo) {
                lifecycleScope.launch {
                    viewModel.activatePass(passInfo).collect {
                        handleActionResource(it)
                    }
                }
            }
        })

        lifecycleScope.launch {
            viewModel.getStatus().collect {
                binding.statusResult.text = it.data?.toString() ?: it.exception?.message ?: ""
            }
        }

        binding.recyclerView.apply {
            val layoutManager = LinearLayoutManager(requireContext())
            this.layoutManager = layoutManager
            this.adapter = passItemAdapter
        }

        binding.fab.setOnClickListener {
            showAddPassDialog()
        }

        viewModel.getPassesLiveData().observe(viewLifecycleOwner) {
            passItemAdapter.update(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun<T> handleActionResource(res: Resource<T>) {
        when (res.status) {
            Status.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
            }
            Status.ERROR -> {
                Toast.makeText(
                    requireContext(),
                    String.format(requireContext().getString(R.string.error_with_message),  res.exception?.message),
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            }
            else -> {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun showAddPassDialog() {
        AlertDialog.Builder(requireContext()).apply {
            val viewInflated = LayoutInflater.from(requireContext()).inflate(R.layout.pass_input_dialog, view as ViewGroup, false)
            val spinner: Spinner = viewInflated.findViewById(R.id.pass_type_spinner)
            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.pass_type_array,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            val method = object : PasswordTransformationMethod() {
                override fun getTransformation(source: CharSequence, view: View): CharSequence {
                    return source
                }
            }
            val passLengthEt: TextInputEditText = viewInflated.findViewById(R.id.pass_length_et)
            val passPriceEt: TextInputEditText = viewInflated.findViewById(R.id.pass_price_et)
            with(method) {
                passPriceEt.transformationMethod = this
                passLengthEt.transformationMethod = this
            }

            setTitle(R.string.add_pass_dialog_title)
            setView(viewInflated)
            setPositiveButton(android.R.string.ok) { _, _ ->
                lifecycleScope.launch {
                    viewModel.addPass(
                        passType = PassType.valueOf(spinner.selectedItem as String),
                        passLength = passLengthEt.text.toString().toIntOrNull() ?: 0,
                        price = passPriceEt.text.toString().toIntOrNull() ?: 0
                    ).collect {
                        handleActionResource(it)
                    }
                }
            }
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }
}
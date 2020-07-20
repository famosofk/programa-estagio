package com.example.olhovivoestagio.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.olhovivoestagio.R
import com.example.olhovivoestagio.databinding.MainFragmentBinding

class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private lateinit var viewModel: SearchViewModel
    private var buscaSelecionada = ""
    private val adapter = SearchAdapter()
    private lateinit var bindingAux: MainFragmentBinding
    private var linha = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: MainFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        bindingAux = binding
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        bindingConfiguration(binding)
        setObservers(binding, adapter)
        viewModel.realizeAuth(resources.getString(R.string.api_key))

        return binding.root
    }


    private fun realizarBusca(binding: MainFragmentBinding) {
        /*Código removido por gerar alguns bugs em relação a listagem de conteúdo.
          val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
          imm.hideSoftInputFromWindow(binding.root.windowToken, 0) */

        binding.progressBar.visibility = View.VISIBLE
        val opcoes = resources.getStringArray(R.array.opcoesPesquisa)
        when (buscaSelecionada) {

            opcoes[0] -> {
                viewModel.getLineData(binding.editTextTextPersonName.text.toString().trim())
            }


            opcoes[1] -> {
                viewModel.getStopPoint(binding.editTextTextPersonName.text.toString().trim())
            }
            opcoes[2] -> {
                viewModel.getVehiclesArrivalAtStopPoint(
                    binding.editTextTextPersonName.text.toString().trim()
                )
            }

            opcoes[3] -> {
                val args: Array<String> =
                    binding.editTextTextPersonName.text.toString().split(",").toTypedArray()
                viewModel.getLineArrivalAtStopPoint(args[0], args[1])
            }
            opcoes[4] -> {
                viewModel.getCorredoresInfo()
            }

            opcoes[5] -> {
                linha = true
                viewModel.dataCollected.lista.clear()
                viewModel.getLinePositionAndLineStop(
                    binding.editTextTextPersonName.text.toString().trim()
                )
            }

            opcoes[6] -> {
                viewModel.getCorredoresStopPoint(
                    binding.editTextTextPersonName.text.toString().trim()
                )
            }
            else -> Log.e("Outras", "outras")

        }

    }


    private fun setObservers(binding: MainFragmentBinding, adapter: SearchAdapter) {

        viewModel.busca.observe(viewLifecycleOwner, Observer {
            realizarBusca(binding)
        })

        viewModel.navigate.observe(viewLifecycleOwner, Observer {

            binding.progressBar.visibility = View.GONE
            if (viewModel.dataCollected.lista.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putSerializable("dataCollected", viewModel.dataCollected)
                bundle.putBoolean("linha?", linha)
                bundle.putString("argumento", binding.editTextTextPersonName.text.toString())
                this.findNavController()
                    .navigate(R.id.action_searchFragment_to_mapsFragment, bundle)
            } else {
                Toast.makeText(context, getString(R.string.error404), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.updateRecycler.observe(viewLifecycleOwner, Observer{

            binding.progressBar.visibility = View.GONE
            adapter.submitList(viewModel.dataCollected.lista)
            Toast.makeText(
                context,
                "Dados atualizados: ${viewModel.dataCollected.lista.size}",
                Toast.LENGTH_SHORT
            ).show()
            if (adapter.itemCount > 0) {
                binding.recycler.smoothScrollToPosition(adapter.itemCount - 1)
            }
        })
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        buscaSelecionada = resources.getStringArray(R.array.opcoesPesquisa)[p2]
        if (::bindingAux.isInitialized) {
            when (p2) {
                0 -> {
                    bindingAux.editTextTextPersonName.text.clear()
                    bindingAux.editTextTextPersonName.hint = getString(R.string.getlinedata_hint)
                    bindingAux.textView.text = getString(R.string.getlinedata_string)
                }

                1 -> {
                    bindingAux.editTextTextPersonName.text.clear()
                    bindingAux.editTextTextPersonName.hint = getString(R.string.getstoppoint_hint)
                    bindingAux.textView.text = getString(R.string.getstoppoint_string)
                }
                2 -> {
                    bindingAux.editTextTextPersonName.text.clear()
                    bindingAux.editTextTextPersonName.hint =
                        getString(R.string.getstoppointLines_hint)
                    bindingAux.textView.text = getString(R.string.getstoppointLines_string)
                }

                3 -> {
                    bindingAux.editTextTextPersonName.text.clear()
                    bindingAux.editTextTextPersonName.hint =
                        getString(R.string.getprevisaochegada_hint)
                    bindingAux.textView.text = getString(R.string.getprevisaochegada_string)
                }
                4 -> {
                    bindingAux.editTextTextPersonName.text.clear()
                    bindingAux.editTextTextPersonName.hint =
                        resources.getString(R.string.getInformacaoCorredor_hint)
                    bindingAux.textView.text =
                        resources.getString(R.string.getInformacaoCorredor_string)
                }
                5 -> {
                    viewModel.dataCollected.lista.clear()
                    bindingAux.editTextTextPersonName.text.clear()
                    bindingAux.editTextTextPersonName.hint = getString(R.string.getline_hint)
                    bindingAux.textView.text = getString(R.string.getline_string)
                }
                6 -> {
                    bindingAux.editTextTextPersonName.text.clear()
                    bindingAux.editTextTextPersonName.hint =
                        resources.getString(R.string.getParadasCorredor_hint)
                    bindingAux.textView.text =
                        resources.getString(R.string.getParadasCorredor_string)
                }
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.v("Implementado", "Por necessidade.")
    }

    private fun bindingConfiguration(binding: MainFragmentBinding) {
        binding.viewModel = viewModel
        binding.spinner.onItemSelectedListener = this
        binding.lifecycleOwner = this
        binding.recycler.adapter = adapter
    }


}
package com.hyperether.pointsnaps.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hyperether.pointsnaps.R
import com.hyperether.pointsnaps.databinding.FragmentDescriptionBinding


class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!)[MainViewModel::class.java]

        binding.apply {
            toolbar.title = getString(R.string.description)
            toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_navigation_icon, null)
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            buttonDes.setOnClickListener {
                val description = descriptionTxt.text.toString()
                if (description.isNotEmpty()) {
                    viewModel.setDescription(description)
                    findNavController().popBackStack()
                }
            }

            descriptionTxt.requestFocus()
        }
    }
}

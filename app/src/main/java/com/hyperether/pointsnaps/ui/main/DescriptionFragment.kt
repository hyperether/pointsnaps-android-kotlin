package com.hyperether.pointsnaps.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.hyperether.pointsnaps.R
import kotlinx.android.synthetic.main.fragment_description.*
import kotlinx.android.synthetic.main.fragment_description.toolbar
import kotlinx.android.synthetic.main.fragment_location.*

/**
 * A simple [Fragment] subclass.
 */
class DescriptionFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_description, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        toolbar.setTitle(getString(R.string.description))
        toolbar.setNavigationIcon(resources.getDrawable(R.drawable.ic_navigation_icon))
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        buttonDes.setOnClickListener {
            val description = descriptionTxt.text.toString()
            if (!description.isEmpty()) {
                viewModel.setDescription(description)
                findNavController().popBackStack()
            }
        }

        descriptionTxt.requestFocus()
    }

}

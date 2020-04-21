package com.hyperether.pointsnaps.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.hyperether.pointsnaps.R
import kotlinx.android.synthetic.main.fragment_description.*

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

        buttonDes.setOnClickListener {
            val description = descriptionTxt.text.toString()
            if (!description.isEmpty())
                viewModel.setDescription(description)
        }
    }

}

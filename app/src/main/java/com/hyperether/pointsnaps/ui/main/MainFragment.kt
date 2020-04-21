package com.hyperether.pointsnaps.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hyperether.pointsnaps.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private var description = ""

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        setupObservers()

        buttonLoc.setOnClickListener { v->
            findNavController().navigate(R.id.action_main_to_location)
        }

        buttonDes.setOnClickListener { v->
            findNavController().navigate(R.id.action_main_to_description)
        }

        mainButton.setOnClickListener {
            Toast.makeText(context, description, Toast.LENGTH_LONG).show()
        }
    }


    fun setupObservers() {
        viewModel.getDescriptionLiveData().observe(viewLifecycleOwner, Observer {
            description = it
        })
    }
}

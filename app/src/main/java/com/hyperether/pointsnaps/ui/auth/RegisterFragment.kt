package com.hyperether.pointsnaps.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.hyperether.pointsnaps.R
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters


    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        signinTxt.setOnClickListener { v ->
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}

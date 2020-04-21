package com.hyperether.pointsnaps.ui.auth

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
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.passwordET

class LoginFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        setupObservers()

        signupTxt.setOnClickListener { v ->
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment);
        }

        signinBtn.setOnClickListener {
            if (!usernameET.text.toString().isEmpty() && !passwordET.text.toString().isEmpty())
                viewModel.loginUser(usernameET.text.toString(), passwordET.text.toString())
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(R.id.goToMainNav)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

}

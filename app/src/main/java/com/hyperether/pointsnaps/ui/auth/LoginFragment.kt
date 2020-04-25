package com.hyperether.pointsnaps.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hyperether.pointsnaps.R
import com.hyperether.pointsnaps.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(AuthViewModel::class.java)
        toolbar.setTitle(getString(R.string.sign_in))
        toolbar.setNavigationIcon(resources.getDrawable(R.drawable.ic_navigation_icon))
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.goToMainNav)
        }
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
                viewModel.user.postValue(null)
                findNavController().navigate(R.id.goToMainNav)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            createToast(it)
        })

        viewModel.registerUser.observe(viewLifecycleOwner, Observer {
            usernameET.setText(it.email)
            passwordET.setText(it.password)
        })
    }

}

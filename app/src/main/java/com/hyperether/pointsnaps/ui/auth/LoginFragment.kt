package com.hyperether.pointsnaps.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hyperether.pointsnaps.R
import com.hyperether.pointsnaps.databinding.FragmentLoginBinding
import com.hyperether.pointsnaps.ui.base.BaseFragment


class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!)[AuthViewModel::class.java]
        binding.apply {
            toolbar.title = getString(R.string.sign_in)
            toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_navigation_icon)
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.goToMainNav)
            }

            signupTxt.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            signinBtn.setOnClickListener {
                if (usernameET.text.toString().isNotEmpty() && passwordET.text.toString()
                        .isNotEmpty()
                )
                    progressBar.visibility = VISIBLE
                viewModel.loginUser(usernameET.text.toString(), passwordET.text.toString()) {
                    progressBar.visibility = GONE
                }
            }
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.user.postValue(null)
                findNavController().navigate(R.id.goToMainNav)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            createToast(it)
        }

        viewModel.registerUser.observe(viewLifecycleOwner) {
            binding.apply {
                usernameET.setText(it.email)
                passwordET.setText(it.password)
            }
        }
    }
}

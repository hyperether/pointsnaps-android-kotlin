package com.hyperether.pointsnaps.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hyperether.pointsnaps.R
import com.hyperether.pointsnaps.databinding.FragmentRegisterBinding
import com.hyperether.pointsnaps.ui.base.BaseFragment
import com.hyperether.pointsnaps.utils.Utils


class RegisterFragment : BaseFragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]
        setupObservers()
        binding.apply {
            toolbar.title = getString(R.string.location)
            toolbar.navigationIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_navigation_icon, null)
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            signinTxt.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            signupBtn.setOnClickListener {
                if (!Utils.fieldEmptyValidator(arrayOf(emailET, passwordET, codeET, usernameET))) {
                    createToast(getString(R.string.fill_fields))
                } else if (!Utils.fullNameValidator(usernameET)) {
                    createToast(getString(R.string.full_name_validation_toast))
                } else {
                    progressBar.visibility = VISIBLE
                    viewModel.registerUser(
                        emailET.text.toString(),
                        usernameET.text.toString().split(" ")[0],
                        usernameET.text.toString().split(" ")[1],
                        passwordET.text.toString(),
                        codeET.text.toString()
                    ) {
                        progressBar.visibility = GONE
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            createToast(it)
        }

        viewModel.registerUser.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}

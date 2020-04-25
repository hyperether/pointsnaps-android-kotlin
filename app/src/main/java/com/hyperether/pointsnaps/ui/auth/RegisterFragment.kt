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
import com.hyperether.pointsnaps.utils.Utils
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : BaseFragment() {
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
        viewModel = ViewModelProvider(activity!!).get(AuthViewModel::class.java)
        setupObservers()
        toolbar.setTitle(getString(R.string.location))
        toolbar.setNavigationIcon(resources.getDrawable(R.drawable.ic_navigation_icon))
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        signinTxt.setOnClickListener { v ->
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        signupBtn.setOnClickListener {
            if (!Utils.fieldEmptyValidator(arrayOf(emailET, passwordET, codeET, usernameET))) {
                createToast(getString(R.string.fill_fields))
            } else if (!Utils.fullNameValidator(usernameET)) {
                createToast(getString(R.string.full_name_validation_toast))
            } else {
                viewModel.registerUser(
                    emailET.text.toString(),
                    usernameET.text.toString().split(" ")[0],
                    usernameET.text.toString().split(" ")[1],
                    passwordET.text.toString(),
                    codeET.text.toString()
                )
            }
        }
    }

    fun setupObservers() {
        viewModel.error.observe(viewLifecycleOwner, Observer {
            createToast(it)
        })

        viewModel.registerUser.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }
}

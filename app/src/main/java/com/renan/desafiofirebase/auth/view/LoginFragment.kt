package com.renan.desafiofirebase.auth.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.renan.desafiofirebase.R
import com.renan.desafiofirebase.auth.viewmodel.AuthenticatorViewModel
import com.renan.desafiofirebase.utils.ProjectUtils
import com.renan.desafiofirebase.utils.ProjectUtils.hideKeyboard


class LoginFragment : Fragment() {

    private lateinit var _view: View
    private val authenticatorViewModel: AuthenticatorViewModel by lazy {
        ViewModelProvider(this).get(AuthenticatorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _view = view
        checkUserId()
        login()
        createAccount()
    }

    private fun checkUserId() {
        if (!ProjectUtils.getUserId(requireActivity()).isNullOrEmpty()) {
            val navController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun login() {
        val loginBtn = _view.findViewById<MaterialButton>(R.id.btnLogin)
        loginBtn.setOnClickListener {
            hideKeyboard(_view)
            navigateLogin()
        }
        initViewModel()
    }

    private fun initViewModel() {
        authenticatorViewModel.stateLogin.observe(viewLifecycleOwner, { state ->
            state?.let {
                navigateToHomeEmail(it)
            }
        })
        authenticatorViewModel.error.observe(viewLifecycleOwner, { loading ->
            loading?.let {
                messageError(it)
            }
        })
    }

    private fun navigateLogin() {
        val email = _view.findViewById<TextInputEditText>(R.id.tietEmailLogin).text.toString()
        val password = _view.findViewById<TextInputEditText>(R.id.tietPasswordLogin).text.toString()
        when {
            ProjectUtils.validateEmailPassword(email, password) -> {
                authenticatorViewModel.loginEmailPassword(requireActivity(), email, password)
            }
            else -> {
                Snackbar.make(
                    _view,
                    "Campos inválidos",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun navigateToHomeEmail(status: Boolean) {
        val navController = findNavController()
        if (status) {
            authenticatorViewModel.signInProvider()
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun messageError(it: String) {
        Snackbar.make(_view, it, Snackbar.LENGTH_LONG).show()
    }

    private fun createAccount() {
        val navControler = findNavController()
        val btnSignup = view?.findViewById<Button>(R.id.btnSignup)

        btnSignup?.setOnClickListener {
            navControler.navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }
}
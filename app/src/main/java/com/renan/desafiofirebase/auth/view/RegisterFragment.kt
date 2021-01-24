package com.renan.desafiofirebase.auth.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.renan.desafiofirebase.R
import com.renan.desafiofirebase.auth.viewmodel.AuthenticatorViewModel
import com.renan.desafiofirebase.utils.AuthUtil.hideKeyboard
import com.renan.desafiofirebase.utils.AuthUtil.validateNameEmailPassword

class RegisterFragment : Fragment() {

    private val viewModel: AuthenticatorViewModel by lazy {
        ViewModelProvider(this).get(AuthenticatorViewModel::class.java)
    }

    private lateinit var _view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _view = view

        registerUser()
    }

    private fun registerUser() {
        val btnRegister = _view.findViewById<MaterialButton>(R.id.btnConfirmSignup)

        btnRegister?.setOnClickListener {
            hideKeyboard(_view)
            validateRegister()
        }
        initViewModel()
    }

    private fun validateRegister() {
        val name = _view.findViewById<TextInputEditText>(R.id.tietNameRegister)?.text.toString()
        val email = _view.findViewById<TextInputEditText>(R.id.tietEmailRegister)?.text.toString()
        val password =
            view?.findViewById<TextInputEditText>(R.id.tietPasswordRegister)?.text.toString()

        when {
            validateNameEmailPassword(name, email, password) -> {
                viewModel.registerUser(requireActivity(), name, email, password)
            }
        }

    }

    private fun initViewModel() {
        viewModel.stateRegister.observe(viewLifecycleOwner, { state ->
            state?.let {
                navigateToLogin(it)
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, { state ->
            state?.let {
                showloading(it)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, { state ->
            state?.let {
                messageError(it)
            }
        })
    }

    private fun messageError(it: String) {
        val btnRegister = _view.findViewById<MaterialButton>(R.id.btnSignup)
        Snackbar.make(btnRegister, it, Snackbar.LENGTH_LONG).show()

    }

    private fun showloading(status: Boolean) {
        val pb_register = _view.findViewById<ProgressBar>(R.id.pb_register)
        when {
            status -> {
                pb_register.visibility = View.VISIBLE
            }
            else -> {
                pb_register.visibility = View.GONE
            }
        }
    }


    private fun navigateToLogin(isRegistered: Boolean) {
        if (isRegistered) {
            Snackbar.make(
                _view, "Email de verificação enviado com sucesso",
                Snackbar.LENGTH_LONG
            ).show()
            Handler(Looper.getMainLooper()).postDelayed({
                requireActivity().onBackPressed()
            }, 500)
        }
    }
}
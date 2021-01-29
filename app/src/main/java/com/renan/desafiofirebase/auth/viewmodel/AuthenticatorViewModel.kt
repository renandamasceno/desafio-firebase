package com.renan.desafiofirebase.auth.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.renan.desafiofirebase.utils.ProjectUtils

class AuthenticatorViewModel(application: Application) : AndroidViewModel(application) {

    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    var stateRegister: MutableLiveData<Boolean> = MutableLiveData()
    var stateLogin: MutableLiveData<Boolean> = MutableLiveData()

    fun registerUser(activity: Activity, name: String, email: String, password: String) {
        loading.value = true

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                loading.value = false

                when {
                    task.isSuccessful -> {
                        val user = FirebaseAuth.getInstance().currentUser
                        ProjectUtils.saveUserId(
                            getApplication(),
                            FirebaseAuth.getInstance().currentUser?.uid
                        )
                        user?.sendEmailVerification()?.addOnCompleteListener {
                            stateRegister.value = true
                        }
                        stateRegister.value = true
                    }
                    else -> {
                        errorMessage("Autenticação Falhou")
                        stateRegister.value = false
                        loading.value = false
                    }
                }

            }
    }


    fun loginEmailPassword(activity: Activity, email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                when {
                    task.isSuccessful -> {
                        if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val provider = signInProvider()

                            ProjectUtils.saveUserId(
                                getApplication(),
                                currentUser?.uid
                            )

                            ProjectUtils.saveUserName(
                                getApplication(),
                                currentUser?.displayName
                            )

                            ProjectUtils.saveUserEmail(
                                getApplication(),
                                currentUser?.email
                            )

                            ProjectUtils.saveUserImage(
                                getApplication(),
                                currentUser?.photoUrl.toString()
                            )

                            stateLogin.value = true
                        } else {
                            errorMessage("Verifique o email")
                            stateLogin.value = false
                            loading.value = false
                        }
                    }
                    else -> {
                        errorMessage("Autenticação falhou")
                        stateLogin.value = false
                        loading.value = false
                    }
                }
            }
    }

    fun signInProvider(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val provider = currentUser?.getIdToken(false)?.result?.signInProvider
        Log.d("authenticator", provider.toString())
        ProjectUtils.saveSignInMethod(
            getApplication(),
            provider
        )
        return provider
    }

    private fun errorMessage(s: String) {
        error.value = s
    }

}
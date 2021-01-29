package com.renan.desafiofirebase.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import com.renan.desafiofirebase.utils.Constants.APP_KEY
import com.renan.desafiofirebase.utils.Constants.EMPTY_STRING
import com.renan.desafiofirebase.utils.Constants.PASSWORD
import com.renan.desafiofirebase.utils.Constants.UIID_KEY
import com.renan.desafiofirebase.utils.Constants.USER_EMAIL
import com.renan.desafiofirebase.utils.Constants.USER_NAME
import com.renan.desafiofirebase.utils.Constants.USER_PHOTO_URL
import com.renan.desafiofirebase.utils.Constants.USER_SIGN_IN_METHOD
import java.util.*

object ProjectUtils {

    fun saveUserId(context: Context, uiid: String?) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        preferences.edit().putString(UIID_KEY, uiid).apply()
    }

    fun saveUserName(context: Context, name: String?) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

        if (!name.isNullOrEmpty()) {
            val newString = name.split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }
            preferences.edit().putString(USER_NAME, newString).apply()
        } else {
            preferences.edit().putString(USER_NAME, name).apply()
        }
    }

    fun saveUserEmail(context: Context, email: String?) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        preferences.edit().putString(USER_EMAIL, email).apply()
    }

    fun saveSignInMethod(context: Context, method: String?) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        if (!method.isNullOrEmpty()) {
            preferences.edit().putString(USER_SIGN_IN_METHOD, method).apply()
        } else {
            preferences.edit().putString(USER_SIGN_IN_METHOD, "").apply()
        }
    }

    fun saveUserImage(context: Context, photoUrl: String?) {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        preferences.edit().putString(USER_PHOTO_URL, photoUrl).apply()
    }

    fun getUserId(context: Context): String? {
        val preferences = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        return preferences.getString(UIID_KEY, EMPTY_STRING)
    }

    fun getUserName(context: Context): String? {
        val preferences = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        return preferences.getString(USER_NAME, EMPTY_STRING)
    }

    fun getUserEmail(context: Context): String? {
        val preferences = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        return preferences.getString(USER_EMAIL, EMPTY_STRING)
    }

    private fun getUserProvider(context: Context): String? {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        return preferences.getString(USER_SIGN_IN_METHOD, EMPTY_STRING)
    }

    fun getUserImage(context: Context): String? {
        val preferences: SharedPreferences =
            context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        return preferences.getString(USER_PHOTO_URL, EMPTY_STRING)
    }

    fun clearUserInfo(context: Context) {
        val preferences = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
        preferences.edit().putString(UIID_KEY, EMPTY_STRING).apply()
        preferences.edit().putString(USER_NAME, EMPTY_STRING).apply()
        preferences.edit().putString(USER_EMAIL, EMPTY_STRING).apply()
        preferences.edit().putString(USER_PHOTO_URL, EMPTY_STRING).apply()
        preferences.edit().putString(USER_SIGN_IN_METHOD, EMPTY_STRING).apply()
    }

    fun validateNameEmailPassword(name: String, email: String, password: String): Boolean {
        return when {
            name.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                false
            }
            password.length < 6 -> {
                false
            }
            else -> true
        }
    }

    fun validateEmailPassword(email: String, password: String): Boolean {
        return when {
            email.isEmpty() || password.isEmpty() -> {
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                false
            }
            password.length < 6 -> {
                false
            }
            else -> true
        }
    }

    fun validateName(name: String): Boolean {
        return name.isNotEmpty()
    }

    fun validadeEmail(context: Context, email: String): Boolean {
        return if (getUserProvider(context) == PASSWORD) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    fun validatePassword(password: String): Boolean {
        return password.length > 6
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getFileExtension(view: View, uri: Uri?): String? {
        return MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(view.context.contentResolver.getType(uri!!))
    }

    fun validateText(title: String?, date: String?, description: String?): Boolean {
        return when {
            title.isNullOrEmpty() || date.isNullOrEmpty() || description.isNullOrEmpty() -> {
                false
            }
            else -> true
        }
    }
}
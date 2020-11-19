package com.ardeal.labandroid.auth.data

import android.util.Log
import com.ardeal.labandroid.auth.data.remote.RemoteAuthDataSource
import com.ardeal.labandroid.core.Api
import com.ardeal.labandroid.core.Constants
import com.ardeal.labandroid.core.Result
import com.ardeal.labandroid.core.TAG

object AuthRepository {
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        Constants.instance()?.deleteValueString("token")
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = RemoteAuthDataSource.login(user)
        Log.v(TAG, "Userul este:")
        Log.v(TAG, user.toString())
        if (result is Result.Success<TokenHolder>) {
            Log.v(TAG, "Tokenul este:")
            Log.v(TAG, result.data.token)
            setLoggedInUser(user, result.data)
            Constants.instance()?.storeValueString("token", result.data.token)
            Log.v(TAG, "l-a salvat oare? tokenul din constants este:") ///////
            Log.v(TAG, Constants.instance()?.fetchValueString("token").toString())
        }
        return result
    }

    private fun setLoggedInUser(user: User, tokenHolder: TokenHolder) {
        AuthRepository.user = user
        Api.tokenInterceptor.token = tokenHolder.token
    }
}
package com.francotte.auth

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager

/** Clears the Android Credential Manager state (used for Google Sign-In). */
class AndroidCredentialStateClearer(context: Context) : CredentialStateClearer {
    private val credentialManager = CredentialManager.create(context)

    override suspend fun clear() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }
}

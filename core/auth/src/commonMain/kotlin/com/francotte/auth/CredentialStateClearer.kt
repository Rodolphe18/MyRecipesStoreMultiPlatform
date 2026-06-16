package com.francotte.auth

/**
 * Clears any saved platform credential state (e.g. Android Credential Manager / Google).
 * No-op on platforms without a credential manager.
 */
interface CredentialStateClearer {
    suspend fun clear()
}

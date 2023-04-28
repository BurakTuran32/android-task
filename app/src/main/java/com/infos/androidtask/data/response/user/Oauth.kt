package com.infos.androidtask.data.response.user

data class Oauth(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: Any,
    val token_type: String
)
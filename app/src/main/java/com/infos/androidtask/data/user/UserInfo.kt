package com.infos.androidtask.data.user

data class UserInfo(
    val active: Boolean,
    val businessUnit: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val personalNo: Int
)
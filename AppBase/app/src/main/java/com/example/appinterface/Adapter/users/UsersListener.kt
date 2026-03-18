package com.example.appinterface.Adapter.users

import com.example.appinterface.Api.Models.User

interface UsersListener {
    fun onShowInfo(user: User)
    fun onEditUser(user: User)
    fun onDelete(user: User)
}
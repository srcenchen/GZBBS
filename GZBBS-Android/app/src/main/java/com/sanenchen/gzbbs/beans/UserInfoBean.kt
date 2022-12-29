package com.sanenchen.gzbbs.beans

data class UserInfoBeanItem(
    val `class`: String,
    val nickName: String,
    val password: String,
    val realName: String,
    val userName: String
)

class UserInfoBean : ArrayList<UserInfoBeanItem>()
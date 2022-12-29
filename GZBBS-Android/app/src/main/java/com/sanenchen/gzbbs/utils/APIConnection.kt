package com.sanenchen.gzbbs.utils

import com.google.gson.Gson
import com.sanenchen.gzbbs.beans.LoginSignBean
import com.sanenchen.gzbbs.beans.UserInfoBean
import com.sanenchen.gzbbs.beans.UserInfoBeanItem
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * @author sanenchen
 * API连接
 */
class APIConnection {
    companion object {

        const val APIUrl = "http://api.gzbbs.luckysan.top"

        /**
         * 登录验证
         */
        fun loginSign(userName: String, password: String): Boolean {
            val client = OkHttpClient()
            val request = Request.Builder().url("$APIUrl/loginSign/?username=$userName&password=$password").build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            // 解析JSON
            val loginSignBean = Gson().fromJson(responseData, LoginSignBean::class.java)
            return loginSignBean.code == 1
        }

        /**
         * 个人信息
         */
        fun userInfo(userName: String): UserInfoBeanItem {
            val client = OkHttpClient()
            val request = Request.Builder().url("$APIUrl/userInfo/?username=$userName").build()
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            // 解析JSON
            return Gson().fromJson(responseData, UserInfoBean::class.java)[0]
        }
    }
}
package com.sanenchen.gzbbs.utils

import android.content.Context

/**
 * @author sanenchen
 * SharedPreferences工具类
 */
class SharedPreferencesUtils {
    companion object {
        /**
         * @description 保存数据
         * @param key 键
         * @param value 值
         */
        fun saveData(context: Context, key: String, value: String) {
            val sharedPreferences = context.getSharedPreferences("GZBBS", 0)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }

        /**
         * @description 读取数据
         * @param key 键
         * @return 值
         */
        fun getData(context: Context, key: String): String {
            val sharedPreferences = context.getSharedPreferences("GZBBS", 0)
            return sharedPreferences.getString(key, "") ?: ""
        }

    }
}
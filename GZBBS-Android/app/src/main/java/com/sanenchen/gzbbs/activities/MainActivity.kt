package com.sanenchen.gzbbs.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.sanenchen.gzbbs.beans.UserInfoBeanItem
import com.sanenchen.gzbbs.ui.theme.GZBBSTheme
import com.sanenchen.gzbbs.utils.APIConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author sanenchen
 * @description 主界面
 */
class MainActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GZBBSTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("作业共享") }) },
                ) {
                    Column(Modifier.padding(it)) {
                        var userInfo by remember { mutableStateOf<UserInfoBeanItem?>(null) }
                        val username = intent.getStringExtra("username")!!
                        LaunchedEffect(Unit) {
                            this.launch(Dispatchers.IO) {
                                userInfo = APIConnection.userInfo(username)
                            }
                        }
                        Text("欢迎你，${userInfo?.realName}")
                        Text("你的学号是：${userInfo?.userName}")
                        Text("你的班级是：${userInfo?.`class`}")
                    }
                }
            }
        }
    }
}
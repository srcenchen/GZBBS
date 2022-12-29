package com.sanenchen.gzbbs.activities

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.gson.Gson
import com.sanenchen.gzbbs.beans.UserInfoBeanItem
import com.sanenchen.gzbbs.ui.theme.GZBBSTheme
import com.sanenchen.gzbbs.utils.APIConnection
import com.sanenchen.gzbbs.utils.SharedPreferencesUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.sanenchen.gzbbs.R
import com.sanenchen.gzbbs.utils.APIConnection.Companion.updateInfo

/**
 * @author sanenchen
 * @description 主界面
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GZBBSTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("作业共享") }) },
                ) {
                    Box(Modifier.padding(it)) {
                        var userInfo by remember {
                            mutableStateOf<UserInfoBeanItem?>(
                                Gson().fromJson(SharedPreferencesUtils.getData(this@MainActivity, "userInfo"), UserInfoBeanItem::class.java)
                            )
                        }
                        val username = intent.getStringExtra("username")!!
                        LaunchedEffect(Unit) {
                            this.launch(Dispatchers.IO) {
                                userInfo = APIConnection.userInfo(username)
                                // 保存用户信息
                                SharedPreferencesUtils.saveData(this@MainActivity, "userInfo", Gson().toJson(userInfo))
                            }
                        }

                        if (userInfo != null) {
                            if (userInfo!!.nickName == "学友") {
                                Welcome()
                            } else {

                            }
                        }


                    }
                }
            }
        }
    }

    /**
     * 密码验证
     */
    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            val username = SharedPreferencesUtils.getData(this@MainActivity, "username")
            val password = SharedPreferencesUtils.getData(this@MainActivity, "password")
            // 密码验证
            val success = APIConnection.loginSign(username, password)
            if (!success) {
                // 验证不通过，重返登录界面
                // 清除保存的密码
                Looper.prepare()
                Toast.makeText(this@MainActivity, "身份信息过期，请重新登录！", Toast.LENGTH_SHORT).show()
                SharedPreferencesUtils.saveData(this@MainActivity, "username", "")
                SharedPreferencesUtils.saveData(this@MainActivity, "password", "")
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                Looper.loop()
            }

        }
    }

    /**
     * 迎新
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Welcome() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            val lottieComposition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hi))
            LottieAnimation(composition = lottieComposition.value, iterations = LottieConstants.IterateForever, modifier = Modifier.size(180.dp))
            Text("Hi，新朋友。", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
            Text("欢迎来到作业共享平台，", fontSize = 16.sp, modifier = Modifier.padding(top = 16.dp))
            Text("你将在这里认识许多伙伴，", fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
            Text("你现在需要完善个人信息。", fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))

            var nickname by remember { mutableStateOf("学友") }
            var password by remember { mutableStateOf("") }
            TextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("昵称") },
                modifier = Modifier.padding(top = 16.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("重置密码") },
                modifier = Modifier.padding(top = 8.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    // 密码非空判断
                    if (password == "") {
                        Toast.makeText(this@MainActivity, "密码不能为空！", Toast.LENGTH_SHORT).show()
                        return@KeyboardActions
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (updateInfo(intent.getStringExtra("username")!!, nickname, password))
                                onResume()
                            else {
                                launch(Dispatchers.Main) {
                                    Toast.makeText(this@MainActivity, "操作失败", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                })
            )
            Button(onClick = {
                // 密码非空判断
                if (password == "") {
                    Toast.makeText(this@MainActivity, "密码不能为空！", Toast.LENGTH_SHORT).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (updateInfo(intent.getStringExtra("username")!!, nickname, password))
                            onResume()
                        else {
                            launch(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity, "操作失败", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }, modifier = Modifier.padding(top = 8.dp)) {
                Text("提交")
            }
        }
    }

}
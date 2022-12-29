package com.sanenchen.gzbbs.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.airbnb.lottie.compose.*
import com.sanenchen.gzbbs.R
import com.sanenchen.gzbbs.beans.LoginSignBean
import com.sanenchen.gzbbs.ui.theme.GZBBSTheme
import com.sanenchen.gzbbs.utils.APIConnection.Companion.loginSign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author sanenchen
 * @description 登录引导界面
 */
class LoginActivity : ComponentActivity() {
    val login = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GZBBSTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AnimatedVisibility(login.value, enter = slideInVertically(), exit = slideOutVertically()) {
                        Login()
                    }
                    if (!login.value) {
                        Guide()
                    }
                }

            }

        }
    }

    /**
     * 引导界面
     */
    @Composable
    fun Guide() {
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val lottieComposition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.welcome))
            LottieAnimation(
                composition = lottieComposition.value,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(370.dp)
            )
            Text("欢迎👏🏻", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(text = "我们欢迎每一位同学的加入。", modifier = Modifier.padding(top = 8.dp))
            Text("但在此之前，", modifier = Modifier.padding(top = 8.dp))
            Text("让我们先确认下身份吧！", modifier = Modifier.padding(top = 8.dp))
            Button(onClick = { login.value = true }, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
                Text("GO GO GO")
            }
        }
    }

    /**
     * 登录界面
     */
    @OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
    @Composable
    fun Login() {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("登录") })
            }
        ) {
            Column(
                Modifier
                    .padding(it)
                    .padding(start = 16.dp, end = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var user by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                val coroutineScope = rememberCoroutineScope()
                TextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text("用户名") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.NumberPassword)
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("密码") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation()
                )
                Row {
                    OutlinedButton(onClick = { login.value = false }, modifier = Modifier.padding(top = 16.dp, end = 8.dp)) {
                        Text("算啦")
                    }
                    Button(
                        onClick = { login(user, password, coroutineScope) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("登录")
                    }
                }

                val lottieComposition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.security))
                if (login.value)
                    LottieAnimation(composition = lottieComposition.value, iterations = LottieConstants.IterateForever, modifier = Modifier.size(370.dp))

            }
        }
    }

    /**
     * 登录
     */
    fun login(user: String, password: String, coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            val isSigned = loginSign(user, password)
            launch(Dispatchers.Main) {
                if (isSigned) {
                    Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java).putExtra("username", user))
                    finish()
                } else
                    Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


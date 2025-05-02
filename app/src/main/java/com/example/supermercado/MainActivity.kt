package com.example.supermercado

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.supermercado.service.ServiceLocator
import com.example.supermercado.util.MessageUtil
import com.example.supermercado.view.PrincipalActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val loginService = ServiceLocator.loginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etUser = findViewById<EditText>(R.id.et_user)
        val etPassword = findViewById<EditText>(R.id.et_password)

        val btnEnter = findViewById<Button>(R.id.btn_enter)
        btnEnter.isEnabled = false
        btnEnter.setOnClickListener {
            Log.i("MainActivity", "Login button clicked")
            CoroutineScope(Dispatchers.Main).launch {
                val username = etUser.text.toString()
                val password = etPassword.text.toString()

                if (username.isEmpty() || password.isEmpty()) {
                    val message = getString(R.string.user_password_not_filled)
                    MessageUtil.showMessage(message, this@MainActivity)
                    return@launch
                }

                try {
                    val token = loginService.login(username, password, this@MainActivity)
                    val intent = Intent(this@MainActivity, PrincipalActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Log.e("LoginError", "Login failed", e)
                    MessageUtil.showMessage(getString(R.string.login_error), this@MainActivity)
                    return@launch
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            val isExpired = loginService.isTokenExpired(this@MainActivity)
            if (!isExpired) {
                val intent = Intent(this@MainActivity, PrincipalActivity::class.java)
                startActivity(intent)
            }
            btnEnter.isEnabled = true
        }
    }
}
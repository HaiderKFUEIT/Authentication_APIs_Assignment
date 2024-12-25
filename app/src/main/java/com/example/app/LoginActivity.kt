package com.example.app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)

        val emailField = findViewById<EditText>(R.id.etEmail)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerButton = findViewById<Button>(R.id.btnRegister)

        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            val client = OkHttpClient()
            val json = JSONObject()
            json.put("email", email)
            json.put("password", password)

            val body = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
            val request = Request.Builder()
                .url("http://192.168.100.3/my_app/login.php")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { Toast.makeText(this@LoginActivity, "Failed: $e", Toast.LENGTH_SHORT).show() }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseString = response.body?.string()

                    if (responseString != null) {
                        val responseJson = JSONObject(responseString)
                        val status = responseJson.getString("status")

                        if (status == "success") {
                            val token = responseJson.getString("token")

                            // Save the token in SharedPreferences
                            val editor = sharedPreferences.edit()
                            editor.putString("user_token", token)
                            editor.apply()

                            // Navigate to DisplayUsersActivity
                            val intent = Intent(this@LoginActivity, DisplayUsersActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            runOnUiThread { Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show() }
                        }
                    }
                }
            })
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}

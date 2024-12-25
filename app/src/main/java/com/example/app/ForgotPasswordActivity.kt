package com.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val emailField = findViewById<EditText>(R.id.etEmail)
        val newPasswordField = findViewById<EditText>(R.id.etNewPassword)
        val submitButton = findViewById<Button>(R.id.btnSubmit)

        submitButton.setOnClickListener {
            val email = emailField.text.toString()
            val newPassword = newPasswordField.text.toString()

            val client = OkHttpClient()
            val json = JSONObject()
            json.put("email", email)
            json.put("new_password", newPassword)

            val body = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
            val request = Request.Builder()
                .url("http://192.168.100.3/my_app/forgot_password.php")
                .post(body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@ForgotPasswordActivity, "Failed: $e", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseString = response.body?.string()

                    if (responseString != null) {
                        val responseJson = JSONObject(responseString)
                        val status = responseJson.getString("status")

                        if (status == "success") {
                            runOnUiThread {
                                Toast.makeText(this@ForgotPasswordActivity, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@ForgotPasswordActivity, "Failed to change password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })
        }
    }
}

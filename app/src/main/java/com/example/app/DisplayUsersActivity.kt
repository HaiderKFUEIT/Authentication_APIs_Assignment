package com.example.app

import User
import UsersAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class DisplayUsersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_users)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://192.168.100.3/my_app/display_users.php") // Replace with correct URL
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@DisplayUsersActivity, "Failed: $e", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body?.string()

                if (responseString != null) {
                    val responseJson = JSONObject(responseString)
                    val status = responseJson.getString("status")

                    if (status == "success") {
                        val usersArray = responseJson.getJSONArray("users")
                        val usersList = mutableListOf<User>()

                        for (i in 0 until usersArray.length()) {
                            val userObject = usersArray.getJSONObject(i)
                            val user = User(
                                userObject.getString("name"),
                                userObject.getString("email"),
                                userObject.getString("password") // Make sure to include password
                            )
                            usersList.add(user)
                        }

                        runOnUiThread {
                            val adapter = UsersAdapter(usersList)
                            recyclerView.adapter = adapter
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@DisplayUsersActivity, "Failed to load users", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}


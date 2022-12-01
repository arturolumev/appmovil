package com.example.recicla

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener{
            Registrar()
        }

        iniciarsesion.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    fun Registrar() {
        AsyncTask.execute {

            val username = txtUsername.text.toString()
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            val first_name = txtFirstName.text.toString()
            val last_name = txtLastName.text.toString()
            val address = txtAddress.text.toString()

            val queue = Volley.newRequestQueue(this)
            var url = getString(R.string.urlAPI) +"/api/usuarios/register"
            val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response
                    Toast.makeText(
                        applicationContext,
                        "Gracias por su registro",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this,CalendarEvents::class.java))
                },
                Response.ErrorListener { response ->// error
                    Toast.makeText(
                        applicationContext,
                        "No se pudo registrar",
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["username"] = username
                    params["email"] = email
                    params["password"] = password
                    params["first_name"] = first_name
                    params["last_name"] = last_name
                    params["address"] = address
                    return params
                }
            }
            queue.add(postRequest)
        }

    }
}
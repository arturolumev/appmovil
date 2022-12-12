package com.example.recicla

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        btnLogin.setOnClickListener {
            BuscarUsername()
        }

        register.setOnClickListener{
            startActivity(Intent(this,Register::class.java))
        }

        val navigation = findViewById<BottomNavigationView>(R.id.menu);
        navigation?.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.action_calendar -> {
                    startActivity(Intent(this,CalendarEvents::class.java))
                }
                R.id.action_top10->{
                    startActivity(Intent(this,TopTenAll::class.java))
                }
                R.id.action_statics->{
                    startActivity(Intent(this,Estadisticas::class.java))
                }
            }
            false
        }
    }fun BuscarUsername() {
        AsyncTask.execute {

            val username = txtUsuario.text.toString()
            val password = txtPassword.text.toString()

            val queue = Volley.newRequestQueue(this)
            var url = getString(R.string.urlAPI) +"/api/usuarios/loginandroid"
            val postRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response -> // response
                    Toast.makeText(
                        applicationContext,
                        "Bienvenido(a) $username",
                        Toast.LENGTH_LONG
                    ).show()
                    var res = JSONObject(response)
                    var intent = Intent(this,CalendarEvents::class.java)
                    intent.putExtra("user_id",res["id"].toString())
                    intent.putExtra("date_joined",res["date_joined"].toString())
                    startActivity(intent)
                    //startActivity(Intent(this,CalendarEvents::class.java))
                },
                Response.ErrorListener { response ->// error
                    Toast.makeText(
                        applicationContext,
                        "Usuario o contraseña incorrecta",
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> =
                        HashMap()
                    params["username"] = username
                    params["password"] = password
                    return params
                }
            }
            queue.add(postRequest)
        }

    }


}
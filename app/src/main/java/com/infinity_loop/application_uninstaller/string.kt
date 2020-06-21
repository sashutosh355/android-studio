package com.infinity_loop.application_uninstaller

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
class string : AppCompatActivity() {
    var strings = arrayOf("")

    private lateinit var textView: TextView
    private var requestQueue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.string)
        title = "KotlinApp"
        textView = findViewById(R.id.textViewResult)
        val button: Button = findViewById(R.id.btnParse)
        requestQueue = Volley.newRequestQueue(this)
        button.setOnClickListener {
            jsonParse()
        }
    }
    private fun jsonParse() {
        val url = "http://192.168.43.49/RewardsProject/package.json"
        val request = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                response ->try {
            var jsonArray = response.getJSONArray("package")

            for (i in 0 until jsonArray.length()) {
                val tblusereareq = jsonArray.getJSONObject(i)
//                val firstName = packages.getInt("id")
//                val age = packages.getString("package")
                val mail = tblusereareq.getString("package")
                var array: Array<String?>
                array = array.plusElement(mail)
//                textView.append("$mail\n\n")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, Response.ErrorListener { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }
}
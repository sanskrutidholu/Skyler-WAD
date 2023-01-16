package com.webandappdevelopment.skyler.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.webandappdevelopment.skyler.R
import com.webandappdevelopment.skyler.utility.CallToast
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector
import com.webandappdevelopment.skyler.utility.ShowCustomDialog
import org.json.JSONException
import org.json.JSONObject


class ForgetPasswordActivity : AppCompatActivity() {


    lateinit var btn_back : Button
    lateinit var btn_verify : Button
    lateinit var et_number : EditText

    lateinit var icd: InternetConnectionDetector
    lateinit var number : String
    val FORGET_PASSWORD_URL = "https://www.app.skyler.co.in/appservice/forget_password.php"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        findViewById<View>(R.id.hide).setOnClickListener { view: View? ->
            if (view != null) {
                this.closeKeyboard(
                    view
                )
            }
        }

        btn_back = findViewById(R.id.frgtPswd_btnBackToLogin)
        btn_verify = findViewById(R.id.frgtPswd_btnVerify)
        et_number = findViewById(R.id.frgtPswd_etMobile)

        btn_back.setOnClickListener {
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
            finish()
        }

        icd = InternetConnectionDetector(this)

        btn_verify.setOnClickListener {
            number = et_number.text.toString()
            if (number == "" || number.isEmpty()){
                CallToast(this, "Please enter all values")
            }else if (number.length > 12){
                CallToast(this, "Invalid Mobile Number")
            }else{
                sendData(number)
            }

        }
    }

    private fun sendData(number: String) {
        if (icd.isConnected){
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, FORGET_PASSWORD_URL,
                Response.Listener { response ->
                    Log.e("","onRespone"+response!!)
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.names().get(0).equals("success")){
                            CallToast(this, "Password sent on your registered number")
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else if (jsonObject.names().get(0).equals("fail") || jsonObject.names().get(0).equals("error")) {
                            CallToast(this, "Something went wrong. Please try again.")
                        } else if (jsonObject.names().get(0).equals("no")) {
                            CallToast(this, "No user found..");
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        CallToast(this, "We are unable to connect with server please try restarting app again.")
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {
                    val param = HashMap<String, String>()
                    param["mobile"] = number
                    return param
                }
            }
            val requestQueue : RequestQueue = Volley.newRequestQueue(this)
            requestQueue.cache.clear()
            requestQueue.add(stringRequest)

        }else{
            ShowCustomDialog(this)
        }
    }

    fun callToast(msg: String){
        Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
    }

    private fun closeKeyboard(view: View) {
        var view: View? = view
        view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
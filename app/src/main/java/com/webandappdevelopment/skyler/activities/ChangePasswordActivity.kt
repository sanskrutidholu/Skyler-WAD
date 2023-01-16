package com.webandappdevelopment.skyler.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.webandappdevelopment.skyler.R
import com.webandappdevelopment.skyler.utility.CallToast
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector
import com.webandappdevelopment.skyler.utility.ShowCustomDialog

class ChangePasswordActivity : AppCompatActivity() {

    lateinit var btn_back : ImageView
    lateinit var o_pswd : EditText
    lateinit var n_pswd : EditText
    lateinit var proceed : TextView

    lateinit var old_password: String
    lateinit var new_password: String

    lateinit var appId: String
    lateinit var icd : InternetConnectionDetector
    val CHANGE_PASSWORD_URL ="https://www.app.skyler.co.in/appservice/change_password.php"


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        findViewById<View>(R.id.hide).setOnClickListener { view: View? ->
            closeKeyboard(
                view!!
            )
        }

        btn_back = findViewById(R.id.back)
        o_pswd = findViewById(R.id.tv_cpaswd)
        n_pswd = findViewById(R.id.tv_npaswd)
        proceed = findViewById(R.id.tv_proceed)

        icd = InternetConnectionDetector(this)
        val sharedPreferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE)
        appId = sharedPreferences.getString("APP_ID", null).toString()

        btn_back.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }

        proceed.setOnClickListener {
            old_password = o_pswd.text.toString()
            new_password = n_pswd.text.toString()

            if (old_password.isEmpty() || new_password.isEmpty()){
                CallToast(this, "Please enter all values")
                Toast.makeText(this,"Please enter all values",Toast.LENGTH_SHORT).show()
            }else if (old_password == new_password){
                CallToast(this, "New password should npt be same as Old Password")
            }else{
                changePassword(old_password,new_password,appId)
            }
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String, appId: String) {
        if (icd.isConnected){
            val stringRequest = object : StringRequest(Method.POST, CHANGE_PASSWORD_URL,
                Response.Listener { response ->
                    Log.e("","changePassword"+response!!)

                    CallToast(this, "Password Changed Successfully..")
                    val preferences = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);//Clear Session data
                    val editor: SharedPreferences.Editor = preferences.edit()
                    editor.clear()
                    editor.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                },
                Response.ErrorListener { error ->

                }){
                override fun getParams(): MutableMap<String, String>? {
                    val param = HashMap<String, String>()
                    param["appId"] = appId
                    param["oldPswd"] = oldPassword
                    param["newPswd"] = newPassword
                    return param
                }
            }

            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.cache.clear()
            requestQueue.add(stringRequest)





        }else{
            ShowCustomDialog(this)
        }

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
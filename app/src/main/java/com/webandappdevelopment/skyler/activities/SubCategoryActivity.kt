package com.webandappdevelopment.skyler.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.webandappdevelopment.skyler.R
import com.webandappdevelopment.skyler.adapter.SubCategoryAdapter
import com.webandappdevelopment.skyler.modelclass.SubCategoryList
import com.webandappdevelopment.skyler.utility.InternetConnectionDetector
import com.webandappdevelopment.skyler.utility.ShowCustomDialog
import org.json.JSONArray
import org.json.JSONException

class SubCategoryActivity : AppCompatActivity() {

    lateinit var cName : String
    lateinit var tv_cName: TextView
    lateinit var rv_subCategory : RecyclerView

    lateinit var icd : InternetConnectionDetector;
    lateinit var subCategoryArrayList: ArrayList<SubCategoryList>
    var SUB_CATEGORY_URL = "https://www.app.skyler.co.in/appservice/fetch_subcategory.php"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            finish()
        }

        rv_subCategory = findViewById(R.id.rv_subCategory)
        tv_cName = findViewById(R.id.tv_cName)

        icd = InternetConnectionDetector(this)
        cName = intent.getStringExtra("cName")!!
        tv_cName.text = cName

        if (icd.isConnected){
            rv_subCategory.setHasFixedSize(true)
            rv_subCategory.layoutManager = LinearLayoutManager(this)
            loadSubCategory(cName)
        }else{
            ShowCustomDialog(this);
        }

    }

    private fun loadSubCategory(cName: String) {
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, SUB_CATEGORY_URL,
            Response.Listener { response ->
                Log.e("","onRespone"+response!!)
                try {

                    if (response != null){
                        val jsonArray = JSONArray(response)
                        subCategoryArrayList = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val subCategoryList = SubCategoryList()

                            subCategoryList.id = jsonObject.getInt("subcategory_id")
                            subCategoryList.subcategory_name = jsonObject.getString("subcategory_name")
                            subCategoryList.subcategory_url = jsonObject.getString("subcategory_url")
                            subCategoryList.subcategory_detail = jsonObject.getString("subcategory_detail")
                            subCategoryList.service_charge = jsonObject.getString("service_charge")

                            subCategoryArrayList.add(subCategoryList)

                        }
                        val subCategoryAdapter = SubCategoryAdapter(this, subCategoryArrayList)
                        rv_subCategory.adapter = subCategoryAdapter
                    } else {
                        Toast.makeText(this,"No Data Found",Toast.LENGTH_SHORT).show();
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                val param = HashMap<String, String>()
                param["category"] = cName
                return param
            }
        }
        val requestQueue : RequestQueue = Volley.newRequestQueue(this)
        requestQueue.cache.clear()
        requestQueue.add(stringRequest)
    }

    override fun onRestart() {
        super.onRestart()
        loadSubCategory(cName)
    }
}
package com.webandappdevelopment.skyler.activities

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.webandappdevelopment.skyler.R
import com.webandappdevelopment.skyler.utility.CallToast
import com.webandappdevelopment.skyler.utility.DBClass
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.properties.Delegates


class CategoryDetailActivity : AppCompatActivity() {

    lateinit var cDetailTitle : TextView
    lateinit var cDetailPrice: TextView
    lateinit var cDetailDescription: TextView
    lateinit var btn: Button
    lateinit var imageView : ScrollView

    lateinit var name : String
    lateinit var code : String
    lateinit var image : String
    lateinit var charge : String
    lateinit var detail : String
    lateinit var status : String
    var id by Delegates.notNull<Int>()


    lateinit var dbClass: DBClass


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_detail)

        cDetailTitle = findViewById(R.id.ctgrDetail_tvTitle)
        cDetailPrice = findViewById(R.id.ctgrDetail_tvPrice)
        cDetailDescription = findViewById(R.id.ctgrDetail_tvDetails)
        btn = findViewById(R.id.ctgrDetail_btnAddCart)
        imageView = findViewById(R.id.ctgrDetail_llBg)

        code = intent.getStringExtra("code").toString()
        name = intent.getStringExtra("scName").toString()
        charge = intent.getStringExtra("scCharge").toString()
        detail = intent.getStringExtra("scDetail").toString()
        image = intent.getStringExtra("scImage").toString()
        status = intent.getStringExtra("scStatus").toString()
        id = intent.getIntExtra("scId",0)

        if (code.equals("0")){
            btn.text = "OK"
        } else{
            if (status.equals("0")){
                btn.text = "ADD"
            }else if (status.equals("1")){
                btn.text = "REMOVE"
            }else{
                btn.text = "ADD"
            }
        }



        if (image == ""){
            imageView.setBackgroundResource(R.mipmap.ic_launcher);
        } else {
            setImagebackground(imageView,image)
        }

        cDetailTitle.text = name
        cDetailPrice.text = charge
        cDetailDescription.text = detail

        dbClass = DBClass(this)

        btn.setOnClickListener {
            if (code.equals("0")){
                finish()
            }else{
                if (status == "1"){
                    dbClass.removeCartItem(id.toInt())
                    btn.text = "Add"
                    CallToast(this, "Removed from your cart")
                } else if (status == "0"){
                    dbClass.insertIntoDatabase(id.toInt(),name,charge,charge,image,detail,"1","1")
                    btn.text = "REMOVE"
                    CallToast(this, "Added to your cart")
                }
            }
        }


    }

    fun setImagebackground(view: View, url: String?) {
        try {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val x2 = URL(url)
            val connection: HttpsURLConnection = x2.openConnection() as HttpsURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            val dr: Drawable = BitmapDrawable(myBitmap)
            view.setBackgroundDrawable(dr)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
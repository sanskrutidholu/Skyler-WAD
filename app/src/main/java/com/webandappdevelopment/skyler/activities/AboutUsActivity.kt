package com.webandappdevelopment.skyler.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.webandappdevelopment.skyler.R

class AboutUsActivity : AppCompatActivity() {

    lateinit var tv_about : TextView
    var ABOUT_CONTENT = """
        Best and affordable home services
        The Skyler app is the easiest way to book home services in Bilaspur(Chhattisgarh).
        If you are looking for a painter in Bilaspur, a beauty services or electricians or other services 'Skyler' home services is the answer.
        
        Why book any services with Skyler?
        -Trusted Professionals: All our partners are reviewed by customers like you
        -Same-day Availability: Book any services with as little as 3 hours notice
        -Live Customer Services: 7 days a week
        
        You can book any services in just few taps. Here’s how it works:
        -Pick any services
        -Go to cart and place order
        -Enter your name, contact number and address
        -We will contact you and give you services
        -You can pay after work in cash or Phonepe or UPI
        
        You can book offline services by contact us
        
        Skyler is the new startup in Bilaspur(Chhattisgarh) for home services. A new full of professional care and we’ll take care of everything else.
        
        Skyler app currently provides the following services:
        1. Appliance repair 
        2. beauty services
        3. electrician services
        4. home cleaning
        5. painting services
        6. plumber services
        7. aquarium cleaning 
        Keep your app updated- we are adding more services and features all the time!
        
        """.trimIndent()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us_activtiy)

        findViewById<View>(R.id.back).setOnClickListener { view: View? ->
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        tv_about = findViewById(R.id.tv_aboutUs)
        tv_about.text = ABOUT_CONTENT

    }
}
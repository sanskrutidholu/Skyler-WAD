package com.webandappdevelopment.skyler.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.webandappdevelopment.skyler.R
import com.webandappdevelopment.skyler.activities.CategoryDetailActivity
import com.webandappdevelopment.skyler.modelclass.SubCategoryList
import com.webandappdevelopment.skyler.utility.DBClass


class SubCategoryAdapter(
    var context: Context, var subList: ArrayList<SubCategoryList>) : RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {
    lateinit var dbClass: DBClass

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        dbClass = DBClass(context)
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            createTableOnFirstStart()
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_sub_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subList = subList[position]
        readCursorData(subList,holder)

        val name = subList.subcategory_name
        val url = subList.subcategory_url
        val detail = subList.subcategory_detail
        val charge = subList.service_charge

        holder.sc_title.text = name
        holder.sc_price.text = "Rs.$charge"

        if (url.equals("")){
            Picasso.get().load(R.drawable.skylersplash).fit().into(holder.sc_image)
        } else {
            Picasso.get().load(url).fit().into(holder.sc_image)
        }

        holder.btn_add.setOnClickListener {
            if (subList.cartStatus.equals("0")){
                subList.cartStatus = "1"
                dbClass.insertIntoDatabase(subList.id,name,charge,charge,url,detail,subList.cartStatus,"1")
                holder.btn_add.setBackgroundResource(R.drawable.signup_bg);
                holder.btn_add.text = "Remove";
                Toast.makeText(context, "Added to your cart", Toast.LENGTH_SHORT).show();
            }
            else if(subList.cartStatus.equals("1")){
                subList.cartStatus = "0"
                dbClass.removeCartItem(subList.id)
                holder.btn_add.setBackgroundResource(R.color.colorBgOrange);
                holder.btn_add.text = "Add";
                Toast.makeText(context, "Removed from your cart", Toast.LENGTH_SHORT).show();
            }
//            else{
//                subList.cartStatus = "1"
//                dbClass.insertIntoDatabase(subList.id,name,charge,url,detail,subList.cartStatus)
//                holder.btn_add.setBackgroundResource(R.drawable.signup_bg);
//                holder.btn_add.text = "Remove";
//                Toast.makeText(context, "Added to your cart", Toast.LENGTH_SHORT).show();
//            }
        }

        holder.btn_detail.setOnClickListener {
            val i = Intent(context, CategoryDetailActivity::class.java)
            i.putExtra("scName", name)
            i.putExtra("scImage",url)
            i.putExtra("scCharge",charge)
            i.putExtra("scDetail",detail)
            i.putExtra("scId",subList.id)
            i.putExtra("code", "1")
            i.putExtra("scStatus",subList.cartStatus)
            context.startActivity(i)
        }


    }

    override fun getItemCount(): Int {
        return subList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sc_image : ImageView = itemView.findViewById(R.id.scr_ivImage)
        var sc_title : TextView = itemView.findViewById(R.id.scr_tvTitle)
        var sc_price : TextView = itemView.findViewById(R.id.scr_tvPrice)
        var btn_detail : Button = itemView.findViewById(R.id.scr_btnViewDetails)
        var btn_add : Button = itemView.findViewById(R.id.scr_btnAdd)
    }

    private fun createTableOnFirstStart() {
        dbClass.insertEmpty()
        val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    @SuppressLint("Range")
    private fun readCursorData(listItem: SubCategoryList, viewHolder: ViewHolder) {
        val cursor: Cursor = dbClass.readAllData(listItem.id)
        val db = dbClass.readableDatabase
        try {
            while (cursor.moveToNext()) {
                val itemCartStatus: String =
                    cursor.getString(cursor.getColumnIndex(DBClass.CART_STATUS))
                listItem.cartStatus = itemCartStatus
                if (itemCartStatus != null && itemCartStatus == "1") {
                    viewHolder.btn_add.setBackgroundResource(R.drawable.signup_bg)
                    viewHolder.btn_add.text = "Remove"
                } else if (itemCartStatus != null && itemCartStatus == "0") {
                    viewHolder.btn_add.setBackgroundResource(R.color.colorBgOrange)
                    viewHolder.btn_add.text = "ADD"
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close()
            }
            db.close()
        }
    }

    }
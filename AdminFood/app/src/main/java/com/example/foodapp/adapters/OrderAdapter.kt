package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.ItemOrderBinding
import com.example.foodapp.model.Order
import com.example.foodapp.onclick.onClickInterface

class OrderAdapter(val onClick: onClickInterface): RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {

    private var orderList = ArrayList<Order>()

    inner class MyViewHolder( var binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvorderid.text = orderList[position].order_id.toString()
        var dateTime: String = orderList[position].order_datetime
        var modifiedString = dateTime.replace("T", " ")
        holder.binding.tvordertime.text = modifiedString
        holder.binding.tvordertotal.text = orderList[position].order_total.toString()
        holder.binding.tvordername.text = orderList[position].user_name
        holder.binding.tvorderphone.text = orderList[position].user_phone
        holder.binding.tvorderaddress.text = orderList[position].order_address


        holder.itemView.setOnClickListener {
            onClick.onClick(position)
        }
    }

    fun getOrderAt(position: Int): Order {
        return orderList[position]
    }


    fun setDataOrder(orders: ArrayList<Order>){
        this.orderList = orders
        notifyDataSetChanged()
    }
}
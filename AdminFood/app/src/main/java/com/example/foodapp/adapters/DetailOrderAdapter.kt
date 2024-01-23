package com.example.foodapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.ItemDetailBinding
import com.example.foodapp.model.Detail

class DetailOrderAdapter(): RecyclerView.Adapter<DetailOrderAdapter.MyViewHolder>(){

    private var detailList = ArrayList<Detail>()

    inner class MyViewHolder( var binding: ItemDetailBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return detailList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.txtfoodname.text = detailList[position].food_name
        val qty = "Số lượng: " + detailList[position].detail_quantity.toString()

        val price = "Giá: "+ detailList[position].food_price.toString()
        holder.binding.txtfoodprice.text = price
        holder.binding.txtfoodquantity.text = qty

        Glide.with(holder.itemView).load(detailList[position].food_image).into(holder.binding.imgfood)

    }

    fun setDataDetail(details: ArrayList<Detail>){
        this.detailList = details
        notifyDataSetChanged()
    }
}
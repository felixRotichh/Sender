package com.example.sender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderListAdapter(private val orderList: List<Order>) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orderList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val receiverNameTextView: TextView = itemView.findViewById(R.id.textViewReceiverName)
        private val parcelIdTextView: TextView = itemView.findViewById(R.id.textViewParcelId)
        private val locationTextView: TextView = itemView.findViewById(R.id.textViewLocation)

        fun bind(order: Order) {
            receiverNameTextView.text = order.receiverName
            parcelIdTextView.text = order.parcelId
            locationTextView.text = order.countryCityTown
            // Set other TextViews as needed
        }
    }
}

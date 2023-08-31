package com.example.sender

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
// OrderListAdapter.kt

class OrderListAdapter(private val orderList: List<Order>, private val listener: OnOrderClickListener)
 : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    interface OnOrderClickListener {
        fun onOrderClick(position: Int)
    }

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
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        init {
            // Set up the click listener for the delete button
            itemView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
                listener.onOrderClick(adapterPosition)
            }
        }        init {
            // Set up the click listener for the delete button
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(position)
                }
            }
        }

        fun bind(order: Order) {
            receiverNameTextView.text = order.receiverName
            parcelIdTextView.text = order.parcelId
            locationTextView.text = order.countryCityTown
        }
    }
}


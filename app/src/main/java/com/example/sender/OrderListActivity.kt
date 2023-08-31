package com.example.sender

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderListActivity : AppCompatActivity(), OrderListAdapter.OnOrderClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderListAdapter: OrderListAdapter
    private val orderList: MutableList<Order> = mutableListOf()

    private lateinit var databaseReference: DatabaseReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        orderListAdapter = OrderListAdapter(orderList, this)
        recyclerView.adapter = orderListAdapter

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return
        databaseReference = FirebaseDatabase.getInstance().getReference("orders").child(userId)

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val order = snapshot.getValue(Order::class.java)
                order?.let {
                    it.key = snapshot.key // Assign the snapshot key to the order's key property
                    orderList.add(it)
                    orderListAdapter.notifyItemInserted(orderList.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle modified data if needed
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val order = snapshot.getValue(Order::class.java)
                order?.let {
                    val orderKey = snapshot.key ?: return // Get the order key from snapshot
                    val orderIndex = getOrderIndexByKey(orderKey)
                    if (orderIndex != -1) {
                        orderList.removeAt(orderIndex)
                        orderListAdapter.notifyItemRemoved(orderIndex)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle moved data if needed
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun onOrderClick(position: Int) {
        val order = orderList[position]
        val orderKey = order.key

        if (orderKey != null) {
            // Remove the order from Firebase
            databaseReference.child(orderKey).removeValue()
        }
    }

    private fun getOrderIndexByKey(key: String): Int {
        for (i in orderList.indices) {
            if (orderList[i].key == key) {
                return i
            }
        }
        return -1
    }
}

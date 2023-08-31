package com.example.sender

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class OrderListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderListAdapter: OrderListAdapter
    private var orderList: MutableList<Order> = mutableListOf()

    // Reference to Firebase Database and "orders" node
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        orderListAdapter = OrderListAdapter(orderList)
        recyclerView.adapter = orderListAdapter

        // Initialize Firebase Database and "orders" node reference
        databaseReference = FirebaseDatabase.getInstance().getReference("orders")

        // Attach ChildEventListener to retrieve specific change events
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val order = snapshot.getValue(Order::class.java)
                order?.let {
                    orderList.add(it)
                    orderListAdapter.notifyItemInserted(orderList.size - 1) // Notify about the insertion
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle modified data if needed
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Handle removed data if needed
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle moved data if needed
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}

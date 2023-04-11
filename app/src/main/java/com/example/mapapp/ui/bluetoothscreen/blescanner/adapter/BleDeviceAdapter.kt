package com.example.mapapp.ui.bluetoothscreen.blescanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapapp.R
import com.example.mapapp.ui.bluetoothscreen.blescanner.model.BleDevice

class BleDeviceAdapter(private val devices: MutableList<BleDevice>) : RecyclerView.Adapter<BleDeviceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val deviceView = inflater.inflate(R.layout.ble_devices_adapter, parent, false)
        return ViewHolder(deviceView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = devices[position]
        val textView = holder.deviceNameTextView
        textView.text = device.name
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun setData(filteredList: List<BleDevice>) {
        devices.clear()
        devices.addAll(filteredList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceNameTextView: TextView = itemView.findViewById(R.id.device_name)
    }
}
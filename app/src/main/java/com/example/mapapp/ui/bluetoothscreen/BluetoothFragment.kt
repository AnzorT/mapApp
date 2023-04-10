package com.example.mapapp.ui.bluetoothscreen

import android.bluetooth.BluetoothManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapapp.databinding.FragmentBluetoothBinding
import com.example.mapapp.ui.bluetoothscreen.blescanner.adapter.BleDeviceAdapter
import com.example.mapapp.ui.bluetoothscreen.blescanner.model.BleDevice
import com.example.mapapp.ui.bluetoothscreen.blescanner.model.BleScanCallback

class BluetoothFragment : Fragment() {


    private lateinit var binding: FragmentBluetoothBinding
    private lateinit var btManager: BluetoothManager
    private lateinit var bleScanManager: BleScanManager
    private lateinit var foundDevices: MutableList<BleDevice>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBluetoothBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

        foundDevices = BleDevice.createBleDevicesList()
        val adapter = BleDeviceAdapter(foundDevices)
        binding.rvFoundDevices.adapter = adapter
        binding.rvFoundDevices.layoutManager = LinearLayoutManager(context)
        getSystemService(requireContext(), BluetoothManager::class.java)?.let { manager ->
            btManager = manager
        }
        bleScanManager = BleScanManager(btManager, 5000, scanCallback = BleScanCallback({
            val name = it?.device?.address
            if (name.isNullOrBlank()) return@BleScanCallback

            val device = BleDevice(name)
            if (!foundDevices.contains(device)) {
                foundDevices.add(device)
                adapter.notifyItemInserted(foundDevices.size - 1)
            }
        }))

        bleScanManager.beforeScanActions.add { binding.btnStartScan.isEnabled = false }
        bleScanManager.beforeScanActions.add {
            foundDevices.clear()
            adapter.notifyDataSetChanged()
        }
        bleScanManager.afterScanActions.add { binding.btnStartScan.isEnabled = true }

        binding.btnStartScan.setOnClickListener {
            bleScanManager.scanBleDevices()
        }

        binding.btnFilter.setOnClickListener {
            val filterText = binding.editTextFilter.text.toString()
            val filteredList = foundDevices.filter {
                it.name.contains(filterText, true)
            }
            adapter.setData(filteredList)
        }
    }
}
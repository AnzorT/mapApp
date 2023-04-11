package com.example.mapapp.ui.bluetoothscreen

import android.bluetooth.BluetoothManager
import androidx.lifecycle.ViewModel
import com.example.mapapp.ui.bluetoothscreen.blescanner.adapter.BleDeviceAdapter
import com.example.mapapp.ui.bluetoothscreen.blescanner.model.BleDevice
import com.example.mapapp.ui.bluetoothscreen.blescanner.model.BleScanCallback

class BluetoothViewModel: ViewModel() {
     val foundDevices = BleDevice.createBleDevicesList()
     lateinit var bleScanManager: BleScanManager
     lateinit var btManager: BluetoothManager

     fun getFilteredDevicesList(filterText: String): List<BleDevice> {
          return foundDevices.filter {
               it.name.contains(filterText, true)
          }
     }

     fun setBleScanManager(adapter: BleDeviceAdapter) {
          bleScanManager = BleScanManager(
               btManager, 5000, scanCallback = BleScanCallback(
                    {
                         val name = it?.device?.address
                         if (name.isNullOrBlank()) return@BleScanCallback
                         val device = BleDevice(name)
                         if (!foundDevices.contains(device)) {
                              foundDevices.add(device)
                              adapter.notifyItemInserted(foundDevices.size - 1)
                         }
                    }
               )
          )
     }
}
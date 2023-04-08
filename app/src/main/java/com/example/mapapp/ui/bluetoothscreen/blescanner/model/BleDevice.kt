package com.example.mapapp.ui.bluetoothscreen.blescanner.model

data class BleDevice(val name: String) {

    companion object {
        fun createBleDevicesList(text: String?): MutableList<BleDevice> {
            val devicesList = if(text.isNullOrBlank()) {
                mutableListOf<BleDevice>()
            } else {
                mutableListOf<BleDevice>().filter {
                    it.name.contains(text)
                } as MutableList<BleDevice>
            }
            return devicesList
        }
    }
}



package com.example.mapapp.ui.bluetoothscreen

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.os.Looper
import com.example.mapapp.ui.bluetoothscreen.blescanner.model.BleScanCallback


class BleScanManager(
    btManager: BluetoothManager,
    private val scanPeriod: Long = DEFAULT_SCAN_PERIOD,
    private val scanCallback: BleScanCallback = BleScanCallback()
) {
    private val btAdapter = btManager.adapter
    private val bleScanner = btAdapter.bluetoothLeScanner

    var beforeScanActions: MutableList<() -> Unit> = mutableListOf()
    var afterScanActions: MutableList<() -> Unit> = mutableListOf()

    private var scanning = false

    private val handler = android.os.Handler(Looper.getMainLooper())

    @SuppressLint("MissingPermission")
    fun scanBleDevices() {
        fun stopScan() {
            scanning = false
            bleScanner.stopScan(scanCallback)
            executeAfterScanActions()
        }

        if (scanning) {
            stopScan()
        } else {
            handler.postDelayed({ stopScan() }, scanPeriod)
            executeBeforeScanActions()

            scanning = true
            bleScanner.startScan(scanCallback)
        }
    }

    private fun executeBeforeScanActions() {
        executeListOfFunctions(beforeScanActions)
    }

    private fun executeAfterScanActions() {
        executeListOfFunctions(afterScanActions)
    }

    companion object {
        const val DEFAULT_SCAN_PERIOD: Long = 10000

        private fun executeListOfFunctions(toExecute: List<() -> Unit>) {
            toExecute.forEach {
                it()
            }
        }
    }
}
package com.example.mapapp.ui.bluetoothscreen

import android.bluetooth.BluetoothManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapapp.R
import com.example.mapapp.databinding.FragmentBluetoothBinding
import com.example.mapapp.isNearbyDevicesPermission
import com.example.mapapp.ui.bluetoothscreen.blescanner.adapter.BleDeviceAdapter

class BluetoothFragment : Fragment() {
    private lateinit var binding: FragmentBluetoothBinding
    private lateinit var viewModel: BluetoothViewModel

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
        viewModel = ViewModelProvider(this)[BluetoothViewModel::class.java]
        val adapter = setAdapter()
        setOnClickListeners(adapter)

        getSystemService(requireContext(), BluetoothManager::class.java)?.let { manager ->
            viewModel.btManager = manager
        }
        viewModel.setBleScanManager(adapter)
        with(viewModel.bleScanManager.beforeScanActions) {
            this.add { binding.btnStartScan.isEnabled = false }
            this.add {
                viewModel.foundDevices.clear()
                adapter.notifyDataSetChanged()
            }
            this.add { binding.btnStartScan.isEnabled = true }
        }

    }

    private fun setAdapter(): BleDeviceAdapter {
        val adapter = BleDeviceAdapter(viewModel.foundDevices)
        binding.rvFoundDevices.adapter = adapter
        binding.rvFoundDevices.layoutManager = LinearLayoutManager(context)
        return adapter
    }

    private fun setOnClickListeners(adapter: BleDeviceAdapter) {
        with(binding) {
            btnStartScan.setOnClickListener {
                if(requireActivity().isNearbyDevicesPermission()) {
                    viewModel.bleScanManager.scanBleDevices()
                } else {
                    displayMissingPermissionsAlert()
                }
            }
            btnFilter.setOnClickListener {
                editTextFilter.text.toString().let { filter ->
                    if(filter.isNotBlank()) {
                        adapter.setData(viewModel.getFilteredDevicesList(filter))
                    }
                }
            }
        }
    }

    private fun displayMissingPermissionsAlert() {
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.missing_permissions_message_title)
            .setMessage(resources.getString(R.string.missing_bluetooth_permissions_message))
            .setPositiveButton("Confirm") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

}
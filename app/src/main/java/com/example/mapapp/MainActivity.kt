package com.example.mapapp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mapapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!isLocationPermission()) {
            displayMissingPermissionsAlert()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }
    private fun setupView() {
        val navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment)
        binding.bottomNavView.setupWithNavController(navHostFragment.navController)
    }

    private fun displayMissingPermissionsAlert() {
        AlertDialog.Builder(this)
            .setTitle(R.string.missing_permissions_message_title)
            .setMessage(resources.getString(R.string.missing_permissions_message))
            .setPositiveButton("Confirm") { dialog, _ ->
                dialog.dismiss()
                finish()
            }.show()
    }
}
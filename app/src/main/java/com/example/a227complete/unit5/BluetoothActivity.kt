package com.example.a227complete.unit5

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.a227complete.R

class BluetoothActivity : AppCompatActivity() {

    private lateinit var btToggle: Button
    private lateinit var btScan: Button
    private lateinit var lstView: ListView

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val btDevice = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private var receiverRegistered = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            when (intent?.action) {

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    btDevice.clear()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@BluetoothActivity, "Scanning...", Toast.LENGTH_SHORT).show()
                }

                BluetoothDevice.ACTION_FOUND -> {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        ContextCompat.checkSelfPermission(
                            this@BluetoothActivity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        askPermissions()
                        return
                    }

                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                    val name = device?.name ?: "Unknown Device"
                    val address = device?.address ?: "N/A"

                    btDevice.add("$name\n$address")
                    adapter.notifyDataSetChanged()
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Toast.makeText(this@BluetoothActivity, "Scan Complete", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bluetooth)

        btToggle = findViewById(R.id.button11)
        btScan = findViewById(R.id.button12)
        lstView = findViewById(R.id.lstView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, btDevice)
        lstView.adapter = adapter

        btToggle.setOnClickListener {
            toggleBluetooth()
        }

        btScan.setOnClickListener {
            scanDevices()
        }
    }

    private fun toggleBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableIntent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askPermissions()
                return
            }

            try {
                bluetoothAdapter.disable()
                Toast.makeText(this, "Bluetooth turned off", Toast.LENGTH_SHORT).show()
            } catch (e: SecurityException) {
                askPermissions()
            }
        }
    }

    private fun scanDevices() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(this, "Please enable Bluetooth first", Toast.LENGTH_SHORT).show()
            return
        }

        if (!hasPermissions()) {
            askPermissions()
            return
        }

        // Check actual device location toggle (required on many phones)
        if (!isLocationEnabled()) {
            Toast.makeText(
                this,
                "Turn on Location for Bluetooth scanning",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return
        }

        btDevice.clear()
        adapter.notifyDataSetChanged()

        if (!receiverRegistered) {
            val filter = IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }
            registerReceiver(receiver, filter)
            receiverRegistered = true
        }

        try {
            bluetoothAdapter.startDiscovery()
        } catch (e: SecurityException) {
            askPermissions()
        }
    }

    private fun hasPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }

    private fun askPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                ),
                101
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }
    }

    private fun isLocationEnabled(): Boolean {
        return try {
            val mode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE)
            mode != Settings.Secure.LOCATION_MODE_OFF
        } catch (_: Exception) {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiverRegistered) unregisterReceiver(receiver)
    }
}

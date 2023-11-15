package com.example.discoorario

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var parkingTimeEditText: EditText
    private lateinit var setTimeButton: Button
    private lateinit var timeRemainingTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var countDownTimer: CountDownTimer

    private var parkingLocation: LatLng? = null
    private var endTime: Long = 0

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val TIMER_INTERVAL = 1000L // 1 second
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        parkingTimeEditText = findViewById(R.id.parkingTimeEditText)
        setTimeButton = findViewById(R.id.setTimeButton)
        timeRemainingTextView = findViewById(R.id.timeRemainingTextView)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync(this)

        sharedPreferences = getPreferences(MODE_PRIVATE)

        setTimeButton.setOnClickListener {
            setParkingTime()
        }

        requestLocationPermission()

        loadParkingData()
        startCountdownTimer()
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            updateLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation()
            } else {
                // Aggiungi qui un codice o un messaggio di gestione in caso di permesso negato
            }
        }
    }

    private fun updateLocation() {
        // Implementa la logica per ottenere la posizione attuale e assegnarla a parkingLocation
    }

    private fun setParkingTime() {
        // Implementa la logica per impostare il tempo di parcheggio e avviare il timer
    }

    private fun startCountdownTimer() {
        countDownTimer = object : CountDownTimer(endTime - System.currentTimeMillis(), TIMER_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (1000 * 60)
                val seconds = (millisUntilFinished / 1000) % 60
                timeRemainingTextView.text = getString(R.string.time_remaining_format, minutes, seconds)
            }

            override fun onFinish() {
                // Implementa la logica quando il timer è scaduto
            }
        }
        countDownTimer.start()
    }

    private fun saveParkingData() {
        val editor = sharedPreferences.edit()
        editor.putLong("endTime", endTime)
        // Salva altri dati se necessario
        editor.apply()
    }

    private fun loadParkingData() {
        endTime = sharedPreferences.getLong("endTime", 0)
        // Carica altri dati se necessario
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Altri aggiustamenti della mappa se necessario
    }
}



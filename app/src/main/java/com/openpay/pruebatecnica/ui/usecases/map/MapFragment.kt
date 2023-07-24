package com.openpay.pruebatecnica.ui.usecases.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.openpay.pruebatecnica.R
import com.openpay.pruebatecnica.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    companion object {
        val MY_TAG: String = MapFragment::class.java.simpleName
        const val COLLECTION_LOCATIONS = "locations"
    }

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private lateinit var googleMap: GoogleMap
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
//        testSaveLocation()
//        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST) {
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setupMap()
        Log.d(MY_TAG, "onMapReady")
    }

    private fun setupMap() {

        Log.d(MY_TAG, "setupMap")
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(MY_TAG, "permission granted")
            googleMap.isMyLocationEnabled = true
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            requestLocationUpdates()
        } else {
            Log.d(MY_TAG, "requestPermissionLauncher")
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
        showLocationsOnMap()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var allPermissionsGranted = true
        permissions.entries.forEach { entry ->
            val permissionName = entry.key
            val isGranted = entry.value
            if (!isGranted) {
                allPermissionsGranted = false
            }
        }
        if (allPermissionsGranted) {
            setupMap()
        } else {
            Toast.makeText(requireContext(), "You must accept all permissions to use the functionality.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        val oneMinute = 60000L
        val fiveMinutes = 300000L
        val aux = 5000L
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, fiveMinutes).apply {
            setMinUpdateDistanceMeters(0f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                val location = locationResult.lastLocation
                location?.let {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f))
                    // Guardar la ubicación y la fecha en Cloud Firestore
//                    Toast.makeText(
//                        requireContext(), "Latitud: ${location.latitude} Longitud: ${location.longitude}", Toast.LENGTH_SHORT
//                    ).show()
                    saveLocationToFirestore(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback as LocationCallback, Looper.getMainLooper())
    }

    private fun saveLocationToFirestore(location: Location) {
        val db = FirebaseFirestore.getInstance()
        val locationData = hashMapOf(
            "latitude" to location.latitude, "longitude" to location.longitude, "timestamp" to Calendar.getInstance().timeInMillis
        )

        db.collection(COLLECTION_LOCATIONS).add(locationData).addOnSuccessListener {
            // Notificar al usuario que se ha registrado la ubicación
            val title = "New location"
            val body = "Location saved : ${location.latitude}, ${location.longitude}"
            showNotification(title, body, requireContext())
        }.addOnFailureListener { e ->
            Log.w(MY_TAG, "Error al guardar la ubicación", e)
        }
    }

    private fun showLocationsOnMap() {
        db.collection(COLLECTION_LOCATIONS).addSnapshotListener { querySnapshot, e ->
            if (e != null) {
                Log.w(MY_TAG, "Error al escuchar cambios en Firestore", e)
                return@addSnapshotListener
            }
            googleMap.clear()
            for (document in querySnapshot!!) {
                val latitude = document.get("latitude")
                val longitude = document.get("longitude")
                val timeStamp = document.get("timestamp")
                var dateStr = ""
                timeStamp?.let {
                    dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(timeStamp)
                }
                if (latitude != null && longitude != null) {
                    Log.d(MY_TAG, "Latitud: $latitude Longitud: $longitude")
                    val newLatitude = if (latitude is String) {
                        latitude.toDouble()
                    } else {
                        latitude as Double
                    }
                    val newLongitude = if (longitude is String) {
                        longitude.toDouble()
                    } else {
                        longitude as Double
                    }

                    val latLng = LatLng(newLatitude, newLongitude)
//                        googleMap.addMarker(MarkerOptions().position(latLng))
                    googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                        override fun getInfoWindow(marker: Marker): View? {
                            return null // Devuelve null para utilizar el contenido personalizado en getInfoContents
                        }

                        override fun getInfoContents(marker: Marker): View {
                            val context = requireContext()
                            val view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)
                            // Obtener la fecha formateada en texto
//                                val dateInMillis = System.currentTimeMillis() // Aquí puedes usar el valor de fecha que desees
//                                val dateTimeString = convertMillisToDateTime(dateInMillis)

                            // Actualizar el contenido de la vista personalizada
                            val textViewDateTime = view.findViewById<TextView>(R.id.textViewDateTime)
                            textViewDateTime.text = dateStr

                            return view
                        }
                    })

                    val markerOptions = MarkerOptions().position(latLng)
                    googleMap.addMarker(markerOptions)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }

    private fun showNotification(title: String?, body: String?, context: Context) {
        Log.d(MY_TAG, "showNotification")
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Configurar el canal de notificación (necesario para versiones de Android >= Oreo)
        val channelId = "my_channel_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "My Channel"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "My Channel Description"
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val notificationBuilder =
            NotificationCompat.Builder(context, channelId).setSmallIcon(R.drawable.location_on_24).setContentTitle(title).setContentText(body)
                .setAutoCancel(true)

        Log.d(MY_TAG, "before notificationManager")
        // Mostrar la notificación
        notificationManager.notify(1110, notificationBuilder.build())
    }

}
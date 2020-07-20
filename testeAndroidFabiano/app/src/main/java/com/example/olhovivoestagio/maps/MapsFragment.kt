package com.example.olhovivoestagio.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.olhovivoestagio.MainActivity
import com.example.olhovivoestagio.R
import com.example.olhovivoestagio.remote.models.InfoCollected
import com.example.olhovivoestagio.remote.models.ListInfoCollected
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        setMapStyle(mMap)
        val position: LatLng = placeMarkers(googleMap)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))
        enableMyLocation()
        openMapsOnRoutes()
        observeMapUpdate()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        viewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        if (bundle != null) {
            val array = bundle.getSerializable("dataCollected") as ListInfoCollected
            viewModel.lista = array.lista
            linha = bundle.getBoolean("linha?")
            argumento = bundle.getString("argumento")!!
        }
        viewModel.getAuthCookie(resources.getString(R.string.api_key))
        viewModel.startCouting(linha, argumento, viewModel)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun observeMapUpdate() {
        viewModel.updateMap.observe(viewLifecycleOwner, Observer {
            if (it) {
                placeMarkers(mMap)
            }
        })
    }


    private fun placeMarkers(googleMap: GoogleMap): LatLng {
        googleMap.clear()
        var position = LatLng(0.0, 0.0)
        Log.e("tamanho", "" + viewModel.lista.size)
        if (viewModel.lista.size > 0) {
            for (item in viewModel.lista) {
                position = LatLng(item.lat, item.lon)

                when (item.tipo) {
                    InfoCollected.TIPO_VEICULO -> {
                        if (item.desc == "Acessível: true") {
                            googleMap.addMarker(
                                MarkerOptions().position(position).title(item.nome)
                                    .snippet("Possui acessibilidade.")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_true))
                            )
                        } else {
                            googleMap.addMarker(
                                MarkerOptions().position(position).title(item.nome)
                                    .snippet("Não possui acessibilidade")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_false))
                            )
                        }
                    }
                    InfoCollected.TIPO_PARADA -> {
                        googleMap.addMarker(
                            MarkerOptions().position(position).title(item.nome).snippet(item.desc)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stop))
                        )
                    }
                    InfoCollected.TIPO_PARADA_CORREDOR -> {
                        googleMap.addMarker(
                            MarkerOptions().position(position).title(item.nome).snippet(item.desc)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_runner))
                        )
                    }
                }
            }
        }
        viewModel.doneUpdateMap()
        Toast.makeText(context, "Posições atualizadas", Toast.LENGTH_SHORT).show()
        return position
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireNotNull(context),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            mMap.setMyLocationEnabled(true)

        } else {
            ActivityCompat.requestPermissions(
                requireNotNull(activity),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_GRANTED
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION_GRANTED) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        mMap.isTrafficEnabled = true
        mMap.isBuildingsEnabled = false
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireNotNull(context),
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }


    private fun openMapsOnRoutes() {

        mMap.setOnMapLongClickListener { latLng ->
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${latLng.latitude},${latLng.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

    }


    private lateinit var mMap: GoogleMap
    private val TAG = MainActivity::class.java.simpleName
    private val REQUEST_LOCATION_PERMISSION_GRANTED = 1
    private lateinit var viewModel: MapsViewModel
    private var linha: Boolean = false
    private var argumento: String = ""


}
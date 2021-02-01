package app.kiran.devkrushna_practical_demo.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import app.kiran.devkrushna_practical_demo.R
import app.kiran.devkrushna_practical_demo.databinding.ActivityMainBinding
import app.kiran.devkrushna_practical_demo.utils.Constant
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.GeoApiContext
import com.google.maps.PlacesApi
import com.google.maps.model.PlaceType
import com.google.maps.model.PlacesSearchResponse
import com.google.maps.model.RankBy
import java.io.IOException
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback, Toolbar.OnMenuItemClickListener {

    private val mTAG = MapActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding

    //    private lateinit var placesClient: PlacesClient
    private lateinit var geoCoder: Geocoder
    private lateinit var addresses: List<Address>

    private var lastTimeClicked: Long = 0


    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@MapActivity)

        initUi()
    }

    private fun initUi() {

        fetchLocation()
        binding.toolBar.setOnMenuItemClickListener(this)
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Log.d(
                    mTAG,
                    "CURRENT_LOCATION -> ${currentLocation.latitude},${currentLocation.longitude}"
                )
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this@MapActivity)
            }
            geoCoder = Geocoder(this, Locale.getDefault())

            addresses =
                geoCoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);

            binding.toolBar.title = "${addresses[0].subLocality},${addresses[0].locality}"
            run()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("You are here!")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        googleMap?.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    fetchLocation()
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.list -> {
                if (SystemClock.elapsedRealtime() - lastTimeClicked < 500) {
                    return false
                }
                lastTimeClicked = SystemClock.elapsedRealtime()
                startActivity(Intent(this@MapActivity, NearByPlacesActivity::class.java))
                return true
            }
        }
        return false
    }


    private fun run(): PlacesSearchResponse {
        var request: PlacesSearchResponse = PlacesSearchResponse()
        val context = GeoApiContext.Builder()
            .apiKey(Constant.API_KEY)
            .build()
        val location =
            com.google.maps.model.LatLng(currentLocation.latitude, currentLocation.longitude)
        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                .radius(5000)
                .rankby(RankBy.PROMINENCE)
                .keyword("cruise")
                .language("en")
                .type(PlaceType.RESTAURANT)
                .await()
        } catch (e: ApiException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            return request
        }
    }
}
package app.kiran.devkrushna_practical_demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.kiran.devkrushna_practical_demo.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mTAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize places
        Places.initialize(this, getString(R.string.api_key))

        //Create new places client instance
        placesClient = Places.createClient(this)
        initUi()
    }

    private fun initUi() {

        setCurrentLocation()
    }

    private fun setCurrentLocation() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap?) {
        mMap = gMap!!
        if (mMap!=null){

        }
    }
}
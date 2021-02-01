package app.kiran.devkrushna_practical_demo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.kiran.devkrushna_practical_demo.R
import app.kiran.devkrushna_practical_demo.databinding.ActivityNearByPlacesBinding

class NearByPlacesActivity : AppCompatActivity() {

    private val mTAG = NearByPlacesActivity::class.java.simpleName
    private lateinit var binding: ActivityNearByPlacesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearByPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        binding.toolBar.title = getString(R.string.title_near_by_place)
    }
}
package com.example.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding.containerView = supportFragmentManager as FragmentContainerView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
//binding.containerView.findNavController()
      //  val bottomNavBar = findViewById<BottomNavigationView>(id.bottomNavigationView)
     //   bottomNavBar.setupWithNavController(navController)
        //  binding.bottomNavigationView.setupWithNavController(binding.containerView.findNavController())



        binding.bottomNavigationView.setupWithNavController(navController)
    }

}
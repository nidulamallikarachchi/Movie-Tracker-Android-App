package com.example.movietracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.movietracker.fragments.DisplayUserProfileFragment
import com.example.movietracker.fragments.HomeFragment
import com.example.movietracker.fragments.WatchListFragment
import com.example.movietracker.fragments.WatchedListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Load the HomeFragment by default
        loadFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_watched -> {
                    loadFragment(WatchedListFragment()) // Create this fragment
                    true
                }
                R.id.nav_watchlist -> {
                    loadFragment(WatchListFragment()) // Create this fragment
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(DisplayUserProfileFragment()) // Create this fragment
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // Replace the current fragment with the new one
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment) // Use your container ID
        transaction.commit()
    }
}

package com.example.devtreetest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.devtreetest.R
import com.example.devtreetest.databinding.ActivityHomeBinding
import com.example.devtreetest.ui.viewLocation.ViewLocationFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.home_container, ViewLocationFragment.getInstance())
            .commitAllowingStateLoss()

    }
}
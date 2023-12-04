package com.syaiful.pengaduan.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.syaiful.pengaduan.data.database.PrefsManager
import com.syaiful.pengaduan.databinding.ActivityMainBinding
import com.syaiful.pengaduan.ui.fragment.UserActivity
import com.syaiful.pengaduan.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var prefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        prefsManager = PrefsManager(this)
        main()
    }

    fun main() {
//        if (prefsManager.prefIsLogin) {
            Handler().postDelayed({
                startActivity(Intent(this, UserActivity::class.java))
                finish()
            }, 2000)
//        } else {
//            Handler().postDelayed({
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            }, 2000)
//        }
    }
}

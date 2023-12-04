package com.syaiful.pengaduan.ui.daftar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.syaiful.pengaduan.databinding.ActivityDaftarBinding
import com.syaiful.pengaduan.ui.login.LoginActivity

class DaftarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi objek binding
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activity()
    }

    private fun activity() {
        binding.btnLanjut.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}

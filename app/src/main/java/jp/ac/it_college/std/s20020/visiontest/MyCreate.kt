package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMyCreateBinding

class MyCreate : AppCompatActivity() {
    private lateinit var binding: ActivityMyCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.okBtn.setOnClickListener{
            intent = Intent(this, File_Foleder_Input::class.java)
            startActivity(intent)
        }


    }
}
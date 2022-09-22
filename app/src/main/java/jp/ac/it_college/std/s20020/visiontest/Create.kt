package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityCreateBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMainBinding

class Create : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener{
            finish()
        }

        binding.mycreateBtn.setOnClickListener{
            val intent = Intent(this, MyCreate::class.java)
            startActivity(intent)
        }


    }
}
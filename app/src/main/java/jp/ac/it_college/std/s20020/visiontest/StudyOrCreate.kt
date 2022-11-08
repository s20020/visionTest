package jp.ac.it_college.std.s20020.visiontest

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMainBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityStudyOrCreateBinding

class StudyOrCreate : AppCompatActivity() {
    private lateinit var binding: ActivityStudyOrCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyOrCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createBtn.setOnClickListener{
            val intent = Intent(this, Create::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.studyBtn.setOnClickListener{
            val intent = Intent(this, FolderSelect::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


    }


}
package jp.ac.it_college.std.s20020.visiontest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityFileFolederInputBinding

class File_Foleder_Input : AppCompatActivity() {

    private lateinit var binding: ActivityFileFolederInputBinding

    //現在作られているフォルダーの選択肢をDBから取得

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileFolederInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
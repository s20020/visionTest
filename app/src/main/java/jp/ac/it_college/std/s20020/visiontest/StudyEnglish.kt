package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityStudyEnglishBinding

class StudyEnglish : AppCompatActivity() {

    private lateinit var binding : ActivityStudyEnglishBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyEnglishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var list_number = intent.getIntExtra("LIST_NUMBER",0)
        val en_list = intent.getStringArrayListExtra("ENLIST")
        val ja_list = intent.getStringArrayListExtra("JALIST")

        //最初の画面のときはleftボタンを押せなくする
        if(list_number == 0){
            binding.leftBtn.isEnabled = false
        }

        //最後のだとrightボタンを押せなくする
        if(list_number == 39){
            binding.rightBtn.isEnabled = false
        }


        println(en_list)
        println(ja_list)
        println(en_list?.get(0))
        println(ja_list?.get(0))
        println(en_list?.get(10))
        println(ja_list?.get(10))

        //２つでセットのため÷２している
        binding.studyEnText.text = en_list?.get(list_number/2)

        binding.endBtn.setOnClickListener{
            val intent = Intent(this, StudyOrCreate::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        binding.rightBtn.setOnClickListener{
            val intent = Intent(this, StudyJapanese::class.java)
            intent.putStringArrayListExtra("ENLIST", en_list)
            intent.putStringArrayListExtra("JALIST", ja_list)
            list_number += 1
            intent.putExtra("LIST_NUMBER", list_number)
            println(list_number)
            startActivity(intent)
        }

        binding.leftBtn.setOnClickListener {
            finish()
        }


    }
}
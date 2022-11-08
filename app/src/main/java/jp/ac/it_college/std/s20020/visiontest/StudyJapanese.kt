package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityStudyJapaneseBinding

class StudyJapanese : AppCompatActivity() {
    private lateinit var binding : ActivityStudyJapaneseBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyJapaneseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var list_number = intent.getIntExtra("LIST_NUMBER",0)


        val en_list = intent.getStringArrayListExtra("ENLIST")
        val ja_list = intent.getStringArrayListExtra("JALIST")

        binding.endBtn2.setOnClickListener{
            val intent = Intent(this, StudyOrCreate::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

        //最初の画面のときはleftボタンを押せなくする
        if(list_number == 0){
            binding.leftBtn2.isEnabled = false
        }

        //最後のだとrightボタンを押せなくする
        if(list_number == 39){
            binding.rightBtn2.isEnabled = false
        }

        println(en_list)
        println(ja_list)
        println(en_list?.get(0))
        println(ja_list?.get(0))
        println(en_list?.get(10))
        println(ja_list?.get(10))

        binding.studyJaText.text = ja_list?.get(list_number/2)


        binding.rightBtn2.setOnClickListener{
            val intent = Intent(this, StudyEnglish::class.java)
            intent.putStringArrayListExtra("ENLIST", en_list)
            intent.putStringArrayListExtra("JALIST", ja_list)
            list_number += 1
            intent.putExtra("LIST_NUMBER", list_number)
            println(list_number)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.leftBtn2.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


    }
}
package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMyCreateBinding

class MyCreate : AppCompatActivity() {
    private lateinit var binding: ActivityMyCreateBinding

    val s_list = listOf<String>()

    var all_english = ""
    var all_japanese = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backBtn.setOnClickListener{
            finish()
        }



        binding.okBtn.setOnClickListener{

            //英単語をカンマ区切りの文字列にして格納
            val list = arrayListOf<String>()
            list.add(binding.edit11.text.toString())
            list.add(binding.edit21.text.toString())
            list.add(binding.edit31.text.toString())
            list.add(binding.edit41.text.toString())
            list.add(binding.edit51.text.toString())
            list.add(binding.edit61.text.toString())
            list.add(binding.edit71.text.toString())
            list.add(binding.edit81.text.toString())
            list.add(binding.edit91.text.toString())
            list.add(binding.edit101.text.toString())
            list.add(binding.edit111.text.toString())
            list.add(binding.edit121.text.toString())
            list.add(binding.edit131.text.toString())
            list.add(binding.edit141.text.toString())
            list.add(binding.edit151.text.toString())
            list.add(binding.edit161.text.toString())
            list.add(binding.edit171.text.toString())
            list.add(binding.edit181.text.toString())
            list.add(binding.edit191.text.toString())
            list.add(binding.edit201.text.toString())

            all_english = list.joinToString(separator = ",")


            //日本語をカンマ区切りの文字列にして格納
            val list2 = arrayListOf<String>()
            list2.add(binding.edit12.text.toString())
            list2.add(binding.edit22.text.toString())
            list2.add(binding.edit32.text.toString())
            list2.add(binding.edit42.text.toString())
            list2.add(binding.edit52.text.toString())
            list2.add(binding.edit62.text.toString())
            list2.add(binding.edit72.text.toString())
            list2.add(binding.edit82.text.toString())
            list2.add(binding.edit92.text.toString())
            list2.add(binding.edit102.text.toString())
            list2.add(binding.edit112.text.toString())
            list2.add(binding.edit122.text.toString())
            list2.add(binding.edit132.text.toString())
            list2.add(binding.edit142.text.toString())
            list2.add(binding.edit152.text.toString())
            list2.add(binding.edit162.text.toString())
            list2.add(binding.edit172.text.toString())
            list2.add(binding.edit182.text.toString())
            list2.add(binding.edit192.text.toString())
            list2.add(binding.edit202.text.toString())

            all_japanese = list2.joinToString(separator = ",")
            


            intent = Intent(this, File_Foleder_Input::class.java)
            intent.putExtra("ES", all_english)
            intent.putExtra("JA", all_japanese)

            startActivity(intent)
        }


    }
}
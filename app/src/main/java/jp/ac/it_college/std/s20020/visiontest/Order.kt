package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityOrderBinding

class Order : AppCompatActivity() {
    private lateinit var binding : ActivityOrderBinding

    private lateinit var _helper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val _id = intent.getIntExtra("ID",0)

        var englishes = ""
        var japaneses = ""

        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(applicationContext)


        val db = _helper.writableDatabase

        val select = """
            SELECT english_word, japanese_word FROM main
            WHERE _id = '${_id}'
        """.trimIndent()

        val c = db.rawQuery(select, null)


        while(c.moveToNext()){
            val cEn = c.getColumnIndex("english_word")
            englishes = c.getString(cEn)
            val cJa = c.getColumnIndex("japanese_word")
            japaneses = c.getString(cJa)
        }

        println(englishes)
        println(japaneses)

        //カンマ区切りの文字列をリストに変換
        val en_list = englishes.split(",").toList()
        val ja_list = japaneses.split(",").toList()

        println(en_list)
        println(ja_list)
        println(en_list[0])
        println(ja_list[0])





        binding.backBtn4.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.enJaBtn.setOnClickListener{
            val intent = Intent(this, StudyEnglish::class.java)
            intent.putStringArrayListExtra("ENLIST", ArrayList<String>(en_list))
            intent.putStringArrayListExtra("JALIST", ArrayList<String>(ja_list))
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.jaEnBtn.setOnClickListener{
            val intent = Intent(this, StudyJapanese::class.java)
            intent.putStringArrayListExtra("ENLIST", ArrayList<String>(en_list))
            intent.putStringArrayListExtra("JALIST", ArrayList<String>(ja_list))
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }





    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }
}
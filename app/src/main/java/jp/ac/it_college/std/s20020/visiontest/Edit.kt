package jp.ac.it_college.std.s20020.visiontest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityEditBinding

class Edit : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    private lateinit var _helper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var englishes = ""
        var japaneses = ""

        val _id = intent.getIntExtra("ID",0)

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


    }
}
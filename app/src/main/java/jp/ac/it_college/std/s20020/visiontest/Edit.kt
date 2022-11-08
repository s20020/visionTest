package jp.ac.it_college.std.s20020.visiontest

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityEditBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMyCreateBinding

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

        binding.backBtn.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

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

        binding.edit11.setText(en_list[0])
        binding.edit21.setText(en_list[1])
        binding.edit31.setText(en_list[2])
        binding.edit41.setText(en_list[3])
        binding.edit51.setText(en_list[4])
        binding.edit61.setText(en_list[5])
        binding.edit71.setText(en_list[6])
        binding.edit81.setText(en_list[7])
        binding.edit91.setText(en_list[8])
        binding.edit101.setText(en_list[9])
        binding.edit111.setText(en_list[10])
        binding.edit121.setText(en_list[11])
        binding.edit131.setText(en_list[12])
        binding.edit141.setText(en_list[13])
        binding.edit151.setText(en_list[14])
        binding.edit161.setText(en_list[15])
        binding.edit171.setText(en_list[16])
        binding.edit181.setText(en_list[17])
        binding.edit191.setText(en_list[18])
        binding.edit201.setText(en_list[19])

        binding.edit12.setText(ja_list[0])
        binding.edit22.setText(ja_list[1])
        binding.edit32.setText(ja_list[2])
        binding.edit42.setText(ja_list[3])
        binding.edit52.setText(ja_list[4])
        binding.edit62.setText(ja_list[5])
        binding.edit72.setText(ja_list[6])
        binding.edit82.setText(ja_list[7])
        binding.edit92.setText(ja_list[8])
        binding.edit102.setText(ja_list[9])
        binding.edit112.setText(ja_list[10])
        binding.edit122.setText(ja_list[11])
        binding.edit132.setText(ja_list[12])
        binding.edit142.setText(ja_list[13])
        binding.edit152.setText(ja_list[14])
        binding.edit162.setText(ja_list[15])
        binding.edit172.setText(ja_list[16])
        binding.edit182.setText(ja_list[17])
        binding.edit192.setText(ja_list[18])
        binding.edit202.setText(ja_list[19])





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

            val all_english = list.joinToString(separator = ",")


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

            val all_japanese = list2.joinToString(separator = ",")


            val values = ContentValues()
            values.put("english_word", all_english)
            values.put("japanese_word", all_japanese)


            db.update("main", values, "_id = ?", arrayOf(_id.toString()))

        }

    }
    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }
}
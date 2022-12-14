package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.deepl.api.Translator
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMyCreateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates



class MyCreate : AppCompatActivity() {
    private lateinit var binding: ActivityMyCreateBinding


    var all_english = ""
    var all_japanese = ""

    val API_KEY = "afe96a24-d9a2-4337-4f8a-bb6582987f21:fx"



    //すべての要素がNULLのりすとをつくる
    var ja_list = arrayListOf<String>("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
    var en_list = arrayListOf<String>("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")


    var ID by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //撮ってきた値をリストとして保管する


        ID = intent.getIntExtra("ID",0)

        //写真ORフォルダからとってきたものだったら、PutTextを実行する
        if(ID == 1) {
            PutText()
            ja_list.clear()
            en_list.clear()
        }


        binding.backBtn.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        //翻訳ボタン
        binding.transBtn.setOnClickListener{

            val a = binding.edit11.text.toString()
            println(a)
            //今かかれているものを取得
            val list = ListAddEn()
            println(list)
            val list2 = arrayListOf<String>()
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val translator = Translator(API_KEY)
                    var i = 0
                    while(i < 20) {
                        if(list[i] != ""){
                            val result = translator.translateText(list[i], null, "ja")
                            list2.add(result.text)
                        }else {
                            val result = ""
                            list2.add(result)
                        }
                        i++
                    }
                }
                println(list2)
                JaSetText(list2)
            }


        }



        binding.okBtn.setOnClickListener{

            //英単語をカンマ区切りの文字列にして格納
            val list = ListAddEn()

            all_english = list.joinToString(separator = ",")


            //日本語をカンマ区切りの文字列にして格納
            val list2 = ListAddJa()
            all_japanese = list2.joinToString(separator = ",")

            intent = Intent(this, File_Foleder_Input::class.java)
            intent.putExtra("ES", all_english)
            intent.putExtra("JA", all_japanese)

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }


    //MyCreateに撮ってきた値をおく。
    fun PutText() {

//        val jlist = intent.getStringArrayListExtra("JA")
        val elist = intent.getStringArrayListExtra("EN")



//        var a = 0
//        try{
//            while(a < jlist?.size!!){
//                ja_list[a] = jlist[a]
//                a++
//            }
//        }catch (e: Exception){
//            Log.e("getAddres","GetAddressErr")
//        }

        try{
            var b = 0
            while(b < elist?.size!!) {
                en_list[b] = elist[b]
                b++
            }
        }catch (e: Exception){
            Log.e("getAddres","GetAddressErr")
        }


        //英語
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



        //日本語
//        binding.edit12.setText(ja_list[0])
//        binding.edit22.setText(ja_list[1])
//        binding.edit32.setText(ja_list[2])
//        binding.edit42.setText(ja_list[3])
//        binding.edit52.setText(ja_list[4])
//        binding.edit62.setText(ja_list[5])
//        binding.edit72.setText(ja_list[6])
//        binding.edit82.setText(ja_list[7])
//        binding.edit92.setText(ja_list[8])
//        binding.edit102.setText(ja_list[9])
//        binding.edit112.setText(ja_list[10])
//        binding.edit122.setText(ja_list[11])
//        binding.edit132.setText(ja_list[12])
//        binding.edit142.setText(ja_list[13])
//        binding.edit152.setText(ja_list[14])
//        binding.edit162.setText(ja_list[15])
//        binding.edit172.setText(ja_list[16])
//        binding.edit182.setText(ja_list[17])
//        binding.edit192.setText(ja_list[18])
//        binding.edit202.setText(ja_list[19])
    }

    fun ListAddEn() : ArrayList<String>{
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
        return list
    }

    fun ListAddJa() : ArrayList<String> {
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

        return list2
    }

    fun EnSetText(en_list: ArrayList<String>) {
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




    }

    fun JaSetText(ja_list: ArrayList<String>){

        //日本語
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
    }
}
package jp.ac.it_college.std.s20020.visiontest

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityStudyEnglishBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.FragmentEnTextBinding
import java.util.*

class StudyEnglish : AppCompatActivity(), TextToSpeech.OnInitListener  {

    private var savedInstanceState: Bundle? = null
    private var textToSpeech: TextToSpeech? = null

    private lateinit var binding : ActivityStudyEnglishBinding


    var list_number = 0
    var speech_text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        super.onCreate(savedInstanceState)
        binding = ActivityStudyEnglishBinding.inflate(layoutInflater)

        setContentView(binding.root)


        list_number = intent.getIntExtra("LIST_NUMBER",0)
        val en_list = intent.getStringArrayListExtra("ENLIST")
        val ja_list = intent.getStringArrayListExtra("JALIST")
        val which = intent.getStringExtra("A")



        // TTS インスタンス生成
        textToSpeech = TextToSpeech(this, this)


        //Fragmenのインスタンスを生成
        var enText = EnText()
        var jaText = JaText()
        var speech = Speech()
        var nothing = Nothing()


        println(speech_text)

        //
        Com()
        if(which == "English") {
            val args = Bundle()
            args.putString("key", en_list?.get(list_number/2))
            //この時点でspeech_textにいれる
            speech_text = en_list?.get(list_number/2)!!
            enText.arguments = args

            replaceFragment(enText, speech)


//            buttonFragment(speech)
        }else {
            val args = Bundle()
            args.putString("key", ja_list?.get(list_number/2))
            jaText.arguments = args
            replaceFragment(jaText, nothing)

//            buttonFragment(nothing)
        }

        //最初の画面のときはleftボタンを押せなくする
//        if(list_number == 0){
//            binding.leftBtn.isEnabled = false
//        }

        //最後のだとrightボタンを押せなくする
//        if(list_number == 39){
//            binding.rightBtn.isEnabled = false
//        }


        println(en_list)
        println(ja_list)





        binding.endBtn.setOnClickListener{
            val intent = Intent(this, FolderSelect::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        //１つ次へ
        binding.rightBtn.setOnClickListener{
            //Fragmenのインスタンスを生成
            val enText = EnText()
            val jaText = JaText()
            val speech = Speech()
            val nothing = Nothing()

            list_number += 1
            println(list_number)
            Com()
            val fragment = supportFragmentManager.fragments
            println(fragment)

            var isJa = false
            for (f in fragment) {
                if (f is JaText) {
                    isJa = true
                }
            }

            if(isJa) {

                val args = Bundle()
                args.putString("key", en_list?.get(list_number/2))
                //この時点でspeech_textにいれる
                speech_text = en_list?.get(list_number/2)!!
                enText.arguments = args

                replaceFragment(enText, speech)
//                buttonFragment(speech)

            }else {
                val args = Bundle()
                args.putString("key", ja_list?.get(list_number/2))
                jaText.arguments = args

                replaceFragment(jaText, nothing)

            }


        }

        //一つ前へ
        binding.leftBtn.setOnClickListener {

            list_number -= 1
            println(list_number)
            Com()

            val fragment = supportFragmentManager.fragments
println(fragment)


            if(jaText in fragment) {
                back(jaText)
            }else {
                back(enText)
                speech_text = en_list?.get(list_number/2)!!

            }

        }


    }



    //フラグメントを入れ替えるための操作・・TEXT
    fun replaceFragment(fragment: Fragment, fragment1: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            //遷移するとき
            jp.ac.it_college.std.s20020.visiontest.R.anim.slide_in_right, jp.ac.it_college.std.s20020.visiontest.R.anim.slide_out_left,
            //戻るときpopBackStack()
            R.anim.slide_in_left, R.anim.slide_out_right
        )
        fragmentTransaction.replace(binding.container.id, fragment)
        fragmentTransaction.replace(binding.container1.id, fragment1)
        //画面を保存しておく
        fragmentTransaction.addToBackStack(list_number.toString())
        fragmentTransaction.commit()

    }



    fun back(fragment: Fragment) {
        supportFragmentManager.popBackStack()
    }



    private fun Com() {
        binding.rightBtn.isEnabled = list_number != 39
        binding.leftBtn.isEnabled = list_number != 0
    }





    override fun onInit(status: Int) {
         // TTS初期化
        if (TextToSpeech.SUCCESS == status) {
            Log.d("debug", "initialized")
        } else {
            Log.e("debug", "failed to initialize")
        }
    }

    // 読み上げのスピード
    private fun setSpeechRate() {
        textToSpeech?.setSpeechRate(0.8.toFloat())
    }

    // 読み上げのピッチ
    private fun setSpeechPitch() {
        textToSpeech?.setPitch(1.0.toFloat())
    }

    fun speechText() {
        val editor = speech_text
        if(editor == "") println("hello")
        println("taiga")
        println(editor)
        // EditTextからテキストを取得
        val string = editor

        if (0 < string.length) {
            if (textToSpeech!!.isSpeaking()) {
                textToSpeech!!.stop()
                return
            }

            textToSpeech!!.setLanguage(Locale.ENGLISH)


            setSpeechRate()
            setSpeechPitch()
            textToSpeech!!.speak(string, TextToSpeech.QUEUE_FLUSH, null, "messageID")
            setTtsListener()
        }
    }

    // 読み上げの始まりと終わりを取得
    private fun setTtsListener() {
        val listenerResult: Int =
            textToSpeech!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onDone(utteranceId: String) {
                    Log.d("debug", "progress on Done $utteranceId")
                }

                override fun onError(utteranceId: String) {
                    Log.d("debug", "progress on Error $utteranceId")
                }

                override fun onStart(utteranceId: String) {
                    Log.d("debug", "progress on Start $utteranceId")
                }
            })
        if (listenerResult != TextToSpeech.SUCCESS) {
            Log.e("debug", "failed to add utterance progress listener")
        }
    }

    override fun onDestroy() {
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}





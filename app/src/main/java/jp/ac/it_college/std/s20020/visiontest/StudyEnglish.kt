package jp.ac.it_college.std.s20020.visiontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityStudyEnglishBinding

class StudyEnglish : AppCompatActivity(), TextToSpeech.OnInitListener  {

    private var savedInstanceState: Bundle? = null
    private var textToSpeech: TextToSpeech? = null

    private lateinit var binding : ActivityStudyEnglishBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        super.onCreate(savedInstanceState)
        binding = ActivityStudyEnglishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var list_number = intent.getIntExtra("LIST_NUMBER",0)
        val en_list = intent.getStringArrayListExtra("ENLIST")
        val ja_list = intent.getStringArrayListExtra("JALIST")

        // TTS インスタンス生成
        textToSpeech = TextToSpeech(this, this)
        binding.speekerBtn.setOnClickListener{ speechText() }

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
        textToSpeech?.setSpeechRate(1.0.toFloat())
    }

    // 読み上げのピッチ
    private fun setSpeechPitch() {
        textToSpeech?.setPitch(1.0.toFloat())
    }

    private fun speechText() {
        val editor = binding.studyEnText
        // EditTextからテキストを取得
        val string = editor.text.toString()

        if (0 < string.length) {
            if (textToSpeech!!.isSpeaking()) {
                textToSpeech!!.stop()
                return
            }
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
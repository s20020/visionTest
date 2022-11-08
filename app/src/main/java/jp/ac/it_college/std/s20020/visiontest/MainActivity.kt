package jp.ac.it_college.std.s20020.visiontest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.*
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var functions: FirebaseFunctions
    //認証　FirebaseAuth インスタンスを宣言します。
    private lateinit var auth: FirebaseAuth

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener{
            val intent = Intent(this, StudyOrCreate::class.java)
            startActivity(intent)
        }

        // 認証　onCreate() メソッドで、FirebaseAuth インスタンスを初期化します。
        auth = Firebase.auth
        functions = Firebase.functions

//        binding.button.apply {
//            isEnabled = false
//            setOnClickListener{ buttonClicked(it) }
        }
//    }
    //現在の認証を確認する
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        //現在のユーザーがいないときｓ２００２０アカウントでログインする
        if (currentUser == null) {
            auth.signInWithEmailAndPassword("s20020@std.it-college.ac.jp", "taiga6666")
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "signInWithEmail: success")
                    } else {
                        Log.d(ContentValues.TAG, "signInWithEmail: login failure")
                    }
                }
        }
        binding.button.isEnabled = true
    }

    private fun buttonClicked(v: View) {
        //認識する画像を入れ込む
        var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.my_photo_11)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

        val request = JsonObject()
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64encoded))
        request.add("image", image)
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("TEXT_DETECTION"))
        val features = JsonArray()
        features.add(feature)
        request.add("features", features)
        val imageContext = JsonObject()
        val languageHints = JsonArray()
        languageHints.add("en")
        languageHints.add("ja")
        imageContext.add("languageHints", languageHints)
        request.add("imageContext", imageContext)

        annotateImage(request.toString())
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("s", "sippai")
                    return@addOnCompleteListener
                } else {
                    val annotation =
                        task.result!!.asJsonArray[0].asJsonObject["fullTextAnnotation"].asJsonObject
                    binding.resultText.text = annotation["text"].asString

                    for (page in annotation["pages"].asJsonArray) {
                        var pageText = ""
                        for (block in page.asJsonObject["blocks"].asJsonArray) {
                            var blockText = ""
                            for (para in block.asJsonObject["paragraphs"].asJsonArray) {
                                var paraText = ""
                                for (word in para.asJsonObject["words"].asJsonArray) {
                                    var wordText = ""
                                    for (symbol in word.asJsonObject["symbols"].asJsonArray) {
                                        wordText += symbol.asJsonObject["text"].asString
                                        System.out.format(
                                            "Symbol text: %s (confidence: %f)%n",
                                            symbol.asJsonObject["text"].asString,
                                            symbol.asJsonObject["confidence"].asFloat
                                        )
                                    }
                                    System.out.format(
                                        "Word text: %s (confidence: %f)%n%n", wordText,
                                        word.asJsonObject["confidence"].asFloat
                                    )
                                    System.out.format(
                                        "Word bounding box: %s%n",
                                        word.asJsonObject["boundingBox"]
                                    )
                                    paraText = String.format("%s%s ", paraText, wordText)
                                }
                                System.out.format("%nParagraph: %n%s%n", paraText)
                                System.out.format(
                                    "Paragraph bounding box: %s%n",
                                    para.asJsonObject["boundingBox"]
                                )
                                System.out.format(
                                    "Paragraph Confidence: %f%n",
                                    para.asJsonObject["confidence"].asFloat
                                )
                                blockText += paraText
                            }
                            pageText += blockText

                            Log.d("Functions", pageText)
                        }
                    }
                }
            }

    }

    private fun annotateImage(requestJson: String): Task<JsonElement> {
        return functions
            .getHttpsCallable("annotateImage")
            .call(requestJson)
            .continueWith { task ->
                val result = task.result?.data
                JsonParser.parseString(Gson().toJson(result))
            }
    }

}
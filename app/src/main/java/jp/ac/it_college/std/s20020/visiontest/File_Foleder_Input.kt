package jp.ac.it_college.std.s20020.visiontest

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityFileFolederInputBinding
import java.util.*

class File_Foleder_Input : AppCompatActivity() {

    private lateinit var binding: ActivityFileFolederInputBinding

    private lateinit var _helper: DatabaseHelper

    val spinnerItems = arrayListOf<String>()


    //現在作られているフォルダーの選択肢をDBから取得


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileFolederInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(applicationContext)


        //DB挿入命令
        val insert = """
            INSERT INTO folder
            (folder_number, folder_name)
            VALUES(?, ?)
        """.trimIndent()

        //DBからfolder_nameを取り出す
        val select = """
            SELECT folder_name FROM folder
        """.trimIndent()


        val db = _helper.writableDatabase
        val c = db.rawQuery(select, null)

        while(c.moveToNext()){
            val data = c.getColumnIndex("folder_name")
            spinnerItems.add(c.getString(data))
        }

        println(spinnerItems)
        println("taiga")

        //スピナーにフォルダ名を表示するための工程

        //アダプター作成
        val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item, spinnerItems)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //spinerViewにアダプターをセット
        binding.spinner.adapter = adapter






        //フォルダー作成ボタンを押すと
        //ダイアログが出力される
        binding.folderAddBtn.setOnClickListener{
            val myedit = EditText(this)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("文字を入力してください")
            dialog.setView(myedit)
            dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                // OKボタン押したときの処理
                //folderDBにインサートする

                //folder_numberの連番のための保存領域インスタンスを生成。
                val pre = getSharedPreferences("id", Context.MODE_PRIVATE)
                val editor = pre.edit()
                editor.putInt("ID", pre.getInt("ID", 0) + 1)
                editor.apply()
                var folder_number = pre.getInt("ID", 0)

                //打ち込まれたフォルダー名を取得
                val userText = myedit.getText().toString()
                val db = _helper.writableDatabase

                //SQL文字列をもとにプリペアドステートメントを取得
                val stmt = db.compileStatement(insert)
                //Preの中の番号を取得
                stmt.bindLong(1, folder_number.toLong())
                //入力したときフォルダー名を取得
                stmt.bindString(2, userText)

                //データベースへのinsert実行。
                stmt.executeInsert()


                Toast.makeText(this, "$userText と入力しました", Toast.LENGTH_SHORT).show()


            })
            dialog.setNegativeButton("キャンセル", null)
            dialog.show()
        }


    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }
}
package jp.ac.it_college.std.s20020.visiontest

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

    var _id = 0
    var file_name = ""
    var folder_name = ""
    //英単語と日本語のカンマ区切り文字列を受け取る
    var all_english = ""
    var all_japanese = ""




    //現在作られているフォルダーの選択肢をDBから取得


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileFolederInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        all_english = intent.getStringExtra("ES").toString()
        all_japanese = intent.getStringExtra("JA").toString()


        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(applicationContext)

        //決定ボタンフォルダ作成ボタンを押せない状態にしておく
        binding.folderOkBtn.isEnabled = false
        binding.folderAddBtn.isEnabled  = false


        binding.failNameEdit.addTextChangedListener(object : TextWatcher {
            //テキストが編集される前に発生するイベント
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            //テキストが変更された直後(日本語IMEの変換候補が出ている間も含む)
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            //テキストが変更された直後(入力が確定された後)
            override fun afterTextChanged(p0: Editable?) {

                if(binding.failNameEdit.text.toString() != ""){
                    binding.folderOkBtn.isEnabled = true
                    binding.folderAddBtn.isEnabled = true
                } else {
                    binding.folderOkBtn.isEnabled = false
                    binding.folderAddBtn.isEnabled  = false
                }
            }

        })


        //DBからfolder_nameを取り出す
        val select_folder_name = """
            SELECT distinct folder_name FROM main
        """.trimIndent()

        //mainDB挿入命令
        val insert_main = """
            INSERT INTO main
            (_id, folder_name, file_name, english_word, japanese_word)
            VALUES(?, ?, ?, ?, ?)
        """.trimIndent()



        //スピナーにフォルダ名を表示するための工程************************

        val db = _helper.writableDatabase
        val c = db.rawQuery(select_folder_name, null)

        while(c.moveToNext()){
            val data = c.getColumnIndex("folder_name")
            spinnerItems.add(c.getString(data))
        }

        println(spinnerItems)
        println("taiga")

        //アダプター作成
        val adapter = ArrayAdapter(applicationContext,android.R.layout.simple_spinner_dropdown_item, spinnerItems)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //spinerViewにアダプターをセット
        binding.spinner.adapter = adapter

        //***************************************************************



        //フォルダー作成ボタンを押すと
        //ダイアログが出力される
        binding.folderAddBtn.setOnClickListener{
            val myedit = EditText(this)
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("文字を入力してください")
            dialog.setView(myedit)

            // OKボタン押したときの処理
            dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->



                //mainDBにインサートする***********************************

                //_idを取得
                val pre = getSharedPreferences("id", Context.MODE_PRIVATE)
                val editor = pre.edit()
                editor.putInt("ID", pre.getInt("ID", 0) + 1)
                editor.apply()
                _id = pre.getInt("ID", 0)

                //フォルダー名を取得
                folder_name = myedit.getText().toString()

                //ファイル名を取得
                file_name = binding.failNameEdit.text.toString()

                val db = _helper.writableDatabase

                //フォルダー名、ファイル名が全く同じものがないか判定する
                //判定のために列を数える
                val select = """
                SELECT COUNT(*) FROM main
                WHERE folder_name = '${folder_name}' 
                AND file_name = '${file_name}'
                """.trimIndent()

                val c = db.rawQuery(select, null)
                c.moveToNext()
                println("unko")
                println(c.getInt(0).toString())



                //かぶっているものがなければ実行する
                if(c.getInt(0).toString() != "0") {
                    Toast.makeText(this, "${folder_name}に${file_name}が既に存在しています", Toast.LENGTH_SHORT).show()
                }else {
                    //SQL文字列をもとにプリペアドステートメントを取得
                    val stmt1 = db.compileStatement(insert_main)

                    stmt1.bindLong(1, _id.toLong())
                    stmt1.bindString(2, folder_name)
                    stmt1.bindString(3, file_name)
                    stmt1.bindString(4, all_english)
                    stmt1.bindString(5, all_japanese)

                    println(_id)
                    println(folder_name)
                    println(file_name)
                    println(all_english)
                    println(all_japanese)

                    //データベースへのinsert実行。
                    stmt1.executeInsert()
                }

                //************************************************************




                //新しいフォルダを作った場合はすぐに決定ボタンを押した時点で完了して
                //初期画面に遷移する。


//                val intent = Intent(this, StudyOrCreate::class.java)
//                startActivity(intent)

            })
            dialog.setNegativeButton("キャンセル", null)
            dialog.show()
        }

        //スピナーからフォルダーを選択した場合
        binding.folderOkBtn.setOnClickListener{


        }

    }

    fun DBinsert() {

    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }
}


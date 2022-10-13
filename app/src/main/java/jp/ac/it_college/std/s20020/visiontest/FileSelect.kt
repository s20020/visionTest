package jp.ac.it_college.std.s20020.visiontest

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityFileSelectBinding
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityFolderSelectBinding

class FileSelect : AppCompatActivity() {
    private lateinit var binding : ActivityFileSelectBinding

    private lateinit var _helper: DatabaseHelper

    var FileItems = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val folder_name = intent.getStringExtra("Folder")
        var file_name = ""
        var _id = 0
        println(folder_name)


        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(applicationContext)

        val select_file_name = """
            SELECT file_name FROM main
            WHERE folder_name = '${folder_name}'
        """.trimIndent()

        binding.backBtn3.setOnClickListener{
            finish()
        }


        val db = _helper.writableDatabase
        val c = db.rawQuery(select_file_name, null)

        while(c.moveToNext()){
            val data = c.getColumnIndex("file_name")
            FileItems.add(c.getString(data))
        }

        binding.fileSelectList.adapter = ArrayAdapter(
            this,
            R.layout.simple_list_item_1,
            FileItems
        )

        binding.fileSelectList.setOnItemClickListener{ adapterView, view, position, id ->
            val intent = Intent(this, Order::class.java)
            file_name = binding.fileSelectList.getItemAtPosition(position).toString()


            //DatabaseHelperオブジェクトを生成
            _helper = DatabaseHelper(applicationContext)


            val db = _helper.writableDatabase

            val select = """
            SELECT _id FROM main
            WHERE folder_name = '${folder_name}'
            AND file_name = '${file_name}'
        """.trimIndent()

            val c = db.rawQuery(select, null)


            while(c.moveToNext()){
                val c_id = c.getColumnIndex("_id")
                _id = c.getLong(c_id).toInt()
            }

            println(_id)

            intent.putExtra("ID", _id)
            startActivity(intent)


        }
    }


}

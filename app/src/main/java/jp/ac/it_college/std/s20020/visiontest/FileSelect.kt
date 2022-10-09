package jp.ac.it_college.std.s20020.visiontest

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import jp.ac.it_college.std.s20020.visiontest.databinding.ActivityFolderSelectBinding

class FileSelect : AppCompatActivity() {
    private lateinit var binding : ActivityFolderSelectBinding

    private lateinit var _helper: DatabaseHelper

    var FileItems = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val folder_name = intent.getStringExtra("Folder")
        println(folder_name)


        //DatabaseHelperオブジェクトを生成
        _helper = DatabaseHelper(applicationContext)

        val select_file_name = """
            SELECT file_name FROM main
            WHERE folder_name = '${folder_name}'
        """.trimIndent()


        val db = _helper.writableDatabase
        val c = db.rawQuery(select_file_name, null)

        while(c.moveToNext()){
            val data = c.getColumnIndex("file_name")
            FileItems.add(c.getString(data))
        }

        binding.folderSelectList.adapter = ArrayAdapter(
            this,
            R.layout.simple_list_item_1,
            FileItems
        )



    }
}


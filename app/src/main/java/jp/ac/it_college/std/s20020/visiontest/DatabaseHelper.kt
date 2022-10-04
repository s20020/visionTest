package jp.ac.it_college.std.s20020.visiontest

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_NAME = "Vocabulary Book.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable_main ="""
            CREATE TABLE main (
            folder_number LONG,
            file_name TEXT,
            english_word TEXT,
            japanese_word TEXT
            );
        """.trimIndent()

        val createTable_folder ="""
            CREATE TABLE folder (
            folder_number,
            folder_name TEXT
            );
        """.trimIndent()

        db?.execSQL(createTable_main)
        db?.execSQL(createTable_folder)


    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


}
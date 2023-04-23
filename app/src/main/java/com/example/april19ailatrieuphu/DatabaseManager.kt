package com.example.april19ailatrieuphu

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class DatabaseManager {
    private val mContext: Context
    private val mPathFileExternalApp: String
    private var mDB: SQLiteDatabase? = null

    constructor(context: Context) {
        mContext = context
        mPathFileExternalApp = Environment.getDataDirectory()!!.path + "/data/" + mContext.packageName + "/db/Question"
        copyDB()
    }

    private fun copyDB() {

        if (File(mPathFileExternalApp).exists()) {
            return
        }
        val input = mContext.assets.open("Question")
        File(mPathFileExternalApp).parentFile?.mkdir()
        val out = FileOutputStream(File(mPathFileExternalApp))
        val b = ByteArray(1024)
        var le = input.read(b)
        while (le >= 0) {
            out.write(b, 0, le)
            le = input.read(b)
        }
        input.close()
        out.close()
    }

    private fun openDB() {
        if (mDB != null && mDB!!.isOpen) {
            return
        }
        mDB = SQLiteDatabase.openDatabase(
            mPathFileExternalApp,
            null,
            SQLiteDatabase.OPEN_READWRITE
        )
    }

    private fun closeDB() {
        if (mDB != null && mDB!!.isOpen) {
            mDB!!.close()
            mDB = null
        }
    }

    fun queryQuestions(
        sql: String = "select * from (select * from Question order by random()) group by level order by level"
    ): MutableList<Question> {
        val questions = mutableListOf<Question>()
        openDB()
        val cursor = mDB!!.rawQuery(sql, null, null)
        val indexId = cursor.getColumnIndex("_id")
        val indexQuestion = cursor.getColumnIndex("question")
        val indexLevel = cursor.getColumnIndex("level")
        val indexcaseA = cursor.getColumnIndex("casea")
        val indexcaseB = cursor.getColumnIndex("caseb")
        val indexcaseC = cursor.getColumnIndex("casec")
        val indexcaseD = cursor.getColumnIndex("cased")
        val indexTrueCase = cursor.getColumnIndex("truecase")
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val id = cursor.getLong(indexId)
            val question = cursor.getString(indexQuestion)
            val level = cursor.getInt(indexLevel)
            val caseA = cursor.getString(indexcaseA)
            val caseB = cursor.getString(indexcaseB)
            val caseC = cursor.getString(indexcaseC)
            val caseD = cursor.getString(indexcaseD)
            val trueCase = cursor.getInt(indexTrueCase)
            questions.add(
                Question(
                    id, question, level, caseA, caseB, caseC, caseD, trueCase
                )
            )
            Log.d("DatabaseManager", "question ${level}: ${question}")
            Log.d("DatabaseManager", "caseA: ${caseA}")
            Log.d("DatabaseManager", "caseB: ${caseB}")
            Log.d("DatabaseManager", "caseC: ${caseC}")
            Log.d("DatabaseManager", "caseD: ${caseD}")
            Log.d("DatabaseManager", "\n\n\n\ntruecase: ${trueCase}")
            Log.d(
                "DatabaseManager",
                "================================================================"
            )

            cursor.moveToNext()
        }
        cursor.close()
        closeDB()
        return questions
    }

    fun getQuestion(level: Int): Question {
        val sql = "select * from Question where level = ${level} order by random() limit 1"
        return queryQuestions(sql)[0]
    }

    fun insertHighScore(
        name: String,
        score: Int
    ) {
        openDB()
        val keyValue = ContentValues()
        keyValue.put("Name", name)
        keyValue.put("Score", score)

//        khi thay doi database thi can transaction
        mDB!!.beginTransaction()

        mDB!!.insert("HighScore", null, keyValue)

        mDB!!.setTransactionSuccessful()
        mDB!!.endTransaction()
        closeDB()
    }

    fun updateScore(id: Int, score: Int){
        openDB()
        mDB!!.beginTransaction()
        val keyValue = ContentValues()
        keyValue.put("Score", score)
        mDB!!.update("HighScore",
            keyValue,
            "ID = ?",
            arrayOf(id.toString())
        )
        mDB!!.setTransactionSuccessful()
        mDB!!.endTransaction()
        closeDB()
    }

    fun deleteScore(id: Int){
        openDB()
        mDB!!.beginTransaction()
        mDB!!.delete("HighScore",
            "ID = ?",
            arrayOf(id.toString())
        )
        mDB!!.setTransactionSuccessful()
        mDB!!.endTransaction()
        closeDB()
    }

    fun queryHighScore(): MutableList<HighScore> {
        val highScores = mutableListOf<HighScore>()
        openDB()
        val sql = "select * from HighScore"
        val cursor = mDB!!.rawQuery(sql, null, null)
        val indexId = cursor.getColumnIndex("ID")
        val indexName = cursor.getColumnIndex("Name")
        val indexScore = cursor.getColumnIndex("Score")
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val id = cursor.getInt(indexId)
            val name = cursor.getString(indexName)
            val score = cursor.getInt(indexScore)
            highScores.add(HighScore(id, name, score))
            Log.d("DatabaseManager", "queryHighScore id: $id name: ${name}, score: ${score}")
            cursor.moveToNext()
        }
        closeDB()


        return highScores
    }
}
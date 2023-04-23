package com.example.april19ailatrieuphu

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class PlayActivity : AppCompatActivity() {
    private lateinit var ivCircle: ImageView
    private var currentQuestion: Int = 0
    private lateinit var answer1: TextView
    private lateinit var answer2: TextView
    private lateinit var answer3: TextView
    private lateinit var answer4: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var tvOrderQuestion: TextView
    private lateinit var tvMoney: TextView
    private var handler = Handler()
    private lateinit var tvTime: TextView
    private var num = 0
    private var isClick = false
    private lateinit var iv_percent50: ImageView
    private lateinit var iv_call: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        answer1 = findViewById(R.id.answer_1)
        answer2 = findViewById(R.id.answer_2)
        answer3 = findViewById(R.id.answer_3)
        answer4 = findViewById(R.id.answer_4)
        tvQuestion = findViewById(R.id.tv_question)
        tvOrderQuestion = findViewById(R.id.tv_order_question)
        tvMoney = findViewById(R.id.tv_money)
        tvTime = findViewById(R.id.tv_time)
        ivCircle = findViewById(R.id.iv_circle)
        iv_percent50 = findViewById(R.id.iv_percent)
        iv_call = findViewById(R.id.iv_call)
        val animation = AnimationUtils.loadAnimation(this, R.anim.spin_around_slow)
        ivCircle.animation = animation
        ivCircle.startAnimation(animation)


//        MediaManager.start(this, R.raw.background_music)
        val databaseManager: DatabaseManager = DatabaseManager(this)
        val questions = databaseManager.queryQuestions()
        prepareAnswer(questions)
        iv_call.setOnClickListener {
            it.visibility = View.INVISIBLE
            isClick = true
            handler.postDelayed(Runnable {
                MediaManager.start(this, R.raw.call)

                MediaManager.mMediaPlayer.setOnCompletionListener {
                    MediaManager.start(this, R.raw.help_call)
                    handler.postDelayed(Runnable {
                        MediaManager.start(this@PlayActivity, R.raw.help_callb)
                    }, 1000)
                    val dialog = Dialog(this@PlayActivity)
                    dialog.setContentView(R.layout.layout_call)
                    val iv1 = dialog.findViewById<ImageView>(R.id.iv_avatar1)
                    val iv2 = dialog.findViewById<ImageView>(R.id.iv_avatar2)
                    val iv3 = dialog.findViewById<ImageView>(R.id.iv_avatar3)
                    val iv4 = dialog.findViewById<ImageView>(R.id.iv_avatar4)
                    val currentQues = questions.get(currentQuestion)
                    val trueCase: Int = currentQues.trueCase
                    var myAnswer: String = ""
                    when (trueCase) {
                        1 -> myAnswer = "Đáp án của tôi là A"
                        2 -> myAnswer = "Đáp án của tôi là B"
                        3 -> myAnswer = "Đáp án của tôi là C"
                        4 -> myAnswer = "Đáp án của tôi là D"
                    }

                    iv1.setOnClickListener {
                        dialog.dismiss()
                        createAnswerDialog(myAnswer)
                    }
                    iv2.setOnClickListener {
                        dialog.dismiss()
                        createAnswerDialog(myAnswer)
                    }
                    iv3.setOnClickListener {
                        dialog.dismiss()
                        createAnswerDialog(myAnswer)
                    }
                    iv4.setOnClickListener {
                        dialog.dismiss()
                        createAnswerDialog(myAnswer)
                    }
                    dialog.show()


                }
            }, 1000)

        }

        iv_percent50.setOnClickListener {
            it.visibility = View.INVISIBLE
            isClick = true
            handler.postDelayed(Runnable {
                val num = Random.nextInt(2)
                when (num) {
                    0 -> {
                        MediaManager.start(this, R.raw.sound5050)
                    }
                    1 -> {
                        MediaManager.start(this, R.raw.sound5050_2)
                    }
                }

                val currentQues = questions.get(currentQuestion)
                val trueCase: Int = currentQues.trueCase
                val numbers = arrayListOf<Int>(1, 2, 3, 4)
                numbers.remove(trueCase)
                numbers.removeAt(Random.nextInt(3))
                MediaManager.mMediaPlayer.setOnCompletionListener {
                    for (item in numbers) {
                        when (item) {
                            1 -> answer1.visibility = View.INVISIBLE
                            2 -> answer2.visibility = View.INVISIBLE
                            3 -> answer3.visibility = View.INVISIBLE
                            4 -> answer4.visibility = View.INVISIBLE
                        }
                    }
                }

            }, 2000)

        }
        answer1.setOnClickListener {
            isClick = true
            answer1.setBackgroundResource(R.drawable.bg_choose1)
            MediaManager.start(this, R.raw.ans_a)
            handler.postDelayed(Runnable {
                MediaManager.start(this@PlayActivity, R.raw.ans_now1)
            }, 3000)
            checkAnswer(questions, 1)
        }
        answer2.setOnClickListener {
            isClick = true
            answer2.setBackgroundResource(R.drawable.bg_choose1)
            MediaManager.start(this, R.raw.ans_b)
            handler.postDelayed({
                MediaManager.start(this@PlayActivity, R.raw.ans_now2)
            }, 3000)
            checkAnswer(questions, 2)


        }
        answer3.setOnClickListener {
            isClick = true
            answer3.setBackgroundResource(R.drawable.bg_choose1)
            MediaManager.start(this, R.raw.ans_c)
            handler.postDelayed({
                MediaManager.start(this@PlayActivity, R.raw.ans_now3)
            }, 3000)

            checkAnswer(questions, 3)

        }
        answer4.setOnClickListener {
            isClick = true
            answer4.setBackgroundResource(R.drawable.bg_choose1)
            MediaManager.start(this, R.raw.ans_d)
            handler.postDelayed({
                MediaManager.start(this@PlayActivity, R.raw.ans_now3)
            }, 3000)
            checkAnswer(questions, 4)
        }
    }

    private var playSound: Runnable = object : Runnable {
        override fun run() {
            MediaManager.start(this@PlayActivity, R.raw.ans_now1)
        }

    }

    private fun prepareAnswer(questions: MutableList<Question>) {
        isClick = false
        num = 0
        answer1.isClickable = true
        answer2.isClickable = true
        answer3.isClickable = true
        answer4.isClickable = true
        answer1.visibility = View.VISIBLE
        answer2.visibility = View.VISIBLE
        answer3.visibility = View.VISIBLE
        answer4.visibility = View.VISIBLE
        val currentQues = questions.get(currentQuestion)
        var quesMedia: Int = 0
        var money: String = "0"
        when (currentQues.level) {
            1 -> {
                money = "0$"
                quesMedia = R.raw.ques1
            }
            2 -> {
                quesMedia = R.raw.ques2
                money = "200,000$"
            }
            3 -> {
                quesMedia = R.raw.ques3

                money = "400.000$"
            }
            4 -> {
                quesMedia = R.raw.ques4

                money = "600,000$"
            }
            5 -> {
                quesMedia = R.raw.ques5

                money = "2,000,000$"
            }
            6 -> {
                quesMedia = R.raw.ques6
                money = "3,000,000$"
            }
            7 -> {
                quesMedia = R.raw.ques7
                money = "6,000,000$"
            }
            8 -> {
                quesMedia = R.raw.ques8
                money = "1,000,000$"
            }
            9 -> {
                quesMedia = R.raw.ques9
                money = "14,000,000$"
            }
            10 -> {
                quesMedia = R.raw.ques10
                money = "22,000,000$"
            }
            11 -> {
                quesMedia = R.raw.ques11
                money = "30,000,000$"
            }
            12 -> {
                quesMedia = R.raw.ques12
                money = "40,000,000$"
            }
            13 -> {
                quesMedia = R.raw.ques13
                money = "60,000,000$"
            }
            14 -> {
                quesMedia = R.raw.ques14
                money = "85,000,000$"
            }
            15 -> {
                quesMedia = R.raw.ques15
                money = "150,000,000$"
            }

        }
        MediaManager.start(this, quesMedia)
        handler.postDelayed(Runnable {
            val num = Random.nextInt(3)
            when (num) {
                0 -> MediaManager.start(this, R.raw.background_music)
                1 -> MediaManager.start(this, R.raw.background_music_b)
                2 -> MediaManager.start(this, R.raw.background_music_c)
            }

        }, 2000)
        tvMoney.setText(money.toString())
        tvQuestion.setText(currentQues.question.toString())
        answer1.setText(currentQues.caseA.toString())
        answer2.setText(currentQues.caseB.toString())
        answer3.setText(currentQues.caseC.toString())
        answer4.setText(currentQues.caseD.toString())
        tvOrderQuestion.setText("Câu " + currentQues.level.toString())
        answer1.setBackgroundResource(R.drawable.bg_nomal)
        answer2.setBackgroundResource(R.drawable.bg_nomal)
        answer3.setBackgroundResource(R.drawable.bg_nomal)
        answer4.setBackgroundResource(R.drawable.bg_nomal)
        handler.postDelayed(updateTime, 1000)
    }

    private var updateTime: Runnable = object : Runnable {
        override fun run() {
            num++
            var time = 60 - num

            if (isClick == false) {
                tvTime.setText(time.toString())
                handler.postDelayed(this, 1000)
            }
            if (time == 0) {
                handler.removeCallbacksAndMessages(null)
                MediaManager.start(this@PlayActivity, R.raw.out_of_time)
                var dialog: Dialog = Dialog(this@PlayActivity)
                dialog.setContentView(R.layout.layout_gift_feedback)
                val tvDiaLogMoney = dialog.findViewById<TextView>(R.id.tv_money)
                tvDiaLogMoney.setText(tvMoney.text)
                val tvMain = dialog.findViewById<TextView>(R.id.tv_main_menu)
                tvMain.setOnClickListener {
                    val intent = Intent(this@PlayActivity, MainActivity::class.java)
//                        dialog.dismiss()
                    finish()
//                        startActivity(intent)
                }
                dialog.show()
            }
        }

    }

    private fun createAnswerDialog(answer: String) {

        val myDialog = Dialog(this@PlayActivity)
        myDialog.setContentView(R.layout.layout_answer)
        val tv_answer = myDialog.findViewById<TextView>(R.id.tv_answer)
        tv_answer.setText(answer.toString())
        val tv_exit = myDialog.findViewById<TextView>(R.id.tv_exit)
        tv_exit.setOnClickListener {
            myDialog.dismiss()
        }
        myDialog.show()
    }

    private fun checkAnswer(questions: MutableList<Question>, answer: Int) {
        //check dung sai thi vao prepare answer
        if (answer == 15) {
            finish()
        }
        answer1.isClickable = false
        answer2.isClickable = false
        answer3.isClickable = false
        answer4.isClickable = false
        if (answer == questions[currentQuestion].trueCase) {


            handler.postDelayed(Runnable {
                when (answer) {
                    1 -> answer1.setBackgroundResource(R.drawable.bg_true1)
                    2 -> answer2.setBackgroundResource(R.drawable.bg_true1)
                    3 -> answer3.setBackgroundResource(R.drawable.bg_true1)
                    4 -> answer4.setBackgroundResource(R.drawable.bg_true1)
                }
                var quesMedia: Int = 0
                when (questions[currentQuestion].trueCase) {
                    1 -> quesMedia = R.raw.true_a
                    2 -> quesMedia = R.raw.true_b
                    3 -> quesMedia = R.raw.true_c
                    4 -> quesMedia = R.raw.true_d2
                }
                MediaManager.start(this, quesMedia)
                currentQuestion++

                handler.postDelayed(Runnable {
                    prepareAnswer(questions)
                }, 3000)

            }, 6000)


        } else {
            handler.postDelayed(Runnable {
                var quesMedia: Int = 0
                when (questions[currentQuestion].trueCase) {
                    1 -> quesMedia = R.raw.lose_a
                    2 -> quesMedia = R.raw.lose_b
                    3 -> quesMedia = R.raw.lose_c
                    4 -> quesMedia = R.raw.lose_d
                }
                when (answer) {
                    1 -> answer1.setBackgroundResource(R.drawable.bg_faile1)
                    2 -> answer2.setBackgroundResource(R.drawable.bg_faile1)
                    3 -> answer3.setBackgroundResource(R.drawable.bg_faile1)
                    4 -> answer4.setBackgroundResource(R.drawable.bg_faile1)
                }
                when (questions[currentQuestion].trueCase) {
                    1 -> answer1.setBackgroundResource(R.drawable.bg_true1)
                    2 -> answer2.setBackgroundResource(R.drawable.bg_true1)
                    3 -> answer3.setBackgroundResource(R.drawable.bg_true1)
                    4 -> answer4.setBackgroundResource(R.drawable.bg_true1)
                }
                MediaManager.start(this@PlayActivity, quesMedia)
                MediaManager.mMediaPlayer.setOnCompletionListener {
                    var dialog: Dialog = Dialog(this)
                    dialog.setContentView(R.layout.layout_gift_feedback)
                    val tvDiaLogMoney = dialog.findViewById<TextView>(R.id.tv_money)
                    tvDiaLogMoney.setText(tvMoney.text)
                    val tvMain = dialog.findViewById<TextView>(R.id.tv_main_menu)
                    tvMain.setOnClickListener {
                        val intent = Intent(this, MainActivity::class.java)
//                        dialog.dismiss()
                        finish()
//                        startActivity(intent)
                    }
                    dialog.show()
                    handler.postDelayed(Runnable {
                        MediaManager.start(this, R.raw.lose)
                    }, 500)
                }


            }, 6000)
//            handler.postDelayed(Runnable {
//
//            },2000)
        }
    }

}
package com.example.whoismillionaire;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whoismillionaire.R.drawable;
import com.example.whoismillionaire.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvStt;
    private TextView tvQuestion;
    private Button btAnswerA;
    private Button btAnswerB;
    private Button btAnswerC;
    private Button btAnswerD;
    private TextView tvTimes;
    private TextView tvPoint;

    private int count = 1;              // Câu hỏi hiện tại
    private boolean clickHelpHalf;      // Trợ giúp 50:50 đã được dùng hay chưa?
    private boolean clickHelpCall;      // Trợ giúp Call đã được dùng hay chưa?
    private boolean clickHelpPerson;    // Trợ giúp Person đã được dùng hay chưa?
    private boolean clickCall = false;  // Chỉ được chọn 1 người trong 3 người để nhận trợ giúp
    private String answerTrue;          // Biến lưu giữ đáp án đúng
    private int indexChoose = -1;       // Đáp án User đã chọn
    private boolean isExit = false;     // User không được chơi nữa
    private String[] pointList = {"0", "200", "400", "600", "1,000", "2,000",
            "3,000", "6,000", "10,000", "14,000", "22,000",
            "30,000", "40,000", "60,000", "85,000", "150,000"};   // Tiền thưởng

    private MyDatabase myDatabase;
    private List<Question> listQuestionLv1 = new ArrayList<>();
    private List<Question> listQuestionLv2 = new ArrayList<>();
    private List<Question> listQuestionLv3 = new ArrayList<>();
    private Question currentQuestion;
    private List<Integer> listMediaQuestion = new ArrayList<>();

    MediaPlayer mediaQuestion, mediaClock, mediaAnswer, mediaAnswerNow, mediaAnswerTrue,
            mediaHelp5050, mediaHelpCall, mediaHelpPerson, media, mediaEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();

        myDatabase = new MyDatabase(this);
        listQuestionLv1.addAll(myDatabase.getData(1));
        listQuestionLv2.addAll(myDatabase.getData(2));
        listQuestionLv3.addAll(myDatabase.getData(3));

        addMediaQuestion();
        newQuestion(count);

//        // Khi sử dụng data là file Json
//        try {
//            JSONArray array = new JSONArray(readData());
//            int length = array.length();
//            for (int i = 0; i < length; i++) {
//                JSONObject object = array.getJSONObject(i);
//                list.add(new Question(object.optString("Question"),
//                        object.optString("A"), object.optString("B"),
//                        object.optString("C"), object.optString("D"),
//                        object.optString("answer")));
//            }
//            // Chạy chương trình
//            newQuestion();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    // List âm thanh đọc câu hỏi
    private void addMediaQuestion() {
        listMediaQuestion.add(R.raw.start_cau1);
        listMediaQuestion.add(R.raw.start_cau2);
        listMediaQuestion.add(R.raw.start_cau3);
        listMediaQuestion.add(R.raw.start_cau4);
        listMediaQuestion.add(R.raw.start_cau5);
        listMediaQuestion.add(R.raw.start_cau6);
        listMediaQuestion.add(R.raw.start_cau7);
        listMediaQuestion.add(R.raw.start_cau8);
        listMediaQuestion.add(R.raw.start_cau9);
        listMediaQuestion.add(R.raw.start_cau10);
        listMediaQuestion.add(R.raw.start_cau11);
        listMediaQuestion.add(R.raw.start_cau12);
        listMediaQuestion.add(R.raw.start_cau13);
        listMediaQuestion.add(R.raw.start_cau14);
        listMediaQuestion.add(R.raw.start_cau15);
    }

    /*
    Click trợ giúp
        + Khi chọn đáp án rồi thì không được chọn trợ giúp nữa
        + Mỗi trợ giúp chỉ được dùng 1 lần
        + Khi sử dụng trợ giúp thì đồng hồ phải dừng lại
     */
    public void onClickHelp(View view) {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        switch (view.getId()) {
            case R.id.bt_Half:
                if (indexChoose == -1) {
                    if (!clickHelpHalf) {
                        dialog.setContentView(R.layout.dialog_question_help);
                        TextView tvQuestionHelp = dialog.findViewById(R.id.tv_question_help);
                        Button btYesHalf = dialog.findViewById(R.id.bt_yesHalf);
                        Button btNoHalf = dialog.findViewById(R.id.bt_noHalf);
                        tvQuestionHelp.setText("Bạn chọn trợ giúp 50:50?");
                        dialog.show();
                        btYesHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timer.pause();
                                mediaClock.pause();

                                dialog.cancel();
                                clickHelpHalf = true;
                                mediaHelp5050 = MediaPlayer.create(MainActivity.this, R.raw.sound5050);
                                mediaHelp5050.start();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch (currentQuestion.getAnswer()) {
                                            case 0:
                                                btAnswerB.setText("");
                                                btAnswerC.setText("");
                                                break;
                                            case 1:
                                                btAnswerC.setText("");
                                                btAnswerD.setText("");
                                                break;
                                            case 2:
                                                btAnswerD.setText("");
                                                btAnswerA.setText("");
                                                break;
                                            case 3:
                                                btAnswerA.setText("");
                                                btAnswerB.setText("");
                                                break;
                                        }
                                    }
                                }, 3000);
                                mediaHelp5050.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        timer.start();
                                    }
                                });
                            }
                        });
                        btNoHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                    } else Toast.makeText(MainActivity.this,
                            "Bạn chỉ được sử dụng các sự trợ giúp một lần", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ibt_Call:
                if (indexChoose == -1) {
                    if (!clickHelpCall) {
                        dialog.setContentView(R.layout.dialog_question_help);
                        TextView tvQuestionHelp = dialog.findViewById(R.id.tv_question_help);
                        Button btYesHalf = dialog.findViewById(R.id.bt_yesHalf);
                        Button btNoHalf = dialog.findViewById(R.id.bt_noHalf);
                        tvQuestionHelp.setText("Bạn chọn trợ giúp gọi điện thoại cho người thân?");
                        dialog.show();
                        btYesHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                timer.pause();
                                mediaClock.pause();

                                dialog.cancel();
                                clickHelpCall = true;
                                mediaHelpCall = MediaPlayer.create(MainActivity.this, R.raw.sfx_help_call);
                                mediaHelpCall.start();
                                mediaHelpCall.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        dialog.setContentView(R.layout.dialog_help_call);
                                        dialog.show();
                                        ImageButton imbTrump = dialog.findViewById(R.id.imb_Trump);
                                        ImageButton imbMessi = dialog.findViewById(R.id.imb_Messi);
                                        ImageButton imbMark = dialog.findViewById(R.id.imb_Mark);
                                        imbTrump.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (!clickCall) {
                                                    Toast.makeText(MainActivity.this,
                                                            "Trump tư vấn cho bạn chọn : " + answerTrue, Toast.LENGTH_SHORT).show();
                                                    clickCall = true;
                                                    dialog.cancel();
                                                    timer.start();
                                                }
                                            }
                                        });
                                        imbMessi.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (!clickCall) {
                                                    Toast.makeText(MainActivity.this,
                                                            "Messi tư vấn cho bạn chọn : " + answerTrue, Toast.LENGTH_SHORT).show();
                                                    clickCall = true;
                                                    dialog.cancel();
                                                    timer.start();
                                                }
                                            }
                                        });
                                        imbMark.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (!clickCall) {
                                                    Toast.makeText(MainActivity.this,
                                                            "Mark tư vấn cho bạn chọn : " + answerTrue, Toast.LENGTH_SHORT).show();
                                                    clickCall = true;
                                                    dialog.cancel();
                                                    timer.start();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        btNoHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                    } else Toast.makeText(MainActivity.this,
                            "Bạn chỉ được sử dụng các sự trợ giúp một lần", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ibt_Person:
                if (indexChoose == -1) {
                    if (!clickHelpPerson) {
                        dialog.setContentView(R.layout.dialog_question_help);
                        TextView tvQuestionHelp = dialog.findViewById(R.id.tv_question_help);
                        Button btYesHalf = dialog.findViewById(R.id.bt_yesHalf);
                        Button btNoHalf = dialog.findViewById(R.id.bt_noHalf);
                        tvQuestionHelp.setText("Bạn chọn trợ giúp hỏi tổ tư vấn?");
                        dialog.show();
                        btYesHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                timer.pause();
                                mediaClock.pause();

                                dialog.cancel();
                                clickHelpPerson = true;
                                mediaHelpPerson = MediaPlayer.create(MainActivity.this, R.raw.sound_trogiup_tuvan);
                                mediaHelpPerson.start();
                                mediaHelpPerson.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        switch (currentQuestion.getAnswer()) {
                                            case 0:
                                                media = MediaPlayer.create(MainActivity.this,
                                                        R.raw.trogiup_a);
                                                media.start();
                                                break;

                                            case 1:
                                                media = MediaPlayer.create(MainActivity.this,
                                                        R.raw.trogiup_b);
                                                media.start();
                                                break;

                                            case 2:
                                                media = MediaPlayer.create(MainActivity.this,
                                                        R.raw.trogiup_c);
                                                media.start();
                                                break;

                                            case 3:
                                                media = MediaPlayer.create(MainActivity.this,
                                                        R.raw.trogiup_d);
                                                media.start();
                                                break;
                                        }
                                        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mediaPlayer) {
                                                timer.start();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        btNoHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                    } else Toast.makeText(MainActivity.this,
                            "Bạn chỉ được sử dụng các sự trợ giúp một lần", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*
        Hộp thoại thông báo User đã trả lời sai và đưa ra lựa chọn có muốn tiếp tục chơi?
            TH1.Nếu User đồng ý, thì bắt đầu lại từ đầu
            TH2.Nếu không đồng ý thì chào và thoát
     */
    private void message() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_message);

        Button btYes = dialog.findViewById(R.id.bt_Yes);
        Button btNo = dialog.findViewById(R.id.bt_No);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count = 1;
                        clickHelpHalf = false;
                        clickHelpCall = false;
                        clickHelpPerson = false;
                        clickCall = false;
                        indexChoose = -1;
                        isExit = false;
                        newQuestion(count);
                    }
                }, 1500);
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                mediaEnd = MediaPlayer.create(MainActivity.this, R.raw.sound_ket_thuc);
                mediaEnd.start();
                mediaEnd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        MainActivity.this.finish();
                    }
                });
            }
        });
        dialog.show();
    }

    // Khởi tạo media đáp án đúng, thay đổi background đáp án đúng
    private void createMediaAnswerTrue() {
        switch (currentQuestion.getAnswer()) {
            case 0:
                if (indexChoose == 0)
                    mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.true_a);
                else
                    mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.lose_a);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btAnswerA.setBackgroundResource(R.drawable.background_answer_true);
                    }
                }, 1000);
                break;
            case 1:
                if (indexChoose == 1) {
                    if (count <= 5)
                        mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                                R.raw.true_b_1);
                    else mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.true_b_2);
                } else
                    mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.lose_b);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btAnswerB.setBackgroundResource(R.drawable.background_answer_true);
                    }
                }, 1000);
                break;
            case 2:
                if (indexChoose == 2) {
                    if (count <= 5) mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.true_c_1);
                    else mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.true_c_2);
                } else
                    mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.lose_c);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btAnswerC.setBackgroundResource(R.drawable.background_answer_true);
                    }
                }, 1000);
                break;
            case 3:
                if (indexChoose == 3) {
                    if (count <= 5) mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.true_d_1);
                    else mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.true_d_2);
                } else
                    mediaAnswerTrue = MediaPlayer.create(MainActivity.this,
                            R.raw.lose_d);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btAnswerD.setBackgroundResource(R.drawable.background_answer_true);
                    }
                }, 1000);
                break;
        }
    }

    // Khởi tạo media đọc câu đáp án mà User chọn
    private void createMediaAnswer() {
        switch (indexChoose) {
            case 0:
                mediaAnswer = MediaPlayer.create(MainActivity.this, R.raw.ans_a);
                mediaAnswer.start();
                break;
            case 1:
                mediaAnswer = MediaPlayer.create(MainActivity.this, R.raw.ans_b);
                mediaAnswer.start();
                break;
            case 2:
                mediaAnswer = MediaPlayer.create(MainActivity.this, R.raw.ans_c);
                mediaAnswer.start();
                break;
            case 3:
                mediaAnswer = MediaPlayer.create(MainActivity.this, R.raw.ans_d);
                mediaAnswer.start();
                break;
        }
    }

    private void showAnswer() {
        if (mediaClock.isPlaying()) mediaClock.stop();

        createMediaAnswer();
        mediaAnswer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaAnswerNow = MediaPlayer.create(MainActivity.this, R.raw.answer_now);
                mediaAnswerNow.start();
                mediaAnswerNow.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        createMediaAnswerTrue();
                        mediaAnswerTrue.start();
                        mediaAnswerTrue.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Nếu chọn đúng thì hiển thị câu hỏi mới, nếu sai thì hiện thông báo
                                        if (indexChoose == currentQuestion.getAnswer() && count <= 15) {
                                            if (count < 15) {
                                                count++;
                                                indexChoose = -1;   // Reset indexChoose
                                                newQuestion(count);
                                            } else {
                                                startActivity(new Intent(MainActivity.this, WinActivity.class));
                                            }
                                        } else {
                                            message();
                                        }
                                    }
                                }, 1000);
                            }
                        });
                    }
                });
            }
        });
    }

    // Bộ đếm thời gian
    private CountDownTimerCustom timer = new CountDownTimerCustom(10600, 500) {
        // Lần đầu chạy thì background đồng hồ sẽ hiện, và âm đồng hồ sẽ được bật
        @Override
        public void onTick(long millisUntilFinished) {
            tvTimes.setBackgroundResource(drawable.custom_times);
            tvTimes.setText(millisUntilFinished / 1000 + "s");
            if (!mediaClock.isPlaying()) mediaClock.start();
            if(indexChoose != -1){
                timer.cancel();
                tvTimes.setBackgroundResource(0);
                tvTimes.setText("");
            }
        }

        // Nếu User chưa chọn đáp án thì hiện thông báo hỏi có muốn chơi tiếp không?
        public void onFinish() {
            if (indexChoose == -1) {
                message();
                isExit = true;
            }
        }
    };

    /*
        User click chọn đáp án ( nếu User chưa chọn đáp án, và đáp án đó không rỗng thì )
            TH1.Câu hỏi ở lv1 : thay đổi ngay background của đáp án đó
            TH2.Hiển thị hộp thoại hỏi User đã chắc chắn chọn chưa? nếu chưa thì cho phép chọn lại
     */
    public void clickAnswer(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_message_when_user_choose_answer);

        TextView tv = dialog.findViewById(R.id.tv);
        Button btAgree = dialog.findViewById(R.id.bt_agree);
        Button btDisagree = dialog.findViewById(R.id.bt_disagree);

        switch (view.getId()) {
            case R.id.bt_AnswerA:
                if (indexChoose == -1 && btAnswerA.getText() != "" && isExit == false) {
                    if (count <= 5) {
                        btAnswerA.setBackgroundResource(R.drawable.background_answer_when_click);
                        indexChoose = 0;
                    } else {
                        tv.setText("Bạn chọn đáp án A?");
                        btAgree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btAnswerA.setBackgroundResource(R.drawable.background_answer_when_click);
                                dialog.cancel();
                                indexChoose = 0;
                            }
                        });
                        btDisagree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                indexChoose = -1;
                            }
                        });
                        dialog.show();
                    }
                }
                break;

            case R.id.bt_AnswerB:
                if (indexChoose == -1 && btAnswerB.getText() != "" && isExit == false) {
                    if (count <= 5) {
                        btAnswerB.setBackgroundResource(R.drawable.background_answer_when_click);
                        indexChoose = 1;
                    } else {
                        tv.setText("Bạn chọn đáp án B?");
                        btAgree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btAnswerB.setBackgroundResource(R.drawable.background_answer_when_click);
                                dialog.cancel();
                                indexChoose = 1;
                            }
                        });
                        btDisagree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                indexChoose = -1;
                            }
                        });
                        dialog.show();
                    }
                }
                break;

            case R.id.bt_AnswerC:
                if (indexChoose == -1 && btAnswerC.getText() != "" && isExit == false) {
                    if (count <= 5) {
                        btAnswerC.setBackgroundResource(R.drawable.background_answer_when_click);
                        indexChoose = 2;
                    } else {
                        tv.setText("Bạn chọn đáp án C?");
                        btAgree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btAnswerC.setBackgroundResource(R.drawable.background_answer_when_click);
                                dialog.cancel();
                                indexChoose = 2;
                            }
                        });
                        btDisagree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                indexChoose = -1;
                            }
                        });
                        dialog.show();
                    }
                }
                break;

            case R.id.bt_AnswerD:
                if (indexChoose == -1 && btAnswerD.getText() != "" && isExit == false) {
                    if (count <= 5) {
                        btAnswerD.setBackgroundResource(R.drawable.background_answer_when_click);
                        indexChoose = 3;
                    } else {
                        tv.setText("Bạn chọn đáp án D?");
                        btAgree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btAnswerD.setBackgroundResource(R.drawable.background_answer_when_click);
                                dialog.cancel();
                                indexChoose = 3;
                            }
                        });
                        btDisagree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                                indexChoose = -1;
                            }
                        });
                        dialog.show();
                    }
                }
                break;
        }
        showAnswer();
    }

    // Chạy âm thanh đọc câu hỏi
    private void startMediaQuestion(int i) {
        mediaQuestion = MediaPlayer.create(MainActivity.this, listMediaQuestion.get(i));
        mediaQuestion.start();
    }

    /*
        Câu hỏi mới ( truyền vào vị trí câu hỏi cần hiện )
            + Hiện tiền thưởng hiện tại ( ban đầu = 0 )
            + Mỗi lần hiểu thị câu hỏi mới thì set lại background cho đáp án
            + Lấy câu hỏi từ các list theo level tương ứng
            + Lưu trữ 4 đáp án vào 1 list, lấy ra đáp án đúng, sau đó trộn đáp án và set lại đáp án đúng
            + Convert đáp án đúng từ kiểu int -> String
            + Khi hết giọng đọc thì bắt đầu hiển thị đồng hồ và bắt đầu tính thời gian
     */
    private void newQuestion(int i) {
        indexChoose = -1;
        startMediaQuestion(i - 1);
        tvPoint.setText(pointList[i - 1]);
        btAnswerA.setBackgroundResource(R.drawable.background_answer);
        btAnswerB.setBackgroundResource(R.drawable.background_answer);
        btAnswerC.setBackgroundResource(R.drawable.background_answer);
        btAnswerD.setBackgroundResource(R.drawable.background_answer);

        // Lấy câu hỏi
        if (i <= 5) currentQuestion = listQuestionLv1.get(i - 1);
        else if (i <= 10) currentQuestion = listQuestionLv2.get(i - 6);
        else currentQuestion = listQuestionLv3.get(i - 11);

        List<String> answers = currentQuestion.getAnswerList();
        answerTrue = answers.get(currentQuestion.getAnswer());
        Collections.shuffle(answers);   // Trộn 4 câu trả lời
        for (String s : answers) {
            if (s.equals(answerTrue)) {
                currentQuestion.setAnswer(answers.indexOf(s));
                break;
            }
        }

        // Convert câu trả lời đúng
        switch (currentQuestion.getAnswer()) {
            case 0:
                answerTrue = "A";
                break;
            case 1:
                answerTrue = "B";
                break;
            case 2:
                answerTrue = "C";
                break;
            case 3:
                answerTrue = "D";
                break;
        }

        tvStt.setText("Câu số : " + i);
        tvQuestion.setText(currentQuestion.getQuestion());
        btAnswerA.setText("A : " + answers.get(0));
        btAnswerB.setText("B : " + answers.get(1));
        btAnswerC.setText("C : " + answers.get(2));
        btAnswerD.setText("D : " + answers.get(3));

        mediaQuestion.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaClock = MediaPlayer.create(MainActivity.this, R.raw.eff_clock);
                timer.cancel();
                timer.start();
            }
        });
    }

    // Ánh xạ đối tượng
    private void mapping() {
        tvStt = findViewById(R.id.tv_Stt);
        tvQuestion = findViewById(R.id.tv_Question);
        btAnswerA = findViewById(R.id.bt_AnswerA);
        btAnswerB = findViewById(R.id.bt_AnswerB);
        btAnswerC = findViewById(R.id.bt_AnswerC);
        btAnswerD = findViewById(R.id.bt_AnswerD);
        tvTimes = findViewById(R.id.tv_times);
        tvPoint = findViewById(R.id.tv_Point);
    }

//     // Đọc file Json trả về danh sách
//    private String readData() {
//        InputStream inputStream = getResources().openRawResource(R.raw.data);
//        Writer writer = new StringWriter();
//        char[] buffer = new char[1024];
//        try {
//            Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//            int n;
//            while ((n = reader.read(buffer)) != -1) {
//                writer.write(buffer, 0, n);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return writer.toString();
//    }
}
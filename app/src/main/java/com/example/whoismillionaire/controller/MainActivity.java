package com.example.whoismillionaire.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whoismillionaire.R;
import com.example.whoismillionaire.R.drawable;
import com.example.whoismillionaire.model.Question;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView tvNumberQuestion;
    private TextView tvQuestion;
    private Button btAnswerA;
    private Button btAnswerB;
    private Button btAnswerC;
    private Button btAnswerD;
    private TextView tvTimes;
    private TextView tvPoint;

    private int numberQuestion = 1;             // Câu hỏi hiện tại
    private boolean clickHelp5050 = false;      // Trợ giúp 50:50 đã được dùng hay chưa?
    private boolean clickHelpCall = false;      // Trợ giúp Call đã được dùng hay chưa?
    private boolean clickHelpPerson = false;    // Trợ giúp Person đã được dùng hay chưa?
    private boolean clickCall = false;          // Chỉ được chọn 1 người trong 3 người để nhận trợ giúp
    private String answerTrue;                  // Biến lưu giữ đáp án đúng
    private int indexChoose = -1;               // Đáp án User đã chọn
    private boolean isExit = false;             // User không được chơi nữa
    private String[] pointList = {"0", "200", "400", "600", "1,000", "2,000",
            "3,000", "6,000", "10,000", "14,000", "22,000",
            "30,000", "40,000", "60,000", "85,000", "150,000"};   // Tiền thưởng

    private DatabaseQuestion myDatabase;
    private List<Question> listQuestionLv1 = new ArrayList<>();
    private List<Question> listQuestionLv2 = new ArrayList<>();
    private List<Question> listQuestionLv3 = new ArrayList<>();
    private Question currentQuestion;

    private DatabaseHistory historyData;
    private int numberQuestionTrue;
    private int timeLoop;

    MediaPlayer media, mediaClock;
    private List<Integer> listMediaQuestion = new ArrayList<>();
    boolean sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        sound = getIntent().getBooleanExtra("sound", true);
        timeLoop = 0;
        numberQuestionTrue = 0;
        historyData = new DatabaseHistory(this);
        addMediaQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDatabase = new DatabaseQuestion(this);
        listQuestionLv1.addAll(myDatabase.getData(1));
        listQuestionLv2.addAll(myDatabase.getData(2));
        listQuestionLv3.addAll(myDatabase.getData(3));
        newQuestion(numberQuestion);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMedia();
    }

    // Khi kết thúc chương trình thì lưu lại kết quả vào lịch sử
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        int minute = timeLoop/2/60;
        int second = timeLoop/2 - minute*60;
        String seconds;
        if(second < 10) seconds = "0" + second;
        else seconds = "" + second;
        String times = "0" + minute + ":" + seconds;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + calendar.get(Calendar.YEAR);
        historyData.setHistory("history", new String[]{"number_question", "times", "date"},
                new String[]{numberQuestionTrue + "", times, date});
    }

    // Không cho phép User nhấn Back
    @Override
    public void onBackPressed() {
    }

    public void stopMedia() {
        if (media != null) {
            media.stop();
            media.release();
            media = null;
        }
    }

    public void playMedia(int mediaId) {
        stopMedia();
        media = MediaPlayer.create(MainActivity.this, mediaId);
        media.start();
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

    private void deleteTwoAnswerFalse() {
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

    private void helpCall() {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
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
                    dialog.dismiss();
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
                    dialog.dismiss();
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
                    dialog.dismiss();
                    timer.start();
                }
            }
        });
    }

    /*
    Click trợ giúp
        + Khi chọn đáp án rồi thì không được chọn trợ giúp nữa
        + Mỗi trợ giúp chỉ được dùng 1 lần
        + Khi sử dụng trợ giúp thì đồng hồ phải dừng lại
     */
    public void onClickHelp(View view) {
        if (indexChoose == -1) {
            final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            switch (view.getId()) {
                case R.id.bt_Half:
                    if (!clickHelp5050) {
                        dialog.setContentView(R.layout.dialog_question_help);
                        TextView tvQuestionHelp = dialog.findViewById(R.id.tv_question_help);
                        Button btYesHalf = dialog.findViewById(R.id.bt_yesHalf);
                        Button btNoHalf = dialog.findViewById(R.id.bt_noHalf);
                        tvQuestionHelp.setText("Bạn chọn trợ giúp 50:50?");
                        dialog.show();
                        btYesHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                clickHelp5050 = true;
                                timer.pause();
                                if (sound) {
                                    mediaClock.pause();
                                    playMedia(R.raw.sound5050);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            deleteTwoAnswerFalse();
                                        }
                                    }, 3000);
                                    media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            timer.start();
                                        }
                                    });
                                } else {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            deleteTwoAnswerFalse();
                                        }
                                    }, 1000);
                                    timer.start();
                                }
                            }
                        });
                        btNoHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    } else Toast.makeText(MainActivity.this,
                            "Bạn chỉ được sử dụng các sự trợ giúp một lần", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.ibt_Call:
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
                                dialog.dismiss();
                                clickHelpCall = true;
                                if (sound) {
                                    mediaClock.pause();
                                    playMedia(R.raw.sfx_help_call);
                                    media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            helpCall();
                                        }
                                    });
                                } else {
                                    helpCall();
                                }
                            }
                        });
                        btNoHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    } else Toast.makeText(MainActivity.this,
                            "Bạn chỉ được sử dụng các sự trợ giúp một lần", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.ibt_Person:
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
                                dialog.dismiss();
                                clickHelpPerson = true;
                                if (sound) {
                                    mediaClock.pause();
                                    playMedia(R.raw.sound_trogiup_tuvan);
                                    media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mediaPlayer) {
                                            switch (currentQuestion.getAnswer()) {
                                                case 0:
                                                    playMedia(R.raw.trogiup_a);
                                                    break;
                                                case 1:
                                                    playMedia(R.raw.trogiup_b);
                                                    break;
                                                case 2:
                                                    playMedia(R.raw.trogiup_c);
                                                    break;
                                                case 3:
                                                    playMedia(R.raw.trogiup_d);
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
                                } else {
                                    Toast.makeText(MainActivity.this,
                                            "Khán giả trợ giúp cho bạn chọn " + answerTrue, Toast.LENGTH_SHORT).show();
                                    timer.start();
                                }
                            }
                        });
                        btNoHalf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    } else Toast.makeText(MainActivity.this,
                            "Bạn chỉ được sử dụng các sự trợ giúp một lần", Toast.LENGTH_SHORT).show();
                    break;
            }
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
        dialog.setContentView(R.layout.dialog_message);
        dialog.setCanceledOnTouchOutside(false);

        Button btYes = dialog.findViewById(R.id.bt_Yes);
        Button btNo = dialog.findViewById(R.id.bt_No);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        numberQuestion = 1;
                        clickHelp5050 = false;
                        clickHelpCall = false;
                        clickHelpPerson = false;
                        clickCall = false;
                        indexChoose = -1;
                        isExit = false;
                        listQuestionLv1.clear();
                        listQuestionLv2.clear();
                        listQuestionLv3.clear();
                        listQuestionLv1.addAll(myDatabase.getData(1));
                        listQuestionLv2.addAll(myDatabase.getData(2));
                        listQuestionLv3.addAll(myDatabase.getData(3));
                        newQuestion(numberQuestion);
                    }
                }, 1500);
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (sound) {
                    playMedia(R.raw.sound_ket_thuc);
                    media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            startActivity(new Intent(MainActivity.this, MenuActivity.class));
                        }
                    });
                } else startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });
        dialog.show();
    }

    private void showAnswer() {
        if (sound) {
            if (mediaClock.isPlaying()) mediaClock.stop();

            switch (indexChoose) {
                case 0:
                    playMedia(R.raw.ans_a);
                    break;
                case 1:
                    playMedia(R.raw.ans_b);
                    break;
                case 2:
                    playMedia(R.raw.ans_c);
                    break;
                case 3:
                    playMedia(R.raw.ans_d);
                    break;
            }
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playMedia(R.raw.answer_now);
                    media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            switch (currentQuestion.getAnswer()) {
                                case 0:
                                    if (indexChoose == 0) playMedia(R.raw.true_a);
                                    else playMedia(R.raw.lose_a);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btAnswerA.setBackgroundResource(R.drawable.background_answer_true);
                                        }
                                    }, 1000);
                                    break;
                                case 1:
                                    if (indexChoose == 1) {
                                        if (numberQuestion <= 5) playMedia(R.raw.true_b_1);
                                        else playMedia(R.raw.true_b_2);
                                    } else playMedia(R.raw.lose_b);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btAnswerB.setBackgroundResource(R.drawable.background_answer_true);
                                        }
                                    }, 1000);
                                    break;
                                case 2:
                                    if (indexChoose == 2) {
                                        if (numberQuestion <= 5) playMedia(R.raw.true_c_1);
                                        else playMedia(R.raw.true_c_2);
                                    } else playMedia(R.raw.lose_c);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btAnswerC.setBackgroundResource(R.drawable.background_answer_true);
                                        }
                                    }, 1000);
                                    break;
                                case 3:
                                    if (indexChoose == 3) {
                                        if (numberQuestion <= 5) playMedia(R.raw.true_d_1);
                                        else playMedia(R.raw.true_d_2);
                                    } else playMedia(R.raw.lose_d);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            btAnswerD.setBackgroundResource(R.drawable.background_answer_true);
                                        }
                                    }, 1000);
                                    break;
                            }
                            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Nếu chọn đúng thì hiển thị câu hỏi mới, nếu sai thì hiện thông báo
                                            if (indexChoose == currentQuestion.getAnswer() && numberQuestion <= 15) {
                                                if (numberQuestion < 15) {
                                                    numberQuestionTrue++;
                                                    numberQuestion++;
                                                    indexChoose = -1;   // Reset indexChoose

                                                    newQuestion(numberQuestion);
                                                } else
                                                    startActivity(new Intent(MainActivity.this, WinActivity.class));
                                            } else message();
                                        }
                                    }, 1000);
                                }
                            });
                        }
                    });
                }
            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (currentQuestion.getAnswer()) {
                        case 0:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btAnswerA.setBackgroundResource(R.drawable.background_answer_true);
                                }
                            }, 1000);
                            break;
                        case 1:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btAnswerB.setBackgroundResource(R.drawable.background_answer_true);
                                }
                            }, 1000);
                            break;
                        case 2:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btAnswerC.setBackgroundResource(R.drawable.background_answer_true);
                                }
                            }, 1000);
                            break;
                        case 3:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btAnswerD.setBackgroundResource(R.drawable.background_answer_true);
                                }
                            }, 1000);
                            break;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Nếu chọn đúng thì hiển thị câu hỏi mới, nếu sai thì hiện thông báo
                            if (indexChoose == currentQuestion.getAnswer() && numberQuestion <= 15) {
                                if (numberQuestion < 15) {
                                    numberQuestionTrue++;
                                    numberQuestion++;
                                    indexChoose = -1;   // Reset indexChoose
                                    newQuestion(numberQuestion);
                                } else {
                                    Intent intent = new Intent(MainActivity.this, WinActivity.class);
                                    intent.putExtra("sound", sound);
                                    startActivity(intent);
                                }
                            } else message();
                        }
                    }, 3000);
                }
            }, 2000);
        }
    }

    // Bộ đếm thời gian
    private CountDownTimerCustom timer = new CountDownTimerCustom(10600, 500) {
        // Lần đầu chạy thì background đồng hồ sẽ hiện, và âm đồng hồ sẽ được bật
        @Override
        public void onTick(long millisUntilFinished) {
            timeLoop++;
            tvTimes.setBackgroundResource(drawable.custom_times);
            tvTimes.setText(millisUntilFinished / 1000 + "s");
            if (sound && !mediaClock.isPlaying()) mediaClock.start();
            if (indexChoose != -1) {
                timer.cancel();
                tvTimes.setBackgroundResource(0);
                tvTimes.setText("");
            }
        }

        // Nếu User chưa chọn đáp án thì tắt đồng hồ và hiện thông báo
        public void onFinish() {
            tvTimes.setBackgroundResource(0);
            tvTimes.setText("");
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
        if (indexChoose == -1) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_message_when_user_choose_answer);
            dialog.setCanceledOnTouchOutside(false);

            TextView tv = dialog.findViewById(R.id.tv);
            Button btAgree = dialog.findViewById(R.id.bt_agree);
            Button btDisagree = dialog.findViewById(R.id.bt_disagree);

            switch (view.getId()) {
                case R.id.bt_AnswerA:
                    if (btAnswerA.getText() != "" && isExit == false) {
                        if (numberQuestion <= 5) {
                            btAnswerA.setBackgroundResource(R.drawable.background_answer_when_click);
                            indexChoose = 0;
                            showAnswer();
                        } else {
                            tv.setText("Bạn chọn đáp án A?");
                            btAgree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btAnswerA.setBackgroundResource(R.drawable.background_answer_when_click);
                                    dialog.dismiss();
                                    indexChoose = 0;
                                    showAnswer();
                                }
                            });
                            btDisagree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    indexChoose = -1;
                                }
                            });
                            dialog.show();
                        }
                    }
                    break;

                case R.id.bt_AnswerB:
                    if (btAnswerB.getText() != "" && isExit == false) {
                        if (numberQuestion <= 5) {
                            btAnswerB.setBackgroundResource(R.drawable.background_answer_when_click);
                            indexChoose = 1;
                            showAnswer();
                        } else {
                            tv.setText("Bạn chọn đáp án B?");
                            dialog.show();
                            btAgree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btAnswerB.setBackgroundResource(R.drawable.background_answer_when_click);
                                    dialog.dismiss();
                                    indexChoose = 1;
                                    showAnswer();
                                }
                            });
                            btDisagree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    indexChoose = -1;
                                }
                            });
                        }
                    }
                    break;

                case R.id.bt_AnswerC:
                    if (btAnswerC.getText() != "" && isExit == false) {
                        if (numberQuestion <= 5) {
                            btAnswerC.setBackgroundResource(R.drawable.background_answer_when_click);
                            indexChoose = 2;
                            showAnswer();
                        } else {
                            tv.setText("Bạn chọn đáp án C?");
                            dialog.show();
                            btAgree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btAnswerC.setBackgroundResource(R.drawable.background_answer_when_click);
                                    dialog.dismiss();
                                    indexChoose = 2;
                                    showAnswer();
                                }
                            });
                            btDisagree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    indexChoose = -1;
                                }
                            });
                        }
                    }
                    break;

                case R.id.bt_AnswerD:
                    if (btAnswerD.getText() != "" && isExit == false) {
                        if (numberQuestion <= 5) {
                            btAnswerD.setBackgroundResource(R.drawable.background_answer_when_click);
                            indexChoose = 3;
                            showAnswer();
                        } else {
                            tv.setText("Bạn chọn đáp án D?");
                            dialog.show();
                            btAgree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    btAnswerD.setBackgroundResource(R.drawable.background_answer_when_click);
                                    dialog.dismiss();
                                    indexChoose = 3;
                                    showAnswer();
                                }
                            });
                            btDisagree.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    indexChoose = -1;
                                }
                            });
                        }
                    }
                    break;
            }
        }
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

        tvNumberQuestion.setText("Câu số : " + i);
        tvQuestion.setText(currentQuestion.getQuestion());
        btAnswerA.setText("A : " + answers.get(0));
        btAnswerB.setText("B : " + answers.get(1));
        btAnswerC.setText("C : " + answers.get(2));
        btAnswerD.setText("D : " + answers.get(3));

        if (sound) {
            playMedia(listMediaQuestion.get(i - 1));
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaClock = MediaPlayer.create(MainActivity.this, R.raw.eff_clock);
                    timer.cancel();
                    timer.start();
                }
            });
        } else {
            timer.cancel();
            timer.start();
        }
    }

    private void initView() {
        tvNumberQuestion = findViewById(R.id.tv_Number_Question);
        tvQuestion = findViewById(R.id.tv_Question);
        btAnswerA = findViewById(R.id.bt_AnswerA);
        btAnswerB = findViewById(R.id.bt_AnswerB);
        btAnswerC = findViewById(R.id.bt_AnswerC);
        btAnswerD = findViewById(R.id.bt_AnswerD);
        tvTimes = findViewById(R.id.tv_Times);
        tvPoint = findViewById(R.id.tv_Point);
    }
}
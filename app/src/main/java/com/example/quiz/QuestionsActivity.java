package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView counter, question;
    private Button button_1, button_2, button_3, button_4;
    private List<Question> listQuestion;
    int questionCurrent;
    private int score;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        question = findViewById(R.id.question);
        counter = findViewById(R.id.counter);
        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        button_3 = findViewById(R.id.button_3);
        button_4 = findViewById(R.id.button_4);

        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);
        getQuestion();


        score = 0;

    }

    private void getQuestion() {
        listQuestion = new ArrayList<>();
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if(params != null){
            firestore = FirebaseFirestore.getInstance();
            firestore.collection("quiz").document(String.valueOf(params.getString("js"))).
                    collection("questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        QuerySnapshot questions = task.getResult();
                        for(QueryDocumentSnapshot doc: questions){
                            listQuestion.add(new Question(doc.getString("questao"),
                                    doc.getString("a"),
                                    doc.getString("b"),
                                    doc.getString("c"),
                                    doc.getString("d"),
                                   Integer.valueOf(doc.getString("resposta"))));
                        }
                        setQuestion();
                    }
                }
            });
        }


    }

    private void setQuestion() {

        question.setText(listQuestion.get(0).getQuestion());
        button_1.setText(listQuestion.get(0).getOption_a());
        button_2.setText(listQuestion.get(0).getOption_b());
        button_3.setText(listQuestion.get(0).getOption_c());
        button_4.setText(listQuestion.get(0).getOption_d());

        counter.setText(String.valueOf(1) + "/" +String.valueOf(listQuestion.size()));

        questionCurrent = 0;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int selected = 0;

        switch (v.getId()){
            case R.id.button_1:
                selected = 1;
                break;

            case R.id.button_2:
                selected = 2;
                break;

            case R.id.button_3:
                selected = 3;
                break;

            case R.id.button_4:
                selected = 4;
                break;
            default:
                break;
        }

        checkAnswer(selected, v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(int selected, View view) {
        if(selected == listQuestion.get(questionCurrent).getOptions_correct()) {
            //certa
            score++;
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            //errado
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (listQuestion.get(questionCurrent).getOptions_correct()){
                case 1:
                    button_1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

                case 2:
                    button_2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

                case 3:
                    button_3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    button_4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        }, 2000);

    }

    private void changeQuestion() {
        if(questionCurrent < listQuestion.size() - 1){
            questionCurrent++;
            changeQuestionAnimation(question, 0, 0);
            changeQuestionAnimation(button_1, 0, 1);
            changeQuestionAnimation(button_2, 0, 2);
            changeQuestionAnimation(button_3, 0, 3);
            changeQuestionAnimation(button_4, 0, 4);

            counter.setText(String.valueOf(questionCurrent+1) + "/"+String.valueOf(listQuestion.size()));



        } else {
            // FINISH
            Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
            intent.putExtra("SCORE", String.valueOf(score) + "/" + String.valueOf(listQuestion.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            QuestionsActivity.this.finish();
        }


    }

    private void changeQuestionAnimation(final View view, final int value, final int viewNumber) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(800)
                .setStartDelay(200).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(value == 0){
                            switch (viewNumber){
                                case 0:
                                    ((TextView)view).setText(listQuestion.get(questionCurrent).getQuestion());
                                    break;

                                case 1:
                                    ((Button)view).setText(listQuestion.get(questionCurrent).getOption_a());
                                    break;
                                case 2:
                                    ((Button)view).setText(listQuestion.get(questionCurrent).getOption_b());
                                    break;
                                case 3:
                                    ((Button)view).setText(listQuestion.get(questionCurrent).getOption_c());
                                    break;
                                case 4:
                                    ((Button)view).setText(listQuestion.get(questionCurrent).getOption_d());
                                    break;
                            }

                            if(viewNumber != 0){
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6bcdfd")));
                            }

                            changeQuestionAnimation(view, 1, viewNumber);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}

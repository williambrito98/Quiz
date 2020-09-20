package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private Button btn_js;
    private Button btn_html;
    private Button btn_css;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_js = findViewById(R.id.btn_js);
        btn_html = findViewById(R.id.btn_html);
        btn_css = findViewById(R.id.btn_css);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("quiz").document("Categorias").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()){
                                long count = (long)doc.get("count");
                                    btn_js.setText(doc.getString("cat"+ String.valueOf(1)));
                                    btn_css.setText(doc.getString("cat"+ String.valueOf(2)));
                                    btn_html.setText(doc.getString("cat"+ String.valueOf(3)));
                            }
                        } else {

                        }
                    }
                });

        btn_js.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                Bundle params = new Bundle();
                params.putString("js" , String.valueOf(btn_js.getText()));
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        btn_css.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                Bundle params = new Bundle();
                params.putString("js" , String.valueOf(btn_css.getText()));
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        btn_html.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                Bundle params = new Bundle();
                params.putString("js" , String.valueOf(btn_html.getText()));
                intent.putExtras(params);
                startActivity(intent);
            }
        });



    }
}

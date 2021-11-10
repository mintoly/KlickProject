package com.example.KlickApp.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.KlickApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class LoginActivity  extends AppCompatActivity{

    FirebaseAuth auth;
    EditText edit_email, edit_pd;
    Button buttonOK, buttonCreate;
    String email, password, dynamic_url;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        edit_email = findViewById(R.id.editTextTextPersonName);
        edit_pd = findViewById(R.id.editTextTextPassword);
        buttonOK = findViewById(R.id.buttonOK);
        buttonCreate = findViewById(R.id.buttonCreate);

        buttonOK.setOnClickListener((onClickListener)->{
                email = edit_email.getText().toString();
                password = edit_pd.getText().toString();
                if (email.length() == 0) {
                    Toast.makeText(LoginActivity.this, "이메일 입력하기", Toast.LENGTH_LONG).show();
                }
                else if (password.length() == 0) {
                    Toast.makeText(LoginActivity.this, "비밀번호 입력하기", Toast.LENGTH_LONG).show();
                } else {
                    loginAccount();
                }

        });
        buttonCreate.setOnClickListener((onClickListener)->{
                email = edit_email.getText().toString();
                password = edit_pd.getText().toString();
                if (email.length() == 0) {
                    Toast.makeText(LoginActivity.this, "이메일 입력하기", Toast.LENGTH_LONG).show();
                }
                else if (password.length() == 0) {
                    Toast.makeText(LoginActivity.this, "비밀번호 입력하기", Toast.LENGTH_LONG).show();
                } else {
                    createAccount();
                }
        });
        auth = FirebaseAuth.getInstance();
        dynamic_url = "KlickApp.page.link";

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private void makeEmptyAccount() {

        auth.signInAnonymously()
                .addOnFailureListener(this, e -> Log.d(TAG, e.getMessage()))
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                    }
                });


    }

    private void linkEmail() {
        getUser().linkWithCredential(EmailAuthProvider.getCredential(email, password))
                .addOnCompleteListener(task -> Log.d(TAG, "linkComplete"));

    }

    private void createAccount() {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        getUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "정상", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "이메일설정실패", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, "이메일전송실패", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void loginAccount() {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "로그인성공", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "로그인실패", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}

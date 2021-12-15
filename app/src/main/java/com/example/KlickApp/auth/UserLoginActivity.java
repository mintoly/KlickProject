package com.example.KlickApp.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.KlickApp.MainActivity;
import com.example.KlickApp.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;
import com.example.KlickApp.databinding.UserLoginBinding;

public class UserLoginActivity extends AppCompatActivity{

    private int LOGIN_CODE = 9000;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private UserLoginBinding binding;
    private String email, password, dynamic_url;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        binding.btnLogin.setOnClickListener((onClickListener)->{
                Log.d(TAG, "OK버튼 클릭");
                email = binding.editId.getText().toString().trim();
                password = binding.editPd.getText().toString().trim();

                if (email.length() == 0) {
                    Log.d(TAG, "이메일 입력하기");
                    Toast.makeText(UserLoginActivity.this.getApplicationContext(), "이메일 입력하기", Toast.LENGTH_LONG).show();
                }
                else if (password.length() == 0) {
                    Log.d(TAG, "비밀번호 입력하기");
                    Toast.makeText(UserLoginActivity.this.getApplicationContext(), "비밀번호 입력하기", Toast.LENGTH_LONG).show();
                } else {
                    loginAccount();
                }
        });
        binding.btnCreate.setOnClickListener((onClickListener)->{
                Log.d(TAG, "Create버튼 클릭");
                /*Intent intent = new Intent(this, UserRegisterActivity.class);
                startActivity(intent);*/

                email = binding.editId.getText().toString().trim();
                password = binding.editPd.getText().toString().trim();
                if (email.length() == 0) {
                    Toast.makeText(UserLoginActivity.this, "이메일 입력하기", Toast.LENGTH_LONG).show();
                }
                else if (password.length() == 0) {
                    Toast.makeText(UserLoginActivity.this, "비밀번호 입력하기", Toast.LENGTH_LONG).show();
                } else {
                    createAccount();
                }
        });

        binding.textChangePd.setOnClickListener((onClickListner)->{
            email = binding.editId.getText().toString();
            if (email.length() == 0) {
                Toast.makeText(UserLoginActivity.this, "재설정할 이메일 입력하기", Toast.LENGTH_LONG).show();
            } else {
                changePassword();
            }

        });

        dynamic_url = "KlickApp.page.link";
        /*
        Button button = findViewById(R.id.button);
        button.setOnClickListener(onclicklistner->{
            try {
                Toast.makeText(LoginActivity.this, "Wht?", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.w(TAG, e.getMessage());
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {

        }
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/


    }
    /*
    private void sginIn() {
        Intent sginInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(sginInIntent, LOGIN_CODE);
    }*/

    private void makeEmptyAccount() {

        mAuth.signInAnonymously()
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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        getUser().sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "정상");
                                        Toast.makeText(UserLoginActivity.this, "프사및 이름 설정", Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.d(TAG, "설정실패");
                                        Toast.makeText(UserLoginActivity.this, "이메일설정실패", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Log.w(TAG, "전송실패", task.getException());
                        Toast.makeText(UserLoginActivity.this, "이메일전송실패", Toast.LENGTH_LONG).show();

                    }
                });
    }

    private void loginAccount() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        if (mUser.isEmailVerified()) {
                            Log.w(TAG, "로그인성공", task.getException());
                            Toast.makeText(UserLoginActivity.this, "로그인성공", Toast.LENGTH_LONG).show();
                            if (task.getResult().getUser().getPhotoUrl() == null) {
                                startActivity(new Intent(this, UserRegisterActivity.class));
                            } else {
                                startActivity(new Intent(this, MainActivity.class));
                            }
                            finish();
                        } else {
                            Log.w(TAG, "이메일인증필요");
                            Toast.makeText(UserLoginActivity.this, "이메일을 인증해주세요", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.w(TAG, "로그인실패", task.getException());
                        Toast.makeText(UserLoginActivity.this, "로그인실패", Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void changePassword() {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(UserLoginActivity.this, "재설정 이메일 확인", Toast.LENGTH_LONG).show();
                    }
        })
                .addOnFailureListener(exception -> {
                    Toast.makeText(UserLoginActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
                })
        ;
    }

    private FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}

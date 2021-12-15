package com.example.KlickApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.KlickApp.ui.resultpage.ResultActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.KlickApp.databinding.ActivityMainBinding;

import java.io.InputStream;

import static android.content.ContentValues.TAG;
import static com.example.KlickApp.ui.userpage.UserFragment.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    class ButtonOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            String type = "";
            switch (v.getId()) {
                case R.id.testButton:
                    Toast.makeText(MainActivity.this, "제발좀돼라", Toast.LENGTH_SHORT).show();
                    return;
            }
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_event, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        ButtonOnClickListner buttonOnClickListner = new ButtonOnClickListner();

        binding.testButton.setOnClickListener(buttonOnClickListner);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    ((ImageView)findViewById(R.id.img_register)).setImageBitmap(img);


                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }
    }




}
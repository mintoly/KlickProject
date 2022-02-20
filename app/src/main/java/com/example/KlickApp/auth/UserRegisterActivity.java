package com.example.KlickApp.auth;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderKt;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.KlickApp.databinding.ActivityRegisterBinding;

import com.example.KlickApp.R;

public class UserRegisterActivity extends AppCompatActivity {
    //TODO: 필요 구간 명확히 해서 다시 짤것
    private AppBarConfiguration appBarConfiguration;
    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    class RegisterViewModel extends ViewModel{
        private String email;
        private String pd;
        private Uri uri;
        private String name;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_register);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        /*
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_register);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String link = "user_photo/" + user.getUid() + ".jpg";
                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://klicker-ccdd8.appspot.com").child(link);
                    Task<Uri> uriTask =  ref.putStream(in)
                            .continueWithTask(task -> {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return ref.getDownloadUrl();
                            })
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Uri downUri = task.getResult();
                                    ((ImageView)findViewById(R.id.img_register)).setImageURI(downUri);
                                }
                            });
                    /*
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    ((ImageView)findViewById(R.id.img_register)).setImageBitmap(img);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    byte[] outputData = outputStream.toByteArray();

                    ;
                    ref.putBytes(outputData)
                            .addOnFailureListener((exception)-> {
                                Log.d(TAG, exception.getMessage());
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            })
                            .addOnSuccessListener(taskSnapshot -> {
                                Url url1 = taskSnapshot.getMetadata().get
                            });



                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }
    }*/
}
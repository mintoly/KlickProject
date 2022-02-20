package com.example.KlickApp.auth.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.KlickApp.MainActivity;
import com.example.KlickApp.R;
import com.example.KlickApp.databinding.FragmentRegisterNameBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class NameFragment extends Fragment {

    private FragmentRegisterNameBinding binding;
    private FirebaseUser user;
    private Uri uri;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRegisterNameBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(NameFragment.this)
                        .navigate(R.id.action_register_name_to_img);
            }
        });
        binding.buttonEnd.setOnClickListener((view1)->{
            String name = binding.editName.getText().toString().trim();
            if (name.length() == 0) {
                Toast.makeText(getContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {

                getParentFragmentManager().setFragmentResultListener("imageuri", this, (requestKey, result) -> {

                            uri = Uri.parse(result.getString("uri"));
                    }
                );


                InputStream in;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String link = "user_photo/" + user.getUid() + ".jpg";
                StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl("gs://klicker-ccdd8.appspot.com").child(link);
                try{
                    in = getActivity().getContentResolver().openInputStream(uri);


                    Task<Uri> uriTask =  ref.putStream(in)
                            .continueWithTask(task -> {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return ref.getDownloadUrl();
                            })
                            .addOnFailureListener((exception)->{
                                Log.d(TAG, "파일 업로드 실패 " + exception.toString());
                            })
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Uri downUri = task.getResult();
                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(downUri)
                                            .setDisplayName(name)
                                            .build();

                                    user.updateProfile(profileChangeRequest)
                                            .addOnFailureListener((exception)->{
                                                Log.d(TAG, "프로필 전송 실패 " + exception.toString());
                                            })
                                            .addOnCompleteListener((task1 -> {
                                                if (task1.isSuccessful()) {

                                                    getActivity().finish();
                                                } else {
                                                    Log.d(TAG, "프로필 업뎃 실패 " + task1.getException().toString());
                                                }
                                            }));

                                }
                            });


                } catch (Exception e) {
                    //TODO: exception
                    Log.d(TAG, "uri 가져오는 중 실패 " + e.toString() + " uri: " + uri);
                }

                Toast.makeText(getContext(), "환영합니다.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(view1.getContext(), MainActivity.class));
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
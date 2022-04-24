package com.example.KlickApp.ui.userpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.KlickApp.databinding.FragmentUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;


public class UserFragment extends Fragment {

    private UserViewModel notificationsViewModel;
    private FragmentUserBinding binding;
    private FirebaseUser user;
    public static final int REQUEST_CODE = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textUserInst;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imgUser.setOnClickListener((view1)->{

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_CODE);
        });
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            binding.btnEmailVerify.setVisibility(View.INVISIBLE);
        } else {
            binding.btnEmailVerify.setOnClickListener(v -> {
                user.sendEmailVerification();
                Toast.makeText(getContext(), "이메일을 재전송했습니다. 메일함을 확인해주세요.", Toast.LENGTH_LONG).show();
            });
        }
        //TODO:  imageuri 고치기
        binding.textEmail.setText(user.getEmail());
        if (user.getDisplayName() == null) binding.textName.setText("공백");
        else binding.textName.setText(user.getDisplayName());

        if (user.getPhotoUrl() != null) {
            Glide.with(getContext())
                    .load(user.getPhotoUrl())
                    .override(400, 400)
                    .into(binding.imgUser);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.KlickApp.auth.User;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.KlickApp.R;
import com.example.KlickApp.databinding.FragmentRegisterEmailVerifyBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class EmailVerifyFragment extends Fragment {
    FragmentRegisterEmailVerifyBinding binding;
    FirebaseUser mUser;
    /* This fragment has deparcreted!
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentRegisterEmailVerifyBinding.inflate(inflater, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textRegisterEmailVerifyResend.setOnClickListener(v -> {
            mUser.sendEmailVerification();
            Toast.makeText(getContext(), "Email Resended. Please check your mailbox.", Toast.LENGTH_LONG).show();
        });

        binding.buttonRegisterEmailVerify.setOnClickListener(v -> {
            if (!mUser.isEmailVerified()) {
                Toast.makeText(getContext(), "Email was not verified. Please check your mailbox.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Email verified!", Toast.LENGTH_LONG).show();
                mUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(mUser.getEmail()).build())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                NavHostFragment.findNavController(EmailVerifyFragment.this)
                                        .navigate(R.id.action_register_ver_to_img);
                            } else {
                                Log.d(TAG, task.getException().toString());
                                Toast.makeText(getContext(), "There is an error. Please retry later.", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

        binding.buttonRegisterEmailVerifyPrev.setOnClickListener(v -> {
            NavHostFragment.findNavController(EmailVerifyFragment.this)
                    .navigate(R.id.action_register_ver_to_id);
        });
    }*/
}

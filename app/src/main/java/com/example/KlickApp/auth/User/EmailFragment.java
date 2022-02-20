package com.example.KlickApp.auth.User;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.KlickApp.R;
import com.example.KlickApp.databinding.FragmentRegisterEmailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static android.content.ContentValues.TAG;

public class EmailFragment extends Fragment {
    @Nullable
    @org.jetbrains.annotations.Nullable
    public static final int REQUEST_CODE = 0;
    private FragmentRegisterEmailBinding binding;
    private String email;
    private String pd;
    private String pdcheck;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRegisterEmailBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegisterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = binding.editRegisterEmail.getText().toString();
                pd = binding.editRegisterPd.getText().toString();
                pdcheck = binding.editRegisterPdCheck.getText().toString();
                if (email.length() == 0 ) {
                    Toast.makeText(getContext(), "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else if (pd.length() == 0) {
                    Toast.makeText(getContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pdcheck.length() == 0) {
                    Toast.makeText(getContext(), "비밀번호를 재입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!(pd.trim().equals( pdcheck.trim()))) {
                    Log.d(TAG, binding.editRegisterPd.getText().toString() +" : " + binding.editRegisterPdCheck.getText().toString());
                    Toast.makeText(getContext(), "재입력하신 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

                } else {

                    //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    email = email.trim();
                    pd = pd.trim();
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, pd)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    //Verification moved to user fragment
                                    //mAuth.getCurrentUser().sendEmailVerification();
                                    mUser = mAuth.getCurrentUser();
                                    mUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(mUser.getEmail()).build());
                                    NavHostFragment.findNavController(EmailFragment.this)
                                            .navigate(R.id.action_register_id_to_ver);
                                } else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        Toast.makeText(getContext(), e.getReason(), Toast.LENGTH_LONG).show();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Toast.makeText(getContext(), "email is invalid", Toast.LENGTH_LONG).show();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Toast.makeText(getContext(), email + " was already exists! Please Login first.", Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Log.d(TAG, e.toString());
                                    }
                                    Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                }
                            });

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

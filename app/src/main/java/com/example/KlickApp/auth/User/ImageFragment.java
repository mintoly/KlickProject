package com.example.KlickApp.auth.User;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.KlickApp.R;
import com.example.KlickApp.databinding.FragmentRegisterImgBinding;
import com.example.KlickApp.ui.resultpage.ResultActivity;
import com.google.android.gms.common.SignInButton;

import static android.app.Activity.RESULT_OK;

public class ImageFragment extends Fragment {

    public static final int REQUEST_CODE = 0;
    private FragmentRegisterImgBinding binding;
    private Uri uri;
    String[] permission_list = {
            Manifest.permission.WRITE_CONTACTS
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRegisterImgBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri == null) {
                    Toast.makeText(getContext(), "사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "멋진 사진이네요!", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("uri", uri.toString());
                    getParentFragmentManager().setFragmentResult("imageuri", bundle);
                    NavHostFragment.findNavController(ImageFragment.this)
                            .navigate(R.id.action_register_img_to_name);
                }
            }
        });
        binding.imgRegister.setOnClickListener((view1)->{
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( getActivity(), new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 0 );
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);
            }
        });
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    uri = result.getData().getData();
                    Glide.with(getActivity()).load(uri).into(binding.imgRegister);


                }
    });

    public Uri getUri() {
        return uri;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}
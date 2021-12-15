package com.example.KlickApp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.KlickApp.R;
import com.example.KlickApp.databinding.FragmentHomeBinding;
import com.example.KlickApp.ui.resultpage.ResultActivity;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        binding.btnFood.setOnClickListener(this);
        binding.btnMovie.setOnClickListener(this);
        binding.btnStay.setOnClickListener(this);

        binding.viewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), ResultActivity.class);
                intent.putExtra("name", query.trim());
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return root;
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        String text = "";
        switch (v.getId()) {
            case R.id.btn_food:
                text = getString(R.string.store_type_food);
                break;
            case R.id.btn_movie:
                text = getString(R.string.store_type_movie);
                break;
            case R.id.btn_stay:
                text = getString(R.string.store_type_stay);
                break;

        }
        intent.putExtra("type", text);
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
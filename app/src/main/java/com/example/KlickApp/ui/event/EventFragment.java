package com.example.KlickApp.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.KlickApp.databinding.EventPageBinding;

import org.jetbrains.annotations.NotNull;


public class EventFragment extends Fragment {

    private EventPageBinding binding;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = EventPageBinding.inflate(getLayoutInflater());


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

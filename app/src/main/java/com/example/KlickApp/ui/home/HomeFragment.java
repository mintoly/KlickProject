package com.example.KlickApp.ui.home;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.KlickApp.R;
import com.example.KlickApp.databinding.FragmentHomeBinding;
import com.example.KlickApp.ui.deal.DealAdaptor;
import com.example.KlickApp.ui.deal.DealItem;
import com.example.KlickApp.ui.home.event.BannerAdaptor;
import com.example.KlickApp.ui.resultpage.ResultActivity;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private BannerAdaptor bannerAdaptor;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        //이하 목차 목록

        binding.btnFood.setOnClickListener(this);
        binding.btnMovie.setOnClickListener(this);
        //binding.btnStay.setOnClickListener(this);

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

        //이하 banner - banner image 가져오기 구현

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        ArrayList<StorageReference> bannerUrlList = new ArrayList<>();
        bannerAdaptor = new BannerAdaptor(bannerUrlList);
        db.collection("event")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "event collection accessed!");
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Log.d(TAG, snapshot.getData().toString());
                            Log.d(TAG, "The gs Link is : " + snapshot.getData().get("banner").toString());
                            StorageReference ref = storage.getReferenceFromUrl(snapshot.getData().get("banner").toString());
                            bannerUrlList.add(ref);

                        }
                        Log.d(TAG, bannerUrlList.toString());
                        bannerAdaptor.setBannerUrlList(bannerUrlList);
                        bannerAdaptor.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "event collection error!" + task.getException().toString());
                    }
                });

        Log.d(TAG, "bannerUri? : " + bannerUrlList.toString());
        binding.viewHomeEventBanner.setAdapter(bannerAdaptor);

        //Today's Deal adaptor
        ArrayList<DealItem> dealItems = new ArrayList<>();
        DealAdaptor dealAdaptor = new DealAdaptor(dealItems);

        db.collection("deal")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Log.d(TAG, "deal result : " + snapshot.getData().toString());
                            String storeId = snapshot.getData().get("store_id").toString();

                            db.collection("store")
                                    .document(storeId)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            if (task1.getResult().exists()) {
                                                Log.d(TAG, "store result : " + task1.getResult().getData());
                                                Double discount;
                                                if (snapshot.getData().get("discount").getClass().getSimpleName().equals("Long")) {
                                                    discount = ((Long)snapshot.getData().get("discount")).doubleValue();
                                                } else {
                                                    discount = (Double)snapshot.getData().get("discount");
                                                }
                                                DealItem dealItem = new DealItem(
                                                        storage.getReferenceFromUrl(task1.getResult().getData().get("image_link").toString()),
                                                        task1.getResult().getData().get("name").toString(),
                                                        discount,
                                                        (Timestamp) snapshot.get("end"),
                                                        new Double(0)
                                                );
                                                dealItems.add(dealItem);
                                                dealAdaptor.notifyDataSetChanged();
                                            } else {
                                                Log.d(TAG, "Document id error!");
                                            }
                                        }
                                    });
                        }
                    }
                });

        binding.viewHomeTodaysDeal.setAdapter(dealAdaptor);
        binding.viewHomeTodaysDeal.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //binding.viewHomeTodaysDeal.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.viewHomeTodaysDeal.setAdapter(dealAdaptor);

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
            /*case R.id.btn_stay:
                text = getString(R.string.store_type_stay);
                break;*/

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
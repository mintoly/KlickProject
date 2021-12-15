package com.example.KlickApp.ui.searchpage;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.ListView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.KlickApp.R;

public class SearchActivity extends AppCompatActivity {
    ListView searchView = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        SearchAdaptor adaptor;

        adaptor = new SearchAdaptor();

        searchView.setAdapter(adaptor);

        adaptor.itemRefresh();

    }
}

package com.example.KlickApp.ui.searchpage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.KlickApp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SearchAdaptor extends BaseAdapter implements Filterable {

    private ArrayList<SearchItem> searchItemList = new ArrayList<SearchItem>();
    private ArrayList<SearchItem> showItemList = searchItemList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Filter searchFilter;
    @Override
    public int getCount() {
        return showItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return showItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.result_item, parent, false);
        }

        //ImageView iconView = (ImageView ) convertView.findViewById(R.id.itemImage);
        TextView titleView = (TextView) convertView.findViewById(R.id.text_item_title);
        TextView contentView = (TextView) convertView.findViewById(R.id.text_item_content);

        SearchItem item = showItemList.get(position);

        //iconView.setImageDrawable(item.getIcon());
        titleView.setText(item.getTitle());
        contentView.setText(item.getContent());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (searchFilter == null) {
            searchFilter = new SearchFilter();
        }
        return searchFilter;
    }

    public void itemRefresh() {

        db.collection("seller")
                .whereEqualTo("name", "클릭치킨")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "Document is" + document.getData() );

                        }
                    } else {
                        Log.d(TAG, "document Error!", task.getException());
                    }
                });
    };



    private class SearchFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = searchItemList;
                results.count = searchItemList.size();
            } else {
                ArrayList<SearchItem> returnList = new ArrayList<SearchItem>();

                for (SearchItem item : searchItemList) {
                    if (item.getTitle().contains(constraint.toString()) || item.getContent().contains(constraint.toString())) {
                        returnList.add(item);
                    }
                }

                results.values = returnList;
                results.count = returnList.size();
            }


            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            showItemList = (ArrayList<SearchItem>)results.values;

            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                notifyDataSetChanged();
            }
        }


    }
}

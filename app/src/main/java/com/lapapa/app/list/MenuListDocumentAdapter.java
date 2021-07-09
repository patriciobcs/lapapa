package com.lapapa.app.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lapapa.app.R;

import java.util.ArrayList;
import java.util.List;


public class MenuListDocumentAdapter extends ArrayAdapter<ListDocumentItem> {
    private Context context;
    private List<ListDocumentItem> itemList = new ArrayList<>();

    public MenuListDocumentAdapter(@NonNull Context context, ArrayList<ListDocumentItem> list) {
        super(context, 0, list);
        this.context = context;
        this.itemList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.menu_item_document, parent, false);
        }
        ListDocumentItem li = itemList.get(position);

        TextView tv_name = (TextView) listItem.findViewById(R.id.menu_item_name);
        tv_name.setText(li.getName());
        TextView tv_date = (TextView) listItem.findViewById(R.id.menu_item_date);
        tv_date.setText(li.getDate());

        return listItem;
    }
}

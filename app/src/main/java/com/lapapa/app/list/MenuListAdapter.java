package com.lapapa.app.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lapapa.app.R;

import java.util.ArrayList;
import java.util.List;


public class MenuListAdapter extends ArrayAdapter<ListItem> {
    private Context context;
    private List<ListItem> itemList = new ArrayList<>();

    public MenuListAdapter(@NonNull Context context, ArrayList<ListItem> list) {
        super(context, 0, list);
        this.context = context;
        this.itemList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        }
        ListItem li = itemList.get(position);

        ImageView img = (ImageView) listItem.findViewById(R.id.menu_item_img);
        img.setImageResource(li.getIcon());

        TextView tv = (TextView) listItem.findViewById(R.id.menu_item_text);
        tv.setText(li.getDescription());

        return listItem;
    }
}

package com.lapapa.app.list;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
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
import com.lapapa.app.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.lapapa.app.main.MainActivity.MODIFIER;


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

        findViews(listItem);

        return listItem;
    }

    public void findViews(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    // recursively call this method
                    findViews(child);
                }
            } else if (v instanceof TextView) {
                //do whatever you want ...
                String textSize = getContext().getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).getString("text_size", "0");
                int textModifier = Integer.parseInt(textSize)*MODIFIER;
                TextView tv = ((TextView)v);
                float sp = tv.getTextSize() / getContext().getResources().getDisplayMetrics().scaledDensity;
                Log.d("findViews", "Found TV with text "+ tv.getText() + " and text size " + sp);
                Log.d("findViews", "Adding up "+ textModifier);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,sp + textModifier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

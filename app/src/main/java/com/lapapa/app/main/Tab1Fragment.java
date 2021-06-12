package com.lapapa.app.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lapapa.app.list.ListItem;
import com.lapapa.app.list.MenuListAdapter;
import com.lapapa.app.new_permit.NewPermitMenuActivity;
import com.lapapa.app.R;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment {
    String[] OPTIONS = {"Permiso Individual de Desplazamiento General","Otros Trámites", "Blablabla"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_tab_fragment, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.main_menu_lv);
        //ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), R.layout.menu_item_simple, OPTIONS);
        ArrayList<ListItem> optionsList = new ArrayList<>();
        for (String op: OPTIONS){
            optionsList.add(new ListItem(R.mipmap.ic_launcher, op));
        }
        MenuListAdapter adapter = new MenuListAdapter(this.getActivity(), optionsList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent proceedToDataOptions = new Intent(getActivity(), NewPermitMenuActivity.class);
                startActivity(proceedToDataOptions);
            }
        });

        return rootView;
    }

}

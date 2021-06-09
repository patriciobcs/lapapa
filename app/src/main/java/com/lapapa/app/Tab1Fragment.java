package com.lapapa.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Tab1Fragment extends Fragment {
    String[] OPTIONS = {"Permiso Individual de Desplazamiento General","Otros Trámites", "Blablabla"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_tab_fragment, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.main_menu_lv);
        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), R.layout.menu_item_simple, OPTIONS);
        lv.setAdapter(adapter);

        return rootView;
    }

}

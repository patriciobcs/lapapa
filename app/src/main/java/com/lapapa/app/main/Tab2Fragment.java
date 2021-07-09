package com.lapapa.app.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.lapapa.app.BuildConfig;
import com.lapapa.app.R;
import com.lapapa.app.list.ListDocumentItem;
import com.lapapa.app.list.ListItem;
import com.lapapa.app.list.MenuListAdapter;
import com.lapapa.app.list.MenuListDocumentAdapter;
import com.lapapa.app.new_permit.NewPermitMenuActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Tab2Fragment extends Fragment {
    public static final String LOGTAG = "Tab2Fragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_tab_fragment, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.main_menu_lv);
        ArrayList<ListDocumentItem> documents = new ArrayList<>();
        String dirPath = getContext().getExternalFilesDir(null) + "";
        Log.d(LOGTAG, "Opening Directory ["+dirPath+"]");

        File directory = new File(dirPath);
        File [] files = directory.listFiles();
        Log.d(LOGTAG, "Found ["+files.length+"] Files");

        for (int i = 0; i < files.length; i++)
        {
            String fileName = files[i].getName();
            Log.d(LOGTAG, "FileName:" + fileName);
            //Checking if file is PDF
            boolean isFilePDF = fileName.endsWith(".pdf");
            if(!isFilePDF){
                continue;
            }
            //Finding Document Date and Type in name
            String[] fileNameParts = fileName.substring(0, fileName.length()-4).split(" ");
            if(fileNameParts.length != 3){
                continue;
            }
            Log.d(LOGTAG, "FileName Part 0:"+ fileNameParts[0]);
            String name = "Desconocido";
            String date = fileNameParts[1] + " " + fileNameParts[2];
            if(fileNameParts[0].equals("DES")){
                name = "Permiso de Desplazamiento Individual";
            }
            documents.add(new ListDocumentItem(name, date, files[i]));
        }

        MenuListDocumentAdapter adapter = new MenuListDocumentAdapter(this.getActivity(), documents);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewPDF = new Intent(Intent.ACTION_VIEW);
                File file = ((ListDocumentItem)parent.getItemAtPosition(position)).getFile();
                Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                Log.d("OnClickItem", "Opening File:" + uri);
                viewPDF.setDataAndType(uri, "application/pdf");
                viewPDF.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                viewPDF.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(viewPDF);
            }
        });

        //TODO DOESNT WORK
        //UPDATE ALL TEXT TO CORRESPOND WITH CONFIGURATION
        if(getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).findViews(rootView);
        }

        return rootView;
    }
}

package com.lapapa.app.list;

import android.net.Uri;

import java.io.File;

public class ListDocumentItem {
    private String name;
    private String date;
    private File file;

    public ListDocumentItem(String name, String date, File file){
        this.name = name;
        this.date = date;
        this.file = file;
    }
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}
    public String getDate(){return this.date;}
    public void setDate(String date){this.date = date;}
    public File getFile(){return this.file;}
    public void setFile(File file){this.file = file;}
}

package com.lapapa.app.get_permit_v2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.snackbar.Snackbar;
import com.lapapa.app.R;
import com.lapapa.app.databinding.ActivityMainBinding;
import com.lapapa.app.databinding.FragmentSecondBinding;
import com.lapapa.app.main.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetPermitActivity extends AppCompatActivity {

    private DownloadManager dm;
    private static final String TAG = "GetPermitActivityV2";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_permit);

        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        String data;
        SharedPreferences sharedPref = getSharedPreferences("com.lapapa.app_preferences", Context.MODE_PRIVATE);
        data = "{"
                + "name: '" + sharedPref.getString("name", "Javier Mendoza") + "',"
                + "rut: '" + sharedPref.getString("rut", "20014544-5") + "',"
                + "code: '" + sharedPref.getString("code", "110495453") + "',"
                + "age: '" + sharedPref.getString("age", "22") + "',"
                + "region: '" + sharedPref.getString("region", "Valparaíso") + "',"
                + "comuna: '" + sharedPref.getString("comuna", "Quilpué") + "',"
                + "address: '" + sharedPref.getString("address", "Pasaje Totoralillo 752") + "',"
                + "destino: '" + sharedPref.getString("destino", "Tramites") + "',"
                + "email: '" + sharedPref.getString("email", "javier.mendoza@sansano.usm.cl") + "'}";


        final String urlDisplacementPage = "https://comisariavirtual.cl/tramites/iniciar/135.html";
        final String jsDisplacementPage = getJS("DisplacementPage.js") + "main("+ data + ");";

        final String urlGetDocumentPage = "https://comisariavirtual.cl/tramites/pdf/index.html";
        final String jsGetDocumentPage = getJS("GetDocumentPage.js");
        Log.d(TAG, jsDisplacementPage.substring(jsDisplacementPage.length()- 200, jsDisplacementPage.length()));

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(urlDisplacementPage);
        webView.getSettings().setLightTouchEnabled(true);
        webView.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView loadedWebView, String url) {
                Log.d("URL", url);
                if (Build.VERSION.SDK_INT >= 19) {
                    if (url.contains(urlDisplacementPage)) {
                        loadedWebView.evaluateJavascript(jsDisplacementPage, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                            }
                        });
                    } else if (url.contains(urlGetDocumentPage)) {
                        loadedWebView.evaluateJavascript(jsGetDocumentPage, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                            }
                        });

                    }
                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage)
            {
                String message = consoleMessage.message();
                Log.d("Console", message);
                String documentURLLabel = "DOCUMENT-URL ";
                if (message.contains(documentURLLabel)) {
                    downloadFile(message.replace(documentURLLabel, ""), "DES");
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

    }

    public String ReadTextFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
        String line = reader.readLine();
        StringBuilder fullText = new StringBuilder();
        while(line != null){
            fullText.append(line);
            fullText.append(" ");
            line = reader.readLine();
        }
        return fullText.toString();
    }

    public String getJS(String filename) {
        try {
            return "javascript:" + ReadTextFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void downloadFile(String url, String identificator) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url cannot be empty or null");
        }

        com.lapapa.app.get_permit_v2.GetPermitActivity self = this;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        if (isExternalStorageWritable()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();
            String timestamp = formatter.format(date);
            String filename = identificator + " " + timestamp + ".pdf";

            //Downloads Folder
            //request.setTitle(filename);
            //request.setMimeType("application/pdf");
            //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

            //External App Folder
            String uriString = getExternalFilesDir(null) + "";
            File file = new File(uriString, filename);
            Log.d("File", file.getPath());
            Uri destinationUri = Uri.fromFile(file);
            request.setDestinationUri(destinationUri);
            dm.enqueue(request);
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctx, Intent intent) {
                    try {
                        Navigation.findNavController(self, R.id.nav_host_fragment_content_main)
                                .navigate(R.id.action_SecondFragment_to_FirstFragment);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            };
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
        Intent goBackToMenu =  new Intent(com.lapapa.app.get_permit_v2.GetPermitActivity.this, MainActivity.class);
        goBackToMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goBackToMenu);
    }

    // Possible Utilization CA
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_D:
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cancelar")
                .setMessage("¿Está seguro que quiere cancelar la descarga del permiso?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goBackToMenu =  new Intent(com.lapapa.app.get_permit_v2.GetPermitActivity.this, MainActivity.class);
                        goBackToMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goBackToMenu);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
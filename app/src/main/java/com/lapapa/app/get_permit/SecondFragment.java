package com.lapapa.app.get_permit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.lapapa.app.R;
import com.lapapa.app.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    WebView webView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        GetPermitActivity getPermitActivity = ((GetPermitActivity) getActivity());
        String data;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.lapapa.app_preferences", Context.MODE_PRIVATE);
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
        final String jsDisplacementPage = getPermitActivity.getJS("DisplacementPage.js") + "main("+ data + ");";;

        final String urlGetDocumentPage = "https://comisariavirtual.cl/tramites/pdf/index.html";
        final String jsGetDocumentPage = getPermitActivity.getJS("GetDocumentPage.js");
        Log.d("SecondFragment", jsDisplacementPage.substring(jsDisplacementPage.length()- 200, jsDisplacementPage.length()));
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        webView = (WebView) view.findViewById(R.id.webview);
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
                    getPermitActivity.downloadFile(message.replace(documentURLLabel, ""), "DES");
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
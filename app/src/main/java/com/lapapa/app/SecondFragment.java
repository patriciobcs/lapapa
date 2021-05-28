package com.lapapa.app;

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

import com.lapapa.app.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    WebView webView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        MainActivity mainActivity = ((MainActivity) getActivity());

        final String urlEntryPage = "https://comisariavirtual.cl/tramites/iniciar/135.html";
        final String jsEntryPage = mainActivity.getJS("scripts.js");

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        webView = (WebView) view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(urlEntryPage);
        webView.getSettings().setLightTouchEnabled(true);
        webView.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView loadedWebView, String url) {
                Log.d("URL", url);
                if (Build.VERSION.SDK_INT >= 19) {
                    if (url.contains(urlEntryPage)) {
                        loadedWebView.evaluateJavascript(jsEntryPage, new ValueCallback<String>() {
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
                Log.d("Console", String.valueOf(message));
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
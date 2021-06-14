package com.lapapa.app.firstOpen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lapapa.app.R;


import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class StartConfFragmentA extends Fragment {

    private int step;

    public StartConfFragmentA(int step) {
        this.step = step;
    }

    public int getStep(){
        return this.step;
    }
    public void setStep(int step){
        this.step = step;
    }

    @NotNull
    @Override
    public String toString() {
        return "ConfFragmentA";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.start_conf_fragment_a, container, false);
        Button btnYes = rootView.findViewById(R.id.start_btn_yes);
        Button btnNo = rootView.findViewById(R.id.start_btn_no);
        btnYes.setOnClickListener(v -> {
            getActivity().getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).edit().putBoolean("tts", true).apply();
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            Fragment nextFragment = new StartConfFragmentB(getStep() + 1);
            fragmentTransaction.replace(R.id.container, nextFragment, nextFragment.toString());
            fragmentTransaction.addToBackStack(nextFragment.toString());
            fragmentTransaction.commit();
        });
        btnNo.setOnClickListener(v -> {
            getActivity().getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).edit().putBoolean("tts", false).apply();
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            Fragment nextFragment = new StartConfFragmentB(getStep() + 1);
            fragmentTransaction.replace(R.id.container, nextFragment, nextFragment.toString());
            fragmentTransaction.addToBackStack(nextFragment.toString());
            fragmentTransaction.commit();
        });
        return rootView;
    }
}
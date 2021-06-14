package com.lapapa.app.firstOpen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.lapapa.app.R;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class StartConfFragmentB extends Fragment {

    private int step;

    public StartConfFragmentB(int step) {
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
        return "ConfFragmentB";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.start_conf_fragment_b, container, false);
        Button btnForm = rootView.findViewById(R.id.start_btn_form);
        Button btnQna = rootView.findViewById(R.id.start_btn_qna);
        //TODO FUNCTIONALITY OF THIS
        Button btnEx = rootView.findViewById(R.id.start_btn_ex);
        btnForm.setOnClickListener(v -> {
            getActivity().getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).edit().putString("form_type", "simple_form").apply();
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            Fragment nextFragment = new StartConfFragmentC(getStep() + 1);
            fragmentTransaction.replace(R.id.container, nextFragment, nextFragment.toString());
            fragmentTransaction.addToBackStack(nextFragment.toString());
            fragmentTransaction.commit();
        });
        btnQna.setOnClickListener(v -> {
            getActivity().getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).edit().putString("form_type", "qna_form").apply();
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            Fragment nextFragment = new StartConfFragmentC(getStep() + 1);
            fragmentTransaction.replace(R.id.container, nextFragment, nextFragment.toString());
            fragmentTransaction.addToBackStack(nextFragment.toString());
            fragmentTransaction.commit();
        });
        btnEx.setOnClickListener(v -> {
            View lv = getActivity().findViewById(R.id.start_conf);
            lv.setVisibility(View.GONE);
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            Fragment nextFragment = new ExampleFragment(0);
            fragmentTransaction.add(R.id.conf_b_frame, nextFragment, nextFragment.toString());
            fragmentTransaction.addToBackStack(nextFragment.toString());
            fragmentTransaction.commit();
        });
        return rootView;
    }
}
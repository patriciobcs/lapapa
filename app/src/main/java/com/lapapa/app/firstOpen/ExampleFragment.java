package com.lapapa.app.firstOpen;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.lapapa.app.R;

import org.jetbrains.annotations.NotNull;

public class ExampleFragment extends Fragment {

    private int step;

    public static ExampleFragment newInstance(int step) {
        return new ExampleFragment(step);
    }

    public ExampleFragment(int step){
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
        return "ExampleFragment"+this.step;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.example_fragment, container, false);

        Button prevPage = rootView.findViewById(R.id.example_btn_back);
        Button nextPage = rootView.findViewById(R.id.example_btn_forward);

        if (this.step > 0){
            TextView tw = rootView.findViewById(R.id.example_title);
            tw.setText(R.string.example_title_b);

            nextPage.setText(R.string.example_btn_done);
            nextPage.setOnClickListener(v -> {
                View lv = getActivity().findViewById(R.id.start_conf);
                lv.setVisibility(View.VISIBLE);
                Activity a = getActivity();
                a.onBackPressed();
                a.onBackPressed();
            });
        }else{
            nextPage.setOnClickListener(v -> {
                FragmentTransaction fragmentTransaction = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                Fragment nextFragment = new ExampleFragment(getStep() + 1);
                fragmentTransaction.replace(R.id.conf_b_frame, nextFragment, nextFragment.toString());
                fragmentTransaction.addToBackStack(nextFragment.toString());
                fragmentTransaction.commit();
            });
        }
        prevPage.setOnClickListener(v ->{
            if(getStep() == 0){
                View lv = getActivity().findViewById(R.id.start_conf);
                lv.setVisibility(View.VISIBLE);
            }
            getActivity().onBackPressed();
        });
        return rootView;
    }

}
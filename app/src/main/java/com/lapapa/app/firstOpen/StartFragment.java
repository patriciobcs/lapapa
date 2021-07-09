package com.lapapa.app.firstOpen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lapapa.app.R;

import org.jetbrains.annotations.NotNull;

public class StartFragment extends Fragment {

    private int step;

    public static StartFragment newInstance(int step) {
        return new StartFragment(step);
    }

    public StartFragment(int step){
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
        return "StartFragment"+this.step;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.start_fragment, container, false);

        //UPDATE ALL TEXT TO CORRESPOND WITH CONFIGURATION
        if(getActivity() instanceof StartActivity){
            ((StartActivity) getActivity()).findViews(rootView);
        }


        Button nextPage = rootView.findViewById(R.id.start_btn);
        if (this.step >= 4){
            TextView tw = rootView.findViewById(R.id.start_title);
            tw.setText(R.string.start_finish);
            TextView tw2 = rootView.findViewById(R.id.start_message);
            tw2.setText(R.string.start_finish2);
            nextPage.setText(R.string.start_btn_out);
            nextPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }else{
            nextPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = getActivity()
                            .getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, new StartConfFragmentA(getStep() + 1));
                    fragmentTransaction.commit();
                }
            });
        }
        return rootView;
    }

}
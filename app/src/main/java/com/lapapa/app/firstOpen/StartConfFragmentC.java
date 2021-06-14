package com.lapapa.app.firstOpen;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.lapapa.app.R;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

public class StartConfFragmentC extends Fragment {

    private int step;
    private String[] OPTION_NAMES = {"Predeterminado de Android", "Pequeño", "Mediano", "Grande", "Muy Grande"};
    private TextView exampleText;

    public StartConfFragmentC(int step) {
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
        return "ConfFragmentC";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.start_conf_fragment_c, container, false);
        Button btnNext = rootView.findViewById(R.id.start_btn_next);
        exampleText = rootView.findViewById(R.id.start_show_text);
        //TODO SAVE THE VALUE
        SeekBar seekBar = rootView.findViewById(R.id.start_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String txt = exampleText.getText().toString().split(":")[0];
                exampleText.setText(txt + ": "+ OPTION_NAMES[progress]);
                exampleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14 + 3*progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnNext.setOnClickListener(v -> {
            //TODO
            getActivity().getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).edit().putString("text_size", String.valueOf(seekBar.getProgress())).apply();
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            Fragment nextFragment = new StartFragment(getStep() + 1);
            fragmentTransaction.replace(R.id.container, nextFragment, nextFragment.toString());
            fragmentTransaction.addToBackStack(nextFragment.toString());
            fragmentTransaction.commit();
        });
        return rootView;
    }
}
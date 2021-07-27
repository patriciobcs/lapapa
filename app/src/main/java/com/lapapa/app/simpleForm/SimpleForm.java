package com.lapapa.app.simpleForm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.lapapa.app.R;
import com.lapapa.app.chatBotForm.ChatBotMainActivity;
import com.lapapa.app.get_permit_v2.GetPermitActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SimpleForm extends AppCompatActivity {

    private final SimpleDateFormat sdf;
    private final DecimalFormat decimalFormat;
    private final Calendar calendar;

    {
        sdf = new SimpleDateFormat("dd 'de' MMMM, yyyy", new Locale("es", "ES"));

        // decimal formatter allows to set thousands separator to numbers
        String pattern = "###,###";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.applyPattern(pattern);
    }

    {
        calendar = Calendar.getInstance();
    }

    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    private void openDatePicker(View view, EditText birthDateInput) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                birthDateInput.setText(sdf.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private String verifierDigit(String run) {
        int sum = 0;
        int counter = 0;
        for (int i = run.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(String.valueOf(run.charAt(i)));
            sum += (counter + 2) * digit;
            counter = (counter + 1) % 6;
        }
        sum = sum % 11;
        sum = 11 - sum;

        if (sum != 10) {
            return String.valueOf(sum);
        }

        return "K";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_form);
        LinearLayout view = findViewById(R.id.coordinatorLayout);

        TextInputEditText birthDateInput = view.findViewById(R.id.birthDate);
        TextInputEditText serialNumberInput = view.findViewById(R.id.serialNumber);
        TextInputEditText runInput = view.findViewById(R.id.run);
        AutoCompleteTextView regionsInput = view.findViewById(R.id.regions);
        AutoCompleteTextView communesInput = view.findViewById(R.id.communes);
        Button saveBasicInformationInput = view.findViewById(R.id.saveBasicInformation);

        HashMap<String, List<String>> map = new HashMap<>();

        JSONArray json = null;

        try {
            InputStream file = getResources().openRawResource(R.raw.regions_and_communes);

            byte[] formArray = new byte[file.available()];
            file.read(formArray);

            String rawJson = new String(formArray, StandardCharsets.UTF_8);
            file.close();

            json = new JSONArray(rawJson);

            for (int i = 0; i < json.length(); i++) {
                JSONObject regionCommunesObject = (JSONObject) json.get(i);
                String region = regionCommunesObject.getString("region");
                JSONArray communesArray = (JSONArray) regionCommunesObject.get("communes");
                for (int j = 0; j < communesArray.length(); j++) {
                    String commune = communesArray.getString(j);
                    if (!map.containsKey(region)) {
                        map.put(region, new ArrayList<>());
                    }
                    map.get(region).add(commune);
                }
            }
        } catch (IOException | JSONException e) {
            Snackbar.make(view, "Error al tratar de recuperar las regiones y comunas", Snackbar.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> regionsAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, map.keySet().toArray(new String[0]));
        ArrayAdapter<String> communesAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, map.get(regionsAdapter.getItem(0)).toArray(new String[0]));
        regionsInput.setText(regionsAdapter.getItem(0));
        communesInput.setText(communesAdapter.getItem(0));
        regionsInput.setAdapter(regionsAdapter);
        communesInput.setAdapter(communesAdapter);

        birthDateInput.setShowSoftInputOnFocus(false);

        saveBasicInformationInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                String name = ((TextInputEditText) view.findViewById(R.id.fullname)).getText().toString();
                String rut = ((TextInputEditText) view.findViewById(R.id.run)).getText().toString().replace(".","");
                String code = ((TextInputEditText) view.findViewById(R.id.serialNumber)).getText().toString().replace(".", "");
                String age = String.valueOf((int) Math.floor(daysBetween(calendar, Calendar.getInstance()) / 365));
                String region = ((AutoCompleteTextView) view.findViewById(R.id.regions)).getText().toString();
                String commune = ((AutoCompleteTextView) view.findViewById(R.id.communes)).getText().toString();
                String street = ((TextInputEditText) view.findViewById(R.id.street)).getText().toString();
                String apartment = ((TextInputEditText) view.findViewById(R.id.apartmentNumber)).getText().toString();
                String email = ((TextInputEditText) view.findViewById(R.id.email)).getText().toString();
                String destino = ((TextInputEditText) view.findViewById(R.id.destination)).getText().toString();
                String address = street;
                rut = rut + "-" + verifierDigit(rut);

                if (!apartment.equals("")) address += " " + apartment;

                editor.putString("name", name);
                editor.putString("rut", rut);
                editor.putString("code", code);
                editor.putString("age", age);
                editor.putString("region", region);
                editor.putString("comuna", commune);
                editor.putString("address", address);
                editor.putString("email", email);
                editor.putString("destino", destino);
                editor.putBoolean("has_data", true);

                editor.commit();

                // navigation
                // Navigation.findNavController(view).navigate(R.id.action_standardForm_to_initialConfigurationFinished);
                Intent proceed = new Intent(SimpleForm.this, GetPermitActivity.class);
                startActivity(proceed);
            }
        });

        regionsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                regionsInput.removeTextChangedListener(this);
                regionsInput.setText(s.toString());
                regionsInput.addTextChangedListener(this);
                ArrayAdapter<String> communesAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.dropdown_item, map.get(s.toString()).toArray(new String[0]));
                communesInput.setText(communesAdapter.getItem(0));
                communesInput.setAdapter(communesAdapter);
            }
        });
        serialNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String serialNumber = s.toString().replace(".", "");
                serialNumberInput.removeTextChangedListener(this);

                if (serialNumber.equals("")) {
                    serialNumberInput.setText(serialNumber);
                } else {
                    String t = decimalFormat.format(Long.parseLong(serialNumber));
                    int initialDots = s.toString().length() - s.toString().replace(".", "").length();
                    int finalDots = t.length() - t.replace(".", "").length();
                    serialNumberInput.setText(t);
                    serialNumberInput.setSelection(start + count + finalDots - initialDots);
                }

                serialNumberInput.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        runInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String serialNumber = s.toString().replace(".", "");
                runInput.removeTextChangedListener(this);

                if (serialNumber.equals("")) {
                    runInput.setText(serialNumber);
                } else {
                    String t = decimalFormat.format(Long.parseLong(serialNumber));
                    int initialDots = s.toString().length() - s.toString().replace(".", "").length();
                    int finalDots = t.length() - t.replace(".", "").length();
                    runInput.setText(t);
                    runInput.setSelection(start + count + finalDots - initialDots);
                }

                runInput.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // allows to preserve default behaviour on inputs
        // specifically, when the current input is no longer focused
        // it will remove the "end icon"
        final View.OnFocusChangeListener listener = birthDateInput.getOnFocusChangeListener();
        birthDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(view, birthDateInput);
                hideSoftKeyboard(view);
            }
        });
        birthDateInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                listener.onFocusChange(v, hasFocus);
                if (hasFocus) {
                    openDatePicker(view, birthDateInput);
                    hideSoftKeyboard(view);
                }
            }
        });
    }
}
package com.lapapa.app.chatBotForm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.lapapa.app.R;
import com.lapapa.app.firstOpen.StartActivity;
import com.lapapa.app.get_permit_v2.GetPermitActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.lapapa.app.main.MainActivity.MODIFIER;

public class ChatBotMainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private static final String TAG = ChatBotMainActivity.class.getSimpleName();
    private static final int USER = 10001;
    private static final int BOT = 10002;

    private final String uuid = UUID.randomUUID().toString();
    private LinearLayout chatLayout;
    private EditText queryEditText;

    private SessionsClient sessionsClient;
    private SessionName session;

    public List<Field> dataFields = new ArrayList<>();
    public int fieldIndex = -1;

    public boolean enableTextToSpeech = true;
    TextToSpeech textToSpeech;

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            //Setting speech Language
            Locale locale = new Locale("spa", "MEX");
            textToSpeech.setLanguage(locale);
            textToSpeech.setPitch(1);
        }

        requestEmptyData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_activity_main);

        final ScrollView scrollview = findViewById(R.id.chatScrollView);
        scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));

        chatLayout = findViewById(R.id.chatLayout);

        ImageView sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this::sendMessage);

        queryEditText = findViewById(R.id.queryEditText);
        queryEditText.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        sendMessage(sendBtn);
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        initV2Chatbot();

        //define data fields
        dataFields.add(new Field("name", "su nombre completo", "text"));
        dataFields.add(new Field("email", "su correo electrónico", "mail"));
        dataFields.add(new Field("rut", "su RUT, sin puntos y con guión antes del dígito verificador", "rut"));
        dataFields.add(new Field("age", "su edad", "age"));
        dataFields.add(new Field("code", "su código de carnet", "text"));
        dataFields.add(new Field("region", "su región", "text"));
        dataFields.add(new Field("comuna", "su comuna", "text"));
        dataFields.add(new Field("address", "su dirección", "text"));
        dataFields.add(new Field("destino", "a dónde se dirige, o qué va a hacer. Si no sabe qué poner, basta con que escriba \"Trámites\"", "text"));

        enableTextToSpeech = getSharedPreferences("com.lapapa.app_preferences", Context.MODE_PRIVATE).getBoolean("tts", false);

        View view = findViewById(android.R.id.content).getRootView();
        findViews(view);

        if (enableTextToSpeech) textToSpeech = new TextToSpeech(this, this);
        else requestEmptyData();
    }

    private void initV2Chatbot() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.test_agent_credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(View view) {
        String msg = queryEditText.getText().toString();
        if (msg.trim().isEmpty()) {
            Toast.makeText(ChatBotMainActivity.this, "¡Contesta aqui!", Toast.LENGTH_LONG).show();
        } else {
            showTextViewWrapper(msg, USER);
            queryEditText.setText("");

            if (this.fieldIndex != -1) {
                Field field = this.dataFields.get(fieldIndex);
                if (Validator.validate(msg, field.type)) {
                    field.value = msg;
                    this.fieldIndex = -1;
                    requestEmptyData();
                } else {
                    QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("en-US")).build();
                    new RequestJavaV2Task(ChatBotMainActivity.this, session, sessionsClient, queryInput).execute();
                }
            }
        }
    }

    public void callbackV2(DetectIntentResponse response) {
        if (response != null) {
            // process aiResponse here
            String botReply = response.getQueryResult().getFulfillmentText();
            String intent = response.getQueryResult().getIntent().getDisplayName();
            Log.d(TAG, "V2 Bot Reply: " + botReply);
            Log.d(TAG, "Response: " + intent);
            showTextViewWrapper(botReply, BOT);
            if (intent == "Default Fallback Intent") showTextViewWrapper(botReply, BOT);
        } else {
            Log.d(TAG, "Bot Reply: Null");
            showTextViewWrapper("Hubo un problema. Intentalo de nuevo!", BOT);
        }
    }

    private void showTextViewWrapper(String message, int type){
        showTextViewWrapper(message, type, 1500);
    }

    private void showTextViewWrapper(String message, int type, int ms){
        final Handler handler = new Handler(Looper.getMainLooper());
        if (type == USER){
            showTextView(message, type);
        }else{
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    showTextView(message, type);
                }
            }, ms);
        }

    }

    private void showTextView(String message, int type) {
        FrameLayout layout;
        switch (type) {
            case USER:
                layout = getUserLayout();
                break;
            case BOT:
                layout = getBotLayout();
                break;
            default:
                layout = getBotLayout();
                break;
        }
        layout.setFocusableInTouchMode(true);
        chatLayout.addView(layout);
        TextView tv = layout.findViewById(R.id.chatMsg);
        tv.setText(message);

        String textSize = getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).getString("text_size", "0");
        int textModifier = Integer.parseInt(textSize)*MODIFIER;
        float sp = tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,sp + textModifier);

        layout.requestFocus();
        queryEditText.requestFocus();
        if (enableTextToSpeech) textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null, null);
    }

    FrameLayout getUserLayout() {
        LayoutInflater inflater = LayoutInflater.from(ChatBotMainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.user_msg_layout, null);
    }

    FrameLayout getBotLayout() {
        LayoutInflater inflater = LayoutInflater.from(ChatBotMainActivity.this);
        return (FrameLayout) inflater.inflate(R.layout.bot_msg_layout, null);
    }

    public int getEmptyFieldIndex() {
        for (int i = 0; i < this.dataFields.size(); i++) {
            if (this.dataFields.get(i).empty()) {
                return i;
            }
        }
        return -1;
    }

    public void requestEmptyData() {
        int fieldIndex = getEmptyFieldIndex();
        Log.d(TAG, String.valueOf(fieldIndex));
        if(fieldIndex == 0){
            showTextViewWrapper("¡Hola! Para sacar tu permiso necesito hacerte unas preguntas", BOT, 0);
            Field field = this.dataFields.get(fieldIndex);
            showTextViewWrapper("Por favor ingrese " + field.name, BOT, 5000);
            this.fieldIndex = fieldIndex;
        }
        else if (fieldIndex > 0) {
            Field field = this.dataFields.get(fieldIndex);
            showTextViewWrapper("Por favor ingrese " + field.name, BOT);
            this.fieldIndex = fieldIndex;
        } else {
            showTextViewWrapper("Gracias, el tramite comenzará enseguida", BOT);

            SharedPreferences sharedPref = getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE);
            SharedPreferences.Editor ed = sharedPref.edit();
            ed.putBoolean("has_data", true);
            for (int i = 0; i < this.dataFields.size(); i++){
                Field f = this.dataFields.get(i);
                ed.putString(f.id, f.value);
            }
            ed.apply();
            //String dataToJS = "var data = {";
            //for (int i = 0; i < this.dataFields.size(); i++) {
            //    dataToJS += this.dataFields.get(i).JSONField();
            //    if (i != this.dataFields.size() - 1) dataToJS += ",";
            //    else dataToJS += "};";
            //}
            //Log.d(TAG, dataToJS);

            //doSomething(fieldToJS);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    Intent proceed = new Intent(ChatBotMainActivity.this, GetPermitActivity.class);
                    startActivity(proceed);
                }
            }, 4000);
        }
    }

    public void findViews(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    // recursively call this method
                    findViews(child);
                }
            } else if (v instanceof TextView) {
                //do whatever you want ...
                String textSize = getSharedPreferences("com.lapapa.app_preferences", MODE_PRIVATE).getString("text_size", "0");
                int textModifier = Integer.parseInt(textSize)*MODIFIER;
                TextView tv = ((TextView)v);
                if(tv.getText().equals("LaPaPa")){
                    return;
                }
                float sp = tv.getTextSize() / getResources().getDisplayMetrics().scaledDensity;
                Log.d("findViews", "Found TV with text "+ tv.getText() + " and text size " + sp);
                Log.d("findViews", "Adding up "+ textModifier);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,sp + textModifier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

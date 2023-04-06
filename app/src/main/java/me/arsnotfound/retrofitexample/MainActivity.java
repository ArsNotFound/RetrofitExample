package me.arsnotfound.retrofitexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "SOME KEY HERE!!!";
    private PredictorService predictorService;

    private Spinner langSpinner;
    private EditText queryET;
    private TextView responseTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        langSpinner = findViewById(R.id.lang_spinner);
        queryET = findViewById(R.id.query_et);
        responseTV = findViewById(R.id.response_tv);

        createServices();
        getAvailableLangs();

        queryET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateResponse(s.toString());
            }
        });
    }

    private void createServices() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://predictor.yandex.net/api/v1/predict.json/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        predictorService = retrofit.create(PredictorService.class);
    }

    private void getAvailableLangs() {
        Call<List<String>> availableLangsCall = predictorService.getLangs(API_KEY);
        Callback<List<String>> callback = new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                List<String> availableLangs = response.body();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, availableLangs);
                langSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                Log.e("Callback", "Error occurred, while performing a query", t);
                Toast.makeText(MainActivity.this, "Error occurred, while performing a query", Toast.LENGTH_LONG).show();
            }
        };

        availableLangsCall.enqueue(callback);
    }

    private void updateResponse(String query) {
        String lang = (String) langSpinner.getSelectedItem();
        Call<CompleteResponse> completeCall = predictorService.complete(API_KEY, lang, query, 1);
        Callback<CompleteResponse> callback = new Callback<CompleteResponse>() {
            @Override
            public void onResponse(Call<CompleteResponse> call, Response<CompleteResponse> response) {
                CompleteResponse resp = response.body();
                if (resp == null || resp.getText().size() == 0) {
                    responseTV.setText("");
                } else {
                    responseTV.setText(resp.getText().get(0));
                }
            }

            @Override
            public void onFailure(Call<CompleteResponse> call, Throwable t) {
                Log.e("Callback", "Error occurred, while performing a query", t);
                Toast.makeText(MainActivity.this, "Error occurred, while performing a query", Toast.LENGTH_LONG).show();
            }
        };

        completeCall.enqueue(callback);
    }
}
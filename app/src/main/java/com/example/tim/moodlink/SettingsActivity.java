package com.example.tim.moodlink;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton return_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        return_button = (ImageButton) findViewById(R.id.return_button);

        return_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        // TEST SAUVEGARDE VALEURS

        final Button dataButton = (Button) findViewById(R.id.buttonLight);

        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // File's reading
                FileInputStream input;

                try {
                    input = openFileInput("LIGHT_VALUES");
                    Log.d("FILE READING", "Fichier ouvert");
                    int character;
                    String fileContent = "";
                    while ((character = input.read()) != -1) {
                        fileContent += (char) character;
                    }

                    TextView dataText = (TextView) findViewById(R.id.dataPrintingTextView);
                    Resources res = getResources();

                    Log.d("FILE READING", "Valeurs = " + fileContent);
                    dataText.setText(fileContent);

                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

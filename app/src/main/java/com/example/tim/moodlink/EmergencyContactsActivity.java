package com.example.tim.moodlink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class EmergencyContactsActivity extends AppCompatActivity {

    private ImageButton return_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_contacts_activity);

        return_button = (ImageButton) findViewById(R.id.return_button);

        return_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(EmergencyContactsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

package com.example.tim.moodlink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GraphActivity extends AppCompatActivity {

    private ImageButton return_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        return_button = (ImageButton) findViewById(R.id.return_button);

        return_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GraphActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

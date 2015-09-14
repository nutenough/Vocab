package app.mueller.schiller.weber.com.vicab;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LanguageActivity extends AppCompatActivity {
    
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        setupUIComponents();
        handleEvents();
    }

    private void setupUIComponents() {
        fab = (FloatingActionButton) findViewById(R.id.fabLanguage);
    }

    private void handleEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LanguageActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });
    }
}
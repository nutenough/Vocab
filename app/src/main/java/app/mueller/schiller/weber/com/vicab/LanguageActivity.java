package app.mueller.schiller.weber.com.vicab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LanguageActivity extends AppCompatActivity {

    private TextView appNameTV;
    private FloatingActionButton fab;
    private EditText sourceLanguageET;
    private EditText targetLanguageET;
    private String languageCouple;
    private String sourceLanguage;
    private String targetLanguage;
    private AlertDialog alertDialog;
    private ListView languageLV;
    private ArrayList<String> listItems = new ArrayList<>();
    private LanguageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        setupUIComponents();
        handleFabEvent();
        attachAdapter();
    }

    private void setupUIComponents() {
        // Font for app name
        Typeface appNameTypeface = Typeface.createFromAsset(getAssets(), "chunkfive.otf");
        appNameTV = (TextView) findViewById(R.id.appNameTV);
        appNameTV.setTypeface(appNameTypeface);

        languageLV = (ListView) findViewById(R.id.langSelectListView);

        fab = (FloatingActionButton) findViewById(R.id.fabLanguage);
    }

    private void handleFabEvent() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        // Setup View for AlertDialog
        final View view = LayoutInflater.from(LanguageActivity.this).inflate(R.layout.language_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LanguageActivity.this);
        alertDialogBuilder.setView(view);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_database);
        alertDialogBuilder.setTitle(R.string.language_title);
        alertDialogBuilder.setMessage(R.string.lang_message);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.language_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getInput(view);
                if (sourceLanguage.isEmpty() || targetLanguage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.no_lang_input, Toast.LENGTH_LONG).show();
                } else {
                    putInArrayList();
                }
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.language_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        // Edit Design alertDialog
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.color_primary));

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.color_primary));
    }

    // Create strings and replacing whitespace
    private void getInput(View view) {
        sourceLanguageET = (EditText) view.findViewById(R.id.sourceLanguageET);
        targetLanguageET = (EditText) view.findViewById(R.id.targetLanguageET);

        sourceLanguage = sourceLanguageET.getText().toString();
        targetLanguage = targetLanguageET.getText().toString();
        languageCouple = sourceLanguage + " - " + targetLanguage;
    }

    // Puts the input from the alertDialog in ListView items
    private void putInArrayList() {
        listItems.add(languageCouple);
        adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "Sprachbeziehung " + languageCouple + " festgelegt", Toast.LENGTH_LONG).show();
    }

    private void attachAdapter() {
        adapter = new LanguageAdapter(this, listItems);
        languageLV.setAdapter(adapter);
        languageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LanguageActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });
    }

}
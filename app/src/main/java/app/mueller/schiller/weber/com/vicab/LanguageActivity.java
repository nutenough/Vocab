package app.mueller.schiller.weber.com.vicab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.Database.ViCabContract;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.LanguageItem;

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
    private ArrayList<LanguageItem> listItems = new ArrayList<>();
    private LanguageAdapter adapter;

    private LanguageItem newLangItem;

    private DBAdmin dbAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        initDB();
        setupUIComponents();
        handleFabEvent();
        attachAdapter();
        setOnClickListener();
        updateList();
    }

    private void updateList() {
        listItems.clear();
        listItems.addAll(dbAdmin.getAllLanguages());
        adapter.notifyDataSetChanged();
    }

    protected void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }

    private void initDB() {
        dbAdmin = new DBAdmin(this);
        dbAdmin.open();
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

        sourceLanguage = sourceLanguageET.getText().toString().replace(" ", "");
        targetLanguage = targetLanguageET.getText().toString().replace(" ", "");
        languageCouple = sourceLanguage + " - " + targetLanguage;
    }

    // Puts the input from the alertDialog in ListView items
    private void putInArrayList() {
        newLangItem = new LanguageItem(languageCouple, sourceLanguage, targetLanguage);
        safeInDB(newLangItem);

        listItems.add(newLangItem);
        updateList();
        Toast.makeText(getApplicationContext(), "Sprachbeziehung " + languageCouple + " festgelegt", Toast.LENGTH_LONG).show();
    }

    private void safeInDB(LanguageItem langItem) {
        dbAdmin.addLanguage(langItem);
    }

    private void attachAdapter() {
        adapter = new LanguageAdapter(this, listItems);
        languageLV.setAdapter(adapter);
    }

    private void setOnClickListener() {
        languageLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                removeItem(position);
                return true;
            }
        });

        languageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ViCabContract.CHOSEN_LANGUAGE_MIX = listItems.get(position).getCouple();
                ViCabContract.CHOSEN_LANGUAGE_SOURCE = listItems.get(position).getSourceLanguage();
                ViCabContract.CHOSEN_LANGUAGE_TARGET = listItems.get(position).getTargetLanguage();

                Intent intent = new Intent(LanguageActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void removeItem(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(
                this);

        alert.setIcon(R.drawable.ic_delete);
        alert.setTitle(R.string.language_delete_title);
        alert.setMessage(R.string.language_delete_message);
        alert.setPositiveButton(R.string.language_delete_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TOD O Auto-generated method stub

                // main code on after clicking yes
                dbAdmin.removeLanguage(listItems.get(deletePosition));
                listItems.remove(deletePosition);
                updateList();

            }
        });
        alert.setNegativeButton(R.string.language_delete_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

}
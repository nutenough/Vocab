package app.mueller.schiller.weber.com.vicab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.Database.ViCabContract;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.ListItem;

public class AddVocabFragmentOne extends Fragment implements
        AdapterView.OnItemSelectedListener {
    View contentView;
    private Button vocabAddBTN;
    private Button vocabCancelBTN;
    private TextView sourceVocabTV;
    private TextView targetVocabTV;
    private EditText sourceVocabET;
    private EditText targetVocabET;
    private String sourceVocab;
    private String targetVocab;
    private String vocabCouple;
    private Spinner spinner;
    private TextView vocabHintTV;

    private DBAdmin dbAdmin;


    // The view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.add_vocab_fragment_one, null);
        return contentView;
    }

    // Page content ("findViewById" has to be "view.findViewById"; "getApplicationContext" has to be "getActivity()")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUIComponents();
        initDB();
        fillSpinnerFromDB();
        setOnClickListeners();
    }


    private void setOnClickListeners(){
        vocabAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                if (sourceVocab.isEmpty() || targetVocab.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.no_vocab_input, Toast.LENGTH_LONG).show();
                } else {
                    saveInput();
                    Toast.makeText(getActivity(), vocabCouple + " wurde hinzugefügt", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), NavigationActivity.class);
                    startActivity(intent);
                }
            }
        });

        vocabCancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

    }

    private void getInput() {
        sourceVocab = sourceVocabET.getText().toString().trim();
        targetVocab = targetVocabET.getText().toString().trim();
        vocabCouple = sourceVocab + " - " + targetVocab;
    }

    private void fillSpinnerFromDB() {

        // Spinner Drop down elements
        List<ListItem> listItems = dbAdmin.getAllListsForChosenLanguage();
        List<String> labels = new ArrayList<>();
        labels.add(getResources().getString(R.string.spinner_no_list));
        for (int i = 0; i <listItems.size(); i++) {
            labels.add(listItems.get(i).getName());
        }

        // adapter for spinner
        ArrayAdapter<ListItem> dataAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item do stuff

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    private void initDB() {
        dbAdmin = new DBAdmin(getActivity());
        dbAdmin.open();
    }

    public void onDestroy() {
        dbAdmin.close();
        dbAdmin.close();
        super.onDestroy();
    }

    private void setupUIComponents() {
        sourceVocabET = (EditText) getActivity().findViewById(R.id.sourceVocabET);
        targetVocabET = (EditText) getActivity().findViewById(R.id.targetVocabET);
        spinner = (Spinner) getActivity().findViewById(R.id.addListSpinner);

        // Gets the source- and target-language
        sourceVocabTV = (TextView) getActivity().findViewById(R.id.sourceVocabTV);
        targetVocabTV = (TextView) getActivity().findViewById(R.id.targetVocabTV);
        sourceVocabTV.setText(ViCabContract.CHOSEN_LANGUAGE_SOURCE);
        targetVocabTV.setText(ViCabContract.CHOSEN_LANGUAGE_TARGET);

        // Adds Vocab to Meine Vokabeln
        vocabAddBTN = (Button) getActivity().findViewById(R.id.vocabAddBTN);

        // Cancel button
        vocabCancelBTN = (Button) getActivity().findViewById(R.id.vocabCancelBTN);

    }

    private void saveInput() {
        // addVocab method wants this: String sourceVocab, String targetVocab, String fotoLink, String soundLink, String wordType, String importance, String hasList, int known, int asked, int timestamp
        dbAdmin.addVocab(sourceVocab, targetVocab, "", AddVocabFragmentTwo.getWordType(), AddVocabFragmentTwo.getRating(), 0, spinner.getSelectedItem().toString(), 0, 0, getTimeStamp());
    }

    private int getTimeStamp() {
        // int time = (int) (new Date().getTime()/1000);
        // Umwandlung zurück zum Datum:  new Date(((long)i)*1000L);
        return (int) (new Date().getTime()/1000);
    }

}


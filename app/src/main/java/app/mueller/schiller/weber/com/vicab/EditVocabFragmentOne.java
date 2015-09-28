package app.mueller.schiller.weber.com.vicab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.Database.ViCabContract;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.ListItem;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class EditVocabFragmentOne extends Fragment implements
        AdapterView.OnItemSelectedListener {
    View contentView;
    private Button buttonLeft, buttonRight, buttonFinish;
    private TextView sourceVocabTV;
    private TextView targetVocabTV;
    private EditText sourceVocabET;
    private EditText targetVocabET;
    private String sourceVocab;
    private String targetVocab;
    private Spinner spinner;
    private int changedVocab = 0;
    private int positionInList = 0;
    private ArrayList<VocItem> listItems = new ArrayList<>();

    private DBAdmin dbAdmin;


    // The view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.edit_vocab_fragment_one, null);
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
        getInfosFromIntent();
    }


    private void getInfosFromIntent(){
            Bundle bundle = getActivity().getIntent().getExtras();
            String listName = bundle.getString("listName");
            positionInList = bundle.getInt("position");

            if (listName.equals("")) {
                listItems.addAll(dbAdmin.getAllVocabForLanguage());
            }
            if (listName.equals("letzte")) {
                listItems.addAll(dbAdmin.getRecentVocab());
            }
            else {
                listItems.addAll(dbAdmin.getAllVocabForList(listName));
            }

            fillViewsWithObjectInfo();
    }

    private void fillViewsWithObjectInfo(){
        sourceVocabET.setText(listItems.get(positionInList).getSourceVocab());
        targetVocabET.setText(listItems.get(positionInList).getTargetVocab());
       // spinner.set
    }

    private void setOnClickListeners(){
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                if (sourceVocab.isEmpty() || targetVocab.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.no_vocab_input, Toast.LENGTH_LONG).show();
                } else {
                    saveInput();
                    Toast.makeText(getActivity(), changedVocab + "Vokabeln wurde(n) bearbeitet", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

    }

    private void getInput() {
        sourceVocab = sourceVocabET.getText().toString().trim();
        targetVocab = targetVocabET.getText().toString().trim();
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
        sourceVocabET = (EditText) getActivity().findViewById(R.id.editVocabSourceVocabET);
        targetVocabET = (EditText) getActivity().findViewById(R.id.editVocabTargetVocabET);
        spinner = (Spinner) getActivity().findViewById(R.id.editVocabAddListSpinner);

        // Gets the source- and target-language
        sourceVocabTV = (TextView) getActivity().findViewById(R.id.editVocabSourceVocabTV);
        targetVocabTV = (TextView) getActivity().findViewById(R.id.editVocabTargetVocabTV);
        sourceVocabTV.setText(ViCabContract.CHOSEN_LANGUAGE_SOURCE);
        targetVocabTV.setText(ViCabContract.CHOSEN_LANGUAGE_TARGET);

        // go forth and back in the vocab list
        buttonRight = (Button) getActivity().findViewById(R.id.editVocabButtonRight);
        buttonLeft = (Button) getActivity().findViewById(R.id.editVocabButtonLeft);

        // finish button
        buttonFinish = (Button) getActivity().findViewById(R.id.editVocabButtonfinished);

    }

    private void saveInput() {
        //SAVE
        Log.d("safe", "Hier");

    }

}


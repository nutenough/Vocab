package app.mueller.schiller.weber.com.vicab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.Database.ViCabContract;

public class AddVocabFragmentOne extends Fragment {
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
    }

    private void initDB() {
        dbAdmin = new DBAdmin(getActivity());
        dbAdmin.open();
    }

    public void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }

    private void setupUIComponents() {
        sourceVocabET = (EditText) getActivity().findViewById(R.id.sourceVocabET);
        targetVocabET = (EditText) getActivity().findViewById(R.id.targetVocabET);

        // Gets the source- and target-language
        sourceVocabTV = (TextView) getActivity().findViewById(R.id.sourceVocabTV);
        targetVocabTV = (TextView) getActivity().findViewById(R.id.targetVocabTV);
        sourceVocabTV.setText(ViCabContract.CHOSEN_LANGUAGE_SOURCE);
        targetVocabTV.setText(ViCabContract.CHOSEN_LANGUAGE_TARGET);

        // Adds Vocab to Meine Vokabeln
        vocabAddBTN = (Button) getActivity().findViewById(R.id.vocabAddBTN);
        vocabAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInput();
                if (sourceVocab.isEmpty() || targetVocab.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.no_vocab_input, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), vocabCouple + " wurde hinzugefügt", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), NavigationActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Cancel button
        vocabCancelBTN = (Button) getActivity().findViewById(R.id.vocabCancelBTN);
        vocabCancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveInput() {
        sourceVocab = sourceVocabET.getText().toString();
        targetVocab = targetVocabET.getText().toString();
        vocabCouple = sourceVocab + " - " + targetVocab;
        // addVocab method wants this: String sourceVocab, String targetVocab, String fotoLink, String soundLink, String wordType, String importance, String hasList
        dbAdmin.addVocab(sourceVocab, targetVocab, "", "", "", 0, "");
    }

}

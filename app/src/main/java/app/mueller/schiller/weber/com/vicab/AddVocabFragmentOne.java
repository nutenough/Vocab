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
import android.widget.Toast;

public class AddVocabFragmentOne extends Fragment {
    View contentView;
    private Button vocabAddBTN;
    private EditText sourceVocabET;
    private EditText targetVocabET;
    private String sourceVocab;
    private String targetVocab;
    private String vocabCouple;

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
        getInput();
    }

    private void setupUIComponents() {
        sourceVocabET = (EditText) getActivity().findViewById(R.id.sourceVocabET);
        targetVocabET = (EditText) getActivity().findViewById(R.id.targetVocabET);
        vocabAddBTN = (Button) getActivity().findViewById(R.id.vocabAddBTN);
        vocabAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInput();
                Toast.makeText(getActivity(), vocabCouple + " wurde hinzugefügt", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                intent.putExtra("VOCAB_COUPLE", vocabCouple);
                startActivity(intent);
            }
        });
    }

    private void getInput() {
        sourceVocab = sourceVocabET.getText().toString();
        targetVocab = targetVocabET.getText().toString();
        vocabCouple = sourceVocab + " - " + targetVocab;
    }

}

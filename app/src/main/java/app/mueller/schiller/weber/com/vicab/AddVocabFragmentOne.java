package app.mueller.schiller.weber.com.vicab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddVocabFragmentOne extends Fragment {
    View contentView;
    private Button vocabAddBTN;
    private EditText sourceVocabET;
    private EditText targetVocabET;
    private String sourceVocab;
    private String targetVocab;
    private String vocabCouple;
    private ArrayList<String> listItems = new ArrayList<>();
    private AddVocabFragmentAdapter adapter;
    private ListView vocabsLV;


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
        vocabAddBTN = (Button) getActivity().findViewById(R.id.vocabAddBTN);
        vocabsLV = (ListView) view.findViewById(R.id.vocabsLV);
        vocabAddBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupUIComponents();
                getInput();
                putInArrayList();
                attachAdapter();
            }
        });
    }

    private void setupUIComponents() {
        sourceVocabET = (EditText) getActivity().findViewById(R.id.sourceVocabET);
        targetVocabET = (EditText) getActivity().findViewById(R.id.targetVocabET);
    }

    private void getInput() {
        sourceVocab = sourceVocabET.getText().toString();
        targetVocab = targetVocabET.getText().toString();
        vocabCouple = sourceVocab + " - " + targetVocab;
    }

    private void putInArrayList() {
        listItems.add(vocabCouple);
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), vocabCouple + " angelegt", Toast.LENGTH_LONG).show();
    }

    private void attachAdapter() {
        adapter = new AddVocabFragmentAdapter(getActivity(), listItems);
        vocabsLV.setAdapter(adapter);
    }

}

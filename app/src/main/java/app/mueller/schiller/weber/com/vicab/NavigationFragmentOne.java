package app.mueller.schiller.weber.com.vicab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class NavigationFragmentOne extends Fragment {

    private View view;
    private FloatingActionButton fab;
    private ListView vocabsLV;
    private String vocabCouple;
    private ArrayList<String> listItems = new ArrayList<>();
    private NavigationFragmentOneAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.navigation_fragment_one, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupUIComponents();
        handleEvents();
        getInput();
        attachAdapter();
        putInArrayList();
    }

    private void getInput() {
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            vocabCouple = extras.getString("VOCAB_COUPLE");
        }
    }

    private void setupUIComponents() {
        fab = (FloatingActionButton) view.findViewById(R.id.fabVocab);
        vocabsLV = (ListView) view.findViewById(R.id.vocabsLV);
    }

    private void handleEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddVocabActivity.class);
                startActivity(intent);
            }
        });
    }

    private void putInArrayList() {
        listItems.add(vocabCouple);
        adapter.notifyDataSetChanged();
    }

    private void attachAdapter() {
        adapter = new NavigationFragmentOneAdapter(getActivity(), listItems);
        vocabsLV.setAdapter(adapter);
    }

}
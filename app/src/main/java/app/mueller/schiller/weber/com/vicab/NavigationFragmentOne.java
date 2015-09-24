package app.mueller.schiller.weber.com.vicab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class NavigationFragmentOne extends Fragment {

    private View view;
    private FloatingActionButton fab;
    private ListView vocabsLV;
    private ArrayList<VocItem> listItems = new ArrayList<>();
    private NavigationFragmentOneAdapter adapter;

    private DBAdmin dbAdmin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.navigation_fragment_one, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUIComponents();
        initDB();
        handleEvents();
        attachAdapter();
        updateList();
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
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fabVocab);
        vocabsLV = (ListView) getActivity().findViewById(R.id.vocabsLV);
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

    private void updateList(){
        listItems.clear();
        listItems.addAll(dbAdmin.getAllVocab());
        Log.d("MyDebug", "items: " + dbAdmin.getAllVocab());
        adapter.notifyDataSetChanged();
    }

    private void attachAdapter() {
        adapter = new NavigationFragmentOneAdapter(getActivity(), listItems);
        vocabsLV.setAdapter(adapter);
    }

}
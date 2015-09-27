package app.mueller.schiller.weber.com.vicab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class NavigationFragmentSix extends Fragment {




    private View view;
    private ListView vocabsLV;
    private ArrayList<VocItem> listItems = new ArrayList<>();
    private ArrayList<VocItem> knownVLItems = new ArrayList<>();
    private NavigationFragmentSixAdapter adapter;
    private int counterAll = 0;
    private int counterKnown = 0 ;
    private TextView textView;

    private DBAdmin dbAdmin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_fragment_six, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUIComponents();
        initDB();
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
        vocabsLV = (ListView) getActivity().findViewById(R.id.knownVocabsLV);
        textView = (TextView) getActivity().findViewById(R.id.textView);



    }



    private void updateList(){
        listItems.clear();
        knownVLItems.clear();
        listItems.addAll(dbAdmin.getAllVocabForLanguage());
        for(int i = 0; i < listItems.size(); i++){
            counterAll++;
            if(listItems.get(i).getKnown() > 5 ){
                knownVLItems.add(listItems.get(i));
                counterKnown++;
            }

        }
        Log.d("MyDebug", "items: " + dbAdmin.getAllVocabForLanguage());
        adapter.notifyDataSetChanged();
        textView.setText("Du hast " + counterKnown + " von " + counterAll + " Vokabeln im Langzeitgedächtnis!");
        for(int i = 0; i < knownVLItems.size(); i++){
            Log.d("hallo", knownVLItems.get(i).getSourceVocab());
        }
    }

    private void attachAdapter() {
        adapter = new NavigationFragmentSixAdapter(getActivity(), knownVLItems);
        vocabsLV.setAdapter(adapter);
    }




}
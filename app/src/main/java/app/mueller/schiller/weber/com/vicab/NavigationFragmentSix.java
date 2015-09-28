package app.mueller.schiller.weber.com.vicab;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class NavigationFragmentSix extends Fragment {




    private View view;
    private ListView vocabsLV;
    private ArrayList<VocItem> listItems = new ArrayList<>();
    private ArrayList<VocItem> knownVLItems = new ArrayList<>();
    private ArrayList<VocItem> currentSearchList = new ArrayList<>();
    private NavigationFragmentSixAdapter adapter;
    private int counterAll = 0;
    private int counterKnown = 0 ;
    private TextView textView;
    private ImageButton resetIB;
    AlertDialog alertDialog;


    private DBAdmin dbAdmin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.navigation_fragment_six, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.navigation_six_search_menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        if (null != searchView) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setIconifiedByDefault(true);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered

                if(newText.equals("")){
                    updateList();
                }

                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                currentSearchList = knownVLItems;

                String s = query.toLowerCase().trim();


                for(int i = 0; i < knownVLItems.size(); i++){
                    String source = knownVLItems.get(i).getSourceVocab().toLowerCase().trim();
                    String target = knownVLItems.get(i).getTargetVocab().toLowerCase().trim();

                    if(source.contains(s)){

                    }else if(target.contains(s)){

                    }else{
                        knownVLItems.remove(i);
                        i=-1;
                    }
                }
                adapter.notifyDataSetChanged();

                return false;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUIComponents();
        initDB();
        attachAdapter();
        updateList();
        setOnClickListener();
    }

    private void setOnClickListener(){
        vocabsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                showAlertDialogReset(position);
                return true;
            }
        });

        resetIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogResetAll();
            }
        });
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
        resetIB = (ImageButton) getActivity().findViewById(R.id.imageButton_reset);

    }



    private void updateList(){
        counterAll = 0;
        counterKnown = 0;
        listItems.clear();
        knownVLItems.clear();
        listItems.addAll(dbAdmin.getAllVocabForLanguage());
        for(int i = 0; i < listItems.size(); i++){
            counterAll++;
            Log.d("4321", listItems.get(i).getSourceVocab() + "     "+ String.valueOf(listItems.get(i).getImportance() +"       " + listItems.get(i).getKnown()));
            if(listItems.get(i).getImportance().equals("1.0")){
                if(listItems.get(i).getKnown() > 4 ){
                    knownVLItems.add(listItems.get(i));
                    counterKnown++;
                }
            }else if(listItems.get(i).getImportance().equals("2.0")){
                if(listItems.get(i).getKnown() > 6 ){
                    knownVLItems.add(listItems.get(i));
                    counterKnown++;
                }
            }else if(listItems.get(i).getImportance().equals("3.0")){
                if(listItems.get(i).getKnown() > 7 ){
                    knownVLItems.add(listItems.get(i));
                    counterKnown++;
                }
            }else {
                if(listItems.get(i).getKnown() > 5 ){
                    knownVLItems.add(listItems.get(i));
                    counterKnown++;
                }
            }
        }
        Log.d("MyDebug", "items: " + dbAdmin.getAllVocabForLanguage());
        adapter.notifyDataSetChanged();
        textView.setText("Du hast " + counterKnown + " von " + counterAll + " Vokabeln im Langzeitgedächtnis!");
    }

    private void attachAdapter() {
        adapter = new NavigationFragmentSixAdapter(getActivity(), knownVLItems);
        vocabsLV.setAdapter(adapter);
    }


    private void showAlertDialogReset(final int position) {
        // Setup View for AlertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_brain);
        alertDialogBuilder.setTitle(R.string.reset_title);
        alertDialogBuilder.setMessage(R.string.reset_message);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                knownVLItems.get(position).resetKnown();
                dbAdmin.updateAskedAndKnown(knownVLItems.get(position));
                updateList();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

    private void showAlertDialogResetAll() {
        // Setup View for AlertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_brain);
        alertDialogBuilder.setTitle(R.string.reset_all_title);
        alertDialogBuilder.setMessage(R.string.reset_all_message);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0; i < knownVLItems.size(); i++){
                    knownVLItems.get(i).resetKnown();
                    dbAdmin.updateAskedAndKnown(knownVLItems.get(i));
                }
                updateList();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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


}
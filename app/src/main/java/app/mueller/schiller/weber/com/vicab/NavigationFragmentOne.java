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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class NavigationFragmentOne extends Fragment {

    private View view;
    private FloatingActionButton fab;
    private ListView vocabsLV;
    private ArrayList<VocItem> listItems = new ArrayList<>();
    private ArrayList<VocItem> currentSearchList = new ArrayList<>();
    private NavigationFragmentOneAdapter adapter;
    private TextView vocabHintTV;

    private DBAdmin dbAdmin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.navigation_fragment_one, container, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.navigation_one_search_menu, menu);
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
                currentSearchList = listItems;

                String s = query.toLowerCase().trim();


                for(int i = 0; i < listItems.size(); i++){
                    String source = listItems.get(i).getSourceVocab().toLowerCase().trim();
                    String target = listItems.get(i).getTargetVocab().toLowerCase().trim();

                    if(source.contains(s)){

                    }else if(target.contains(s)){

                    }else{
                        listItems.remove(i);
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
        handleEvents();
        attachAdapter();
        updateList();
        setOnClickListener();
        checkHint();
    }



    private void checkHint() {
        if (listItems.size() > 0) {
            vocabHintTV.setVisibility(View.INVISIBLE);
        }
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
        vocabHintTV = (TextView) getActivity().findViewById(R.id.vocabHintTV);
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
        listItems.addAll(dbAdmin.getAllVocabForLanguage());
        adapter.notifyDataSetChanged();
    }

    private void attachAdapter() {
        adapter = new NavigationFragmentOneAdapter(getActivity(), listItems);
        vocabsLV.setAdapter(adapter);
    }

    private void setOnClickListener() {

        vocabsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                removeItem(position);
                return true;
            }
        });

        vocabsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Gehe zu bearbeiten
                Intent intent = new Intent(getActivity(), EditVocabActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("listName", "");
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    protected void removeItem(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setIcon(R.drawable.ic_delete);
        alert.setTitle(R.string.vocab_delete_title);
        alert.setMessage(R.string.vocab_delete_message);
        alert.setPositiveButton(R.string.delete_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // main code on after clicking yes
                dbAdmin.removeVocab(listItems.get(deletePosition));
                listItems.remove(deletePosition);
                updateList();
            }
        });
        alert.setNegativeButton(R.string.delete_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateList();
    }

}
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
        setOnClickListener();
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
        listItems.addAll(dbAdmin.getAllVocabForLanguage());
        Log.d("MyDebug", "items: " + dbAdmin.getAllVocabForLanguage());
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

                // Vokabel bearbeiten

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

}
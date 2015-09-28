package app.mueller.schiller.weber.com.vicab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class ListVocabActivity extends AppCompatActivity {

    private ListView vocabsLV;
    private ArrayList<VocItem> listItems = new ArrayList<>();
    private NavigationFragmentOneAdapter adapter;
    private String listName = "";
    private Toolbar toolbar;
    private DBAdmin dbAdmin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_vocab);
        setupUIComponents();
        initDB();
        attachAdapter();
        getListInfo();
        setActivityTitle();
        setOnClickListener();
    }

    public void onResume() {
        updateList();
        super.onResume();
    }


    private void setActivityTitle() {
        getSupportActionBar().setTitle(listName);
    }

    private void getListInfo() {
        Bundle bundle = getIntent().getExtras();
        listName = bundle.getString("listName");
        getDataFromDB();
    }

    private void getDataFromDB() {
        dbAdmin.getAllVocabForList(listName);
        updateList();
    }

    private void initDB() {
        dbAdmin = new DBAdmin(this);
        dbAdmin.open();
    }

    public void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }

    private void setupUIComponents() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(listName);

        vocabsLV = (ListView) findViewById(R.id.vocabsListLV);
    }


    private void updateList() {
        listItems.clear();
        listItems.addAll(dbAdmin.getAllVocabForList(listName));
        adapter.notifyDataSetChanged();
    }

    private void attachAdapter() {
        Log.d("ListVoc", "adapter: " + adapter);
        Log.d("ListVoc", "listView: " + vocabsLV);

        adapter = new NavigationFragmentOneAdapter(this, listItems);
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

                // zu bearbeiten
                Intent intent = new Intent(ListVocabActivity.this, EditVocabActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("listName", listName);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    protected void removeItem(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

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
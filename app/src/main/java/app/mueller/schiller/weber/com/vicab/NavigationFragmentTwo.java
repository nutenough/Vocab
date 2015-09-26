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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;
import app.mueller.schiller.weber.com.vicab.PersistanceClasses.ListItem;

public class NavigationFragmentTwo extends Fragment {

    private FloatingActionButton fab;
    private ListView listsLV;
    private EditText listNameET;
    private String listName;
    private AlertDialog alertDialog;
    private ArrayList<ListItem> listItems = new ArrayList<>();
    private NavigationFragmentTwoAdapter adapter;

    private DBAdmin dbAdmin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_fragment_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDB();
        setupUIComponents();
        handleFabEvent();
        attachAdapter();
        setOnClickListener();
        updateList();
    }

    private void updateList() {
        listItems.clear();
        listItems.addAll(dbAdmin.getAllListsForChosenLanguage());
        adapter.notifyDataSetChanged();
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
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fabList);
        listsLV = (ListView) getActivity().findViewById(R.id.listsLV);
    }

    private void handleFabEvent() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        // Setup View for AlertDialog
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.navigation_fragment_two_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_lists);
        alertDialogBuilder.setTitle(R.string.list_title);
        alertDialogBuilder.setMessage(R.string.list_message);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.list_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getInput(view);
                if (listName.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.no_list_input, Toast.LENGTH_LONG).show();
                } else {
                    putInArrayList();
                }
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.list_negative_button, new DialogInterface.OnClickListener() {
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

    // Create string
    private void getInput(View view) {
        listNameET = (EditText) view.findViewById(R.id.listNameET);
        listName = listNameET.getText().toString();
    }

    private void putInArrayList() {
        addToDB();
        updateList();
        Toast.makeText(getActivity(), listName + " wurde erstellt", Toast.LENGTH_LONG).show();
    }

    private void addToDB(){
        dbAdmin.addList(listName);
    }

    private void attachAdapter() {
        adapter = new NavigationFragmentTwoAdapter(getActivity(), listItems);
        listsLV.setAdapter(adapter);
    }


    private void setOnClickListener() {

        listsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                showOptionDialog(position);
                return true;
            }
        });

        listsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

                // Liste lernen im Lernmodus mit den darin enthaltenen Vokabeln

            }
        });


    }

   private void showOptionDialog(final int position) {

       AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
       builderSingle.setIcon(R.drawable.ic_action_question);
       builderSingle.setTitle(R.string.option_choose);

       final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
       arrayAdapter.add(getResources().getString(R.string.option_learnList));
       arrayAdapter.add(getResources().getString(R.string.option_look_vocab));
       arrayAdapter.add(getResources().getString(R.string.option_edit));
       arrayAdapter.add(getResources().getString(R.string.option_delete));


       builderSingle.setAdapter(
               arrayAdapter,
               new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if (which == 0) {
                           // Lernen. genau so wie normaler klick
                       }
                       if (which == 1) {
                           Intent intent = new Intent(getActivity(), ListVocabActivity.class);
                           Bundle bundle = new Bundle();
                           bundle.putString("listName", listItems.get(position).getName());

                           intent.putExtras(bundle);
                           startActivity(intent);
                       }
                       if (which == 2) {
                            showEdit(position);
                       }
                       if (which == 3) {
                           removeItem(position);
                       }
                   }
               });
       builderSingle.show();
   }

    private void showEdit(final int position) {
        // Setup View for AlertDialog
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.navigation_fragment_two_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(view);

        // Dialog cancelable with back key
        alertDialogBuilder.setCancelable(true);

        // Setup title and message of alertDialog
        alertDialogBuilder.setIcon(R.drawable.ic_lists);
        alertDialogBuilder.setTitle(R.string.list_edit_title);

        // Setup Buttons for dialog
        alertDialogBuilder.setPositiveButton(R.string.list_edit_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getInput(view);
                if (listName.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.list_edit_title, Toast.LENGTH_LONG).show();
                } else {
                    renameList(listItems.get(position));
                }
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.list_negative_button, new DialogInterface.OnClickListener() {
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
    
    protected void renameList (ListItem listItem) {
        dbAdmin.renameList(listItem, listName);
        updateList();
    }

    protected void removeItem(int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(R.string.list_delete_title);
        alert.setIcon(R.drawable.ic_delete);
        alert.setMessage(R.string.list_delete_message);
        alert.setPositiveButton(R.string.delete_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TOD O Auto-generated method stub

                // main code on after clicking yes
                dbAdmin.removeList(listItems.get(deletePosition));
                listItems.remove(deletePosition);
                updateList();

            }
        });
        alert.setNegativeButton(R.string.delete_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }

}

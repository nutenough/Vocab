package app.mueller.schiller.weber.com.vicab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NavigationFragmentTwo extends Fragment {

    private FloatingActionButton fab;
    private ListView listsLV;
    private EditText listNameET;
    private String listName;
    private AlertDialog alertDialog;
    private ArrayList<String> listItems = new ArrayList<>();
    private NavigationFragmentTwoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_fragment_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUIComponents();
        handleFabEvent();
        attachAdapter();
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
        listItems.add(listName);
        adapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), listName + " wurde erstellt", Toast.LENGTH_LONG).show();
    }

    private void attachAdapter() {
        adapter = new NavigationFragmentTwoAdapter(getActivity(), listItems);
        listsLV.setAdapter(adapter);
    }
}
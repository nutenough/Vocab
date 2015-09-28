package app.mueller.schiller.weber.com.vicab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.mueller.schiller.weber.com.vicab.Database.DBAdmin;

public class NavigationFragmentFive extends Fragment {

    View contentView;
    private ImageView startIV;
    private ImageView directionIV;
    private ImageView bagIV;
    private ImageView noteIV;
    private ImageView toolboxIV;
    private ImageView serverIV;
    private ImageView expressIV;
    private ImageView ueberfliegerIV;
    private ImageView spracheIV;
    private TextView startTV;
    private TextView directionTV;
    private TextView bagTV;
    private TextView noteTV;
    private TextView toolboxTV;
    private TextView serverTV;
    private TextView expressTV;
    private TextView ueberfliegerTV;
    private TextView spracheTV;
    private DBAdmin dbAdmin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.navigation_fragment_five, container, false);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUIComponents();
        initDB();
        updateSuccess();
    }

    private void setupUIComponents() {
        // Images
        startIV = (ImageView) contentView.findViewById(R.id.startIV);
        directionIV = (ImageView) contentView.findViewById(R.id.directionIV);
        bagIV = (ImageView) contentView.findViewById(R.id.bagIV);
        noteIV = (ImageView) contentView.findViewById(R.id.noteIV);
        toolboxIV = (ImageView) contentView.findViewById(R.id.toolboxIV);
        serverIV = (ImageView) contentView.findViewById(R.id.serverIV);
        expressIV = (ImageView) contentView.findViewById(R.id.expressIV);
        ueberfliegerIV = (ImageView) contentView.findViewById(R.id.ueberfliegerIV);
        spracheIV = (ImageView) contentView.findViewById(R.id.spracheIV);
        // Texts
        startTV = (TextView) contentView.findViewById(R.id.startTV);
        directionTV = (TextView) contentView.findViewById(R.id.directionTV);
        bagTV = (TextView) contentView.findViewById(R.id.bagTV);
        noteTV = (TextView) contentView.findViewById(R.id.noteTV);
        toolboxTV = (TextView) contentView.findViewById(R.id.toolboxTV);
        serverTV = (TextView) contentView.findViewById(R.id.serverTV);
        expressTV = (TextView) contentView.findViewById(R.id.expressTV);
        ueberfliegerTV = (TextView) contentView.findViewById(R.id.ueberfliegerTV);
        spracheTV = (TextView) contentView.findViewById(R.id.spracheTV);
    }

    private void initDB() {
        dbAdmin = new DBAdmin(getActivity());
        dbAdmin.open();
    }

    public void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }

    // Display trophies for success
    private void updateSuccess() {
        if (dbAdmin.getAllVocabForLanguage().size() >= 1) {
            startIV.setImageResource(R.drawable.start);
            startTV.setText(R.string.start_success);
        }
        if (dbAdmin.getAllVocabForLanguage().size() >= 25) {
            directionIV.setImageResource(R.drawable.direction);
            directionTV.setText(R.string.direction_success);
        }
        if (dbAdmin.getAllVocabForLanguage().size() >= 100) {
            bagIV.setImageResource(R.drawable.bag);
            bagTV.setText(R.string.bag_success);
        }
        if (dbAdmin.getAllListsForChosenLanguage().size() >= 1) {
            noteIV.setImageResource(R.drawable.note);
            noteTV.setText(R.string.note_success);
        }
        if (dbAdmin.getAllListsForChosenLanguage().size() >= 5) {
            toolboxIV.setImageResource(R.drawable.toolbox);
            toolboxTV.setText(R.string.toolbox_success);
        }
        if (dbAdmin.getAllListsForChosenLanguage().size() >= 20) {
            serverIV.setImageResource(R.drawable.server);
            serverTV.setText(R.string.server_success);
        }
        if (dbAdmin.getAllLanguages().size() >= 3) {
            spracheIV.setImageResource(R.drawable.sprache);
            spracheTV.setText(R.string.server_success);
        }
        // Erfolge für Tipp-Modus und Express-Modus fehlerfrei noch zu implementieren
    }
}
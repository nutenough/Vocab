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
    private ImageView noteIV;
    private ImageView serverIV;
    private ImageView expressIV;
    private ImageView ueberfliegerIV;
    private TextView noteTV;
    private TextView serverTV;
    private TextView expressTV;
    private TextView ueberfliegerTV;
    private TextView startTV;
    private TextView directionTV;
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
        noteIV = (ImageView) contentView.findViewById(R.id.noteIV);
        serverIV = (ImageView) contentView.findViewById(R.id.serverIV);
        expressIV = (ImageView) contentView.findViewById(R.id.expressIV);
        ueberfliegerIV = (ImageView) contentView.findViewById(R.id.ueberfliegerIV);
        // Texts
        startTV = (TextView) contentView.findViewById(R.id.startTV);
        directionTV = (TextView) contentView.findViewById(R.id.directionTV);
        noteTV = (TextView) contentView.findViewById(R.id.noteTV);
        serverTV = (TextView) contentView.findViewById(R.id.serverTV);
        expressTV = (TextView) contentView.findViewById(R.id.expressTV);
        ueberfliegerTV = (TextView) contentView.findViewById(R.id.ueberfliegerTV);
    }

    private void initDB() {
        dbAdmin = new DBAdmin(getActivity());
        dbAdmin.open();
    }

    public void onDestroy() {
        dbAdmin.close();
        super.onDestroy();
    }

    private void updateSuccess() {
        if (dbAdmin.getAllVocabForLanguage().size() >= 1) {
            startIV.setImageResource(R.drawable.start);
            startTV.setText("Klappe Auf: Dein erster Vokabeleintrag");
        }
        if (dbAdmin.getAllVocabForLanguage().size() >= 25) {
            directionIV.setImageResource(R.drawable.direction);
            directionTV.setText("Richtige Richtung: 25 Vokabeleinträge");
        }
        if (dbAdmin.getAllListsForChosenLanguage().size() >= 1) {
            noteIV.setImageResource(R.drawable.note);
            noteTV.setText("Klemmbrett: Deine erste Liste");
        }
        if (dbAdmin.getAllListsForChosenLanguage().size() >= 5) {
            serverIV.setImageResource(R.drawable.server);
            serverTV.setText("Organizer: Fünf Listen erstellt");
        }

    }
}
package app.mueller.schiller.weber.com.vicab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.PersistanceClasses.VocItem;

public class NavigationFragmentSixAdapter extends ArrayAdapter<VocItem> {
    public NavigationFragmentSixAdapter(Context context, ArrayList<VocItem> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        VocItem dataItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.navigation_fragment_six_list_row, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.knownVocabCoupleTV);
        // Populate the data into the template view using the data object
        title.setText(dataItem.getSourceVocab() + " - " + dataItem.getTargetVocab());
        // Return the completed view to render on screen



        return convertView;

    }


}


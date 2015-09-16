package app.mueller.schiller.weber.com.vicab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.mueller.schiller.weber.com.vicab.PersistanceClasses.LanguageItem;

public class LanguageAdapter extends ArrayAdapter<LanguageItem> {
    public LanguageAdapter(Context context, ArrayList<LanguageItem> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        LanguageItem dataItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.language_list_row, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.languageCoupleTV);
        // Populate the data into the template view using the data object
        title.setText(dataItem.getCouple());
        // Return the completed view to render on screen
        return convertView;
    }
}

package app.mueller.schiller.weber.com.vicab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddVocabFragmentAdapter extends ArrayAdapter<String> {
    public AddVocabFragmentAdapter(Context context, ArrayList<String> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String dataItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.navigation_fragment_one_list_row, parent, false);
        }
        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.vocabCoupleTV);
        // Populate the data into the template view using the data object
        title.setText(dataItem);
        // Return the completed view to render on screen
        return convertView;
    }
}

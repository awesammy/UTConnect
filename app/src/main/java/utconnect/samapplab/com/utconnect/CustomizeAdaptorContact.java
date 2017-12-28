package utconnect.samapplab.com.utconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by samchen on 2017-11-29.
 */

public class CustomizeAdaptorContact extends ArrayAdapter {
    CustomizeAdaptorContact(Context context, ArrayList<String> contacts){
        super(context,R.layout.service_layout_item,contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.service_layout_item, parent, false);
        }
        return convertView;

    }
}

package com.data4sports.chasecricket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.data4sports.chasecricket.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MatchListAdapter extends ArrayAdapter<JSONObject> {//String> { // Update don 28/07/2021

    customButtonListener customListner;

    public interface customButtonListener {
        public void onViewButtonClickListener(int position,String value);
        public void onResumeButtonClickListener(int position,String value);
        public void onDeleteButtonClickListener(int position,String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    private Context context;
//    private ArrayList<String> data = new ArrayList<String>(); // Commente don 28/07/2021
    private ArrayList<JSONObject> data = new ArrayList<JSONObject>();



//    public  MatchListAdapter(Context context, ArrayList<String> dataTeams) {// Commented on 28/07/2021
    public  MatchListAdapter(Context context, ArrayList<JSONObject> dataTeams) {// Updated  on 28/07/2021
        super(context, R.layout.matchlist, dataTeams);  // matchlist2
        this.data = dataTeams;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.matchlist2, null);//matchlist, null);   // matchlist2
            viewHolder = new ViewHolder();
            viewHolder.slno = (TextView) convertView
                    .findViewById(R.id.list_slno);
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.list_teams);
            viewHolder.match_type = (TextView) convertView
                    .findViewById(R.id.list_type);
            viewHolder.resume = (ImageView) convertView
                    .findViewById(R.id.list_resume_score);
            viewHolder.view = (ImageView) convertView
                    .findViewById(R.id.list_view_score);
            viewHolder.delete = (ImageView) convertView.
                    findViewById(R.id.list_delete_score);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        final String temp = getItem(position);    // Commented on 28/07/2021
        final JSONObject json = getItem(position);    // Commented on 28/07/2021
        String team = null;
        try {
            team = json.getString("teamA") + " vs " + json.getString("teamB");
            String type = json.getString("type");
            String date = json.getString("date");
            String id = json.getString("matchId");
            String str = type + " Match (" + date + ")"+
                    System.getProperty("line.separator") +
                    System.getProperty("line.separator") +"Match ID: "+ id +" ";
            System.getProperty("line.separa" +
                    "");
            viewHolder.slno.setText(String.valueOf(position + 1));
            viewHolder.text.setText(team);
            viewHolder.match_type.setText(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String finalTeam = team;
        viewHolder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onViewButtonClickListener(position, finalTeam);
                }

            }
        });

        viewHolder.resume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onResumeButtonClickListener(position,finalTeam);
                }

            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customListner != null) {
                    customListner.onDeleteButtonClickListener(position, finalTeam);
                }
            }
        });



        return convertView;
    }

    public class ViewHolder {
        TextView slno;
        TextView text;
        TextView match_type;
        ImageView resume;
        ImageView view;
        ImageView delete;
    }
}

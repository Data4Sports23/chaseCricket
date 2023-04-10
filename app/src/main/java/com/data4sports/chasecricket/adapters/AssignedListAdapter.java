package com.data4sports.chasecricket.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.data4sports.chasecricket.R;

import java.util.ArrayList;

public class AssignedListAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> data = new ArrayList<String>();

    //Deepak Code starts
    private ArrayList<Integer> midScore = new ArrayList<Integer>();
    private ArrayList<Integer> gameIdArray = new ArrayList<Integer>();
    //Deepak Code ends
    customButtonListener customListner;

    public AssignedListAdapter(Context context, ArrayList<String> dataTeams, ArrayList<Integer> midScore, ArrayList<Integer> gameIdArray){
        super(context, R.layout.assigned_list,dataTeams);
        this.context = context;
        this.data = dataTeams;
        this.midScore = midScore;
        this.gameIdArray = gameIdArray;
    }

    public interface customButtonListener {
        public void onSaveButtonClickListener(int position,String value);
        // Deepak Code starts
        public void onMidScoreClickListener(int position, Integer gameId);
        // Deepak Code ends
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int i = 0;
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.assigned_list, null);
            viewHolder = new ViewHolder();
            viewHolder.slno = (TextView) convertView
                    .findViewById(R.id.slno);
            viewHolder.text = (TextView) convertView
                    .findViewById(R.id.team);
            /*viewHolder.type = (TextView) convertView
                    .findViewById(R.id.type);*/
            viewHolder.save = (Button) convertView
                    .findViewById(R.id.save_to_device);


            // Deepak Code start
            viewHolder.viewDetailsContainer = (LinearLayout) convertView
                    .findViewById(R.id.view_details_container);
            viewHolder.midScoreContainer = (LinearLayout) convertView
                    .findViewById(R.id.midScoreContainer);
            viewHolder.midScore = (Button) convertView
                    .findViewById(R.id.midScore);
            // Deepak Code ends


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        Log.d("TAG", "getView: test position Value String " + temp);

        //Deepak Code starts

        Log.d("TAG", "getView: Mid score " + midScore + position);
        if (midScore.get(position) == 1){
            viewHolder.midScoreContainer.setVisibility(View.VISIBLE);
            viewHolder.viewDetailsContainer.setVisibility(View.GONE);
        }else {
            viewHolder.midScoreContainer.setVisibility(View.GONE);
            viewHolder.viewDetailsContainer.setVisibility(View.VISIBLE);
        }


        viewHolder.midScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: Click Function");
                if (customListner != null){
                    customListner.onMidScoreClickListener(position,gameIdArray.get(position));
                }
//                if (customListner != null) {
//                    customListner.onSaveButtonClickListener(position,temp);
//                }

            }
        });



        viewHolder.slno.setText("" + (position + 1));
        viewHolder.text.setText(temp);
        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onSaveButtonClickListener(position,temp);
                }
//                if (customListner != null){
//                    customListner.onMidScoreClickListener(position,gameIdArray.get(position));
//                }

            }
        });


        return convertView;
    }



    public class ViewHolder {
        TextView slno;
        TextView text;
//        TextView type;
        Button save,midScore;

        LinearLayout viewDetailsContainer, midScoreContainer;

    }
}

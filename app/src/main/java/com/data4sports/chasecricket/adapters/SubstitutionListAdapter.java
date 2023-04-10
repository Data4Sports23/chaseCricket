package com.data4sports.chasecricket.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.data4sports.chasecricket.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SubstitutionListAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> sub;
//    editSubListener listener;

    public SubstitutionListAdapter(Context context, ArrayList<String> sub) {
        super(context, R.layout.substitution_list, sub);
        this.context = context;
        this.sub = sub;
    }


    /*public interface editSubListener{

        public void onEditSubClickListener(int position, String name);
    }



    public void setCustomListener(editSubListener listener) {
        this.listener = listener;
    }*/



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SubstitutionViewHolder substitutionViewHolder;

        if (convertView == null){

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.substitution_list, null);

            substitutionViewHolder = new SubstitutionViewHolder();
            substitutionViewHolder.slno = convertView.findViewById(R.id.list_sub_slno);
            substitutionViewHolder.playerLeft = convertView.findViewById(R.id.list_sub_left);
            substitutionViewHolder.playerIn = convertView.findViewById(R.id.list_sub_in);
            substitutionViewHolder.team = convertView.findViewById(R.id.list_sub_team);
            substitutionViewHolder.innings = convertView.findViewById(R.id.list_sub_innings);
            substitutionViewHolder.concussion = convertView.findViewById(R.id.list_sub_concussion);
//            substitutionViewHolder.edit = convertView.findViewById(R.id.list_sub_edit);

            convertView.setTag(substitutionViewHolder);

        }

        else {

            substitutionViewHolder = (SubstitutionViewHolder) convertView.getTag();
        }

        final String temp = getItem(position);
        if (!temp.matches("") || temp != null) {

            String[] separated = temp.split(",");

            Log.e("SubListAdapter", "getView, temp : "+temp);
            Log.e("SubListAdapter", "getView, separated : "+ Arrays.toString(separated));


            substitutionViewHolder.slno.setText(String.valueOf(position + 1));
            substitutionViewHolder.playerLeft.setText(separated[0]);
            substitutionViewHolder.playerIn.setText(separated[1]);
            substitutionViewHolder.team.setText(String.valueOf(separated[2]));
            substitutionViewHolder.innings.setText(String.valueOf(separated[3]));
            substitutionViewHolder.concussion.setText(separated[4]);

            /*substitutionViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null){

                        listener.onEditSubClickListener(position, temp);
                    }
                }
            });*/

        }

        else
            Log.e("SubstitutionListAdapter", "getView, temp : "+temp);




        return convertView;
    }


    public class SubstitutionViewHolder {
        TextView slno;
        TextView playerLeft;
        TextView playerIn;
        TextView team;
        TextView innings;
        TextView concussion;
//        ImageView edit;
    }
}

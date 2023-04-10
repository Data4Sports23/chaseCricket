package com.data4sports.chasecricket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.models.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerListAdapter extends ArrayAdapter<Player> {

    private Context context;
//    private ArrayList<String> name = new ArrayList<String>();
    private List<Player> name = new ArrayList<Player>();
    customPlayerListButtonListener customListner;

    public PlayerListAdapter(Context context, ArrayList<Player> playerName){//ArrayList<String> playerName){

        super(context, R.layout.addplayerlist, playerName);
        this.context = context;
        this.name = playerName;
    }


    public interface customPlayerListButtonListener {
        public void onEditClickListener(int position,String value);
        public void onDeleteClickListener(int position,String value);
    }

    public void setCustomListner(customPlayerListButtonListener listener) {
        this.customListner = listener;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        PlayerViewHolder playerViewHolder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.addplayerlist, null);

            playerViewHolder = new PlayerViewHolder();
            playerViewHolder.slno = convertView.findViewById(R.id.list_ap_slno);
            playerViewHolder.name = convertView.findViewById(R.id.list_ap_name);
            playerViewHolder.edit = convertView.findViewById(R.id.list_ap_edit);
            playerViewHolder.delete = convertView.findViewById(R.id.list_ap_delete);

            convertView.setTag(playerViewHolder);
        }
        else {

            playerViewHolder = (PlayerViewHolder) convertView.getTag();
        }

//        final String temp = getItem(position);
        final Player player = getItem(position);
        if (player != null) {

            playerViewHolder.slno.setText(String.valueOf(position + 1));
            playerViewHolder.name.setText(player.getPlayerName());//temp);
            // Added on 17/11/2021
            if (player.getD4s_playerid() > 0) {
                playerViewHolder.edit.setVisibility(View.INVISIBLE);
                playerViewHolder.delete.setVisibility(View.INVISIBLE);
            } else {
                playerViewHolder.edit.setVisibility(View.VISIBLE);
                playerViewHolder.delete.setVisibility(View.VISIBLE);
            }
            // === till here

            playerViewHolder.edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (customListner != null) {
                        customListner.onEditClickListener(position, player.getPlayerName());//temp);
                    }

                }
            });

            playerViewHolder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (customListner != null) {
                        customListner.onDeleteClickListener(position, player.getPlayerName());//temp);
                    }

                }
            });
        }



        return convertView;
    }





    public class PlayerViewHolder {
        TextView slno;
        TextView name;
        ImageView edit;
        ImageView delete;
    }



}

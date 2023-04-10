package com.data4sports.chasecricket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.data4sports.chasecricket.R;
import com.data4sports.chasecricket.models.Player;

import java.util.ArrayList;

public class EditListAdapter extends ArrayAdapter<Player> {

    private Context context;
    private ArrayList<Player> nameList;
    editButtonListener listener;


    public EditListAdapter(Context context, ArrayList<Player> nameList){

        super(context, R.layout.editplayers, nameList);
        this.context = context;
        this.nameList = nameList;

    }


    public interface editButtonListener{

        public void onEditButtonClickListener(Player player);//int position, String name);
    }



    public void setCustomListener(editButtonListener listener) {
        this.listener = listener;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EditViewHolder editViewHolder;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.editplayers, null);

            editViewHolder = new EditViewHolder();
            editViewHolder.slno = convertView.findViewById(R.id.list_e_slno);
            editViewHolder.name = convertView.findViewById(R.id.list_e_name);
            editViewHolder.edit = convertView.findViewById(R.id.list_e_edit);

            convertView.setTag(editViewHolder);
        }

        else {

            editViewHolder = (EditViewHolder) convertView.getTag();
        }

//        final String temp = getItem(position);
        Player temp = getItem(position);

        if (temp != null) {
            editViewHolder.slno.setText(String.valueOf(position + 1));
            editViewHolder.name.setText(temp.getPlayerName());
            if (temp.getPulled() == 1)
                editViewHolder.edit.setVisibility(View.INVISIBLE);
            else
                editViewHolder.edit.setVisibility(View.VISIBLE);
        }


        editViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null){

                    listener.onEditButtonClickListener(/*position, */temp);
                }
            }
        });
        return convertView;
    }




    public class EditViewHolder {
        TextView slno;
        TextView name;
        ImageView edit;

    }
}

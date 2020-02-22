package com.bangla.love_sms.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bangla.love_sms.Interface.ClickValuePass;
import com.bangla.love_sms.Helper.DatabaseHelper;
import com.bangla.love_sms.ModelClass.ModelClass;
import com.bangla.love_sms.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private ArrayList<ModelClass> list;
    private DatabaseHelper databaseHelper;
    //InterstitialAd interstitialAd;
    private ClickValuePass clickValuePass;


    public MessageAdapter(Context context, ArrayList<ModelClass> list,ClickValuePass clickValuePass) {
        this.context = context;
        this.list = list;
        this.clickValuePass=clickValuePass;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        databaseHelper = new DatabaseHelper(context);
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.message_layout, parent, false);
        return new MessageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        final ModelClass model = list.get(position);
        final String id = model.getId();
        final String message = model.getSms();
        final String catagory = model.getCatagory();
        final String favourite = model.getFavourite();

        //Checking Favourite On Starting
        favChecker(holder, id);
        holder.tvHolderMessage.setText(message);
        //Copy To Clipboard

        holder.linLayHolderCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

               clickValuePass.ShowAd();
               copyText(holder, v);

            }
        });
        //Sharing Text
        holder.linLayHolderShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = model.getSms();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent.createChooser(intent, "Share via");
                context.startActivity(intent);
            }
        });
        //Set Favourite
        holder.linLayHolderFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValuePass.ShowAd();
                Cursor cursor = databaseHelper.readFavData(id);
                if (cursor.getCount() == 0) {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    Boolean isUpdated = databaseHelper.updateData(id, message, catagory, "true");

                    if (isUpdated.equals(true)) {
                        Toast.makeText(context, "Added To Favourite", Toast.LENGTH_SHORT).show();
                        holder.ivHolderFavIcon.setImageResource(R.drawable.ic_favourite_select);
                        holder.tvHolderFavSelect.setTextColor(Color.RED);
                    } else {
                        Toast.makeText(context, "Can't Be Done", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    Boolean isUpdated = databaseHelper.updateData(id, message, catagory, "false");
                    if (isUpdated.equals(true)) {
                        Toast.makeText(context, "Removed From Favourite", Toast.LENGTH_SHORT).show();
                        holder.ivHolderFavIcon.setImageResource(R.drawable.ic_favourite);
                        holder.tvHolderFavSelect.setTextColor(Color.BLACK);
                    } else {
                        Toast.makeText(context, "Can't Be Done", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void favChecker(MessageViewHolder holder, String id) {
        Cursor cursor = databaseHelper.readFavData(id);
        if (cursor.getCount() == 0) {
            holder.ivHolderFavIcon.setImageResource(R.drawable.ic_favourite);
            holder.tvHolderFavSelect.setTextColor(Color.BLACK);
        } else {

            holder.ivHolderFavIcon.setImageResource(R.drawable.ic_favourite_select);
            holder.tvHolderFavSelect.setTextColor(Color.RED);
        }
    }
    private void copyText(MessageViewHolder holder, View v) {
        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Message", holder.tvHolderMessage.getText().toString());
        clipboard.setPrimaryClip(data);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvHolderMessage, tvHolderFavSelect;
        LinearLayout linLayHolderFav, linLayHolderCopy, linLayHolderShare;
        ToggleButton toogleButtonHolder;
        ImageView ivHolderFavIcon;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHolderMessage = itemView.findViewById(R.id.tvMsgLayoutMsgId);
            tvHolderFavSelect = itemView.findViewById(R.id.tvMsgLayoutFavId);
            linLayHolderCopy = itemView.findViewById(R.id.linLayMsgLayoutCopy);
            linLayHolderFav = itemView.findViewById(R.id.linLayMsgLayoutFav);
            linLayHolderShare = itemView.findViewById(R.id.linLayMsgLayoutShare);
            ivHolderFavIcon = itemView.findViewById(R.id.ivMsgLayoutFavId);
        }
    }

}
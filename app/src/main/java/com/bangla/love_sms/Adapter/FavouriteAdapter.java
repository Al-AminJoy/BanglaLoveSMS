package com.bangla.love_sms.Adapter;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bangla.love_sms.Interface.ClickValuePass;
import com.bangla.love_sms.Helper.DatabaseHelper;
import com.bangla.love_sms.ModelClass.ModelClass;
import com.bangla.love_sms.Interface.NoMessageShowListener;
import com.bangla.love_sms.R;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavViewHolder> {
    private Context context;
    private ArrayList<ModelClass> list;
    private DatabaseHelper databaseHelper;
    private ClickValuePass clickValuePass;
    private NoMessageShowListener noMessageShowListener;

    public FavouriteAdapter(Context context, ArrayList<ModelClass> list, ClickValuePass clickValuePass, NoMessageShowListener noMessageShowListener) {
        this.context = context;
        this.list = list;
        this.clickValuePass=clickValuePass;
        this.noMessageShowListener=noMessageShowListener;
    }

    @NonNull
    @Override
    public FavouriteAdapter.FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        databaseHelper = new DatabaseHelper(context);
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.message_layout, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavouriteAdapter.FavViewHolder holder, final int position) {
        final ModelClass model = list.get(position);
        final String id = model.getId();
        final String message = model.getSms();
        final String catagory = model.getCatagory();
        final String favourite = model.getFavourite();

        holder.tvFavHolderMessage.setText(message);
        //Favourite Check
        checkFav(holder, id);
        //Sharing Text
        holder.linLayFavHolderShare.setOnClickListener(new View.OnClickListener() {
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
        //Copy To Clipboard
        holder.linLayFavHolderCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValuePass.ShowAd();
                copyText(holder, v);

            }
        });

        holder.linLayFavHolderFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickValuePass.ShowAd();
                favRemover(id, message, catagory, holder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {
        TextView tvFavHolderMessage, tvFavHolderFavSelect;
        LinearLayout linLayFavHolderFav, linLayFavHolderCopy, linLayFavHolderShare;
        ImageView ivFavHolderFavIcon;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFavHolderMessage = itemView.findViewById(R.id.tvMsgLayoutMsgId);
            tvFavHolderFavSelect = itemView.findViewById(R.id.tvMsgLayoutFavId);
            linLayFavHolderCopy = itemView.findViewById(R.id.linLayMsgLayoutCopy);
            linLayFavHolderFav = itemView.findViewById(R.id.linLayMsgLayoutFav);
            linLayFavHolderShare = itemView.findViewById(R.id.linLayMsgLayoutShare);
            ivFavHolderFavIcon = itemView.findViewById(R.id.ivMsgLayoutFavId);
        }
    }

    private void favRemover(String id, String message, String catagory, FavViewHolder holder, int position) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = databaseHelper.readFavData(id);
        if (cursor.getCount() == 0) {
            Toast.makeText(context, "Can't Be Done", Toast.LENGTH_SHORT).show();


        } else {
            deleteItem(position, holder, id, message, catagory);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void checkFav(FavViewHolder holder, String id) {
        Cursor cursor = databaseHelper.readFavData(id);
        if (cursor.getCount() != 0) {
            holder.ivFavHolderFavIcon.setImageResource(R.drawable.ic_favourite_select);
            holder.tvFavHolderFavSelect.setTextColor(Color.RED);
        }
    }

    private void copyText(FavViewHolder holder, View v) {
        ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("Message", holder.tvFavHolderMessage.getText().toString());
        clipboard.setPrimaryClip(data);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();

    }

    private void deleteItem(int position, FavViewHolder holder, String id, String message, String catagory) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Boolean isUpdated = databaseHelper.updateData(id, message, catagory, "false");
        if (isUpdated.equals(true)) {
            Toast.makeText(context, "Removed From Favourite", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Can't Be Done", Toast.LENGTH_SHORT).show();
        }
        list.remove(position);
        if (list.size()==0){
            noMessageShowListener.noMessage(0);
        }
        notifyDataSetChanged();
    }

}

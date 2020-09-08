package com.example.notika;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notika.services.TokenRenewInterceptor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ItemViewHolder> {
    private ArrayList<Menus> menuData;
    private Context context;
    public MenuAdapter(ArrayList<Menus> menuData){

        this.menuData=menuData;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_header,parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Menus menus = menuData.get(position);

        //display the image
        String imageUrl = TokenRenewInterceptor.getImageUrl(context);

        //load into the imageView
        Picasso.get().load(imageUrl).into(holder.img);


       // holder.img.setImageResource(menus.getProfile_img());
        holder.title.setText(menus.getUserName()) ;

    }

    @Override
    public int getItemCount() {
        return menuData.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title;

        public ItemViewHolder(@NonNull View itemView) {

            super(itemView);
            img = itemView.findViewById(R.id.profile_image);
            title = itemView.findViewById(R.id.user_name);
        }
    }

}


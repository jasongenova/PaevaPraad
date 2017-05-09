package com.example.paevapraad;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {
    private List<MenuItem> menuItems;

    public MenuItemAdapter(List<MenuItem> items) {
        menuItems = items;
    }

    @Override
    public MenuItemAdapter.MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);

        return new MenuItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MenuItemAdapter.MenuItemViewHolder holder, int position) {
        MenuItem item = menuItems.get(position);

        holder.name.setText(item.name);
        holder.price.setText(String.valueOf(item.price) + " €");
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView price;

        public MenuItemViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
        }
    }
}

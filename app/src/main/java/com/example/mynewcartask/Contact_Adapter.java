package com.example.mynewcartask;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Contact_Adapter extends RecyclerView.Adapter<Contact_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Contact_Model> contact_models;

    public Contact_Adapter(Context context, ArrayList<Contact_Model> contact_models) {
        this.context = context;
        this.contact_models = contact_models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Contact_Model contact_model = contact_models.get(position);
        if (!TextUtils.isEmpty(contact_model.getContactName())) {
            holder.contactName.setText(contact_model.getContactName());
        }
        if (!TextUtils.isEmpty(contact_model.getContactNumber())) {
            holder.contactNumber.setText(contact_model.getContactNumber());
        }
        holder.remove_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_models.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contact_models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactNumber, remove_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            remove_text = itemView.findViewById(R.id.remove_text);
        }
    }
}

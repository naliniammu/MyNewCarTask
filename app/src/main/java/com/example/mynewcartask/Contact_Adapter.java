package com.example.mynewcartask;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Contact_Adapter extends RecyclerView.Adapter<Contact_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<Contact_Model> arrayList;
    public Contact_Adapter(Context context, ArrayList<Contact_Model> arrayListt) {
        this.context = context;
        this.arrayList = arrayList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact_Model contact_model=arrayList.get(position);
        if (!TextUtils.isEmpty(contact_model.getContactName())) {
            holder.contactName.setText("CONTACT NUMBER - \n" + contact_model.getContactName());
        }
        if (!TextUtils.isEmpty(contact_model.getContactNumber())) {
            holder.contactNumber.setText("CONTACT NUMBER - \n" + contact_model.getContactNumber());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView contactName, contactNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
        }
    }
}

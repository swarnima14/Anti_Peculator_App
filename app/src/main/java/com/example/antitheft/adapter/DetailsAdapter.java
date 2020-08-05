package com.example.antitheft.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antitheft.R;
import com.example.antitheft.model.Details;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
//adapter for recycler view
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> implements Filterable {


    Context context;
    ArrayList<Details> details;
    ArrayList<Details> detailsArrayList;

    public DetailsAdapter(Context c , ArrayList<Details> d)
    {
        context = c;
        details = d;
        this.detailsArrayList=new ArrayList<>(details);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.details_row,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvName.setText("Name:\n"+details.get(position).getName());
       holder.tvCar.setText(details.get(position).getCarNum());
        holder.tvPin.setText("Pin: "+details.get(position).getPin());
        holder.tvDistrict.setText("District: "+details.get(position).getDistrict());
        holder.tvPhone.setText("Contact: "+details.get(position).getPhone());

        //call intent when clicked
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+details.get(position).getPhone()));
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 99);
                }
                else
                {
                    context.startActivity(callIntent);
                }

            }
        });

       boolean isExpanded = details.get(position).isExpanded();
       holder.materialCardView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Details> filteredList=new ArrayList<>();
            if(charSequence.toString().isEmpty())
            {
                filteredList.addAll(detailsArrayList);
            }
            else
            {
                for(Details car:detailsArrayList)
                {
                    if(car.toString().toLowerCase().contains(charSequence.toString().toLowerCase()))
                    {
                        filteredList.add(car);
                    }
                }
            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            details.clear();
            details=(ArrayList<Details>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvPhone,tvName,tvPin,tvDistrict,tvCar;
        MaterialCardView layout;
        CardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPhone=itemView.findViewById(R.id.tvPhone);
            tvCar=itemView.findViewById(R.id.tvCar);
            tvPin=itemView.findViewById(R.id.tvPin);
            tvDistrict=itemView.findViewById(R.id.tvDistrict);
            tvName=itemView.findViewById(R.id.tvName);
            layout=itemView.findViewById(R.id.layout);
            materialCardView=itemView.findViewById(R.id.expandableLayout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View view) {
                    Details d = details.get(getAdapterPosition());
                    d.setExpanded(!d.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }

}

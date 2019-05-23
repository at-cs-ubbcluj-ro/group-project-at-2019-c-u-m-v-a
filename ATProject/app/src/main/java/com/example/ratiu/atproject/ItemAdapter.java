package com.example.ratiu.atproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Comparator;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private List<List<Integer>> measurements;
    private Context context;
    private int[] limits;

    public void setLimits(int[] limits) {
        this.limits = limits;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View adapterView;

        public MyViewHolder(View v) {
            super(v);
            adapterView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(List<List<Integer>> measurements,int[] limits) {
        this.measurements = measurements;
        this.limits=limits;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final List<Integer> line = measurements.get(position);
        Integer value=line.get(1);
        Integer id=line.get(0);
        context = holder.adapterView.getContext();
        if(value<limits[0])
            holder.adapterView.setBackgroundColor(holder.adapterView.getResources().getColor(R.color.lowest));
        else if(value<limits[1])
            holder.adapterView.setBackgroundColor(holder.adapterView.getResources().getColor(R.color.low_to_mid));
        else if(value<limits[2])
            holder.adapterView.setBackgroundColor(holder.adapterView.getResources().getColor(R.color.mid_to_high));
        else
            holder.adapterView.setBackgroundColor(holder.adapterView.getResources().getColor(R.color.highest));

        TextView measurementId = holder.adapterView.findViewById(R.id.id);
        measurementId.setText(String.valueOf(id));

        TextView measurementValue = holder.adapterView.findViewById(R.id.value);
        measurementValue.setText(String.valueOf(value));
    }

    @Override
    public int getItemCount() {
        if (measurements == null)
            return 0;
        return measurements.size();
    }

    public void setNewList(List<List<Integer>> newList) {
        measurements = newList;
        sortById();
    }

    public void sortById(){
        measurements.sort(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> first, List<Integer> second) {
                return -first.get(0)+second.get(0);
            }
        });
        notifyDataSetChanged();
    }

    public void sortByValue(){
        measurements.sort(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> first, List<Integer> second) {
                return first.get(1)-second.get(1);
            }
        });
        notifyDataSetChanged();
    }
}
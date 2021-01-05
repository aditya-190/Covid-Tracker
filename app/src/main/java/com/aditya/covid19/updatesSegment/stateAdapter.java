package com.aditya.covid19.updatesSegment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.covid19.R;

import java.util.ArrayList;

public class stateAdapter extends RecyclerView.Adapter<stateAdapter.stateViewHolder> {

    private final ArrayList<state> data;

    public stateAdapter(ArrayList<state> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public stateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new stateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.updates_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull stateViewHolder holder, int position) {
        state singleState = data.get(position);

        holder.state.setText(singleState.getState());
        holder.active.setText(singleState.getActive());
        holder.recover.setText(singleState.getRecover());
        holder.death.setText(singleState.getDeath());
        holder.confirm.setText(singleState.getConfirm());

        holder.confirm_inc.setText(singleState.getConfirm_inc());
        holder.death_inc.setText(singleState.getDeath_inc());
        holder.recover_inc.setText(singleState.getRecover_inc());

        if (singleState.getConfirm_inc().equals("0")) {
            holder.confirm_inc.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            holder.confirm_inc.setText("");
        }
        if (singleState.getDeath_inc().equals("0")) {
            holder.death_inc.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            holder.death_inc.setText("");
        }
        if (singleState.getRecover_inc().equals("0")) {
            holder.recover_inc.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            holder.recover_inc.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class stateViewHolder extends RecyclerView.ViewHolder {

        TextView state, active, recover, death, confirm, recover_inc, death_inc, confirm_inc;

        public stateViewHolder(@NonNull View itemView) {
            super(itemView);

            state = itemView.findViewById(R.id.state);
            active = itemView.findViewById(R.id.active);
            recover = itemView.findViewById(R.id.recover);
            death = itemView.findViewById(R.id.death);
            confirm = itemView.findViewById(R.id.confirm);
            recover_inc = itemView.findViewById(R.id.recover_inc);
            death_inc = itemView.findViewById(R.id.death_inc);
            confirm_inc = itemView.findViewById(R.id.confirm_inc);
        }
    }
}

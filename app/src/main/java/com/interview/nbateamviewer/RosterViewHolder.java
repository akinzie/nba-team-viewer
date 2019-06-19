package com.interview.nbateamviewer;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RosterViewHolder extends RecyclerView.ViewHolder {
    LinearLayout rosterView;
    public RosterViewHolder(@NonNull LinearLayout itemView) {
        super(itemView);
        rosterView = itemView;
    }
}

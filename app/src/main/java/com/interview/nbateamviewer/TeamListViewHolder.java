package com.interview.nbateamviewer;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeamListViewHolder extends RecyclerView.ViewHolder {
    LinearLayout teamListView;
    public TeamListViewHolder(@NonNull LinearLayout itemView) {
        super(itemView);
        teamListView = itemView;
    }
}

package com.interview.nbateamviewer;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class TeamListRecyclerAdapter extends RecyclerView.Adapter implements DataChangeListener {
    private static final String LOGGER_TAG = "TeamViewer:RcyclrAdptr";
    @NonNull
    private DataModel dataModel;
    private Team[] data;
    private WeakReference<Activity> activityWeakReference;
    private Comparator<Team> teamComparator;

    public TeamListRecyclerAdapter(@NonNull Activity activity, @NonNull DataModel dataModel) {
        Objects.requireNonNull(activity);
        Objects.requireNonNull(dataModel);
        activityWeakReference = new WeakReference<>(activity);
        this.dataModel = dataModel;
        dataModel.setDataChangeListener(this);
        teamComparator = this::compareTeamNameAscending;
        data = dataModel.getData();
        sort();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout teamListView = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_list_view, parent, false);
        return new TeamListViewHolder(teamListView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (data == null || position >= data.length) {
            Log.w(LOGGER_TAG, "onBindViewHolder called for position out of bounds.");
            return;
        }
        TeamListViewHolder teamListViewHolder = (TeamListViewHolder)holder;
        LinearLayout teamListView = teamListViewHolder.teamListView;
        ((TextView)teamListView.findViewById(R.id.teamListNameLabel)).setText(data[position].getFullName());
        ((TextView)teamListView.findViewById(R.id.teamListWins)).setText(Integer.toString(data[position].getWins()));
        ((TextView)teamListView.findViewById(R.id.teamListLosses)).setText(Integer.toString(data[position].getLosses()));
        teamListView.setOnClickListener(v -> {
            Activity activity = activityWeakReference.get();
            if (activity != null) {
                Intent intent = TeamPageActivity.getStartIntent(activity, data[position]);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }

    public void setSortOrder(@NonNull SortOrder sortOrder) {
        switch (sortOrder) {
            case NAME_ASCENDING:
                teamComparator = this::compareTeamNameAscending;
                break;
            case NAME_DESCENDING:
                teamComparator = this::compareTeamNameDescending;
                break;
            case WINS_ASCENDING:
                teamComparator = this::compareTeamWinsAscending;
                break;
            case WINS_DESCENDING:
                teamComparator = this::compareTeamWinsDescending;
                break;
            case LOSSES_ASCENDING:
                teamComparator = this::compareTeamLossesAscending;
                break;
            case LOSSES_DESCENDING:
                teamComparator = this::compareTeamLossesDescending;
                break;
        }
        sort();
    }

    @Override
    public void onDataChanged() {
        data = dataModel.getData();
        sort();
    }

    /**
     * This method sorts the data according to {@link this#teamComparator}, then calls
     * {@link RecyclerView.Adapter#notifyDataSetChanged()}.
     */
    private void sort() {
        if (data != null) {
            Arrays.sort(data, teamComparator);
        }
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            activity.runOnUiThread(this::notifyDataSetChanged);
        }
    }

    /**
     * One of the comparators that {@link this#teamComparator} might reference.
     */
    private int compareTeamNameAscending(Team a, Team b) {
        return a.getFullName().compareTo(b.getFullName());
    }

    /**
     * One of the comparators that {@link this#teamComparator} might reference.
     */
    private int compareTeamNameDescending(Team a, Team b) {
        return b.getFullName().compareTo(a.getFullName());
    }

    /**
     * One of the comparators that {@link this#teamComparator} might reference.
     */
    private int compareTeamWinsAscending(Team a, Team b) {
        return a.getWins() - b.getWins();
    }

    /**
     * One of the comparators that {@link this#teamComparator} might reference.
     */
    private int compareTeamWinsDescending(Team a, Team b) {
        return b.getWins() - a.getWins();
    }

    /**
     * One of the comparators that {@link this#teamComparator} might reference.
     */
    private int compareTeamLossesAscending(Team a, Team b) {
        return a.getLosses() - b.getLosses();
    }

    /**
     * One of the comparators that {@link this#teamComparator} might reference.
     */
    private int compareTeamLossesDescending(Team a, Team b) {
        return b.getLosses() - a.getLosses();
    }
}

package com.interview.nbateamviewer;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TeamListRecyclerAdapterUnitTests {

    @Test
    public void testConstructor() {
        Activity activity = mock(Activity.class);
        DataModel dataModel = new DataModel(ApplicationProvider.getApplicationContext());
        TeamListRecyclerAdapter teamListRecyclerAdapter = new TeamListRecyclerAdapter(activity, dataModel);
        try {
            teamListRecyclerAdapter = new TeamListRecyclerAdapter(null, dataModel);
            fail("No null pointer when passing null player[] to constructor.");
        } catch (NullPointerException e) {
            // expected.
        }
        try {
            teamListRecyclerAdapter = new TeamListRecyclerAdapter(activity, null);
            fail("No null pointer when passing null player[] to constructor.");
        } catch (NullPointerException e) {
            // expected.
        }
    }

    @Test
    public void testItemCount() {
        int id = 838;
        String fullName = "Basket Weavers";
        int wins = 32;
        int losses = 31;
        Player[] players = new Player[]{};
        Team team = new Team(id, fullName, wins, losses, players);
        Activity activity = mock(Activity.class);
        DataModel dataModel = mock(DataModel.class);
        when(dataModel.getData()).thenReturn(new Team[]{});
        TeamListRecyclerAdapter teamListRecyclerAdapter = new TeamListRecyclerAdapter(activity, dataModel);
        assertEquals(0, teamListRecyclerAdapter.getItemCount());
        dataModel = mock(DataModel.class);
        when(dataModel.getData()).thenReturn(new Team[]{team, team, team});
        teamListRecyclerAdapter = new TeamListRecyclerAdapter(activity, dataModel);
        assertEquals(3, teamListRecyclerAdapter.getItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        Activity activity = mock(Activity.class);
        DataModel dataModel = mock(DataModel.class);
        when(dataModel.getData()).thenReturn(new Team[]{});
        TeamListRecyclerAdapter teamListRecyclerAdapter = new TeamListRecyclerAdapter(activity, dataModel);
        ViewGroup viewGroup = mock(ViewGroup.class);
        when(viewGroup.getContext()).thenReturn(ApplicationProvider.getApplicationContext());
        ViewGroup.LayoutParams layoutParams = mock(ViewGroup.LayoutParams.class);
        when(viewGroup.generateLayoutParams(any(AttributeSet.class))).thenReturn(layoutParams);
        RecyclerView.ViewHolder viewHolder = teamListRecyclerAdapter.onCreateViewHolder(viewGroup, 0);
        assertTrue(viewHolder instanceof TeamListViewHolder);
    }

    @Test
    public void testOnBind() {
        int id = 838;
        String fullName = "Basket Weavers";
        int wins = 32;
        int losses = 31;
        Player[] players = new Player[]{};
        Team team = new Team(id, fullName, wins, losses, players);
        Activity activity = mock(Activity.class);
        DataModel dataModel = mock(DataModel.class);
        when(dataModel.getData()).thenReturn(new Team[]{team});
        TeamListRecyclerAdapter teamListRecyclerAdapter = new TeamListRecyclerAdapter(activity, dataModel);
        LinearLayout teamListView = mock(LinearLayout.class);
        TextView teamNameTextView = mock(TextView.class);
        TextView teamWinsTextView = mock(TextView.class);
        TextView teamLossesTextView = mock(TextView.class);
        when(teamListView.findViewById(R.id.teamListNameLabel)).thenReturn(teamNameTextView);
        when(teamListView.findViewById(R.id.teamListWins)).thenReturn(teamWinsTextView);
        when(teamListView.findViewById(R.id.teamListLosses)).thenReturn(teamLossesTextView);
        TeamListViewHolder teamListViewHolder = new TeamListViewHolder(teamListView);
        teamListRecyclerAdapter.onBindViewHolder(teamListViewHolder, 0);
        verify(teamNameTextView).setText(fullName);
        verify(teamWinsTextView).setText(Integer.toString(wins));
        verify(teamLossesTextView).setText(Integer.toString(losses));
    }
}

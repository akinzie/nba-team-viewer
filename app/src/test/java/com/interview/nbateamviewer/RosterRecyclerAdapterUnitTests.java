package com.interview.nbateamviewer;

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
public class RosterRecyclerAdapterUnitTests {

    @Test
    public void testConstructor() {
        RosterRecyclerAdapter rosterRecyclerAdapter = new RosterRecyclerAdapter(new Player[]{});
        try {
            rosterRecyclerAdapter = new RosterRecyclerAdapter(null);
            fail("No null pointer when passing null player[] to constructor.");
        } catch (NullPointerException e) {
            // expected.
        }
    }

    @Test
    public void testItemCount() {
        RosterRecyclerAdapter rosterRecyclerAdapter = new RosterRecyclerAdapter(new Player[]{});
        assertEquals(0, rosterRecyclerAdapter.getItemCount());
        rosterRecyclerAdapter = new RosterRecyclerAdapter(new Player[]{new Player(), new Player(), new Player()});
        assertEquals(3, rosterRecyclerAdapter.getItemCount());
    }

    @Test
    public void testOnCreateViewHolder() {
        RosterRecyclerAdapter rosterRecyclerAdapter = new RosterRecyclerAdapter(new Player[]{});
        ViewGroup viewGroup = mock(ViewGroup.class);
        when(viewGroup.getContext()).thenReturn(ApplicationProvider.getApplicationContext());
        ViewGroup.LayoutParams layoutParams = mock(ViewGroup.LayoutParams.class);
        when(viewGroup.generateLayoutParams(any(AttributeSet.class))).thenReturn(layoutParams);
        RecyclerView.ViewHolder viewHolder = rosterRecyclerAdapter.onCreateViewHolder(viewGroup, 0);
        assertTrue(viewHolder instanceof RosterViewHolder);
    }

    @Test
    public void testOnBind() {
        int id = 838;
        int number = 42;
        String position = "PG";
        String firstName = "Michael";
        String lastName = "Jordan";
        RosterRecyclerAdapter rosterRecyclerAdapter = new RosterRecyclerAdapter(new Player[]{new Player(id, firstName, lastName, position, number)});
        LinearLayout rosterView = mock(LinearLayout.class);
        TextView firstNameTextView = mock(TextView.class);
        TextView lastNameTextView = mock(TextView.class);
        TextView positionTextView = mock(TextView.class);
        TextView numberTextView = mock(TextView.class);
        when(rosterView.findViewById(R.id.playerFirstName)).thenReturn(firstNameTextView);
        when(rosterView.findViewById(R.id.playerLastName)).thenReturn(lastNameTextView);
        when(rosterView.findViewById(R.id.playerPosition)).thenReturn(positionTextView);
        when(rosterView.findViewById(R.id.playerNumber)).thenReturn(numberTextView);
        RosterViewHolder rosterViewHolder = new RosterViewHolder(rosterView);
        rosterRecyclerAdapter.onBindViewHolder(rosterViewHolder, 0);
        verify(firstNameTextView).setText(firstName);
        verify(lastNameTextView).setText(lastName);
        verify(positionTextView).setText(position);
        verify(numberTextView).setText(Integer.toString(number));
    }
}

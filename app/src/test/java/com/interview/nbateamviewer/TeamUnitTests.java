package com.interview.nbateamviewer;

import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TeamUnitTests {
    @Test
    public void testTeamDefaults() {
        Team team = new Team();
        assertEquals(0, team.getId());
        assertEquals(0, team.getWins());
        assertEquals(0, team.getLosses());
        assertNull(team.getFullName());
        assertNull(team.getPlayers());
    }

    @Test
    public void testTeamInfo() {
        int id = 103;
        int wins = 44;
        int losses = 24;
        String fullName = "Basket Weavers";
        Player[] players = new Player[]{};
        Team team = new Team(id, fullName, wins, losses, players);
        assertEquals(id, team.getId());
        assertEquals(wins, team.getWins());
        assertEquals(losses, team.getLosses());
        assertEquals(fullName, team.getFullName());
        assertArrayEquals(players, team.getPlayers());
    }

    @Test
    public void testNonNullEnforcement() {
        int id = 103;
        int wins = 44;
        int losses = 24;
        String fullName = "Basket Weavers";
        Player[] players = new Player[]{};
        try {
            Team team = new Team(id, null, wins, losses, players);
            fail("No null pointer exception on unexpected null parameter.");
        } catch (NullPointerException e) {
            // expected.
        }
        try {
            Team team = new Team(id, fullName, wins, losses, null);
            fail("No null pointer exception on unexpected null parameter.");
        } catch (NullPointerException e) {
            // expected.
        }
    }
}

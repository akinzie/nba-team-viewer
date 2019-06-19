package com.interview.nbateamviewer;

import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlayerUnitTests {
    @Test
    public void testPlayerDefaults() {
        Player player = new Player();
        assertEquals(0, player.getId());
        assertEquals(0, player.getNumber());
        assertNull(player.getFirstName());
        assertNull(player.getLastName());
        assertNull(player.getPosition());
    }

    @Test
    public void testPlayerInfo() {
        int id = 838;
        int number = 42;
        String position = "PG";
        String firstName = "Michael";
        String lastName = "Jordan";
        Player player = new Player(id, firstName, lastName, position, number);
        assertEquals(id, player.getId());
        assertEquals(firstName, player.getFirstName());
        assertEquals(lastName, player.getLastName());
        assertEquals(position, player.getPosition());
        assertEquals(number, player.getNumber());
    }

    @Test
    public void testNonNullEnforcement() {
        int id = 838;
        int number = 42;
        String position = "PG";
        String firstName = "Michael";
        String lastName = "Jordan";
        try {
            Player player = new Player(id, null, lastName, position, number);
            fail("No null pointer on unexpected null parameter.");
        } catch (NullPointerException e) {
            // expected.
        }
        try {
            Player player = new Player(id, firstName, null, position, number);
            fail("No null pointer on unexpected null parameter.");
        } catch (NullPointerException e) {
            // expected.
        }
        try {
            Player player = new Player(id, firstName, lastName, null, number);
            fail("No null pointer on unexpected null parameter.");
        } catch (NullPointerException e) {
            // expected.
        }
    }
}

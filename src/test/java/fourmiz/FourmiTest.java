package fourmiz;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class FourmiTest {

    // ── Constructor ──────────────────────────────────────────────────────────

    @Test
    public void constructor_setsPosition() {
        Fourmi f = new Fourmi(3, 7);
        assertEquals(3, f.getx());
        assertEquals(7, f.gety());
    }

    @Test
    public void constructor_nourritureIsFalse() {
        assertFalse(new Fourmi(0, 0).nourriture);
    }

    @Test
    public void constructor_initialWeightsAreEight_allTwelve() {
        Fourmi f = new Fourmi(0, 0);
        assertEquals(8, f.poids.length);
        for (int w : f.poids) {
            assertEquals(12, w);
        }
    }

    // ── Getters / setters ────────────────────────────────────────────────────

    @Test
    public void setx_getx_roundTrip() {
        Fourmi f = new Fourmi(0, 0);
        f.setx(15);
        assertEquals(15, f.getx());
    }

    @Test
    public void sety_gety_roundTrip() {
        Fourmi f = new Fourmi(0, 0);
        f.sety(9);
        assertEquals(9, f.gety());
    }

    // ── Movement: setxy (private — tested via reflection) ───────────────────

    /**
     * Direction encoding used by setxy:
     *  0 → NW (x-1, y-1)   1 → N  (x,   y-1)   2 → NE (x+1, y-1)
     *  3 → E  (x+1, y  )   4 → SE (x+1, y+1)   5 → S  (x,   y+1)
     *  6 → SW (x-1, y+1)   7 → W  (x-1, y  )
     */
    @Test
    public void setxy_allEightDirections() throws Exception {
        Method setxy = Fourmi.class.getDeclaredMethod("setxy", int.class);
        setxy.setAccessible(true);

        int[][] deltas = {
            {-1, -1}, // 0 NW
            { 0, -1}, // 1 N
            { 1, -1}, // 2 NE
            { 1,  0}, // 3 E
            { 1,  1}, // 4 SE
            { 0,  1}, // 5 S
            {-1,  1}, // 6 SW
            {-1,  0}, // 7 W
        };

        for (int dir = 0; dir < 8; dir++) {
            Fourmi f = new Fourmi(5, 5);
            setxy.invoke(f, dir);
            assertEquals("x delta wrong for direction " + dir,
                    5 + deltas[dir][0], f.getx());
            assertEquals("y delta wrong for direction " + dir,
                    5 + deltas[dir][1], f.gety());
        }
    }

    // ── Movement: deplacerrand ───────────────────────────────────────────────

    @Test
    public void deplacerrand_movesByAtMostOneStep() {
        Fourmi f = new Fourmi(10, 10);
        for (int i = 0; i < 200; i++) {
            int prevX = f.getx();
            int prevY = f.gety();
            f.deplacerrand();
            assertTrue("x moved by more than 1: " + prevX + " → " + f.getx(),
                    Math.abs(f.getx() - prevX) <= 1);
            assertTrue("y moved by more than 1: " + prevY + " → " + f.gety(),
                    Math.abs(f.gety() - prevY) <= 1);
            f.setx(10);
            f.sety(10);
        }
    }

    @Test
    public void deplacerrand_weightsSumUnchanged() {
        // setpoids rewrites the array from a fixed all-12 template,
        // so the total weight should always remain 96 (8 × 12).
        Fourmi f = new Fourmi(5, 5);
        for (int i = 0; i < 50; i++) {
            f.deplacerrand();
            int sum = 0;
            for (int w : f.poids) sum += w;
            assertEquals("Weight sum should remain 96 after deplacerrand", 96, sum);
        }
    }

    // ── setpoids (private — tested via reflection) ───────────────────────────

    @Test
    public void setpoids_allValuesRemainTwelve() throws Exception {
        // The current implementation copies from an all-12 template, so every
        // entry should still be 12 regardless of the starting index.
        Method setpoids = Fourmi.class.getDeclaredMethod("setpoids", int.class);
        setpoids.setAccessible(true);

        for (int max = 0; max < 8; max++) {
            Fourmi f = new Fourmi(0, 0);
            setpoids.invoke(f, max);
            for (int j = 0; j < 8; j++) {
                assertEquals("poids[" + j + "] should be 12 after setpoids(" + max + ")",
                        12, f.poids[j]);
            }
        }
    }

    @Test
    public void setpoids_wrapsCorrectlyAtBoundary() throws Exception {
        // max = 7 is the highest valid index; the loop must wrap i from 7 to 0
        // without ArrayIndexOutOfBoundsException.
        Method setpoids = Fourmi.class.getDeclaredMethod("setpoids", int.class);
        setpoids.setAccessible(true);

        Fourmi f = new Fourmi(0, 0);
        try {
            setpoids.invoke(f, 7);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new AssertionError("setpoids(7) threw unexpectedly: " + e.getCause(), e.getCause());
        }
    }
}

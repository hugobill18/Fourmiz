package fourmiz;

import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.*;

/**
 * Tests for the Bloc class hierarchy: Vide, Fourmiliere, Obstacle.
 * Also covers the shared mvtfourmi() logic inherited from Bloc.
 */
public class BlocTest {

    // ── Vide ─────────────────────────────────────────────────────────────────

    @Test
    public void vide_isAccessible() {
        assertTrue(new Vide().getpossibility());
    }

    @Test
    public void vide_initialNourritureIsZero() {
        assertEquals(0, new Vide().nourriture);
    }

    @Test
    public void vide_initialFourmisIsZero() {
        assertEquals(0, new Vide().fourmis);
    }

    @Test
    public void vide_setfourmis() {
        Vide v = new Vide();
        v.setfourmis(3);
        assertEquals(3, v.fourmis);
    }

    @Test
    public void vide_setnourriture() {
        Vide v = new Vide();
        v.setnourriture(10);
        assertEquals(10, v.nourriture);
    }

    // ── Fourmiliere ──────────────────────────────────────────────────────────

    @Test
    public void fourmiliere_isAccessible() {
        assertTrue(new Fourmiliere().getpossibility());
    }

    @Test
    public void fourmiliere_colorIsRed() {
        assertEquals(Color.RED, new Fourmiliere().couleur);
    }

    @Test
    public void fourmiliere_setfourmis() {
        Fourmiliere f = new Fourmiliere();
        f.setfourmis(5);
        assertEquals(5, f.fourmis);
    }

    @Test
    public void fourmiliere_setnourriture() {
        Fourmiliere f = new Fourmiliere();
        f.setnourriture(20);
        assertEquals(20, f.nourriture);
    }

    // ── Obstacle ─────────────────────────────────────────────────────────────

    @Test
    public void obstacle_isNotAccessible() {
        assertFalse(new Obstacle().getpossibility());
    }

    @Test
    public void obstacle_colorIsGray() {
        assertEquals(Color.GRAY, new Obstacle().couleur);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void obstacle_setfourmis_throwsUnsupportedOperation() {
        new Obstacle().setfourmis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void obstacle_setnourriture_throwsUnsupportedOperation() {
        new Obstacle().setnourriture(1);
    }

    // ── mvtfourmi (shared Bloc logic) ────────────────────────────────────────

    @Test
    public void mvtfourmi_true_incrementsFourmis() {
        Vide v = new Vide();
        v.setfourmis(2);
        v.mvtfourmi(true);
        assertEquals(3, v.fourmis);
    }

    @Test
    public void mvtfourmi_false_decrementsFourmis() {
        Vide v = new Vide();
        v.setfourmis(2);
        v.mvtfourmi(false);
        assertEquals(1, v.fourmis);
    }

    @Test
    public void mvtfourmi_false_underflow_bug() {
        // Known bug: calling mvtfourmi(false) when fourmis == 0 produces -1.
        // This test documents the current (incorrect) behaviour.
        // A fix should either throw an IllegalStateException or clamp to 0.
        Vide v = new Vide();
        assertEquals(0, v.fourmis);
        v.mvtfourmi(false);
        assertEquals("Bug: ant count went negative", -1, v.fourmis);
    }

    @Test
    public void mvtfourmi_multipleIncrements() {
        Vide v = new Vide();
        for (int i = 1; i <= 5; i++) {
            v.mvtfourmi(true);
            assertEquals(i, v.fourmis);
        }
    }
}

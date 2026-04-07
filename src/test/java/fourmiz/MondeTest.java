package fourmiz;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for Monde: terrain loading and simulation stepping.
 *
 * terrain.dat layout (6 rows × 5 cols, 5 ants):
 *   row 0: [ 0,  0, -1,  0,  0]   -1 = Fourmiliere at (0,2)
 *   row 1: [ 0,  0,  0,  0,  0]
 *   row 2: [ 0, -2, -2, -2,  0]   -2 = Obstacle at (2,1),(2,2),(2,3)
 *   row 3: [ 0,  0,  0,  0,  0]
 *   row 4: [ 0,  0, 50,  0,  0]   50 food at (4,2)
 *   row 5: [ 0,  0,  0,  0,  0]
 */
public class MondeTest {

    // ── chargement (file parsing) ────────────────────────────────────────────

    @Test
    public void chargement_correctDimensions() {
        Monde m = new Monde("terrain.dat");
        assertEquals(6, m.getLignes());
        assertEquals(5, m.getColonnes());
    }

    @Test
    public void chargement_correctAntCount() {
        Monde m = new Monde("terrain.dat");
        assertEquals(5, m.fourmis);
        assertEquals(5, m.fourmi.length);
    }

    @Test
    public void chargement_fourmiliereAtCorrectPosition() {
        Monde m = new Monde("terrain.dat");
        assertTrue("Expected Fourmiliere at (0,2)",
                m.terrain[0][2] instanceof Fourmiliere);
    }

    @Test
    public void chargement_obstaclesAtCorrectPositions() {
        Monde m = new Monde("terrain.dat");
        assertTrue("Expected Obstacle at (2,1)", m.terrain[2][1] instanceof Obstacle);
        assertTrue("Expected Obstacle at (2,2)", m.terrain[2][2] instanceof Obstacle);
        assertTrue("Expected Obstacle at (2,3)", m.terrain[2][3] instanceof Obstacle);
    }

    @Test
    public void chargement_videBlocksAtOpenCells() {
        Monde m = new Monde("terrain.dat");
        assertTrue("Expected Vide at (0,0)", m.terrain[0][0] instanceof Vide);
        assertTrue("Expected Vide at (1,1)", m.terrain[1][1] instanceof Vide);
        assertTrue("Expected Vide at (5,4)", m.terrain[5][4] instanceof Vide);
    }

    @Test
    public void chargement_foodCountPreserved() {
        Monde m = new Monde("terrain.dat");
        assertEquals(50, m.terrain[4][2].nourriture);
    }

    @Test
    public void chargement_antsSpawnAtFourmiliere() {
        Monde m = new Monde("terrain.dat");
        for (Fourmi f : m.fourmi) {
            assertEquals("Ants should start at Fourmiliere row", 0, f.getx());
            assertEquals("Ants should start at Fourmiliere col", 2, f.gety());
        }
    }

    // ── step ─────────────────────────────────────────────────────────────────

    @Test
    public void step_antsRemainInBounds() {
        Monde m = new Monde("terrain.dat");
        for (int s = 0; s < 200; s++) {
            m.step();
            for (Fourmi f : m.fourmi) {
                assertTrue("Ant x out of bounds: " + f.getx(),
                        f.getx() >= 0 && f.getx() < m.getLignes());
                assertTrue("Ant y out of bounds: " + f.gety(),
                        f.gety() >= 0 && f.gety() < m.getColonnes());
            }
        }
    }

    @Test
    public void step_antsNeverLandOnObstacle() {
        Monde m = new Monde("terrain.dat");
        for (int s = 0; s < 200; s++) {
            m.step();
            for (Fourmi f : m.fourmi) {
                assertTrue("Ant landed on an impassable tile at ("
                        + f.getx() + "," + f.gety() + ")",
                        m.terrain[f.getx()][f.gety()].getpossibility());
            }
        }
    }

    @Test
    public void step_fourmisCountConsistent() {
        // The total number of ants tracked across all tiles should always equal
        // the number of Fourmi objects (each ant occupies exactly one tile).
        Monde m = new Monde("terrain.dat");
        for (int s = 0; s < 50; s++) {
            m.step();
            int total = 0;
            for (int r = 0; r < m.getLignes(); r++) {
                for (int c = 0; c < m.getColonnes(); c++) {
                    total += m.terrain[r][c].fourmis;
                }
            }
            assertEquals("Tile ant counts don't sum to total ants after step " + s,
                    m.fourmis, total);
        }
    }

    // ── Known bug: infinite loop when ant is surrounded by obstacles ─────────

    @Ignore("Known bug: step() infinite-loops when an ant has no valid neighbour. "
          + "Fix by adding a maximum-retry guard in Monde.step(). "
          + "Remove @Ignore once the bug is resolved.")
    @Test
    public void step_surroundedByObstacles_doesNotInfiniteLoop() {
        // terrain_surrounded.dat: 3×3 grid, single ant at (1,1) = Fourmiliere,
        // all 8 neighbours are Obstacles → the retry loop in step() never exits.
        Monde m = new Monde("src/test/resources/terrain_surrounded.dat");
        m.step(); // would loop forever without a fix
    }
}

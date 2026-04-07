package fourmiz;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the Obstacle block.
 * Obstacle is impassable and rejects any mutation of ant or food counts.
 */
public class ObstacleTest {

    @Test(expected = UnsupportedOperationException.class)
    public void setfourmis_throwsUnsupportedOperation() {
        new Obstacle().setfourmis(1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setnourriture_throwsUnsupportedOperation() {
        new Obstacle().setnourriture(1);
    }
}

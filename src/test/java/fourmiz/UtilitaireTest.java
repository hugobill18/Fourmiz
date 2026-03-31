package fourmiz;

import org.junit.Test;
import simengine.Utilitaire;

import static org.junit.Assert.*;

public class UtilitaireTest {

    @Test
    public void returnsValueInRange_uniformWeights() {
        int[] poids = {1, 1, 1, 1};
        for (int i = 0; i < 1000; i++) {
            int result = Utilitaire.randomPondere(poids);
            assertTrue("Expected result in [0,3] but got " + result,
                    result >= 0 && result < 4);
        }
    }

    @Test
    public void returnsValueInRange_unequalWeights() {
        int[] poids = {5, 1, 10, 2};
        for (int i = 0; i < 1000; i++) {
            int result = Utilitaire.randomPondere(poids);
            assertTrue("Expected result in [0,3] but got " + result,
                    result >= 0 && result < 4);
        }
    }

    @Test
    public void respectsWeightBias() {
        // Index 1 has weight 100 vs 1 each for the others → ~97% of draws should be index 1
        int[] poids = {1, 100, 1, 1};
        int countOne = 0;
        for (int i = 0; i < 1000; i++) {
            if (Utilitaire.randomPondere(poids) == 1) countOne++;
        }
        assertTrue("Expected index 1 to dominate, but only got " + countOne + "/1000",
                countOne > 900);
    }

    @Test
    public void singleElement_alwaysReturnsZero() {
        int[] poids = {42};
        for (int i = 0; i < 100; i++) {
            assertEquals(0, Utilitaire.randomPondere(poids));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void allZeroWeights_throwsIllegalArgumentException() {
        // Bug: summing all-zero weights produces 0, and Random.nextInt(0) throws
        Utilitaire.randomPondere(new int[]{0, 0, 0});
    }

    @Test
    public void standardAntWeights_returnsValidIndex() {
        // Verify the default 8-direction weight array used by Fourmi works correctly
        int[] poids = {12, 12, 12, 12, 12, 12, 12, 12};
        for (int i = 0; i < 1000; i++) {
            int result = Utilitaire.randomPondere(poids);
            assertTrue("Expected result in [0,7] but got " + result,
                    result >= 0 && result < 8);
        }
    }
}

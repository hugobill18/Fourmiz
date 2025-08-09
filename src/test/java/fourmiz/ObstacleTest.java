package fourmiz;

public class ObstacleTest {
    public static void main(String[] args) {
        Obstacle obstacle = new Obstacle();

        boolean threw = false;
        try {
            obstacle.setfourmis(1);
        } catch (UnsupportedOperationException e) {
            threw = true;
        }
        assert threw : "setfourmis should throw UnsupportedOperationException";

        threw = false;
        try {
            obstacle.setnourriture(1);
        } catch (UnsupportedOperationException e) {
            threw = true;
        }
        assert threw : "setnourriture should throw UnsupportedOperationException";

        System.out.println("Obstacle tests passed.");
    }
}

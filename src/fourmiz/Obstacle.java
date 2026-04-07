package fourmiz;

import java.awt.Color;


public class Obstacle extends Bloc{
	
public Obstacle(){
        possibility=false;
        couleur=Color.GRAY;
}


public void setfourmis(int fourm) {
        throw new UnsupportedOperationException("Cannot place ants on an obstacle");
}

public void setnourriture (int x)
{
        throw new UnsupportedOperationException("Cannot place food on an obstacle");
}
}

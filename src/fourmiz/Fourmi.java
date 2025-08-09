package fourmiz;
import simengine.Utilitaire;

public class Fourmi {
	int x,y; // Positionnement de la fourmi
	boolean nourriture; // Transporte ou non de la nourriture
	int[] poids= {12,12,12,12,12,12,12,12};
	
	
	public Fourmi(int x,int y)
	{
		this.x=x;
		this.y=y;
		nourriture=false;
	}

	/*public void deplacer()
	{
		while (nourriture=false){
		deplacerrand();
		}
		
	}*/

	public void deplacerrand()
	{
		int rand;
		rand=Utilitaire.randomPondere(poids);
		setxy(rand);
		setpoids(rand);
	}

	private void setxy(int rand)
	{
		if(rand==0){x=x-1; y=y-1;}
		if(rand==1){y=y-1;}
		if(rand==2){x=x+1; y=y-1;}
		if(rand==3){x=x+1;}
		if(rand==4){x=x+1; y=y+1;}
		if(rand==5){y=y+1;}
		if(rand==6){x=x-1; y=y+1;}
		if(rand==7){x=x-1;}
	}

	private void setpoids(int max)
	{
		int[] ponde={12,12,12,12,12,12,12,12}; // tableau de ponderation
		int i=max,j=0;
		while (j != 8)
		{
			poids [i]=ponde[j];
			if (i==7)i=0;
			i++;
			j++;
		}
	}
	public void setx(int x){
		this.x=x;
	}
	public void sety(int y){
		this.y=y;
	}
	public int getx(){
		return x;
	}
	public int gety(){
		return y;
	}
}

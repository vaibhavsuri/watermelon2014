package watermelon.group5;
import watermelon.sim.seed;

public class Seed {
  public double x;
  public double y;
  public boolean tetraploid;
  public double score;
  public Ploidy ploidy;
  public Seed() { x = 0.0; y = 0.0; ploidy = Ploidy.NONE; }

  public Seed(double xx, double yy, boolean tetra) {
    x = xx;
    y = yy;
    ploidy = Ploidy.TETRA;
    tetraploid = true;
  }

  
  public Seed(double xx, double yy) {
	    x = xx;
	    y = yy;
	    ploidy = Ploidy.NONE;

	  }
  public Seed (seed s){
	x = s.x;
	y = s.y;
	tetraploid = s.tetraploid;
	score = s.score;
	ploidy = Ploidy.NONE;
	
  }
  public seed getSeed(Seed s){
	seed rtnSeed = new seed();
	rtnSeed.x = s.x;
	rtnSeed.y = s.y;
	rtnSeed.tetraploid = s.tetraploid;
	rtnSeed.score = s.score;
	return rtnSeed;
  }
  
  public boolean equals(Seed s){
	  if(s.x == this.x && s.y == this.y){
		  return true;
	  }else{
		  return false;
	  }
  }
  
}
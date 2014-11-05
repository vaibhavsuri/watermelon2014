package watermelon.group5;

import java.util.ArrayList;
import java.util.List;

import watermelon.sim.*;

// Some util function for Player class
public class PlayerUtil {

	public PlayerUtil(){}
	
	// Taken from watermelon.sim.watermelon.java
	public static double calculatescore(
	    List<seed> seedList, 
	    double s) {
	  double total = 0;
		
	  for ( int i = 0; i < seedList.size(); i++) {
	    double score;
		double chance = 0.0;
		double totaldis = 0.0;
		double difdis = 0.0;
		for (int j = 0; j < seedList.size(); j++) {
		  if (j != i) {
		    totaldis = totaldis
							+ Math.pow(
									distanceSeed(seedList.get(i),
											seedList.get(j)), -2);
				}
			}
			for (int j = 0; j < seedList.size(); j++) {
			  if (j != i
						&& ((seedList.get(i).tetraploid && !seedList.get(j).tetraploid) || (!seedList
								.get(i).tetraploid && seedList.get(j).tetraploid))) {
					difdis = difdis
							+ Math.pow(
									distanceSeed(seedList.get(i),
											seedList.get(j)), -2);
			  }
			}
			chance = difdis / totaldis;
			score = chance + (1 - chance) * s;
			total = total + score;
		}
		return total;
	}
	
	public static double distanceSeed(
	  seed a, 
	  seed b) {
	  return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}
	
	public List<seed> getNeighborSeeds(
	    seed s, 
	    List<seed>seedList){
		List <seed> neighborSeeds = new ArrayList<>();
	  for(int i = 0 ; i < seedList.size() ; i++){
		if(!s.equals(seedList.get(i)) && distanceSeed(seedList.get(i),s) < 2.11){
		  neighborSeeds.add(seedList.get(i));		
		}
	  }
	  return neighborSeeds;
	}
	
}

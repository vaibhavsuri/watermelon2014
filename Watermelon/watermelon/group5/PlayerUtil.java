package watermelon.group5;

import java.util.ArrayList;
import java.util.List;

import watermelon.sim.*;

// Some util function for Player class
public class PlayerUtil {

	static double distowall = 1.00;
	static double distotree = 2.00;
	static double distoseed = 2.00;
	

	
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
	  Seed a, 
	  Seed b) {
	  return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}
	
	public static double distanceSeed(
			  seed a, 
			  seed b) {
			  return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}
	// Get adjacent seeds from given seed location
	public static List<Seed> getNeighborSeeds(
	    Seed s, 
	    List<Seed>seedList){
		List <Seed> neighborSeeds = new ArrayList<>();
	  for(int i = 0 ; i < seedList.size() ; i++){
		if(!s.equals(seedList.get(i)) && distanceSeed(seedList.get(i),s) < 2.11){
		  neighborSeeds.add(seedList.get(i));		
		}
	  }
	  return neighborSeeds;
	}
	public static List<seed> getNeighborSeeds(
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
	public static double distanceSquared(
		Seed s1,
		Seed s2) {
		return (s1.x - s2.x) * (s1.x - s2.x) + (s1.y - s2.y) * (s1.y - s2.y);
    }
	
	public static double getSeedEffect(
		Seed to,
		Seed from){
	  return 1 / (distanceSeed(to, from));
	}
	public static double getSeedEffect(
			seed to,
			seed from){
		  return 1 / (distanceSeed(to, from));
		}
	
	public static ArrayList<Seed> convertSeedList(ArrayList<seed> seedList){
	  ArrayList<Seed> sList = new ArrayList<>();
	  for(seed s : seedList){
	    sList.add(new Seed(s));
	  }
	  return sList;
	}
	


	public static boolean validateseed(
			List<seed> seedlistin, 
			double W, double L,
			List<Pair> treelist) {
		
		int nseeds = seedlistin.size();

		for (int i = 0; i < nseeds; i++) {
			for (int j = i + 1; j < nseeds; j++) {
				if (distanceSeed(seedlistin.get(i), seedlistin.get(j)) < distoseed) {

					return false;
				}
			}
		}
		for (int i = 0; i < nseeds; i++) {
			if (seedlistin.get(i).x < 0 || seedlistin.get(i).x > W
					|| seedlistin.get(i).y < 0 || seedlistin.get(i).y > L) {
				return false;
			}
			if (seedlistin.get(i).x < distowall
					|| W - seedlistin.get(i).x < distowall
					|| seedlistin.get(i).y < distowall
					|| L - seedlistin.get(i).y < distowall) {
				return false;
			}
		}
		for (int i = 0; i < treelist.size(); i++) {
			for (int j = 0; j < nseeds; j++) {
				if (distance(seedlistin.get(j), treelist.get(i)) < distotree) {
					return false;
				}
			}
		}
		return true;
	}
	// compute Euclidean distance between two points
	static double distance(seed a, Pair pair) {
		return Math.sqrt((a.x - pair.x) * (a.x - pair.x) + (a.y - pair.y) * (a.y - pair.y));
	}
}

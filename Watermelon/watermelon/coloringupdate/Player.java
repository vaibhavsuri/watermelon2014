package watermelon.coloringupdate;

import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Player extends watermelon.sim.Player {
	static double distowall = 2.1;
	static double distotree = 2.2;
	static double distoseed = 1.01;
	double m_length, m_width;
	
	public void init() {

	}

	static double distance(seed tmp, Pair pair) {
		return Math.sqrt((tmp.x - pair.x) * (tmp.x - pair.x) + (tmp.y - pair.y) * (tmp.y - pair.y));
	}

	// Return: the next position
	// my position: dogs[id-1]
	static double distance(seed a, Point b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

	@Override
	public ArrayList<seed> move(ArrayList<Pair> treelist, double width, double length, double s) {
		// TODO Auto-generated method stub

		m_length = length;
		m_width = width;
		
		ArrayList<seed> seedlist = new ArrayList<seed>();
		for (double i = distowall; i < width - distowall; i = i + distoseed) {
			for (double j = distowall; j < length - distowall; j = j + distoseed) {
				
				seed tmp;	
			    tmp = new seed(i, j, false);	
				boolean add = true;
				for (int f = 0; f < treelist.size(); f++) {
					if (distance(tmp, treelist.get(f)) < distotree) {
						add = false;
						break;
					}
				}
				if (add) {
					seedlist.add(tmp);
				}
			}
		}
		color(seedlist, s);
		System.out.printf("seedlist size is %d", seedlist.size());
		return seedlist;
	}
	
	//Calculating Euclidean distance
	double dist(double Ax, double Ay, double Bx, double By)
	{
		return (Math.sqrt(Math.pow(Ax-Bx,2)+Math.pow(Ay-By,2)));
	}
	
	//Colors the seeds in the seedlist inside out by evaluating the score for the board
	//for the two choices (tetraploid or diploid) for each seed
	public void color(ArrayList<seed> seedlist, double s)
	{
		boolean[] color_check = new boolean[seedlist.size()];
		Arrays.fill(color_check, false);
	    double middle_x = m_length *0.5, middle_y = m_width*0.5;
	    double least_distance = Double.POSITIVE_INFINITY;
	    seed middle_seed = new seed();
	    
	    //finding the seed in the "middle"
	    for(seed X: seedlist)
	    {
	    	double curr_dist = dist(middle_x, middle_y, X.x, X.y);
	    	if (curr_dist < least_distance)
	    	{
	    		least_distance = curr_dist;
	    		middle_seed = X;
	    	}
	    }
	    color_check[seedlist.indexOf(middle_seed)]=true;
	    //System.out.println("Seed List size: "+seedlist.size());
	    
	    double offset = 1;//offset determines the radius for coloring
	    int seeds_changes=0;
	    while(!checkIfFilled(color_check))
	    {
	    	int count = 0;
	    	for (int i = 0; i < seedlist.size(); i++)
	    	{
	    		if ((distanceseed(seedlist.get(i), middle_seed) < offset * distoseed) && color_check[i]==false)
	    		{
	    			count++;
	    			double false_score = calculate_score(seedlist, s);
	    			seedlist.get(i).tetraploid = true;
	    			double true_score = calculate_score(seedlist, s);
	    			//System.out.println("True Score: "+true_score+ " False score: "+false_score);
	    			if(false_score > true_score)
	    			{
	    				seedlist.get(i).tetraploid = false;
	    				//System.out.println("Chose False");
	    			}
	    			//else
	    				//System.out.println("Chose True");
	    			color_check[i]=true;
	    			seeds_changes++;
	    			System.out.println(seeds_changes);
	    		}
	    	}
	    	if (count == 0) //all seeds within the current radius are colored
	    	{
	    		offset++; //increase the radius
	    	}
	    }
	    
	}
	
	//checks if all the seeds in the seedlist have been colored
	private static boolean checkIfFilled(boolean[] colored){
		boolean all=true;
		for (boolean color: colored){
			if(!color)
				all=false;
		}
		return all;
	}
	
	//SIMULATOR CODE
	private double calculate_score(ArrayList<seed> seedlist, double s){
		double total = 0;
		for (int i = 0; i < seedlist.size(); i++) {
			double score;
			double chance = 0.0;
			double totaldis = 0.0;
			double difdis = 0.0;
			for (int j = 0; j < seedlist.size(); j++) {
				if (j != i) {
					totaldis = totaldis
							+ Math.pow(
									distanceseed(seedlist.get(i),
											seedlist.get(j)), -2);
				}
			}
			for (int j = 0; j < seedlist.size(); j++) {
				if (j != i
						&& ((seedlist.get(i).tetraploid && !seedlist.get(j).tetraploid) || (!seedlist
								.get(i).tetraploid && seedlist.get(j).tetraploid))) {
					difdis = difdis
							+ Math.pow(
									distanceseed(seedlist.get(i),
											seedlist.get(j)), -2);
				}
			}
			//System.out.println(totaldis);
			//System.out.println(difdis);
			chance = difdis / totaldis;
			score = chance + (1 - chance) * s;
			total = total + score;
		}
		return total;
	}

	static double distanceseed(seed a, seed b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

}
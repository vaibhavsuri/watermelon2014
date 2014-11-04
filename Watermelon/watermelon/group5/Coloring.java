package watermelon.group5;

import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Coloring{
	static double distowall = 1.00;
	static double distotree = 2.00;
	static double distoseed = 2.00;

	//double m_length, m_width;
	
	public void init() {

	}
	
	//brute force coloring
	public  static ArrayList<seed> bruteforce(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean[] partition_check = new boolean[seedlist.size()];
		Arrays.fill(partition_check, false);
		int num_partitions = (int)Math.ceil(seedlist.size()/23);
		int curr_partition = -1;
		ArrayList<ArrayList<seed>> partitions = new ArrayList<ArrayList<seed>>();
		while (curr_partition < num_partitions)
		{
			for (int i = 0; i < seedlist.size(); i++)
	    	{
				if(!partition_check[i])
				{
					ArrayList<seed> onepart = new ArrayList<seed>();
					onepart.add(seedlist.get(i));
					partition_check[i] = true;
					curr_partition++;
					break;
				}
	    	}
			double offset=0;
			while(partitions.get(curr_partition).size() < 24)
			{
				for (int i = 0; i < seedlist.size(); i++)
		    	{
					if ((distanceseed(seedlist.get(i), partitions.get(curr_partition).get(0)) < offset * distoseed) && !partition_check[i])
		    		{
						partitions.get(curr_partition).add(seedlist.get(i));
						partition_check[i]=true;
		    		}
		    	}
				offset++;
			}
		}
		return seedlist;
	}
	
	
	

	//colors the seeds in concentric circles starting in the topleft corner
	public  static ArrayList<seed> concentric_top_left(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean[] color_check = new boolean[seedlist.size()];
		Arrays.fill(color_check, false);
	    double middle_x = m_width *0.5, middle_y = m_length*0.5;
	    double least_distance = Double.POSITIVE_INFINITY;
	    
	    ArrayList<seed> templist = new ArrayList<seed>();
	    seed middle_seed = seedlist.get(0);
	    templist.add(middle_seed);
	    color_check[seedlist.indexOf(middle_seed)]=true;
	    
	    double offset = 2;//offset determines the radius for coloring
	    boolean color = true;
	    while(!checkIfFilled(color_check))
	    {
	    	int count = 0;
	    	for (int i = 0; i < seedlist.size(); i++)
	    	{
	    		if ((distanceseed(seedlist.get(i), middle_seed) < offset * distoseed) && color_check[i]==false)
	    		{
		    		count++;
	    			seedlist.get(i).tetraploid = color;
	    			templist.add(seedlist.get(i));
	    			color_check[i]=true;
	    		}
	    	}
	    	if (count == 0) //all seeds within the current radius are colored
	    	{
	    		offset++; //increase the radius
	    		color = !color;
	    	}
	    }
	    return templist;
	    
	}

	
	
	//colors the seeds in concentric circles moving inside out
	public  static ArrayList<seed> concentric(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean[] color_check = new boolean[seedlist.size()];
		Arrays.fill(color_check, false);
	    double middle_x = m_width *0.5, middle_y = m_length*0.5;
	    double least_distance = Double.POSITIVE_INFINITY;
	    seed middle_seed=new seed();
	    ArrayList<seed> templist = new ArrayList<seed>();
	    
	    //finding the seed in the "middle"
	    for(seed X: seedlist)
	    {
	    	double curr_dist = Math.abs(dist(middle_x, middle_y, X.x, X.y));
	    	if (curr_dist < least_distance)
	    	{
	    		least_distance = curr_dist;
	    		middle_seed = X;
	    	}
	    }
	    templist.add(middle_seed);
	    color_check[seedlist.indexOf(middle_seed)]=true;
	    //System.out.println("Seed List size: "+seedlist.size());
	    
	    double offset = 2;//offset determines the radius for coloring
	    boolean color = true;
	    while(!checkIfFilled(color_check))
	    {
	    	int count = 0;
	    	for (int i = 0; i < seedlist.size(); i++)
	    	{
	    		if ((distanceseed(seedlist.get(i), middle_seed) < offset * distoseed) && color_check[i]==false)
	    		{
		    		count++;
	    			seedlist.get(i).tetraploid = color;
	    			templist.add(seedlist.get(i));
	    			color_check[i]=true;
	    		}
	    	}
	    	if (count == 0) //all seeds within the current radius are colored
	    	{
	    		offset++; //increase the radius
	    		color = !color;
	    	}
	    }
	    return templist;
	    
	}

	//starts from top left and colors all the "uncolored" neighbors of a seed with its opposite ploidy
	public  static  ArrayList<seed> neighbor_invert_topleft(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean[] color_check = new boolean[seedlist.size()];
		Arrays.fill(color_check, false);
	    ArrayList<seed> finallist = new ArrayList<seed>();
	    
	    
	    seed middle_seed = seedlist.get(0);
	    finallist.add(middle_seed);
	    color_check[seedlist.indexOf(middle_seed)]=true;
	    
	    while(!checkIfFilled(color_check))
	    {
	    	for(int i = 0; i < finallist.size(); i++)
	    	{
	    		seed center = finallist.get(i);
		    	for (int j = 0; j < seedlist.size(); j++)
		    	{
		    		if ((distanceseed(seedlist.get(j), center) < 1.5 * distoseed) && color_check[j]==false)
		    		{
		    			seedlist.get(j).tetraploid = !center.tetraploid;
		    			finallist.add(seedlist.get(j));
		    			color_check[j]=true;
		    		}
		    	}
	    	}
	    }	    
	    return finallist;
	}

	//moves inside out and colors all the "uncolored" neighbors of a seed with its opposite ploidy
	public  static  ArrayList<seed> neighbor_invert(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean[] color_check = new boolean[seedlist.size()];
		Arrays.fill(color_check, false);
	    double middle_x = m_width *0.5, middle_y = m_length*0.5;
	    double least_distance = Double.POSITIVE_INFINITY;
	    seed middle_seed=new seed();
	    ArrayList<seed> finallist = new ArrayList<seed>();
	    
	    //finding the seed in the "middle"
	    for(seed X: seedlist)
	    {
	    	double curr_dist = Math.abs(dist(middle_x, middle_y, X.x, X.y));
	    	if (curr_dist < least_distance)
	    	{
	    		least_distance = curr_dist;
	    		middle_seed = X;
	    	}
	    }
	    finallist.add(middle_seed);
	    color_check[seedlist.indexOf(middle_seed)]=true;
	    
	    while(!checkIfFilled(color_check))
	    {
	    	for(int i = 0; i < finallist.size(); i++)
	    	{
	    		seed center = finallist.get(i);
		    	for (int j = 0; j < seedlist.size(); j++)
		    	{
		    		if ((distanceseed(seedlist.get(j), center) < 1.5 * distoseed) && color_check[j]==false)
		    		{
		    			seedlist.get(j).tetraploid = !center.tetraploid;
		    			finallist.add(seedlist.get(j));
		    			color_check[j]=true;
		    		}
		    	}
	    	}
	    }	    
	    return finallist;
	}
	
	//alternating the color for each seed
	public  static  ArrayList<seed>  alternate(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean color = true;
		for (seed current: seedlist)
		{
			current.tetraploid = color;
			color = !color;
		}
		return seedlist;
	}
	
	//alternating the color for each seed, but this coloring is independent of the previous row of seeds
	//the first seed in every row is a tetraploid and then the alternating begins
	public  static  ArrayList<seed>  alternate_edge_start(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean color = true;
		for (seed current: seedlist)
		{
			if (dist(current.x, current.y, 0, current.y) == distowall)
				color = true;
			else 
				color = !color;
			current.tetraploid = color;
		}
		return seedlist;
	}
	
	
	//Colors the seeds in the seedlist inside out by evaluating the score for the board
	//for the two choices (tetraploid or diploid) for each seed
	public  static ArrayList<seed> insideout(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean[] color_check = new boolean[seedlist.size()];
		Arrays.fill(color_check, false);
	    double middle_x = m_width *0.5, middle_y = m_length*0.5;
	    double least_distance = Double.POSITIVE_INFINITY;
	    seed middle_seed=new seed();
	    ArrayList<seed> templist = new ArrayList<seed>();
	    
	    //finding the seed in the "middle"
	    for(seed X: seedlist)
	    {
	    	double curr_dist = Math.abs(dist(middle_x, middle_y, X.x, X.y));
	    	if (curr_dist < least_distance)
	    	{
	    		least_distance = curr_dist;
	    		middle_seed = X;
	    	}
	    }
	    templist.add(middle_seed);
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
	    			templist.add(seedlist.get(i));
	    			double false_score = calculate_score(templist, s);
	    			templist.get(templist.size()-1).tetraploid = true;
	    			double true_score = calculate_score(templist, s);
	    			System.out.println("True Score: "+true_score+ " False score: "+false_score);
	    			if(false_score > true_score)
	    			{
	    				templist.get(templist.size()-1).tetraploid = false;
	    				System.out.println("Chose False");
	    			}
	    			else
	    				System.out.println("Chose True");
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
	    return templist;
	    
	}
	
	
	//same as inside out but the the coloring starts from the top left corner
	public  static ArrayList<seed> topleft(ArrayList<seed> seedlist, double s, double m_length, double m_width)
	{
		boolean[] color_check = new boolean[seedlist.size()];
		Arrays.fill(color_check, false);
	    seed first_seed=new seed();
	    ArrayList<seed> templist = new ArrayList<seed>();
	    
	    first_seed = seedlist.get(0);
	    templist.add(first_seed);
	    color_check[seedlist.indexOf(first_seed)]=true;
	    //System.out.println("Seed List size: "+seedlist.size());
	    
	    double offset = 1;//offset determines the radius for coloring
	    int seeds_changes=0;
	    while(!checkIfFilled(color_check))
	    {
	    	int count = 0;
	    	for (int i = 0; i < seedlist.size(); i++)
	    	{
	    		if ((distanceseed(seedlist.get(i), first_seed) < offset * distoseed) && color_check[i]==false)
	    		{
	    			count++;
	    			templist.add(seedlist.get(i));
	    			double false_score = calculate_score(templist, s);
	    			templist.get(templist.size()-1).tetraploid = true;
	    			double true_score = calculate_score(templist, s);
	    			System.out.println("True Score: "+true_score+ " False score: "+false_score);
	    			if(false_score > true_score)
	    			{
	    				templist.get(templist.size()-1).tetraploid = false;
	    				System.out.println("Chose False");
	    			}
	    			else
	    				System.out.println("Chose True");
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
	    return templist;
	    
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
	
	//Calculating Euclidean distance
		public static double dist(double Ax, double Ay, double Bx, double By)
		{
			return (Math.sqrt(Math.pow(Ax-Bx,2)+Math.pow(Ay-By,2)));
		}
	
	//SIMULATOR CODE
	public static double calculate_score(ArrayList<seed> seedlist, double s){
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

	//Calculating distance between two seeds
	public static double distanceseed(seed a, seed b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
	}

}
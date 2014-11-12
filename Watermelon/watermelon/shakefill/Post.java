package watermelon.shakefill;

import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Post{
	static double distowall = 1.00;
	static double distotree = 2.00;
	static double distoseed = 2.00;

	double m_length, m_width;
	
	public void init() {

	}
	
	//Calls the shake method for different direction shakes and mutliple shakes
	public static void shake_things_up(ArrayList<seed> seedlist, ArrayList<Pair> treelist, double length, double width)
	{
		int curr_iter = 0; 
		int limit;
		if (length<=30 && width<=30)
			limit=8;
		else
			limit=6;
		while(curr_iter < limit)
		{
			System.out.println("Iteration = "+curr_iter);
			shake(seedlist,treelist,length, width, -1); //-1 is one direction
			shake(seedlist,treelist,length, width, +1); //+1 is the opposite direction
			curr_iter++;
		}
	}
	
	//shakes the map left and right in order to settle the current seeds
	// in the bottom area and make space for adding new seeds
	public static void shake(ArrayList<seed> seedlist, ArrayList<Pair> treelist, double length, double width, int direction)
	{
		double granularity = 0.005; //sets movement granularity
		boolean[] shake_check = new boolean[seedlist.size()];
		Arrays.fill(shake_check, false);
		int i = 0;
		int next_iter=15; //next iteration value to increase multiplier value
		double multiplier = 1; //determines distance to search for seed from the bottom wall
		seed temp = new seed();
		while(i<500)
		{
			for (int s = 0; s<seedlist.size(); s++)
			{
				if (PlayerUtil.distance(seedlist.get(s), new Pair(seedlist.get(s).x, length)) <= multiplier*distowall)
				{
					shake_check[s] = true;
					temp.x = seedlist.get(s).x;
					temp.y = seedlist.get(s).y;
					seedlist.remove(s);
					seedlist.add(temp);
					seed best = temp;
					while(PlayerUtil.validateseed(seedlist, width, length, treelist))
					{		
						best = new seed(temp.x, temp.y, true);
						seedlist.remove(seedlist.size()-1);
						temp.y += granularity;
						seedlist.add(temp);
					}

					seedlist.remove(seedlist.size()-1);
					temp.x = best.x;
					temp.y = best.y;
					seedlist.add(temp);
					while(PlayerUtil.validateseed(seedlist, width, length, treelist))
					{

						best = new seed(temp.x, temp.y, true);
						seedlist.remove(seedlist.size()-1);
						if (direction == -1) //when the direction input is -1, the shaking is done along one direction
							temp.x -= granularity;//and along the opposite direction when the input is not -1
						else
							temp.x += granularity;
						
						seedlist.add(temp);
					}

					seedlist.remove(seedlist.size()-1);
					seedlist.add(best);
				}
			}
			if (next_iter == i)//increasing the multiplier value after every 15th iteration
			{
				next_iter+=15;
				multiplier++;
			}
			i++;
		}
	}
	
	//filling seeds with higher granularity search and WITHOUT pushing seeds
	public static void fill_seeds(ArrayList<seed> seedlist, ArrayList<Pair> treelist, double length, double width)
	{
		double offset = 0.005;
		for (double i = distowall; i < length - distowall; i = i + offset) {
				for (double j = distowall; j < width - distowall; j = j + offset) {
					seed tmp;
					tmp = new seed(j, i, false);
					boolean add = true;
					for (int f = 0; f < treelist.size(); f++) {
						if (PlayerUtil.distance(tmp, treelist.get(f)) < distotree) {
							add = false;
							break;
						}
					}
					for (int f = 0; f < seedlist.size(); f++) {
						if (PlayerUtil.distanceSeed(tmp, seedlist.get(f)) < distoseed) {
							add = false;
							break;
						}
					}
					if (add) {
						seedlist.add(tmp);
					}
				}
		}
	}
	
	//filling seeds with medium granularity search while trying to pushing seeds
	public static void fill_seeds_with_push(ArrayList<seed> seedlist, ArrayList<Pair> treelist, double length, double width)
	{
		double offset = 0.05;
		for (double i = distowall; i < length - distowall; i = i + offset) {
				for (double j = distowall; j < width - distowall; j = j + offset) {
					//System.out.println("i "+i+" j "+j);
					seed tmp;
					tmp = new seed(j, i, false);
					boolean add = true;
					for (int f = 0; f < treelist.size(); f++) {
						if (PlayerUtil.distance(tmp, treelist.get(f)) < distotree) {
							add = false;
							break;
						}
					}
					for (int f = 0; f < seedlist.size(); f++) 
					{
						if (PlayerUtil.distanceSeed(tmp, seedlist.get(f)) < distoseed)
						{
							if (tmp.x!=seedlist.get(f).x && tmp.y!=seedlist.get(f).y)
							{
						        ArrayList<Integer> already=new ArrayList<Integer>();
								ArrayList<Integer> closeSeeds=Post.get_close_seeds(new Pair(tmp.x,tmp.y),seedlist);
						        for(Integer seed_index:closeSeeds){
						            boolean already_moved=false;
						            for(Integer seed: already){
						                if(seed.intValue()==seed_index.intValue()){
						                    already_moved=true;
						                }
						            }
						            if(true){
						                //select seed
						                seed closeSeed=seedlist.get(seed_index.intValue());
						                seedlist.remove(seed_index.intValue());

						                //push seed away from the potential new seed location
						                seedlist=Post.push_seeds_from(new Pair(tmp.x,tmp.y),seedlist,closeSeed,treelist,width,length,seed_index.intValue());
						                already.add(seed_index);
						            }
						            
						        }
							}
						        if (PlayerUtil.distanceSeed(tmp, seedlist.get(f)) < distoseed)
						        {
						        	add = false;
									break;
						        }		
								
							}
					}
					if (add) {
						seedlist.add(tmp);
					}
				}
		}
	}
	
	//pushing seeds away from potential new seed location
	  public static ArrayList<seed> push_seeds_from(Pair new_seed, ArrayList<seed> seedList, seed start, ArrayList<Pair> treelist, double width, double length, int seed_index){

	        double deltaPosition = 0.005;
	        
	        double angle=anglebw(start,new_seed); //calculating angle b/w current seed and potential new seed
	        seed temp=new seed(start.x,start.y, start.tetraploid);
	        seedList.add(temp);
	        seed best=start;
	        
	        
	        while(PlayerUtil.validateseed(seedList,width,length,treelist)){
	            best=new seed(temp.x,temp.y,temp.tetraploid);
	            seedList.remove(seedList.size()-1);
	            double deltaX = Math.cos(angle) * deltaPosition;
	            double deltaY = Math.sin(angle) * deltaPosition;
	            temp.x-=deltaX;//pushing seed away
	            temp.y-=deltaY;//pushing seed away
	            seedList.add(temp);
	        }
	     
	        seedList.remove(seedList.size()-1);
	        start=best;
	        
	        seedList.add(seed_index,start);
	        return seedList;
	    }
		    
	  //getting the seeds close to the potential new seed location
	    public static ArrayList<Integer> get_close_seeds(Pair new_seed, ArrayList<seed> seedlist){
	        ArrayList<Integer> seed_indices=new ArrayList<Integer>();
	        
	        for(seed a_seed: seedlist){
	            double distToTreeForSeed=PlayerUtil.distance(a_seed,new_seed);
	            
	            if(distToTreeForSeed<=3.5){
	                seed_indices.add(seedlist.indexOf(a_seed));
	            }
	        }
	        
	        return seed_indices;
	    }
	    
	    //calculating angle between seed and pair (new seed potential location)
	    public static double anglebw(seed the_seed,Pair new_seed)
	    {
	        return Math.atan((new_seed.y-the_seed.y)/(new_seed.x-the_seed.x));
	    }
}
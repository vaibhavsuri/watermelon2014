package watermelon.group5;

import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Packing{
	static double distowall = 1.00;
	static double distotree = 2.00;
	static double distoseed = 2.00;

	double m_length, m_width;
	
	public void init() {

	}
	


	
	static double distance(seed tmp, Pair pair) {
		return Math.sqrt((tmp.x - pair.x) * (tmp.x - pair.x) + (tmp.y - pair.y) * (tmp.y - pair.y));
	}
	
	//simple row-wise horizontal packing of the seeds
	public static ArrayList<seed> horizontal(ArrayList<Pair> treelist, double length, double width)
	{
		ArrayList<seed> seedlist = new ArrayList<seed>();
		for (double i = distowall; i < length - distowall; i = i + distoseed) {
			for (double j = distowall; j < width - distowall; j = j + distoseed) {
				
				seed tmp;	
			    tmp = new seed(j, i, false);	
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
		return seedlist;
	}
	
	//row wise horizontal packing while accounting for the trees
	//the offset for the next seed to be planted is changed when a tree is encountered
	public static ArrayList<seed> horizontal_mind_trees(ArrayList<Pair> treelist, double length, double width)
	{
		ArrayList<seed> seedlist = new ArrayList<seed>();
		for (double i = distowall; i < length - distowall; i = i + distoseed) {
			for (double j = distowall; j < width - distowall; j = j + distoseed) {
				
				seed tmp;	
			    tmp = new seed(j, i, false);	
				boolean add = true;
				int tree_index = -1;
				for (int f = 0; f < treelist.size(); f++) {
					if (distance(tmp, treelist.get(f)) < distotree) {
						add = false;
						tree_index = f;
						break;
					}
				}
				if (add) {
					seedlist.add(tmp);
				}
				else
					j = j - (distoseed - distotree);
			}
		}
		return seedlist;
	}
	
	
	//hexagonal packing
	public static ArrayList<seed> hexagonal(ArrayList<Pair> treelist, double length, double width)
	{
		ArrayList<seed> seedlist = new ArrayList<seed>();
		double i = distowall;
		double j;
		double offset = 0;
		while(i <= length - distowall)
		{
			j = distowall + offset;
			while (j <= width - distowall)
			{
				boolean add = true;
				seed tmp = new seed(j, i, false);
				for (int f = 0; f < treelist.size(); f++) {
					if (distance(tmp, treelist.get(f)) < distotree) {
						add = false;
						break;
					}
				}
				if (add) {
					seedlist.add(tmp);
				}
				
				j = j + distoseed;
			}
			
			if (offset==0)
				offset = distowall;
			else 
				offset=0;
			
			i = i + 1.7321;
		}
		return seedlist;
	}
	
	//hexagonal packing inverted
		public static ArrayList<seed> hexagonal_invert(ArrayList<Pair> treelist, double length, double width)
		{
			ArrayList<seed> seedlist = new ArrayList<seed>();
			double i = distowall;
			double j;
			double offset = 0;
			while(i <= width - distowall)
			{
				j = distowall + offset;
				while (j <= length - distowall)
				{
					boolean add = true;
					seed tmp = new seed(i,j, false);
					for (int f = 0; f < treelist.size(); f++) {
						if (distance(tmp, treelist.get(f)) < distotree) {
							add = false;
							break;
						}
					}
					if (add) {
						seedlist.add(tmp);
					}
					
					j = j + distoseed;
				}
				
				if (offset==0)
					offset = distowall;
				else 
					offset=0;
				
				i = i + Math.sqrt(3) + 0.000001;
			}
			return seedlist;
		}

}
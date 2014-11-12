package watermelon.shakefill;


import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;



public class Player extends watermelon.sim.Player{
	static double distowall = 1.00;
	static double distotree = 2.00;
	static double distoseed = 2.00;

	double m_length, m_width;
	
	public void init() {

	}

	@Override
	public ArrayList<seed> move(
			ArrayList<Pair> treelist, 
			double width, 
			double length, 
			double s) {
		// TODO Auto-generated method stub

		ArrayList<seed> bestList = null;
	    
		Date date1 = new Date();
	    System.out.println(date1.toString());

		bestList = Packing.hexagonal(treelist, length, width);
		Post.shake_things_up(bestList, treelist, length, width);

		Post.fill_seeds_with_push(bestList, treelist, length, width);
		Post.fill_seeds(bestList, treelist, length, width);

		bestList = Coloring.neighbor_invert_topleft(bestList,s, length, width);
		System.out.println("Seeds "+bestList.size());
		Date date2 = new Date();
	    System.out.println(date2.toString());
		return bestList;
	}
	
}
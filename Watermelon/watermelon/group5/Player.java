package watermelon.group5;

import java.util.*;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;

public class Player extends watermelon.sim.Player {
	static double distowall = 1.00;
	static double distotree = 2.00;
	static double distoseed = 2.00;

	double m_length, m_width;
	
	public void init() {

	}

	@Override
	public ArrayList<seed> move(ArrayList<Pair> treelist, double width, double length, double s) {
		// TODO Auto-generated method stub
		ArrayList<seed> seedlist;
		if (length > width)
			seedlist = Packing.hexagonal(treelist, length, width);
		else
			seedlist = Packing.hexagonal_invert(treelist, length, width);
		
		ArrayList<seed> finallist = Coloring.concentric(seedlist, s, length, width);

		return finallist;
	}
	

}
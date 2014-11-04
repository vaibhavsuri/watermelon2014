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
		ArrayList<seed> seedlistA, seedlistB;
		
		seedlistA = Packing.hexagonal(treelist, length, width);
		seedlistB = Packing.hexagonal_invert(treelist, length, width);
		ArrayList<seed> choicelistA = Coloring.neighbor_invert_topleft(seedlistA, s, length, width);
		ArrayList<seed> choicelistB = Coloring.neighbor_invert_topleft(seedlistB, s, length, width);
		double scoreA = Coloring.calculate_score(choicelistA, s);
		double scoreB = Coloring.calculate_score(choicelistB, s);
		if (scoreA>scoreB)
			return choicelistA;
		else
			return choicelistB;
	}
	

}
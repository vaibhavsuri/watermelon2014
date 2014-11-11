package watermelon.group5;


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
		ArrayList<seed>treeSeed = new ArrayList<>();
		for (Pair p : treelist){
			treeSeed.add(new seed(p.x,p.y,false));
		}
		Map<ArrayList<seed>,String>packingList = new HashMap<>();
		Map<ArrayList<seed>,String>finalSolutionList = new HashMap<>(); 
		String bestStrategy = "";
		double bestScore = 0;
		ArrayList<seed> bestList = null;
		packingList.put(Packing.hexagonal(treelist, length, width),"hexagonal");
		packingList.put(Packing.hexagonal_invert(treelist, length, width),"hexagonal_invert");
		packingList.put(Packing.horizontal(treelist, length, width),"horizontal");
		packingList.put(Packing.horizontal_mind_trees(treelist, length, width),"horizontal_mind_trees");
		packingList.put(Packing.physicalWithExisting(treeSeed, null, width, length),"Physcal Packing");
		for (java.util.Map.Entry<ArrayList<seed>, String> packList : packingList.entrySet()){
			finalSolutionList.put(Coloring.neighbor_invert_topleft(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: neighbor_invert_topleft");
			finalSolutionList.put(Coloring.incrementalColoring(packList.getKey(), length, width, s),"Packing: "+packList.getValue()+"\tColoring: incrementalColoring");
			finalSolutionList.put(Coloring.alternate(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: alternate");
			finalSolutionList.put(Coloring.alternate_edge_start(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: alternate_edge_start");
			finalSolutionList.put(Coloring.concentric(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: concentric");
			finalSolutionList.put(Coloring.randomColoring(packList.getKey()),"random");
			finalSolutionList.put(Coloring.neighbor_invert(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: neighbor_invert");
			finalSolutionList.put(Coloring.insideout(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: insideout");
			finalSolutionList.put(Coloring.topleft(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: topleft");
			
		}
		
		
		
		for ( java.util.Map.Entry<ArrayList<seed>,String> solution : finalSolutionList.entrySet()){
				ArrayList<seed>tempList = Moving.moveAround(solution.getKey(), treelist, s, width, length);
				double score = PlayerUtil.calculatescore(tempList, s);
				if (score > bestScore){
					bestScore = score;
					bestStrategy = solution.getValue();
					bestList = tempList;
				}
		}	
		//bestList = Coloring.incrementalColoring(bestList, length, width, s);
		System.out.println("\nBest Strategies " + bestStrategy);
		//return choicelistB;
		return (ArrayList<seed>) bestList;
		//return scoreA > scoreB ? choicelistA : choicelistB;
	}
	

}
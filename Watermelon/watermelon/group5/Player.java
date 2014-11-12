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
        
        //Starting
        Date start=new Date();
        System.out.println("Starting..."+start.toString());
        
		ArrayList<seed>treeSeed = new ArrayList<>();
		for (Pair p : treelist){
			treeSeed.add(new seed(p.x,p.y,false));
		}
		Map<ArrayList<seed>,String>packingList = new HashMap<>();
		Map<ArrayList<seed>,String>finalSolutionList = new HashMap<>(); 
		String bestStrategy = "";
		double bestScore = 0;
		ArrayList<seed> bestList = null;
        
        //Generating Packing
        System.out.println("Trying Different Types of Packing...");
        System.out.println("Hexagonal...");
        packingList.put(Packing.hexagonal(treelist, length, width),"hexagonal");
        System.out.println("Hexagonal with Spacing...");
        packingList.put(Packing.hexagonal_with_spacing(treelist,length,width),"Hexagonal With Spacing");
        System.out.println("Hexagonal Inverted...");
        packingList.put(Packing.hexagonal_invert(treelist, length, width),"hexagonal_invert");
        System.out.println("Horizontal...");
        packingList.put(Packing.horizontal(treelist, length, width),"horizontal");
		packingList.put(Packing.horizontal_mind_trees(treelist, length, width),"horizontal_mind_trees");
        System.out.println("Physical...");
        packingList.put(Packing.physicalWithExisting(treeSeed, null, width, length),"Physcal Packing");
        System.out.println("Shaking and Fll...");
        packingList.put(getShakePacking(treelist,width,length,s),"Shaking and Fill Packing");
        
        //Trying different colorings
        System.out.println("Trying different Colorings...");
		for (java.util.Map.Entry<ArrayList<seed>, String> packList : packingList.entrySet()){
            System.out.println("Neighbor Inverted Top Left...");
            finalSolutionList.put(Coloring.neighbor_invert_topleft(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: neighbor_invert_topleft");
			//finalSolutionList.put(Coloring.incrementalColoring(packList.getKey(), length, width, s),"Packing: "+packList.getValue()+"\tColoring: incrementalColoring");
            System.out.println("Alternate...");
            finalSolutionList.put(Coloring.alternate(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: alternate");
            System.out.println("Alternate Edge Start..");
            finalSolutionList.put(Coloring.alternate_edge_start(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: alternate_edge_start");
            System.out.println("Concentric Coloring..");
            finalSolutionList.put(Coloring.concentric(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: concentric");
            System.out.println("Random Coloring...");
            finalSolutionList.put(Coloring.randomColoring(packList.getKey()),"random");
            System.out.println("Neighbor Inverted Coloring...");
            finalSolutionList.put(Coloring.neighbor_invert(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: neighbor_invert");
            System.out.println("Inside Out Coloring...");
			finalSolutionList.put(Coloring.insideout(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: insideout");
            System.out.println("Top Left Coloring...");
            finalSolutionList.put(Coloring.topleft(packList.getKey(), s, length, width),"Packing: "+packList.getValue()+"\tColoring: topleft");
			
		}
		
		
		//Evaluating Best Score
        System.out.println("Evaluating Best Score...");
		for ( java.util.Map.Entry<ArrayList<seed>,String> solution : finalSolutionList.entrySet()){
				ArrayList<seed>tempList = Moving.moveAround(solution.getKey(), treelist, s, width, length);
				double score = PlayerUtil.calculatescore(tempList, s);
				if (score > bestScore){
					bestScore = score;
					bestStrategy = solution.getValue();
					bestList = tempList;
				}
		}
        
        //Do last coloring check
        System.out.println("Optimizing the best list...");
        checkColoring(bestList,s);
        
		//bestList = Coloring.incrementalColoring(bestList, length, width, s);
		System.out.println("\nBest Strategy " + bestStrategy);
        
        //Ending Time
        Date end=new Date();
        System.out.println("Ending..."+end.toString());
        
		//return choicelistB;
		return (ArrayList<seed>) bestList;
		//return scoreA > scoreB ? choicelistA : choicelistB;
	}
    
    //method for generating shaking packing
    public static ArrayList<seed> getShakePacking(ArrayList<Pair> treelist, double width, double length, double s){
        ArrayList<seed> bestList = null;
        bestList = Packing.hexagonal(treelist, length, width);
        Post.shake_things_up(bestList, treelist, length, width);
        
        Post.fill_seeds_with_push(bestList, treelist, length, width);
        Post.fill_seeds(bestList, treelist, length, width);
        
        return bestList;
    }
	
    //scans best list and checks if any minor improvements in coloring can be made
    public static void checkColoring(ArrayList<seed> seedlist, double s){
        for(seed a_seed: seedlist){
            double current_score=PlayerUtil.calculatescore(seedlist, s);
            reverse_seed_color(a_seed);
            double reverse_score=PlayerUtil.calculatescore(seedlist,s);
            
            //checking if the seed reversal improves the score
            if(reverse_score<=current_score){
                reverse_seed_color(a_seed);
            } else {
                System.out.println("Improved Score by: "+(reverse_score-current_score));
            }
        }
    }
    
    //reverses the color of the seed
    public static void reverse_seed_color(seed the_seed){
        if(the_seed.tetraploid){
            the_seed.tetraploid=false;
        } else {
            the_seed.tetraploid=true;
        }
    }
    
}
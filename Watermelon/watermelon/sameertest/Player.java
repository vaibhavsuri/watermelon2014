package watermelon.sameertest;


import java.util.*;
import java.util.Map;
import java.util.HashMap;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;
import watermelon.group5.PlayerUtil;


public class Player extends watermelon.sim.Player{
	static double distowall = 1.00;
	static double distotree = 2.00;
	static double distoseed = 2.00;

	double m_length, m_width;
	
	public void init() {

	}

	@Override
	public ArrayList<seed> move(ArrayList<Pair> treelist, double width,	double length, double s) {
        ArrayList<seed> seedlist=hexagonal(treelist,length,width);
//        seedlist=moveSeedsTowardsTrees(treelist,seedlist,length,width);
        System.out.println("Number of Seeds: "+seedlist.size());
        return alternate(seedlist,s,length,width);
    }
    
    public static ArrayList<seed> moveSeedsTowardsTrees(ArrayList<Pair> treelist, ArrayList<seed> seedlist, double length, double width){
        ArrayList<Integer> already=new ArrayList<Integer>();
        
        //iterate over trees
        for(Pair tree: treelist){
            //find closest seeds
            ArrayList<Integer> closeSeeds=getSeedsNearTree(tree,seedlist);
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

                    //move seed towards tree
                    seedlist=moveSeedTowardTree(tree,seedlist,closeSeed,treelist,width,length,seed_index.intValue());
                    already.add(seed_index);
                }
                
            }
            
        }
        
        return seedlist;
    }
    
    public static ArrayList<seed> moveSeedTowardTree(Pair tree, ArrayList<seed> seedList, seed start, ArrayList<Pair> treelist, double width, double length, int seed_index){

        double deltaPosition = 0.001;
        
        //calculate angle between tree and seed
        double angle=angleBetweenSeedAndTree(start,tree);
        
        seed temp=new seed(start.x,start.y, start.tetraploid);
        seedList.add(temp);
        seed best=start;
        
        
        //iterating until seed cannot be closer to the tree
        while(PlayerUtil.validateseed(seedList,width,length,treelist)){
            best=new seed(temp.x,temp.y,temp.tetraploid);
//            System.out.println("Delta X: "+(start.x-best.x)+" Delta Y: "+(start.y-best.y));
            seedList.remove(seedList.size()-1);
            double deltaX = Math.cos(angle) * deltaPosition;
            double deltaY = Math.sin(angle) * deltaPosition;
            temp.x+=deltaX;
            temp.y+=deltaY;
            seedList.add(temp);
//            System.out.println("Delta X: "+(start.x-best.x)+" Delta Y: "+(start.y-best.y));
        }
     
        seedList.remove(seedList.size()-1);
//        System.out.println("Tree: "+tree.x+","+tree.y+" Angle: "+angle);
        System.out.println("Tree: "+tree.x+","+tree.y+" Seed: "+seed_index+" Delta X: "+(start.x-best.x)+" Delta Y: "+(start.y-best.y));
        start=best;
        
        seedList.add(seed_index,start);
        return seedList;
    }
    
    public static ArrayList<Integer> getSeedsNearTree(Pair tree, ArrayList<seed> seedlist){
        ArrayList<Integer> seed_indices=new ArrayList<Integer>();
        
        //iterate through seeds
        for(seed a_seed: seedlist){
            //calculate distance from seed to tree
            double distToTreeForSeed=distance(a_seed,tree);
            
            if(distToTreeForSeed<5){
                seed_indices.add(seedlist.indexOf(a_seed));
            }
        }
        
        return seed_indices;
    }
    
    public static double angleBetweenSeedAndTree(seed the_seed,Pair tree){
        return Math.atan((tree.y-the_seed.y)/(tree.x-the_seed.x));
    }
	
    
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
    
    public static ArrayList<seed> hexagonal(ArrayList<Pair> treelist, double length, double width)
    {
        double wallLength=length-distowall;
        double k=1.0;
        int rows=1;
        while(k<=wallLength){
            System.out.println("K: "+k);
            rows++;
            k+=1.7321;
        }
        k-=1.7321;
        rows=rows-2;
        double remainingSpace=wallLength-k;
        double extraSpacing=(remainingSpace-.01)/rows;
        
        System.out.println("Length Remaining: "+wallLength+" Remaining Space: "+remainingSpace+" Rows: "+rows+" Extra Spacing: "+extraSpacing);
        
        ArrayList<seed> seedlist = new ArrayList<seed>();
        double i = distowall;
        double j;
        double offset = 0;
        while(i <= length - distowall)
        {
            System.out.println("Row: "+i);
            j = distowall + offset;
            while (j <= width - distowall)
            {
                boolean add = true;
                boolean far = false;
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
                if(far){
                    j = j + distoseed+.01;
                    far=false;
                }
                else {
                    j= j + distoseed;
                    far=true;
                }
            }
            
            if (offset==0)
                offset = distowall;
            else 
                offset=0;
            
            i = i + 1.7321+extraSpacing;
        }
        return seedlist;
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
    
    //Calculating Euclidean distance
    public static double dist(double Ax, double Ay, double Bx, double By)
    {
        return (Math.sqrt(Math.pow(Ax-Bx,2)+Math.pow(Ay-By,2)));
    }
    
    static double distance(seed tmp, Pair pair) {
        return Math.sqrt((tmp.x - pair.x) * (tmp.x - pair.x) + (tmp.y - pair.y) * (tmp.y - pair.y));
    }
    

}
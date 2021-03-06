package watermelon.group5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import watermelon.sim.Pair;
import watermelon.sim.Point;
import watermelon.sim.seed;
public class Moving {

	 public static ArrayList<seed> moveAround(
			  ArrayList<seed> seedList, 
			  ArrayList<Pair>treeList,
			  double s,
			  double width,
			  double length){
			int maxIteration = 2;
			for(int i = 0 ; i < seedList.size() ;i++){
				//rand = (int) (Math.random() * seedList.size());
				seed seedToMove = seedList.remove(i);
				moveAround(seedList, seedToMove, treeList, s, width, length);
			}	
			
		 return seedList;
	 }
	
  // start is not included in the seedList
  public static void moveAround(
		  List<seed> seedList, 
		  seed start,
		  List<Pair>treeList,
		  double s,
		  double width,
		  double length){
	  
	  	
	  double deltaAngle = 0.5;
	  double deltaPosition = 0.01;
	  seedList.add(start);
	  double bestScore = PlayerUtil.calculatescore(seedList, s);
	  seedList.remove(seedList.size()-1);
	  seed temp = new seed(start.x, start.y, start.tetraploid);
	  
	  for ( double theta = 0 ; theta < 360 ; theta += deltaAngle){
		  double deltaX = Math.cos(Math.toRadians(theta)) * deltaPosition;
		  double deltaY = Math.sin(Math.toRadians(theta)) * deltaPosition;
		  while(true){
			  temp.x += deltaX;
			  temp.y += deltaY;
			  seedList.add(temp);
			  if(!PlayerUtil.validateseed(seedList, width, length, treeList)){
				  seedList.remove(seedList.size()-1);
				  break;
			  }else{
				  seedList.remove(seedList.size()-1);
			  }
			  
			  double tempScore = PlayerUtil.calculatescore(seedList, s);
			  
			  if (tempScore > bestScore ){
				  start.x = temp.x;
				  start.y = temp.y;
				  bestScore = tempScore;
			  }
			  
		  }
		 
		  
	  }
	  seedList.add(start);	  
  }
	
}

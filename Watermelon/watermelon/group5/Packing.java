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
		fill_seeds(seedlist,treelist,length, width);
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
		fill_seeds(seedlist,treelist,length, width);
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
		fill_seeds(seedlist,treelist,length, width);
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
			fill_seeds(seedlist,treelist,length, width);
			return seedlist;
		}
		
		
		public static void fill_seeds(ArrayList<seed> seedlist, ArrayList<Pair> treelist, double length, double width)
		{
			double offset = 0.005;
			for (double i = distowall; i < length - distowall; i = i + offset) {
				for (double j = distowall; j < width - distowall; j = j + offset) {
					
					seed tmp;	
				    tmp = new seed(j, i, false);	
					boolean add = true;
					for (int f = 0; f < treelist.size(); f++) {
						if (distance(tmp, treelist.get(f)) < distotree) {
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
	
		// this is from group1 physical pack 
		public static ArrayList<seed> physicalWithExisting(ArrayList<seed> tree, ArrayList<seed> existingLocation, double width, double height) {
			
			ArrayList<Seed> trees = PlayerUtil.convertSeedList(tree);
			ArrayList<Seed> existingLocations = null;
			if (existingLocation != null)
				existingLocations = PlayerUtil.convertSeedList(existingLocation);
			
			ArrayList<Seed> locationsPacked = new ArrayList<>();			// Final locations to return
			ArrayList<Seed> locationsToTry = new ArrayList<>();				// List of locations to try for the simulation; the front of the list has higher priority locations to try than the end of the list
			ArrayList<Seed> locationsToSimulate = null;								// The current set of locations being simulated; locations will be set to this if the simulation succeeds in finding a valid field
			Seed locationToTry = null;
			HashMap<String, Integer> numFailures = new HashMap<String, Integer>();		// Stores the number of times the simulation has failed for a particular location, so that we can avoid repeating those failures
			
			// Loose upper bound on the maximum number of seeds
			int maxSeeds = (int) (width * height / (Math.PI * Math.pow(1.0, 2)));
			
			// We set up the vector list now to avoid expensive object creation and garbage collection during each simulation
			ArrayList<Vector> vectors = new ArrayList<Vector>(maxSeeds);
			for (int i = 0; i < maxSeeds; i++)
				vectors.add(new Vector());
			
			if (existingLocations != null) {
				for (Seed l : existingLocations)
					locationsPacked.add(l);
			}
			
			for (double x = 1.0; x <= width - 1.0; x += .9282) {
				for (double y = 1.0; y <= height - 1.0; y += .9282) {
					Seed l = new Seed(x, y);
					boolean isValid = true;
							
					for (Seed tre : trees) {
						if (l.equals(tre)) {
							isValid = false;
							break;
						}
					}
					
					if (!isValid)
						continue;
						
					locationsToTry.add(l);
					numFailures.put(l.toString(), 0);
				}
			}
			
			while (true) {
				// Try various places to place a new seed on the field.  We shuffle the locations to avoid repeatedly running likely fruitless simulations in each iteration
				Collections.shuffle(locationsToTry);
				boolean success = false;
				
				for (Seed locationToTryOrig : locationsToTry) {
					locationToTry = new Seed(locationToTryOrig);
									
					if (numFailures.get(locationToTry.toString()) >= 3)
						continue;
					
					boolean isValid = true;
					
					for (Seed location : locationsPacked)
						if (locationToTry.equals(location))
							isValid = false;
									
					if (!isValid)
						continue;
					
					// Deep copy the existing best locations and add the new one to try
					locationsToSimulate = new ArrayList<Seed>();
					for (Seed location : locationsPacked)
						locationsToSimulate.add(new Seed(location));
					
					locationsToSimulate.add(locationToTry);
					
					for (int i = 0; i < 1000; i++) {
						success = simulateForces(locationsToSimulate, vectors, trees, width, height);
						if (success) {
							numFailures.put(locationToTryOrig.toString(), 0);
							break;
						} else {
							numFailures.put(locationToTryOrig.toString(), numFailures.get(locationToTryOrig.toString()) + 1);
						}
					}
					
					if (success) break;
				}
				
				if (!success) break;
				
				locationsPacked = locationsToSimulate;
			}
			ArrayList<seed>seedList = new ArrayList<>();
		    for(Seed s : locationsPacked){
		      seedList.add(s.getSeed(s));
		    }
			return seedList;
		}		
		
		private static boolean simulateForces(ArrayList<Seed> locations, ArrayList<Vector> vectors, ArrayList<Seed> trees, double width, double height) {
			for (Vector vector : vectors) {
				vector.x = 0;
				vector.y = 0;
			}
			
			// For each location, calculate its vector
			for (int i = 0; i < locations.size(); i++) {
				Seed location = locations.get(i);
				Vector vector = vectors.get(i);
				double d;
				
				// Test against the walls
				if (location.x < 1.0)
					vector.x += Math.max((1.0 - location.x) / 2, Math.min(1.0 - location.x, 0.01));
				
				if (location.y < 1.0)
					vector.y += Math.max((1.0 - location.y) / 2, Math.min(1.0 - location.y, 0.01));
				
				if (location.x > width - 1.0)
					vector.x -= Math.max((location.x - (width - 1.0)) / 2, Math.min(location.x - (width - 1.0), 0.01));
				
				if (location.y > height - 1.0)
					vector.y -= Math.max((location.y - (height - 1.0)) / 2, Math.min(location.y - (height - 1.0), 0.01));
				
				// Test against the trees
				for (Seed tree : trees) {
					if ((d = PlayerUtil.distanceSeed(location, tree)) < 2.0) {
						double m = Math.max((2.0 - d) / 2, Math.min(2.0 - d, 0.01));
						
						vector.x += Math.sqrt(Math.abs(location.x - tree.x)) * m * Math.signum(location.x - tree.x);
						vector.y += Math.sqrt(Math.abs(location.y - tree.y)) * m * Math.signum(location.y - tree.y);
					}
				}
				
				// Test against the other locations
				for (int j = i + 1; j < locations.size(); j++) {
					Seed testLocation = locations.get(j);
					
					if ((d = PlayerUtil.distanceSeed(location, testLocation)) < 2.0) {
						double m = Math.max((2.0- d) / 2, Math.min((2.0 - d), 0.01));
						
						double x = Math.sqrt(Math.abs(location.x - testLocation.x)) * m * Math.signum(location.x - testLocation.x);
						double y = Math.sqrt(Math.abs(location.y - testLocation.y)) * m * Math.signum(location.y - testLocation.y);
						
						vector.add(x, y);
						vectors.get(j).add(-x, -y);
					}
				}
			}
			
			boolean success = true;
			
			// Move each location by its vector and ensure it's not a zero vector because it's balanced between trees
			for (int i = 0; i < locations.size(); i++) {
				Vector v = vectors.get(i);
				Seed location = locations.get(i);
				
				if (!v.isNone()) {
					success = false;
					location.x += v.x;
					location.y += v.y;
				}
				
				if (!PlayerUtil.inBoundry(location, width, height))
					success = false;
				
				if (PlayerUtil.closeToTree(location, trees))
					success = false;
			}
			
			return success;
		}
		
}

		


package watermelon.shakeup;

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
	
	public static void shake_direction(ArrayList<seed> seedlist, ArrayList<Pair> treelist, double length, double width, int direction)
	{
		double granularity = 0.005;
		boolean[] shake_check = new boolean[seedlist.size()];
		Arrays.fill(shake_check, false);
		int i = 0;
		int prev_count = 0;
		double multiplier = 1;
		while(i<10000)
		{
			//System.out.println("Push iter "+i+" multiplier "+multiplier);
			for (seed s: seedlist)
			{
				if (PlayerUtil.distance(s, new Pair(0, s.y)) <= multiplier*distowall)
				{
					while(PlayerUtil.validSeed(s, seedlist, treelist, length,  width))
					{
						s.y += granularity;
					}
					s.y -= granularity;
					while(PlayerUtil.validSeed(s, seedlist, treelist, length,  width))
					{
						s.x -= granularity;
					}
					s.x += granularity;
				}
			}
			if (prev_count == shake_count(shake_check))
			{
				multiplier++;
				prev_count = shake_count(shake_check);
			}
			i++;
		}
	}
	
	public static int shake_count(boolean[] shake_check)
	{
		int count = 0;
		for(boolean shake: shake_check)
		{
			if (shake)
				count++;
		}
		return count;
	}
	public static void shake_things_up(ArrayList<seed> seedlist, ArrayList<Pair> treelist, double length, double width)
	{
		
	}

}
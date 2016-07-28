package spotipos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import spotipos.model.Propertie;

public class PropertiesServiceImpl implements PropertiesService {
	private Jedis jedis = new Jedis("localhost");
	
	private boolean isInside(int x, int y, int ax, int ay, int bx, int by) {

		if (x >= ax && y <= ay && x <= bx && y >= by) {
			return true;
		}
		
		return false;
	}
	
	public Propertie[] getProperties(Integer ax, Integer ay, Integer bx, Integer by) {
		
		long start = System.nanoTime();
		
		List<String> allPartitions = jedis.lrange("allpartitions", 0, -1);
		List<String> paritionsToSearch = new ArrayList<>();
		
		// selection area points, s1, s2, s3, s4
		int s1x = ax;
		int s1y = ay;
		
		int s2x = bx;
		int s2y = ay;
		
		int s3x = bx;
		int s3y = by;
		
		int s4x = ax;
		int s4y = by;
		
		for (String partitionId: allPartitions) {
			Map<String, String> bounds = jedis.hgetAll("partition:" + partitionId);
			
			int ulx = Integer.parseInt(bounds.get("ulx"));
			int uly = Integer.parseInt(bounds.get("uly"));
			
			int brx = Integer.parseInt(bounds.get("brx"));
			int bry = Integer.parseInt(bounds.get("bry"));
			
			//partition points p1, p2, p3, p4
			int p1x = ulx;
			int p1y = uly;
			
			int p2x = brx;
			int p2y = uly;
			
			int p3x = brx;
			int p3y = bry;
			
			int p4x = ulx;
			int p4y = bry;
			
			// checking if there is any partition point inside
			// selection area
			
			if (isInside(p1x, p1y, ax, ay, bx, by)) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			if (isInside(p2x, p2y, ax, ay, bx, by)) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			if (isInside(p3x, p3y, ax, ay, bx, by)) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			if (isInside(p4x, p4y, ax, ay, bx, by)) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			// checking if there is any selection area point
			// inside partition
			
			if (isInside(s1x, s1y, ulx, uly, brx, bry)) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			if (isInside(s2x, s2y, ulx, uly, brx, bry)) {
				paritionsToSearch.add(partitionId);
				continue;
			}

			if (isInside(s3x, s3y, ulx, uly, brx, bry)) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			if (isInside(s4x, s4y, ulx, uly, brx, bry)) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			// checkin side intersections between partition and selection area

			if (ulx >= ax && ulx <= bx && 
			    (isInside(ulx, ay, ulx, uly, brx, bry) || isInside(ulx, by, ulx, uly, brx, bry))) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			if (brx >= ax && brx <= bx && 
				(isInside(brx, ay, ulx, uly, brx, bry) || isInside(brx, by, ulx, uly, brx, bry))) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			if (uly >= ay && uly <= by && 
			    (isInside(ax, uly, ulx, uly, brx, bry) || isInside(bx, uly, ulx, uly, brx, bry))) {
				paritionsToSearch.add(partitionId);
				continue;
			}
			
			if (bry >= ay && bry <= by && 
			    (isInside(ax, bry, ulx, uly, brx, bry) || isInside(bx, bry, ulx, uly, brx, bry))) {
				paritionsToSearch.add(partitionId);
				continue;
			}
		}
		
		System.out.println(paritionsToSearch);
		
		Set<String> allIds = null;
		for (String partitionId: paritionsToSearch) {
			Set<String> propertiesIds = jedis.smembers("idsByPartition:" + partitionId);
			
			if (allIds == null) {
				allIds = propertiesIds;
			} else {
				allIds.addAll(propertiesIds);
			}
		}
		
		// old approach without optimization
		//Set<String> allIds = jedis.smembers("allids");
		
		List<Propertie> properties = new ArrayList<Propertie>();
		for(String id: allIds) {
			Map<String, String> data = jedis.hgetAll("properties-data:" + id);
			Propertie p = new Propertie();
			p.loadMap(data);
			
			if (isInside(p.x, p.y, ax, ay, bx, by)) {
				properties.add(p);
			}
		}
		
		Propertie[] propertiesArray = new Propertie[properties.size()];
		properties.toArray(propertiesArray);
		
		long end = System.nanoTime();
		
		System.out.println((double)(end - start) / 1000000000.0);
		
		return propertiesArray;
	}
	
}

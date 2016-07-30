package spotipos.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exceptions.InvalidPropertieException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import spotipos.model.Propertie;

public class PropertiesServiceImpl implements PropertiesService {
	private Jedis jedis = new Jedis("localhost");
	
	/**
	 * Check if the point (x, y) is inside the rectangle (A, B)
	 * where A = (ax, ay) is the upperLeft point and B = (bx, by)
	 * is the lowerRight point
	 * */
	private boolean isInside(int x, int y, int ax, int ay, int bx, int by) {

		if (x >= ax && y <= ay && x <= bx && y >= by) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Validate a propertie and return the list of errors
	 * */
	private String[] validate(Propertie p) {
		List<String> validationResult = new ArrayList<String>();

		if (p.getX() == null) {
			validationResult.add("X coordinate must be greater or equals to zero");
		}
		
		if (p.getY() == null) {
			validationResult.add("Y coordinate must be greater or equals to zero");
		}
		
		if (p.getTitle() == null || p.getTitle().isEmpty()) {
			validationResult.add("Title cannot be empty");
		}
		
		if (p.getDescription() == null || p.getDescription().isEmpty()) {
			validationResult.add("Description cannot be empty");
		}
		
		if (p.getPrice() == null) {
			validationResult.add("Price must be grater or equals to zero");
		}
		
		if (p.getSquareMeters() == null) {
			validationResult.add("SquareMeters must be grater than zero");
		}
		
		if (p.getBaths() == null) {
			validationResult.add("Baths must be grater or equals to one");
		}
		
		if (p.getBeds() == null) {
			validationResult.add("Beds must be grater or equals to one");
		}
		
		
		String[] validation = new String[validationResult.size()];
		validationResult.toArray(validation);
		
		return validation;
		
	}
	
	/**
	 * Create a propertie
	 * */
	public Propertie create(Propertie data) throws InvalidPropertieException {
		
		//validating propertie
		String[] validation = validate(data);
		
		if (validation.length > 0) {
			//invalid
			throw new InvalidPropertieException(validation, "Invalid Propertie");
		}
		
		//creating new propertie
		String newId = "prop-" + Long.toString(jedis.incr("idcounter"));
		data.setId(newId);
		
		String newPropertieKey = "properties-data:" + newId;
		
		Pipeline pipe = jedis.pipelined();
		pipe.hset(newPropertieKey, "id", newId);
		pipe.hset(newPropertieKey, "x", Integer.toString(data.getX()));
		pipe.hset(newPropertieKey, "y", Integer.toString(data.getY()));
		pipe.hset(newPropertieKey, "beds", Integer.toString(data.getBeds()));
		pipe.hset(newPropertieKey, "baths", Integer.toString(data.getBaths()));
		pipe.hset(newPropertieKey, "squareMeters", Integer.toString(data.getSquareMeters()));
		pipe.hset(newPropertieKey, "price", Long.toString(data.getPrice()));
		pipe.hset(newPropertieKey, "title", data.getTitle());
		pipe.hset(newPropertieKey, "description", data.getDescription());
		pipe.sync();
		
		//finding propertie partition
		List<String> allPartitions = jedis.lrange("allpartitions", 0, -1);
		Map<String, Response<Map<String, String>>> allPartitionsData = new HashMap<>();
		for (String partitionId: allPartitions) { 
			allPartitionsData.put(partitionId, pipe.hgetAll("partition:" + partitionId));
		}
		pipe.sync();
		
		for (String partitionId: allPartitionsData.keySet()) {
			Map<String, String> partitionData = allPartitionsData.get(partitionId).get();
			
			int ulx = Integer.parseInt(partitionData.get("ulx"));
			int uly = Integer.parseInt(partitionData.get("uly"));
			
			int brx = Integer.parseInt(partitionData.get("brx"));
			int bry = Integer.parseInt(partitionData.get("bry"));
			
			if (isInside(data.getX(), data.getY(), ulx, uly, brx, bry)) {
				pipe.sadd("idsByPartition:" + partitionId, data.getId());
				pipe.sadd("allids", data.getId());
			}
		}
		
		pipe.sync();
		
		return data;
	}
	
	/**
	 * Get a propertie by id
	 * */
	public Propertie getPropertie(String id) {
		Map<String, String> data = jedis.hgetAll("properties-data:" + id);
		
		if (data != null) {
			Propertie p = new Propertie();
			p.loadMap(data);
			
			return p;
		}
		
		return null;
	}
	
	/**
	 * Return a list of properties inside the rectangle (A, B)
	 * A = upperLeft na B = lowerRight
	 * */
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
		Pipeline pipe = jedis.pipelined();
		List<Response<Map<String, String>>> responses = new ArrayList<>();
		for(String id: allIds) {
			Response<Map<String, String>> data = pipe.hgetAll("properties-data:" + id);
			responses.add(data);
		}
		
		pipe.sync(); 
		
		for (Response<Map<String, String>> response: responses) {
			Map<String, String> data = response.get();
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

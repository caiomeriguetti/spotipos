package spotipos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import spotipos.model.Propertie;

public class PropertiesServiceImpl implements PropertiesService {
	private Jedis jedis = new Jedis("localhost");
	
	public Propertie[] getProperties(Integer ax, Integer ay, Integer bx, Integer by) {
		
		Set<String> allIds = jedis.smembers("allids");
		List<Propertie> properties = new ArrayList<Propertie>();
		for(String id: allIds) {
			Map<String, String> data = jedis.hgetAll("properties-data:" + id);
			Propertie p = new Propertie();
			p.loadMap(data);
			
			if (p.x >= ax && p.y >= ay && p.x <= bx && p.y <= by) {
				properties.add(p);
			}
		}
		
		Propertie[] propertiesArray = new Propertie[properties.size()];
		properties.toArray(propertiesArray);
		return propertiesArray;
	}
	
}

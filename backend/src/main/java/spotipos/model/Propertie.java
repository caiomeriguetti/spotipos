package spotipos.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Propertie {
	public String id;
	public Integer x;
	public Integer y;
	public Integer beds;
	public Integer baths;
	public Integer squareMeters;
    
    public void loadMap(Map<String, String> data) {
    	id = data.get("id");
    	x = Integer.parseInt(data.get("x"));
    	y = Integer.parseInt(data.get("y"));
    	beds = Integer.parseInt(data.get("beds"));
    	baths = Integer.parseInt(data.get("baths"));
    	squareMeters = Integer.parseInt(data.get("squareMeters"));
    }
    
}

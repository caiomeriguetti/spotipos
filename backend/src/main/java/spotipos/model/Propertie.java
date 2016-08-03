package spotipos.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

@XmlRootElement
public class Propertie {
	public String id;
	public String title;
	public String description;
	public Integer x;
	public Integer y;
	public Integer beds;
	public Integer baths;
	public Integer squareMeters;
	public Long price;
	public String[] provinces;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getBeds() {
		return beds;
	}

	public void setBeds(Integer beds) {
		this.beds = beds;
	}

	public Integer getBaths() {
		return baths;
	}

	public void setBaths(Integer baths) {
		this.baths = baths;
	}

	public Integer getSquareMeters() {
		return squareMeters;
	}

	public void setSquareMeters(Integer squareMeters) {
		this.squareMeters = squareMeters;
	}
	
	public void loadJsonObject(JSONObject data) {
		
		if (data.has("id")) {
			String dataId = data.getString("id");
			if (dataId != null) {
				id = dataId;
			}
		}
		
		if (data.has("x")) {
			Integer dataX = data.getInt("x");
			if (dataX != null) {
				x = dataX;
			}
		}
		
		if (data.has("y")) {
			Integer dataY = data.getInt("y");
			if (dataY != null) {
				y = dataY;
			}
		}
		
		if (data.has("beds")) {
			Integer dataBeds = data.getInt("beds");
			if (dataBeds != null) {
				beds = dataBeds;
			}
		}
		
		if (data.has("baths")) {
			Integer dataBaths = data.getInt("baths");
			if (dataBaths != null) {
				baths = dataBaths;
			}
		}
		
		if (data.has("squareMeters")) {
			Integer dataSquareMeters = data.getInt("squareMeters");
			if (dataSquareMeters != null) {
				squareMeters = dataSquareMeters;
			}
		}
		
		if (data.has("price")) {
			Long dataPrice = data.getLong("price");
			if (dataPrice != null) {
				price = dataPrice;
			}
		}
    	
		if (data.has("title")) {
	    	String dataTitle = data.getString("title");
	    	if (dataTitle != null && !"".equals(dataTitle)) {
	    		title = dataTitle;
	    	}
		}
    	
		if (data.has("description")) {
	    	String dataDescription = data.getString("description");
	    	if (dataDescription != null && !"".equals(dataDescription)) {
	    		description = dataDescription;
	    	}
		}
	}

	public void loadMap(Map<String, String> data) {
		String dataId = data.get("id");
		
		if (dataId != null) {
			id = dataId;
		}
		
		String dataX = data.get("x");
		if (dataX != null) {
			x = Integer.parseInt(dataX);
		}
		
		String dataY = data.get("y");
		if (dataY != null) {
			y = Integer.parseInt(dataY);
		}
		
		String dataBeds = data.get("beds");
		if (dataBeds != null) {
			beds = Integer.parseInt(dataBeds);
		}
		
		String dataBaths = data.get("baths");
		if (dataBaths != null) {
			baths = Integer.parseInt(dataBaths);
		}
		
		String dataSquareMeters = data.get("squareMeters");
		if (dataSquareMeters != null) {
			squareMeters = Integer.parseInt(dataSquareMeters);
		}
		
		String dataPrice = data.get("price");
		if (dataPrice != null) {
			price = Long.parseLong(dataPrice);
		}
    	
    	String dataTitle = data.get("title");
    	if (dataTitle != null && !"".equals(dataTitle)) {
    		title = dataTitle;
    	}
    	
    	String dataDescription = data.get("description");
    	if (dataDescription != null && !"".equals(dataDescription)) {
    		description = dataDescription;
    	}
    }
	
	public void setProvinces(String[] provinces) {
		this.provinces = provinces;
	}
	
	public String[] getProvinces() {
		return this.provinces;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
    
}

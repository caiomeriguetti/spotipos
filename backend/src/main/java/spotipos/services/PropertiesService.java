package spotipos.services;

import spotipos.model.Propertie;

public interface PropertiesService {
	public Propertie[] getProperties(Integer ax, Integer ay, Integer bx, Integer by);
	public Propertie getPropertie(String id);
}

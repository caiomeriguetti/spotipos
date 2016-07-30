package spotipos.services;

import exceptions.InvalidPropertieException;
import spotipos.model.Propertie;

public interface PropertiesService {
	public Propertie[] getProperties(Integer ax, Integer ay, Integer bx, Integer by);
	public Propertie getPropertie(String id);
	public Propertie create(Propertie data) throws InvalidPropertieException;
}

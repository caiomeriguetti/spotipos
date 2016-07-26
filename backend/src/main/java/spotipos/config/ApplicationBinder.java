package spotipos.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import spotipos.services.PropertiesService;
import spotipos.services.PropertiesServiceImpl;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        this.bind(PropertiesServiceImpl.class).to(PropertiesService.class);
    }
}
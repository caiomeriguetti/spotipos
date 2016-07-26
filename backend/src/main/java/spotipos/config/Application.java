package spotipos.config;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;

import spotipos.rest.GlobalHeadersFilter;

public class Application extends ResourceConfig {
    public Application() {
        register(new ApplicationBinder());
        register(getMoxyProvider());
        register(GlobalHeadersFilter.class);
        packages(true, "spotipos.rest");
    }
    
    private ContextResolver<MoxyJsonConfig> getMoxyProvider() {
    	final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
        Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
        return moxyJsonConfig.resolver();
    }
}
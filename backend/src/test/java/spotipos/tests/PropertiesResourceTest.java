package spotipos.tests;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.test.JerseyTest;


public class PropertiesResourceTest extends JerseyTest {
	
	public PropertiesResourceTest() {
        super(new spotipos.config.Application());
    }
	
	private Invocation.Builder invocation(WebTarget target) {
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
		invocationBuilder.header("Content-Type", "text/plain; charset=utf-8");
		invocationBuilder.header("Accept", "*/*");
		invocationBuilder.header("Accept-Encoding", "gzip, deflate, sdch");
		invocationBuilder.header("Accept-Language", "en-US,en;q=0.8");
		
		return invocationBuilder;
	}

}

package simplyrestful.springboot.configuration.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.autoconfigure.jersey.ResourceConfigCustomizer;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import io.openapitools.jackson.dataformat.hal.HALMapper;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import simplyrestful.api.framework.core.servicedocument.WebResourceRoot;

@Configuration
public class JerseySpringBootConfiguration implements ResourceConfigCustomizer{
	@Override
	public void customize(ResourceConfig config) {
		config.register(WebResourceRoot.class);
		config.register(new JacksonJsonProvider(new HALMapper()));
		config.register(ApiListingResource.class);
		config.register(SwaggerSerializers.class);
	}
}

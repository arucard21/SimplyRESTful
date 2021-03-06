package simplyrestful.api.framework.servicedocument;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import io.openapitools.jackson.dataformat.hal.HALLink;
import io.swagger.v3.oas.annotations.Operation;
import simplyrestful.api.framework.MediaTypeUtils;
import simplyrestful.api.framework.resources.HALServiceDocument;

@Named
@Path("")
@Produces(MediaTypeUtils.APPLICATION_HAL_JSON + "; profile=\""+HALServiceDocument.PROFILE_STRING + "\"")
@Consumes(MediaTypeUtils.APPLICATION_HAL_JSON + "; profile=\""+HALServiceDocument.PROFILE_STRING + "\"")
public class WebResourceRoot{
	@Context
	private UriInfo uriInfo;

	@Produces({MediaTypeUtils.APPLICATION_HAL_JSON})
	@GET
	@Operation(
		description  = "Provide a service document that describes the service and links to the OpenAPI Specification"
	)
	public HALServiceDocument getServiceDocument() {
		HALServiceDocument serviceDocument = new HALServiceDocument();
		HALLink descriptionLink = new HALLink.Builder(uriInfo.getRequestUriBuilder().path("openapi.json").build()).build();
		serviceDocument.setDescribedBy(descriptionLink);
		HALLink selfLink = new HALLink.Builder(uriInfo.getRequestUriBuilder().build())
				.type(MediaTypeUtils.APPLICATION_HAL_JSON)
				.profile(serviceDocument.getProfile())
				.build();
		serviceDocument.setSelf(selfLink);
		return serviceDocument;
	}
}

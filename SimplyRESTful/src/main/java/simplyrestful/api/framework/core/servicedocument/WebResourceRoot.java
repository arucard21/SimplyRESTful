package simplyrestful.api.framework.core.servicedocument;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import io.openapitools.jackson.dataformat.hal.HALLink;
import io.swagger.annotations.ApiOperation;
import simplyrestful.api.framework.core.MediaType;

@Named
@Path("")
public class WebResourceRoot{
	@Context
	private UriInfo uriInfo;
	
	@Produces({MediaType.APPLICATION_HAL_JSON})
	@GET
	@ApiOperation(
		value = "Provide a service document that describes the service and links to the OpenAPI Specification",
		notes = "Provide a service document that describes the service and links to the OpenAPI Specification"
	)
	public HALServiceDocument getServiceDocument() {
		HALServiceDocument serviceDocument = new HALServiceDocument();
		HALLink descriptionLink = new HALLink.Builder(uriInfo.getRequestUriBuilder().path("swagger.json").build()).build();
		serviceDocument.setDescribedby(descriptionLink);
		HALLink selfLink = new HALLink.Builder(uriInfo.getRequestUriBuilder().build()).build();
		serviceDocument.setSelf(selfLink);
		return serviceDocument;
	}
}
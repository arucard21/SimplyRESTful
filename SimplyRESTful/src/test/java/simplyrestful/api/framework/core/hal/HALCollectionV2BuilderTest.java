package simplyrestful.api.framework.core.hal;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.openapitools.jackson.dataformat.hal.HALLink;
import simplyrestful.api.framework.core.AdditionalMediaTypes;
import simplyrestful.api.framework.resources.HALCollectionV2;
import simplyrestful.api.framework.resources.HALResource;

public class HALCollectionV2BuilderTest {
    protected static final String HALCOLLECTION_PROFILE = "https://arucard21.github.io/SimplyRESTful-Framework/HALCollection/v2";
    protected static final int TEST_RESOURCES_SIZE = 1000;
    protected static final URI requestURI = URI.create("local://resources/testresources/");
    protected List<TestResource> testResourcesList;

    @BeforeEach
    public void createSourceData() {
	testResourcesList = new ArrayList<TestResource>();
	for (int i = 0; i < TEST_RESOURCES_SIZE; i++) {
	    TestResource testResource = new TestResource();
	    testResource.setNumber(i);
	    URI selfLink = UriBuilder.fromUri(requestURI).path(String.valueOf(i)).build();
	    testResource.setSelf(new HALLink.Builder(selfLink).build());
	    testResourcesList.add(testResource);
	}
    }

    protected HALCollectionV2<TestResource> createExpectedCollection(
	    int startOfFirst, 
	    int startOfLast,
	    int startOfPrev,
	    int startOfNext, 
	    int sublistBegin,
	    int sublistEnd) {
	HALCollectionV2<TestResource> expected = new HALCollectionV2<TestResource>();
	expected.setSelf(new HALLink.Builder(requestURI).type(AdditionalMediaTypes.APPLICATION_HAL_JSON)
		.profile(expected.getProfile()).build());
	expected.setTotal(TEST_RESOURCES_SIZE);
	HALLink firstPage = startOfFirst == -1 ? null
		: new HALLink.Builder(UriBuilder.fromUri(requestURI).replaceQueryParam("pageStart", startOfFirst).build()).build();
	HALLink lastPage = startOfLast == -1 ? null
		: new HALLink.Builder(UriBuilder.fromUri(requestURI).replaceQueryParam("pageStart", startOfLast).build()).build();
	HALLink prevPage = startOfPrev == -1 ? null
		: new HALLink.Builder(UriBuilder.fromUri(requestURI).replaceQueryParam("pageStart", startOfPrev).build()).build();
	HALLink nextPage = startOfNext == -1 ? null
		: new HALLink.Builder(UriBuilder.fromUri(requestURI).replaceQueryParam("pageStart", startOfNext).build()).build();
	expected.setFirst(firstPage);
	expected.setLast(lastPage);
	expected.setPrev(prevPage);
	expected.setNext(nextPage);
	List<TestResource> resourcesForPage = testResourcesList.subList(sublistBegin, sublistEnd);
	
	expected.setItem(resourcesForPage);
	return expected;
    }

    protected class TestResource extends HALResource {
	private int number;

	@Override
	public URI getProfile() {
	    return URI.create("local://docs/resources/testresource");
	}

	public int getNumber() {
	    return number;
	}

	public void setNumber(int number) {
	    this.number = number;
	}
    }

    @Test
    public void test_createPagedCollection_DefaultValues() {
	int pageStart = 0;
	int maxPageSize = 100;
	List<TestResource> resources = testResourcesList.subList(0, 100);
	
	HALCollectionV2<TestResource> actual = HALCollectionV2Builder
		.from(resources, requestURI)
		.collectionSize(TEST_RESOURCES_SIZE)
		.withNavigation(pageStart, maxPageSize)
		.build();
	HALCollectionV2<TestResource> expected = createExpectedCollection(0, 900, -1, 100, 0, 100);
	Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_createPagedCollection_WithPrevPage() {
	int pageStart = 300;
	int maxPageSize = 100;
	List<TestResource> resources = testResourcesList.subList(200, 300);
	
	HALCollectionV2<TestResource> actual = HALCollectionV2Builder
		.from(resources, requestURI)
		.collectionSize(TEST_RESOURCES_SIZE)
		.withNavigation(pageStart, maxPageSize)
		.build();
	HALCollectionV2<TestResource> expected = createExpectedCollection(0, 900, 200, 400, 200, 300);
	Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_createPagedCollection_WithoutNextPage() {
	int pageStart = 900;
	int maxPageSize = 100;
	List<TestResource> resources = testResourcesList.subList(900, 1000);

	HALCollectionV2<TestResource> actual = HALCollectionV2Builder
		.from(resources, requestURI)
		.collectionSize(TEST_RESOURCES_SIZE)
		.withNavigation(pageStart, maxPageSize)
		.build();
	HALCollectionV2<TestResource> expected = createExpectedCollection(0, 900, 800, -1, 900, 1000);
	Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_createPagedCollection_WithLastPageNotEqualToMaxPageSize() {
	int pageStart = 4;
	int maxPageSize = 300;
	List<TestResource> resources = testResourcesList.subList(900, 1000);
	
	HALCollectionV2<TestResource> actual = HALCollectionV2Builder
		.from(resources, requestURI)
		.collectionSize(TEST_RESOURCES_SIZE)
		.withNavigation(pageStart, maxPageSize)
		.build();
	Assertions.assertEquals(100, actual.getItem().size());
    }

    @Test
    public void test_createPagedCollection_UsesCorrectProfile() {
	int pageStart = 1;
	int maxPageSize = 100;
	List<TestResource> resources = testResourcesList.subList(200, 300);
	
	HALCollectionV2<TestResource> actual = HALCollectionV2Builder
		.from(resources, requestURI)
		.collectionSize(TEST_RESOURCES_SIZE)
		.withNavigation(pageStart, maxPageSize)
		.build();
	Assertions.assertEquals(URI.create(HALCOLLECTION_PROFILE), actual.getProfile());
    }

    @Test
    public void test_createPagedCollection_UsesCorrectMediaType() {
	int pageStart = 1;
	int maxPageSize = 100;
	List<TestResource> resources = testResourcesList.subList(200, 300);
	HALCollectionV2<TestResource> actual = HALCollectionV2Builder
		.from(resources, requestURI)
		.collectionSize(TEST_RESOURCES_SIZE)
		.withNavigation(pageStart, maxPageSize)
		.build();
	Assertions.assertEquals(AdditionalMediaTypes.APPLICATION_HAL_JSON, actual.getSelf().getType());
    }
}

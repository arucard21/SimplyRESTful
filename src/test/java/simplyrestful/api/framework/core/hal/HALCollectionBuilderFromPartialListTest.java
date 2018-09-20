package simplyrestful.api.framework.core.hal;

import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import simplyrestful.api.framework.core.MediaType;

public class HALCollectionBuilderFromPartialListTest extends AbstractHALCollectionBuilderTest{
	@Test
	public void test_createPagedCollectionFromPartialList_DefaultValues() {
		int page = 1;
		int maxPageSize = 100;
		boolean compact = true;
		builder = new HALCollectionBuilderFromPartialList<TestResource>(testResourcesList.subList(0, 100), requestURI, TEST_RESOURCES_SIZE);
		HALCollection<TestResource> actual = builder.page(page).maxPageSize(maxPageSize).compact(compact).build();
		HALCollection<TestResource> expected = createExpectedCollection(page, maxPageSize, 1, 10, -1, 2, 0, 100, compact);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_createPagedCollectionFromPartialList_WithPrevPage() {
		int page = 3;
		int maxPageSize = 100;
		boolean compact = true;
		builder = new HALCollectionBuilderFromPartialList<TestResource>(testResourcesList.subList(200, 300), requestURI, TEST_RESOURCES_SIZE);
		HALCollection<TestResource> actual = builder.page(page).maxPageSize(maxPageSize).compact(compact).build();
		HALCollection<TestResource> expected = createExpectedCollection(page, 100, 1, 10, 2, 4, 200, 300, compact);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_createPagedCollectionFromPartialList_WithoutNextPage() {
		int page = 10;
		int maxPageSize = 100;
		boolean compact = true;
		builder = new HALCollectionBuilderFromPartialList<TestResource>(testResourcesList.subList(900, 1000), requestURI, TEST_RESOURCES_SIZE);
		HALCollection<TestResource> actual = builder.page(page).maxPageSize(maxPageSize).compact(compact).build();
		HALCollection<TestResource> expected = createExpectedCollection(page, 100, 1, 10, 9, -1, 900, 1000, compact);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_createPagedCollectionFromPartialList_Embedded() {
		int page = 10;
		int maxPageSize = 100;
		boolean compact = false;
		builder = new HALCollectionBuilderFromPartialList<TestResource>(testResourcesList.subList(900, 1000), requestURI, TEST_RESOURCES_SIZE);
		HALCollection<TestResource> actual = builder.page(page).maxPageSize(maxPageSize).compact(compact).build();
		HALCollection<TestResource> expected = createExpectedCollection(page, 100, 1, 10, 9, -1, 900, 1000, compact);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_createPagedCollectionFromPartialList_WithLastPageNotEqualToMaxPageSize() {
		int page = 4;
		int maxPageSize = 300;
		boolean compact = true;
		builder = new HALCollectionBuilderFromPartialList<TestResource>(testResourcesList.subList(900, 1000), requestURI, TEST_RESOURCES_SIZE);
		HALCollection<TestResource> actual = builder.page(page).maxPageSize(maxPageSize).compact(compact).build();
		Assertions.assertEquals(100, actual.getItem().size());
		Assertions.assertEquals(300, actual.getMaxPageSize());
	}

	@Test
	public void test_createPagedCollectionFromPartialList_UsesCorrectProfile() {
		int page = 1;
		int maxPageSize = 100;
		boolean compact = true;
		builder = new HALCollectionBuilderFromPartialList<TestResource>(testResourcesList.subList(200, 300), requestURI, TEST_RESOURCES_SIZE);
		HALCollection<TestResource> actual = builder.page(page).maxPageSize(maxPageSize).compact(compact).build();
		Assertions.assertEquals(URI.create(HALCOLLECTION_PROFILE), actual.getProfile());
	}

	@Test
	public void test_createPagedCollectionFromPartialList_UsesCorrectMediaType() {
		int page = 1;
		int maxPageSize = 100;
		boolean compact = true;
		builder = new HALCollectionBuilderFromPartialList<TestResource>(testResourcesList.subList(200, 300), requestURI, TEST_RESOURCES_SIZE);
		HALCollection<TestResource> actual = builder.page(page).maxPageSize(maxPageSize).compact(compact).build();
		Assertions.assertEquals(MediaType.APPLICATION_HAL_JSON, actual.getSelf().getType());
	}
}

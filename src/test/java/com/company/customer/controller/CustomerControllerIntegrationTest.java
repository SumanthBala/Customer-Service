package com.company.customer.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import com.company.customer.CustomerApplication;
import com.company.customer.model.Customer;
import com.company.customer.util.GenerateJwtToken;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CustomerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIntegrationTest {

	@LocalServerPort
	private int port;

	private static final String CUSTOMER_SERVICE_PATH = "/v1/api/customers/";

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	@BeforeAll
	public void loadCustomer() throws OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + validToken);
		Customer customer = Customer.builder().firstName("Arun").lastName("Garimella")
				.dateOfBirth(LocalDate.of(1992, 06, 14)).build();

		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH), HttpMethod.POST, entity, Customer.class);
	}

	@Test
	public void testAddCustomerWithoutOAuthToken() {

		Customer customer = Customer.builder().firstName("Iest").lastName("Integration").dateOfBirth(LocalDate.now())
				.build();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.POST, entity, Customer.class);

		int actual = response.getStatusCode().value();

		assertTrue(actual == 403);

	}

	@Test
	public void testAddCustomerWithInvalidOAuthToken() {
		String mockToken = GenerateJwtToken.generateMockJwtToken();
		headers.set("Authorization", "Bearer " + mockToken);
		Customer customer = Customer.builder().firstName("Iest").lastName("Integration").dateOfBirth(LocalDate.now())
				.build();

		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.POST, entity, Customer.class);

		int actual = response.getStatusCode().value();

		assertTrue(actual == 401);

	}

	@Test
	public void testAddCustomerWithValidOAuthToken() throws OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		headers.set("Authorization", "Bearer " + validToken);
		Customer customer = Customer.builder().firstName("Iest").lastName("Integration")
				.dateOfBirth(LocalDate.of(2024, 04, 28)).build();

		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.POST, entity, Customer.class);

		int actual = response.getStatusCode().value();

		assertTrue(actual == 200);

	}

	@Test
	public void testRetrieveCustomers() throws JSONException, OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		headers.set("Authorization", "Bearer " + validToken);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.GET, entity, String.class);

		String expected = "[{\"firstName\":\"Arun\",\"lastName\":\"Garimella\",\"dateOfBirth\":\"1992-06-14\"},{\"firstName\":\"Iest\",\"lastName\":\"Integration\",\"dateOfBirth\":\"2024-04-28\"}]";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void testRetrieveCustomersByLastName() throws JSONException, OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		headers.set("Authorization", "Bearer " + validToken);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(createURLWithPort(CUSTOMER_SERVICE_PATH + "search")).queryParam("lastName", "Garimella");
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);
		System.out.println(response.getBody());
		String expected = "[{\"firstName\":\"Arun\",\"lastName\":\"Garimella\",\"dateOfBirth\":\"1992-06-14\"}]";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void testRetrieveCustomersByNotExistedLastName()
			throws JSONException, OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		headers.set("Authorization", "Bearer " + validToken);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(createURLWithPort(CUSTOMER_SERVICE_PATH + "search")).queryParam("lastName", "blala");
		ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);
		int actual = response.getStatusCode().value();

		assertTrue(actual == 404);
	}

	@Test
	public void testAddCustomerWithInvalidFirstName() throws OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		headers.set("Authorization", "Bearer " + validToken);
		Customer customer = Customer.builder().firstName("123").lastName("Integration")
				.dateOfBirth(LocalDate.of(2024, 04, 28)).build();

		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.POST, entity, Customer.class);

		int actual = response.getStatusCode().value();

		assertTrue(actual == 400);

	}

	@Test
	public void testAddCustomerWithInvalidLastName() throws OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		headers.set("Authorization", "Bearer " + validToken);
		Customer customer = Customer.builder().firstName("Test").lastName("Integration123")
				.dateOfBirth(LocalDate.of(2024, 04, 28)).build();

		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.POST, entity, Customer.class);

		int actual = response.getStatusCode().value();

		assertTrue(actual == 400);

	}

	@Test
	public void testAddCustomerWithoutProvidingAllRequiredFileds() throws OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		headers.set("Authorization", "Bearer " + validToken);
		Customer customer = Customer.builder().firstName("Test").build();

		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.POST, entity, Customer.class);

		int actual = response.getStatusCode().value();

		assertTrue(actual == 400);

	}

	@Test
	public void testAddingDuplicateCustomer() throws OAuthSystemException, OAuthProblemException {
		String validToken = GenerateJwtToken.generateToken();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + validToken);

		HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<String> initialResponse = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.GET, httpEntity, String.class);

		Customer customer = Customer.builder().firstName("Arun").lastName("Garimella")
				.dateOfBirth(LocalDate.of(1992, 06, 14)).build();

		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH), HttpMethod.POST, entity, Customer.class);

		ResponseEntity<String> finalResponse = restTemplate.exchange(createURLWithPort(CUSTOMER_SERVICE_PATH),
				HttpMethod.GET, httpEntity, String.class);

		assertTrue(initialResponse.getBody().equals(finalResponse.getBody()));
	}

}

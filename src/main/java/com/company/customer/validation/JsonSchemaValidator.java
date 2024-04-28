package com.company.customer.validation;

import java.io.InputStream;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.company.customer.exception.CustomerValidationException;
import com.company.customer.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

@Component
public class JsonSchemaValidator {

	public void validateSchema(Customer customer)
			throws JsonMappingException, JsonProcessingException, CustomerValidationException {
		InputStream schemaAsStream = JsonSchemaValidator.class.getClassLoader()
				.getResourceAsStream("customer-schema.json");
		JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaAsStream);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
		JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(customer));

		Set<ValidationMessage> errors = schema.validate(jsonNode);
		String errorsCombined = "";
		for (ValidationMessage error : errors) {

			errorsCombined += error.getMessage() + "\n";
		}

		if (errors.size() > 0)
			throw new CustomerValidationException("Please fix the json! " + errorsCombined);
	}
}

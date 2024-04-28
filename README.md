# Customer-Service
**Customer Service API**

A simple Spring Boot application providing the customer service API.

1. **Database Integration**:
   - Uses H2 database for storing customer data.
   - Utilizes Spring Data JPA for interacting with the H2 database.

2. **JSON Schema Validation**:
   - Implements JSON schema validation for validating incoming JSON requests.
   - Ensures that the structure and data of JSON documents conform to a predefined schema.

3. **Swagger-UI Documentation**:
   - Integrates Swagger-UI for documenting and testing the APIs.
   - Enables developers to explore and test the API endpoints easily.

4. **OAuth2 Security**:
   - Secures the endpoints using OAuth2 security.
   - Utilizes Okta for Authentication and Authorization, ensuring secure access to the API.
   
5. **Liquibase**
   - Liquibase is a powerful tool for database schema management and version control. 
   
6. **Integration Tests**:
   - Includes integration tests covering various scenarios to ensure the API functions as expected in a real environment.

7. **Unit Tests**:
   - Contains unit tests for individual components, ensuring their functionality in isolation.

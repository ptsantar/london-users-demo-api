package london.users.demo.api;

import london.users.demo.api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LondonUsersDemoApiApplicationTests {
	@LocalServerPort
	private int port;

	private URL base;

	@Autowired
	private TestRestTemplate template;


	@BeforeEach
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port + "/");
	}

	@Test
	void instructionsTest() {
		String todo = "{\"todo\": \"Build an API which calls this API, and returns people who are listed as either living in " +
				"London, or whose current coordinates are within 50 miles of London. Push the answer to Github, " +
				"and send us a link.\"}\n";
		ResponseEntity<String> instructions = template.getForEntity(
				base.toString()+"/london-users/api/v1/instructions",
				String.class
		);
		assertEquals(200, instructions.getStatusCodeValue());
		assertEquals(todo, instructions.getBody());
	}

	@Test
	void allUsersTest(){
		// get the results from `localhost` endpoint
		ResponseEntity<User[]> users = template.exchange(
				base.toString()+"/london-users/api/v1/users/",
				HttpMethod.GET,
				null,
				User[].class);
		// get the results from the 3rd API party
		ResponseEntity<User[]> expectedUsers = template.exchange(
				"https://bpdts-test-app.herokuapp.com/users",
				HttpMethod.GET,
				null,
				User[].class);
		// assert that both bodies are not null
		assertAll(
				() -> assertNotNull(expectedUsers.getBody()),
				() -> assertNotNull(users.getBody())
		);
		// assert that the bodies are of the same size and then compare the user 1 by 1
		assertAll(
				() -> assertEquals(expectedUsers.getBody().length, users.getBody().length),
				() -> IntStream.range(0, 1000).forEach(i ->
						assertEquals(expectedUsers.getBody()[i], users.getBody()[i])
				)
		);
	}

	@Test
	void individualUserTest(){
		Random r = new Random();
		// for 10 random user
		IntStream.range(0, 10).map(i -> r.nextInt(1000)).forEach( i -> {
			// get the results from `localhost` endpoint
			ResponseEntity<User> user = template.exchange(
					base.toString()+"/london-users/api/v1/users/"+i,
					HttpMethod.GET,
					null,
					User.class);
			// get the results from the 3rd party endpoint
			ResponseEntity<User> expectedUser = template.exchange(
					"https://bpdts-test-app.herokuapp.com/user/"+i,
					HttpMethod.GET,
					null,
					User.class);
			// assert that bodies are not empty and the users are the same
			assertAll(
					() -> assertNotNull(expectedUser.getBody()),
					() -> assertNotNull(user.getBody()),
					() -> assertEquals(expectedUser.getBody(), user.getBody())
			);
		});
	}

	@Test
	void nonExistingUserTest(){
		// get the results from `localhost` endpoint
		ResponseEntity<User> user = template.exchange(
				base.toString()+"/london-users/api/v1/users/"+1001,
				HttpMethod.GET,
				null,
				User.class);
		// get the results from the 3rd party endpoint
		ResponseEntity<User> expectedUser = template.exchange(
				"https://bpdts-test-app.herokuapp.com/user/"+1001,
				HttpMethod.GET,
				null,
				User.class);
		// assert that bodies are not empty and the users are the same
		assertAll(
				() -> assertNotNull(expectedUser.getBody()),
				() -> assertNotNull(user.getBody()),
				() -> assertEquals(expectedUser.getBody(), user.getBody())
		);
	}

	@ParameterizedTest
	@ValueSource(strings = {"London", "Athens"})
	void usersByCityTest(String city){
		// get the results from `localhost` endpoint
		ResponseEntity<User[]> users = template.exchange(
				base.toString()+"/london-users/api/v1/users/city/"+city,
				HttpMethod.GET,
				null,
				User[].class);
		// get the results from the 3rd API party
		ResponseEntity<User[]> expectedUsers = template.exchange(
				"https://bpdts-test-app.herokuapp.com/city/"+city+"/users",
				HttpMethod.GET,
				null,
				User[].class);
		// assert that both bodies are not null
		assertAll(
				() -> assertNotNull(expectedUsers.getBody()),
				() -> assertNotNull(users.getBody())
		);
		// assert that the bodies are of the same size and then compare the user 1 by 1
		assertAll(
				() -> assertEquals(expectedUsers.getBody().length, users.getBody().length),
				() -> IntStream.range(0, expectedUsers.getBody().length).forEach( i ->
							assertEquals(expectedUsers.getBody()[i], users.getBody()[i])
					   )
		);
	}

	@Test
	void usersByCityRadius0Test(){
		// === First Scenario get users with radius 0, which is only who live in London
		// get all the user from `localhost` endpoint
		ResponseEntity<User[]> users = template.exchange(
				base.toString()+"/london-users/api/v1/users/city-radius/London?radius=0",
				HttpMethod.GET,
				null,
				User[].class);
		// get the results from the 3rd API party
		ResponseEntity<User[]> expectedUsers = template.exchange(
				"https://bpdts-test-app.herokuapp.com/city/London/users",
				HttpMethod.GET,
				null,
				User[].class);
		// assert that both bodies are not null
		assertAll(
				() -> assertNotNull(expectedUsers.getBody()),
				() -> assertNotNull(users.getBody())
		);
		// assert that the bodies are of the same size and then compare the user 1 by 1
		assertAll(
				() -> assertEquals(expectedUsers.getBody().length, users.getBody().length),
				() -> IntStream.range(0, expectedUsers.getBody().length).forEach( i ->
							assertEquals(expectedUsers.getBody()[i], users.getBody()[i])
					  )

		);
	}

	@ParameterizedTest
	@MethodSource("userCountByLondonRadius")
	void usersByCityRadiusTest(int radius, int expectedCount){
		// === Second Scenario get users with different radius around London and validate their counts
		// get all the user from `localhost` endpoint
		ResponseEntity<User[]> users = template.exchange(
				base.toString()+"/london-users/api/v1/users/city-radius/London?radius="+radius,
				HttpMethod.GET,
				null,
				User[].class);

		assertAll(
				() -> assertNotNull(users.getBody()),
				() -> assertEquals(expectedCount, users.getBody().length)
		);
	}

	private static Stream<Arguments> userCountByLondonRadius() {
		return Stream.of(
				Arguments.of( 5, 6),
				Arguments.of( 10, 6),
				Arguments.of( 15, 7),
				Arguments.of( 25, 8),
				Arguments.of( 50, 9),
				Arguments.of( 500, 44),
				Arguments.of( 99999, 1000)
		);
	}

}

package se.fearless.spaceship.auth;

import se.fearless.service.HttpMethod;
import se.fearless.service.MicroService;
import se.fearless.service.Router;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

public class Auth {
	public static void main(String[] args) throws InterruptedException {
		final Set<String> userNames = new CopyOnWriteArraySet<>(Arrays.asList("hiflyer", "demazia"));

		Router router = new Router();
		router.addRoute(HttpMethod.GET, Pattern.compile("/basic/.*"), (request, response) -> {
			String userName = request.getPath().substring(1);
			if (userNames.contains(userName)) {
				response.writeString("SUCCESS");
			} else {
				response.writeString("FAIL");
			}
			return response.close();
		});

		MicroService microService = new MicroService.Builder(router, "spaceship", "auth").build();
		microService.start();
		microService.waitTillShutdown();
	}
}

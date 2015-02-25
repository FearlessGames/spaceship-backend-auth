package se.fearless.spaceship.auth;

import se.fearless.service.MicroService;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Auth {
	public static void main(String[] args) {
		final Set<String> userNames = new CopyOnWriteArraySet<>(Arrays.asList("hiflyer", "demazia"));

		MicroService microService = new MicroService(9999, (request, response) -> {
			String userName = request.getPath().substring(1);
			if (userNames.contains(userName)) {
				response.writeString("SUCCESS");
			} else {
				response.writeString("FAIL");
			}
			return response.close();

		});
		microService.start();
	}
}

package se.fearless.spaceship.auth;

import se.fearless.common.json.GsonSerializer;
import se.fearless.service.HttpMethod;
import se.fearless.service.MicroService;
import se.fearless.service.Router;

import java.util.regex.Pattern;

public class Auth {
	public static void main(String[] args) throws InterruptedException {


		Router router = new Router();
		GsonSerializer jsonSerializer = new GsonSerializer();
		router.addRoute(HttpMethod.GET, Pattern.compile("/basic/.*"), new BasicRequestHandler(jsonSerializer));

		router.addRoute(HttpMethod.GET, Pattern.compile("/fame/.*"), new FameRequestHandler("http://localhost:8080/", jsonSerializer));

		MicroService microService = new MicroService.Builder(router, "spaceship", "auth").build();
		microService.start();
		microService.waitTillShutdown();
	}

}

package se.fearless.spaceship.auth;

import com.netflix.eureka2.client.resolver.ServerResolver;
import com.netflix.eureka2.client.resolver.ServerResolvers;
import se.fearless.service.*;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;

public class Auth {
	public static void main(String[] args) throws InterruptedException {
		final Set<String> userNames = new CopyOnWriteArraySet<>(Arrays.asList("hiflyer", "demazia"));

		Router router = new Router();
		router.addRoute(HttpMethod.GET, Pattern.compile("/.*"), (request, response) -> {
			String userName = request.getPath().substring(1);
			if (userNames.contains(userName)) {
				response.writeString("SUCCESS");
			} else {
				response.writeString("FAIL");
			}
			return response.close();
		});
		EurekaServerInfo eurekaServerInfo = new EurekaServerInfo(ServerResolvers.from(new ServerResolver.Server("localhost", 2222)),
				ServerResolvers.from(new ServerResolver.Server("localhost", 2223)));
		MicroService microService = new MicroService(9999, router, "spaceship", "auth", eurekaServerInfo, new HostnameProvider());
		microService.start();
		microService.waitTillShutdown();
	}
}

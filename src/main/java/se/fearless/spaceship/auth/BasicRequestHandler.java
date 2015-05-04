package se.fearless.spaceship.auth;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rx.Observable;
import se.fearless.common.json.JsonSerializer;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BasicRequestHandler implements RequestHandler<ByteBuf, ByteBuf> {
	private final Set<String> userNames = new CopyOnWriteArraySet<>(Arrays.asList("hiflyer", "demazia"));
	private final JsonSerializer jsonSerializer;

	public BasicRequestHandler(JsonSerializer jsonSerializer) {
		this.jsonSerializer = jsonSerializer;
	}

	@Override
	public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
		String userName = request.getPath().substring(7);
		if (userNames.contains(userName)) {
			response.writeString(jsonSerializer.toJson(AuthResultDTO.success(userName)));
		} else {
			response.writeString(jsonSerializer.toJson(AuthResultDTO.fail()));
		}
		return response.close();
	}
}

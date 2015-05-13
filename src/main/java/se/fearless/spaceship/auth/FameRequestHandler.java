package se.fearless.spaceship.auth;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.AbstractHttpContentHolder;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import rx.Observable;
import se.fearless.common.json.JsonSerializer;

import java.nio.charset.Charset;
import java.util.Date;

public class FameRequestHandler implements RequestHandler<ByteBuf, ByteBuf> {
	private final JsonSerializer jsonSerializer;
	private final String server;

	public FameRequestHandler(String server, JsonSerializer jsonSerializer) {
		this.server = server;
		this.jsonSerializer = jsonSerializer;
	}

	@Override
	public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
		String fameToken = request.getPath().substring("/fame/".length());
		Observable<HttpClientResponse<ByteBuf>> httpGet = RxNetty.createHttpGet(server + "/api/private/users/spaceship/" + fameToken);

		Observable<ByteBuf> byteBufObservable = httpGet.flatMap(AbstractHttpContentHolder::getContent);
		Observable<String> stringObservable = byteBufObservable.map(byteBuf1 -> byteBuf1.toString(Charset.forName("UTF-8")));

		Observable<UserAccountDTO> userAccountDTOObservable = stringObservable.map(s -> jsonSerializer.fromJson(s, UserAccountDTO.class));

		Observable<Observable<Void>> observable = userAccountDTOObservable
				.map(userAccountDTO -> response.writeStringAndFlush(jsonSerializer.toJson(AuthResultDTO.success(userAccountDTO.username))));

		return observable.flatMap(voidObservable -> voidObservable);

	}

	private static class UserAccountDTO {
		private String username;
		private String email;
		private Date timeOfRegistration;
	}
}

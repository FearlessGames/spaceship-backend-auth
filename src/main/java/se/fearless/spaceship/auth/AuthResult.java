package se.fearless.spaceship.auth;

public class AuthResult {
	public static final AuthResult FAIL_RESULT = new AuthResult(false, null);
	boolean success;
	String userName;

	private AuthResult(boolean success, String userName) {
		this.success = success;
		this.userName = userName;
	}

	public static AuthResult success(String userName) {
		return new AuthResult(true, userName);
	}

	public static AuthResult fail() {
		return FAIL_RESULT;
	}
}

package api;

public class ApiClient {
    public final AuthApiClient login = new AuthApiClient();
    public final UsersApiClient users = new UsersApiClient();
}

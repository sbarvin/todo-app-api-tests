package todoapp.tests;

import todoapp.rest.client.RestClient;

public class TestBase {
    public final RestClient restClient = RestClient.api();
}

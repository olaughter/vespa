package ai.vespa.feed.client.impl;

import ai.vespa.feed.client.HttpResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPOutputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApacheClusterTest {

    @RegisterExtension
    final WireMockExtension server = new WireMockExtension();

    @Test
    void testClient() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        try (ApacheCluster cluster = new ApacheCluster(new FeedClientBuilderImpl(List.of(URI.create("http://localhost:" + server.port())))
                                                               .setGzipRequests(true))) {
            server.stubFor(any(anyUrl()))
                  .setResponse(okJson("{}").build());

            CompletableFuture<HttpResponse> vessel = new CompletableFuture<>();
            cluster.dispatch(new HttpRequest("POST",
                                             "/path",
                                             Map.of("name1", () -> "value1",
                                                    "name2", () -> "value2"),
                                             "content".getBytes(UTF_8),
                                             Duration.ofSeconds(1)),
                             vessel);
            HttpResponse response = vessel.get(5, TimeUnit.SECONDS);
            assertEquals("{}", new String(response.body(), UTF_8));
            assertEquals(200, response.code());

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (OutputStream zip = new GZIPOutputStream(buffer)) { zip.write("content".getBytes(UTF_8)); }
            server.verify(1, anyRequestedFor(anyUrl()));
            server.verify(1, postRequestedFor(urlEqualTo("/path")).withHeader("name1", equalTo("value1"))
                                                                  .withHeader("name2", equalTo("value2"))
                                                                  .withHeader("Content-Type", equalTo("application/json; charset=UTF-8"))
                                                                  .withHeader("Content-Encoding", equalTo("gzip"))
                                                                  .withRequestBody(equalTo("content")));
            server.resetRequests();
        }
    }

}

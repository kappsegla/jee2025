package org.fungover.jee2025.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import org.fungover.jee2025.business.BookService;
import org.fungover.jee2025.exceptions.mappers.NotFoundMapper;
import org.hamcrest.Matchers;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookRestEasyTest {
    @Mock
    BookService bookService;

    private BookResource bookResource;
    private UndertowJaxrsServer server;
    private int port;

    @BeforeEach
    public void setup() throws IOException {
        bookResource = new BookResource(bookService);

        // Find an available port
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }

        // Create and start the server
        server = new UndertowJaxrsServer();
        server.start(Undertow.builder().addHttpListener(port, "localhost"));

        // Create deployment
        ResteasyDeployment deployment = new ResteasyDeploymentImpl();

        // Register resources and providers
        deployment.getResources().add(bookResource);
        deployment.getProviders().add(new NotFoundMapper());

        // Deploy our application at /api
        DeploymentInfo deploymentInfo = server.undertowDeployment(deployment);
        deploymentInfo.setClassLoader(BookResourceTest.class.getClassLoader());
        deploymentInfo.setDeploymentName("REST API");
        deploymentInfo.setContextPath("/api");

        server.deploy(deploymentInfo);

        // Configure REST-Assured
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @AfterEach
    public void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void getAllBooks() {
        // Arrange
        Mockito.when(bookService.getAllBooks()).thenReturn(List.of());

        // Act & Assert
        RestAssured.given()
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("data", Matchers.empty());
    }
}

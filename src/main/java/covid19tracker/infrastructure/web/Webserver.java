package covid19tracker.infrastructure.web;

import java.sql.Connection;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;


public class Webserver {

    private final Connection connection;

    public Webserver(Connection connection) {
        this.connection = connection;
    }

    public void startJetty() throws Exception {
        final ContextHandler health = new ContextHandler("/health");
        final ContextHandler pingpong = new ContextHandler("/api/pingpong");

        health.setAllowNullPathInfo(true);
        pingpong.setAllowNullPathInfo(true);


        health.setHandler(new covid19tracker.infrastructure.web.HealthEndpoint());
        pingpong.setHandler(new covid19tracker.infrastructure.web.PingPongHandler());

        ContextHandlerCollection contexts = new ContextHandlerCollection(health, pingpong);

        String port = System.getenv("PORT");
        if (port == null) {
            port = "8080";
        }
        System.out.println("PORT: " + port);
        final Server server = new Server(Integer.parseInt(port));

        server.setHandler(contexts);
        server.start();
        server.join();
    }
}
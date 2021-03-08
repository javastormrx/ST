package st.catalog;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@SpringBootApplication
@EnableTransactionManagement
public class Catalogger
{
    private static Logger logger = LoggerFactory.getLogger(Catalogger.class);

    private Dispatcher dispatcher;

    @Value("${st.nats.url}")
    private String natsUrl;

    public static void main(String[] args)
    {
        SpringApplication.run(Catalogger.class, args);
    }

    @EventListener
    private void initializeDispatchers(ApplicationReadyEvent event)
    {
        Connection connection = null;

        try
        {
            logger.info(String.format("Connecting to NATS server \"%s\"", natsUrl));
            Nats.connect(natsUrl);
            logger.info(String.format("Connection to \"%s\" SUCCESSFUL", natsUrl));
        }
        catch (IOException | InterruptedException e)
        {
            logger.error(String.format("Connection to \"%s\" FAILED", natsUrl));
            e.printStackTrace();
            return;
        }

        logger.info("Creating dispatchers");

        dispatcher = connection.createDispatcher(new CataloggerMessageHandler());
        dispatcher.subscribe("executions");
        logger.info("Subscribed to \"executions\"");
        dispatcher.subscribe("sports");
        logger.info("Subscribed to \"sports\"");
    }
}

package st.catalog;

import io.nats.client.Message;
import io.nats.client.MessageHandler;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public class CataloggerMessageHandler implements MessageHandler
{
    private static Logger logger = LoggerFactory.getLogger(CataloggerMessageHandler.class);

    @Autowired
    private LocalSessionFactoryBean sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void onMessage(Message message) throws InterruptedException
    {
        Session session = sessionFactory.getObject().getCurrentSession();
        CatalogEntry entry = new CatalogEntry();

        entry.setMessageData(new String(message.getData()));
        entry.setReceived(new Date());

        session.save(entry);
    }
}

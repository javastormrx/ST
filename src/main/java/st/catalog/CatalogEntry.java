package st.catalog;

import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "CatalogEntry")
public class CatalogEntry
{
    private static Logger logger = LoggerFactory.getLogger(CatalogEntry.class);

    private static final int MAX_MESSAGE_LEN = 4096;

    @Id
    @Basic(optional = false)
    @GenericGenerator(name = "CatalogEntryGenerator", strategy = "increment")
    @GeneratedValue(generator = "CatalogEntryGenerator")
    @Column(name = "CatalaogEntryNo")
    private Long catalogEntryNo;

    @Column(name = "MessageData", length = MAX_MESSAGE_LEN)
    private String messageData;

    @Column(name = "Received")
    @Temporal(TemporalType.TIMESTAMP)
    private Date received;

    public Long getCatalogEntryNo()
    {
        return catalogEntryNo;
    }

    public Date getReceived()
    {
        return received;
    }

    public void setReceived(Date received)
    {
        this.received = received;
    }

    public String getMessageData()
    {
        return messageData;
    }

    public void setMessageData(String messageData)
    {
        if (messageData != null && messageData.length() > MAX_MESSAGE_LEN)
        {
            logger.warn(String.format("Received message longer than %d, truncating...", MAX_MESSAGE_LEN));
            messageData = messageData.substring(0, MAX_MESSAGE_LEN);
        }
        this.messageData = messageData;
    }
}

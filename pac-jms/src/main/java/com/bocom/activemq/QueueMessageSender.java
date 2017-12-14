package com.bocom.activemq;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Session;
import org.apache.activemq.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
@Component
public class QueueMessageSender {
    
    private static final Logger logger = LoggerFactory.getLogger(QueueMessageSender.class);
    
    @Resource(name="jmsQueueTemplate")
    private JmsTemplate jmsTemplate;
    /**
     * 向指定队列发送消息
     */
    public void sendMessage(String queueName, final String messgae) {
          try {
                   logger.info("发送MQ的Queue消息:{}",messgae);
                jmsTemplate.send(queueName, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return (Message) session.createTextMessage(messgae);
                    }
                });
            } catch (Exception e) {
                logger.error("发送MQ的Queue消息出错:{}", e);
            }
        }
 
    /**
    * 向默认队列发送消息
    */
    public void sendMessage(final String messgae) {
         try {
             logger.info("发送MQ的Queue消息:{}",messgae);
             jmsTemplate.send(new MessageCreator() {
                 @Override
                 public Message createMessage(Session session) throws JMSException {
                     return (Message) session.createTextMessage(messgae);
                 }
              });
         } catch (Exception e) {
             logger.error("发送MQ的Queue消息出错:{}", e);
         }
     }
}

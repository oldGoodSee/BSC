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

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TopicMessageSender {

	private static final Logger logger = LoggerFactory
			.getLogger(TopicMessageSender.class);

	@Resource(name = "jmsTopicTemplate")
	private JmsTemplate jmsTemplate;

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 向指定队列发送消息
	 */
	public void sendMessage(String topicName, final String messgae) {
		try {
			logger.info("发送MQ的Topic消息:{}", messgae);
			jmsTemplate.send(topicName, new MessageCreator() {
				@Override
				public Message createMessage(Session session)
						throws JMSException {
					return (Message) session.createTextMessage(messgae);
				}
			});
		} catch (Exception e) {
			logger.error("发送MQ的Topic消息出错:{}", e);
		}
	}

	/**
	 * 向默认队列发送消息
	 */
	public void sendMessage(final String messgae) {
		try {
			logger.info("发送MQ的Topic消息:{}", messgae);
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session)
						throws JMSException {
					return (Message) session.createTextMessage(messgae);
				}
			});
		} catch (Exception e) {
			logger.error("发送MQ的Topic消息出错:{}", e);
		}
	}

	/**
	 * 向指定队列发送消息
	 */
	public void sendMessage(String topicName, final Object data) {
		try {
			final String messgae = objectMapper.writeValueAsString(data);
			logger.info("发送MQ的Topic消息:{}", messgae);
			jmsTemplate.send(topicName, new MessageCreator() {
				@Override
				public Message createMessage(Session session)
						throws JMSException {
					return (Message) session.createTextMessage(messgae);
				}
			});
		} catch (Exception e) {
			logger.error("发送MQ的Topic消息出错:{}", e);
		}
	}

	/**
	 * 向默认队列发送消息
	 */
	public void sendMessage(final Object data) {
		try {
			final String messgae = objectMapper.writeValueAsString(data);
			logger.info("发送MQ的Topic消息:{}", messgae);
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session)
						throws JMSException {
					return (Message) session.createTextMessage(messgae);
				}
			});
		} catch (Exception e) {
			logger.error("发送MQ的Topic消息出错:{}", e);
		}
	}
}

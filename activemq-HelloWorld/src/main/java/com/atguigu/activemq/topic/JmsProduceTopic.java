package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

public class JmsProduceTopic {

    public static String ACTIVEMQ_USER = "admin";
    public static String ACTIVEMQ_PASSWORD = "admin";
    public static String ACTIVEMQ_BROKER_URL = "tcp://192.168.198.129:61616";
    public static String TOPIC_NAME = "topic_01";

    public static void main(String[] args) throws JMSException {

        //1 创建连接工场,使用默认用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USER, ACTIVEMQ_PASSWORD, ACTIVEMQ_BROKER_URL);

        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3 创建会话,此步骤有两个参数，第一个是否以事务的方式提交，第二个默认的签收方式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地，具体可以是队列 也可以是 主题  (与queue不一样的地方1：用session创建topic对象)
        //Destination destination = session.createTopic(TOPIC_NAME);
        Topic topic = session.createTopic(TOPIC_NAME);

        //======生产者不同的地方======
        //5.创建消息的生产者（此时mq控制台，会创建topic的队列）  (与queue不一样的地方2:传入Topic类型参数)
        MessageProducer messageProducer = session.createProducer(topic);

        //6.通过使用messageProducer 产生3条消息到队列里面
        for (int i = 0; i < 6; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("This is Topic-msg: " + i);//字符串消息
            //8.通过messageProducer发给mq（此时mq控制台的队列里面会多一条消息）
            messageProducer.send(textMessage);
        }

        //9.关闭消息
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("MQ-Topic-消息发送完成！");
    }
}

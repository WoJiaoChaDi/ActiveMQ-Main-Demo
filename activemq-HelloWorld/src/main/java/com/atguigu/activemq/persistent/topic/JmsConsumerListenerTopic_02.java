package com.atguigu.activemq.persistent.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import java.io.IOException;

public class JmsConsumerListenerTopic_02 {

    public static String ACTIVEMQ_USER = "admin";
    public static String ACTIVEMQ_PASSWORD = "admin";
    public static String ACTIVEMQ_BROKER_URL = "tcp://192.168.198.129:61616";
    public static String TOPIC_NAME = "topic_persistent";

    public static void main(String[] args) throws JMSException, IOException {

        System.out.println("订阅者_02");

        //1 创建连接工场,使用默认用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USER, ACTIVEMQ_PASSWORD, ACTIVEMQ_BROKER_URL);

        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        //topic订阅者的持久化，不能先把连接打开了
        //connection.start();
        //***设置订阅者id***
        connection.setClientID("sub_02");


        //3 创建会话,此步骤有两个参数，第一个是否以事务的方式提交，第二个默认的签收方式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地，具体可以是队列 也可以是 主题  (与queue不一样的地方1：用session创建topic的对象)
        //Destination destination = session.createTopic(TOPIC_NAME);
        Topic topic = session.createTopic(TOPIC_NAME);

        //***创建持久化订阅者***
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "remark....");

        //启动连接
        connection.start();

        Message message = topicSubscriber.receive();
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("*** sub_02 -> 收到的持久化topic***" + textMessage.getText());
            message = topicSubscriber.receive(1000L);
        }

        session.close();
        connection.close();
        System.out.println("***消费者接受消息完成！！！***");
    }
}

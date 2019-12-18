package com.atguigu.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsProduce {

    public static String ACTIVEMQ_USER = "admin";
    public static String ACTIVEMQ_PASSWORD = "admin";
    public static String ACTIVEMQ_BROKER_URL = "tcp://192.168.198.129:61616";
    public static String QUEUE_NAME = "queue_02";

    public static void main(String[] args) throws JMSException {

        //1 创建连接工场,使用默认用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USER, ACTIVEMQ_PASSWORD, ACTIVEMQ_BROKER_URL);

        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3 创建会话,此步骤有两个参数，第一个是否以事务的方式提交，第二个默认的签收方式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地，具体可以是队列 也可以是 主题
        //Destination destination = session.createQueue(QUEUE_NAME);
        Queue queue = session.createQueue(QUEUE_NAME);

        //5.创建消息的生产者（此时mq控制台，会创建queue01的队列）
        MessageProducer messageProducer = session.createProducer(queue);

        //6.通过使用messageProducer 产生3条消息到队列里面
        for (int i = 0; i < 2; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("This is msg: " + i);//字符串消息
            //8.通过messageProducer发给mq（此时mq控制台的队列里面会多一条消息）
            messageProducer.send(textMessage);
        }

        //9.关闭消息
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("MQ消息发送完成！");
    }
}

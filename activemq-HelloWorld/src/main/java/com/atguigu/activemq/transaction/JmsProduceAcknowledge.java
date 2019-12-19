package com.atguigu.activemq.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsProduceAcknowledge {

    public static String ACTIVEMQ_USER = "admin";
    public static String ACTIVEMQ_PASSWORD = "admin";
    public static String ACTIVEMQ_BROKER_URL = "tcp://192.168.198.129:61616";
    public static String QUEUE_NAME = "queue_acknoweledge";

    public static void main(String[] args) throws JMSException {

        //1 创建连接工场,使用默认用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USER, ACTIVEMQ_PASSWORD, ACTIVEMQ_BROKER_URL);

        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3 创建会话,此步骤有两个参数，
        // 第一个是否以事务的方式提交
        // 第二个默认的签收方式(默认是自动签收)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地，具体可以是队列 也可以是 主题
        //Destination destination = session.createQueue(QUEUE_NAME);
        Queue queue = session.createQueue(QUEUE_NAME);

        //======生产者不同的地方======
        //5.创建消息的生产者（此时mq控制台，会创建queue01的队列）
        MessageProducer messageProducer = session.createProducer(queue);

        //6.通过使用messageProducer 产生3条消息到队列里面
        for (int i = 0; i < 6; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("This is msg: " + i);//字符串消息

            //设置消息的额外参数，也可以在 messageProducer.send方法中进行设置
            //设置消息的目的地，可以重新设置目的地
            textMessage.setJMSDestination(queue);
            //设置消息的持久性
            textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
            //消息的过期时间（默认永不过期）
            textMessage.setJMSExpiration(1000L);
            //消息的优先级，0-9 十个级别， 0-4 普通消息  5-9 加急消息
            textMessage.setJMSPriority(9);
            //消息的id，唯一识别编号
            textMessage.setJMSMessageID("MsgId_" + i);

            //模拟异常
            //if(i == 5){
            //    i = i/0;
            //}

            //8.通过messageProducer发给mq（此时mq控制台的队列里面会多一条消息）  设置消息的额外参数，有多种重写的方法
            messageProducer.send(textMessage);
        }

        //9.关闭消息
        messageProducer.close();
        session.close();
        connection.close();


        System.out.println("MQ_queue_transaction消息发送完成！");
    }
}

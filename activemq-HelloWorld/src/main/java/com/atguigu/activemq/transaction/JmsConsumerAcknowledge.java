package com.atguigu.activemq.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JmsConsumerAcknowledge {

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
        // 第一个是否以事务的方式提交，
        // 第二个默认的签收方式(默认是自动签收)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地，具体可以是队列 也可以是 主题
        //Destination destination = session.createQueue(QUEUE_NAME);
        Queue queue = session.createQueue(QUEUE_NAME);

        //======消费者不同的地方======
        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /*
         * 同步堵塞方式( receive() )
         * 订阅者或接收者调用 MessageConsumer.receive()方法接收消息，receive方法在能够接收到消息之前（或超时之前）将一直阻塞。
         */
        while(true){
            //6.消费者获取一条消息（此时mq控制台里面会少一条消息）  receive()方法参数为空，则会一直等待
            //TextMessage textMessage = (TextMessage) messageConsumer.receive();
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L); //添加recive()添加参数，则4秒后自动关闭进程
            if(null != textMessage){
                System.out.println("***消费者收到消息***：" + textMessage.getText());
            }else{
                break;
            }
        }

        messageConsumer.close();
        session.close();
        connection.close();
        System.out.println("***queue_transaction_消费者接受消息完成！！！***");
    }
}

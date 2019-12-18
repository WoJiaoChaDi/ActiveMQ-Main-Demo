package com.atguigu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.io.IOException;

public class JmsConsumerListenerTopic_02 {

    public static String ACTIVEMQ_USER = "admin";
    public static String ACTIVEMQ_PASSWORD = "admin";
    public static String ACTIVEMQ_BROKER_URL = "tcp://192.168.198.129:61616";
    public static String TOPIC_NAME = "topic_01";

    public static void main(String[] args) throws JMSException, IOException {
        //1 创建连接工场,使用默认用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_USER, ACTIVEMQ_PASSWORD, ACTIVEMQ_BROKER_URL);

        //2 获得连接并启动
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3 创建会话,此步骤有两个参数，第一个是否以事务的方式提交，第二个默认的签收方式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地，具体可以是队列 也可以是 主题  (与queue不一样的地方1：用session创建topic的对象)
        //Destination destination = session.createTopic(TOPIC_NAME);
        Topic topic = session.createTopic(TOPIC_NAME);

        //======消费者不同的地方======
        //5.创建消费者  (与queue不一样的地方2：传入Topic类型参数)
        MessageConsumer messageConsumer = session.createConsumer(topic);

        /*
         * 用消息监听的方式
         */
        //messageConsumer.setMessageListener(new MessageListener() {
        //    @Override
        //    public void onMessage(Message message) {
        //        if(null != message && message instanceof  TextMessage){
        //            TextMessage textMessage = (TextMessage) message;
        //            try {
        //                System.out.println("***消费者-消息监听-收到消息***： " + textMessage.getText());
        //            } catch (JMSException e) {
        //                e.printStackTrace();
        //            }
        //        }
        //    }
        //});

        /*
         * 用消息监听的方式（lambda表达式）
         */
        messageConsumer.setMessageListener((message) -> {
            if(null != message && message instanceof  TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("***消费者-消息监听-收到消息***： " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //press any key to exit  保持进程不灭,给消费者连接一些时间，不然还没连上消费就把连接关了
        System.in.read();

        messageConsumer.close();
        session.close();
        connection.close();
        System.out.println("***消费者接受消息完成！！！***");
    }
}

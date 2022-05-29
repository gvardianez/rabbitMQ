package com.rabbitmq.IT_blog.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ITBlogConsumer {

    private static final String EXCHANGE_NAME = "blogExchanger";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        factory.setPort(8080);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

        consoleHandle(channel, queueName);
    }

    private static void consoleHandle(Channel channel, String queueName) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String choice, topic, command;
        while (true) {
            System.out.println("Для подписки на тему введите команду в формате 'set_topic тема подписки', отписаться от темы 'unsubscribe_topic тема подписки' для выхода нажмите q \n");
            choice = scanner.nextLine();
            if (choice.equals("q")) break;
            String[] messageArr = choice.split(" ", 2);
            command = messageArr[0];
            topic = messageArr[1];
            if (command.equals("set_topic")) {
                channel.queueBind(queueName, EXCHANGE_NAME, topic);
            }
            if (command.equals("unsubscribe_topic")) {
                channel.queueUnbind(queueName, EXCHANGE_NAME, topic);
            }
        }
    }
}




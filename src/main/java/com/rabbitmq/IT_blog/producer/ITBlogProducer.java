package com.rabbitmq.IT_blog.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ITBlogProducer {

    private static final String EXCHANGE_NAME = "blogExchanger";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(8080);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            consoleHandle(channel);
        }
    }

    private static void consoleHandle(Channel channel) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String choice, topic, message;
        while (true) {
            System.out.println("Введите сообщение в формате(тема_сообщение), для выхода нажмите q\n");
            choice = scanner.nextLine();
            if (choice.equals("q")) break;
            String[] messageArr = choice.split("_", 2);
            topic = messageArr[0];
            message = messageArr[1];
            System.out.println(" [x] Sent to " + topic + " '" + message + "'");
            channel.basicPublish(EXCHANGE_NAME, topic, null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}


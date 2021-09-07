package org.example.ataraxiawarmup.shop;

import org.bukkit.Bukkit;

import java.util.Random;

public class OrderRotator {

    public void rotate() {
        for (Order order : Order.getAllOrders()) {
            if (order.isInRotation()) {
                order.removeFromRotation();
            }
            for (int i = 0; i < 10; i++) {
                Random random = new Random();
                int randomNumber = random.nextInt(Order.getAllOrders().size());
                Bukkit.getPlayer("MexLr").sendMessage("random: " + randomNumber + " / " + Order.getAllOrders().size());
                Order.getOrder(randomNumber).addIntoRotation();
            }
        }
    }

}

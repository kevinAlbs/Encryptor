package gui;

import javax.swing.*;

public class GUI extends JFrame{
    public GUI(){

    }
    public static void main(String[] args){
        JFrame screen = new GUI();
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setSize(600,400);
        screen.setLocationRelativeTo(null);
        screen.setVisible(true);
        screen.setTitle("Password Encryptor");
    }
}
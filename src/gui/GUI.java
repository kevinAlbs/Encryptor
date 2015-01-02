package gui;


import encryption.FileEncryptor;
import encryption.NaiveEncryptor;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GUI extends JFrame{
    private Editor ed;
    //not using threads, so storing shared info in static class
    public static class Info{
        private static String key = "";
        private static File working_file = null;
        public static boolean keySet(){return key.length() > 0;}
        public static boolean workingFileSet(){return working_file != null;}
        public static String getKey(){
            return key;
        }
        public static void setKey(String s){
            if(s == null || s.length() == 0){
                throw new IllegalArgumentException("Key cannot be null or empty");
            }
            key = s;
        }
        public static File getWorkingFile(){
            return working_file;
        }
        public static void setWorkingFile(File f){
            if(f == null){
                throw new IllegalArgumentException("File cannot be null");
            }
            working_file = f;
        }
    }

    public GUI(){
        ed = new Editor(this);
        this.add(ed, BorderLayout.CENTER);
    }
    public void showKeyDialog(String message){
        showKeyDialog(message, false);
    }
    public void showKeyDialog(String message, boolean unencryptionOption){
        JPasswordField pw_key = new JPasswordField(30);
        if(Info.getKey() != ""){
            pw_key.setText(Info.getKey());
        } else {
            pw_key = new JPasswordField(30);
        }
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        panel.add(pw_key);
        Object[] options = {"Ok", "Cancel"};
        if(unencryptionOption){
            Object[] o = {"Ok", "File is not encrypted", "Cancel"};
            options = o;
        }
        int option = JOptionPane.showOptionDialog(this,panel, "Enter your key", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0);
        if(option == 0) { //Ok
            String k = new String(pw_key.getPassword());
            Info.setKey(k);
        }
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
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        public static void removeKey(){
            key = "";
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
        public static void removeWorkingFile(){
            working_file = null;
        }
    }

    public GUI(){
        ed = new Editor(this);
        this.add(ed, BorderLayout.CENTER);

        final String default_filepath = "encrypted_text.etx";
        File def = new File(default_filepath);
        if(def.exists()){
            if(showKeyDialog("Enter key to open default file \"" + default_filepath + "\"") && Info.keySet()){
                Info.setWorkingFile(def);
                ed.updateEditor();
            }
        }
    }
    public boolean showKeyDialog(String message){
        return showKeyDialog(message, false);
    }
    public boolean showKeyDialog(String message, boolean unencryptionOption){
        final JPasswordField pw_key = new JPasswordField(30);
        final JTextField txt_key = new JTextField(30);
        final JCheckBox ck_show = new JCheckBox("Show password");
        if(Info.getKey() != ""){
            pw_key.setText(Info.getKey());
            txt_key.setText(Info.getKey());
        }


        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(new JLabel(message));
        panel.add(ck_show);
        ck_show.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                if(ck_show.isSelected()){
                    txt_key.setVisible(true);
                    pw_key.setVisible(false);
                    txt_key.setText(new String(pw_key.getPassword()));
                } else {
                    txt_key.setVisible(false);
                    pw_key.setVisible(true);
                    pw_key.setText(txt_key.getText());
                }
                panel.revalidate();
                panel.repaint();
            }
        });
        panel.add(pw_key);
        panel.add(txt_key);

        txt_key.setVisible(false);

        Object[] options = {"Ok", "Cancel"};
        if(unencryptionOption){
            Object[] o = {"Ok", "File is not encrypted", "Cancel"};
            options = o;
        }
        int option = JOptionPane.showOptionDialog(this,panel, "Enter your key", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0);

        if(option == 0) { //Ok for both
            if(ck_show.isSelected()) {
                Info.setKey(txt_key.getText());
            }
            else {
                Info.setKey(new String(pw_key.getPassword()));
            }
        }

        if(unencryptionOption){
            if(option == 1) {
                //not encrypted, remove current key
                Info.removeKey();
            } else if (option == 2){
                //cancel
                return false;
            }
        } else if(option == 1){
            return false;
        }

        return true;
    }

    public static void main(String[] args){
        JFrame screen = new GUI();
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setSize(600, 400);
        screen.setLocationRelativeTo(null);
        screen.setVisible(true);
        screen.setTitle("Password Encryptor");

    }
}
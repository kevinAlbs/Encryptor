package gui;

import encryption.FileEncryptor;
import encryption.NaiveEncryptor;
import encryption.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class Editor extends JPanel implements ActionListener {
    private JButton save, saveAs, open;
    private JTextField filename;
    private JEditorPane pane;
    private GUI parent;
    private JFileChooser fc;
    public Editor(GUI parent) {
        //initialize components
        this.parent = parent;
        save = new JButton("Save");
        saveAs = new JButton("Save As");
        open = new JButton("Open");
        pane = new JEditorPane();
        filename = new JTextField(30);
        filename.setEditable(false);
        fc = new JFileChooser();

        //set listeners
        open.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);

        //add to layout
        this.setLayout(new BorderLayout());
        JPanel southP = new JPanel();
        southP.setLayout(new FlowLayout());
        southP.add(open, BorderLayout.SOUTH);
        southP.add(filename);
        southP.add(save, BorderLayout.SOUTH);
        southP.add(saveAs, BorderLayout.SOUTH);
        JScrollPane jsp = new JScrollPane(pane);
        this.add(jsp, BorderLayout.CENTER);
        this.add(southP, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e){
        Object src = e.getSource();
        if(src == open){
            if(showWorkingFileDialog()){
                File f = GUI.Info.getWorkingFile();
                parent.showKeyDialog("Open file with encryption key:", true);
                if(!GUI.Info.keySet()){
                    //do not decrypt
                    pane.setText(Utilities.readFile(f));
                } else {
                    pane.setText(FileEncryptor.read(f, new NaiveEncryptor(), GUI.Info.getKey()));
                }


                filename.setText(f.getName());
                GUI.Info.setWorkingFile(f);
            }
        } else if(src  == save){
            save();
        } else if(src == saveAs){
            if(showWorkingFileDialog()){
                parent.showKeyDialog("Save file with encryption key:");
                if(!GUI.Info.keySet()){
                    return;//cannot save
                }
                FileEncryptor.write(pane.getText(), GUI.Info.getWorkingFile(), new NaiveEncryptor(), GUI.Info.getKey());
            }
        }
    }

    //returns true if the user actually selects a file, false if the user cancels
    public boolean showWorkingFileDialog(){
        fc.showOpenDialog(parent);
        File f = fc.getSelectedFile();
        if(f != null) {
            filename.setText(f.getName());
            GUI.Info.setWorkingFile(f);
            return true;
        }
        return false;
    }

    public void save(){
        parent.showKeyDialog("Save file with the following key:");
        if(!GUI.Info.workingFileSet()){
            showWorkingFileDialog();
        }
        if(!GUI.Info.keySet() || !GUI.Info.workingFileSet()){
            System.out.println("Save cancelled");
            return;
        }
        FileEncryptor.write(pane.getText(), GUI.Info.getWorkingFile(), new NaiveEncryptor(), GUI.Info.getKey());
    }

}

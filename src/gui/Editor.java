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
    public static enum CHOOSER_TYPE {OPEN, SAVE};
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
            if(showWorkingFileDialog(CHOOSER_TYPE.OPEN)){
                File f = GUI.Info.getWorkingFile();
                if(!parent.showKeyDialog("Open file with encryption key:", true)) {
                    //user hit cancel
                    return;
                }
                GUI.Info.setWorkingFile(f);
                updateEditor();
            }
        } else if(src  == save){
            save();
        } else if(src == saveAs){
            if(showWorkingFileDialog(CHOOSER_TYPE.SAVE)){
                if(!parent.showKeyDialog("Save file with encryption key:") || !GUI.Info.keySet()){
                    return;//cannot save
                }
                FileEncryptor.write(pane.getText(), GUI.Info.getWorkingFile(), new NaiveEncryptor(), GUI.Info.getKey());
            }
        }
    }

    //returns true if the user actually selects a file, false if the user cancels
    public boolean showWorkingFileDialog(CHOOSER_TYPE c){
        if(c == CHOOSER_TYPE.OPEN){
            fc.setDialogTitle("Select a file to edit");
            fc.showOpenDialog(parent);
        } else if(c == CHOOSER_TYPE.SAVE){
            fc.setDialogTitle("Choose a destination");
            fc.showSaveDialog(parent);
        }

        File f = fc.getSelectedFile();
        if(f != null) {
            filename.setText(f.getName());
            GUI.Info.setWorkingFile(f);
            return true;
        }
        return false;
    }

    public void save(){
        if(!GUI.Info.workingFileSet()){
            showWorkingFileDialog(CHOOSER_TYPE.SAVE);
        }
        if(!parent.showKeyDialog("Save file with the following key:")){
            return;
        }
        if(!GUI.Info.keySet() || !GUI.Info.workingFileSet()){
            System.out.println("Save cancelled");
            return;
        }
        FileEncryptor.write(pane.getText(), GUI.Info.getWorkingFile(), new NaiveEncryptor(), GUI.Info.getKey());
    }

    public void updateEditor(){
        File f = GUI.Info.getWorkingFile();
        filename.setText(f.getName());
        if(!GUI.Info.keySet()){
            //do not decrypt
            pane.setText(Utilities.readFile(f));
        } else {
            pane.setText(FileEncryptor.read(f, new NaiveEncryptor(), GUI.Info.getKey()));
        }
    }
}

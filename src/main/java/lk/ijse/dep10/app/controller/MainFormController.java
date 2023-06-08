package lk.ijse.dep10.app.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainFormController {

    public Label lblProgress;
    @FXML
    private Button btnCopy;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnDestination;

    @FXML
    private Button btnMove;

    @FXML
    private Button btnSelect;

    @FXML
    private ProgressBar pgrBar;

    @FXML
    private ProgressIndicator pgrIndicator;

    @FXML
    private TextField txtDestination;

    @FXML
    private TextField txtSource;
    private File fileSource ;
    private File fileDestination ;
    double write = 0.0;
    int size=0;


    @FXML
    void btnSelectOnAction(ActionEvent event) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jFileChooser.showOpenDialog(null);
        fileSource= jFileChooser.getSelectedFile();
        btnEnable();
        if (fileSource==null)return;
        txtSource.setText(fileSource.getAbsolutePath());
        size(fileSource);
        System.out.println(size);



    }

    @FXML
    void btnDestinationOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        fileDestination = directoryChooser.showDialog(txtSource.getScene().getWindow());
        if (fileSource.getParentFile().equals(fileDestination)) new Alert(Alert.AlertType.INFORMATION,"You Selected the Same Directory. Choose else where").show();
        btnEnable();
        if (fileDestination == null) return;
        txtDestination.setText(fileDestination.getAbsolutePath());
    }

    private void btnEnable() {

        btnDelete.setDisable(fileSource==null);
        btnCopy.setDisable(fileSource==null||fileDestination==null||fileSource.getParentFile().equals(fileDestination));
        btnMove.setDisable(fileSource==null||fileDestination==null||fileSource.getParentFile().equals(fileDestination));

    }

    @FXML
    void btnCopyOnAction(ActionEvent event) {
        File theDir = new File(fileDestination.getAbsolutePath(),fileSource.getName());
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        copyFiles(fileSource,theDir);
        new Alert(Alert.AlertType.INFORMATION, "File Copied").show();


    }

    @FXML
    void btnMoveOnAction(ActionEvent event) {
        btnCopy.fire();
        btnDelete.fire();
        new Alert(Alert.AlertType.INFORMATION, "File Moved").show();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        delete(fileSource);
        fileSource.delete();
        new Alert(Alert.AlertType.INFORMATION, "File Deleted").show();
    }

    private void delete (File files){
        for (File file : files.listFiles()) {
            if (file.isDirectory()) {
                delete(file);
                file.delete();
            }if (file.isFile())  file.delete();
        }

    }
    private int size (File files){
        for (File file : files.listFiles()) {
            if (file.isDirectory()) {
                size(file);
                size+=file.length();
            }else size+=file.length();
        }
        return size;

    }


    private void copyFiles (File files,File parent) {
        for (File file : files.listFiles()) {
            if (file.isDirectory()) {
                File theDir = new File(parent.getAbsolutePath(), file.getName());
                theDir.mkdirs();
                copyFiles(file, theDir);

            }
            if (file.isFile()) {

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        FileInputStream fileInputStream = new FileInputStream(file);
                        File newDir = new File(parent, file.getName());
                        FileOutputStream fileOutputStream = new FileOutputStream(newDir);
                        while (true) {
                            byte[] buffer = new byte[1024];
                            int read = fileInputStream.read(buffer);
                            if (read == -1) break;
                            fileOutputStream.write(buffer, 0, read);
                            write+=read;
                            pgrBar.setProgress(write/size);
                            System.out.println(pgrBar.getProgress());
                            updateMessage(String.format("%2.2f",pgrBar.getProgress()*100) +"% Complete");

                        }
                        fileInputStream.close();
                        fileOutputStream.close();
                        return null;
                    }

                };
                new Thread(task).start();
                lblProgress.textProperty().bind(task.messageProperty());


            }


        }
    }

}

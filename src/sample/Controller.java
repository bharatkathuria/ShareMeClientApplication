package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.io.IOException;

public class Controller {

    static String userName="";
    private static UDPClientToConnectServer udpClient;
    private static TCPMessageClient tcpClient;
    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private TextArea targetArea;

    public void setData(String data) {
        targetArea.setText(data);
    }


    @FXML
    public void handleSubmitButtonAction(javafx.event.ActionEvent actionEvent) throws IOException {

        Window reference = submitButton.getScene().getWindow();

        if (userNameField.getText().isEmpty()) {

            AlertHelper.showAlert(Alert.AlertType.ERROR, reference, "Form Error!",
                    "Please enter valid username");
        }

        if (passwordField.getText().isEmpty()) {

            AlertHelper.showAlert(Alert.AlertType.ERROR, reference, "Form Error!",
                    "Please enter password");
        }
        if (!passwordField.getText().isEmpty() && !userNameField.getText().isEmpty()) {

            userName=userNameField.getText();
            Stage stage = (Stage) reference;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChattingWindowGUI.fxml"));
            Parent root = loader.load();
            stage.setResizable(false);
            stage.setScene(new Scene(root, 820, 600));
            stage.setTitle("ShareMeClient - Message and File Sharing "+userName);

            Controller controller = loader.<Controller>getController();
            udpClient = new UDPClientToConnectServer();
            udpClient.startUDPClientService(stage);

            tcpClient=new TCPMessageClient(userName,udpClient.getServerIP());
            tcpClient.startTCPClientService(stage,controller);


        }
    }


    @FXML
    private TextArea incomingMessageTextArea;

    @FXML
    private TextArea outgoingMessageTextArea;

    @FXML
    private ListView listViewClient;

    @FXML
    private ListView listViewFiles;

    @FXML
    private Button sendButton;

    @FXML
    private Button attachButton;




    public void updateClient(String clientList){

        while (!clientList.equals("")) {
            listViewClient.getItems().add(clientList.substring(0, clientList.indexOf("+")));
            clientList = clientList.substring(clientList.indexOf("+") + 1);
        }
    }
    public  void addClient(String clientName){
        listViewClient.getItems().add(clientName);
    }

    public  void removeClient(String clientName){
        listViewClient.getItems().remove(clientName);
    }

    public void addMessageToIncomingList(String message){

        String messageString = incomingMessageTextArea.getText()+"\n"+message;
        incomingMessageTextArea.setText(messageString);
    }



    @FXML
    public void handleSendButtonAction(javafx.event.ActionEvent actionEvent) {

        if(outgoingMessageTextArea.getText().equals(""))
            return;
        String stringMessage =  userName+ " says:" + "\n\n" + outgoingMessageTextArea.getText();
        outgoingMessageTextArea.setText("");

        tcpClient.sendMessage(stringMessage);

    }



}

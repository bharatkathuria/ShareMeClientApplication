package sample;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPMessageClient {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String userName="";
    private String serverIp="";


    public TCPMessageClient(String userName,String serverIp){
        this.userName=userName;
        this.serverIp=serverIp;
    }

    public void startTCPClientService(Stage stage, Controller c){

        System.out.println("Starting tcp connection");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Please Wait");
        alert.setHeaderText(null);
        alert.setContentText("Connecting To Server");
        alert.initOwner(stage);

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                alert.show();
            }
        });

        try{
            socket =new Socket(serverIp,2222);
            dataInputStream=new DataInputStream(socket.getInputStream());
            dataOutputStream=new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                   AlertHelper.showAlert(Alert.AlertType.ERROR, stage, "Error!",
                            "FATAL ERROR.....");
                }
            });
            System.exit(0);

        }
        try{
            dataOutputStream.writeUTF(userName);
        } catch (IOException e) {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, stage, "Error!",
                            "FATAL ERROR.....");

                }
            });
            System.exit(0);
        }

        String userNamesOfActiveClients="";
        try{
            userNamesOfActiveClients = dataInputStream.readUTF();
        }
        catch (IOException e) {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, stage, "Error!",
                            "FATAL ERROR.....");
                }
            });
            System.exit(0);
        }
        final String clientListString = userNamesOfActiveClients;
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    c.updateClient(clientListString);
                }
            });
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                alert.close();
            }
        });

        System.out.println("connection established");

        Thread t = new Thread(new Runnable() {
            String message;
            @Override
            public void run() {
                while (true){

                    try{
                        System.out.println("reading message");

                        message = dataInputStream.readUTF();
                    }catch (IOException e){
                        System.out.println("normal client"+e);
                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                AlertHelper.showAlert(Alert.AlertType.ERROR, stage, "Error!",
                                        "FATAL ERROR.....");
                            }
                        });
                        System.exit(0);

                    }

                    if(message.trim().startsWith("INSERT@")){
                        final String clientName = message.substring(message.indexOf("@")+1);

                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                c.addClient(clientName);
                            }
                        });
                    }
                    else if(message.trim().startsWith("REMOVE@")){
                        final String clientName = message.substring(message.indexOf("@")+1);
                        Platform.runLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                c.removeClient(clientName);
                            }
                        });

                    }
                    else{

                        final String sendingMessage = message;

                        c.addMessageToIncomingList(sendingMessage);
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void sendMessage(String message){
        try{
            System.out.println("messge sended");
            dataOutputStream.writeUTF(message);
        }catch (IOException e){

        }

    }
}


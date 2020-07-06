package sample;

import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class UDPClientToConnectServer {


    private DatagramSocket udpClientSocket=null;
    String serverIPAddress;

    public void startUDPClientService(Stage stage){

        try{
            System.out.println("Starting UDP Client service to send ip address and connect to server broadcast service");
            udpClientSocket = new DatagramSocket();
            byte[] clientByteString="CLIENT_HERE".getBytes();
            DatagramPacket sendingPacket  = new DatagramPacket(clientByteString,clientByteString.length, InetAddress.getByName("255.255.255.255"),1234);
            udpClientSocket.send(sendingPacket);

            sendingPacket  = new DatagramPacket(clientByteString,clientByteString.length, InetAddress.getByName("255.255.255.255"),1234);
            udpClientSocket.send(sendingPacket);

            Enumeration<NetworkInterface> networkInterfaces  = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()){

                NetworkInterface nInterface  = networkInterfaces.nextElement();
                for(InterfaceAddress interfaceAddress : nInterface.getInterfaceAddresses()){

                    InetAddress brodcastAddress =interfaceAddress.getBroadcast();
                    if(brodcastAddress==null)
                        continue;

                    sendingPacket  = new DatagramPacket(clientByteString,clientByteString.length, InetAddress.getByName("255.255.255.255"),1234);
                    udpClientSocket.send(sendingPacket);

                    sendingPacket  = new DatagramPacket(clientByteString,clientByteString.length, InetAddress.getByName("255.255.255.255"),1234);
                    udpClientSocket.send(sendingPacket);
                }
            }

            while (true){

                byte[] recieveBuffer = new byte[100000];
                DatagramPacket recievingPacket =new DatagramPacket(recieveBuffer,recieveBuffer.length);
                System.out.println("reciecivnnnnr here...");
                udpClientSocket.receive(recievingPacket);
                System.out.println("Server here.bnnnnnnnnnn..");

                String recievedString = new String(recievingPacket.getData(),0,recievingPacket.getLength());
                recievedString = recievedString.trim();

                if(recievedString.equals("SERVER_HERE")){
                    System.out.println("Server here...");
                    serverIPAddress =recievingPacket.getAddress().getHostAddress();
                    System.out.println(serverIPAddress);
                    break;
                }

            }
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }


    }

    public String getServerIP(){
        return serverIPAddress;
    }


}

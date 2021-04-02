/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import dao.sinhVienDAO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import model.SinhVien;

/**
 *
 * @author ADMIN
 */
public class testServer {
   private int serverPort = 8888;
   private DatagramSocket myServer;
   private DatagramPacket receivePacket;
   private sinhVienDAO svDAO;

    public testServer() {
        svDAO = new sinhVienDAO();
        openConnection();
        while (true) {            
            listening();
        }
    }

    private void openConnection() {
        try {
            myServer = new DatagramSocket(serverPort);
        } catch (Exception e) {
        }
    }

    private void listening() {
        Object obj = receiveData();
        
    }
   
    private void sendData(Object obj){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            InetAddress IpAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            byte[] send = IpAddress.getAddress();
            DatagramPacket sendPacket = new DatagramPacket(send,send.length, IpAddress, clientPort);
            myServer.send(sendPacket);
        } catch (IOException e) {
        }
    }
    private Object receiveData(){
        Object obj = null;
        try {
            byte[] receive = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receive,receive.length);
            myServer.receive(receivePacket);
            ByteArrayInputStream bais = new ByteArrayInputStream(receive);
            ObjectInputStream ois = new ObjectInputStream(bais);
            ois.readObject();
        } catch (Exception e) {
        }
        return obj;
    }
}

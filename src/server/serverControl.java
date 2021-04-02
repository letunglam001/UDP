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
import java.net.SocketException;
import model.SinhVien;



/**
 *
 * @author ADMIN
 */
public class serverControl {
    private sinhVienDAO svDAO;
    private DatagramSocket serverSocket;
    private int port = 8888;
    private DatagramPacket receivePacket;

    public serverControl() {
        svDAO = new sinhVienDAO();
        openServer();
        while (true) {            
            listening();
        }
    }

    private void openServer() {
        try {
            serverSocket = new DatagramSocket(port);
        } catch (SocketException e) {
        }
    }

    private void listening() {
        Object obj = receiveData();
        if(obj instanceof String){
            String request = String.valueOf(obj);
            if(request.equals("getList")){
                sendData(svDAO.getAllSinhVien());
            }
            if(request.equals("getByName")){
                Object obj1 = receiveData();
                String name = String.valueOf(obj1);
                sendData(svDAO.searchByName(name));
            }
            if(request.equals("getById")){
                Object obj1 = receiveData();
                int ma = Integer.parseInt(String.valueOf(obj1));
                sendData(svDAO.getByID(ma));
            }
        }
        if(obj instanceof SinhVien){
            SinhVien sv = (SinhVien) obj;
            if(svDAO.updateSinhVien(sv)){
                sendData("updateSuccess");
            }else{
                sendData("updateFailed");
            }
        }
    }

    private Object receiveData() {
        Object obj = null;
        try {
            byte[] receiveData = new byte[1024];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
        }
        return obj;  
    }
    private void sendData(Object obj){
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            InetAddress ipAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, clientPort);
            serverSocket.send(sendPacket);
        } catch (IOException e) {
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import javax.swing.JOptionPane;
import model.SinhVien;


/**
 *
 * @author ADMIN
 */
public class clientControl {
    private SinhVienFrm svf;
    private UpdateFrm uf;
    private int serverPort=8888;
    private int clientPort=9999;
    private String serverHost="localhost";
    private DatagramSocket myClient;

    public clientControl(SinhVienFrm svf) {
        this.svf = svf;
        this.svf.addSearchListener(new SearchListener());
        this.svf.addUpdateListener(new PreUpdateListener());
        openConnection();
        sendData("getList");
        System.out.println("Get List");
        List<SinhVien> sv = (List<SinhVien>) receiveData();
        svf.showAll(sv);
    }

    
    clientControl(){
        
    }
    public void openConnection(){
        try {
            myClient = new DatagramSocket(clientPort);
            System.out.println("Connect ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void closeConnection(){
        try {
            myClient.close();
        } catch (Exception e) {
        }
    }
    public void sendData(Object obj){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            InetAddress IPAddress = InetAddress.getByName(serverHost);
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IPAddress, serverPort);
            myClient.send(sendPacket);
        } catch (IOException e) {
        }
    }
    public Object receiveData(){
        Object obj = null;
        ObjectInputStream ois =null;
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            myClient.receive(receivePacket);
            ByteArrayInputStream bis = new ByteArrayInputStream(receiveData);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
        }
        return obj;
    }


    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = svf.getSearch();
            sendData("getByName");
            sendData(name);
            List<SinhVien> sv =(List<SinhVien>) receiveData();
            svf.showAll(sv);
        }
    }

    class PreUpdateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = e.getActionCommand();
            sendData("getById");
            sendData(id);
            SinhVien sv = (SinhVien) receiveData();
            uf = new UpdateFrm();
            uf.setVisible(true);
            uf.addUpdateListener(new UpdateListener());
            uf.setSV(sv);
        }
    }
    
    class UpdateListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            SinhVien sv = uf.getSVData();
            sendData(sv);
            String response = (String)receiveData();
            if(response.equals("updateSuccess")){
                uf.dispose();
                JOptionPane.showMessageDialog(svf, response);
                sendData("getList");
                List<SinhVien> sv1 = (List<SinhVien>) receiveData();
                svf.showAll(sv1);
            }
            else if(response.equals("updateFailed")){
                JOptionPane.showMessageDialog(uf, response);
            }
        } 
    }
}

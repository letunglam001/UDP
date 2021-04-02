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
import java.net.SocketException;
import java.util.List;
import javax.swing.JOptionPane;
import model.SinhVien;



/**
 *
 * @author ADMIN
 */
public class testControl {

    private SinhVienFrm svf;
    private UpdateFrm uf;
    private int serverPort = 8888;
    private int clientPort = 9999;
    private String serverHost = "localhost";
    private DatagramSocket myClient;

    public testControl(SinhVienFrm svf) {
        this.svf = svf;
        this.svf.addSearchListener(new SearchListener());
        this.svf.addUpdateListener(new PreUpdateListener());
        openConnection();
        sendData("getList");
        List<SinhVien> sv = (List<SinhVien>) receiveData();
        svf.showAll(sv);
    }

    private void openConnection() {
        try {
            myClient = new DatagramSocket(clientPort);
        } catch (SocketException e) {
        }
    }

    private void sendData(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            InetAddress IpAddress = InetAddress.getByName(serverHost);
            byte[] send = IpAddress.getAddress();
            DatagramPacket sendPacket = new DatagramPacket(send,send.length, IpAddress, serverPort);
            myClient.send(sendPacket);
        } catch (IOException e) {
        }
    }

    private Object receiveData(){
        Object obj = null;
        ObjectInputStream ois = null;
        try {
            byte[] receive = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receive,receive.length);
            myClient.receive(receivePacket);
            ByteArrayInputStream bais = new ByteArrayInputStream(receive);
            ois = new ObjectInputStream(bais);
            ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
        }
        return obj;
    }

    private class UpdateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SinhVien sv = uf.getSVData();
            String response = (String) receiveData();
            if(response.equals("updateSuccess")){
                uf.dispose();
                JOptionPane.showMessageDialog(svf, response);
                sendData("getList");
                List<SinhVien> s = (List<SinhVien>) receiveData();
                svf.showAll(s);
            }
            if(response.equals("updateFailed")){
                JOptionPane.showMessageDialog(uf, response);
            }
        }
    }
    private class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = svf.getSearch();
            sendData("getByName");
            sendData(name);
            List<SinhVien> sv = (List<SinhVien>) receiveData();
            svf.showAll(sv);
        }

       
    }

    private class PreUpdateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String id = e.getActionCommand();
            sendData("getById");
            sendData(Integer.parseInt(id));
            SinhVien sv = (SinhVien) receiveData();
            uf = new UpdateFrm();
            uf.setVisible(true);
            uf.setSV(sv);
            uf.addUpdateListener(new UpdateListener());
        }
    }
    
    

}

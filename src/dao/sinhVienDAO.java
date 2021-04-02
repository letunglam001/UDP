/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.SinhVien;

/**
 *
 * @author ADMIN
 */
public class sinhVienDAO {
    private Connection con;
    String url="jdbc:mysql://localhost:3306/sinhvien";
    String username="root";
    String password="letunglam";
    String driver="com.mysql.cj.jdbc.Driver";

    public sinhVienDAO() {
        con = this.getConnection();
    }

    
    public Connection getConnection(){
        con = null;
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
        }catch(ClassNotFoundException | SQLException e){
        }
        return con;
    }
    public List<SinhVien> getAllSinhVien(){
        String query="select *from student";
        List<SinhVien> sv = new ArrayList<SinhVien>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                SinhVien s = new SinhVien();
                s.setId(rs.getInt("ma"));
                s.setTen(rs.getString("ten"));
                s.setDob(rs.getString("dob"));
                s.setKhoa(rs.getString("khoa"));
                s.setQue(rs.getString("quequan"));
                sv.add(s);
            }
        } catch (SQLException e) {
        }
        return sv;
    }
    public List<SinhVien> searchByName(String ten){
        String query="select *from student where ten like ?";
        List<SinhVien> sv = new ArrayList<SinhVien>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            pst = con.prepareStatement(query);
            pst.setString(1,"%"+ ten +"%");
            rs = pst.executeQuery();
            while(rs.next()){
                SinhVien s = new SinhVien();
                s.setId(rs.getInt("ma"));
                s.setTen(rs.getString("ten"));
                s.setDob(rs.getString("dob"));
                s.setKhoa(rs.getString("khoa"));
                s.setQue(rs.getString("quequan"));
                sv.add(s);
            }
        } catch (SQLException e) {
        }
        return sv;
    }
    public SinhVien getByID(int ma){
        String query="select *from student where ma=?";
        SinhVien sv = new SinhVien();
        PreparedStatement pst;
        ResultSet rs;
        try {
            pst = con.prepareStatement(query);
            pst.setInt(1,ma);
            rs = pst.executeQuery();
            if(rs.next()){
                sv.setId(rs.getInt("ma"));
                sv.setTen(rs.getString("ten"));
                sv.setDob(rs.getString("dob"));
                sv.setKhoa(rs.getString("khoa"));
                sv.setQue(rs.getString("quequan"));
            }
        } catch (SQLException e) {
        }
        return sv;
    }
    public boolean updateSinhVien(SinhVien s){
        boolean a = false;
        String query="Update student set ten=?,dob=?,khoa=?,quequan=? where ma=?";
        PreparedStatement pst;
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, s.getTen());
            pst.setString(2, s.getDob());
            pst.setString(3, s.getKhoa());
            pst.setString(4, s.getQue());
            pst.setInt(5,s.getId());
            if(pst.executeUpdate()>0){
                a=true;
            }
        } catch (SQLException e) {
        }
        return a;
    }
}

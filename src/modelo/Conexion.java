/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class Conexion {
// Variables Globales para la conexion
    private final String DB = "db_ventas";
    private final String URL="jdbc:mysql://localhost:3306/"+DB;
    private final String USER="root";
    private final String PASS="";
    
    Connection con;
    
    // Creacion del Constructor de la Clase
    public Conexion() {
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public Connection getConexion(){
        return con;
    }
    
    public void cerrarConexion() {
        if (con != null) {
            try {
                con.close();
                
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
               
            }
        }
    }
  

}

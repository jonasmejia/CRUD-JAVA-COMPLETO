package modelo;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOCategoria {

    Conexion oC;
    Connection con;
    ResultSet rs;
    CallableStatement cst;

    public DAOCategoria() {
        this.oC = new Conexion();
        this.con = oC.getConexion();
    }

    public ArrayList<DTOCategoria> ListaCategoria() throws SQLException {
        ArrayList<DTOCategoria> lista = new ArrayList<>();
        DTOCategoria categoria;
        
        try {
            String sql = "{CALL get_categoria()}";
            cst = con.prepareCall(sql);
            // Establecer el parámetro de entrada
            //cst.setInt(1, buscar);
            rs = cst.executeQuery();

            // Pasamos los datos del resulset a participante (DTOCategoria)
            while (rs.next()) {
                categoria = new DTOCategoria(); // instanciamos la clase clsParticipante (objeto categoria)
                categoria.setId(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));
                lista.add(categoria);
            }
        } catch (SQLException e) {
            System.out.println("Error" + e);
        }
        return lista;
    }

    //Registrar
    public boolean grabar(DTOCategoria oCA) {
        boolean estado = false;
        try {
            String sql = "{CALL insert_categoria(?)}";
            cst = con.prepareCall(sql);
            
            cst.setString(1, oCA.getNombre());
            
            int resp = cst.executeUpdate();
            
            estado = resp > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DAOCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return estado;
    }
    
    //para actualizar
    public boolean actualizar(DTOCategoria oCA) throws SQLException {
        boolean estado = false;
        try {            
            String sql = "{CALL update_categoria(? , ?)}";

            cst = con.prepareCall(sql);
            cst.setInt(1, oCA.getId());
            cst.setString(2, oCA.getNombre());
            
            int resp = cst.executeUpdate();
            estado = resp > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DAOMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
        return estado;
    }
}

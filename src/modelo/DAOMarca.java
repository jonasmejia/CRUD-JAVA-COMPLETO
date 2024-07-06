package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class DAOMarca {

    Conexion oC;
    Statement st;
    Connection con;
    ResultSet rs;
    //Paso 1
    PreparedStatement pstm;

    public DAOMarca() {
        oC = new Conexion();
        con = oC.getConexion();
    }

    //para registrar
    public boolean grabar(DTOMarca oM) {
        boolean estado = false;
        try {
            //Verificar:
            //st = con.createStatement();
            //Paso 2
            String sqlV = "SELECT COUNT(*) FROM marca WHERE nombre = ? ;";
            //paso 3
            pstm = con.prepareStatement(sqlV);
            //paso 4
            pstm.setString(1, oM.getNombre());
            //ResultSet rs = st.executeQuery(sqlV);
            //PASO 5
            ResultSet rs = pstm.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("El registro ya existe.");
                JOptionPane.showMessageDialog(null, "El registro " + oM.getNombre() + "ya existe.");
            } else {
                //st = con.createStatement();
                //Paso 2
                String sql = "INSERT INTO marca(nombre) VALUES(?)";
                //Paso 3
                pstm = con.prepareStatement(sql);
                //Paso 4
                pstm.setString(1, oM.getNombre());
                
                int resp = pstm.executeUpdate();
                estado = resp > 0;

            }

        } catch (SQLException ex) {
            Logger.getLogger(DAOMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
        return estado;
    }

    //para actualizar
    public boolean actualizarMarca(DTOMarca oM) throws SQLException {
        boolean estado = false;
        try {
            st = con.createStatement();
            String sql = "UPDATE marca SET nombre='" + oM.getNombre() + "' WHERE id_marca='" + oM.getIdMarca() + "'";
            int resp = st.executeUpdate(sql);
            estado = resp > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DAOMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
        return estado;
    }

    //Para eliminar
    public boolean eliminarMarca(DTOMarca oM) throws SQLException {
        boolean estado = false;
        try {
            st = con.createStatement();
            String sql = "DELETE FROM marca WHERE id_marca='" + oM.getIdMarca() + "'";
            int resp = st.executeUpdate(sql);
            estado = resp > 0;

        } catch (SQLException ex) {
            Logger.getLogger(DAOMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
        return estado;
    }

    //Listar registros
    public ArrayList<DTOMarca> ListaMarcas(String buscar) throws SQLException {
        ArrayList<DTOMarca> lista = new ArrayList<>();
        DTOMarca marca;

        try {
            st = con.createStatement();

            String sql = "SELECT * FROM marca WHERE nombre LIKE '%" + buscar + "%';";
            rs = st.executeQuery(sql);
            // Pasamos los datos del resulset a participante (clsParticipante)
            while (rs.next()) {
                marca = new DTOMarca(); // instanciamos la clase clsParticipante (objeto participante)
                marca.setIdMarca(rs.getInt("id_marca"));
                marca.setNombre(rs.getString("nombre"));
                lista.add(marca);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return lista;
    }
    
}

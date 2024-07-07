package modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JOptionPane;

public class DAOProducto {

    Conexion oC;
    PreparedStatement pst;
    Connection con;
    ResultSet rs;

    //Constantes
    private static final String GET_ID_MARCA = "SELECT id_marca as id FROM marca WHERE nombre = ?";
    private static final String GET_ID_CATEGORIA = "SELECT id_categoria as id FROM categoria WHERE nombre = ?";

    public DAOProducto() {
        oC = new Conexion();
        con = oC.getConexion();
    }

    //listar productos
    public ArrayList<DTOProducto> listarProductos() throws SQLException {
        ArrayList<DTOProducto> lista = new ArrayList<>();
        DTOProducto producto;
        try {

            String sql = "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, p.stock, m.nombre nombre_marca, c.nombre nombre_categoria\n"
                    + "FROM producto p\n"
                    + "INNER JOIN categoria c ON c.id_categoria = p.id_categoria\n"
                    + "INNER JOIN marca m ON m.id_marca = p.id_marca";
            pst = con.prepareStatement(sql);

            rs = pst.executeQuery(sql);

            while (rs.next()) {
                producto = new DTOProducto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getInt("stock"));
                producto.setNombre_marca(rs.getString("nombre_marca"));
                producto.setNombre_categoria(rs.getString("nombre_categoria"));

                lista.add(producto);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lista;
    }

    //listar de marcas para el combobox
    public ArrayList<DTOMarca> listaMarca() throws SQLException {
        ArrayList<DTOMarca> lista = new ArrayList<>();
        DTOMarca marca;

        try {
            String sql = "SELECT nombre FROM marca";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
           
            while (rs.next()) {
                marca = new DTOMarca();
                marca.setNombre(rs.getString("nombre").toUpperCase());
                lista.add(marca);
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
        return lista;
    }

    //listar de categorias para el comobobox
    public ArrayList<DTOCategoria> listaCategoria() throws SQLException {
        ArrayList<DTOCategoria> lista = new ArrayList<>();
        DTOCategoria categoria;

        try {
            String sql = "SELECT nombre FROM categoria";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery(sql);
  
            while (rs.next()) {
                categoria = new DTOCategoria();
                categoria.setNombre(rs.getString("nombre"));
                lista.add(categoria);
            }
        } catch (SQLException e) {

        }
        return lista;
    }

    //Capturar el id de categoria y marca
    private int obtenerIdPorNombre(String nombre, String query) throws SQLException {
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, nombre);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return -1;
    }

    //Validamos que los campos del producto no esten vacios
    public boolean validarProducto(DTOProducto producto, String nombreMarca, String nombreCategoria) {
        // Verificar que los campos obligatorios no estén vacíos
        if (Objects.isNull(producto)
                || producto.getNombre() == null || producto.getNombre().isEmpty()
                || producto.getDescripcion() == null || producto.getDescripcion().isEmpty()
                || nombreMarca == null || nombreMarca.isEmpty()
                || nombreCategoria == null || nombreCategoria.isEmpty()) {
            return false;
        }

        // Puedes agregar más validaciones según tus requisitos, por ejemplo, validar rangos, tipos de datos, etc.
        return true;
    }

    //Registrar
    public boolean grabar(DTOProducto producto, String nombreMarca, String nombreCategoria) throws SQLException {
        boolean estado = false;
        if (validarProducto(producto, nombreMarca, nombreCategoria)) {
            int idMarca = obtenerIdPorNombre(nombreMarca, GET_ID_MARCA);
            int idCategoria = obtenerIdPorNombre(nombreCategoria, GET_ID_CATEGORIA);
            //System.out.println("marca: "+nombreMarca+"_"+idMarca+"-"+"categoria: "+nombreCategoria+"_"+idCategoria);
            try {
                String sql = "INSERT INTO producto (`nombre`, `descripcion`, `precio`,`stock`, `id_marca`,`id_categoria`) VALUES (?, ?, ?, ?, ?, ?);";
                pst = con.prepareStatement(sql);
                pst.setString(1, producto.getNombre());
                pst.setString(2, producto.getDescripcion());
                pst.setDouble(3, producto.getPrecio());
                pst.setDouble(4, producto.getStock());
                pst.setInt(5, idMarca);
                pst.setInt(6, idCategoria);

                int resp = pst.executeUpdate();

                estado = resp > 0; //true = 1 o false = 0

            } catch (SQLException e) {
                System.out.println("" + e);
            }

        }else{
            JOptionPane.showMessageDialog(null, "Faltan Datos");
        }

        return estado;
    }
    
    //Actualizar
    public boolean actualizar(DTOProducto producto, String nombreMarca, String nombreCategoria) throws SQLException {
        boolean estado = false;
        if (validarProducto(producto, nombreMarca, nombreCategoria)) {
            int idMarca = obtenerIdPorNombre(nombreMarca, GET_ID_MARCA);
            int idCategoria = obtenerIdPorNombre(nombreCategoria, GET_ID_CATEGORIA);
            //System.out.println("marca: "+nombreMarca+"_"+idMarca+"-"+"categoria: "+nombreCategoria+"_"+idCategoria);
            try {
                String sql = "UPDATE producto "
                        + "SET `nombre` = ?, "
                        + "`descripcion` = ?, "
                        + "`precio` = ?, "
                        + "`stock`= ?, "
                        + "`id_marca` = ?, "
                        + "`id_categoria` = ? "
                        + "WHERE id_producto = ?;"; 
                
                pst = con.prepareStatement(sql);
                pst.setString(1, producto.getNombre());
                pst.setString(2, producto.getDescripcion());
                pst.setDouble(3, producto.getPrecio());
                pst.setInt(4, producto.getStock());
                pst.setInt(5, idMarca);
                pst.setInt(6, idCategoria);
                pst.setInt(7, producto.getIdProducto());

                int resp = pst.executeUpdate();

                estado = resp > 0; //true = 1 o false = 0

            } catch (SQLException e) {
                System.out.println("" + e);
            }

        }else{
            JOptionPane.showMessageDialog(null, "Faltan Datos");
        }

        return estado;
    }
    
    //Eliminar
    public boolean eliminar(DTOProducto producto) throws SQLException {
        boolean estado = false;
        try {
          
            String sql = "DELETE FROM producto WHERE id_producto = ?;";
            pst=con.prepareStatement(sql);
            pst.setInt(1, producto.getIdProducto());
            
            int resp = pst.executeUpdate();
            estado = resp > 0;
        } catch (SQLException ex) {
            System.out.println("Error: "+ex);
        }
        return estado;
    }
}

package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.DAOProducto;
import modelo.DTOCategoria;
import modelo.DTOMarca;
import modelo.DTOProducto;
import vista.FrmProducto;

/**
 *
 * @author JONAS
 */
public class CtrlProducto implements ActionListener {

    private DTOProducto mod;
    private DAOProducto dao;
    private FrmProducto frm;

    DefaultTableModel modeloTabla;
    DefaultComboBoxModel modeloComboM;
    DefaultComboBoxModel modeloComboC;

    //CONSTRUCTOR
    public CtrlProducto(DTOProducto mmod, DAOProducto mdao, FrmProducto frm) {
        this.mod = mmod;
        this.dao = mdao;
        this.frm = frm;

        this.frm.btnRegistrar.addActionListener(this);
        this.frm.btnActualizar.addActionListener(this);
        this.frm.mnEditar.addActionListener(this);
        this.frm.mnEliminar.addActionListener(this);
    }

    //INICIO
    public void inicio() throws SQLException {
        frm.setTitle("Módulo Productos");
        frm.setLocationRelativeTo(null);
        cargarTabla(frm.tblProducto);
        frm.btnActualizar.setEnabled(false);
        cargarMarca();
        cargarCategoria();
    }

    //CARGA TABLA PRODUCTO
    public void cargarTabla(JTable tabla) throws SQLException {
        modeloTabla = (DefaultTableModel) tabla.getModel();

        ArrayList<DTOProducto> lista = new ArrayList<>();
        try {
            lista = dao.listarProductos();
            Object[] objeto = new Object[7];
            for (int i = 0; i < lista.size(); i++) {

                objeto[0] = lista.get(i).getIdProducto();
                objeto[1] = lista.get(i).getNombre();
                objeto[2] = lista.get(i).getDescripcion();
                objeto[3] = lista.get(i).getPrecio();
                objeto[4] = lista.get(i).getStock();
                objeto[5] = lista.get(i).getNombre_marca().toUpperCase();
                objeto[6] = lista.get(i).getNombre_categoria();

                modeloTabla.addRow(objeto);
            }
            //Pasanos el modelo tabla temporal a la tabla del formulario
            frm.tblProducto.setModel(modeloTabla);
            //Reportamos el total de productos de la tabla productos
            frm.lblTotalProductos.setText(String.valueOf(modeloTabla.getRowCount()));
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    //CARGA COMBO MARCA
    public void cargarMarca() {
        modeloComboM = (DefaultComboBoxModel) frm.cboMarca.getModel();
        ArrayList<DTOMarca> lista = new ArrayList<>();

        try {
            lista = dao.listaMarca();
            for (int i = 0; i < lista.size(); i++) {
                modeloComboM.addElement(lista.get(i).getNombre());
            }
            frm.cboMarca.setModel(modeloComboM);
        } catch (Exception e) {
        }
    }

    //MARCA - Selecciona el elemento del combo de acuerdo a la selección de edición
    public void seleccionaMarca(String nombre) {
        //Obtenemos los datos ya cargados del objeto comboCategoria lo asignamos al modelo temporal modeloComboC
        modeloComboM = (DefaultComboBoxModel) frm.cboMarca.getModel();

        //Recorremos el modeloTemporal
        for (int i = 0; i < modeloComboM.getSize(); i++) {
            //Evaluamos si el valor de esa posición del combo es igual al nombre de la categoria obtenido de la tabla para editar
            if (modeloComboM.getElementAt(i).equals(nombre)) {
                //Si coinciden decir al modelo temporal del combo que ese elemento del combo debe ser seleccionado
                modeloComboM.setSelectedItem(nombre);
            }
            //System.out.println("" + modeloComboC.getElementAt(i));

        }
        //Por ultimo pasamos el combo temporal modeloComboc al combo del formulario cboCategoria
        frm.cboMarca.setModel(modeloComboM);

    }

    //CARGA COMBO CATEGORIA
    public void cargarCategoria() {
        modeloComboC = (DefaultComboBoxModel) frm.cboCategoria.getModel();
        ArrayList<DTOCategoria> lista = new ArrayList<>();

        try {
            lista = dao.listaCategoria();
            for (int i = 0; i < lista.size(); i++) {
                modeloComboC.addElement(lista.get(i).getNombre());
            }
            frm.cboCategoria.setModel(modeloComboC);
        } catch (Exception e) {
        }
    }

    //SELECCIONA CATEGORIA - Selecciona el elemento del combo de acuerdo a la selección de edición
    public void seleccionaCategoria(String nombre) {
        //Obtenemos los datos ya cargados del objeto comboCategoria lo asignamos al modelo temporal modeloComboC
        modeloComboC = (DefaultComboBoxModel) frm.cboCategoria.getModel();

        //Recorremos el modeloTemporal
        for (int i = 0; i < modeloComboC.getSize(); i++) {
            //Evaluamos si el valor de esa posición del combo es igual al nombre de la categoria obtenido de la tabla para editar
            if (modeloComboC.getElementAt(i).equals(nombre)) {
                //Si coinciden decir al modelo temporal del combo que ese elemento del combo debe ser seleccionado
                modeloComboC.setSelectedItem(nombre);
            }
            //System.out.println("" + modeloComboC.getElementAt(i));

        }
        //Por ultimo pasamos el combo temporal modeloComboc al combo del formulario cboCategoria
        frm.cboCategoria.setModel(modeloComboC);

    }

    //REGISTRAR
    public void metodoRegistrar() {
        String nombre = frm.txtNombre.getText().toUpperCase().trim();
        String descripcion = frm.txtADescripcion.getText().trim();
        String precioStr = frm.txtPrecio.getText().trim();
        String stockStr = frm.txtStock.getText().trim();

        String nombreMarca = String.valueOf(frm.cboMarca.getSelectedItem()).trim();
        String nombreCategoria = String.valueOf(frm.cboCategoria.getSelectedItem()).trim();

        // Validar campos obligatorios
        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || nombreMarca.equals("Seleccionar") || nombreCategoria.equals("Seleccionar")) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }
        mod.setNombre(nombre);
        mod.setDescripcion(descripcion);
        mod.setPrecio(Double.parseDouble(precioStr));
        mod.setStock(Integer.parseInt(stockStr));

        try {

            if (dao.grabar(mod, nombreMarca, nombreCategoria)) {
                JOptionPane.showMessageDialog(null, "Producto registrado correctamente");

                limpiar();

            } else {
                JOptionPane.showMessageDialog(null, "Error no se registro el producto");
            }
        } catch (SQLException e) {

        }
    }

    //SELECCIONAR PARA EDITAR
    public void metodoEditar() {
        int fila = frm.tblProducto.getSelectedRow(); // devuelve la posicion de la fila seleccionada cuando hago clic en el jtable
        // si devuelve -1 significa que no dio clic a ningun elemento del jtable
        if (fila != -1) {
            // obtenemos los datos del jtable y los pasamos a variables locales
            String id = String.valueOf(frm.tblProducto.getValueAt(fila, 0)); //devuelve el codigo del jtable

            String nombre = String.valueOf(frm.tblProducto.getValueAt(fila, 1));
            String descripcion = String.valueOf(frm.tblProducto.getValueAt(fila, 2));
            String precio = String.valueOf(frm.tblProducto.getValueAt(fila, 3));
            String stock = String.valueOf(frm.tblProducto.getValueAt(fila, 4));
            String nombreMarca = String.valueOf(frm.tblProducto.getValueAt(fila, 5));
            String nombreCategoria = String.valueOf(frm.tblProducto.getValueAt(fila, 6));

            // asignamos los datos de las variables locales a los controles jtextfield
            frm.txtId.setText(id);
            frm.txtNombre.setText(nombre);
            frm.txtADescripcion.setText(descripcion);
            frm.txtPrecio.setText(precio);
            frm.txtStock.setText(stock);
            seleccionaMarca(nombreMarca);
            seleccionaCategoria(nombreCategoria);
            frm.btnRegistrar.setEnabled(false);
            frm.btnActualizar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un registro de la tabla para editar.");
        }
    }

    //ACTUALIZAR
    public void metodoActualizar() {

        String id = frm.txtId.getText().toUpperCase().trim();
        String nombre = frm.txtNombre.getText().toUpperCase().trim();
        String descripcion = frm.txtADescripcion.getText().trim();
        String precioStr = frm.txtPrecio.getText().trim();
        String stockStr = frm.txtStock.getText().trim();

        String nombreMarca = String.valueOf(frm.cboMarca.getSelectedItem()).trim();
        String nombreCategoria = String.valueOf(frm.cboCategoria.getSelectedItem()).trim();

        // Validar campos obligatorios
        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || nombreMarca.equals("Seleccionar") || nombreCategoria.equals("Seleccionar")) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }
        mod.setIdProducto(Integer.parseInt(id));
        mod.setNombre(nombre);
        mod.setDescripcion(descripcion);
        mod.setPrecio(Double.parseDouble(precioStr));
        mod.setStock(Integer.parseInt(stockStr));

        try {
            if (dao.actualizar(mod, nombreMarca, nombreCategoria)) { // la actualizacion se realizó (true)
                JOptionPane.showMessageDialog(null, "Actualización de Datos Correcta", "Actualizar", JOptionPane.INFORMATION_MESSAGE);
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "No se realizó la actualización", "Actualizar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            Logger.getLogger(CtrlMarca.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    //ELIMINAR
    public void metodoEliminar() {
        int fila = frm.tblProducto.getSelectedRow(); // devuelve la posicion de la fila seleccionada cuando hago clic en el jtable
        // si devuelve -1 significa que no dio clic a ningun elemento del jtable
        if (fila != -1) {
            //Confirmamos la eliminación
            int confirmar = JOptionPane.showConfirmDialog(null, "¿Deseas ELIMINAR el registro?", "Eliminar", JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {
                // obtenemos los datos del jtable y los pasamos a variables locales
                mod.setIdProducto(Integer.parseInt(String.valueOf(frm.tblProducto.getValueAt(fila, 0)))); //devuelve el codigo del jtable

                try {
                    if (dao.eliminar(mod)) { // la actualizacion se realizó (true)
                        JOptionPane.showMessageDialog(null, "Registro eliminado correctamente", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                        limpiar();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se realizó la eliminación", "Eliminado", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    Logger.getLogger(CtrlMarca.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un registro de la tabla para ELIMINAR.");
        }

    }

    //ACTIONPERFORMED
    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if (e.getSource() == frm.btnRegistrar) {
            metodoRegistrar();
        }
        if (e.getSource() == frm.btnActualizar) {
            metodoActualizar();
        }
        if (e.getSource() == frm.mnEditar) {
            metodoEditar();
        }
        if (e.getSource() == frm.mnEliminar) {
            metodoEliminar();
        }
    }

    //LIMPIAR
    public void limpiar() throws SQLException {
        frm.txtId.setText("");
        frm.txtNombre.setText("");
        frm.txtADescripcion.setText("");
        frm.txtPrecio.setText("");
        frm.txtStock.setText("");
        frm.cboMarca.setSelectedIndex(0);
        frm.cboCategoria.setSelectedIndex(0);
        frm.txtNombre.requestFocus();
        frm.btnRegistrar.setEnabled(true);
        frm.btnActualizar.setEnabled(false);

        modeloTabla.setRowCount(0); // establecer las filas en 0
        cargarTabla(frm.tblProducto);
    }
}

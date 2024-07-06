package controlador;

import javax.swing.table.DefaultTableModel;
import modelo.DAOMarca;
import modelo.DTOMarca;
import vista.FrmMarca;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class CtrlMarca implements ActionListener, KeyListener {

    private DTOMarca mod;
    private DAOMarca dao;
    private FrmMarca frm;
    DefaultTableModel modelo; // para asignar al JTable (tbParticipante)

    public CtrlMarca(DTOMarca mmod, DAOMarca mdao, FrmMarca mfrm) {
        this.mod = mmod;
        this.dao = mdao;
        this.frm = mfrm;

        this.frm.btnGrabar.addActionListener(this);
        this.frm.menuEditar.addActionListener(this);
        this.frm.btnActualizar.addActionListener(this);
        this.frm.btnEliminar.addActionListener(this);
        this.frm.menuEliminar.addActionListener(this);
        
        this.frm.txtBuscar.addKeyListener(this);
    }

    public void inicio() throws SQLException {
        frm.setTitle("Módulo Marcas"); // titulo al formulario
        frm.setLocationRelativeTo(null); // al centro
        cargaTabla(frm.tblMarca, "");
    }

    public void cargaTabla(JTable tabla, String dato) throws SQLException {
        // la estructura del JTable se la paso a variable modelo
        if(dato == ""){
             modelo = (DefaultTableModel) tabla.getModel();
        }else{
            String[] titulo = {"id","nombre"};
             modelo = new DefaultTableModel(titulo, 0);
        }
   
       
        ArrayList<DTOMarca> lista = new ArrayList<>(); // variable que recibe datos del metodo selectPart...(DaoParticipante)
        String buscar = dato; // primer parametro del metodo selectParticipante
        //ArrayList<String> datos = new ArrayList<>(); // segundo parametro del metodo selectParticipante
        /* Para cargar todos los datos del metodo selectParticipante a la lista, debemos tomar en cuenta el valor de los
        parametros para que entre en la opcion DEFAULT, esto significa que el parametro filtro debe ser "" y  que el
        parametro datos sea un ArrayList<> vacio */

        try {
            lista = dao.ListaMarcas(buscar);
            Object[] objeto = new Object[2]; // numero de columnas del jtable
            for (int i = 0; i < lista.size(); i++) { // lista.size devuelve el numero de filas
                objeto[0] = lista.get(i).getIdMarca();
                objeto[1] = lista.get(i).getNombre(); // nombre

                modelo.addRow(objeto);
            }
            frm.tblMarca.setModel(modelo);
        } catch (SQLException e) {
            Logger.getLogger(CtrlMarca.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == frm.btnGrabar) {
            metodoRegistrar();
        }

        if (e.getSource() == frm.menuEditar) {
            metodoEditar();
        }

        if (e.getSource() == frm.btnActualizar) {
            metodoActualizar();
        }

        if (e.getSource() == frm.btnEliminar) {
            metodoEliminar();
        }
        
        if (e.getSource() == frm.menuEliminar) {
            metodoEliminar();
        }

    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == frm.txtBuscar) {
            try {
                cargaTabla(frm.tblMarca,frm.txtBuscar.getText());
            } catch (SQLException ex) {
                Logger.getLogger(CtrlMarca.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public void metodoRegistrar() {
        mod.setNombre(frm.txtNombre.getText().toUpperCase().trim());
        try {
            if (dao.grabar(mod)) { // se asume que es true, osea si se realizó el registro
                JOptionPane.showMessageDialog(null, "Registro Correcto", "Registrar", JOptionPane.INFORMATION_MESSAGE);
                limpiar();
            } else {
                JOptionPane.showMessageDialog(null, "No se realizó el registro", "Registrar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            Logger.getLogger(CtrlMarca.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void metodoEditar() {
        int fila = frm.tblMarca.getSelectedRow(); // devuelve la posicion de la fila seleccionada cuando hago clic en el jtable
        // si devuelve -1 significa que no dio clic a ningun elemento del jtable
        if (fila != -1) {
            // obtenemos los datos del jtable y los pasamos a variables locales
            int id = Integer.parseInt(String.valueOf(frm.tblMarca.getValueAt(fila, 0))); //devuelve el codigo del jtable
            String nombre = (String) frm.tblMarca.getValueAt(fila, 1);

            // asignamos los datos de las variables locales a los controles jtextfield
            frm.txtID.setText(String.valueOf(id));
            frm.txtNombre.setText(nombre);
        }
    }

    public void metodoActualizar() {
        if (frm.txtID.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la lista y presionar Editar", "Actualizar", JOptionPane.WARNING_MESSAGE);
        } else {
            mod.setIdMarca(Integer.parseInt(frm.txtID.getText()));
            mod.setNombre(frm.txtNombre.getText());

            try {
                if (dao.actualizarMarca(mod)) { // la actualizacion se realizó (true)
                    JOptionPane.showMessageDialog(null, "Actualización de Datos Correcta", "Actualizar", JOptionPane.INFORMATION_MESSAGE);
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "No se realizó la actualización", "Actualizar", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                Logger.getLogger(CtrlMarca.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void metodoEliminar() {
        int fila = frm.tblMarca.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la lista y presionar Editar", "Eliminar", JOptionPane.WARNING_MESSAGE);
        } else {
            mod.setIdMarca(Integer.parseInt(String.valueOf(frm.tblMarca.getValueAt(fila, 0))));
            try {
                if (dao.eliminarMarca(mod)) { // la actualizacion se realizó (true)
                    JOptionPane.showMessageDialog(null, "Eliminación de Datos Correcta", "Eliminar", JOptionPane.INFORMATION_MESSAGE);
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "No se realizó la eliminación", "Eliminar", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                Logger.getLogger(CtrlMarca.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void limpiar() throws SQLException {

        frm.txtID.setText("");
        frm.txtNombre.setText("");
        modelo.setRowCount(0); // establecer las filas en 0
        cargaTabla(frm.tblMarca, "");
//        frm.txtApellidos.setText("");
//        frm.txtDni.setText("");
//        frm.txtCelular.setText("");
//        frm.txtEmail.setText("");
//        frm.txtCarrera.setText("");
//        frm.txtNombres.requestFocus();
//        modelo.setRowCount(0); // establecer las filas en 0
//        cargaTable(frm.tbParticipante);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

   
}

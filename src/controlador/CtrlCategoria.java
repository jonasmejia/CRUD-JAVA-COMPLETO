package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.DAOCategoria;
import modelo.DTOCategoria;
import vista.FrmCategoria;

public class CtrlCategoria implements ActionListener{

    private DTOCategoria mod;
    private DAOCategoria dao;
    private FrmCategoria frm;
    
    private int idCategoria;

    DefaultTableModel modeloTabla; // para asignar al JTable (tblCategoria)

    public CtrlCategoria(DTOCategoria mod, DAOCategoria dao, FrmCategoria frm) {
        this.mod = mod;
        this.dao = dao;
        this.frm = frm;
       
        this.frm.btnRegistrar.addActionListener(this);
        //mnEditar Item de menu del popupMenu para editar
        this.frm.mnEditar.addActionListener(this);
        //btnActualizar boton para actualizar el registro
        this.frm.btnActualizar.addActionListener(this);
        
    }

    public void inicio() throws SQLException {
        frm.setTitle("Módulo Categoria"); // titulo al formulario
        frm.setLocationRelativeTo(null); // al centro
        cargaTabla(frm.tblCategoria);
    }

    public void cargaTabla(JTable tabla) throws SQLException {

        String[] titulo = {"id", "nombre"};
        modeloTabla = new DefaultTableModel(titulo, 0);

        ArrayList<DTOCategoria> lista = new ArrayList<>(); // variable que recibe datos del metodo selectPart...(DAOCategoria)

        try {
            lista = dao.ListaCategoria();
            Object[] objeto = new Object[2]; // numero de columnas del jtable
            for (int i = 0; i < lista.size(); i++) { // lista.size devuelve el numero de filas
                objeto[0] = lista.get(i).getId();
                objeto[1] = lista.get(i).getNombre(); // nombre

                modeloTabla.addRow(objeto);
            }
            frm.tblCategoria.setModel(modeloTabla);
        } catch (SQLException e) {
            Logger.getLogger(CtrlMarca.class.getName()).log(Level.SEVERE, null, e);
        }
    }
     
     public void limpiar() throws SQLException {
        frm.txtNombre.setText("");
        frm.txtNombre.requestFocus();
        modeloTabla.setRowCount(0); // establecer las filas en 0
        cargaTabla(frm.tblCategoria);
        idCategoria = 0;
     }
     
     public void metodoRegistrar() {
        mod.setNombre(frm.txtNombre.getText().toUpperCase());
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
        int fila = frm.tblCategoria.getSelectedRow(); // devuelve la posicion de la fila seleccionada cuando hago clic en el jtable
        // si devuelve -1 significa que no dio clic a ningun elemento del jtable
        if (fila != -1) {
            // obtenemos los datos del jtable y los pasamos a variables locales
            idCategoria = Integer.parseInt(String.valueOf(frm.tblCategoria.getValueAt(fila, 0))); //devuelve el codigo del jtable
            String nombre = (String) frm.tblCategoria.getValueAt(fila, 1);

            // asignamos los datos de las variables locales a los controles jtextfield
            frm.txtNombre.setText(nombre);
            System.out.println("idCategoria: "+idCategoria);
        }
    }
     
     public void metodoActualizar() {
        if (idCategoria == 0) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento de la lista y presionar Editar", "Actualizar", JOptionPane.WARNING_MESSAGE);
        } else {
            mod.setId(idCategoria);
            mod.setNombre(frm.txtNombre.getText());

            try {
                if (dao.actualizar(mod)) { // la actualizacion se realizó (true)
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == frm.btnRegistrar) {
            metodoRegistrar();
        }
        
        //Método para seleccionar el registro a editar
        if (e.getSource() == frm.mnEditar) {
            metodoEditar();
        }
        //Método para actualizar el registro
        if (e.getSource() == frm.btnActualizar) {
            metodoActualizar();
        }
    }
}









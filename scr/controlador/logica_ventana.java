package controlador;

import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import vista.ventana;
import modelo.*;

/**
 * Controlador principal de la aplicación de gestión de contactos.
 * Se encarga de manejar los eventos de la interfaz (botones, tabla, checkboxes, etc.)
 */
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {

    private ventana delegado;              // Referencia a la vista
    private List<persona> contactos;       // Lista de contactos cargados
    private String nombres, email, telefono, categoria = "";
    private boolean favorito = false;

    public logica_ventana(ventana delegado) {
        this.delegado = delegado;

        // Cargar los contactos existentes al iniciar
        cargarContactosRegistrados();

        // Asignar listeners a los componentes
        this.delegado.btn_add.addActionListener(this);
        this.delegado.btn_eliminar.addActionListener(this);
        this.delegado.btn_modificar.addActionListener(this);
        this.delegado.tablaContactos.getSelectionModel().addListSelectionListener(this);
        this.delegado.cmb_categoria.addItemListener(this);
        this.delegado.chb_favorito.addItemListener(this);
    }

    /**
     * Inicializa los campos de texto con la información ingresada por el usuario.
     */
    private void inicializarCampos() {
        nombres = delegado.txt_nombres.getText().trim();
        email = delegado.txt_email.getText().trim();
        telefono = delegado.txt_telefono.getText().trim();
        categoria = delegado.cmb_categoria.getSelectedItem().toString();
    }

    /**
     * Verifica que los campos obligatorios no estén vacíos.
     */
    private boolean camposLlenos() {
        return !nombres.isEmpty() && !telefono.isEmpty() && !email.isEmpty() && !categoria.equals("Elija una Categoria");
    }

    /**
     * Carga los contactos guardados en el archivo CSV y los muestra en la tabla.
     */
    private void cargarContactosRegistrados() {
        try {
            contactos = new personaDAO(new persona()).leerArchivo();
            actualizarTabla(contactos);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer los contactos: " + e.getMessage());
            contactos = new ArrayList<>();
        }
    }

    /**
     * Actualiza la tabla de contactos en pantalla con la lista actual.
     */
    private void actualizarTabla(List<persona> lista) {
        DefaultTableModel modelo = delegado.modeloTabla;
        modelo.setRowCount(0); // limpiar tabla

        for (persona p : lista) {
            Object[] fila = {
                    p.getNombre(),
                    p.getTelefono(),
                    p.getEmail(),
                    p.getCategoria(),
                    p.isFavorito() ? "Sí" : "No"
            };
            modelo.addRow(fila);
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCampos() {
        delegado.txt_nombres.setText("");
        delegado.txt_email.setText("");
        delegado.txt_telefono.setText("");
        delegado.cmb_categoria.setSelectedIndex(0);
        delegado.chb_favorito.setSelected(false);
        delegado.tablaContactos.clearSelection();
    }

    /**
     * Manejo de eventos de botones.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        if (fuente == delegado.btn_add) {
            agregarContacto();
        } else if (fuente == delegado.btn_eliminar) {
            eliminarContacto();
        } else if (fuente == delegado.btn_modificar) {
            modificarContacto();
        }
    }

    /**
     * Agrega un nuevo contacto al archivo y la tabla.
     */
    private void agregarContacto() {
        inicializarCampos();

        if (!camposLlenos()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        persona nueva = new persona(nombres, telefono, email, categoria, favorito);
        personaDAO dao = new personaDAO(nueva);
        dao.escribirArchivo();

        contactos.add(nueva);
        actualizarTabla(contactos);
        limpiarCampos();
        JOptionPane.showMessageDialog(null, "Contacto agregado correctamente.");
    }

    /**
     * Elimina el contacto seleccionado de la lista y el archivo.
     */
    private void eliminarContacto() {
        int fila = delegado.tablaContactos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un contacto para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el contacto seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        contactos.remove(fila);

        try {
            new personaDAO(new persona()).actualizarContactos(contactos);
            actualizarTabla(contactos);
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "Contacto eliminado.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar contacto: " + e.getMessage());
        }
    }

    /**
     * Modifica los datos del contacto seleccionado.
     */
    private void modificarContacto() {
        int fila = delegado.tablaContactos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione un contacto para modificar.");
            return;
        }

        inicializarCampos();
        if (!camposLlenos()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
            return;
        }

        persona p = contactos.get(fila);
        p.setNombre(nombres);
        p.setTelefono(telefono);
        p.setEmail(email);
        p.setCategoria(categoria);
        p.setFavorito(favorito);

        try {
            new personaDAO(new persona()).actualizarContactos(contactos);
            actualizarTabla(contactos);
            limpiarCampos();
            JOptionPane.showMessageDialog(null, "Contacto modificado correctamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar contacto: " + e.getMessage());
        }
    }

    /**
     * Cuando el usuario selecciona una fila en la tabla, se muestran los datos en los campos.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int fila = delegado.tablaContactos.getSelectedRow();
        if (fila != -1 && fila < contactos.size()) {
            persona p = contactos.get(fila);
            delegado.txt_nombres.setText(p.getNombre());
            delegado.txt_telefono.setText(p.getTelefono());
            delegado.txt_email.setText(p.getEmail());
            delegado.cmb_categoria.setSelectedItem(p.getCategoria());
            delegado.chb_favorito.setSelected(p.isFavorito());
        }
    }

    /**
     * Cuando cambia el valor del JComboBox o el CheckBox.
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        Object fuente = e.getSource();

        if (fuente == delegado.cmb_categoria && e.getStateChange() == ItemEvent.SELECTED) {
            categoria = delegado.cmb_categoria.getSelectedItem().toString();
        } else if (fuente == delegado.chb_favorito) {
            favorito = delegado.chb_favorito.isSelected();
        }
    }
}

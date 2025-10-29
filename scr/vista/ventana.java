package vista;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import controlador.logica_ventana;

/**
 * Ventana principal con soporte multilenguaje (Español, Inglés, Francés)
 */
public class ventana extends JFrame {

    public JPanel contentPane;
    public JTextField txt_nombres, txt_telefono, txt_email;
    public JCheckBox chb_favorito;
    public JComboBox<String> cmb_categoria, cmb_idioma;
    public JButton btn_add, btn_modificar, btn_eliminar;
    public JTable tablaContactos;
    public DefaultTableModel modeloTabla;
    public JTabbedPane tabbedPane;
    public JPanel panelContactos, panelEstadisticas;
    public JLabel lblNombre, lblTelefono, lblEmail;

    private ResourceBundle mensajes;
    private String idiomaActual;

    // 🔹 Constructor que recibe el idioma
    public ventana(String idioma) {
        setIdioma(idioma);
        idiomaActual = idioma;
        initUI();
    }

    // 🔹 Configuración del idioma usando ResourceBundle
    private void setIdioma(String idioma) {
        Locale locale;
        switch (idioma) {
            case "en": locale = new Locale("en", "US"); break;
            case "fr": locale = new Locale("fr", "FR"); break;
            default: locale = new Locale("es", "ES"); break;
        }
        mensajes = ResourceBundle.getBundle("idiomas.mensajes", locale);
    }

    // 🔹 Interfaz gráfica
    private void initUI() {
        setTitle(mensajes.getString("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 1026, 748);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Panel de contactos
        panelContactos = new JPanel();
        panelContactos.setLayout(null);
        tabbedPane.addTab(mensajes.getString("tab.contacts"), panelContactos);

        // ComboBox de idioma
        JLabel lblIdioma = new JLabel("Idioma:");
        lblIdioma.setBounds(850, 10, 100, 25);
        panelContactos.add(lblIdioma);

        cmb_idioma = new JComboBox<>(new String[]{"Español", "English", "Français"});
        cmb_idioma.setBounds(850, 35, 120, 25);
        panelContactos.add(cmb_idioma);

        // Selecciona el idioma actual en el combo
        switch (idiomaActual) {
            case "en": cmb_idioma.setSelectedItem("English"); break;
            case "fr": cmb_idioma.setSelectedItem("Français"); break;
            default: cmb_idioma.setSelectedItem("Español"); break;
        }

        lblNombre = new JLabel(mensajes.getString("label.name") + ":");
        lblNombre.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNombre.setBounds(25, 41, 150, 20);
        panelContactos.add(lblNombre);

        lblTelefono = new JLabel(mensajes.getString("label.phone") + ":");
        lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblTelefono.setBounds(25, 80, 150, 20);
        panelContactos.add(lblTelefono);

        lblEmail = new JLabel(mensajes.getString("label.email") + ":");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblEmail.setBounds(25, 122, 150, 20);
        panelContactos.add(lblEmail);

        txt_nombres = new JTextField();
        txt_nombres.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txt_nombres.setBounds(150, 28, 427, 31);
        panelContactos.add(txt_nombres);

        txt_telefono = new JTextField();
        txt_telefono.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txt_telefono.setBounds(150, 69, 427, 31);
        panelContactos.add(txt_telefono);

        txt_email = new JTextField();
        txt_email.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txt_email.setBounds(150, 110, 427, 31);
        panelContactos.add(txt_email);

        chb_favorito = new JCheckBox(mensajes.getString("checkbox.favorite"));
        chb_favorito.setFont(new Font("Tahoma", Font.PLAIN, 15));
        chb_favorito.setBounds(25, 170, 250, 21);
        panelContactos.add(chb_favorito);

        cmb_categoria = new JComboBox<>();
        cmb_categoria.setBounds(300, 167, 251, 31);
        cmb_categoria.addItem(mensajes.getString("combo.choose"));
        cmb_categoria.addItem(mensajes.getString("combo.family"));
        cmb_categoria.addItem(mensajes.getString("combo.friends"));
        cmb_categoria.addItem(mensajes.getString("combo.work"));
        panelContactos.add(cmb_categoria);

        btn_add = new JButton(mensajes.getString("button.add"));
        btn_add.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_add.setBounds(601, 70, 125, 65);
        panelContactos.add(btn_add);

        btn_modificar = new JButton(mensajes.getString("button.edit"));
        btn_modificar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_modificar.setBounds(736, 70, 125, 65);
        panelContactos.add(btn_modificar);

        btn_eliminar = new JButton(mensajes.getString("button.delete"));
        btn_eliminar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        btn_eliminar.setBounds(871, 69, 125, 65);
        panelContactos.add(btn_eliminar);

        String[] columnas = {
            mensajes.getString("label.name"),
            mensajes.getString("label.phone"),
            mensajes.getString("label.email"),
            mensajes.getString("label.category"),
            mensajes.getString("label.favorite")
        };
        modeloTabla = new DefaultTableModel(null, columnas);
        tablaContactos = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaContactos);
        scrollTabla.setBounds(25, 242, 971, 398);
        panelContactos.add(scrollTabla);

        // 🔹 Evento para cambiar idioma
        cmb_idioma.addActionListener(e -> {
            String selected = cmb_idioma.getSelectedItem().toString();
            String nuevoIdioma;

            if (selected.equals("English")) nuevoIdioma = "en";
            else if (selected.equals("Français")) nuevoIdioma = "fr";
            else nuevoIdioma = "es";

            dispose(); // cerrar ventana actual
            ventana nueva = new ventana(nuevoIdioma); // abrir con nuevo idioma
            nueva.setVisible(true);
        });

        // Inicializa la lógica del controlador
        new logica_ventana(this);
    }

    // 🔹 Método principal
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ventana frame = new ventana("es"); // idioma por defecto: español
            frame.setVisible(true);
        });
    }
}
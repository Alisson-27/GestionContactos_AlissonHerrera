package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la gestión de contactos.
 * Utiliza un archivo CSV almacenado en el directorio del usuario.
 */
public class personaDAO {

    private File archivo;
    private persona persona;

    public personaDAO(persona persona) {
        this.persona = persona;
        archivo = new File(System.getProperty("user.home") + "/gestionContactos");
        prepararArchivo();
    }

    private void prepararArchivo() {
        if (!archivo.exists()) {
            archivo.mkdir();
        }

        archivo = new File(archivo.getAbsolutePath(), "datosContactos.csv");

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                String encabezado = "NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO";
                escribir(encabezado);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void escribir(String texto) {
        try (FileWriter escribir = new FileWriter(archivo, true)) {
            escribir.write(texto + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean escribirArchivo() {
        escribir(persona.datosContacto());
        return true;
    }

    public List<persona> leerArchivo() throws IOException {
        List<persona> personas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primera = true;

            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }

                if (linea.trim().isEmpty()) continue;

                String[] campos = linea.split(";");
                if (campos.length < 5) continue;

                persona p = new persona(campos[0], campos[1], campos[2], campos[3], Boolean.parseBoolean(campos[4]));
                personas.add(p);
            }
        }

        return personas;
    }

    public void actualizarContactos(List<persona> personas) throws IOException {
        try (FileWriter fw = new FileWriter(archivo, false)) {
            fw.write("NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO\n");
            for (persona p : personas) {
                fw.write(p.datosContacto() + "\n");
            }
        }
    }
}
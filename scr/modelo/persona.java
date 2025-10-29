package modelo;

/**
 * Clase que representa a una persona (contacto) dentro de la aplicación.
 * Contiene sus datos básicos y métodos de acceso.
 */
public class persona {

    private String nombre, telefono, email, categoria;
    private boolean favorito;

    // Constructor vacío
    public persona() {
        this.nombre = "";
        this.telefono = "";
        this.email = "";
        this.categoria = "";
        this.favorito = false;
    }

    // Constructor con parámetros
    public persona(String nombre, String telefono, String email, String categoria, boolean favorito) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.categoria = categoria;
        this.favorito = favorito;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    // Valida el formato del correo
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            this.email = "correo@invalido.com";
        } else {
            this.email = email;
        }
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    // Devuelve el formato CSV para guardar en archivo
    public String datosContacto() {
        return String.format("%s;%s;%s;%s;%s", nombre, telefono, email, categoria, favorito);
    }

    // Formato para impresión en consola o depuración
    public String formatoLista() {
        return String.format("%-30s %-15s %-30s %-15s %-5s", nombre, telefono, email, categoria, favorito);
    }
}
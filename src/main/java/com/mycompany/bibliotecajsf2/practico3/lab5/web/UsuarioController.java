package com.mycompany.bibliotecajsf2.practico3.lab5.web;

import com.mycompany.bibliotecajsf2.practico3.lab5.Servicios.UsuarioServicio;
import com.mycompany.bibliotecajsf2.practico3.lab5.entidades.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author Usuario
 */
@Named(value = "usuarioController")
@ViewScoped
public class UsuarioController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuarioReg;
    private String txtUsuario;
    private String txtPassword;

    private boolean mostrandoRegistro = false;

    @Inject
    private UsuarioServicio usuarioServicio;

    @Inject
    private UsuarioSesionBean usuarioBean;

    @PostConstruct
    public void init() {
        usuarioReg = new Usuario();
    }

    /**
     * LÓGICA DE INICIO DE SESIÓN
     */
    public String iniciarSesion() {
        Optional<Usuario> uAux = usuarioServicio.buscarPorNombre(txtUsuario);

        if (uAux.isPresent() && uAux.get().getPassword().equals(txtPassword)) {

            usuarioBean.setId(uAux.get().getId());
            usuarioBean.setNombreUsuario(uAux.get().getNombreUsuario());
            usuarioBean.setRol(uAux.get().getRol());
            usuarioBean.setLogueado(true);

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            session.setAttribute("usuarioSBean", usuarioBean);

            if ("ADMIN".equals(uAux.get().getRol())) {
                return "/backend/librosBackend.xhtml?faces-redirect=true";
            } else {
                return "/index.xhtml?faces-redirect=true";
            }
        } else {
            FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Nombre de usuario o contraseña incorrecta.", "Credenciales inválidas");
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            return "/login.xhtml";
        }
    }

    /**
     * LÓGICA DE CIERRE DE SESIÓN
     */
    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    /**
     * VERIFICACIÓN DE SEGURIDAD PARA EL BACKEND
     */
    public void verificarSesion() throws IOException {
        // ⚡ OPTIMIZACIÓN COMPATIBLE: Usamos directamente 'usuarioBean' que está inyectado.
        // Es mucho más seguro y evita errores de casteo en el servidor GlassFish.
        if (usuarioBean == null || !usuarioBean.getLogueado()) {
            // Expulsamos al login si no está logueado
            FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");
        } else if (!"ADMIN".equals(usuarioBean.getRol())) {
            // Si está logueado pero NO es ADMIN, lo mandamos al catálogo público
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
        }
    }

    /**
     * LÓGICA DE REGISTRO PÚBLICO
     */
    public String registrar() {
        try {
            usuarioReg.setRol("USER");
            usuarioServicio.agregar(usuarioReg);

            usuarioReg = new Usuario(); // Limpiamos para el próximo

            // Cuando se registra con éxito, lo mandamos a que inicie sesión
            return "/login.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al registrarse. El usuario o email ya podrían existir.", "");
            FacesContext.getCurrentInstance().addMessage(null, mensaje);
            return null;
        }
    }

   public String mostrarFormularioRegistro() {
        this.mostrandoRegistro = true;
        return null; // ¡Borrá la línea de abajo!
    }

    public String mostrarFormularioLogin() {
        this.mostrandoRegistro = false;
        return null; // ¡Borrá la línea de abajo!
    }

    // ==========================================================================
    // GETTERS Y SETTERS
    // ==========================================================================
    public Usuario getUsuarioReg() {
        return usuarioReg;
    }

    public void setUsuarioReg(Usuario usuarioReg) {
        this.usuarioReg = usuarioReg;
    }

    public String getTxtUsuario() {
        return txtUsuario;
    }

    public void setTxtUsuario(String txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    public String getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(String txtPassword) {
        this.txtPassword = txtPassword;
    }

    public boolean isMostrandoRegistro() {
        return mostrandoRegistro;
    }

    public void setMostrandoRegistro(boolean mostrandoRegistro) {
        this.mostrandoRegistro = mostrandoRegistro;
    }
}

package com.sirelab.controller;

import com.sirelab.bo.interfacebo.GestionarLoginSistemaBOInterface;
import com.sirelab.utilidades.UsuarioLogin;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ANDRES PINEDA
 */
@Named(value = "controllerTemplate")
@ViewScoped
public class ControllerTemplate implements Serializable {

    @EJB
    GestionarLoginSistemaBOInterface gestionarLoginSistemaBO;
    
    private Date fechaSistema;
    private final DateFormat formatoFecha = DateFormat.getDateInstance(DateFormat.FULL);
    private String mensajeFechaActual;
    private UsuarioLogin usuarioLoginSistema;

    public ControllerTemplate() {
    }

    @PostConstruct
    public void init() {
        FacesContext faceContext = FacesContext.getCurrentInstance();
        HttpServletRequest httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuarioLoginSistema = (UsuarioLogin) httpServletRequest.getSession().getAttribute("sessionUsuario");
        mensajeFechaActual = new String();
    }

    public void cerrarSession() throws IOException {
        FacesContext x = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
    }

    public String obtenerFechaSistema() {
        fechaSistema = new Date();
        mensajeFechaActual = formatoFecha.format(fechaSistema);
        return mensajeFechaActual;
    }

    public Date getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }

    public String getMensajeFechaActual() {
        return mensajeFechaActual;
    }

    public void setMensajeFechaActual(String mensajeFechaActual) {
        this.mensajeFechaActual = mensajeFechaActual;
    }

    public UsuarioLogin getUsuarioLoginSistema() {
        return usuarioLoginSistema;
    }

    public void setUsuarioLoginSistema(UsuarioLogin usuarioLoginSistema) {
        this.usuarioLoginSistema = usuarioLoginSistema;
    }

}

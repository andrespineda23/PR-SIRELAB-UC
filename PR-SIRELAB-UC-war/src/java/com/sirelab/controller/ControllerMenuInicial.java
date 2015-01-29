/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.controller;

import com.sirelab.utilidades.UsuarioLogin;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ANDRES PINEDA
 */
@ManagedBean
@SessionScoped
public class ControllerMenuInicial implements Serializable {

    private UsuarioLogin usuarioLoginSistema;
    private String infoAdministracion;
    private String leyendaImagen;

    public ControllerMenuInicial() {
    }

    @PostConstruct
    public void init() {
        infoAdministracion = "Administración de los Estudiantes adscritos a la Universidad. \n"
                + "Permite activar, ,..... Permite activar, ,..... Permite activar, ,..... \n"
                + "Permite activar, ,..... Permite activar, ,..... Permite activar, ,..... \n"
                + "Permite activar, ,..... Permite activar, ,..... Permite activar, ,..... \n"
                + "Permite activar, ,..... Permite activar, ,..... Permite activar, ,..... \n"
                + "Permite activar, ,..... Permite activar, ,..... Permite activar, ,..... \n"
                + "Permite activar, ,..... Permite activar, ,..... Permite activar, ,..... \n"
                + "Permite activar, ,..... Permite activar, ,..... Permite activar, ,.....\n"
                + "Permite activar, ,..... Permite activar, ,..... Permite activar, ,..... ";
        leyendaImagen="Administración de los Estudiantes adscritos a la Universidad. Permite activar, ,....";
    }

    public String mensajeEntrada() {
        return "INICIAL";
    }

    public String getInfoAdministracion() {
        return infoAdministracion;
    }

    public void setInfoAdministracion(String infoAdministracion) {
        this.infoAdministracion = infoAdministracion;
    }

    public String getLeyendaImagen() {
        return leyendaImagen;
    }

    public void setLeyendaImagen(String leyendaImagen) {
        this.leyendaImagen = leyendaImagen;
    }

}

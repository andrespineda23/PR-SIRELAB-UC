/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarDocentesBOInterface;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Docente;
import com.sirelab.entidades.Facultad;
import com.sirelab.utilidades.UsuarioLogin;
import com.sirelab.utilidades.Utilidades;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ANDRES PINEDA
 */
@ManagedBean
@SessionScoped
public class ControllerDetallesDocente {

    @EJB
    AdministrarDocentesBOInterface administrarDocentesBO;

    private Docente docenteDetalles;
    private BigInteger idDocente;
    private boolean activarEditar, disabledEditar;
    private boolean modificacionRegistro;
    private boolean disabledActivar, disabledInactivar;
    private boolean visibleGuardar;
    private List<Facultad> listaFacultad;
    private Facultad facultadDocente;
    private List<Departamento> listaDepartamento;
    private Departamento departamentoDocente;
    private boolean activoDepartamento;
    private String nombreDocente, apellidoDocente, correoDocente, identificacionDocente;
    private String telefono1Docente, telefono2Docente, direccionDocente;
    private String cargoDocente;

    public ControllerDetallesDocente() {
    }

    @PostConstruct
    public void init() {
        activoDepartamento = true;
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        FacesContext faceContext = FacesContext.getCurrentInstance();
        HttpServletRequest httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        UsuarioLogin usuarioLoginSistema = (UsuarioLogin) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if ("ADMINISTRADOR".equalsIgnoreCase(usuarioLoginSistema.getNombreTipoUsuario())) {
            disabledEditar = false;
        }
    }

    public void asignarValoresVariablesDocente() {
        cargoDocente = docenteDetalles.getCargodocente();
        nombreDocente = docenteDetalles.getPersona().getNombrespersona();
        apellidoDocente = docenteDetalles.getPersona().getApellidospersona();
        correoDocente = docenteDetalles.getPersona().getEmailpersona();
        identificacionDocente = docenteDetalles.getPersona().getIdentificacionpersona();
        telefono1Docente = docenteDetalles.getPersona().getTelefono1persona();
        telefono2Docente = docenteDetalles.getPersona().getTelefono2persona();
        direccionDocente = docenteDetalles.getPersona().getDireccionpersona();
        facultadDocente = docenteDetalles.getDepartamento().getFacultad();
        departamentoDocente = docenteDetalles.getDepartamento();
    }

    public void recibirIDDocentesDetalles(BigInteger idDocente) {
        this.idDocente = idDocente;
        docenteDetalles = administrarDocentesBO.obtenerDocentePorIDDocente(idDocente);
        if (docenteDetalles.getPersona().getUsuario().getEstado() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesDocente();
    }

    public void activarEditarRegistro() {
        activarEditar = false;
        disabledEditar = true;
        modificacionRegistro = false;
        visibleGuardar = true;
        listaFacultad = administrarDocentesBO.obtenerListaFacultades();
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void restaurarInformacionDocente() {
        docenteDetalles = new Docente();
        docenteDetalles = administrarDocentesBO.obtenerDocentePorIDDocente(idDocente);
        if (docenteDetalles.getPersona().getUsuario().getEstado() == true) {
            System.out.println("Activo");
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            System.out.println("No Activo");
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesDocente();
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        activoDepartamento = true;
        listaDepartamento = null;
        listaFacultad = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void actualizarFacultades() {
        try {
            if (Utilidades.validarNulo(facultadDocente)) {
                activoDepartamento = false;
                departamentoDocente = null;
                listaDepartamento = administrarDocentesBO.obtenerDepartamentosPorIDFacultad(facultadDocente.getIdfacultad());
                modificacionesRegistroDocente();
            } else {
                activoDepartamento = true;
                listaDepartamento = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:departamentoDocente");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesDocente actualizarFacultades : " + e.toString());
        }
    }

    public void almacenarModificacionesDocente() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionRegistro == true) {
            System.out.println("Changes");
            if (validarNombreApellidoDocente() == true) {
                if (validarCorreoDocente() == true) {
                    if (validarIdentificacionDocente() == true) {
                        if (validarDatosNumericosDocente() == true) {
                            if (validarDireccionDocente() == true) {
                                if (validarDatosUniversidadDocente() == true) {
                                    if (validaDocenteYaRegistrado() == true) {
                                        modificarInformacionDocente();
                                    } else {
                                        context.execute("errorDocenteRegistrado.show()");
                                    }
                                } else {
                                    context.execute("errorUniversidadDocente.show()");
                                }
                            } else {
                                context.execute("errorDireccionDocente.show()");
                            }
                        } else {
                            context.execute("errorNumerosDocente.show()");
                        }
                    } else {
                        context.execute("errorDocumentoDocente.show()");
                    }
                } else {
                    context.execute("errorEmailDocente.show()");
                }
            } else {
                context.execute("errorNombreApellidoDocente.show()");
            }
        } else {
            System.out.println("No changes");
            context.execute("mensajeNoCambiosReg.show()");
            restaurarInformacionDocente();
        }
    }

    public void modificarInformacionDocente() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            docenteDetalles.getPersona().setApellidospersona(apellidoDocente);
            docenteDetalles.getPersona().setDireccionpersona(direccionDocente);
            docenteDetalles.getPersona().setEmailpersona(correoDocente);
            docenteDetalles.getPersona().setIdentificacionpersona(identificacionDocente);
            docenteDetalles.getPersona().setNombrespersona(nombreDocente);
            docenteDetalles.getPersona().setTelefono1persona(telefono1Docente);
            docenteDetalles.getPersona().setTelefono2persona(telefono2Docente);
            docenteDetalles.setDepartamento(departamentoDocente);
            docenteDetalles.setCargodocente(cargoDocente);
            administrarDocentesBO.actualizarInformacionDocente(docenteDetalles);
            context.execute("registroExitosoDocente.show()");
            restaurarInformacionDocente();
        } catch (Exception e) {
            System.out.println("Error modificarInformacionDocente almacenarNuevoDocenteEnSistema : " + e.toString());
            context.execute("registroFallidoDocente.show()");
        }
    }

    public boolean validarNombreApellidoDocente() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nombreDocente)) && (Utilidades.validarNulo(apellidoDocente))) {
            if (Utilidades.validarCaracterString(nombreDocente) == false) {
                retorno = false;
            }
            if (Utilidades.validarCaracterString(apellidoDocente) == false) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarCorreoDocente() {
        boolean retorno = true;
        if (Utilidades.validarNulo(correoDocente)) {
            if (Utilidades.validarCorreoElectronico(correoDocente)) {
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarIdentificacionDocente() {
        boolean retorno = true;
        if (Utilidades.validarNulo(identificacionDocente)) {
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosDocente() {
        boolean retorno = true;
        if (Utilidades.validarNulo(telefono1Docente)) {
            if (!Utilidades.isNumber(telefono1Docente)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(telefono2Docente)) {
            if (!Utilidades.isNumber(telefono2Docente)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarDireccionDocente() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(direccionDocente)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosUniversidadDocente() {
        boolean retorno = true;
        if (Utilidades.validarNulo(cargoDocente) && Utilidades.validarNulo(departamentoDocente) && Utilidades.validarNulo(facultadDocente)) {
            if (!Utilidades.validarCaracterString(cargoDocente.toString())) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validaDocenteYaRegistrado() {
        boolean retorno = true;
        Docente docenteRegistrado = administrarDocentesBO.obtenerDocentePorCorreoNumDocumento(correoDocente, identificacionDocente);
        if (null != docenteRegistrado) {
            if (!docenteDetalles.getIddocente().equals(docenteRegistrado.getIddocente())) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificacionesRegistroDocente() {
        if (modificacionRegistro == false) {
            modificacionRegistro = true;
        }
    }

    public void activarDocente() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(true);
                docenteDetalles.getPersona().getUsuario().setEstado(bool);
                administrarDocentesBO.actualizarInformacionDocente(docenteDetalles);
                restaurarInformacionDocente();
                context.execute("registroExitosoDocente.show()");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesDocentes activarDocente : " + e.toString());
        }
    }

    public void inactivarDocente() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(false);
                docenteDetalles.getPersona().getUsuario().setEstado(bool);
                administrarDocentesBO.actualizarInformacionDocente(docenteDetalles);
                docenteDetalles = new Docente();
                context.update("formT:form:panelMenu");
                restaurarInformacionDocente();
                context.execute("registroExitosoDocente.show();");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesDocentes inactivarDocente : " + e.toString());
        }
    }
// GET - SET

    public Docente getDocenteDetalles() {
        return docenteDetalles;
    }

    public void setDocenteDetalles(Docente docenteDetalles) {
        this.docenteDetalles = docenteDetalles;
    }

    public boolean isActivarEditar() {
        return activarEditar;
    }

    public void setActivarEditar(boolean activarEditar) {
        this.activarEditar = activarEditar;
    }

    public boolean isModificacionRegistro() {
        return modificacionRegistro;
    }

    public void setModificacionRegistro(boolean modificacionRegistro) {
        this.modificacionRegistro = modificacionRegistro;
    }

    public boolean isDisabledEditar() {
        return disabledEditar;
    }

    public void setDisabledEditar(boolean disabledEditar) {
        this.disabledEditar = disabledEditar;
    }

    public boolean isDisabledActivar() {
        return disabledActivar;
    }

    public void setDisabledActivar(boolean disabledActivar) {
        this.disabledActivar = disabledActivar;
    }

    public boolean isDisabledInactivar() {
        return disabledInactivar;
    }

    public void setDisabledInactivar(boolean disabledInactivar) {
        this.disabledInactivar = disabledInactivar;
    }

    public boolean isVisibleGuardar() {
        return visibleGuardar;
    }

    public void setVisibleGuardar(boolean visibleGuardar) {
        this.visibleGuardar = visibleGuardar;
    }

    public List<Facultad> getListaFacultad() {
        return listaFacultad;
    }

    public void setListaFacultad(List<Facultad> listaFacultad) {
        this.listaFacultad = listaFacultad;
    }

    public Facultad getFacultadDocente() {
        return facultadDocente;
    }

    public void setFacultadDocente(Facultad facultadDocente) {
        this.facultadDocente = facultadDocente;
    }

    public List<Departamento> getListaDepartamento() {
        return listaDepartamento;
    }

    public void setListaDepartamento(List<Departamento> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }

    public Departamento getDepartamentoDocente() {
        return departamentoDocente;
    }

    public void setDepartamentoDocente(Departamento departamentoDocente) {
        this.departamentoDocente = departamentoDocente;
    }

    public boolean isActivoDepartamento() {
        return activoDepartamento;
    }

    public void setActivoDepartamento(boolean activoDepartamento) {
        this.activoDepartamento = activoDepartamento;
    }

    public BigInteger getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(BigInteger idDocente) {
        this.idDocente = idDocente;
    }

    public String getNombreDocente() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }

    public String getApellidoDocente() {
        return apellidoDocente;
    }

    public void setApellidoDocente(String apellidoDocente) {
        this.apellidoDocente = apellidoDocente;
    }

    public String getCorreoDocente() {
        return correoDocente;
    }

    public void setCorreoDocente(String correoDocente) {
        this.correoDocente = correoDocente;
    }

    public String getIdentificacionDocente() {
        return identificacionDocente;
    }

    public void setIdentificacionDocente(String identificacionDocente) {
        this.identificacionDocente = identificacionDocente;
    }

    public String getTelefono1Docente() {
        return telefono1Docente;
    }

    public void setTelefono1Docente(String telefono1Docente) {
        this.telefono1Docente = telefono1Docente;
    }

    public String getTelefono2Docente() {
        return telefono2Docente;
    }

    public void setTelefono2Docente(String telefono2Docente) {
        this.telefono2Docente = telefono2Docente;
    }

    public String getDireccionDocente() {
        return direccionDocente;
    }

    public void setDireccionDocente(String direccionDocente) {
        this.direccionDocente = direccionDocente;
    }

    public String getCargoDocente() {
        return cargoDocente;
    }

    public void setCargoDocente(String cargoDocente) {
        this.cargoDocente = cargoDocente;
    }

}

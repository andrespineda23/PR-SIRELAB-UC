package com.sirelab.controller.usuarios;

import com.sirelab.bo.interfacebo.AdministrarEncargadosLaboratoriosBOInterface;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.EncargadoLaboratorio;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.utilidades.UsuarioLogin;
import com.sirelab.utilidades.Utilidades;
import java.io.Serializable;
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
public class ControllerDetallesEncargadoLaboratorio implements Serializable {
    
    @EJB
    AdministrarEncargadosLaboratoriosBOInterface administrarEncargadosLaboratoriosBO;
    
    private EncargadoLaboratorio encargadoLaboratorioDetalles;
    private BigInteger idEncargadoLaboratorio;
    private boolean activarEditar, disabledEditar;
    private boolean modificacionRegistro;
    private boolean disabledActivar, disabledInactivar;
    private boolean visibleGuardar;
    private List<Facultad> listaFacultad;
    private Facultad facultadEncargadoLaboratorio;
    private List<Departamento> listaDepartamento;
    private Departamento departamentoEncargadoLaboratorio;
    private List<Laboratorio> listaLaboratorio;
    private Laboratorio laboratorioEncargadoLaboratorio;
    private boolean activoDepartamento, activoLaboratorio;
    private String nombreEncargadoLaboratorio, apellidoEncargadoLaboratorio, correoEncargadoLaboratorio, identificacionEncargadoLaboratorio;
    private String telefono1EncargadoLaboratorio, telefono2EncargadoLaboratorio, direccionEncargadoLaboratorio;
    
    public ControllerDetallesEncargadoLaboratorio() {
    }
    
    @PostConstruct
    public void init() {
        activoDepartamento = true;
        activoLaboratorio = true;
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
    
    public void asignarValoresVariablesEncargadoLaboratorio() {
        laboratorioEncargadoLaboratorio = encargadoLaboratorioDetalles.getLaboratorio();
        nombreEncargadoLaboratorio = encargadoLaboratorioDetalles.getPersona().getNombrespersona();
        apellidoEncargadoLaboratorio = encargadoLaboratorioDetalles.getPersona().getApellidospersona();
        correoEncargadoLaboratorio = encargadoLaboratorioDetalles.getPersona().getEmailpersona();
        identificacionEncargadoLaboratorio = encargadoLaboratorioDetalles.getPersona().getIdentificacionpersona();
        telefono1EncargadoLaboratorio = encargadoLaboratorioDetalles.getPersona().getTelefono1persona();
        telefono2EncargadoLaboratorio = encargadoLaboratorioDetalles.getPersona().getTelefono2persona();
        direccionEncargadoLaboratorio = encargadoLaboratorioDetalles.getPersona().getDireccionpersona();
        facultadEncargadoLaboratorio = encargadoLaboratorioDetalles.getLaboratorio().getDepartamento().getFacultad();
        departamentoEncargadoLaboratorio = encargadoLaboratorioDetalles.getLaboratorio().getDepartamento();
    }
    
    public void recibirIDEncargadoLaboratorioDetalles(BigInteger idEncargadoLaboratorio) {
        this.idEncargadoLaboratorio = idEncargadoLaboratorio;
        encargadoLaboratorioDetalles = administrarEncargadosLaboratoriosBO.obtenerEncargadoLaboratorioPorIDEncargadoLaboratorio(idEncargadoLaboratorio);
        if (encargadoLaboratorioDetalles.getPersona().getUsuario().getEstado() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesEncargadoLaboratorio();
    }
    
    public void activarEditarRegistro() {
        activarEditar = false;
        disabledEditar = true;
        modificacionRegistro = false;
        visibleGuardar = true;
        listaFacultad = administrarEncargadosLaboratoriosBO.obtenerListaFacultades();
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }
    
    public void restaurarInformacionEncargadoLaboratorio() {
        encargadoLaboratorioDetalles = new EncargadoLaboratorio();
        encargadoLaboratorioDetalles = administrarEncargadosLaboratoriosBO.obtenerEncargadoLaboratorioPorIDEncargadoLaboratorio(idEncargadoLaboratorio);
        if (encargadoLaboratorioDetalles.getPersona().getUsuario().getEstado() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesEncargadoLaboratorio();
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        activoDepartamento = true;
        listaDepartamento = null;
        listaFacultad = null;
        activoLaboratorio = true;
        listaLaboratorio = null;
        laboratorioEncargadoLaboratorio = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }
    
    public void actualizarFacultades() {
        try {
            if (Utilidades.validarNulo(facultadEncargadoLaboratorio)) {
                activoDepartamento = false;
                departamentoEncargadoLaboratorio = null;
                activoLaboratorio = true;
                listaLaboratorio = null;
                laboratorioEncargadoLaboratorio = null;
                listaDepartamento = administrarEncargadosLaboratoriosBO.obtenerDepartamentosPorIDFacultad(facultadEncargadoLaboratorio.getIdfacultad());
                modificacionesRegistroEncargadoLaboratorio();
            } else {
                activoDepartamento = true;
                listaDepartamento = null;
                departamentoEncargadoLaboratorio = null;
                activoLaboratorio = true;
                listaLaboratorio = null;
                laboratorioEncargadoLaboratorio = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:departamentoEncargadoLaboratorio");
            context.update("formT:form:laboratorioEncargadoLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEncargadoLaboratorio actualizarFacultades : " + e.toString());
        }
    }
    
    public void actualizarDepartamentos() {
        try {
            if (Utilidades.validarNulo(departamentoEncargadoLaboratorio)) {
                activoLaboratorio = false;
                laboratorioEncargadoLaboratorio = null;
                listaLaboratorio = administrarEncargadosLaboratoriosBO.obtenerLaboratoriosPorIDDepartamento(departamentoEncargadoLaboratorio.getIddepartamento());
                modificacionesRegistroEncargadoLaboratorio();
            } else {
                activoLaboratorio = true;
                listaLaboratorio = null;
                laboratorioEncargadoLaboratorio = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:laboratorioEncargadoLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEncargadoLaboratorio actualizarDepartamentos : " + e.toString());
        }
    }
    
    public void almacenarModificacionesEncargadoLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionRegistro == true) {
            if (validarNombreApellidoEncargadoLaboratorio() == true) {
                if (validarCorreoEncargadoLaboratorio() == true) {
                    if (validarIdentificacionEncargadoLaboratorio() == true) {
                        if (validarDatosNumericosEncargadoLaboratorio() == true) {
                            if (validarDireccionEncargadoLaboratorio() == true) {
                                if (validarDatosUniversidadEncargadoLaboratorio() == true) {
                                    if (validaEncargadoLaboratorioYaRegistrado() == true) {
                                        modificarInformacionEncargadoLaboratorio();
                                    } else {
                                        context.execute("errorEncargadoLaboratorioRegistrado.show()");
                                    }
                                } else {
                                    context.execute("errorUniversidadEncargadoLaboratorio.show()");
                                }
                            } else {
                                context.execute("errorDireccionEncargadoLaboratorio.show()");
                            }
                        } else {
                            context.execute("errorNumerosEncargadoLaboratorio.show()");
                        }
                    } else {
                        context.execute("errorDocumentoEncargadoLaboratorio.show()");
                    }
                } else {
                    context.execute("errorEmailEncargadoLaboratorio.show()");
                }
            } else {
                context.execute("errorNombreApellidoEncargadoLaboratorio.show()");
            }
        } else {
            context.execute("mensajeNoCambiosReg.show()");
            restaurarInformacionEncargadoLaboratorio();
        }
    }
    
    public void modificarInformacionEncargadoLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            encargadoLaboratorioDetalles.getPersona().setApellidospersona(apellidoEncargadoLaboratorio);
            encargadoLaboratorioDetalles.getPersona().setDireccionpersona(direccionEncargadoLaboratorio);
            encargadoLaboratorioDetalles.getPersona().setEmailpersona(correoEncargadoLaboratorio);
            encargadoLaboratorioDetalles.getPersona().setIdentificacionpersona(identificacionEncargadoLaboratorio);
            encargadoLaboratorioDetalles.getPersona().setNombrespersona(nombreEncargadoLaboratorio);
            encargadoLaboratorioDetalles.getPersona().setTelefono1persona(telefono1EncargadoLaboratorio);
            encargadoLaboratorioDetalles.getPersona().setTelefono2persona(telefono2EncargadoLaboratorio);
            encargadoLaboratorioDetalles.setLaboratorio(laboratorioEncargadoLaboratorio);
            administrarEncargadosLaboratoriosBO.actualizarInformacionPersona(encargadoLaboratorioDetalles.getPersona());
            administrarEncargadosLaboratoriosBO.actualizarInformacionEncargadoLaboratorio(encargadoLaboratorioDetalles);
            context.execute("registroExitosoEncargadoLaboratorio.show()");
            restaurarInformacionEncargadoLaboratorio();
        } catch (Exception e) {
            System.out.println("Error modificarInformacionEncargadoLaboratorio almacenarNuevoEncargadoLaboratorioEnSistema : " + e.toString());
            context.execute("registroFallidoEncargadoLaboratorio.show()");
        }
    }
    
    public boolean validarNombreApellidoEncargadoLaboratorio() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nombreEncargadoLaboratorio)) && (Utilidades.validarNulo(apellidoEncargadoLaboratorio))) {
            if (Utilidades.validarCaracterString(nombreEncargadoLaboratorio) == false) {
                retorno = false;
            }
            if (Utilidades.validarCaracterString(apellidoEncargadoLaboratorio) == false) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }
    
    public boolean validarCorreoEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(correoEncargadoLaboratorio)) {
            if (Utilidades.validarCorreoElectronico(correoEncargadoLaboratorio)) {
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }
    
    public boolean validarIdentificacionEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(identificacionEncargadoLaboratorio)) {
        } else {
            retorno = false;
        }
        return retorno;
    }
    
    public boolean validarDatosNumericosEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(telefono1EncargadoLaboratorio)) {
            if (!Utilidades.isNumber(telefono1EncargadoLaboratorio)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(telefono2EncargadoLaboratorio)) {
            if (!Utilidades.isNumber(telefono2EncargadoLaboratorio)) {
                retorno = false;
            }
        }
        return retorno;
    }
    
    public boolean validarDireccionEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(direccionEncargadoLaboratorio)) {
            
        }
        return retorno;
    }
    
    public boolean validarDatosUniversidadEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(laboratorioEncargadoLaboratorio) && Utilidades.validarNulo(departamentoEncargadoLaboratorio) && Utilidades.validarNulo(facultadEncargadoLaboratorio)) {
        } else {
            retorno = false;
        }
        return retorno;
    }
    
    public boolean validaEncargadoLaboratorioYaRegistrado() {
        boolean retorno = true;
        EncargadoLaboratorio encargadoLaboratorioRegistrado = administrarEncargadosLaboratoriosBO.obtenerEncargadoLaboratorioPorCorreoNumDocumento(correoEncargadoLaboratorio, identificacionEncargadoLaboratorio);
        if (null != encargadoLaboratorioRegistrado) {
            if (!encargadoLaboratorioDetalles.getIdencargadolaboratorio().equals(encargadoLaboratorioRegistrado.getIdencargadolaboratorio())) {
                retorno = false;
            }
        }
        return retorno;
    }
    
    public void modificacionesRegistroEncargadoLaboratorio() {
        if (modificacionRegistro == false) {
            modificacionRegistro = true;
        }
    }
    
    public void activarEncargadoLaboratorio() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(true);
                encargadoLaboratorioDetalles.getPersona().getUsuario().setEstado(bool);
                administrarEncargadosLaboratoriosBO.actualizarInformacionUsuario(encargadoLaboratorioDetalles.getPersona().getUsuario());
                restaurarInformacionEncargadoLaboratorio();
                context.execute("registroExitosoEncargadoLaboratorio.show()");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEncargadosLaboratorios activarEncargadoLaboratorio : " + e.toString());
        }
    }
    
    public void inactivarEncargadoLaboratorio() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(false);
                encargadoLaboratorioDetalles.getPersona().getUsuario().setEstado(bool);
                administrarEncargadosLaboratoriosBO.actualizarInformacionUsuario(encargadoLaboratorioDetalles.getPersona().getUsuario());
                encargadoLaboratorioDetalles = new EncargadoLaboratorio();
                context.update("formT:form:panelMenu");
                restaurarInformacionEncargadoLaboratorio();
                context.execute("registroExitosoEncargadoLaboratorio.show();");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEncargadosLaboratorios inactivarEncargadoLaboratorio : " + e.toString());
        }
    }
// GET - SET

    public EncargadoLaboratorio getEncargadoLaboratorioDetalles() {
        return encargadoLaboratorioDetalles;
    }
    
    public void setEncargadoLaboratorioDetalles(EncargadoLaboratorio encargadoLaboratorioDetalles) {
        this.encargadoLaboratorioDetalles = encargadoLaboratorioDetalles;
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
    
    public Facultad getFacultadEncargadoLaboratorio() {
        return facultadEncargadoLaboratorio;
    }
    
    public void setFacultadEncargadoLaboratorio(Facultad facultadEncargadoLaboratorio) {
        this.facultadEncargadoLaboratorio = facultadEncargadoLaboratorio;
    }
    
    public List<Departamento> getListaDepartamento() {
        return listaDepartamento;
    }
    
    public void setListaDepartamento(List<Departamento> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }
    
    public Departamento getDepartamentoEncargadoLaboratorio() {
        return departamentoEncargadoLaboratorio;
    }
    
    public void setDepartamentoEncargadoLaboratorio(Departamento departamentoEncargadoLaboratorio) {
        this.departamentoEncargadoLaboratorio = departamentoEncargadoLaboratorio;
    }
    
    public boolean isActivoDepartamento() {
        return activoDepartamento;
    }
    
    public void setActivoDepartamento(boolean activoDepartamento) {
        this.activoDepartamento = activoDepartamento;
    }
    
    public BigInteger getIdEncargadoLaboratorio() {
        return idEncargadoLaboratorio;
    }
    
    public void setIdEncargadoLaboratorio(BigInteger idEncargadoLaboratorio) {
        this.idEncargadoLaboratorio = idEncargadoLaboratorio;
    }
    
    public String getNombreEncargadoLaboratorio() {
        return nombreEncargadoLaboratorio;
    }
    
    public void setNombreEncargadoLaboratorio(String nombreEncargadoLaboratorio) {
        this.nombreEncargadoLaboratorio = nombreEncargadoLaboratorio;
    }
    
    public String getApellidoEncargadoLaboratorio() {
        return apellidoEncargadoLaboratorio;
    }
    
    public void setApellidoEncargadoLaboratorio(String apellidoEncargadoLaboratorio) {
        this.apellidoEncargadoLaboratorio = apellidoEncargadoLaboratorio;
    }
    
    public String getCorreoEncargadoLaboratorio() {
        return correoEncargadoLaboratorio;
    }
    
    public void setCorreoEncargadoLaboratorio(String correoEncargadoLaboratorio) {
        this.correoEncargadoLaboratorio = correoEncargadoLaboratorio;
    }
    
    public String getIdentificacionEncargadoLaboratorio() {
        return identificacionEncargadoLaboratorio;
    }
    
    public void setIdentificacionEncargadoLaboratorio(String identificacionEncargadoLaboratorio) {
        this.identificacionEncargadoLaboratorio = identificacionEncargadoLaboratorio;
    }
    
    public String getTelefono1EncargadoLaboratorio() {
        return telefono1EncargadoLaboratorio;
    }
    
    public void setTelefono1EncargadoLaboratorio(String telefono1EncargadoLaboratorio) {
        this.telefono1EncargadoLaboratorio = telefono1EncargadoLaboratorio;
    }
    
    public String getTelefono2EncargadoLaboratorio() {
        return telefono2EncargadoLaboratorio;
    }
    
    public void setTelefono2EncargadoLaboratorio(String telefono2EncargadoLaboratorio) {
        this.telefono2EncargadoLaboratorio = telefono2EncargadoLaboratorio;
    }
    
    public String getDireccionEncargadoLaboratorio() {
        return direccionEncargadoLaboratorio;
    }
    
    public void setDireccionEncargadoLaboratorio(String direccionEncargadoLaboratorio) {
        this.direccionEncargadoLaboratorio = direccionEncargadoLaboratorio;
    }
    
    public List<Laboratorio> getListaLaboratorio() {
        return listaLaboratorio;
    }
    
    public void setListaLaboratorio(List<Laboratorio> listaLaboratorio) {
        this.listaLaboratorio = listaLaboratorio;
    }
    
    public Laboratorio getLaboratorioEncargadoLaboratorio() {
        return laboratorioEncargadoLaboratorio;
    }
    
    public void setLaboratorioEncargadoLaboratorio(Laboratorio laboratorioEncargadoLaboratorio) {
        this.laboratorioEncargadoLaboratorio = laboratorioEncargadoLaboratorio;
    }
    
    public boolean isActivoLaboratorio() {
        return activoLaboratorio;
    }
    
    public void setActivoLaboratorio(boolean activoLaboratorio) {
        this.activoLaboratorio = activoLaboratorio;
    }
    
}

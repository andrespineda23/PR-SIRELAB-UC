package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarEntidadesExternasBOInterface;
import com.sirelab.entidades.EntidadExterna;
import com.sirelab.utilidades.UsuarioLogin;
import com.sirelab.utilidades.Utilidades;
import java.io.Serializable;
import java.math.BigInteger;
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
public class ControllerDetallesEntidadExterna implements Serializable {

    @EJB
    AdministrarEntidadesExternasBOInterface administrarEntidadesExternasBO;

    private EntidadExterna entidadExternaDetalles;
    private BigInteger idEntidadExterna;
    private boolean activarEditar, disabledEditar;
    private boolean modificacionRegistro;
    private boolean disabledActivar, disabledInactivar;
    private boolean visibleGuardar;
    private String nombreEntidadExterna, apellidoEntidadExterna, correoEntidadExterna, identificacionEntidadExterna;
    private String telefono1EntidadExterna, telefono2EntidadExterna, direccionEntidadExterna;
    private String nombreUnicoEntidadExterna, emailUnicoEntidadExterna, idUnicoEntidadExterna;

    public ControllerDetallesEntidadExterna() {
    }

    @PostConstruct
    public void init() {
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

    public void asignarValoresVariablesEntidadExterna() {
        nombreUnicoEntidadExterna = entidadExternaDetalles.getNombreentidad();
        emailUnicoEntidadExterna = entidadExternaDetalles.getEmailentidad();
        idUnicoEntidadExterna = entidadExternaDetalles.getIdentificacionentidad();
        nombreEntidadExterna = entidadExternaDetalles.getPersona().getNombrespersona();
        apellidoEntidadExterna = entidadExternaDetalles.getPersona().getApellidospersona();
        correoEntidadExterna = entidadExternaDetalles.getPersona().getEmailpersona();
        identificacionEntidadExterna = entidadExternaDetalles.getPersona().getIdentificacionpersona();
        telefono1EntidadExterna = entidadExternaDetalles.getPersona().getTelefono1persona();
        telefono2EntidadExterna = entidadExternaDetalles.getPersona().getTelefono2persona();
        direccionEntidadExterna = entidadExternaDetalles.getPersona().getDireccionpersona();
    }

    public void recibirIDEntidadesExternasDetalles(BigInteger idEntidadExterna) {
        this.idEntidadExterna = idEntidadExterna;
        entidadExternaDetalles = administrarEntidadesExternasBO.obtenerEntidadExternaPorIDEntidadExterna(idEntidadExterna);
        if (entidadExternaDetalles.getPersona().getUsuario().getEstado() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesEntidadExterna();
    }

    public void activarEditarRegistro() {
        activarEditar = false;
        disabledEditar = true;
        modificacionRegistro = false;
        visibleGuardar = true;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void restaurarInformacionEntidadExterna() {
        entidadExternaDetalles = new EntidadExterna();
        entidadExternaDetalles = administrarEntidadesExternasBO.obtenerEntidadExternaPorIDEntidadExterna(idEntidadExterna);
        if (entidadExternaDetalles.getPersona().getUsuario().getEstado() == true) {
            System.out.println("Activo");
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            System.out.println("No Activo");
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesEntidadExterna();
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void almacenarModificacionesEntidadExterna() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionRegistro == true) {
            System.out.println("Changes");
            if (validarNombreApellidoEntidadExterna() == true) {
                if (validarCorreoEntidadExterna() == true) {
                    if (validarIdentificacionEntidadExterna() == true) {
                        if (validarDatosNumericosEntidadExterna() == true) {
                            if (validarDireccionEntidadExterna() == true) {
                                if (validaEntidadExternaYaRegistrado() == true) {
                                    modificarInformacionEntidadExterna();
                                } else {
                                    context.execute("errorEntidadExternaRegistrado.show()");
                                }
                            } else {
                                context.execute("errorDireccionEntidadExterna.show()");
                            }
                        } else {
                            context.execute("errorNumerosEntidadExterna.show()");
                        }
                    } else {
                        context.execute("errorDocumentoEntidadExterna.show()");
                    }
                } else {
                    context.execute("errorEmailEntidadExterna.show()");
                }
            } else {
                context.execute("errorNombreApellidoEntidadExterna.show()");
            }
        } else {
            System.out.println("No changes");
            context.execute("mensajeNoCambiosReg.show()");
            restaurarInformacionEntidadExterna();
        }
    }

    public void modificarInformacionEntidadExterna() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            entidadExternaDetalles.getPersona().setApellidospersona(apellidoEntidadExterna);
            entidadExternaDetalles.getPersona().setDireccionpersona(direccionEntidadExterna);
            entidadExternaDetalles.getPersona().setEmailpersona(correoEntidadExterna);
            entidadExternaDetalles.getPersona().setIdentificacionpersona(identificacionEntidadExterna);
            entidadExternaDetalles.getPersona().setNombrespersona(nombreEntidadExterna);
            entidadExternaDetalles.getPersona().setTelefono1persona(telefono1EntidadExterna);
            entidadExternaDetalles.getPersona().setTelefono2persona(telefono2EntidadExterna);
            entidadExternaDetalles.setNombreentidad(nombreUnicoEntidadExterna);
            entidadExternaDetalles.setIdentificacionentidad(idUnicoEntidadExterna);
            entidadExternaDetalles.setEmailentidad(emailUnicoEntidadExterna);
            administrarEntidadesExternasBO.actualizarInformacionPersona(entidadExternaDetalles.getPersona());
            administrarEntidadesExternasBO.actualizarInformacionEntidadExterna(entidadExternaDetalles);
            context.execute("registroExitosoEntidadExterna.show()");
            restaurarInformacionEntidadExterna();
        } catch (Exception e) {
            System.out.println("Error modificarInformacionEntidadExterna almacenarNuevoEntidadExternaEnSistema : " + e.toString());
            context.execute("registroFallidoEntidadExterna.show()");
        }
    }

    public boolean validarNombreApellidoEntidadExterna() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nombreEntidadExterna)) && (Utilidades.validarNulo(apellidoEntidadExterna))) {
            if (Utilidades.validarCaracterString(nombreEntidadExterna) == false) {
                retorno = false;
            }
            if (Utilidades.validarCaracterString(apellidoEntidadExterna) == false) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        if (Utilidades.validarNulo(nombreUnicoEntidadExterna)) {
            if (Utilidades.validarCaracterString(nombreUnicoEntidadExterna) == false) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCorreoEntidadExterna() {
        boolean retorno = true;
        if (Utilidades.validarNulo(correoEntidadExterna)) {
            if (!Utilidades.validarCorreoElectronico(correoEntidadExterna)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        if (Utilidades.validarNulo(emailUnicoEntidadExterna)) {
            if (!Utilidades.validarCorreoElectronico(emailUnicoEntidadExterna)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarIdentificacionEntidadExterna() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(identificacionEntidadExterna)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(idUnicoEntidadExterna)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosEntidadExterna() {
        boolean retorno = true;
        if (Utilidades.validarNulo(telefono1EntidadExterna)) {
            if (!Utilidades.isNumber(telefono1EntidadExterna)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(telefono2EntidadExterna)) {
            if (!Utilidades.isNumber(telefono2EntidadExterna)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarDireccionEntidadExterna() {
        boolean retorno = true;
        if (Utilidades.validarNulo(direccionEntidadExterna)) {

        }
        return retorno;
    }

    public boolean validaEntidadExternaYaRegistrado() {
        boolean retorno = true;
        EntidadExterna entidadexternaRegistrado = administrarEntidadesExternasBO.obtenerEntidadExternaPorCorreoNumDocumento(correoEntidadExterna, identificacionEntidadExterna);
        if (null != entidadexternaRegistrado) {
            if (!entidadExternaDetalles.getIdentidadexterna().equals(entidadexternaRegistrado.getIdentidadexterna())) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificacionesRegistroEntidadExterna() {
        if (modificacionRegistro == false) {
            modificacionRegistro = true;
        }
    }

    public void activarEntidadExterna() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(true);
                entidadExternaDetalles.getPersona().getUsuario().setEstado(bool);
                administrarEntidadesExternasBO.actualizarInformacionUsuario(entidadExternaDetalles.getPersona().getUsuario());
                restaurarInformacionEntidadExterna();
                context.execute("registroExitosoEntidadExterna.show()");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEntidadesExternas activarEntidadExterna : " + e.toString());
        }
    }

    public void inactivarEntidadExterna() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(false);
                entidadExternaDetalles.getPersona().getUsuario().setEstado(bool);
                administrarEntidadesExternasBO.actualizarInformacionUsuario(entidadExternaDetalles.getPersona().getUsuario());
                entidadExternaDetalles = new EntidadExterna();
                context.update("formT:form:panelMenu");
                restaurarInformacionEntidadExterna();
                context.execute("registroExitosoEntidadExterna.show();");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEntidadesExternas inactivarEntidadExterna : " + e.toString());
        }
    }
// GET - SET

    public EntidadExterna getEntidadExternaDetalles() {
        return entidadExternaDetalles;
    }

    public void setEntidadExternaDetalles(EntidadExterna entidadExternaDetalles) {
        this.entidadExternaDetalles = entidadExternaDetalles;
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

    public BigInteger getIdEntidadExterna() {
        return idEntidadExterna;
    }

    public void setIdEntidadExterna(BigInteger idEntidadExterna) {
        this.idEntidadExterna = idEntidadExterna;
    }

    public String getNombreEntidadExterna() {
        return nombreEntidadExterna;
    }

    public void setNombreEntidadExterna(String nombreEntidadExterna) {
        this.nombreEntidadExterna = nombreEntidadExterna;
    }

    public String getApellidoEntidadExterna() {
        return apellidoEntidadExterna;
    }

    public void setApellidoEntidadExterna(String apellidoEntidadExterna) {
        this.apellidoEntidadExterna = apellidoEntidadExterna;
    }

    public String getCorreoEntidadExterna() {
        return correoEntidadExterna;
    }

    public void setCorreoEntidadExterna(String correoEntidadExterna) {
        this.correoEntidadExterna = correoEntidadExterna;
    }

    public String getIdentificacionEntidadExterna() {
        return identificacionEntidadExterna;
    }

    public void setIdentificacionEntidadExterna(String identificacionEntidadExterna) {
        this.identificacionEntidadExterna = identificacionEntidadExterna;
    }

    public String getTelefono1EntidadExterna() {
        return telefono1EntidadExterna;
    }

    public void setTelefono1EntidadExterna(String telefono1EntidadExterna) {
        this.telefono1EntidadExterna = telefono1EntidadExterna;
    }

    public String getTelefono2EntidadExterna() {
        return telefono2EntidadExterna;
    }

    public void setTelefono2EntidadExterna(String telefono2EntidadExterna) {
        this.telefono2EntidadExterna = telefono2EntidadExterna;
    }

    public String getDireccionEntidadExterna() {
        return direccionEntidadExterna;
    }

    public void setDireccionEntidadExterna(String direccionEntidadExterna) {
        this.direccionEntidadExterna = direccionEntidadExterna;
    }

    public String getNombreUnicoEntidadExterna() {
        return nombreUnicoEntidadExterna;
    }

    public void setNombreUnicoEntidadExterna(String nombreUnicoEntidadExterna) {
        this.nombreUnicoEntidadExterna = nombreUnicoEntidadExterna;
    }

    public String getEmailUnicoEntidadExterna() {
        return emailUnicoEntidadExterna;
    }

    public void setEmailUnicoEntidadExterna(String emailUnicoEntidadExterna) {
        this.emailUnicoEntidadExterna = emailUnicoEntidadExterna;
    }

    public String getIdUnicoEntidadExterna() {
        return idUnicoEntidadExterna;
    }

    public void setIdUnicoEntidadExterna(String idUnicoEntidadExterna) {
        this.idUnicoEntidadExterna = idUnicoEntidadExterna;
    }

}

package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarEstudiantesBOInterface;
import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Estudiante;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.PlanEstudios;
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
public class ControllerDetallesEstudiante implements Serializable {

    @EJB
    AdministrarEstudiantesBOInterface administrarEstudiantesBO;

    private Estudiante estudianteDetalles;
    private BigInteger idEstudiante;
    private boolean activarEditar, disabledEditar;
    private boolean modificacionRegistro;
    private boolean disabledActivar, disabledInactivar;
    private boolean visibleGuardar;
    private List<Facultad> listaFacultad;
    private Facultad facultadEstudiante;
    private List<Departamento> listaDepartamento;
    private Departamento departamentoEstudiante;
    private boolean activoDepartamento;
    private List<Carrera> listaCarrera;
    private Carrera carreraEstudiante;
    private boolean activoCarrera;
    private List<PlanEstudios> listaPlanEstudios;
    private PlanEstudios planEstudioEstudiante;
    private boolean activoPlanEstudio;
    private String nombreEstudiante, apellidoEstudiante, correoEstudiante, identificacionEstudiante;
    private String telefono1Estudiante, telefono2Estudiante, direccionEstudiante;
    private Integer semestreEstudiante;

    public ControllerDetallesEstudiante() {
    }

    @PostConstruct
    public void init() {
        activoDepartamento = true;
        activoCarrera = true;
        activoPlanEstudio = true;
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

    public void asignarValoresVariablesEstudiante() {
        semestreEstudiante = estudianteDetalles.getSemestreestudiante();
        nombreEstudiante = estudianteDetalles.getPersona().getNombrespersona();
        apellidoEstudiante = estudianteDetalles.getPersona().getApellidospersona();
        correoEstudiante = estudianteDetalles.getPersona().getEmailpersona();
        identificacionEstudiante = estudianteDetalles.getPersona().getIdentificacionpersona();
        telefono1Estudiante = estudianteDetalles.getPersona().getTelefono1persona();
        telefono2Estudiante = estudianteDetalles.getPersona().getTelefono2persona();
        direccionEstudiante = estudianteDetalles.getPersona().getDireccionpersona();
        facultadEstudiante = estudianteDetalles.getPlanestudio().getCarrera().getDepartamento().getFacultad();
        departamentoEstudiante = estudianteDetalles.getPlanestudio().getCarrera().getDepartamento();
        carreraEstudiante = estudianteDetalles.getPlanestudio().getCarrera();
        planEstudioEstudiante = estudianteDetalles.getPlanestudio();
    }

    public void recibirIDEstudiantesDetalles(BigInteger idEstudiante) {
        this.idEstudiante = idEstudiante;
        estudianteDetalles = administrarEstudiantesBO.obtenerEstudiantePorIDEstudiante(idEstudiante);
        if (estudianteDetalles.getPersona().getUsuario().getEstado() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesEstudiante();
    }

    public void activarEditarRegistro() {
        activarEditar = false;
        disabledEditar = true;
        modificacionRegistro = false;
        visibleGuardar = true;
        listaFacultad = administrarEstudiantesBO.obtenerListaFacultades();
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void restaurarInformacionEstudiante() {
        estudianteDetalles = new Estudiante();
        estudianteDetalles = administrarEstudiantesBO.obtenerEstudiantePorIDEstudiante(idEstudiante);
        if (estudianteDetalles.getPersona().getUsuario().getEstado() == true) {
            System.out.println("Activo");
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            System.out.println("No Activo");
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesEstudiante();
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        activoDepartamento = true;
        activoCarrera = true;
        activoPlanEstudio = true;
        listaCarrera = null;
        listaDepartamento = null;
        listaPlanEstudios = null;
        listaFacultad = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void actualizarFacultades() {
        try {
            if (Utilidades.validarNulo(facultadEstudiante)) {
                activoDepartamento = false;
                departamentoEstudiante = null;
                listaDepartamento = administrarEstudiantesBO.obtenerDepartamentosPorIDFacultad(facultadEstudiante.getIdfacultad());
                carreraEstudiante = null;
                listaCarrera = null;
                planEstudioEstudiante = null;
                listaPlanEstudios = null;
                modificacionesRegistroEstudiante();
            } else {
                activoDepartamento = true;
                departamentoEstudiante = null;
                listaDepartamento = null;
                activoCarrera = true;
                carreraEstudiante = null;
                listaCarrera = null;
                activoPlanEstudio = true;
                planEstudioEstudiante = null;
                listaPlanEstudios = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:departamentoEstudiante");
            context.update("formT:form:carreraEstudiante");
            context.update("formT:form:planEstudioEstudiante");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEstudiante actualizarFacultades : " + e.toString());
        }
    }

    public void actualizarDepartamentos() {
        try {
            if (Utilidades.validarNulo(departamentoEstudiante)) {
                activoCarrera = false;
                carreraEstudiante = null;
                listaCarrera = administrarEstudiantesBO.obtenerListasCarrerasPorDepartamento(departamentoEstudiante.getIddepartamento());
                planEstudioEstudiante = null;
                listaPlanEstudios = null;
                modificacionesRegistroEstudiante();
            } else {
                activoCarrera = true;
                carreraEstudiante = null;
                listaCarrera = null;
                activoPlanEstudio = true;
                listaPlanEstudios = null;
                planEstudioEstudiante = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:carreraEstudiante");
            context.update("formT:form:planEstudioEstudiante");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEstudiante actualizarDepartamentos : " + e.toString());
        }
    }

    public void actualizarCarreras() {
        try {
            if (Utilidades.validarNulo(carreraEstudiante)) {
                activoPlanEstudio = false;
                planEstudioEstudiante = null;
                listaPlanEstudios = administrarEstudiantesBO.obtenerListasPlanesEstudioPorCarrera(carreraEstudiante.getIdcarrera());
                modificacionesRegistroEstudiante();
            } else {
                activoPlanEstudio = true;
                listaPlanEstudios = null;
                planEstudioEstudiante = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:planEstudioEstudiante");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEstudiante actualizarCarreras : " + e.toString());
        }
    }

    public void almacenarModificacionesEstudiante() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionRegistro == true) {
            System.out.println("Changes");
            if (validarNombreApellidoEstudiante() == true) {
                if (validarCorreoEstudiante() == true) {
                    if (validarIdentificacionEstudiante() == true) {
                        if (validarDatosNumericosEstudiante() == true) {
                            if (validarDireccionEstudiante() == true) {
                                if (validarDatosUniversidadEstudiante() == true) {
                                    if (validaEstudianteYaRegistrado() == true) {
                                        modificarInformacionEstudiante();
                                    } else {
                                        context.execute("errorEstudianteRegistrado.show()");
                                    }
                                } else {
                                    context.execute("errorUniversidadEstudiante.show()");
                                }
                            } else {
                                context.execute("errorDireccionEstudiante.show()");
                            }
                        } else {
                            context.execute("errorNumerosEstudiante.show()");
                        }
                    } else {
                        context.execute("errorDocumentoEstudiante.show()");
                    }
                } else {
                    context.execute("errorEmailEstudiante.show()");
                }
            } else {
                context.execute("errorNombreApellidoEstudiante.show()");
            }
        } else {
            System.out.println("No changes");
            context.execute("mensajeNoCambiosReg.show()");
            restaurarInformacionEstudiante();
        }
    }

    public void modificarInformacionEstudiante() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            estudianteDetalles.getPersona().setApellidospersona(apellidoEstudiante);
            estudianteDetalles.getPersona().setDireccionpersona(direccionEstudiante);
            estudianteDetalles.getPersona().setEmailpersona(correoEstudiante);
            estudianteDetalles.getPersona().setIdentificacionpersona(identificacionEstudiante);
            estudianteDetalles.getPersona().setNombrespersona(nombreEstudiante);
            estudianteDetalles.getPersona().setTelefono1persona(telefono1Estudiante);
            estudianteDetalles.getPersona().setTelefono2persona(telefono2Estudiante);
            estudianteDetalles.setPlanestudio(planEstudioEstudiante);
            estudianteDetalles.setSemestreestudiante(semestreEstudiante);
            administrarEstudiantesBO.actualizarInformacionPersona(estudianteDetalles.getPersona());
            administrarEstudiantesBO.actualizarInformacionEstudiante(estudianteDetalles);
            context.execute("registroExitosoEstudiante.show()");
            restaurarInformacionEstudiante();
        } catch (Exception e) {
            System.out.println("Error modificarInformacionEstudiante almacenarNuevoEstudianteEnSistema : " + e.toString());
            context.execute("registroFallidoEstudiante.show()");
        }
    }

    public boolean validarNombreApellidoEstudiante() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nombreEstudiante)) && (Utilidades.validarNulo(apellidoEstudiante))) {
            if (Utilidades.validarCaracterString(nombreEstudiante) == false) {
                retorno = false;
            }
            if (Utilidades.validarCaracterString(apellidoEstudiante) == false) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarCorreoEstudiante() {
        boolean retorno = true;
        if (Utilidades.validarNulo(correoEstudiante)) {
            if (Utilidades.validarCorreoElectronico(correoEstudiante)) {
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarIdentificacionEstudiante() {
        boolean retorno = true;
        if (Utilidades.validarNulo(identificacionEstudiante)) {
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosEstudiante() {
        boolean retorno = true;
        if (Utilidades.validarNulo(telefono1Estudiante)) {
            if (!Utilidades.isNumber(telefono1Estudiante)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(telefono2Estudiante)) {
            if (!Utilidades.isNumber(telefono2Estudiante)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarDireccionEstudiante() {
        boolean retorno = true;
        if (Utilidades.validarNulo(direccionEstudiante)) {
            
        }
        return retorno;
    }

    public boolean validarDatosUniversidadEstudiante() {
        boolean retorno = true;
        if (Utilidades.validarNulo(semestreEstudiante) && Utilidades.validarNulo(departamentoEstudiante) && Utilidades.validarNulo(carreraEstudiante) && Utilidades.validarNulo(planEstudioEstudiante) && Utilidades.validarNulo(facultadEstudiante)) {
            if (!Utilidades.isNumber(semestreEstudiante.toString())) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validaEstudianteYaRegistrado() {
        boolean retorno = true;
        Estudiante estudianteRegistrado = administrarEstudiantesBO.obtenerEstudiantePorCorreoNumDocumento(correoEstudiante, identificacionEstudiante);
        if (null != estudianteRegistrado) {
            if (!estudianteDetalles.getIdestudiante().equals(estudianteRegistrado.getIdestudiante())) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificacionesRegistroEstudiante() {
        if (modificacionRegistro == false) {
            modificacionRegistro = true;
        }
    }

    public void activarEstudiante() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(true);
                estudianteDetalles.getPersona().getUsuario().setEstado(bool);
                administrarEstudiantesBO.actualizarInformacionUsuario(estudianteDetalles.getPersona().getUsuario());
                restaurarInformacionEstudiante();
                context.execute("registroExitosoEstudiante.show()");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEstudiantes activarEstudiante : " + e.toString());
        }
    }

    public void inactivarEstudiante() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(false);
                estudianteDetalles.getPersona().getUsuario().setEstado(bool);
                administrarEstudiantesBO.actualizarInformacionUsuario(estudianteDetalles.getPersona().getUsuario());
                estudianteDetalles = new Estudiante();
                context.update("formT:form:panelMenu");
                restaurarInformacionEstudiante();
                context.execute("registroExitosoEstudiante.show();");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesEstudiantes inactivarEstudiante : " + e.toString());
        }
    }
// GET - SET

    public Estudiante getEstudianteDetalles() {
        return estudianteDetalles;
    }

    public void setEstudianteDetalles(Estudiante estudianteDetalles) {
        this.estudianteDetalles = estudianteDetalles;
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

    public Facultad getFacultadEstudiante() {
        return facultadEstudiante;
    }

    public void setFacultadEstudiante(Facultad facultadEstudiante) {
        this.facultadEstudiante = facultadEstudiante;
    }

    public List<Departamento> getListaDepartamento() {
        return listaDepartamento;
    }

    public void setListaDepartamento(List<Departamento> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }

    public Departamento getDepartamentoEstudiante() {
        return departamentoEstudiante;
    }

    public void setDepartamentoEstudiante(Departamento departamentoEstudiante) {
        this.departamentoEstudiante = departamentoEstudiante;
    }

    public boolean isActivoDepartamento() {
        return activoDepartamento;
    }

    public void setActivoDepartamento(boolean activoDepartamento) {
        this.activoDepartamento = activoDepartamento;
    }

    public List<Carrera> getListaCarrera() {
        return listaCarrera;
    }

    public void setListaCarrera(List<Carrera> listaCarrera) {
        this.listaCarrera = listaCarrera;
    }

    public Carrera getCarreraEstudiante() {
        return carreraEstudiante;
    }

    public void setCarreraEstudiante(Carrera carreraEstudiante) {
        this.carreraEstudiante = carreraEstudiante;
    }

    public boolean isActivoCarrera() {
        return activoCarrera;
    }

    public void setActivoCarrera(boolean activoCarrera) {
        this.activoCarrera = activoCarrera;
    }

    public List<PlanEstudios> getListaPlanEstudios() {
        return listaPlanEstudios;
    }

    public void setListaPlanEstudios(List<PlanEstudios> listaPlanEstudios) {
        this.listaPlanEstudios = listaPlanEstudios;
    }

    public PlanEstudios getPlanEstudioEstudiante() {
        return planEstudioEstudiante;
    }

    public void setPlanEstudioEstudiante(PlanEstudios planEstudioEstudiante) {
        this.planEstudioEstudiante = planEstudioEstudiante;
    }

    public boolean isActivoPlanEstudio() {
        return activoPlanEstudio;
    }

    public void setActivoPlanEstudio(boolean activoPlanEstudio) {
        this.activoPlanEstudio = activoPlanEstudio;
    }

    public BigInteger getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(BigInteger idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getApellidoEstudiante() {
        return apellidoEstudiante;
    }

    public void setApellidoEstudiante(String apellidoEstudiante) {
        this.apellidoEstudiante = apellidoEstudiante;
    }

    public String getCorreoEstudiante() {
        return correoEstudiante;
    }

    public void setCorreoEstudiante(String correoEstudiante) {
        this.correoEstudiante = correoEstudiante;
    }

    public String getIdentificacionEstudiante() {
        return identificacionEstudiante;
    }

    public void setIdentificacionEstudiante(String identificacionEstudiante) {
        this.identificacionEstudiante = identificacionEstudiante;
    }

    public String getTelefono1Estudiante() {
        return telefono1Estudiante;
    }

    public void setTelefono1Estudiante(String telefono1Estudiante) {
        this.telefono1Estudiante = telefono1Estudiante;
    }

    public String getTelefono2Estudiante() {
        return telefono2Estudiante;
    }

    public void setTelefono2Estudiante(String telefono2Estudiante) {
        this.telefono2Estudiante = telefono2Estudiante;
    }

    public String getDireccionEstudiante() {
        return direccionEstudiante;
    }

    public void setDireccionEstudiante(String direccionEstudiante) {
        this.direccionEstudiante = direccionEstudiante;
    }

    public Integer getSemestreEstudiante() {
        return semestreEstudiante;
    }

    public void setSemestreEstudiante(Integer semestreEstudiante) {
        this.semestreEstudiante = semestreEstudiante;
    }

}

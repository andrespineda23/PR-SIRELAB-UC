package com.sirelab.controller;

import com.sirelab.bo.interfacebo.GestionarLoginSistemaBOInterface;
import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Docente;
import com.sirelab.entidades.EncargadoLaboratorio;
import com.sirelab.entidades.EntidadExterna;
import com.sirelab.entidades.Estudiante;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.PlanEstudios;
import com.sirelab.entidades.Usuario;
import com.sirelab.utilidades.UsuarioLogin;
import com.sirelab.utilidades.Utilidades;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ANDRES PINEDA
 */
@Named(value = "controllerLogin")
@SessionScoped
public class ControllerLogin implements Serializable {

    //
    @EJB
    GestionarLoginSistemaBOInterface gestionarLoginSistemaBO;
    //
    private String nombreEstudiante, apellidoEstudiante, correoEstudiante, identificacionEstudiante;
    private String telefono1Estudiante, telefono2Estudiante, direccionEstudiante;
    private String userEstudiante, passwordEstudiante;
    private String correoRecuperacion, identificacionRecuperacion;
    private String usuarioLogin, passwordLogin;
    private String paginaSiguiente;
    private Integer semestreEstudiante;
    private Departamento departamentoEstudiante;
    private List<Departamento> listasDepartamentos;
    private Carrera carreraEstudiante;
    private List<Carrera> listaCarreras;
    private PlanEstudios planEstudioEstudiante;
    private List<PlanEstudios> listaPlanesEstudios;
    private boolean activoCarrera, activoPlan;
    //
    private UsuarioLogin usuarioLoginSistema;
    //
    private HttpServletRequest httpServletRequest = null;

    public ControllerLogin() {
    }

    @PostConstruct
    public void init() {
        listasDepartamentos = null;
        departamentoEstudiante = null;
        listaCarreras = null;
        carreraEstudiante = null;
        listaPlanesEstudios = null;
        planEstudioEstudiante = null;
        semestreEstudiante = null;
        activoCarrera = true;
        activoPlan = true;
    }

    public void dispararDialogoLoginUsuario() {
        usuarioLogin = null;
        passwordLogin = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:loginUsuario");
        context.execute("loginUsuario.show()");
    }

    public void dispararDialogoNuevoEstudiante() {
        cancelarRegistroEstudiante();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formularioDialogos:NuevoRegistroEstudiante");
        context.execute("NuevoRegistroEstudiante.show();");
    }

    public void dispararDialogoRecuperarContrasenia() {
        cancelarRecuperarContrasenia();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("loginUsuario.hide()");
        context.execute("recuperarContrasenia.show()");
    }

    public void actualizarDepartamentos() {
        try {
            if (Utilidades.validarNulo(departamentoEstudiante)) {
                activoCarrera = false;
                activoPlan = true;
                listaPlanesEstudios = null;
                planEstudioEstudiante = null;
                carreraEstudiante = null;
                listaCarreras = gestionarLoginSistemaBO.obtenerListasCarrerasPorDepartamento(departamentoEstudiante.getIddepartamento());
            } else {
                activoCarrera = true;
                activoPlan = true;
                listaCarreras = null;
                carreraEstudiante = null;
                listaPlanesEstudios = null;
                planEstudioEstudiante = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:nuevoCarreraEstudiante");
            context.update("formularioDialogos:nuevoPlanEstudioEstudiante");
        } catch (Exception e) {
            System.out.println("Error ControllerLogin actualizarDepartamentos : " + e.toString());
        }
    }

    public void actualizarCarreras() {
        try {
            if (Utilidades.validarNulo(carreraEstudiante)) {
                activoPlan = false;
                planEstudioEstudiante = null;
                listaPlanesEstudios = gestionarLoginSistemaBO.obtenerListasPlanesEstudioPorCarrera(carreraEstudiante.getIdcarrera());
            } else {
                activoPlan = true;
                listaPlanesEstudios = null;
                planEstudioEstudiante = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formularioDialogos:nuevoPlanEstudioEstudiante");
        } catch (Exception e) {
            System.out.println("Error ControllerLogin actualizarDepartamentos : " + e.toString());
        }
    }

    public boolean validarNombreApellidoEstudiante() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nombreEstudiante)) && (Utilidades.validarNulo(apellidoEstudiante))) {
            if (!Utilidades.validarCaracterString(nombreEstudiante)) {
                retorno = false;
            }
            if (!Utilidades.validarCaracterString(apellidoEstudiante)) {
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
            if ((Utilidades.isNumber(telefono1Estudiante)) == false) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(telefono2Estudiante)) {
            if ((Utilidades.isNumber(telefono2Estudiante)) == false) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarDireccionEstudiante() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(direccionEstudiante)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosUniversidadEstudiante() {
        boolean retorno = true;
        if (Utilidades.validarNulo(semestreEstudiante) && Utilidades.validarNulo(departamentoEstudiante) && Utilidades.validarNulo(carreraEstudiante) && Utilidades.validarNulo(planEstudioEstudiante)) {
            if (!Utilidades.isNumber(semestreEstudiante.toString())) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosUserPassEstudiante() {
        boolean retorno = true;
        if (Utilidades.validarNulo(userEstudiante) && Utilidades.validarNulo(passwordEstudiante)) {
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validaEstudianteYaRegistrado() {
        boolean retorno = true;
        Estudiante estudianteRegistrado = gestionarLoginSistemaBO.obtenerEstudiantePorCorreoNumDocumento(correoEstudiante, identificacionEstudiante);
        if (estudianteRegistrado != null) {
            retorno = false;
        }
        return retorno;
    }

    public void registrarNuevoEstudiante() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarNombreApellidoEstudiante() == true) {
            if (validarCorreoEstudiante() == true) {
                if (validarIdentificacionEstudiante() == true) {
                    if (validarDatosNumericosEstudiante() == true) {
                        if (validarDireccionEstudiante() == true) {
                            if (validarDatosUniversidadEstudiante() == true) {
                                if (validarDatosUserPassEstudiante() == true) {
                                    if (validaEstudianteYaRegistrado() == true) {
                                        almacenarNuevoEstudianteEnSistema();
                                    } else {
                                        context.execute("errorEstudianteRegistrado.show()");
                                    }
                                } else {
                                    context.execute("errorUserPassEstudiante.show()");
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
    }

    public void cancelarRegistroEstudiante() {
        userEstudiante = null;
        passwordEstudiante = null;
        nombreEstudiante = null;
        apellidoEstudiante = null;
        identificacionEstudiante = null;
        correoEstudiante = null;
        telefono1Estudiante = null;
        telefono2Estudiante = null;
        direccionEstudiante = null;
        listasDepartamentos = null;
        departamentoEstudiante = null;
        listaCarreras = null;
        carreraEstudiante = null;
        listaPlanesEstudios = null;
        planEstudioEstudiante = null;
        semestreEstudiante = null;
        activoCarrera = true;
        activoPlan = true;
    }

    public void almacenarNuevoEstudianteEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroEstudiante.hide()");
        try {
            Usuario usuarioNuevo = new Usuario();
            usuarioNuevo.setEstado(false);
            usuarioNuevo.setNombreusuario(userEstudiante);
            usuarioNuevo.setPasswordusuario(passwordEstudiante);
            Persona personaNueva = new Persona();
            personaNueva.setApellidospersona(apellidoEstudiante);
            personaNueva.setDireccionpersona(direccionEstudiante);
            personaNueva.setEmailpersona(correoEstudiante);
            personaNueva.setIdentificacionpersona(identificacionEstudiante);
            personaNueva.setNombrespersona(nombreEstudiante);
            personaNueva.setTelefono1persona(telefono1Estudiante);
            personaNueva.setTelefono2persona(telefono2Estudiante);
            Estudiante estudianteNueva = new Estudiante();
            estudianteNueva.setPlanestudio(planEstudioEstudiante);
            estudianteNueva.setSemestreestudiante(semestreEstudiante);
            gestionarLoginSistemaBO.almacenarNuevoEstudianteEnSistema(usuarioNuevo, personaNueva, estudianteNueva);
            context.execute("registroExitosoEstudiante.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerLogin almacenarNuevoEstudianteEnSistema : " + e.toString());
            context.execute("registroFallidoEstudiante.show()");
        }
    }

    public void recuperarContraseñaUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if ((correoRecuperacion != null && (!correoRecuperacion.isEmpty())) && (identificacionRecuperacion != null && (!identificacionRecuperacion.isEmpty()))) {
                boolean validarCorreo = Utilidades.validarCorreoElectronico(correoRecuperacion);
                if (validarCorreo == true) {
                    Persona recuperar = gestionarLoginSistemaBO.obtenerPersonaRecuperarContrasenia(correoRecuperacion, identificacionRecuperacion);
                    correoRecuperacion = null;
                    identificacionRecuperacion = null;
                    context.execute("recuperarContrasenia.hide()");
                    if (recuperar != null) {
                        Persona personaRecuperada = gestionarLoginSistemaBO.configurarContraseñaPersona(recuperar);
                        enviarCorreoRecuperacion(personaRecuperada);
                        context.execute("cambioContraseniaOK.show()");
                    } else {
                        context.execute("errorUsuarioNoExiste.show()");
                    }
                } else {
                    context.execute("errorEmailEstudiante.show()");
                }
            } else {
                context.execute("errorDatosRecuperacion.show()");
            }
        } catch (Exception e) {
            System.out.println("Error recuperarContraseñaUsuario ControllerLogin : " + e.toString());
        }
    }

    public void enviarCorreoRecuperacion(Persona personaRecuperada) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("proyecto.sirelab@gmail.com", "ucentral");
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("proyecto.sirelab@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(personaRecuperada.getEmailpersona()));
            message.setSubject("Recuperación de Contraseña - SIRELAB UC");
            message.setText("Se solicito la recuperación de la contraseña de SIRELAB, la contraseña restaurada es la siguiente: " + personaRecuperada.getUsuario().getPasswordusuario() + " . Se solicita ingresar al sistema y cambiar la contraseña.");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void cancelarRecuperarContrasenia() {
        correoRecuperacion = null;
        identificacionRecuperacion = null;
    }

    public void loginUsuarioPruebas() {
        paginaSiguiente = "menuinicial";
    }

    public void loginUsuario() {
        RequestContext context = RequestContext.getCurrentInstance();
        paginaSiguiente = null;
        try {
            Persona personaLogin = null;
            if ((usuarioLogin != null && (!usuarioLogin.isEmpty())) && (passwordLogin != null && (!passwordLogin.isEmpty()))) {
                personaLogin = gestionarLoginSistemaBO.obtenerPersonaLogin(usuarioLogin, passwordLogin);
                usuarioLogin = null;
                passwordLogin = null;
                context.execute("NuevoRegistroEstudiante.hide();");
                if (personaLogin != null) {
                    String nombreTipoUsuario = personaLogin.getUsuario().getTipousuario().getNombretipousuario();
                    //nombreTipoUsuario = "ADMINISTRADOR";
                    //BigInteger secuenciaPrueba = new BigInteger(String.valueOf(4));
                    usuarioLoginSistema = new UsuarioLogin();
                    if ("ADMINISTRADOR".equals(nombreTipoUsuario)) {
                        usuarioLoginSistema.setNombreTipoUsuario(nombreTipoUsuario);
                        usuarioLoginSistema.setIdUsuarioLogin(personaLogin.getIdpersona());
                        usuarioLoginSistema.setUserUsuario(personaLogin.getUsuario().getNombreusuario());
                        paginaSiguiente = "menuinicial";
                    } else {
                        Object usuarioFinal = gestionarLoginSistemaBO.obtenerUsuarioFinalLogin(nombreTipoUsuario, personaLogin.getIdpersona());
                        if ("ESTUDIANTE".equals(nombreTipoUsuario)) {
                            Estudiante estudianteLogin = (Estudiante) usuarioFinal;
                            usuarioLoginSistema.setNombreTipoUsuario(nombreTipoUsuario);
                            usuarioLoginSistema.setIdUsuarioLogin(estudianteLogin.getIdestudiante());
                            usuarioLoginSistema.setUserUsuario(estudianteLogin.getPersona().getUsuario().getNombreusuario());
                            paginaSiguiente = "menuestudiante";
                        } else {
                            if ("DOCENTE".equals(nombreTipoUsuario)) {
                                Docente docenteLogin = (Docente) usuarioFinal;
                                usuarioLoginSistema.setNombreTipoUsuario(nombreTipoUsuario);
                                usuarioLoginSistema.setIdUsuarioLogin(docenteLogin.getIddocente());
                                usuarioLoginSistema.setUserUsuario(docenteLogin.getPersona().getUsuario().getNombreusuario());
                                paginaSiguiente = "menudocente";
                            } else {
                                if ("ENCARGADOLAB".equals(nombreTipoUsuario)) {
                                    EncargadoLaboratorio encargadoLabLogin = (EncargadoLaboratorio) usuarioFinal;
                                    usuarioLoginSistema.setNombreTipoUsuario(nombreTipoUsuario);
                                    usuarioLoginSistema.setIdUsuarioLogin(encargadoLabLogin.getIdencargadolaboratorio());
                                    usuarioLoginSistema.setUserUsuario(encargadoLabLogin.getPersona().getUsuario().getNombreusuario());
                                    paginaSiguiente = "menuinicial";
                                } else {
                                    EntidadExterna entidadExternaLogin = (EntidadExterna) usuarioFinal;
                                    usuarioLoginSistema.setNombreTipoUsuario(nombreTipoUsuario);
                                    usuarioLoginSistema.setIdUsuarioLogin(entidadExternaLogin.getIdentidadexterna());
                                    usuarioLoginSistema.setUserUsuario(entidadExternaLogin.getPersona().getUsuario().getNombreusuario());
                                    paginaSiguiente = "menuentidad";
                                }
                            }
                        }
                    }
                    FacesContext faceContext = FacesContext.getCurrentInstance();
                    httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
                    httpServletRequest.getSession().setAttribute("sessionUsuario", usuarioLoginSistema);
                } else {
                    System.out.println("Diapra dualogo");
                    context.execute("errorUsuarioNoExiste.show();");
                }
            } else {
                context.execute("errorCredencialesLogin.show();");
            }
        } catch (NoResultException nre) {
            context.execute("NuevoRegistroEstudiante.hide();");
            context.execute("errorUsuarioNoExiste.show();");
            System.out.println("NoResultException loginUsuario ControllerLogin : " + nre.toString());
        } catch (Exception e) {
            System.out.println("Exception loginUsuario ControllerLogin : " + e.toString());
        }
    }

    public void cerrarSession() throws IOException {
        FacesContext x = FacesContext.getCurrentInstance();
        HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
    }

    public String retornarPaginaSiguiente() {
        if (usuarioLoginSistema == null) {
            paginaSiguiente = null;
        }
        return paginaSiguiente;
    }

    //GET-SET
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

    public Departamento getDepartamentoEstudiante() {
        return departamentoEstudiante;
    }

    public void setDepartamentoEstudiante(Departamento departamentoEstudiante) {
        this.departamentoEstudiante = departamentoEstudiante;
    }

    public List<Departamento> getListasDepartamentos() {
        listasDepartamentos = gestionarLoginSistemaBO.obtenerListasDepartamentos();
        return listasDepartamentos;
    }

    public void setListasDepartamentos(List<Departamento> listasDepartamentos) {
        this.listasDepartamentos = listasDepartamentos;
    }

    public Carrera getCarreraEstudiante() {
        return carreraEstudiante;
    }

    public void setCarreraEstudiante(Carrera carreraEstudiante) {
        this.carreraEstudiante = carreraEstudiante;
    }

    public List<Carrera> getListaCarreras() {
        return listaCarreras;
    }

    public void setListaCarreras(List<Carrera> listaCarreras) {
        this.listaCarreras = listaCarreras;
    }

    public PlanEstudios getPlanEstudioEstudiante() {
        return planEstudioEstudiante;
    }

    public void setPlanEstudioEstudiante(PlanEstudios planEstudioEstudiante) {
        this.planEstudioEstudiante = planEstudioEstudiante;
    }

    public List<PlanEstudios> getListaPlanesEstudios() {
        return listaPlanesEstudios;
    }

    public void setListaPlanesEstudios(List<PlanEstudios> listaPlanesEstudios) {
        this.listaPlanesEstudios = listaPlanesEstudios;
    }

    public Integer getSemestreEstudiante() {
        return semestreEstudiante;
    }

    public void setSemestreEstudiante(Integer semestreEstudiante) {
        this.semestreEstudiante = semestreEstudiante;
    }

    public boolean isActivoCarrera() {
        return activoCarrera;
    }

    public void setActivoCarrera(boolean activoCarrera) {
        this.activoCarrera = activoCarrera;
    }

    public boolean isActivoPlan() {
        return activoPlan;
    }

    public void setActivoPlan(boolean activoPlan) {
        this.activoPlan = activoPlan;
    }

    public String getUserEstudiante() {
        return userEstudiante;
    }

    public void setUserEstudiante(String userEstudiante) {
        this.userEstudiante = userEstudiante;
    }

    public String getPasswordEstudiante() {
        return passwordEstudiante;
    }

    public void setPasswordEstudiante(String passwordEstudiante) {
        this.passwordEstudiante = passwordEstudiante;
    }

    public String getCorreoRecuperacion() {
        return correoRecuperacion;
    }

    public void setCorreoRecuperacion(String correoRecuperacion) {
        this.correoRecuperacion = correoRecuperacion;
    }

    public String getIdentificacionRecuperacion() {
        return identificacionRecuperacion;
    }

    public void setIdentificacionRecuperacion(String identificacionRecuperacion) {
        this.identificacionRecuperacion = identificacionRecuperacion;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getPasswordLogin() {
        return passwordLogin;
    }

    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin = passwordLogin;
    }

    public String getPaginaSiguiente() {
        return paginaSiguiente;
    }

    public void setPaginaSiguiente(String paginaSiguiente) {
        this.paginaSiguiente = paginaSiguiente;
    }

}

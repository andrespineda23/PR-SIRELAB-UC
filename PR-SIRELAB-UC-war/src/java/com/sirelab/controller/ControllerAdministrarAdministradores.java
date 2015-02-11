package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarAdministradoresBOInterface;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.Usuario;
import com.sirelab.exporter.ExportarPDFTablasAnchas;
import com.sirelab.exporter.ExportarXLS;
import com.sirelab.utilidades.Utilidades;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ANDRES PINEDA
 */
@ManagedBean
@SessionScoped
public class ControllerAdministrarAdministradores implements Serializable {

    @EJB
    AdministrarAdministradoresBOInterface administrarAdministradoresBO;

    private String parametroNombre, parametroApellido, parametroDocumento, parametroCorreo;
    private boolean parametroEstado;
    private boolean todosAdministradores;
    private Map<String, String> filtros;
    //
    private List<Persona> listaAdministradores;
    private List<Persona> filtrarListaAdministradores;
    //
    private Column numeroIDTabla, nombresTabla, apellidosTabla, correoTabla, estadoTabla;
    //
    private String altoTabla;
    //
    private boolean activarExport;
    //
    private String nuevoNombresAdministrador, nuevoApellidoAdministrador, nuevoCorreoAdministrador, nuevoIdentificacionAdministrador, nuevoNumero1Administrador, nuevoNumero2Administrador, nuevoDireccionAdministrador;
    private String nuevoUserAdministrador, nuevoPassAdministrador;
    //
    private String editarNombre, editarApellido, editarCorreo, editarIdentificacion, editarNumero1, editarNumero2, editarDireccion;
    private Persona administradorEditar;

    public ControllerAdministrarAdministradores() {
    }

    @PostConstruct
    public void init() {
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroEstado = true;
        todosAdministradores = false;
        altoTabla = "150";
        inicializarFiltros();
        listaAdministradores = null;
        filtrarListaAdministradores = null;
        activarExport = true;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroApellido", null);
        filtros.put("parametroDocumento", null);
        filtros.put("parametroCorreo", null);
        filtros.put("parametroEstado", null);
        filtros.put("parametroIDUnico", null);
        filtros.put("parametroEmailUnico", null);
        filtros.put("parametroNombreUnico", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroApellido) == true) && (!parametroApellido.isEmpty())) {
            filtros.put("parametroApellido", parametroApellido.toString());
        }
        if ((Utilidades.validarNulo(parametroDocumento) == true) && (!parametroDocumento.isEmpty())) {
            filtros.put("parametroDocumento", parametroDocumento.toString());
        }
        if ((Utilidades.validarNulo(parametroCorreo) == true) && (!parametroCorreo.isEmpty())) {
            filtros.put("parametroCorreo", parametroCorreo.toString());
        }
        if (todosAdministradores == false) {
            if (parametroEstado == true) {
                filtros.put("parametroEstado", "true");
            } else {
                filtros.put("parametroEstado", "false");
            }
        }
    }

    public void buscarAdministradoresPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaAdministradores = null;
            listaAdministradores = administrarAdministradoresBO.consultarAdministradoresPorParametro(filtros);
            if (listaAdministradores != null) {
                if (listaAdministradores.size() > 0) {
                    activarFiltrosTabla();
                    activarExport = false;
                } else {
                    activarExport = true;
                    context.execute("consultaSinDatos.show();");
                }
            } else {
                activarExport = true;
                context.execute("consultaSinDatos.show();");
            }
            context.update("form:datosBusqueda");
            context.update("form:exportarXLS");
            context.update("form:exportarXML");
            context.update("form:exportarPDF");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarAdministradores buscarAdministradoresPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarExport = true;
        if (null != listaAdministradores) {
            desactivarFiltrosTabla();
        }
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroEstado = true;
        todosAdministradores = false;
        inicializarFiltros();
        listaAdministradores = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void modificacionTodosAdministradores() {
        RequestContext.getCurrentInstance().update("formT:form:parametroEstado");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        numeroIDTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:numeroIDTabla");
        numeroIDTabla.setFilterStyle("width: 80px");
        nombresTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombresTabla");
        nombresTabla.setFilterStyle("width: 80px");
        apellidosTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:apellidosTabla");
        apellidosTabla.setFilterStyle("width: 80px");
        correoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:correoTabla");
        correoTabla.setFilterStyle("width: 80px");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        numeroIDTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:numeroIDTabla");
        numeroIDTabla.setFilterStyle("display: none; visibility: hidden;");
        nombresTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombresTabla");
        nombresTabla.setFilterStyle("display: none; visibility: hidden;");
        apellidosTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:apellidosTabla");
        apellidosTabla.setFilterStyle("display: none; visibility: hidden;");
        correoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:correoTabla");
        correoTabla.setFilterStyle("display: none; visibility: hidden;");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaAdministradores = null;
    }

    public String verDetallesAdministrador() {
        limpiarProcesoBusqueda();
        return "detallesentidadexterna";
    }

    public void dispararDialogoNuevoAdministrador() {
        limpiarRegistroAdministrador();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroAdministrador");
        context.execute("NuevoRegistroAdministrador.show()");
    }

    public void dispararDialogoEditarAdministrador(BigInteger idAdministrador) {
        editarApellido = null;
        editarCorreo = null;
        editarIdentificacion = null;
        editarDireccion = null;
        editarNumero1 = null;
        editarNumero2 = null;
        editarNombre = null;
        cargarInformacionUsuarioEditar(idAdministrador);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroAdministrador");
        context.execute("EditarRegistroAdministrador.show()");
    }

    public void cargarInformacionUsuarioEditar(BigInteger idAdministrador) {
        try {
            administradorEditar = administrarAdministradoresBO.obtenerAdministradorPorIDPersona(idAdministrador);
            if (administradorEditar != null) {
                editarApellido = administradorEditar.getApellidospersona();
                editarCorreo = administradorEditar.getEmailpersona();
                editarIdentificacion = administradorEditar.getIdentificacionpersona();
                editarNombre = administradorEditar.getNombrespersona();
                editarNumero1 = administradorEditar.getTelefono1persona();
                editarNumero2 = administradorEditar.getTelefono2persona();
                editarDireccion = administradorEditar.getDireccionpersona();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarAdministradores cargarInformacionUsuarioEditar : " + e.toString());
        }
    }

    public void limpiarRegistroAdministrador() {
        nuevoApellidoAdministrador = null;
        nuevoCorreoAdministrador = null;
        nuevoDireccionAdministrador = null;
        nuevoIdentificacionAdministrador = null;
        nuevoNombresAdministrador = null;
        nuevoNumero1Administrador = null;
        nuevoNumero2Administrador = null;
        nuevoPassAdministrador = null;
        nuevoUserAdministrador = null;
    }

    public boolean validarNombreApellidoAdministrador(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoNombresAdministrador)) && (Utilidades.validarNulo(nuevoApellidoAdministrador))) {
                if (Utilidades.validarCaracterString(nuevoNombresAdministrador) == false) {
                    retorno = false;
                }
                if (Utilidades.validarCaracterString(nuevoApellidoAdministrador) == false) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        } else {
            if ((Utilidades.validarNulo(editarApellido)) && (Utilidades.validarNulo(editarNombre))) {
                if (Utilidades.validarCaracterString(editarNombre) == false) {
                    retorno = false;
                }
                if (Utilidades.validarCaracterString(editarApellido) == false) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCorreoAdministrador(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoCorreoAdministrador)) {
                if (!Utilidades.validarCorreoElectronico(nuevoCorreoAdministrador)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        } else {
            if (Utilidades.validarNulo(editarCorreo)) {
                if (!Utilidades.validarCorreoElectronico(editarCorreo)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarIdentificacionAdministrador(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (!Utilidades.validarNulo(nuevoIdentificacionAdministrador)) {
                retorno = false;
            }
        } else {
            if (!Utilidades.validarNulo(editarIdentificacion)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarDatosNumericosAdministrador(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoNumero1Administrador)) {
                if ((Utilidades.isNumber(nuevoNumero1Administrador)) == false) {
                    retorno = false;
                }
            }
            if (Utilidades.validarNulo(nuevoNumero2Administrador)) {
                if ((Utilidades.isNumber(nuevoNumero2Administrador)) == false) {
                    retorno = false;
                }
            }
        } else {
            if (Utilidades.validarNulo(editarNumero1)) {
                if ((Utilidades.isNumber(editarNumero1)) == false) {
                    retorno = false;
                }
            }
            if (Utilidades.validarNulo(editarNumero2)) {
                if ((Utilidades.isNumber(editarNumero2)) == false) {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public boolean validarDireccionAdministrador(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoDireccionAdministrador)) {

            }
        } else {
            if (Utilidades.validarNulo(editarDireccion)) {

            }
        }
        return retorno;
    }

    public boolean validaAdministradorYaRegistrado(int tipoRegistro) {
        boolean retorno = true;
        Persona personaRegistrada = null;
        if (tipoRegistro == 0) {
            personaRegistrada = administrarAdministradoresBO.obtenerAdministradorPorCorreoNumDocumento(nuevoCorreoAdministrador, nuevoIdentificacionAdministrador);
            if (personaRegistrada != null) {
                retorno = false;
            }
        } else {
            personaRegistrada = administrarAdministradoresBO.obtenerAdministradorPorCorreoNumDocumento(editarCorreo, editarIdentificacion);
            if (personaRegistrada != null) {
                if (!administradorEditar.getIdpersona().equals(personaRegistrada.getIdpersona())) {
                    retorno = false;
                }
            }
        }

        return retorno;
    }

    public void registrarNuevoAdministrador() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarNombreApellidoAdministrador(0) == true) {
            if (validarCorreoAdministrador(0) == true) {
                if (validarIdentificacionAdministrador(0) == true) {
                    if (validarDatosNumericosAdministrador(0) == true) {
                        if (validarDireccionAdministrador(0) == true) {
                            if (validaAdministradorYaRegistrado(0) == true) {
                                almacenarNuevoAdministradorEnSistema();
                            } else {
                                context.execute("errorAdministradorRegistrado.show()");
                            }
                        } else {
                            context.execute("errorDireccionAdministrador.show()");
                        }
                    } else {
                        context.execute("errorNumerosAdministrador.show()");
                    }
                } else {
                    context.execute("errorDocumentoAdministrador.show()");
                }
            } else {
                context.execute("errorEmailAdministrador.show()");
            }
        } else {
            context.execute("errorNombreApellidoAdministrador.show()");
        }
    }

    public void almacenarNuevoAdministradorEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroAdministrador.hide()");
        try {
            Usuario usuarioNuevo = new Usuario();
            usuarioNuevo.setEstado(true);
            usuarioNuevo.setNombreusuario(nuevoUserAdministrador);
            usuarioNuevo.setPasswordusuario(nuevoPassAdministrador);
            Persona personaNueva = new Persona();
            personaNueva.setApellidospersona(nuevoApellidoAdministrador);
            personaNueva.setDireccionpersona(nuevoDireccionAdministrador);
            personaNueva.setEmailpersona(nuevoCorreoAdministrador);
            personaNueva.setIdentificacionpersona(nuevoIdentificacionAdministrador);
            personaNueva.setNombrespersona(nuevoNombresAdministrador);
            personaNueva.setTelefono1persona(nuevoNumero1Administrador);
            personaNueva.setTelefono2persona(nuevoNumero2Administrador);
            administrarAdministradoresBO.almacenarNuevaPersonaEnSistema(usuarioNuevo, personaNueva);
            context.execute("registroExitosoAdministrador.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarAdministradores almacenarNuevoAdministradorEnSistema : " + e.toString());
            context.execute("registroFallidoAdministrador.show()");
        }
    }

    public void modificarInformacionAdministrador() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarNombreApellidoAdministrador(1) == true) {
            if (validarCorreoAdministrador(1) == true) {
                if (validarIdentificacionAdministrador(1) == true) {
                    if (validarDatosNumericosAdministrador(1) == true) {
                        if (validarDireccionAdministrador(1) == true) {
                            if (validaAdministradorYaRegistrado(1) == true) {
                                almacenarModificacion();
                            } else {
                                context.execute("errorAdministradorRegistrado.show()");
                            }
                        } else {
                            context.execute("errorDireccionAdministrador.show()");
                        }
                    } else {
                        context.execute("errorNumerosAdministrador.show()");
                    }
                } else {
                    context.execute("errorDocumentoAdministrador.show()");
                }
            } else {
                context.execute("errorEmailAdministrador.show()");
            }
        } else {
            context.execute("errorNombreApellidoAdministrador.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroAdministrador.hide()");
        try {
            administradorEditar.setApellidospersona(editarApellido);
            administradorEditar.setDireccionpersona(editarDireccion);
            administradorEditar.setEmailpersona(editarCorreo);
            administradorEditar.setIdentificacionpersona(editarIdentificacion);
            administradorEditar.setNombrespersona(editarNombre);
            administradorEditar.setTelefono1persona(editarNumero1);
            administradorEditar.setTelefono2persona(editarNumero2);
            administrarAdministradoresBO.actualizarInformacionUsuario(administradorEditar.getUsuario());
            administrarAdministradoresBO.actualizarInformacionAdministrador(administradorEditar);
            limpiarEditarAdministrador();
            context.execute("registroExitosoAdministrador.show()");
            limpiarProcesoBusqueda();
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarAdministradores almacenarModificacion : " + e.toString());
            context.execute("registroFallidoAdministrador.show()");
        }
    }

    public void limpiarEditarAdministrador() {
        administradorEditar = null;
        editarApellido = null;
        editarCorreo = null;
        editarIdentificacion = null;
        editarDireccion = null;
        editarNumero1 = null;
        editarNumero2 = null;
        editarNombre = null;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "Administrar_Administrador_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Administrar_Administrador_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    // GET - SET
    public String getParametroNombre() {
        return parametroNombre;
    }

    public void setParametroNombre(String parametroNombre) {
        this.parametroNombre = parametroNombre;
    }

    public String getParametroApellido() {
        return parametroApellido;
    }

    public void setParametroApellido(String parametroApellido) {
        this.parametroApellido = parametroApellido;
    }

    public String getParametroDocumento() {
        return parametroDocumento;
    }

    public void setParametroDocumento(String parametroDocumento) {
        this.parametroDocumento = parametroDocumento;
    }

    public String getParametroCorreo() {
        return parametroCorreo;
    }

    public void setParametroCorreo(String parametroCorreo) {
        this.parametroCorreo = parametroCorreo;
    }

    public boolean isParametroEstado() {
        return parametroEstado;
    }

    public void setParametroEstado(boolean parametroEstado) {
        this.parametroEstado = parametroEstado;
    }

    public boolean isTodosAdministradores() {
        return todosAdministradores;
    }

    public void setTodosAdministradores(boolean todosAdministradores) {
        this.todosAdministradores = todosAdministradores;
    }

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public List<Persona> getListaAdministradores() {
        return listaAdministradores;
    }

    public void setListaAdministradores(List<Persona> listaAdministradores) {
        this.listaAdministradores = listaAdministradores;
    }

    public List<Persona> getFiltrarListaAdministradores() {
        return filtrarListaAdministradores;
    }

    public void setFiltrarListaAdministradores(List<Persona> filtrarListaAdministradores) {
        this.filtrarListaAdministradores = filtrarListaAdministradores;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombresAdministrador() {
        return nuevoNombresAdministrador;
    }

    public void setNuevoNombresAdministrador(String nuevoNombresAdministrador) {
        this.nuevoNombresAdministrador = nuevoNombresAdministrador;
    }

    public String getNuevoApellidoAdministrador() {
        return nuevoApellidoAdministrador;
    }

    public void setNuevoApellidoAdministrador(String nuevoApellidoAdministrador) {
        this.nuevoApellidoAdministrador = nuevoApellidoAdministrador;
    }

    public String getNuevoCorreoAdministrador() {
        return nuevoCorreoAdministrador;
    }

    public void setNuevoCorreoAdministrador(String nuevoCorreoAdministrador) {
        this.nuevoCorreoAdministrador = nuevoCorreoAdministrador;
    }

    public String getNuevoIdentificacionAdministrador() {
        return nuevoIdentificacionAdministrador;
    }

    public void setNuevoIdentificacionAdministrador(String nuevoIdentificacionAdministrador) {
        this.nuevoIdentificacionAdministrador = nuevoIdentificacionAdministrador;
    }

    public String getNuevoNumero1Administrador() {
        return nuevoNumero1Administrador;
    }

    public void setNuevoNumero1Administrador(String nuevoNumero1Administrador) {
        this.nuevoNumero1Administrador = nuevoNumero1Administrador;
    }

    public String getNuevoNumero2Administrador() {
        return nuevoNumero2Administrador;
    }

    public void setNuevoNumero2Administrador(String nuevoNumero2Administrador) {
        this.nuevoNumero2Administrador = nuevoNumero2Administrador;
    }

    public String getNuevoDireccionAdministrador() {
        return nuevoDireccionAdministrador;
    }

    public void setNuevoDireccionAdministrador(String nuevoDireccionAdministrador) {
        this.nuevoDireccionAdministrador = nuevoDireccionAdministrador;
    }

    public String getNuevoUserAdministrador() {
        return nuevoUserAdministrador;
    }

    public void setNuevoUserAdministrador(String nuevoUserAdministrador) {
        this.nuevoUserAdministrador = nuevoUserAdministrador;
    }

    public String getNuevoPassAdministrador() {
        return nuevoPassAdministrador;
    }

    public void setNuevoPassAdministrador(String nuevoPassAdministrador) {
        this.nuevoPassAdministrador = nuevoPassAdministrador;
    }

    public String getEditarNombre() {
        return editarNombre;
    }

    public void setEditarNombre(String editarNombre) {
        this.editarNombre = editarNombre;
    }

    public String getEditarApellido() {
        return editarApellido;
    }

    public void setEditarApellido(String editarApellido) {
        this.editarApellido = editarApellido;
    }

    public String getEditarCorreo() {
        return editarCorreo;
    }

    public void setEditarCorreo(String editarCorreo) {
        this.editarCorreo = editarCorreo;
    }

    public String getEditarIdentificacion() {
        return editarIdentificacion;
    }

    public void setEditarIdentificacion(String editarIdentificacion) {
        this.editarIdentificacion = editarIdentificacion;
    }

    public Persona getAdministradorEditar() {
        return administradorEditar;
    }

    public void setAdministradorEditar(Persona administradorEditar) {
        this.administradorEditar = administradorEditar;
    }

    public String getEditarNumero1() {
        return editarNumero1;
    }

    public void setEditarNumero1(String editarNumero1) {
        this.editarNumero1 = editarNumero1;
    }

    public String getEditarNumero2() {
        return editarNumero2;
    }

    public void setEditarNumero2(String editarNumero2) {
        this.editarNumero2 = editarNumero2;
    }

    public String getEditarDireccion() {
        return editarDireccion;
    }

    public void setEditarDireccion(String editarDireccion) {
        this.editarDireccion = editarDireccion;
    }

    public boolean isActivarExport() {
        return activarExport;
    }

    public void setActivarExport(boolean activarExport) {
        this.activarExport = activarExport;
    }

}

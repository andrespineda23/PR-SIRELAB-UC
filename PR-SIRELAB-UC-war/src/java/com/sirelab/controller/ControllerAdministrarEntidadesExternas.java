package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarEntidadesExternasBOInterface;
import com.sirelab.entidades.EntidadExterna;
import com.sirelab.entidades.Persona;
import com.sirelab.entidades.Usuario;
import com.sirelab.exporter.ExportarPDFTablasAnchas;
import com.sirelab.exporter.ExportarXLS;
import com.sirelab.utilidades.Utilidades;
import java.io.IOException;
import java.io.Serializable;
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
public class ControllerAdministrarEntidadesExternas implements Serializable {

    @EJB
    AdministrarEntidadesExternasBOInterface administrarEntidadesExternasBO;

    private String parametroNombre, parametroApellido, parametroDocumento, parametroCorreo;
    private String parametroNombreUnico, parametroIDUnico, parametroEmailUnico;
    private boolean parametroEstado;
    private boolean todosEntidadesExternas;
    private Map<String, String> filtros;
    //
    private List<EntidadExterna> listaEntidadesExternas;
    private List<EntidadExterna> filtrarListaEntidadesExternas;
    //
    private Column numeroIDTabla, nombresTabla, apellidosTabla, correoTabla, idUnicoTabla, nombreUnicoTabla, emailUnicoTabla, estadoTabla;
    //
    private String altoTabla;
    //
    private boolean activarExport;
    //
    private String nuevoNombresEntidadExterna, nuevoApellidoEntidadExterna, nuevoCorreoEntidadExterna, nuevoIdentificacionEntidadExterna, nuevoNumero1EntidadExterna, nuevoNumero2EntidadExterna, nuevoDireccionEntidadExterna;
    private String nuevoUserEntidadExterna, nuevoPassEntidadExterna;
    private String nuevoIDUnicoEntidadExterna;
    private String nuevoNombreUnicoEntidadExterna;
    private String nuevoEmailUnicoEntidadExterna;

    public ControllerAdministrarEntidadesExternas() {
    }

    @PostConstruct
    public void init() {
        activarExport = true;
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroEstado = true;
        parametroNombreUnico = null;
        parametroEmailUnico = null;
        parametroIDUnico = null;
        todosEntidadesExternas = false;
        altoTabla = "150";
        inicializarFiltros();
        listaEntidadesExternas = null;
        filtrarListaEntidadesExternas = null;
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
        if (todosEntidadesExternas == false) {
            if (parametroEstado == true) {
                filtros.put("parametroEstado", "true");
            } else {
                filtros.put("parametroEstado", "false");
            }
        }
        if ((Utilidades.validarNulo(parametroNombreUnico) == true) && (!parametroNombreUnico.isEmpty())) {
            filtros.put("parametroNombreUnico", parametroNombreUnico.toString());
        }
        if ((Utilidades.validarNulo(parametroEmailUnico) == true) && (!parametroEmailUnico.isEmpty())) {
            filtros.put("parametroEmailUnico", parametroEmailUnico.toString());
        }
        if ((Utilidades.validarNulo(parametroIDUnico) == true) && (!parametroIDUnico.isEmpty())) {
            filtros.put("parametroIDUnico", parametroIDUnico.toString());
        }
    }

    public void buscarEntidadesExternasPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaEntidadesExternas = null;
            listaEntidadesExternas = administrarEntidadesExternasBO.consultarEntidadesExternasPorParametro(filtros);
            if (listaEntidadesExternas != null) {
                if (listaEntidadesExternas.size() > 0) {
                    activarFiltrosTabla();
                    activarExport = false;
                } else {
                    activarExport = true;
                    context.execute("consultaSinDatos.show();");
                }
            } else {
                context.execute("consultaSinDatos.show();");
            }
            context.update("form:datosBusqueda");
            context.update("form:exportarXLS");
            context.update("form:exportarXML");
            context.update("form:exportarPDF");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarEntidadesExternas buscarEntidadesExternasPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        desactivarFiltrosTabla();
        activarExport = true;
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroEmailUnico = null;
        parametroIDUnico = null;
        parametroNombreUnico = null;
        parametroEstado = true;
        todosEntidadesExternas = false;
        inicializarFiltros();
        listaEntidadesExternas = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void modificacionTodosEntidadesExternas() {
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
        nombreUnicoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreUnicoTabla");
        nombreUnicoTabla.setFilterStyle("width: 80px");
        idUnicoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:idUnicoTabla");
        idUnicoTabla.setFilterStyle("width: 80px");
        emailUnicoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:emailUnicoTabla");
        emailUnicoTabla.setFilterStyle("width: 80px");
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
        nombreUnicoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreUnicoTabla");
        nombreUnicoTabla.setFilterStyle("display: none; visibility: hidden;");
        idUnicoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:idUnicoTabla");
        idUnicoTabla.setFilterStyle("display: none; visibility: hidden;");
        emailUnicoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:emailUnicoTabla");
        emailUnicoTabla.setFilterStyle("display: none; visibility: hidden;");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaEntidadesExternas = null;
    }

    public String verDetallesEntidadExterna() {
        limpiarProcesoBusqueda();
        return "detallesentidadexterna";
    }

    public void dispararDialogoNuevoEntidadExterna() {
        limpiarRegistroEntidadExterna();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroEntidadExterna");
        context.execute("NuevoRegistroEntidadExterna.show()");
    }

    public void limpiarRegistroEntidadExterna() {
        nuevoNombreUnicoEntidadExterna = null;
        nuevoApellidoEntidadExterna = null;
        nuevoCorreoEntidadExterna = null;
        nuevoDireccionEntidadExterna = null;
        nuevoIDUnicoEntidadExterna = null;
        nuevoEmailUnicoEntidadExterna = null;
        nuevoIdentificacionEntidadExterna = null;
        nuevoNombresEntidadExterna = null;
        nuevoNumero1EntidadExterna = null;
        nuevoNumero2EntidadExterna = null;
        nuevoPassEntidadExterna = null;
        nuevoUserEntidadExterna = null;
    }

    public boolean validarNombreApellidoEntidadExterna() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nuevoNombresEntidadExterna)) && (Utilidades.validarNulo(nuevoApellidoEntidadExterna))) {
            if (!Utilidades.validarCaracterString(nuevoNombresEntidadExterna)) {
                retorno = false;
            }
            if (!Utilidades.validarCaracterString(nuevoApellidoEntidadExterna)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }

        if (Utilidades.validarNulo(nuevoNombreUnicoEntidadExterna)) {
            if (!Utilidades.validarCaracterString(nuevoNombreUnicoEntidadExterna)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarCorreoEntidadExterna() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoCorreoEntidadExterna)) {
            if (!Utilidades.validarCorreoElectronico(nuevoCorreoEntidadExterna)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        if (Utilidades.validarNulo(nuevoEmailUnicoEntidadExterna)) {
            if (!Utilidades.validarCorreoElectronico(nuevoEmailUnicoEntidadExterna)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarIdentificacionEntidadExterna() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(nuevoIdentificacionEntidadExterna)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(nuevoIDUnicoEntidadExterna)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosEntidadExterna() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoNumero1EntidadExterna)) {
            if ((Utilidades.isNumber(nuevoNumero1EntidadExterna)) == false) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(nuevoNumero2EntidadExterna)) {
            if ((Utilidades.isNumber(nuevoNumero2EntidadExterna)) == false) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarDireccionEntidadExterna() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(nuevoDireccionEntidadExterna)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validaEntidadExternaYaRegistrado() {
        boolean retorno = true;
        EntidadExterna estudianteRegistrado = administrarEntidadesExternasBO.obtenerEntidadExternaPorCorreoNumDocumento(nuevoCorreoEntidadExterna, nuevoIdentificacionEntidadExterna);
        if (estudianteRegistrado != null) {
            retorno = false;
        }
        return retorno;
    }

    public void registrarNuevoEntidadExterna() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarNombreApellidoEntidadExterna() == true) {
            if (validarCorreoEntidadExterna() == true) {
                if (validarIdentificacionEntidadExterna() == true) {
                    if (validarDatosNumericosEntidadExterna() == true) {
                        if (validarDireccionEntidadExterna() == true) {
                            if (validaEntidadExternaYaRegistrado() == true) {
                                almacenarNuevoEntidadExternaEnSistema();
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
    }

    public void almacenarNuevoEntidadExternaEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroEntidadExterna.hide()");
        try {
            Usuario usuarioNuevo = new Usuario();
            usuarioNuevo.setEstado(true);
            usuarioNuevo.setNombreusuario(nuevoUserEntidadExterna);
            usuarioNuevo.setPasswordusuario(nuevoPassEntidadExterna);
            Persona personaNueva = new Persona();
            personaNueva.setApellidospersona(nuevoApellidoEntidadExterna);
            personaNueva.setDireccionpersona(nuevoDireccionEntidadExterna);
            personaNueva.setEmailpersona(nuevoCorreoEntidadExterna);
            personaNueva.setIdentificacionpersona(nuevoIdentificacionEntidadExterna);
            personaNueva.setNombrespersona(nuevoNombresEntidadExterna);
            personaNueva.setTelefono1persona(nuevoNumero1EntidadExterna);
            personaNueva.setTelefono2persona(nuevoNumero2EntidadExterna);
            EntidadExterna entidadNuevo = new EntidadExterna();
            entidadNuevo.setNombreentidad(nuevoNombreUnicoEntidadExterna);
            entidadNuevo.setEmailentidad(nuevoEmailUnicoEntidadExterna);
            entidadNuevo.setNombreentidad(nuevoNombreUnicoEntidadExterna);
            entidadNuevo.setIdentificacionentidad(nuevoIDUnicoEntidadExterna);
            administrarEntidadesExternasBO.almacenarNuevaEntidadExternaEnSistema(usuarioNuevo, personaNueva, entidadNuevo);
            context.execute("registroExitosoEntidadExterna.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerLogin almacenarNuevoEntidadExternaEnSistema : " + e.toString());
            context.execute("registroFallidoEntidadExterna.show()");
        }
    }

    public void modificarIdentificacionEntidadExterna() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean validar = validarIdentificacionEntidadExterna();
        if (validar == true) {
            nuevoUserEntidadExterna = nuevoIdentificacionEntidadExterna;
            nuevoPassEntidadExterna = nuevoIdentificacionEntidadExterna;
        } else {
            nuevoUserEntidadExterna = null;
            nuevoPassEntidadExterna = null;
        }
        context.update("formT:formularioDialogos:nuevoPasswordEntidadExterna");
        context.update("formT:formularioDialogos:nuevoUsuarioEntidadExterna");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "Administrar_Entidades_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Administrar_Entidades_XLS", false, false, "UTF-8", null, null);
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

    public boolean isTodosEntidadesExternas() {
        return todosEntidadesExternas;
    }

    public void setTodosEntidadesExternas(boolean todosEntidadesExternas) {
        this.todosEntidadesExternas = todosEntidadesExternas;
    }

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public List<EntidadExterna> getListaEntidadesExternas() {
        return listaEntidadesExternas;
    }

    public void setListaEntidadesExternas(List<EntidadExterna> listaEntidadesExternas) {
        this.listaEntidadesExternas = listaEntidadesExternas;
    }

    public List<EntidadExterna> getFiltrarListaEntidadesExternas() {
        return filtrarListaEntidadesExternas;
    }

    public void setFiltrarListaEntidadesExternas(List<EntidadExterna> filtrarListaEntidadesExternas) {
        this.filtrarListaEntidadesExternas = filtrarListaEntidadesExternas;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombresEntidadExterna() {
        return nuevoNombresEntidadExterna;
    }

    public void setNuevoNombresEntidadExterna(String nuevoNombresEntidadExterna) {
        this.nuevoNombresEntidadExterna = nuevoNombresEntidadExterna;
    }

    public String getNuevoApellidoEntidadExterna() {
        return nuevoApellidoEntidadExterna;
    }

    public void setNuevoApellidoEntidadExterna(String nuevoApellidoEntidadExterna) {
        this.nuevoApellidoEntidadExterna = nuevoApellidoEntidadExterna;
    }

    public String getNuevoCorreoEntidadExterna() {
        return nuevoCorreoEntidadExterna;
    }

    public void setNuevoCorreoEntidadExterna(String nuevoCorreoEntidadExterna) {
        this.nuevoCorreoEntidadExterna = nuevoCorreoEntidadExterna;
    }

    public String getNuevoIdentificacionEntidadExterna() {
        return nuevoIdentificacionEntidadExterna;
    }

    public void setNuevoIdentificacionEntidadExterna(String nuevoIdentificacionEntidadExterna) {
        this.nuevoIdentificacionEntidadExterna = nuevoIdentificacionEntidadExterna;
    }

    public String getNuevoNumero1EntidadExterna() {
        return nuevoNumero1EntidadExterna;
    }

    public void setNuevoNumero1EntidadExterna(String nuevoNumero1EntidadExterna) {
        this.nuevoNumero1EntidadExterna = nuevoNumero1EntidadExterna;
    }

    public String getNuevoNumero2EntidadExterna() {
        return nuevoNumero2EntidadExterna;
    }

    public void setNuevoNumero2EntidadExterna(String nuevoNumero2EntidadExterna) {
        this.nuevoNumero2EntidadExterna = nuevoNumero2EntidadExterna;
    }

    public String getNuevoDireccionEntidadExterna() {
        return nuevoDireccionEntidadExterna;
    }

    public void setNuevoDireccionEntidadExterna(String nuevoDireccionEntidadExterna) {
        this.nuevoDireccionEntidadExterna = nuevoDireccionEntidadExterna;
    }

    public String getNuevoIDUnicoEntidadExterna() {
        return nuevoIDUnicoEntidadExterna;
    }

    public void setNuevoIDUnicoEntidadExterna(String nuevoIDUnicoEntidadExterna) {
        this.nuevoIDUnicoEntidadExterna = nuevoIDUnicoEntidadExterna;
    }

    public String getNuevoNombreUnicoEntidadExterna() {
        return nuevoNombreUnicoEntidadExterna;
    }

    public void setNuevoNombreUnicoEntidadExterna(String nuevoNombreUnicoEntidadExterna) {
        this.nuevoNombreUnicoEntidadExterna = nuevoNombreUnicoEntidadExterna;
    }

    public String getNuevoUserEntidadExterna() {
        return nuevoUserEntidadExterna;
    }

    public String getParametroNombreUnico() {
        return parametroNombreUnico;
    }

    public void setParametroNombreUnico(String parametroNombreUnico) {
        this.parametroNombreUnico = parametroNombreUnico;
    }

    public String getParametroIDUnico() {
        return parametroIDUnico;
    }

    public void setParametroIDUnico(String parametroIDUnico) {
        this.parametroIDUnico = parametroIDUnico;
    }

    public String getParametroEmailUnico() {
        return parametroEmailUnico;
    }

    public void setParametroEmailUnico(String parametroEmailUnico) {
        this.parametroEmailUnico = parametroEmailUnico;
    }

    public String getNuevoEmailUnicoEntidadExterna() {
        return nuevoEmailUnicoEntidadExterna;
    }

    public void setNuevoEmailUnicoEntidadExterna(String nuevoEmailUnicoEntidadExterna) {
        this.nuevoEmailUnicoEntidadExterna = nuevoEmailUnicoEntidadExterna;
    }

    public void setNuevoUserEntidadExterna(String nuevoUserEntidadExterna) {
        this.nuevoUserEntidadExterna = nuevoUserEntidadExterna;
    }

    public String getNuevoPassEntidadExterna() {
        return nuevoPassEntidadExterna;
    }

    public void setNuevoPassEntidadExterna(String nuevoPassEntidadExterna) {
        this.nuevoPassEntidadExterna = nuevoPassEntidadExterna;
    }

    public boolean isActivarExport() {
        return activarExport;
    }

    public void setActivarExport(boolean activarExport) {
        this.activarExport = activarExport;
    }

}

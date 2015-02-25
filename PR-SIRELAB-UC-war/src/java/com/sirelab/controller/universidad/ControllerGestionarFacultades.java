package com.sirelab.controller.universidad;

import com.sirelab.bo.interfacebo.GestionarFacultadesBOInterface;
import com.sirelab.entidades.Facultad;
import com.sirelab.exporter.ExportarPDF;
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
public class ControllerGestionarFacultades implements Serializable {

    @EJB
    GestionarFacultadesBOInterface gestionarFacultadBO;

    private String parametroNombre, parametroCodigo;
    private Map<String, String> filtros;
    //
    private List<Facultad> listaFacultades;
    private List<Facultad> filtrarListaFacultades;
    //
    private Column nombreTabla, codigoTabla;
    //
    private String altoTabla;
    //
    private boolean activarExport;
    //
    private String nuevoNombreFacultad, nuevoCodigoFacultad;
    //
    private String editarNombre, editarCodigo;
    private Facultad facultadEditar;

    public ControllerGestionarFacultades() {
    }

    @PostConstruct
    public void init() {
        parametroNombre = null;
        parametroCodigo = null;
        altoTabla = "150";
        inicializarFiltros();
        listaFacultades = null;
        filtrarListaFacultades = null;
        activarExport = true;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroCodigo", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroCodigo) == true) && (!parametroCodigo.isEmpty())) {
            filtros.put("parametroCodigo", parametroCodigo.toString());
        }
    }

    public void buscarFacultadesPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaFacultades = null;
            listaFacultades = gestionarFacultadBO.consultarFacultadesPorParametro(filtros);
            if (listaFacultades != null) {
                if (listaFacultades.size() > 0) {
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
            System.out.println("Error ControllerGestionarFacultades buscarFacultadesPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarExport = true;
        if (null != listaFacultades) {
            desactivarFiltrosTabla();
        }
        parametroNombre = null;
        parametroCodigo = null;
        inicializarFiltros();
        listaFacultades = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("width: 80px");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("display: none; visibility: hidden;");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaFacultades = null;
    }

    public String verDetallesFacultad() {
        limpiarProcesoBusqueda();
        return "detallesentidadexterna";
    }

    public void dispararDialogoNuevoFacultad() {
        limpiarRegistroFacultad();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroFacultad");
        context.execute("NuevoRegistroFacultad.show()");
    }

    public void dispararDialogoEditarFacultad(BigInteger idFacultad) {
        editarCodigo = null;
        editarNombre = null;
        cargarInformacionUsuarioEditar(idFacultad);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroFacultad");
        context.execute("EditarRegistroFacultad.show()");
    }

    public void cargarInformacionUsuarioEditar(BigInteger idFacultad) {
        try {
            facultadEditar = gestionarFacultadBO.obtenerFacultadPorIDFacultad(idFacultad);
            if (facultadEditar != null) {
                editarCodigo = facultadEditar.getCodigofacultad();
                editarNombre = facultadEditar.getNombrefacultad();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarFacultades cargarInformacionUsuarioEditar : " + e.toString());
        }
    }

    public void limpiarRegistroFacultad() {
        nuevoCodigoFacultad = null;
        nuevoNombreFacultad = null;
    }

    public boolean validarInformacionFacultad(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoNombreFacultad)) && (Utilidades.validarNulo(nuevoCodigoFacultad))) {
                if (!Utilidades.validarCaracterString(nuevoNombreFacultad)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        } else {
            if ((Utilidades.validarNulo(editarCodigo)) && (Utilidades.validarNulo(editarNombre))) {
                if (!Utilidades.validarCaracterString(editarNombre)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoFacultad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarInformacionFacultad(0) == true) {
            almacenarNuevoFacultadEnSistema();
        } else {
            context.execute("errorInformacionFacultad.show()");
        }
    }

    public void almacenarNuevoFacultadEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroFacultad.hide()");
        try {
            Facultad nuevaFacultad = new Facultad();
            nuevaFacultad.setCodigofacultad(nuevoCodigoFacultad);
            nuevaFacultad.setNombrefacultad(nuevoNombreFacultad);
            gestionarFacultadBO.crearNuevaFacultad(nuevaFacultad);
            context.execute("registroExitosoFacultad.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarFacultades almacenarNuevoFacultadEnSistema : " + e.toString());
            context.execute("registroFallidoFacultad.show()");
        }
    }

    public void modificarInformacionFacultad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarInformacionFacultad(1) == true) {
            almacenarModificacion();
        } else {
            context.execute("errorInformacionFacultad.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroFacultad.hide()");
        try {
            facultadEditar.setCodigofacultad(editarCodigo);
            facultadEditar.setNombrefacultad(editarNombre);
            gestionarFacultadBO.modificarInformacionFacultad(facultadEditar);
            limpiarEditarFacultad();
            context.execute("registroExitosoFacultad.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarFacultades almacenarModificacion : " + e.toString());
            context.execute("registroFallidoFacultad.show()");
        }
    }

    public void limpiarEditarFacultad() {
        facultadEditar = null;
        editarCodigo = null;
        editarNombre = null;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Gestionar_Facultad_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Gestionar_Facultad_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    // GET - SET
    public String getParametroNombre() {
        return parametroNombre;
    }

    public void setParametroNombre(String parametroNombre) {
        this.parametroNombre = parametroNombre;
    }

    public String getParametroCodigo() {
        return parametroCodigo;
    }

    public void setParametroCodigo(String parametroCodigo) {
        this.parametroCodigo = parametroCodigo;
    }

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public List<Facultad> getListaFacultades() {
        return listaFacultades;
    }

    public void setListaFacultades(List<Facultad> listaFacultades) {
        this.listaFacultades = listaFacultades;
    }

    public List<Facultad> getFiltrarListaFacultades() {
        return filtrarListaFacultades;
    }

    public void setFiltrarListaFacultades(List<Facultad> filtrarListaFacultades) {
        this.filtrarListaFacultades = filtrarListaFacultades;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public boolean isActivarExport() {
        return activarExport;
    }

    public void setActivarExport(boolean activarExport) {
        this.activarExport = activarExport;
    }

    public String getNuevoNombreFacultad() {
        return nuevoNombreFacultad;
    }

    public void setNuevoNombreFacultad(String nuevoNombreFacultad) {
        this.nuevoNombreFacultad = nuevoNombreFacultad;
    }

    public String getNuevoCodigoFacultad() {
        return nuevoCodigoFacultad;
    }

    public void setNuevoCodigoFacultad(String nuevoCodigoFacultad) {
        this.nuevoCodigoFacultad = nuevoCodigoFacultad;
    }

    public String getEditarNombre() {
        return editarNombre;
    }

    public void setEditarNombre(String editarNombre) {
        this.editarNombre = editarNombre;
    }

    public String getEditarCodigo() {
        return editarCodigo;
    }

    public void setEditarCodigo(String editarCodigo) {
        this.editarCodigo = editarCodigo;
    }

    public Facultad getFacultadEditar() {
        return facultadEditar;
    }

    public void setFacultadEditar(Facultad facultadEditar) {
        this.facultadEditar = facultadEditar;
    }

}

package com.sirelab.controller.universidad;

import com.sirelab.bo.interfacebo.GestionarEdificiosBOInterface;
import com.sirelab.entidades.Edificio;
import com.sirelab.entidades.Sede;
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
public class ControllerGestionarEdificios implements Serializable {

    @EJB
    GestionarEdificiosBOInterface gestionarEdificiosBO;

    private String parametroDescripcion, parametroDireccion;
    private List<Sede> listaSedes;
    private Sede parametroSede;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<Edificio> listaEdificios;
    private List<Edificio> filtrarListaEdificios;
    //
    private Column descripcionTabla, direccionTabla, sedeTabla;
    //
    private String altoTabla;
    //
    private String nuevoDescripcionEdificio, nuevoDireccionEdificio;
    private Sede nuevoSedeEdificio;
    //
    private Edificio edificioEditar;
    private String editarDescripcion, editarDireccion;
    private Sede editarSede;

    public ControllerGestionarEdificios() {
    }

    @PostConstruct
    public void init() {
        activarExport = true;
        parametroDescripcion = null;
        parametroDireccion = null;
        parametroSede = new Sede();
        listaSedes = gestionarEdificiosBO.consultarSedesRegistradas();
        altoTabla = "150";
        inicializarFiltros();
        listaEdificios = null;
        filtrarListaEdificios = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroDescripcion", null);
        filtros.put("parametroDireccion", null);
        filtros.put("parametroSede", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroDescripcion) == true) && (!parametroDescripcion.isEmpty())) {
            filtros.put("parametroDescripcion", parametroDescripcion.toString());
        }
        if ((Utilidades.validarNulo(parametroDireccion) == true) && (!parametroDireccion.isEmpty())) {
            filtros.put("parametroDireccion", parametroDireccion.toString());
        }
        if (parametroSede.getIdsede() != null) {
            filtros.put("parametroSede", parametroSede.getIdsede().toString());
        }
    }

    public void buscarEdificiosPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaEdificios = null;
            listaEdificios = gestionarEdificiosBO.consultarEdificiosPorParametro(filtros);
            if (listaEdificios != null) {
                if (listaEdificios.size() > 0) {
                    activarExport = false;
                    activarFiltrosTabla();
                } else {
                    activarExport = true;
                    context.execute("consultaSinDatos.show();");
                }
            } else {
                context.execute("consultaSinDatos.show()");
            }
            context.update("form:datosBusqueda");
            context.update("form:exportarXLS");
            context.update("form:exportarXML");
            context.update("form:exportarPDF");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarEdificios buscarEdificiosPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        if (null != listaEdificios) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroDescripcion = null;
        parametroDireccion = null;
        parametroSede = new Sede();
        inicializarFiltros();
        listaEdificios = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        descripcionTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:descripcionTabla");
        descripcionTabla.setFilterStyle("width: 80px");
        direccionTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:direccionTabla");
        direccionTabla.setFilterStyle("width: 80px");
        sedeTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:sedeTabla");
        sedeTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        descripcionTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:descripcionTabla");
        descripcionTabla.setFilterStyle("display: none; visibility: hidden;");
        direccionTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:direccionTabla");
        direccionTabla.setFilterStyle("display: none; visibility: hidden;");
        sedeTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:sedeTabla");
        sedeTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaEdificios = null;
    }

    public String verDetallesEdificio() {
        limpiarProcesoBusqueda();
        return "detallesedificio";
    }

    public void dispararDialogoNuevoEdificio() {
        limpiarRegistroEdificio();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroEdificio");
        context.execute("NuevoRegistroEdificio.show()");
    }

    public void limpiarRegistroEdificio() {
        nuevoDireccionEdificio = null;
        nuevoSedeEdificio = null;
        nuevoDescripcionEdificio = null;
    }

    public boolean validarStringEdificio(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoDescripcionEdificio)) {
                if (!Utilidades.validarCaracterString(nuevoDescripcionEdificio)) {
                    retorno = false;
                }
            }
            if (!Utilidades.validarNulo(nuevoDireccionEdificio)) {
                retorno = false;
            }
        } else {
            if (!Utilidades.validarNulo(editarDireccion)) {
                retorno = false;
            }
            if (Utilidades.validarNulo(editarDescripcion)) {
                if (!Utilidades.validarCaracterString(editarDescripcion)) {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public boolean validarSedeEdificio(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (!Utilidades.validarNulo(nuevoSedeEdificio)) {
                retorno = false;
            }
        } else {
            if (!Utilidades.validarNulo(editarSede)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoEdificio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringEdificio(0) == true) {
            if (validarSedeEdificio(0) == true) {
                almacenarNuevoEdificioEnSistema();
            } else {
                context.execute("errorSedeEdificio.show()");
            }
        } else {
            context.execute("errorInformacionEdificio.show()");
        }
    }

    public void almacenarNuevoEdificioEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroEdificio.hide()");
        try {
            Edificio edificioNuevo = new Edificio();
            edificioNuevo.setDescripcionedificio(nuevoDireccionEdificio);
            edificioNuevo.setDireccion(nuevoDescripcionEdificio);
            edificioNuevo.setSede(nuevoSedeEdificio);
            gestionarEdificiosBO.crearNuevaEdificio(edificioNuevo);
            context.execute("registroExitosoEdificio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerLogin almacenarNuevoEdificioEnSistema : " + e.toString());
            context.execute("registroFallidoEdificio.show()");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "Gestionar_Edificios_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Gestionar_Edificios_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void dispararDialogoEditarEdificio(BigInteger idEdificio) {
        editarDireccion = null;
        editarDescripcion = null;
        editarSede = null;
        cargarInformacionUsuarioEditar(idEdificio);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroEdificio");
        context.execute("EditarRegistroEdificio.show()");
    }

    public void cargarInformacionUsuarioEditar(BigInteger idEdificio) {
        try {
            edificioEditar = gestionarEdificiosBO.obtenerEdificioPorIDEdificio(idEdificio);
            if (edificioEditar != null) {
                editarDireccion = edificioEditar.getDireccion();
                editarDescripcion = edificioEditar.getDescripcionedificio();
                editarSede = edificioEditar.getSede();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarEdificios cargarInformacionUsuarioEditar : " + e.toString());
        }
    }

    public void modificarInformacionEdificio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringEdificio(1) == true) {
            if (validarSedeEdificio(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorSedeEdificio.show()");
            }
        } else {
            context.execute("errorInformacionEdificio.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroEdificio.hide()");
        try {
            edificioEditar.setDescripcionedificio(editarDescripcion);
            edificioEditar.setDireccion(editarDireccion);
            edificioEditar.setSede(editarSede);
            gestionarEdificiosBO.modificarInformacionEdificio(edificioEditar);
            limpiarEditarEdificio();
            context.execute("registroExitosoEdificio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarEdificios almacenarModificacion : " + e.toString());
            context.execute("registroFallidoEdificio.show()");
        }
    }

    public void limpiarEditarEdificio() {
        editarSede = null;
        editarDireccion = null;
        editarDescripcion = null;
    }

    // GET - SET
    public String getParametroDescripcion() {
        return parametroDescripcion;
    }

    public void setParametroDescripcion(String parametroDescripcion) {
        this.parametroDescripcion = parametroDescripcion;
    }

    public String getParametroDireccion() {
        return parametroDireccion;
    }

    public void setParametroDireccion(String parametroDireccion) {
        this.parametroDireccion = parametroDireccion;
    }

    public List<Sede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<Sede> listaSedes) {
        this.listaSedes = listaSedes;
    }

    public Sede getParametroSede() {
        return parametroSede;
    }

    public void setParametroSede(Sede parametroSede) {
        this.parametroSede = parametroSede;
    }

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public boolean isActivarExport() {
        return activarExport;
    }

    public void setActivarExport(boolean activarExport) {
        this.activarExport = activarExport;
    }

    public List<Edificio> getListaEdificios() {
        return listaEdificios;
    }

    public void setListaEdificios(List<Edificio> listaEdificios) {
        this.listaEdificios = listaEdificios;
    }

    public List<Edificio> getFiltrarListaEdificios() {
        return filtrarListaEdificios;
    }

    public void setFiltrarListaEdificios(List<Edificio> filtrarListaEdificios) {
        this.filtrarListaEdificios = filtrarListaEdificios;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoDescripcionEdificio() {
        return nuevoDescripcionEdificio;
    }

    public void setNuevoDescripcionEdificio(String nuevoDescripcionEdificio) {
        this.nuevoDescripcionEdificio = nuevoDescripcionEdificio;
    }

    public String getNuevoDireccionEdificio() {
        return nuevoDireccionEdificio;
    }

    public void setNuevoDireccionEdificio(String nuevoDireccionEdificio) {
        this.nuevoDireccionEdificio = nuevoDireccionEdificio;
    }

    public Sede getNuevoSedeEdificio() {
        return nuevoSedeEdificio;
    }

    public void setNuevoSedeEdificio(Sede nuevoSedeEdificio) {
        this.nuevoSedeEdificio = nuevoSedeEdificio;
    }

    public Edificio getEdificioEditar() {
        return edificioEditar;
    }

    public void setEdificioEditar(Edificio edificioEditar) {
        this.edificioEditar = edificioEditar;
    }

    public String getEditarDescripcion() {
        return editarDescripcion;
    }

    public void setEditarDescripcion(String editarDescripcion) {
        this.editarDescripcion = editarDescripcion;
    }

    public String getEditarDireccion() {
        return editarDireccion;
    }

    public void setEditarDireccion(String editarDireccion) {
        this.editarDireccion = editarDireccion;
    }

    public Sede getEditarSede() {
        return editarSede;
    }

    public void setEditarSede(Sede editarSede) {
        this.editarSede = editarSede;
    }

}

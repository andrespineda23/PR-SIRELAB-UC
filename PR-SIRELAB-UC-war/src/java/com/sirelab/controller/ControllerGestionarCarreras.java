package com.sirelab.controller;

import com.sirelab.bo.interfacebo.GestionarCarrerasBOInterface;
import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Facultad;
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
public class ControllerGestionarCarreras implements Serializable {

    @EJB
    GestionarCarrerasBOInterface gestionarCarrerasBO;

    private String parametroNombre, parametroCodigo;
    private List<Facultad> listaFacultades;
    private Facultad parametroFacultad;
    private List<Departamento> listaDepartamentos;
    private Departamento parametroDepartamento;
    private boolean activarDepartamento;
    private boolean activarNuevoDepartamento;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<Carrera> listaCarreras;
    private List<Carrera> filtrarListaCarreras;
    //
    private Column nombreTabla, facultadTabla, codigoTabla, departamentoTabla;
    //
    private String altoTabla;
    //
    private String nuevoNombreCarrera, nuevoCodigoCarrera;
    private Facultad nuevoFacultadCarrera;
    private Departamento nuevoDepartamentoCarrera;
    //
    private Carrera carreraEditar;
    private String editarNombre, editarCodigo;
    private Facultad editarFacultad;
    private Departamento editarDepartamento;

    public ControllerGestionarCarreras() {
    }

    @PostConstruct
    public void init() {
        activarDepartamento = true;
        activarNuevoDepartamento = true;
        activarExport = true;
        parametroNombre = null;
        parametroCodigo = null;
        parametroFacultad = new Facultad();
        parametroDepartamento = new Departamento();
        listaFacultades = gestionarCarrerasBO.consultarFacultadesRegistradas();
        altoTabla = "150";
        inicializarFiltros();
        listaCarreras = null;
        listaDepartamentos = null;
        filtrarListaCarreras = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroCodigo", null);
        filtros.put("parametroDepartamento", null);
        filtros.put("parametroFacultad", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroCodigo) == true) && (!parametroCodigo.isEmpty())) {
            filtros.put("parametroCodigo", parametroCodigo.toString());
        }
        if (parametroFacultad.getIdfacultad() != null) {
            filtros.put("parametroFacultad", parametroFacultad.getIdfacultad().toString());
        }
        if (parametroDepartamento.getIddepartamento() != null) {
            filtros.put("parametroDepartamento", parametroDepartamento.getIddepartamento().toString());
        }
    }

    public void buscarCarrerasPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaCarreras = null;
            listaCarreras = gestionarCarrerasBO.consultarCarrerasPorParametro(filtros);
            if (listaCarreras != null) {
                if (listaCarreras.size() > 0) {
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
            System.out.println("Error ControllerGestionarCarreras buscarCarrerasPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarDepartamento = true;
        if(null != listaCarreras){
        desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroNombre = null;
        parametroCodigo = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        inicializarFiltros();
        listaCarreras = null;
        listaDepartamentos = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("width: 80px");
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("width: 80px");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("width: 80px");
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("display: none; visibility: hidden;");
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("display: none; visibility: hidden;");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("display: none; visibility: hidden;");
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaCarreras = null;
    }

    public void dispararDialogoNuevoCarrera() {
        limpiarRegistroCarrera();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroCarrera");
        context.execute("NuevoRegistroCarrera.show()");
    }

    public void limpiarRegistroCarrera() {
        nuevoFacultadCarrera = null;
        nuevoDepartamentoCarrera = null;
        nuevoNombreCarrera = null;
        nuevoCodigoCarrera = null;
        activarNuevoDepartamento = true;
    }

    public boolean validarStringCarrera(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoNombreCarrera)) && (Utilidades.validarNulo(nuevoCodigoCarrera))) {
                if (!Utilidades.validarCaracterString(nuevoNombreCarrera)) {
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

    public boolean validarFacultadDepartamentoCarrera(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (!Utilidades.validarNulo(nuevoFacultadCarrera)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoDepartamentoCarrera)) {
                retorno = false;
            }
        } else {
            if (!Utilidades.validarNulo(editarFacultad)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(editarDepartamento)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoCarrera() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringCarrera(0) == true) {
            if (validarFacultadDepartamentoCarrera(0) == true) {
                almacenarNuevoCarreraEnSistema();
            } else {
                context.execute("errorFacultadDepartamentoCarrera.show()");
            }
        } else {
            context.execute("errorInformacionCarrera.show()");
        }
    }

    public void almacenarNuevoCarreraEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroCarrera.hide()");
        try {
            Carrera carreraNuevo = new Carrera();
            carreraNuevo.setNombrecarrera(nuevoNombreCarrera);
            carreraNuevo.setCodigocarrera(nuevoCodigoCarrera);
            carreraNuevo.setDepartamento(nuevoDepartamentoCarrera);
            gestionarCarrerasBO.crearNuevaCarrera(carreraNuevo);
            context.execute("registroExitosoCarrera.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarCarreras almacenarNuevoCarreraEnSistema : " + e.toString());
            context.execute("registroFallidoCarrera.show()");
        }
    }

    public void actualizarFacultades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroFacultad)) {
            parametroDepartamento = new Departamento();
            listaDepartamentos = gestionarCarrerasBO.consultarDepartamentosPorIDFacultad(parametroFacultad.getIdfacultad());
            activarDepartamento = false;
        } else {
            parametroDepartamento = new Departamento();
            listaDepartamentos = null;
            activarDepartamento = true;
        }
        context.update("formT:form:parametroDepartamento");
    }

    public void actualizarNuevoFacultades(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoFacultadCarrera)) {
                nuevoDepartamentoCarrera = null;
                listaDepartamentos = gestionarCarrerasBO.consultarDepartamentosPorIDFacultad(nuevoFacultadCarrera.getIdfacultad());
                activarNuevoDepartamento = false;
            } else {
                nuevoDepartamentoCarrera = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
            }
            context.update("formT:formularioDialogos:nuevoDepartamentoCarrera");
        } else {
            if (Utilidades.validarNulo(editarFacultad)) {
                editarDepartamento = null;
                listaDepartamentos = gestionarCarrerasBO.consultarDepartamentosPorIDFacultad(editarFacultad.getIdfacultad());
                activarNuevoDepartamento = false;
            } else {
                editarDepartamento = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
            }
            context.update("formT:formularioDialogos:editarDepartamentoCarrera");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "Gestionar_Carreras_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Gestionar_Carreras_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void dispararDialogoEditarCarrera(BigInteger idCarrera) {
        editarNombre = null;
        editarDepartamento = null;
        editarCodigo = null;
        editarFacultad = null;
        activarNuevoDepartamento = false;
        cargarInformacionCarreraEditar(idCarrera);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroCarrera");
        context.execute("EditarRegistroCarrera.show()");
    }

    public void cargarInformacionCarreraEditar(BigInteger idCarrera) {
        try {
            carreraEditar = gestionarCarrerasBO.obtenerCarreraPorIDCarrera(idCarrera);
            if (carreraEditar != null) {
                editarNombre = carreraEditar.getNombrecarrera();
                editarFacultad = carreraEditar.getDepartamento().getFacultad();
                editarDepartamento = carreraEditar.getDepartamento();
                editarCodigo = carreraEditar.getCodigocarrera();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarCarreras cargarInformacionCarreraEditar : " + e.toString());
        }
    }

    public void modificarInformacionCarrera() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringCarrera(1) == true) {
            if (validarFacultadDepartamentoCarrera(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorFacultadDepartamentoCarrera.show()");
            }
        } else {
            context.execute("errorInformacionCarrera.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroCarrera.hide()");
        try {
            carreraEditar.setNombrecarrera(editarNombre);
            carreraEditar.setCodigocarrera(editarCodigo);
            carreraEditar.setDepartamento(editarDepartamento);
            gestionarCarrerasBO.modificarInformacionCarrera(carreraEditar);
            limpiarEditarCarrera();
            context.execute("registroExitosoCarrera.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarCarreras almacenarModificacion : " + e.toString());
            context.execute("registroFallidoCarrera.show()");
        }
    }

    public void limpiarEditarCarrera() {
        editarFacultad = null;
        editarNombre = null;
        editarCodigo = null;
        editarDepartamento = null;
        activarNuevoDepartamento = true;
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

    public List<Facultad> getListaFacultades() {
        return listaFacultades;
    }

    public void setListaFacultades(List<Facultad> listaFacultades) {
        this.listaFacultades = listaFacultades;
    }

    public Facultad getParametroFacultad() {
        return parametroFacultad;
    }

    public void setParametroFacultad(Facultad parametroFacultad) {
        this.parametroFacultad = parametroFacultad;
    }

    public List<Departamento> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public Departamento getParametroDepartamento() {
        return parametroDepartamento;
    }

    public void setParametroDepartamento(Departamento parametroDepartamento) {
        this.parametroDepartamento = parametroDepartamento;
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

    public List<Carrera> getListaCarreras() {
        return listaCarreras;
    }

    public void setListaCarreras(List<Carrera> listaCarreras) {
        this.listaCarreras = listaCarreras;
    }

    public List<Carrera> getFiltrarListaCarreras() {
        return filtrarListaCarreras;
    }

    public void setFiltrarListaCarreras(List<Carrera> filtrarListaCarreras) {
        this.filtrarListaCarreras = filtrarListaCarreras;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombreCarrera() {
        return nuevoNombreCarrera;
    }

    public void setNuevoNombreCarrera(String nuevoNombreCarrera) {
        this.nuevoNombreCarrera = nuevoNombreCarrera;
    }

    public String getNuevoCodigoCarrera() {
        return nuevoCodigoCarrera;
    }

    public void setNuevoCodigoCarrera(String nuevoCodigoCarrera) {
        this.nuevoCodigoCarrera = nuevoCodigoCarrera;
    }

    public Facultad getNuevoFacultadCarrera() {
        return nuevoFacultadCarrera;
    }

    public void setNuevoFacultadCarrera(Facultad nuevoFacultadCarrera) {
        this.nuevoFacultadCarrera = nuevoFacultadCarrera;
    }

    public Departamento getNuevoDepartamentoCarrera() {
        return nuevoDepartamentoCarrera;
    }

    public void setNuevoDepartamentoCarrera(Departamento nuevoDepartamentoCarrera) {
        this.nuevoDepartamentoCarrera = nuevoDepartamentoCarrera;
    }

    public Carrera getCarreraEditar() {
        return carreraEditar;
    }

    public void setCarreraEditar(Carrera carreraEditar) {
        this.carreraEditar = carreraEditar;
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

    public Facultad getEditarFacultad() {
        return editarFacultad;
    }

    public void setEditarFacultad(Facultad editarFacultad) {
        this.editarFacultad = editarFacultad;
    }

    public Departamento getEditarDepartamento() {
        return editarDepartamento;
    }

    public void setEditarDepartamento(Departamento editarDepartamento) {
        this.editarDepartamento = editarDepartamento;
    }

    public boolean isActivarDepartamento() {
        return activarDepartamento;
    }

    public void setActivarDepartamento(boolean activarDepartamento) {
        this.activarDepartamento = activarDepartamento;
    }

    public boolean isActivarNuevoDepartamento() {
        return activarNuevoDepartamento;
    }

    public void setActivarNuevoDepartamento(boolean activarNuevoDepartamento) {
        this.activarNuevoDepartamento = activarNuevoDepartamento;
    }

}

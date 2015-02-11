package com.sirelab.controller;

import com.sirelab.bo.interfacebo.GestionarPlanesEstudiosBOInterface;
import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.PlanEstudios;
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
public class ControllerGestionarPlanesEstudios implements Serializable {

    @EJB
    GestionarPlanesEstudiosBOInterface gestionarPlanesEstudiosBO;

    private String parametroNombre, parametroCodigo;
    private List<Facultad> listaFacultades;
    private Facultad parametroFacultad;
    private List<Departamento> listaDepartamentos;
    private Departamento parametroDepartamento;
    private List<Carrera> listaCarreras;
    private Carrera parametroCarrera;
    private boolean activarDepartamento;
    private boolean activarCarrera;
    private boolean activarNuevoDepartamento;
    private boolean activarNuevoCarrera;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<PlanEstudios> listaPlanesEstudios;
    private List<PlanEstudios> filtrarListaPlanesEstudios;
    //
    private Column nombreTabla, codigoTabla, facultadTabla, departamentoTabla, carreraTabla;
    //
    private String altoTabla;
    //
    private String nuevoNombrePlanEstudio, nuevoCodigoPlanEstudio;
    private Facultad nuevoFacultadPlanEstudio;
    private Departamento nuevoDepartamentoPlanEstudio;
    private Carrera nuevoCarreraPlanEstudio;
    //
    private PlanEstudios planEstudioEditar;
    private String editarNombre, editarCodigo;
    private Facultad editarFacultad;
    private Departamento editarDepartamento;
    private Carrera editarCarrera;

    public ControllerGestionarPlanesEstudios() {
    }

    @PostConstruct
    public void init() {
        activarDepartamento = true;
        activarCarrera = true;
        activarNuevoDepartamento = true;
        activarNuevoCarrera = true;
        activarExport = true;
        parametroNombre = null;
        parametroCodigo = null;
        parametroFacultad = new Facultad();
        parametroDepartamento = new Departamento();
        parametroCarrera = new Carrera();
        listaFacultades = gestionarPlanesEstudiosBO.consultarFacultadesRegistradas();
        altoTabla = "150";
        inicializarFiltros();
        listaPlanesEstudios = null;
        listaDepartamentos = null;
        listaCarreras = null;
        filtrarListaPlanesEstudios = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroCodigo", null);
        filtros.put("parametroDepartamento", null);
        filtros.put("parametroCarrera", null);
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
        if (parametroCarrera.getIdcarrera() != null) {
            filtros.put("parametroCarrera", parametroCarrera.getIdcarrera().toString());
        }
    }

    public void buscarPlanesEstudiosPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaPlanesEstudios = null;
            listaPlanesEstudios = gestionarPlanesEstudiosBO.consultarPlanesEstudiosPorParametro(filtros);
            if (listaPlanesEstudios != null) {
                if (listaPlanesEstudios.size() > 0) {
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
            System.out.println("Error ControllerGestionarPlanesEstudios buscarPlanesEstudiosPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarDepartamento = true;
        activarCarrera = true;
        if (null != listaPlanesEstudios) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroNombre = null;
        parametroCodigo = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        parametroCarrera = new Carrera();
        inicializarFiltros();
        listaPlanesEstudios = null;
        listaDepartamentos = null;
        listaCarreras = null;
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
        carreraTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:carreraTabla");
        carreraTabla.setFilterStyle("width: 80px");
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
        carreraTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:carreraTabla");
        carreraTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaPlanesEstudios = null;
    }

    public void dispararDialogoNuevoPlanEstudio() {
        limpiarRegistroPlanEstudio();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroPlanEstudio");
        context.execute("NuevoRegistroPlanEstudio.show()");
    }

    public void limpiarRegistroPlanEstudio() {
        nuevoFacultadPlanEstudio = null;
        nuevoDepartamentoPlanEstudio = null;
        nuevoCarreraPlanEstudio = null;
        nuevoNombrePlanEstudio = null;
        nuevoCodigoPlanEstudio = null;
        activarNuevoDepartamento = true;
        activarNuevoCarrera = true;
    }

    public boolean validarStringPlanEstudio(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoNombrePlanEstudio)) && (Utilidades.validarNulo(nuevoCodigoPlanEstudio))) {
                if (!Utilidades.validarCaracterString(nuevoNombrePlanEstudio)) {
                    retorno = false;
                }
                if (!Utilidades.validarCaracterString(nuevoCodigoPlanEstudio)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        } else {
            if ((Utilidades.validarNulo(editarCodigo)) && (Utilidades.validarNulo(editarNombre))) {
                if (!Utilidades.validarCaracterString(editarCodigo)) {
                    retorno = false;
                }
                if (!Utilidades.validarCaracterString(editarNombre)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarEstructuraPlanEstudio(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (!Utilidades.validarNulo(nuevoFacultadPlanEstudio)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoDepartamentoPlanEstudio)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoCarreraPlanEstudio)) {
                retorno = false;
            }
        } else {
            if (!Utilidades.validarNulo(editarFacultad)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(editarDepartamento)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(editarCarrera)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoPlanEstudio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringPlanEstudio(0) == true) {
            if (validarEstructuraPlanEstudio(0) == true) {
                almacenarNuevoPlanEstudioEnSistema();
            } else {
                context.execute("errorEstructuraPlanEstudio.show()");
            }
        } else {
            context.execute("errorInformacionPlanEstudio.show()");
        }
    }

    public void almacenarNuevoPlanEstudioEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroPlanEstudio.hide()");
        try {
            PlanEstudios planNuevo = new PlanEstudios();
            planNuevo.setNombreplanestudio(nuevoNombrePlanEstudio);
            planNuevo.setCodigoplanestudio(nuevoCodigoPlanEstudio);
            planNuevo.setCarrera(nuevoCarreraPlanEstudio);
            gestionarPlanesEstudiosBO.crearNuevoPlanEstudio(planNuevo);
            context.execute("registroExitosoPlanEstudio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlanesEstudios almacenarNuevoPlanEstudioEnSistema : " + e.toString());
            context.execute("registroFallidoPlanEstudio.show()");
        }
    }

    public void actualizarFacultades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroFacultad)) {
            parametroDepartamento = new Departamento();
            listaDepartamentos = gestionarPlanesEstudiosBO.consultarDepartamentosPorIDFacultad(parametroFacultad.getIdfacultad());
            activarDepartamento = false;
            activarCarrera = true;
            listaCarreras = null;
            parametroCarrera = new Carrera();
        } else {
            parametroDepartamento = new Departamento();
            listaDepartamentos = null;
            activarDepartamento = true;
            activarCarrera = true;
            listaCarreras = null;
            parametroCarrera = new Carrera();
        }
        context.update("formT:form:parametroDepartamento");
        context.update("formT:form:parametroCarrera");
    }

    public void actualizarDepartamentos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroDepartamento)) {
            parametroCarrera = new Carrera();
            listaCarreras = gestionarPlanesEstudiosBO.consultarCarrerasPorIDDepartamento(parametroDepartamento.getIddepartamento());
            activarCarrera = false;

        } else {
            activarCarrera = true;
            listaCarreras = null;
            parametroCarrera = new Carrera();
        }
        context.update("formT:form:parametroCarrera");
    }

    public void actualizarNuevoFacultades(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoFacultadPlanEstudio)) {
                nuevoDepartamentoPlanEstudio = null;
                listaDepartamentos = gestionarPlanesEstudiosBO.consultarDepartamentosPorIDFacultad(nuevoFacultadPlanEstudio.getIdfacultad());
                activarNuevoDepartamento = false;
                nuevoCarreraPlanEstudio = null;
                activarNuevoCarrera = true;
                listaCarreras = null;
            } else {
                nuevoDepartamentoPlanEstudio = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
                nuevoCarreraPlanEstudio = null;
                activarNuevoCarrera = true;
                listaCarreras = null;
            }
            context.update("formT:formularioDialogos:nuevoDepartamentoPlanEstudio");
            context.update("formT:formularioDialogos:nuevoCarreraPlanEstudio");
        } else {
            if (Utilidades.validarNulo(editarFacultad)) {
                editarDepartamento = null;
                listaDepartamentos = gestionarPlanesEstudiosBO.consultarDepartamentosPorIDFacultad(editarFacultad.getIdfacultad());
                activarNuevoDepartamento = false;
                editarCarrera = null;
                activarNuevoCarrera = true;
                listaCarreras = null;
            } else {
                editarDepartamento = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
                editarCarrera = null;
                activarNuevoCarrera = true;
                listaCarreras = null;
            }
            context.update("formT:formularioDialogos:editarDepartamentoPlanEstudio");
            context.update("formT:formularioDialogos:editarCarreraPlanEstudio");
        }
    }

    public void actualizarNuevoDepartamentos(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoDepartamentoPlanEstudio)) {
                nuevoCarreraPlanEstudio = null;
                listaCarreras = gestionarPlanesEstudiosBO.consultarCarrerasPorIDDepartamento(nuevoDepartamentoPlanEstudio.getIddepartamento());
                activarNuevoCarrera = false;
            } else {
                nuevoCarreraPlanEstudio = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
            }
            context.update("formT:formularioDialogos:nuevoCarreraPlanEstudio");
        } else {
            if (Utilidades.validarNulo(editarDepartamento)) {
                editarCarrera = null;
                listaCarreras = gestionarPlanesEstudiosBO.consultarCarrerasPorIDDepartamento(editarDepartamento.getIddepartamento());
                activarNuevoCarrera = false;
            } else {
                editarCarrera = null;
                listaCarreras = null;
                activarNuevoCarrera = true;
            }
            context.update("formT:formularioDialogos:editarCarreraPlanEstudio");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "Gestionar_PlanesEstudios_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Gestionar_PlanesEstudios_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void dispararDialogoEditarPlanEstudio(BigInteger idPlan) {
        editarNombre = null;
        editarDepartamento = null;
        editarCodigo = null;
        editarCarrera = null;
        editarFacultad = null;
        activarNuevoDepartamento = true;
        activarNuevoCarrera = true;
        cargarInformacionPlanEditar(idPlan);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroPlanEstudio");
        context.execute("EditarRegistroPlanEstudio.show()");
    }

    public void cargarInformacionPlanEditar(BigInteger idPlan) {
        try {
            planEstudioEditar = gestionarPlanesEstudiosBO.obtenerPlanEstudiosPorIDCarrera(idPlan);
            if (planEstudioEditar != null) {
                editarNombre = planEstudioEditar.getNombreplanestudio();
                editarFacultad = planEstudioEditar.getCarrera().getDepartamento().getFacultad();
                editarDepartamento = planEstudioEditar.getCarrera().getDepartamento();
                editarCarrera = planEstudioEditar.getCarrera();
                editarCodigo = planEstudioEditar.getCodigoplanestudio();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlanesEstudios cargarInformacionPlanEditar : " + e.toString());
        }
    }

    public void modificarInformacionPlanEstudio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringPlanEstudio(1) == true) {
            if (validarEstructuraPlanEstudio(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorEstructuraPlanEstudio.show()");
            }
        } else {
            context.execute("errorInformacionPlanEstudio.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroPlanEstudio.hide()");
        try {
            planEstudioEditar.setCodigoplanestudio(editarCodigo);
            planEstudioEditar.setNombreplanestudio(editarNombre);
            planEstudioEditar.setCarrera(editarCarrera);
            gestionarPlanesEstudiosBO.modificarInformacionPlanEstudios(planEstudioEditar);
            limpiarEditarPlanEstudio();
            context.execute("registroExitosoPlanEstudio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlanesEstudios almacenarModificacion : " + e.toString());
            context.execute("registroFallidoPlanEstudio.show()");
        }
    }

    public void limpiarEditarPlanEstudio() {
        editarFacultad = null;
        editarNombre = null;
        editarCodigo = null;
        editarDepartamento = null;
        editarCarrera = null;
        activarNuevoDepartamento = true;
        activarNuevoCarrera = true;
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

    public List<Carrera> getListaCarreras() {
        return listaCarreras;
    }

    public void setListaCarreras(List<Carrera> listaCarreras) {
        this.listaCarreras = listaCarreras;
    }

    public Carrera getParametroCarrera() {
        return parametroCarrera;
    }

    public void setParametroCarrera(Carrera parametroCarrera) {
        this.parametroCarrera = parametroCarrera;
    }

    public boolean isActivarDepartamento() {
        return activarDepartamento;
    }

    public void setActivarDepartamento(boolean activarDepartamento) {
        this.activarDepartamento = activarDepartamento;
    }

    public boolean isActivarCarrera() {
        return activarCarrera;
    }

    public void setActivarCarrera(boolean activarCarrera) {
        this.activarCarrera = activarCarrera;
    }

    public boolean isActivarNuevoDepartamento() {
        return activarNuevoDepartamento;
    }

    public void setActivarNuevoDepartamento(boolean activarNuevoDepartamento) {
        this.activarNuevoDepartamento = activarNuevoDepartamento;
    }

    public boolean isActivarNuevoCarrera() {
        return activarNuevoCarrera;
    }

    public void setActivarNuevoCarrera(boolean activarNuevoCarrera) {
        this.activarNuevoCarrera = activarNuevoCarrera;
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

    public List<PlanEstudios> getListaPlanesEstudios() {
        return listaPlanesEstudios;
    }

    public void setListaPlanesEstudios(List<PlanEstudios> listaPlanesEstudios) {
        this.listaPlanesEstudios = listaPlanesEstudios;
    }

    public List<PlanEstudios> getFiltrarListaPlanesEstudios() {
        return filtrarListaPlanesEstudios;
    }

    public void setFiltrarListaPlanesEstudios(List<PlanEstudios> filtrarListaPlanesEstudios) {
        this.filtrarListaPlanesEstudios = filtrarListaPlanesEstudios;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombrePlanEstudio() {
        return nuevoNombrePlanEstudio;
    }

    public void setNuevoNombrePlanEstudio(String nuevoNombrePlanEstudio) {
        this.nuevoNombrePlanEstudio = nuevoNombrePlanEstudio;
    }

    public String getNuevoCodigoPlanEstudio() {
        return nuevoCodigoPlanEstudio;
    }

    public void setNuevoCodigoPlanEstudio(String nuevoCodigoPlanEstudio) {
        this.nuevoCodigoPlanEstudio = nuevoCodigoPlanEstudio;
    }

    public Facultad getNuevoFacultadPlanEstudio() {
        return nuevoFacultadPlanEstudio;
    }

    public void setNuevoFacultadPlanEstudio(Facultad nuevoFacultadPlanEstudio) {
        this.nuevoFacultadPlanEstudio = nuevoFacultadPlanEstudio;
    }

    public Departamento getNuevoDepartamentoPlanEstudio() {
        return nuevoDepartamentoPlanEstudio;
    }

    public void setNuevoDepartamentoPlanEstudio(Departamento nuevoDepartamentoPlanEstudio) {
        this.nuevoDepartamentoPlanEstudio = nuevoDepartamentoPlanEstudio;
    }

    public Carrera getNuevoCarreraPlanEstudio() {
        return nuevoCarreraPlanEstudio;
    }

    public void setNuevoCarreraPlanEstudio(Carrera nuevoCarreraPlanEstudio) {
        this.nuevoCarreraPlanEstudio = nuevoCarreraPlanEstudio;
    }

    public PlanEstudios getPlanEstudioEditar() {
        return planEstudioEditar;
    }

    public void setPlanEstudioEditar(PlanEstudios planEstudioEditar) {
        this.planEstudioEditar = planEstudioEditar;
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

    public Carrera getEditarCarrera() {
        return editarCarrera;
    }

    public void setEditarCarrera(Carrera editarCarrera) {
        this.editarCarrera = editarCarrera;
    }

}

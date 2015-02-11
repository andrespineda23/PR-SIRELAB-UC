package com.sirelab.controller;

import com.sirelab.bo.interfacebo.GestionarAsignaturasBOInterface;
import com.sirelab.entidades.Asignatura;
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
public class ControllerGestionarAsignaturas implements Serializable {

    @EJB
    GestionarAsignaturasBOInterface gestionarAsignaturasBO;

    private String parametroNombre, parametroCreditos;
    private List<Facultad> listaFacultades;
    private Facultad parametroFacultad;
    private List<Departamento> listaDepartamentos;
    private Departamento parametroDepartamento;
    private List<Carrera> listaCarreras;
    private Carrera parametroCarrera;
    private List<PlanEstudios> listaPlanesEstudios;
    private PlanEstudios parametroPlanEstudio;
    private boolean activarDepartamento;
    private boolean activarCarrera;
    private boolean activarPlanEstudio;
    private boolean activarNuevoDepartamento;
    private boolean activarNuevoCarrera;
    private boolean activarNuevoPlanEstudio;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<Asignatura> listaAsignaturas;
    private List<Asignatura> filtrarListaAsignaturas;
    //
    private Column nombreTabla, creditosTabla, facultadTabla, departamentoTabla, carreraTabla, planEstudioTabla;
    //
    private String altoTabla;
    //
    private String nuevoNombreAsignatura, nuevoCreditoAsignatura;
    private Facultad nuevoFacultadAsignatura;
    private Departamento nuevoDepartamentoAsignatura;
    private Carrera nuevoCarreraAsignatura;
    private PlanEstudios nuevoPlanEstudioAsignatura;
    //
    private Asignatura asignaturaEditar;
    private String editarNombre, editarCreditos;
    private Facultad editarFacultad;
    private Departamento editarDepartamento;
    private Carrera editarCarrera;
    private PlanEstudios editarPlanEstudio;

    public ControllerGestionarAsignaturas() {
    }

    @PostConstruct
    public void init() {
        activarDepartamento = true;
        activarCarrera = true;
        activarPlanEstudio = true;
        activarNuevoDepartamento = true;
        activarNuevoCarrera = true;
        activarNuevoPlanEstudio = true;
        activarExport = true;
        parametroNombre = null;
        parametroCreditos = null;
        parametroFacultad = new Facultad();
        parametroDepartamento = new Departamento();
        parametroCarrera = new Carrera();
        parametroPlanEstudio = new PlanEstudios();
        listaFacultades = gestionarAsignaturasBO.consultarFacultadesRegistradas();
        altoTabla = "150";
        inicializarFiltros();
        listaPlanesEstudios = null;
        listaAsignaturas = null;
        listaDepartamentos = null;
        listaCarreras = null;
        filtrarListaAsignaturas = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroCreditos", null);
        filtros.put("parametroDepartamento", null);
        filtros.put("parametroPlanEstudio", null);
        filtros.put("parametroCarrera", null);
        filtros.put("parametroFacultad", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroCreditos) == true) && (!parametroCreditos.isEmpty())) {
            filtros.put("parametroCreditos", parametroCreditos.toString());
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
        if (parametroPlanEstudio.getIdplanestudios() != null) {
            filtros.put("parametroPlanEstudio", parametroPlanEstudio.getIdplanestudios().toString());
        }
    }

    public void buscarAsignaturasPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaAsignaturas = null;
            listaAsignaturas = gestionarAsignaturasBO.consultarAsignaturasPorParametro(filtros);
            if (listaAsignaturas != null) {
                if (listaAsignaturas.size() > 0) {
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
            System.out.println("Error ControllerGestionarAsignaturas buscarAsignaturasPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarDepartamento = true;
        activarCarrera = true;
        activarPlanEstudio = true;
        if (null != listaAsignaturas) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroNombre = null;
        parametroCreditos = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        parametroCarrera = new Carrera();
        parametroPlanEstudio = new PlanEstudios();
        inicializarFiltros();
        listaAsignaturas = null;
        listaDepartamentos = null;
        listaCarreras = null;
        listaPlanesEstudios = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("width: 80px");
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("width: 80px");
        creditosTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:creditosTabla");
        creditosTabla.setFilterStyle("width: 80px");
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("width: 80px");
        carreraTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:carreraTabla");
        carreraTabla.setFilterStyle("width: 80px");
        planEstudioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:planEstudioTabla");
        planEstudioTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("display: none; visibility: hidden;");
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("display: none; visibility: hidden;");
        creditosTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:creditosTabla");
        creditosTabla.setFilterStyle("display: none; visibility: hidden;");
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("display: none; visibility: hidden;");
        carreraTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:carreraTabla");
        carreraTabla.setFilterStyle("display: none; visibility: hidden;");
        planEstudioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:planEstudioTabla");
        planEstudioTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaAsignaturas = null;
    }

    public void dispararDialogoNuevAsignatura() {
        limpiarRegistroAsignatura();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroAsignatura");
        context.execute("NuevoRegistroAsignatura.show()");
    }

    public void limpiarRegistroAsignatura() {
        nuevoFacultadAsignatura = null;
        nuevoPlanEstudioAsignatura = null;
        nuevoDepartamentoAsignatura = null;
        nuevoCarreraAsignatura = null;
        nuevoNombreAsignatura = null;
        nuevoCreditoAsignatura = null;
        activarNuevoDepartamento = true;
        activarNuevoCarrera = true;
        activarNuevoPlanEstudio = true;
    }

    public boolean validarStringAsignatura(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoNombreAsignatura)) && (Utilidades.validarNulo(nuevoCreditoAsignatura))) {
                if (!Utilidades.validarCaracterString(nuevoNombreAsignatura)) {
                    retorno = false;
                }
                if (!Utilidades.isNumber(nuevoCreditoAsignatura)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        } else {
            if ((Utilidades.validarNulo(editarCreditos)) && (Utilidades.validarNulo(editarNombre))) {
                if (!Utilidades.isNumber(editarCreditos)) {
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

    public boolean validarEstructuraAsignatura(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (!Utilidades.validarNulo(nuevoFacultadAsignatura)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoDepartamentoAsignatura)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoCarreraAsignatura)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoPlanEstudioAsignatura)) {
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
            if (!Utilidades.validarNulo(editarPlanEstudio)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoAsignatura() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringAsignatura(0) == true) {
            if (validarEstructuraAsignatura(0) == true) {
                almacenarNuevoAsignaturaEnSistema();
            } else {
                context.execute("errorEstructuraAsignatura.show()");
            }
        } else {
            context.execute("errorInformacionAsignatura.show()");
        }
    }

    public void almacenarNuevoAsignaturaEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroAsignatura.hide()");
        try {
            Asignatura asignaturaNueva = new Asignatura();
            asignaturaNueva.setNombreasignatura(nuevoNombreAsignatura);
            Integer creditos = Integer.valueOf(nuevoCreditoAsignatura);
            asignaturaNueva.setNumerocreditos(creditos.intValue());
            asignaturaNueva.setPlanestudios(nuevoPlanEstudioAsignatura);
            gestionarAsignaturasBO.crearNuevoAsignatura(asignaturaNueva);
            context.execute("registroExitosoAsignatura.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarAsignaturas almacenarNuevoAsignaturaEnSistema : " + e.toString());
            context.execute("registroFallidoAsignatura.show()");
        }
    }

    public void actualizarFacultades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroFacultad)) {
            parametroDepartamento = new Departamento();
            listaDepartamentos = gestionarAsignaturasBO.consultarDepartamentosPorIDFacultad(parametroFacultad.getIdfacultad());
            activarDepartamento = false;
            activarCarrera = true;
            listaCarreras = null;
            parametroCarrera = new Carrera();
            listaPlanesEstudios = null;
            parametroPlanEstudio = new PlanEstudios();
            activarPlanEstudio = true;
        } else {
            parametroDepartamento = new Departamento();
            listaDepartamentos = null;
            activarDepartamento = true;
            activarCarrera = true;
            listaCarreras = null;
            parametroCarrera = new Carrera();
            listaPlanesEstudios = null;
            parametroPlanEstudio = new PlanEstudios();
            activarPlanEstudio = true;
        }
        context.update("formT:form:parametroDepartamento");
        context.update("formT:form:parametroCarrera");
        context.update("formT:form:parametroPlanEstudio");
    }

    public void actualizarDepartamentos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroDepartamento)) {
            parametroCarrera = new Carrera();
            listaCarreras = gestionarAsignaturasBO.consultarCarrerasPorIDDepartamento(parametroDepartamento.getIddepartamento());
            activarCarrera = false;
            listaPlanesEstudios = null;
            parametroPlanEstudio = new PlanEstudios();
            activarPlanEstudio = true;
        } else {
            activarCarrera = true;
            listaCarreras = null;
            parametroCarrera = new Carrera();
            listaPlanesEstudios = null;
            parametroPlanEstudio = new PlanEstudios();
            activarPlanEstudio = true;
        }
        context.update("formT:form:parametroCarrera");
        context.update("formT:form:parametroPlanEstudio");
    }

    public void actualizarCarreras() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroCarrera)) {
            parametroPlanEstudio = new PlanEstudios();
            listaPlanesEstudios = gestionarAsignaturasBO.consultarPlanesEstudiosPorIDCarrera(parametroCarrera.getIdcarrera());
            activarPlanEstudio = false;
        } else {
            listaPlanesEstudios = null;
            parametroPlanEstudio = new PlanEstudios();
            activarPlanEstudio = true;
        }
        context.update("formT:form:parametroPlanEstudio");
    }

    public void actualizarNuevoFacultades(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoFacultadAsignatura)) {
                nuevoDepartamentoAsignatura = null;
                listaDepartamentos = gestionarAsignaturasBO.consultarDepartamentosPorIDFacultad(nuevoFacultadAsignatura.getIdfacultad());
                activarNuevoDepartamento = false;
                nuevoCarreraAsignatura = null;
                activarNuevoCarrera = true;
                listaCarreras = null;
                listaPlanesEstudios = null;
                nuevoPlanEstudioAsignatura = null;
                activarNuevoPlanEstudio = true;
            } else {
                nuevoDepartamentoAsignatura = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
                nuevoCarreraAsignatura = null;
                activarNuevoCarrera = true;
                listaCarreras = null;
                listaPlanesEstudios = null;
                nuevoPlanEstudioAsignatura = null;
                activarNuevoPlanEstudio = true;
            }
            context.update("formT:formularioDialogos:nuevoDepartamentoAsignatura");
            context.update("formT:formularioDialogos:nuevoCarreraAsignatura");
            context.update("formT:formularioDialogos:nuevoPlanEstudioAsignatura");
        } else {
            if (Utilidades.validarNulo(editarFacultad)) {
                editarDepartamento = null;
                listaDepartamentos = gestionarAsignaturasBO.consultarDepartamentosPorIDFacultad(editarFacultad.getIdfacultad());
                activarNuevoDepartamento = false;
                editarCarrera = null;
                activarNuevoCarrera = true;
                listaCarreras = null;
                listaPlanesEstudios = null;
                editarPlanEstudio = null;
                activarNuevoPlanEstudio = true;
            } else {
                editarDepartamento = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
                editarCarrera = null;
                activarNuevoCarrera = true;
                listaCarreras = null;
                listaPlanesEstudios = null;
                editarPlanEstudio = null;
                activarNuevoPlanEstudio = true;
            }
            context.update("formT:formularioDialogos:editarDepartamentoAsignatura");
            context.update("formT:formularioDialogos:editarCarreraAsignatura");
            context.update("formT:formularioDialogos:editarPlanEstudioAsignatura");
        }
    }

    public void actualizarNuevoDepartamentos(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoDepartamentoAsignatura)) {
                nuevoCarreraAsignatura = null;
                listaCarreras = gestionarAsignaturasBO.consultarCarrerasPorIDDepartamento(nuevoDepartamentoAsignatura.getIddepartamento());
                activarNuevoCarrera = false;
                listaPlanesEstudios = null;
                nuevoPlanEstudioAsignatura = null;
                activarNuevoPlanEstudio = true;
            } else {
                nuevoCarreraAsignatura = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
                listaPlanesEstudios = null;
                nuevoPlanEstudioAsignatura = null;
                activarNuevoPlanEstudio = true;
            }
            context.update("formT:formularioDialogos:nuevoCarreraAsignatura");
            context.update("formT:formularioDialogos:nuevoPlanEstudioAsignatura");
        } else {
            if (Utilidades.validarNulo(editarDepartamento)) {
                editarCarrera = null;
                listaCarreras = gestionarAsignaturasBO.consultarCarrerasPorIDDepartamento(editarDepartamento.getIddepartamento());
                activarNuevoCarrera = false;
                listaPlanesEstudios = null;
                editarPlanEstudio = null;
                activarNuevoPlanEstudio = true;
            } else {
                editarCarrera = null;
                listaCarreras = null;
                activarNuevoCarrera = true;
                listaPlanesEstudios = null;
                editarPlanEstudio = null;
                activarNuevoPlanEstudio = true;
            }
            context.update("formT:formularioDialogos:editarCarreraAsignatura");
            context.update("formT:formularioDialogos:editarPlanEstudioAsignatura");
        }
    }

    public void actualizarNuevoCarreras(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoCarreraAsignatura)) {
                nuevoPlanEstudioAsignatura = null;
                listaPlanesEstudios = gestionarAsignaturasBO.consultarPlanesEstudiosPorIDCarrera(nuevoCarreraAsignatura.getIdcarrera());
                activarNuevoPlanEstudio = false;
            } else {
                listaPlanesEstudios = null;
                nuevoPlanEstudioAsignatura = null;
                activarNuevoPlanEstudio = true;
            }
            context.update("formT:formularioDialogos:nuevoPlanEstudioAsignatura");
        } else {
            if (Utilidades.validarNulo(editarCarrera)) {
                editarPlanEstudio = null;
                listaPlanesEstudios = gestionarAsignaturasBO.consultarPlanesEstudiosPorIDCarrera(editarCarrera.getIdcarrera());
                activarNuevoPlanEstudio = false;
            } else {
                listaPlanesEstudios = null;
                editarPlanEstudio = null;
                activarNuevoPlanEstudio = true;
            }
            context.update("formT:formularioDialogos:editarPlanEstudioAsignatura");
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

    public void dispararDialogoEditarPlanEstudio(BigInteger idAsignatura) {
        editarNombre = null;
        editarPlanEstudio = null;
        editarDepartamento = null;
        editarCreditos = null;
        editarCarrera = null;
        editarFacultad = null;
        activarNuevoDepartamento = true;
        activarNuevoCarrera = true;
        activarNuevoPlanEstudio = true;
        cargarInformacionAsignaturaEditar(idAsignatura);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroAsignatura");
        context.execute("EditarRegistroAsignatura.show()");
    }

    public void cargarInformacionAsignaturaEditar(BigInteger idAsignatura) {
        try {
            asignaturaEditar = gestionarAsignaturasBO.obtenerAsignaturaPorIDCarrera(idAsignatura);
            if (asignaturaEditar != null) {
                editarNombre = asignaturaEditar.getNombreasignatura();
                editarFacultad = asignaturaEditar.getPlanestudios().getCarrera().getDepartamento().getFacultad();
                editarDepartamento = asignaturaEditar.getPlanestudios().getCarrera().getDepartamento();
                editarCarrera = asignaturaEditar.getPlanestudios().getCarrera();
                editarCreditos = String.valueOf(asignaturaEditar.getNumerocreditos());
                editarPlanEstudio = asignaturaEditar.getPlanestudios();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarAsignaturas cargarInformacionAsignaturaEditar : " + e.toString());
        }
    }

    public void modificarInformacionAsignatura() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringAsignatura(1) == true) {
            if (validarEstructuraAsignatura(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorEstructuraAsignatura.show()");
            }
        } else {
            context.execute("errorInformacionAsignatura.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroAsignatura.hide()");
        try {
            Integer creditos = Integer.valueOf(editarCreditos);
            asignaturaEditar.setNumerocreditos(creditos);
            asignaturaEditar.setNombreasignatura(editarNombre);
            asignaturaEditar.setPlanestudios(editarPlanEstudio);
            gestionarAsignaturasBO.modificarInformacionAsignatura(asignaturaEditar);
            limpiarEditarAsignatura();
            context.execute("registroExitosoAsignatura..show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarAsignaturas almacenarModificacion : " + e.toString());
            context.execute("registroFallidoAsignatura.show()");
        }
    }

    public void limpiarEditarAsignatura() {
        editarFacultad = null;
        editarPlanEstudio = null;
        editarNombre = null;
        editarCreditos = null;
        editarDepartamento = null;
        editarCarrera = null;
        activarNuevoDepartamento = true;
        activarNuevoCarrera = true;
        activarNuevoPlanEstudio = true;
    }

    // GET - SET
    public String getParametroNombre() {
        return parametroNombre;
    }

    public void setParametroNombre(String parametroNombre) {
        this.parametroNombre = parametroNombre;
    }

    public String getParametroCreditos() {
        return parametroCreditos;
    }

    public void setParametroCreditos(String parametroCreditos) {
        this.parametroCreditos = parametroCreditos;
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

    public List<PlanEstudios> getListaPlanesEstudios() {
        return listaPlanesEstudios;
    }

    public void setListaPlanesEstudios(List<PlanEstudios> listaPlanesEstudios) {
        this.listaPlanesEstudios = listaPlanesEstudios;
    }

    public PlanEstudios getParametroPlanEstudio() {
        return parametroPlanEstudio;
    }

    public void setParametroPlanEstudio(PlanEstudios parametroPlanEstudio) {
        this.parametroPlanEstudio = parametroPlanEstudio;
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

    public boolean isActivarPlanEstudio() {
        return activarPlanEstudio;
    }

    public void setActivarPlanEstudio(boolean activarPlanEstudio) {
        this.activarPlanEstudio = activarPlanEstudio;
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

    public boolean isActivarNuevoPlanEstudio() {
        return activarNuevoPlanEstudio;
    }

    public void setActivarNuevoPlanEstudio(boolean activarNuevoPlanEstudio) {
        this.activarNuevoPlanEstudio = activarNuevoPlanEstudio;
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

    public List<Asignatura> getListaAsignaturas() {
        return listaAsignaturas;
    }

    public void setListaAsignaturas(List<Asignatura> listaAsignaturas) {
        this.listaAsignaturas = listaAsignaturas;
    }

    public List<Asignatura> getFiltrarListaAsignaturas() {
        return filtrarListaAsignaturas;
    }

    public void setFiltrarListaAsignaturas(List<Asignatura> filtrarListaAsignaturas) {
        this.filtrarListaAsignaturas = filtrarListaAsignaturas;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombreAsignatura() {
        return nuevoNombreAsignatura;
    }

    public void setNuevoNombreAsignatura(String nuevoNombreAsignatura) {
        this.nuevoNombreAsignatura = nuevoNombreAsignatura;
    }

    public String getNuevoCreditoAsignatura() {
        return nuevoCreditoAsignatura;
    }

    public void setNuevoCreditoAsignatura(String nuevoCreditoAsignatura) {
        this.nuevoCreditoAsignatura = nuevoCreditoAsignatura;
    }

    public Facultad getNuevoFacultadAsignatura() {
        return nuevoFacultadAsignatura;
    }

    public void setNuevoFacultadAsignatura(Facultad nuevoFacultadAsignatura) {
        this.nuevoFacultadAsignatura = nuevoFacultadAsignatura;
    }

    public Departamento getNuevoDepartamentoAsignatura() {
        return nuevoDepartamentoAsignatura;
    }

    public void setNuevoDepartamentoAsignatura(Departamento nuevoDepartamentoAsignatura) {
        this.nuevoDepartamentoAsignatura = nuevoDepartamentoAsignatura;
    }

    public Carrera getNuevoCarreraAsignatura() {
        return nuevoCarreraAsignatura;
    }

    public void setNuevoCarreraAsignatura(Carrera nuevoCarreraAsignatura) {
        this.nuevoCarreraAsignatura = nuevoCarreraAsignatura;
    }

    public PlanEstudios getNuevoPlanEstudioAsignatura() {
        return nuevoPlanEstudioAsignatura;
    }

    public void setNuevoPlanEstudioAsignatura(PlanEstudios nuevoPlanEstudioAsignatura) {
        this.nuevoPlanEstudioAsignatura = nuevoPlanEstudioAsignatura;
    }

    public Asignatura getAsignaturaEditar() {
        return asignaturaEditar;
    }

    public void setAsignaturaEditar(Asignatura asignaturaEditar) {
        this.asignaturaEditar = asignaturaEditar;
    }

    public String getEditarNombre() {
        return editarNombre;
    }

    public void setEditarNombre(String editarNombre) {
        this.editarNombre = editarNombre;
    }

    public String getEditarCreditos() {
        return editarCreditos;
    }

    public void setEditarCreditos(String editarCreditos) {
        this.editarCreditos = editarCreditos;
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

    public PlanEstudios getEditarPlanEstudio() {
        return editarPlanEstudio;
    }

    public void setEditarPlanEstudio(PlanEstudios editarPlanEstudio) {
        this.editarPlanEstudio = editarPlanEstudio;
    }

}

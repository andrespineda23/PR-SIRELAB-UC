package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarEstudiantesBOInterface;
import com.sirelab.entidades.Carrera;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Estudiante;
import com.sirelab.entidades.PlanEstudios;
import com.sirelab.exporter.ExportarPDF;
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
import javax.faces.bean.RequestScoped;
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
public class ControllerAdministrarEstudiantes implements Serializable {

    @EJB
    AdministrarEstudiantesBOInterface administrarEstudiantesBO;

    private String parametroNombre, parametroApellido, parametroDocumento, parametroCorreo, parametroSemestre;
    private List<Departamento> listaDepartamentos;
    private Departamento parametroDepartamento;
    private List<Carrera> listaCarreras;
    private Carrera parametroCarrera;
    private List<PlanEstudios> listaPlanesEstudios;
    private PlanEstudios parametroPlanEst;
    private boolean parametroEstado;
    private boolean todosEstudiantes;
    private Map<String, String> filtros;
    //
    private boolean activoCarrera, activoPlan;
    //
    private boolean activarExport;
    //
    private List<Estudiante> listaEstudiantes;
    private List<Estudiante> filtrarListaEstudiantes;
    //
    private Column numeroIDTabla, nombresTabla, apellidosTabla, correoTabla, departamentoTabla, carreraTabla, planEstTabla, semestreTabla, estadoTabla;
    //
    private String altoTabla;

    public ControllerAdministrarEstudiantes() {

    }

    @PostConstruct
    public void init() {
        activarExport = true;
        activoCarrera = true;
        activoPlan = true;
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroSemestre = null;
        parametroDepartamento = new Departamento();
        parametroCarrera = new Carrera();
        parametroPlanEst = new PlanEstudios();
        parametroEstado = true;
        todosEstudiantes = false;
        listaDepartamentos = administrarEstudiantesBO.obtenerListasDepartamentos();
        altoTabla = "150";
        inicializarFiltros();
        listaEstudiantes = null;
        filtrarListaEstudiantes = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroApellido", null);
        filtros.put("parametroDocumento", null);
        filtros.put("parametroCorreo", null);
        filtros.put("parametroSemestre", null);
        filtros.put("parametroEstado", null);
        filtros.put("parametroDepartamento", null);
        filtros.put("parametroCarrera", null);
        filtros.put("parametroPlanEst", null);
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
        if (todosEstudiantes == false) {
            if (parametroEstado == true) {
                filtros.put("parametroEstado", "true");
            } else {
                filtros.put("parametroEstado", "false");
            }
        }
        if (parametroDepartamento.getIddepartamento() != null) {
            filtros.put("parametroDepartamento", parametroDepartamento.getIddepartamento().toString());
        }
        if (parametroCarrera.getIdcarrera() != null) {
            filtros.put("parametroCarrera", parametroCarrera.getIdcarrera().toString());
        }
        if (parametroPlanEst.getIdplanestudios() != null) {
            filtros.put("parametroPlanEst", parametroPlanEst.getIdplanestudios().toString());
        }
        if ((Utilidades.validarNulo(parametroSemestre)) && (!parametroSemestre.isEmpty())) {
            filtros.put("parametroPlanEst", parametroPlanEst.getIdplanestudios().toString());
        }
    }

    public void buscarEstudiantesPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaEstudiantes = null;
            listaEstudiantes = administrarEstudiantesBO.consultarEstudiantesPorParametro(filtros);
            if (listaEstudiantes != null) {
                if (listaEstudiantes.size() > 0) {
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
            System.out.println("Error ControllerAdministrarEstudiantes buscarEstudiantesPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        desactivarFiltrosTabla();
        activarExport = true;
        activoCarrera = true;
        activoPlan = true;
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroSemestre = null;
        parametroDepartamento = new Departamento();
        parametroCarrera = new Carrera();
        parametroPlanEst = new PlanEstudios();
        parametroEstado = true;
        todosEstudiantes = false;
        inicializarFiltros();
        listaEstudiantes = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void modificacionTodosEstudiantes() {
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
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("width: 80px");
        carreraTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:carreraTabla");
        carreraTabla.setFilterStyle("width: 80px");
        planEstTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:planEstTabla");
        planEstTabla.setFilterStyle("width: 80px");
        semestreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:semestreTabla");
        semestreTabla.setFilterStyle("width: 80px");
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
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("display: none; visibility: hidden;");
        carreraTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:carreraTabla");
        carreraTabla.setFilterStyle("display: none; visibility: hidden;");
        planEstTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:planEstTabla");
        planEstTabla.setFilterStyle("display: none; visibility: hidden;");
        semestreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:semestreTabla");
        semestreTabla.setFilterStyle("display: none; visibility: hidden;");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaEstudiantes = null;
    }

    public void actualizarDepartamentos() {
        System.out.println("actualizarDepartamentos");
        try {
            if (Utilidades.validarNulo(parametroDepartamento)) {
                activoCarrera = false;
                parametroCarrera = new Carrera();
                listaCarreras = administrarEstudiantesBO.obtenerListasCarrerasPorDepartamento(parametroDepartamento.getIddepartamento());
            } else {
                activoCarrera = true;
                activoPlan = true;
                listaCarreras = null;
                parametroCarrera = new Carrera();
                listaPlanesEstudios = null;
                parametroPlanEst = new PlanEstudios();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:parametroCarrera");
            context.update("formT:form:parametroPlanEst");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarEstudiantes actualizarDepartamentos : " + e.toString());
        }
    }

    public void actualizarCarreras() {
        System.out.println("actualizarCarreras");
        try {
            if (Utilidades.validarNulo(parametroCarrera)) {
                activoPlan = false;
                parametroPlanEst = new PlanEstudios();
                listaPlanesEstudios = administrarEstudiantesBO.obtenerListasPlanesEstudioPorCarrera(parametroCarrera.getIdcarrera());
            } else {
                activoPlan = true;
                listaPlanesEstudios = null;
                parametroPlanEst = new PlanEstudios();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:parametroPlanEst");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarEstudiantes actualizarCarreras : " + e.toString());
        }
    }

    public String verDetallesEstudiante() {
        limpiarProcesoBusqueda();
        return "detallesestudiante";
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "Administrar_Estudiantes_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Administrar_Estudiantes_XLS", false, false, "UTF-8", null, null);
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

    public String getParametroSemestre() {
        return parametroSemestre;
    }

    public void setParametroSemestre(String parametroSemestre) {
        this.parametroSemestre = parametroSemestre;
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

    public PlanEstudios getParametroPlanEst() {
        return parametroPlanEst;
    }

    public void setParametroPlanEst(PlanEstudios parametroPlanEst) {
        this.parametroPlanEst = parametroPlanEst;
    }

    public boolean isParametroEstado() {
        return parametroEstado;
    }

    public void setParametroEstado(boolean parametroEstado) {
        this.parametroEstado = parametroEstado;
    }

    public boolean isTodosEstudiantes() {
        return todosEstudiantes;
    }

    public void setTodosEstudiantes(boolean todosEstudiantes) {
        this.todosEstudiantes = todosEstudiantes;
    }

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public List<Estudiante> getListaEstudiantes() {
        return listaEstudiantes;
    }

    public void setListaEstudiantes(List<Estudiante> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public List<Estudiante> getFiltrarListaEstudiantes() {
        return filtrarListaEstudiantes;
    }

    public void setFiltrarListaEstudiantes(List<Estudiante> filtrarListaEstudiantes) {
        this.filtrarListaEstudiantes = filtrarListaEstudiantes;
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

}

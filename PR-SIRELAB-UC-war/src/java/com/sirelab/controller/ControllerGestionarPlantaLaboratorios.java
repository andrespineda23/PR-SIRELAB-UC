package com.sirelab.controller;

import com.sirelab.bo.interfacebo.GestionarPlantaLaboratoriosBOInterface;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Laboratorio;
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
public class ControllerGestionarPlantaLaboratorios implements Serializable {

    @EJB
    GestionarPlantaLaboratoriosBOInterface gestionarPlantaLaboratoriosBO;

    private String parametroNombre;
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
    private List<Laboratorio> listaLaboratorios;
    private List<Laboratorio> filtrarListaLaboratorios;
    //
    private Column nombreTabla, facultadTabla, departamentoTabla;
    //
    private String altoTabla;
    //
    private String nuevoNombreLaboratorio;
    private Facultad nuevoFacultadLaboratorio;
    private Departamento nuevoDepartamentoLaboratorio;
    //
    private Laboratorio laboratorioEditar;
    private String editarNombre;
    private Facultad editarFacultad;
    private Departamento editarDepartamento;

    public ControllerGestionarPlantaLaboratorios() {
    }

    @PostConstruct
    public void init() {
        activarDepartamento = true;
        activarNuevoDepartamento = true;
        activarExport = true;
        parametroNombre = null;
        parametroFacultad = new Facultad();
        parametroDepartamento = new Departamento();
        listaFacultades = gestionarPlantaLaboratoriosBO.consultarFacultadesRegistradas();
        altoTabla = "150";
        inicializarFiltros();
        listaLaboratorios = null;
        listaDepartamentos = null;
        filtrarListaLaboratorios = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroDepartamento", null);
        filtros.put("parametroFacultad", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if (parametroFacultad.getIdfacultad() != null) {
            filtros.put("parametroFacultad", parametroFacultad.getIdfacultad().toString());
        }
        if (parametroDepartamento.getIddepartamento() != null) {
            filtros.put("parametroDepartamento", parametroDepartamento.getIddepartamento().toString());
        }
    }

    public void buscarLaboratoriosPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaLaboratorios = null;
            listaLaboratorios = gestionarPlantaLaboratoriosBO.consultarLaboratoriosPorParametro(filtros);
            if (listaLaboratorios != null) {
                if (listaLaboratorios.size() > 0) {
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
            System.out.println("Error ControllerGestionarPlantaLaboratorios buscarLaboratoriosPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarDepartamento = true;
        if (null != listaLaboratorios) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroNombre = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        inicializarFiltros();
        listaLaboratorios = null;
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
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaLaboratorios = null;
    }

    public void dispararDialogoNuevoLaboratorio() {
        limpiarRegistroLaboratorio();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroLaboratorio");
        context.execute("NuevoRegistroLaboratorio.show()");
    }

    public void limpiarRegistroLaboratorio() {
        nuevoFacultadLaboratorio = null;
        nuevoDepartamentoLaboratorio = null;
        nuevoNombreLaboratorio = null;
        activarNuevoDepartamento = true;
    }

    public boolean validarStringLaboratorio(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoNombreLaboratorio)) {
                if (!Utilidades.validarCaracterString(nuevoNombreLaboratorio)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        } else {
            if (Utilidades.validarNulo(editarNombre)) {
                if (!Utilidades.validarCaracterString(editarNombre)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFacultadDepartamentoLaboratorio(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (!Utilidades.validarNulo(nuevoFacultadLaboratorio)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoDepartamentoLaboratorio)) {
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

    public void registrarNuevoLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringLaboratorio(0) == true) {
            if (validarFacultadDepartamentoLaboratorio(0) == true) {
                almacenarNuevoLaboratorioEnSistema();
            } else {
                context.execute("errorFacultadDepartamentoLaboratorio.show()");
            }
        } else {
            context.execute("errorInformacionLaboratorio.show()");
        }
    }

    public void almacenarNuevoLaboratorioEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroLaboratorio.hide()");
        try {
            Laboratorio carreraNuevo = new Laboratorio();
            carreraNuevo.setNombrelaboratorio(nuevoNombreLaboratorio);
            carreraNuevo.setDepartamento(nuevoDepartamentoLaboratorio);
            gestionarPlantaLaboratoriosBO.crearNuevaLaboratorio(carreraNuevo);
            context.execute("registroExitosoLaboratorio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlantaLaboratorios almacenarNuevoLaboratorioEnSistema : " + e.toString());
            context.execute("registroFallidoLaboratorio.show()");
        }
    }

    public void actualizarFacultades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroFacultad)) {
            parametroDepartamento = new Departamento();
            listaDepartamentos = gestionarPlantaLaboratoriosBO.consultarDepartamentosPorIDFacultad(parametroFacultad.getIdfacultad());
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
            if (Utilidades.validarNulo(nuevoFacultadLaboratorio)) {
                nuevoDepartamentoLaboratorio = null;
                listaDepartamentos = gestionarPlantaLaboratoriosBO.consultarDepartamentosPorIDFacultad(nuevoFacultadLaboratorio.getIdfacultad());
                activarNuevoDepartamento = false;
            } else {
                nuevoDepartamentoLaboratorio = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
            }
            context.update("formT:formularioDialogos:nuevoDepartamentoLaboratorio");
        } else {
            if (Utilidades.validarNulo(editarFacultad)) {
                editarDepartamento = null;
                listaDepartamentos = gestionarPlantaLaboratoriosBO.consultarDepartamentosPorIDFacultad(editarFacultad.getIdfacultad());
                activarNuevoDepartamento = false;
            } else {
                editarDepartamento = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
            }
            context.update("formT:formularioDialogos:editarDepartamentoLaboratorio");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Gestionar_Planta_Laboratorios_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Gestionar_Planta_Laboratorios_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void dispararDialogoEditarLaboratorio(BigInteger idLaboratorio) {
        editarNombre = null;
        editarDepartamento = null;
        editarFacultad = null;
        activarNuevoDepartamento = false;
        cargarInformacionLaboratorioEditar(idLaboratorio);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroLaboratorio");
        context.execute("EditarRegistroLaboratorio.show()");
    }

    public void cargarInformacionLaboratorioEditar(BigInteger idLaboratorio) {
        try {
            laboratorioEditar = gestionarPlantaLaboratoriosBO.obtenerLaboratorioPorIDLaboratorio(idLaboratorio);
            if (laboratorioEditar != null) {
                editarNombre = laboratorioEditar.getNombrelaboratorio();
                editarFacultad = laboratorioEditar.getDepartamento().getFacultad();
                editarDepartamento = laboratorioEditar.getDepartamento();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlantaLaboratorios cargarInformacionLaboratorioEditar : " + e.toString());
        }
    }

    public void modificarInformacionLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringLaboratorio(1) == true) {
            if (validarFacultadDepartamentoLaboratorio(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorFacultadDepartamentoLaboratorio.show()");
            }
        } else {
            context.execute("errorInformacionLaboratorio.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroLaboratorio.hide()");
        try {
            laboratorioEditar.setNombrelaboratorio(editarNombre);
            laboratorioEditar.setDepartamento(editarDepartamento);
            gestionarPlantaLaboratoriosBO.modificarInformacionLaboratorio(laboratorioEditar);
            limpiarEditarLaboratorio();
            context.execute("registroExitosoLaboratorio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlantaLaboratorios almacenarModificacion : " + e.toString());
            context.execute("registroFallidoLaboratorio.show()");
        }
    }

    public void limpiarEditarLaboratorio() {
        editarFacultad = null;
        editarNombre = null;
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

    public List<Laboratorio> getListaLaboratorios() {
        return listaLaboratorios;
    }

    public void setListaLaboratorios(List<Laboratorio> listaLaboratorios) {
        this.listaLaboratorios = listaLaboratorios;
    }

    public List<Laboratorio> getFiltrarListaLaboratorios() {
        return filtrarListaLaboratorios;
    }

    public void setFiltrarListaLaboratorios(List<Laboratorio> filtrarListaLaboratorios) {
        this.filtrarListaLaboratorios = filtrarListaLaboratorios;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombreLaboratorio() {
        return nuevoNombreLaboratorio;
    }

    public void setNuevoNombreLaboratorio(String nuevoNombreLaboratorio) {
        this.nuevoNombreLaboratorio = nuevoNombreLaboratorio;
    }

    public Facultad getNuevoFacultadLaboratorio() {
        return nuevoFacultadLaboratorio;
    }

    public void setNuevoFacultadLaboratorio(Facultad nuevoFacultadLaboratorio) {
        this.nuevoFacultadLaboratorio = nuevoFacultadLaboratorio;
    }

    public Departamento getNuevoDepartamentoLaboratorio() {
        return nuevoDepartamentoLaboratorio;
    }

    public void setNuevoDepartamentoLaboratorio(Departamento nuevoDepartamentoLaboratorio) {
        this.nuevoDepartamentoLaboratorio = nuevoDepartamentoLaboratorio;
    }

    public Laboratorio getLaboratorioEditar() {
        return laboratorioEditar;
    }

    public void setLaboratorioEditar(Laboratorio laboratorioEditar) {
        this.laboratorioEditar = laboratorioEditar;
    }

    public String getEditarNombre() {
        return editarNombre;
    }

    public void setEditarNombre(String editarNombre) {
        this.editarNombre = editarNombre;
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

}

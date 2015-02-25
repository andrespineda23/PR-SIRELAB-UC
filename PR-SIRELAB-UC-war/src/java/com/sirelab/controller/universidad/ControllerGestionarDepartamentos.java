package com.sirelab.controller.universidad;

import com.sirelab.bo.interfacebo.GestionarDepartamentosBOInterface;
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
public class ControllerGestionarDepartamentos implements Serializable {

    @EJB
    GestionarDepartamentosBOInterface gestionarDepartamentosBO;

    private String parametroNombre;
    private List<Facultad> listaFacultades;
    private Facultad parametroFacultad;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<Departamento> listaDepartamentos;
    private List<Departamento> filtrarListaDepartamentos;
    //
    private Column nombreTabla, facultadTabla;
    //
    private String altoTabla;
    //
    private String nuevoNombreDepartamento;
    private Facultad nuevoFacultadDepartamento;
    //
    private Departamento departamentoEditar;
    private String editarNombre;
    private Facultad editarFacultad;

    public ControllerGestionarDepartamentos() {
    }

    @PostConstruct
    public void init() {
        activarExport = true;
        parametroNombre = null;
        parametroFacultad = new Facultad();
        listaFacultades = gestionarDepartamentosBO.consultarFacultadesRegistradas();
        altoTabla = "150";
        inicializarFiltros();
        listaDepartamentos = null;
        filtrarListaDepartamentos = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
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
    }

    public void buscarDepartamentosPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaDepartamentos = null;
            listaDepartamentos = gestionarDepartamentosBO.consultarDepartamentosPorParametro(filtros);
            if (listaDepartamentos != null) {
                if (listaDepartamentos.size() > 0) {
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
            System.out.println("Error ControllerAdministrarDepartamentos buscarDepartamentosPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        if (null != listaDepartamentos) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroNombre = null;
        parametroFacultad = new Facultad();
        inicializarFiltros();
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
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("display: none; visibility: hidden;");
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaDepartamentos = null;
    }

    public void dispararDialogoNuevoDepartamento() {
        limpiarRegistroDepartamento();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroDepartamento");
        context.execute("NuevoRegistroDepartamento.show()");
    }

    public void limpiarRegistroDepartamento() {
        nuevoFacultadDepartamento = null;
        nuevoNombreDepartamento = null;
    }

    public boolean validarStringDepartamento(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoNombreDepartamento)) {
                if (!Utilidades.validarCaracterString(nuevoNombreDepartamento)) {
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

    public boolean validarFacultadDepartamento(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (!Utilidades.validarNulo(nuevoFacultadDepartamento)) {
                retorno = false;
            }
        } else {
            if (!Utilidades.validarNulo(editarFacultad)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoDepartamento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringDepartamento(0) == true) {
            if (validarFacultadDepartamento(0) == true) {
                almacenarNuevoDepartamentoEnSistema();
            } else {
                context.execute("errorFacultadDepartamento.show()");
            }
        } else {
            context.execute("errorInformacionDepartamento.show()");
        }
    }

    public void almacenarNuevoDepartamentoEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroDepartamento.hide()");
        try {
            Departamento departamentoNuevo = new Departamento();
            departamentoNuevo.setNombredepartamento(nuevoNombreDepartamento);
            departamentoNuevo.setFacultad(nuevoFacultadDepartamento);
            gestionarDepartamentosBO.crearNuevaDepartamento(departamentoNuevo);
            context.execute("registroExitosoDepartamento.show()");
        } catch (Exception e) {
            System.out.println("Error almacenarNuevoDepartamentoEnSistema almacenarNuevoDepartamentoEnSistema : " + e.toString());
            context.execute("registroFallidoDepartamento.show()");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "Gestionar_Departamentos_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Gestionar_Departamentos_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void dispararDialogoEditarDepartamento(BigInteger idDepartamento) {
        editarNombre = null;
        editarFacultad = null;
        cargarInformacionUsuarioEditar(idDepartamento);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroDepartamento");
        context.execute("EditarRegistroDepartamento.show()");
    }

    public void cargarInformacionUsuarioEditar(BigInteger idDepartamento) {
        try {
            departamentoEditar = gestionarDepartamentosBO.obtenerDepartamentoPorIDDepartamento(idDepartamento);
            if (departamentoEditar != null) {
                editarNombre = departamentoEditar.getNombredepartamento();
                editarFacultad = departamentoEditar.getFacultad();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarDepartamentos cargarInformacionUsuarioEditar : " + e.toString());
        }
    }

    public void modificarInformacionDepartamento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringDepartamento(1) == true) {
            if (validarFacultadDepartamento(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorFacultadDepartamento.show()");
            }
        } else {
            context.execute("errorInformacionDepartamento.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroDepartamento.hide()");
        try {
            departamentoEditar.setNombredepartamento(editarNombre);
            departamentoEditar.setFacultad(editarFacultad);
            gestionarDepartamentosBO.modificarInformacionDepartamento(departamentoEditar);
            limpiarEditarDepartamento();
            context.execute("registroExitosoDepartamento.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarDepartamentos almacenarModificacion : " + e.toString());
            context.execute("registroFallidoDepartamento.show()");
        }
    }

    public void limpiarEditarDepartamento() {
        editarFacultad = null;
        editarNombre = null;
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

    public List<Departamento> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public List<Departamento> getFiltrarListaDepartamentos() {
        return filtrarListaDepartamentos;
    }

    public void setFiltrarListaDepartamentos(List<Departamento> filtrarListaDepartamentos) {
        this.filtrarListaDepartamentos = filtrarListaDepartamentos;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombreDepartamento() {
        return nuevoNombreDepartamento;
    }

    public void setNuevoNombreDepartamento(String nuevoNombreDepartamento) {
        this.nuevoNombreDepartamento = nuevoNombreDepartamento;
    }

    public Facultad getNuevoFacultadDepartamento() {
        return nuevoFacultadDepartamento;
    }

    public void setNuevoFacultadDepartamento(Facultad nuevoFacultadDepartamento) {
        this.nuevoFacultadDepartamento = nuevoFacultadDepartamento;
    }

    public Departamento getDepartamentoEditar() {
        return departamentoEditar;
    }

    public void setDepartamentoEditar(Departamento departamentoEditar) {
        this.departamentoEditar = departamentoEditar;
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

}

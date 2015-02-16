package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarParametroAreaProfundizacionBOInterface;
import com.sirelab.entidades.AreaProfundizacion;
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
public class ControllerAdministrarParametroAreaProfudizacion implements Serializable {

    @EJB
    AdministrarParametroAreaProfundizacionBOInterface administrarParametroAreaProfundizacionBO;

    private String parametroNombre, parametroCodigo;
    private List<Facultad> listaFacultades;
    private Facultad parametroFacultad;
    private List<Departamento> listaDepartamentos;
    private Departamento parametroDepartamento;
    private List<Laboratorio> listaLaboratorios;
    private Laboratorio parametroLaboratorio;
    private boolean activarDepartamento;
    private boolean activarLaboratorio;
    private boolean activarNuevoDepartamento;
    private boolean activarNuevoLaboratorio;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<AreaProfundizacion> listaAreasProfundizacion;
    private List<AreaProfundizacion> filtrarListaAreasProfundizacion;
    //
    private Column nombreTabla, codigoTabla, laboratorioTabla, facultadTabla, departamentoTabla;
    //
    private String altoTabla;
    //
    private String nuevoNombreAreaProfundizacion, nuevoCodigoAreaProfundizacion;
    private Facultad nuevoFacultadAreaProfundizacion;
    private Departamento nuevoDepartamentoAreaProfundizacion;
    private Laboratorio nuevoLaboratorioAreaProfundizacion;
    //
    private AreaProfundizacion areaProfundizacionEditar;
    private String editarNombre, editarCodigo;
    private Facultad editarFacultad;
    private Departamento editarDepartamento;
    private Laboratorio editarLaboratorio;

    public ControllerAdministrarParametroAreaProfudizacion() {
    }

    @PostConstruct
    public void init() {
        activarDepartamento = true;
        activarLaboratorio = true;
        activarNuevoDepartamento = true;
        activarNuevoLaboratorio = true;
        activarExport = true;
        parametroNombre = null;
        parametroCodigo = null;
        parametroFacultad = new Facultad();
        parametroDepartamento = new Departamento();
        parametroLaboratorio = new Laboratorio();
        listaFacultades = administrarParametroAreaProfundizacionBO.consultarFacultadesRegistradas();
        altoTabla = "150";
        inicializarFiltros();
        listaAreasProfundizacion = null;
        listaLaboratorios = null;
        listaDepartamentos = null;
        filtrarListaAreasProfundizacion = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroCodigo", null);
        filtros.put("parametroLaboratorio", null);
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
        if (parametroLaboratorio.getIdlaboratorio() != null) {
            filtros.put("parametroLaboratorio", parametroLaboratorio.getIdlaboratorio().toString());
        }
    }

    public void buscarAreasProfundizacionPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaAreasProfundizacion = null;
            listaAreasProfundizacion = administrarParametroAreaProfundizacionBO.consultarAreasProfundizacionPorParametro(filtros);
            if (listaAreasProfundizacion != null) {
                if (listaAreasProfundizacion.size() > 0) {
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
            System.out.println("Error ControllerAdministrarAreaProfudizacion buscarLaboratoriosPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarDepartamento = true;
        activarLaboratorio = true;
        if (null != listaAreasProfundizacion) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroNombre = null;
        parametroCodigo = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        parametroLaboratorio = new Laboratorio();
        inicializarFiltros();
        listaAreasProfundizacion = null;
        listaDepartamentos = null;
        listaLaboratorios = null;
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
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("width: 80px");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("width: 80px");
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
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("display: none; visibility: hidden;");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaAreasProfundizacion = null;
    }

    public void dispararDialogoNuevoAreaProfundizacion() {
        limpiarRegistroAreaProfundizacion();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroAreaProfundizacion");
        context.execute("NuevoRegistroAreaProfundizacion.show()");
    }

    public void limpiarRegistroAreaProfundizacion() {
        nuevoFacultadAreaProfundizacion = null;
        nuevoLaboratorioAreaProfundizacion = null;
        nuevoDepartamentoAreaProfundizacion = null;
        nuevoNombreAreaProfundizacion = null;
        nuevoCodigoAreaProfundizacion = null;
        activarNuevoDepartamento = true;
        activarNuevoLaboratorio = true;
    }

    public boolean validarStringAreaProfundizacion(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoNombreAreaProfundizacion)) && (Utilidades.validarNulo(nuevoCodigoAreaProfundizacion))) {
                if (!Utilidades.validarCaracterString(nuevoNombreAreaProfundizacion)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        } else {
            if ((Utilidades.validarNulo(editarNombre)) && (Utilidades.validarNulo(editarCodigo))) {
                if (!Utilidades.validarCaracterString(editarNombre)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarEstructuraAreaProfundizacion(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (!Utilidades.validarNulo(nuevoFacultadAreaProfundizacion)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoDepartamentoAreaProfundizacion)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(nuevoLaboratorioAreaProfundizacion)) {
                retorno = false;
            }
        } else {
            if (!Utilidades.validarNulo(editarFacultad)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(editarDepartamento)) {
                retorno = false;
            }
            if (!Utilidades.validarNulo(editarLaboratorio)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoAreaProfundizacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringAreaProfundizacion(0) == true) {
            if (validarEstructuraAreaProfundizacion(0) == true) {
                almacenarNuevoLaboratorioEnSistema();
            } else {
                context.execute("errorEstructuraAreaProfundizacion.show()");
            }
        } else {
            context.execute("errorInformacionAreaProfundizacion.show()");
        }
    }

    public void almacenarNuevoLaboratorioEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroAreaProfundizacion.hide()");
        try {
            AreaProfundizacion areaNuevo = new AreaProfundizacion();
            areaNuevo.setNombrearea(nuevoNombreAreaProfundizacion);
            areaNuevo.setCodigoarea(nuevoCodigoAreaProfundizacion);
            areaNuevo.setLaboratorio(nuevoLaboratorioAreaProfundizacion);
            administrarParametroAreaProfundizacionBO.crearNuevaAreaProfundizacion(areaNuevo);
            context.execute("registroExitosoAreaProfundizacion.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarAreaProfudizacion almacenarNuevoLaboratorioEnSistema : " + e.toString());
            context.execute("registroFallidoAreaProfundizacion.show()");
        }
    }

    public void actualizarFacultades() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroFacultad)) {
            parametroDepartamento = new Departamento();
            listaDepartamentos = administrarParametroAreaProfundizacionBO.consultarDepartamentosPorIDFacultad(parametroFacultad.getIdfacultad());
            activarDepartamento = false;
            parametroLaboratorio = new Laboratorio();
            activarLaboratorio = true;
            listaLaboratorios = null;
        } else {
            parametroDepartamento = new Departamento();
            listaDepartamentos = null;
            activarDepartamento = true;
            parametroLaboratorio = new Laboratorio();
            activarLaboratorio = true;
            listaLaboratorios = null;
        }
        context.update("formT:form:parametroDepartamento");
        context.update("formT:form:parametroLaboratorio");
    }

    public void actualizarDepartamentos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroDepartamento)) {
            parametroLaboratorio = new Laboratorio();
            listaLaboratorios = administrarParametroAreaProfundizacionBO.consultarLaboratoriosPorIDDepartamento(parametroDepartamento.getIddepartamento());
            activarLaboratorio = false;
        } else {
            parametroLaboratorio = new Laboratorio();
            activarLaboratorio = true;
            listaLaboratorios = null;
        }
        context.update("formT:form:parametroLaboratorio");
    }

    public void actualizarNuevoFacultades(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoFacultadAreaProfundizacion)) {
                nuevoDepartamentoAreaProfundizacion = null;
                listaDepartamentos = administrarParametroAreaProfundizacionBO.consultarDepartamentosPorIDFacultad(nuevoFacultadAreaProfundizacion.getIdfacultad());
                activarNuevoDepartamento = false;
                activarNuevoLaboratorio = true;
                listaLaboratorios = null;
                nuevoLaboratorioAreaProfundizacion = null;
            } else {
                nuevoDepartamentoAreaProfundizacion = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
                activarNuevoLaboratorio = true;
                listaLaboratorios = null;
                nuevoLaboratorioAreaProfundizacion = null;
            }
            context.update("formT:formularioDialogos:nuevoDepartamentoAreaProfundizacion");
            context.update("formT:formularioDialogos:nuevoLaboratorioAreaProfundizacion");
        } else {
            if (Utilidades.validarNulo(editarFacultad)) {
                editarDepartamento = null;
                listaDepartamentos = administrarParametroAreaProfundizacionBO.consultarDepartamentosPorIDFacultad(editarFacultad.getIdfacultad());
                activarNuevoDepartamento = false;
                activarNuevoLaboratorio = true;
                listaLaboratorios = null;
                editarLaboratorio = null;
            } else {
                editarDepartamento = null;
                listaDepartamentos = null;
                activarNuevoDepartamento = true;
                activarNuevoLaboratorio = true;
                listaLaboratorios = null;
                editarLaboratorio = null;
            }
            context.update("formT:formularioDialogos:editarDepartamentoAreaProfundizacion");
            context.update("formT:formularioDialogos:editarLaboratorioAreaProfundizacion");
        }
    }

    public void actualizarNuevoDepartamentos(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoDepartamentoAreaProfundizacion)) {
                nuevoLaboratorioAreaProfundizacion = null;
                listaLaboratorios = administrarParametroAreaProfundizacionBO.consultarLaboratoriosPorIDDepartamento(nuevoDepartamentoAreaProfundizacion.getIddepartamento());
                activarNuevoLaboratorio = false;
            } else {
                activarNuevoLaboratorio = true;
                listaLaboratorios = null;
                nuevoLaboratorioAreaProfundizacion = null;
            }
            context.update("formT:formularioDialogos:nuevoLaboratorioAreaProfundizacion");
        } else {
            if (Utilidades.validarNulo(editarDepartamento)) {
                editarLaboratorio = null;
                listaLaboratorios = administrarParametroAreaProfundizacionBO.consultarLaboratoriosPorIDDepartamento(editarDepartamento.getIddepartamento());
                activarNuevoLaboratorio = false;
            } else {
                activarNuevoLaboratorio = true;
                listaLaboratorios = null;
                editarLaboratorio = null;
            }
            context.update("formT:formularioDialogos:editarLaboratorioAreaProfundizacion");
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Administrar_Areas_Profundizacion_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Administrar_Areas_Profundizacion_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void dispararDialogoEditarAreaProfundizacion(BigInteger idArea) {
        editarNombre = null;
        editarCodigo = null;
        editarDepartamento = null;
        editarFacultad = null;
        editarLaboratorio = null;
        activarNuevoDepartamento = true;
        activarNuevoLaboratorio = true;
        cargarInformacionAreaProfundizacionEditar(idArea);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroAreaProfundizacion");
        context.execute("EditarRegistroAreaProfundizacion.show()");
    }

    public void cargarInformacionAreaProfundizacionEditar(BigInteger idArea) {
        try {
            areaProfundizacionEditar = administrarParametroAreaProfundizacionBO.obtenerAreaProfundizacionPorIDAreaProfundizacion(idArea);
            if (areaProfundizacionEditar != null) {
                editarNombre = areaProfundizacionEditar.getNombrearea();
                editarCodigo = areaProfundizacionEditar.getCodigoarea();
                editarFacultad = areaProfundizacionEditar.getLaboratorio().getDepartamento().getFacultad();
                editarDepartamento = areaProfundizacionEditar.getLaboratorio().getDepartamento();
                editarLaboratorio = areaProfundizacionEditar.getLaboratorio();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarAreaProfudizacion cargarInformacionAreaProfundizacionEditar : " + e.toString());
        }
    }

    public void modificarInformacionAreaProfundizacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringAreaProfundizacion(1) == true) {
            if (validarEstructuraAreaProfundizacion(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorEstructuraAreaProfundizacion.show()");
            }
        } else {
            context.execute("errorInformacionAreaProfundizacion.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroAreaProfundizacion.hide()");
        try {
            areaProfundizacionEditar.setNombrearea(editarNombre);
            areaProfundizacionEditar.setCodigoarea(editarCodigo);
            areaProfundizacionEditar.setLaboratorio(editarLaboratorio);
            administrarParametroAreaProfundizacionBO.modificarInformacionAreaProfundizacion(areaProfundizacionEditar);
            limpiarEditarAreaProfundizacion();
            context.execute("registroExitosoAreaProfundizacion.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarAreaProfudizacion almacenarModificacion : " + e.toString());
            context.execute("registroFallidoAreaProfundizacion.show()");
        }
    }

    public void limpiarEditarAreaProfundizacion() {
        editarFacultad = null;
        editarNombre = null;
        editarDepartamento = null;
        activarNuevoDepartamento = true;
        editarCodigo = null;
        activarNuevoLaboratorio = true;
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

    public List<Laboratorio> getListaLaboratorios() {
        return listaLaboratorios;
    }

    public void setListaLaboratorios(List<Laboratorio> listaLaboratorios) {
        this.listaLaboratorios = listaLaboratorios;
    }

    public Laboratorio getParametroLaboratorio() {
        return parametroLaboratorio;
    }

    public void setParametroLaboratorio(Laboratorio parametroLaboratorio) {
        this.parametroLaboratorio = parametroLaboratorio;
    }

    public boolean isActivarDepartamento() {
        return activarDepartamento;
    }

    public void setActivarDepartamento(boolean activarDepartamento) {
        this.activarDepartamento = activarDepartamento;
    }

    public boolean isActivarLaboratorio() {
        return activarLaboratorio;
    }

    public void setActivarLaboratorio(boolean activarLaboratorio) {
        this.activarLaboratorio = activarLaboratorio;
    }

    public boolean isActivarNuevoDepartamento() {
        return activarNuevoDepartamento;
    }

    public void setActivarNuevoDepartamento(boolean activarNuevoDepartamento) {
        this.activarNuevoDepartamento = activarNuevoDepartamento;
    }

    public boolean isActivarNuevoLaboratorio() {
        return activarNuevoLaboratorio;
    }

    public void setActivarNuevoLaboratorio(boolean activarNuevoLaboratorio) {
        this.activarNuevoLaboratorio = activarNuevoLaboratorio;
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

    public List<AreaProfundizacion> getListaAreasProfundizacion() {
        return listaAreasProfundizacion;
    }

    public void setListaAreasProfundizacion(List<AreaProfundizacion> listaAreasProfundizacion) {
        this.listaAreasProfundizacion = listaAreasProfundizacion;
    }

    public List<AreaProfundizacion> getFiltrarListaAreasProfundizacion() {
        return filtrarListaAreasProfundizacion;
    }

    public void setFiltrarListaAreasProfundizacion(List<AreaProfundizacion> filtrarListaAreasProfundizacion) {
        this.filtrarListaAreasProfundizacion = filtrarListaAreasProfundizacion;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombreAreaProfundizacion() {
        return nuevoNombreAreaProfundizacion;
    }

    public void setNuevoNombreAreaProfundizacion(String nuevoNombreAreaProfundizacion) {
        this.nuevoNombreAreaProfundizacion = nuevoNombreAreaProfundizacion;
    }

    public String getNuevoCodigoAreaProfundizacion() {
        return nuevoCodigoAreaProfundizacion;
    }

    public void setNuevoCodigoAreaProfundizacion(String nuevoCodigoAreaProfundizacion) {
        this.nuevoCodigoAreaProfundizacion = nuevoCodigoAreaProfundizacion;
    }

    public Facultad getNuevoFacultadAreaProfundizacion() {
        return nuevoFacultadAreaProfundizacion;
    }

    public void setNuevoFacultadAreaProfundizacion(Facultad nuevoFacultadAreaProfundizacion) {
        this.nuevoFacultadAreaProfundizacion = nuevoFacultadAreaProfundizacion;
    }

    public Departamento getNuevoDepartamentoAreaProfundizacion() {
        return nuevoDepartamentoAreaProfundizacion;
    }

    public void setNuevoDepartamentoAreaProfundizacion(Departamento nuevoDepartamentoAreaProfundizacion) {
        this.nuevoDepartamentoAreaProfundizacion = nuevoDepartamentoAreaProfundizacion;
    }

    public Laboratorio getNuevoLaboratorioAreaProfundizacion() {
        return nuevoLaboratorioAreaProfundizacion;
    }

    public void setNuevoLaboratorioAreaProfundizacion(Laboratorio nuevoLaboratorioAreaProfundizacion) {
        this.nuevoLaboratorioAreaProfundizacion = nuevoLaboratorioAreaProfundizacion;
    }

    public AreaProfundizacion getAreaProfundizacionEditar() {
        return areaProfundizacionEditar;
    }

    public void setAreaProfundizacionEditar(AreaProfundizacion areaProfundizacionEditar) {
        this.areaProfundizacionEditar = areaProfundizacionEditar;
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

    public Laboratorio getEditarLaboratorio() {
        return editarLaboratorio;
    }

    public void setEditarLaboratorio(Laboratorio editarLaboratorio) {
        this.editarLaboratorio = editarLaboratorio;
    }

}

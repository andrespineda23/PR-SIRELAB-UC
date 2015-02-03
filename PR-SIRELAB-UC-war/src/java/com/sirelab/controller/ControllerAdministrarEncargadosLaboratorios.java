package com.sirelab.controller;

import com.sirelab.bo.interfacebo.AdministrarEncargadosLaboratoriosBOInterface;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.EncargadoLaboratorio;
import com.sirelab.entidades.Facultad;
import com.sirelab.entidades.Laboratorio;
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
public class ControllerAdministrarEncargadosLaboratorios implements Serializable {

    @EJB
    AdministrarEncargadosLaboratoriosBOInterface administrarEncargadosLaboratoriosBO;

    private String parametroNombre, parametroApellido, parametroDocumento, parametroCorreo;
    private List<Facultad> listaFacultades;
    private Facultad parametroFacultad;
    private List<Departamento> listaDepartamentos;
    private Departamento parametroDepartamento;
    private List<Laboratorio> listaLaboratorios;
    private Laboratorio parametroLaboratorio;
    private boolean parametroEstado;
    private boolean todosEncargadosLaboratorios;
    private Map<String, String> filtros;
    //
    private boolean activoDepartamento;
    private boolean activoLaboratorio;
    //
    private List<EncargadoLaboratorio> listaEncargadosLaboratorios;
    private List<EncargadoLaboratorio> filtrarListaEncargadosLaboratorios;
    //
    private Column numeroIDTabla, nombresTabla, apellidosTabla, correoTabla, facultadTabla, departamentoTabla, laboratorioTabla, estadoTabla;
    //
    private String altoTabla;
    //
    private boolean activarExport;
    //
    private String nuevoNombresEncargadoLaboratorio, nuevoApellidoEncargadoLaboratorio, nuevoCorreoEncargadoLaboratorio, nuevoIdentificacionEncargadoLaboratorio, nuevoNumero1EncargadoLaboratorio, nuevoNumero2EncargadoLaboratorio, nuevoDireccionEncargadoLaboratorio;
    private String nuevoUserEncargadoLaboratorio, nuevoPassEncargadoLaboratorio;
    private Facultad nuevoFacultadEncargadoLaboratorio;
    private Departamento nuevoDepartamentoEncargadoLaboratorio;
    private Laboratorio nuevoLaboratorioEncargadoLaboratorio;
    private boolean activoNuevoDepartamento;
    private boolean activoNuevoLaboratorio;

    public ControllerAdministrarEncargadosLaboratorios() {
    }

    @PostConstruct
    public void init() {
        activarExport = true;
        activoDepartamento = true;
        activoLaboratorio = true;
        activoNuevoDepartamento = true;
        activoNuevoLaboratorio = true;
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        parametroLaboratorio = new Laboratorio();
        parametroEstado = true;
        todosEncargadosLaboratorios = false;
        listaFacultades = administrarEncargadosLaboratoriosBO.obtenerListaFacultades();
        altoTabla = "150";
        inicializarFiltros();
        listaEncargadosLaboratorios = null;
        filtrarListaEncargadosLaboratorios = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroApellido", null);
        filtros.put("parametroDocumento", null);
        filtros.put("parametroCorreo", null);
        filtros.put("parametroLaboratorio", null);
        filtros.put("parametroEstado", null);
        filtros.put("parametroDepartamento", null);
        filtros.put("parametroFacultad", null);
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
        if (todosEncargadosLaboratorios == false) {
            if (parametroEstado == true) {
                filtros.put("parametroEstado", "true");
            } else {
                filtros.put("parametroEstado", "false");
            }
        }
        if (parametroDepartamento.getIddepartamento() != null) {
            filtros.put("parametroDepartamento", parametroDepartamento.getIddepartamento().toString());
        }
        if (parametroFacultad.getIdfacultad() != null) {
            filtros.put("parametroFacultad", parametroFacultad.getIdfacultad().toString());
        }
        if (parametroLaboratorio.getIdlaboratorio() != null) {
            filtros.put("parametroLaboratorio", parametroLaboratorio.getIdlaboratorio().toString());
        }
    }

    public void buscarEncargadosLaboratoriosPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaEncargadosLaboratorios = null;
            listaEncargadosLaboratorios = administrarEncargadosLaboratoriosBO.consultarEncargadoLaboratoriosPorParametro(filtros);
            if (listaEncargadosLaboratorios != null) {
                if (listaEncargadosLaboratorios.size() > 0) {
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
            System.out.println("Error ControllerAdministrarEncargadosLaboratorios buscarEncargadosLaboratoriosPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        desactivarFiltrosTabla();
        activarExport = true;
        activoDepartamento = true;
        activoLaboratorio = true;
        parametroNombre = null;
        parametroApellido = null;
        parametroDocumento = null;
        parametroCorreo = null;
        parametroDepartamento = new Departamento();
        parametroFacultad = new Facultad();
        parametroLaboratorio = new Laboratorio();
        parametroEstado = true;
        todosEncargadosLaboratorios = false;
        inicializarFiltros();
        listaEncargadosLaboratorios = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void modificacionTodosEncargadosLaboratorios() {
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
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("width: 80px");
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("width: 80px");
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
        facultadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:facultadTabla");
        facultadTabla.setFilterStyle("display: none; visibility: hidden;");
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("display: none; visibility: hidden;");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaEncargadosLaboratorios = null;
    }

    public void actualizarFacultades() {
        try {
            if (Utilidades.validarNulo(parametroFacultad)) {
                activoDepartamento = false;
                parametroDepartamento = new Departamento();
                activoLaboratorio = true;
                parametroLaboratorio = new Laboratorio();
                listaLaboratorios = null;
                listaDepartamentos = administrarEncargadosLaboratoriosBO.obtenerDepartamentosPorIDFacultad(parametroFacultad.getIdfacultad());
            } else {
                activoDepartamento = true;
                listaDepartamentos = null;
                parametroDepartamento = new Departamento();
                activoLaboratorio = true;
                parametroLaboratorio = new Laboratorio();
                listaLaboratorios = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:parametroDepartamento");
            context.update("formT:form:parametroLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarEncargadosLaboratorios actualizarFacultades : " + e.toString());
        }
    }

    public void actualizarNuevoFacultades() {
        try {
            if (Utilidades.validarNulo(nuevoFacultadEncargadoLaboratorio)) {
                activoNuevoDepartamento = false;
                nuevoDepartamentoEncargadoLaboratorio = new Departamento();
                listaDepartamentos = administrarEncargadosLaboratoriosBO.obtenerDepartamentosPorIDFacultad(nuevoFacultadEncargadoLaboratorio.getIdfacultad());
                activoNuevoLaboratorio = true;
                nuevoLaboratorioEncargadoLaboratorio = new Laboratorio();
                listaLaboratorios = null;
            } else {
                listaDepartamentos = null;
                activoNuevoDepartamento = false;
                nuevoDepartamentoEncargadoLaboratorio = new Departamento();
                activoNuevoLaboratorio = true;
                nuevoLaboratorioEncargadoLaboratorio = new Laboratorio();
                listaLaboratorios = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:formularioDialogos:nuevoDepartamentoEncargadoLaboratorio");
            context.update("formT:formularioDialogos:nuevoLaboratorioEncargadoLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarEncargadosLaboratorios actualizarNuevoFacultades : " + e.toString());
        }
    }

    public void actualizarDepartamentos() {
        try {
            if (Utilidades.validarNulo(parametroDepartamento)) {
                activoLaboratorio = false;
                parametroLaboratorio = new Laboratorio();
                listaLaboratorios = administrarEncargadosLaboratoriosBO.obtenerLaboratoriosPorIDDepartamento(parametroDepartamento.getIddepartamento());
            } else {
                activoLaboratorio = true;
                listaLaboratorios = null;
                parametroLaboratorio = new Laboratorio();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:parametroLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarEncargadosLaboratorios actualizarDepartamentos : " + e.toString());
        }
    }

    public void actualizarNuevoDepartamento() {
        try {
            if (Utilidades.validarNulo(nuevoDepartamentoEncargadoLaboratorio)) {
                activoNuevoLaboratorio = false;
                nuevoLaboratorioEncargadoLaboratorio = new Laboratorio();
                listaLaboratorios = administrarEncargadosLaboratoriosBO.obtenerLaboratoriosPorIDDepartamento(nuevoDepartamentoEncargadoLaboratorio.getIddepartamento());
            } else {
                listaLaboratorios = null;
                activoNuevoLaboratorio = false;
                nuevoLaboratorioEncargadoLaboratorio = new Laboratorio();
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:formularioDialogos:nuevoLaboratorioEncargadoLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerAdministrarEncargadosLaboratorios actualizarNuevoDepartamento : " + e.toString());
        }
    }

    public String verDetallesEncargadoLaboratorio() {
        limpiarProcesoBusqueda();
        return "detallesencargadolaboratorio";
    }

    public void dispararDialogoNuevoEncargadoLaboratorio() {
        limpiarRegistroEncargadoLaboratorio();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroEncargadoLaboratorio");
        context.execute("NuevoRegistroEncargadoLaboratorio.show()");
    }

    public void limpiarRegistroEncargadoLaboratorio() {
        listaDepartamentos = null;
        activoNuevoDepartamento = true;
        activoLaboratorio = true;
        listaLaboratorios = null;
        nuevoDepartamentoEncargadoLaboratorio = new Departamento();
        nuevoApellidoEncargadoLaboratorio = null;
        nuevoLaboratorioEncargadoLaboratorio = new Laboratorio();
        nuevoCorreoEncargadoLaboratorio = null;
        nuevoDireccionEncargadoLaboratorio = null;
        nuevoFacultadEncargadoLaboratorio = new Facultad();
        nuevoIdentificacionEncargadoLaboratorio = null;
        nuevoNombresEncargadoLaboratorio = null;
        nuevoNumero1EncargadoLaboratorio = null;
        nuevoNumero2EncargadoLaboratorio = null;
        nuevoPassEncargadoLaboratorio = null;
        nuevoUserEncargadoLaboratorio = null;
    }

    public boolean validarNombreApellidoEncargadoLaboratorio() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nuevoNombresEncargadoLaboratorio)) && (Utilidades.validarNulo(nuevoApellidoEncargadoLaboratorio))) {
            if (!Utilidades.validarCaracterString(nuevoNombresEncargadoLaboratorio)) {
                retorno = false;
            }
            if (!Utilidades.validarCaracterString(nuevoApellidoEncargadoLaboratorio)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarCorreoEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoCorreoEncargadoLaboratorio)) {
            if (Utilidades.validarCorreoElectronico(nuevoCorreoEncargadoLaboratorio)) {
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarIdentificacionEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoIdentificacionEncargadoLaboratorio)) {
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoNumero1EncargadoLaboratorio)) {
            if ((Utilidades.isNumber(nuevoNumero1EncargadoLaboratorio)) == false) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(nuevoNumero2EncargadoLaboratorio)) {
            if ((Utilidades.isNumber(nuevoNumero2EncargadoLaboratorio)) == false) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarDireccionEncargadoLaboratorio() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(nuevoDireccionEncargadoLaboratorio)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosUniversidadEncargadoLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoLaboratorioEncargadoLaboratorio) && Utilidades.validarNulo(nuevoFacultadEncargadoLaboratorio) && Utilidades.validarNulo(nuevoDepartamentoEncargadoLaboratorio)) {
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validaEncargadoLaboratorioYaRegistrado() {
        boolean retorno = true;
        EncargadoLaboratorio encargadoRegistrado = administrarEncargadosLaboratoriosBO.obtenerEncargadoLaboratorioPorCorreoNumDocumento(nuevoCorreoEncargadoLaboratorio, nuevoIdentificacionEncargadoLaboratorio);
        if (encargadoRegistrado != null) {
            retorno = false;
        }
        return retorno;
    }

    public void registrarNuevoEncargadoLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarNombreApellidoEncargadoLaboratorio() == true) {
            if (validarCorreoEncargadoLaboratorio() == true) {
                if (validarIdentificacionEncargadoLaboratorio() == true) {
                    if (validarDatosNumericosEncargadoLaboratorio() == true) {
                        if (validarDireccionEncargadoLaboratorio() == true) {
                            if (validarDatosUniversidadEncargadoLaboratorio() == true) {
                                if (validaEncargadoLaboratorioYaRegistrado() == true) {
                                    almacenarNuevoEncargadoLaboratorioEnSistema();
                                } else {
                                    context.execute("errorEncargadoLaboratorioRegistrado.show()");
                                }
                            } else {
                                context.execute("errorUniversidadEncargadoLaboratorio.show()");
                            }
                        } else {
                            context.execute("errorDireccionEncargadoLaboratorio.show()");
                        }
                    } else {
                        context.execute("errorNumerosEncargadoLaboratorio.show()");
                    }
                } else {
                    context.execute("errorDocumentoEncargadoLaboratorio.show()");
                }
            } else {
                context.execute("errorEmailEncargadoLaboratorio.show()");
            }
        } else {
            context.execute("errorNombreApellidoEncargadoLaboratorio.show()");
        }
    }

    public void almacenarNuevoEncargadoLaboratorioEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroEncargadoLaboratorio.hide()");
        try {
            Usuario usuarioNuevo = new Usuario();
            usuarioNuevo.setEstado(true);
            usuarioNuevo.setNombreusuario(nuevoUserEncargadoLaboratorio);
            usuarioNuevo.setPasswordusuario(nuevoPassEncargadoLaboratorio);
            Persona personaNueva = new Persona();
            personaNueva.setApellidospersona(nuevoApellidoEncargadoLaboratorio);
            personaNueva.setDireccionpersona(nuevoDireccionEncargadoLaboratorio);
            personaNueva.setEmailpersona(nuevoCorreoEncargadoLaboratorio);
            personaNueva.setIdentificacionpersona(nuevoIdentificacionEncargadoLaboratorio);
            personaNueva.setNombrespersona(nuevoNombresEncargadoLaboratorio);
            personaNueva.setTelefono1persona(nuevoNumero1EncargadoLaboratorio);
            personaNueva.setTelefono2persona(nuevoNumero2EncargadoLaboratorio);
            EncargadoLaboratorio encargadoLaboratorioNuevo = new EncargadoLaboratorio();
            encargadoLaboratorioNuevo.setLaboratorio(nuevoLaboratorioEncargadoLaboratorio);
            administrarEncargadosLaboratoriosBO.almacenarNuevoEncargadoLaboratorioEnSistema(usuarioNuevo, personaNueva, encargadoLaboratorioNuevo);
            context.execute("registroExitosoEncargadoLaboratorio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerLogin almacenarNuevoEncargadoLaboratorioEnSistema : " + e.toString());
            context.execute("registroFallidoEncargadoLaboratorio.show()");
        }
    }

    public void modificarIdentificacionEncargadoLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean validar = validarIdentificacionEncargadoLaboratorio();
        if (validar == true) {
            nuevoUserEncargadoLaboratorio = nuevoIdentificacionEncargadoLaboratorio;
            nuevoPassEncargadoLaboratorio = nuevoIdentificacionEncargadoLaboratorio;
        } else {
            nuevoUserEncargadoLaboratorio = null;
            nuevoPassEncargadoLaboratorio = null;
        }
        context.update("formT:formularioDialogos:nuevoPasswordEncargadoLaboratorio");
        context.update("formT:formularioDialogos:nuevoUsuarioEncargadoLaboratorio");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "Administrar_EncargadoLab_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Administrar_EncargadoLab_XLS", false, false, "UTF-8", null, null);
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

    public boolean isParametroEstado() {
        return parametroEstado;
    }

    public void setParametroEstado(boolean parametroEstado) {
        this.parametroEstado = parametroEstado;
    }

    public boolean isTodosEncargadosLaboratorios() {
        return todosEncargadosLaboratorios;
    }

    public void setTodosEncargadosLaboratorios(boolean todosEncargadosLaboratorios) {
        this.todosEncargadosLaboratorios = todosEncargadosLaboratorios;
    }

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
    }

    public List<EncargadoLaboratorio> getListaEncargadosLaboratorios() {
        return listaEncargadosLaboratorios;
    }

    public void setListaEncargadosLaboratorios(List<EncargadoLaboratorio> listaEncargadosLaboratorios) {
        this.listaEncargadosLaboratorios = listaEncargadosLaboratorios;
    }

    public List<EncargadoLaboratorio> getFiltrarListaEncargadosLaboratorios() {
        return filtrarListaEncargadosLaboratorios;
    }

    public void setFiltrarListaEncargadosLaboratorios(List<EncargadoLaboratorio> filtrarListaEncargadosLaboratorios) {
        this.filtrarListaEncargadosLaboratorios = filtrarListaEncargadosLaboratorios;
    }

    public boolean isActivoDepartamento() {
        return activoDepartamento;
    }

    public void setActivoDepartamento(boolean activoDepartamento) {
        this.activoDepartamento = activoDepartamento;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
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

    public String getNuevoNombresEncargadoLaboratorio() {
        return nuevoNombresEncargadoLaboratorio;
    }

    public void setNuevoNombresEncargadoLaboratorio(String nuevoNombresEncargadoLaboratorio) {
        this.nuevoNombresEncargadoLaboratorio = nuevoNombresEncargadoLaboratorio;
    }

    public String getNuevoApellidoEncargadoLaboratorio() {
        return nuevoApellidoEncargadoLaboratorio;
    }

    public void setNuevoApellidoEncargadoLaboratorio(String nuevoApellidoEncargadoLaboratorio) {
        this.nuevoApellidoEncargadoLaboratorio = nuevoApellidoEncargadoLaboratorio;
    }

    public String getNuevoCorreoEncargadoLaboratorio() {
        return nuevoCorreoEncargadoLaboratorio;
    }

    public void setNuevoCorreoEncargadoLaboratorio(String nuevoCorreoEncargadoLaboratorio) {
        this.nuevoCorreoEncargadoLaboratorio = nuevoCorreoEncargadoLaboratorio;
    }

    public String getNuevoIdentificacionEncargadoLaboratorio() {
        return nuevoIdentificacionEncargadoLaboratorio;
    }

    public void setNuevoIdentificacionEncargadoLaboratorio(String nuevoIdentificacionEncargadoLaboratorio) {
        this.nuevoIdentificacionEncargadoLaboratorio = nuevoIdentificacionEncargadoLaboratorio;
    }

    public String getNuevoNumero1EncargadoLaboratorio() {
        return nuevoNumero1EncargadoLaboratorio;
    }

    public void setNuevoNumero1EncargadoLaboratorio(String nuevoNumero1EncargadoLaboratorio) {
        this.nuevoNumero1EncargadoLaboratorio = nuevoNumero1EncargadoLaboratorio;
    }

    public String getNuevoNumero2EncargadoLaboratorio() {
        return nuevoNumero2EncargadoLaboratorio;
    }

    public void setNuevoNumero2EncargadoLaboratorio(String nuevoNumero2EncargadoLaboratorio) {
        this.nuevoNumero2EncargadoLaboratorio = nuevoNumero2EncargadoLaboratorio;
    }

    public String getNuevoDireccionEncargadoLaboratorio() {
        return nuevoDireccionEncargadoLaboratorio;
    }

    public void setNuevoDireccionEncargadoLaboratorio(String nuevoDireccionEncargadoLaboratorio) {
        this.nuevoDireccionEncargadoLaboratorio = nuevoDireccionEncargadoLaboratorio;
    }

    public Facultad getNuevoFacultadEncargadoLaboratorio() {
        return nuevoFacultadEncargadoLaboratorio;
    }

    public void setNuevoFacultadEncargadoLaboratorio(Facultad nuevoFacultadEncargadoLaboratorio) {
        this.nuevoFacultadEncargadoLaboratorio = nuevoFacultadEncargadoLaboratorio;
    }

    public Departamento getNuevoDepartamentoEncargadoLaboratorio() {
        return nuevoDepartamentoEncargadoLaboratorio;
    }

    public void setNuevoDepartamentoEncargadoLaboratorio(Departamento nuevoDepartamentoEncargadoLaboratorio) {
        this.nuevoDepartamentoEncargadoLaboratorio = nuevoDepartamentoEncargadoLaboratorio;
    }

    public boolean isActivoNuevoDepartamento() {
        return activoNuevoDepartamento;
    }

    public void setActivoNuevoDepartamento(boolean activoNuevoDepartamento) {
        this.activoNuevoDepartamento = activoNuevoDepartamento;
    }

    public String getNuevoUserEncargadoLaboratorio() {
        return nuevoUserEncargadoLaboratorio;
    }

    public void setNuevoUserEncargadoLaboratorio(String nuevoUserEncargadoLaboratorio) {
        this.nuevoUserEncargadoLaboratorio = nuevoUserEncargadoLaboratorio;
    }

    public String getNuevoPassEncargadoLaboratorio() {
        return nuevoPassEncargadoLaboratorio;
    }

    public void setNuevoPassEncargadoLaboratorio(String nuevoPassEncargadoLaboratorio) {
        this.nuevoPassEncargadoLaboratorio = nuevoPassEncargadoLaboratorio;
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

    public boolean isActivoLaboratorio() {
        return activoLaboratorio;
    }

    public void setActivoLaboratorio(boolean activoLaboratorio) {
        this.activoLaboratorio = activoLaboratorio;
    }

    public Laboratorio getNuevoLaboratorioEncargadoLaboratorio() {
        return nuevoLaboratorioEncargadoLaboratorio;
    }

    public void setNuevoLaboratorioEncargadoLaboratorio(Laboratorio nuevoLaboratorioEncargadoLaboratorio) {
        this.nuevoLaboratorioEncargadoLaboratorio = nuevoLaboratorioEncargadoLaboratorio;
    }

    public boolean isActivoNuevoLaboratorio() {
        return activoNuevoLaboratorio;
    }

    public void setActivoNuevoLaboratorio(boolean activoNuevoLaboratorio) {
        this.activoNuevoLaboratorio = activoNuevoLaboratorio;
    }

    public boolean isActivarExport() {
        return activarExport;
    }

    public void setActivarExport(boolean activarExport) {
        this.activarExport = activarExport;
    }

}

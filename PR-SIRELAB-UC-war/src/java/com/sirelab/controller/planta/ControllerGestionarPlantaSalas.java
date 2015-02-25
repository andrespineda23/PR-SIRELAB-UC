package com.sirelab.controller.planta;

import com.sirelab.bo.interfacebo.GestionarPlantaSalasBOInterface;
import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Edificio;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.SalaLaboratorio;
import com.sirelab.entidades.Sede;
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
public class ControllerGestionarPlantaSalas implements Serializable {

    @EJB
    GestionarPlantaSalasBOInterface gestionarPlantaSalasBO;

    private String parametroNombre, parametroCodigo, parametroCapacidad;
    private boolean parametroEstado;
    private boolean activarTodosSala;
    private List<Departamento> listaDepartamentos;
    private Departamento parametroDepartamento;
    private List<Laboratorio> listaLaboratorios;
    private Laboratorio parametroLaboratorio;
    private List<AreaProfundizacion> listaAreasProfundizacion;
    private AreaProfundizacion parametroAreaProfundizacion;
    private List<Sede> listaSedes;
    private Sede parametroSede;
    private List<Edificio> listaEdificios;
    private Edificio parametroEdificio;
    private boolean activarLaboratorio;
    private boolean activarAreaProfundizacion;
    private boolean activarEdificio;
    private boolean activarNuevoLaboratorio;
    private boolean activarNuevoAreaProfundizacion;
    private boolean activarNuevoEdificio;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<SalaLaboratorio> listaSalasLaboratorios;
    private List<SalaLaboratorio> filtrarListaSalasLaboratorios;
    //
    private Column nombreTabla, codigoTabla, estadoTabla, capacidadTabla, laboratorioTabla, departamentoTabla, areaTabla, sedeTabla, edificioTabla;
    //
    private String altoTabla;
    //
    private String nuevoNombreSala, nuevoCodigoSala, nuevoDescripcionSala, nuevoUbicacionSala, nuevoCapacidadSala, nuevoCostoSala, nuevoInversionSala;
    private Departamento nuevoDepartamentoSala;
    private Laboratorio nuevoLaboratorioSala;
    private AreaProfundizacion nuevoAreaProfundizacionSala;
    private Sede nuevoSedeSala;
    private Edificio nuevoEdificioSala;

    public ControllerGestionarPlantaSalas() {
    }

    @PostConstruct
    public void init() {
        activarTodosSala = false;
        activarLaboratorio = true;
        activarEdificio = true;
        activarAreaProfundizacion = true;
        activarNuevoLaboratorio = true;
        activarNuevoAreaProfundizacion = true;
        activarNuevoEdificio = true;
        activarExport = true;
        parametroNombre = null;
        parametroCodigo = null;
        parametroCapacidad = null;
        parametroAreaProfundizacion = new AreaProfundizacion();
        parametroDepartamento = new Departamento();
        parametroLaboratorio = new Laboratorio();
        parametroEdificio = new Edificio();
        parametroSede = new Sede();
        listaSedes = gestionarPlantaSalasBO.consultarSedesRegistradas();
        listaDepartamentos = gestionarPlantaSalasBO.consultarDepartamentosRegistrados();
        altoTabla = "150";
        inicializarFiltros();
        listaAreasProfundizacion = null;
        listaLaboratorios = null;
        listaEdificios = null;
        filtrarListaSalasLaboratorios = null;
        parametroEstado = true;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroCodigo", null);
        filtros.put("parametroCapacidad", null);
        filtros.put("parametroEstado", null);
        filtros.put("parametroAreaProfundizacion", null);
        filtros.put("parametroDepartamento", null);
        filtros.put("parametroLaboratorio", null);
        filtros.put("parametroEdificio", null);
        filtros.put("parametroSede", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if (activarTodosSala == false) {
            if (parametroEstado == true) {
                filtros.put("parametroEstado", "true");
            } else {
                filtros.put("parametroEstado", "false");
            }
        }
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroCapacidad) == true) && (!parametroCapacidad.isEmpty())) {
            filtros.put("parametroCapacidad", parametroCapacidad.toString());
        }
        if ((Utilidades.validarNulo(parametroCodigo) == true) && (!parametroCodigo.isEmpty())) {
            filtros.put("parametroCodigo", parametroCodigo.toString());
        }
        if (parametroDepartamento.getIddepartamento() != null) {
            filtros.put("parametroDepartamento", parametroDepartamento.getIddepartamento().toString());
        }
        if (parametroLaboratorio.getIdlaboratorio() != null) {
            filtros.put("parametroLaboratorio", parametroLaboratorio.getIdlaboratorio().toString());
        }
        if (parametroEdificio.getIdedificio() != null) {
            filtros.put("parametroEdificio", parametroEdificio.getIdedificio().toString());
        }
        if (parametroSede.getIdsede() != null) {
            filtros.put("parametroSede", parametroSede.getIdsede().toString());
        }
        if (parametroAreaProfundizacion.getIdareaprofundizacion() != null) {
            filtros.put("parametroAreaProfundizacion", parametroAreaProfundizacion.getIdareaprofundizacion().toString());
        }
    }

    public void buscarSalasLaboratorioPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaSalasLaboratorios = null;
            listaSalasLaboratorios = gestionarPlantaSalasBO.consultarSalasLaboratoriosPorParametro(filtros);
            if (listaSalasLaboratorios != null) {
                if (listaSalasLaboratorios.size() > 0) {
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
            System.out.println("Error ControllerGestionarPlantaSalas buscarSalasLaboratorioPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarAreaProfundizacion = true;
        activarEdificio = true;
        activarLaboratorio = true;
        parametroEstado = true;
        activarTodosSala = false;
        if (null != listaSalasLaboratorios) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroNombre = null;
        parametroCodigo = null;
        parametroCapacidad = null;
        parametroAreaProfundizacion = new AreaProfundizacion();
        parametroDepartamento = new Departamento();
        parametroLaboratorio = new Laboratorio();
        parametroEdificio = new Edificio();
        parametroSede = new Sede();
        inicializarFiltros();
        listaAreasProfundizacion = null;
        listaEdificios = null;
        listaLaboratorios = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("width: 80px");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("width: 80px");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("width: 80px");
        capacidadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:capacidadTabla");
        capacidadTabla.setFilterStyle("width: 80px");

        areaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:areaTabla");
        areaTabla.setFilterStyle("width: 80px");
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("width: 80px");
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("width: 80px");
        sedeTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:sedeTabla");
        sedeTabla.setFilterStyle("width: 80px");
        edificioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:edificioTabla");
        edificioTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("display: none; visibility: hidden;");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("display: none; visibility: hidden;");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("display: none; visibility: hidden;");
        capacidadTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:capacidadTabla");
        capacidadTabla.setFilterStyle("display: none; visibility: hidden;");

        areaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:areaTabla");
        areaTabla.setFilterStyle("display: none; visibility: hidden;");
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("display: none; visibility: hidden;");
        departamentoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:departamentoTabla");
        departamentoTabla.setFilterStyle("display: none; visibility: hidden;");
        sedeTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:sedeTabla");
        sedeTabla.setFilterStyle("display: none; visibility: hidden;");
        edificioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:edificioTabla");
        edificioTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaSalasLaboratorios = null;
    }

    public void dispararDialogoNuevoSalaLaboratorio() {
        limpiarRegistroSalaLaboratorio();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroSalaLaboratorio");
        context.execute("NuevoRegistroSalaLaboratorio.show()");
    }

    public void limpiarRegistroSalaLaboratorio() {
        activarNuevoLaboratorio = true;
        activarNuevoAreaProfundizacion = true;
        activarNuevoEdificio = true;
        nuevoNombreSala = null;
        nuevoCodigoSala = null;
        nuevoUbicacionSala = null;
        nuevoDescripcionSala = null;
        nuevoCostoSala = null;
        nuevoCapacidadSala = null;
        nuevoInversionSala = null;
        nuevoLaboratorioSala = null;
        nuevoDepartamentoSala = null;
        nuevoAreaProfundizacionSala = null;
        nuevoSedeSala = null;
        nuevoEdificioSala = null;
        listaAreasProfundizacion = null;
        listaEdificios = null;
        listaEdificios = null;
    }

    public boolean validarStringSalaLaboratorio() {
        boolean retorno = true;

        if ((Utilidades.validarNulo(nuevoNombreSala)) && (Utilidades.validarNulo(nuevoCodigoSala))
                && (Utilidades.validarNulo(nuevoUbicacionSala)) && (Utilidades.validarNulo(nuevoDescripcionSala))) {
            if (!Utilidades.validarCaracterString(nuevoNombreSala)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarEstructuraSalaLaboratorio() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(nuevoAreaProfundizacionSala)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(nuevoLaboratorioSala)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(nuevoDepartamentoSala)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(nuevoSedeSala)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(nuevoEdificioSala)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosSalaLaboratorio() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nuevoCostoSala)) && (Utilidades.validarNulo(nuevoCapacidadSala))) {
            if (!Utilidades.isNumberLong(nuevoCostoSala)) {
                retorno = false;
            }
            if (!Utilidades.isNumber(nuevoCapacidadSala)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        if (Utilidades.validarNulo(nuevoInversionSala)) {
            if (!Utilidades.isNumber(nuevoInversionSala)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoSalaLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringSalaLaboratorio() == true) {
            if (validarEstructuraSalaLaboratorio() == true) {
                if (validarDatosNumericosSalaLaboratorio() == true) {
                    almacenaNuevoSalaEnSistema();
                } else {
                    context.execute("errorNumerosSalaLaboratorio.show()");
                }
            } else {
                context.execute("errorEstructuraSalaLaboratorio.show()");
            }
        } else {
            context.execute("errorInformacionSalaLaboratorio.show()");
        }
    }

    public void almacenaNuevoSalaEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroSalaLaboratorio.hide()");
        try {
            SalaLaboratorio salaNuevo = new SalaLaboratorio();
            salaNuevo.setNombresala(nuevoNombreSala);
            salaNuevo.setCodigosala(nuevoCodigoSala);
            salaNuevo.setPisoubicacion(nuevoUbicacionSala);
            salaNuevo.setDescripcionsala(nuevoDescripcionSala);
            salaNuevo.setCostoalquiler(Long.valueOf(nuevoCostoSala).longValue());
            salaNuevo.setEstadosala(true);
            salaNuevo.setCapacidadsala(Integer.valueOf(nuevoCapacidadSala).intValue());
            salaNuevo.setValorinversion(new BigInteger(nuevoInversionSala));
            salaNuevo.setEdificio(parametroEdificio);
            salaNuevo.setAreaprofundizacion(parametroAreaProfundizacion);
            gestionarPlantaSalasBO.crearNuevaSalaLaboratorio(salaNuevo);
            context.execute("registroExitosoSalaLaboratorio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlantaSalas almacenaNuevoSalaEnSistema : " + e.toString());
            context.execute("registroFallidoSalaLaboratorio.show()");
        }
    }

    public void actualizarDepartamentos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroDepartamento)) {
            parametroLaboratorio = new Laboratorio();
            listaLaboratorios = gestionarPlantaSalasBO.consultarLaboratoriosPorIDDepartamento(parametroDepartamento.getIddepartamento());
            activarLaboratorio = false;
            parametroAreaProfundizacion = new AreaProfundizacion();
            activarAreaProfundizacion = true;
            listaAreasProfundizacion = null;
        } else {
            parametroLaboratorio = new Laboratorio();
            activarLaboratorio = true;
            listaLaboratorios = null;
            parametroAreaProfundizacion = new AreaProfundizacion();
            activarAreaProfundizacion = true;
            listaAreasProfundizacion = null;
        }
        context.update("formT:form:parametroLaboratorio");
        context.update("formT:form:parametroAreaProfundizacion");
    }

    public void actualizarLaboratorios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroLaboratorio)) {
            parametroAreaProfundizacion = new AreaProfundizacion();
            listaAreasProfundizacion = gestionarPlantaSalasBO.consultarAreasProfundizacionPorIDLaboratorio(parametroLaboratorio.getIdlaboratorio());
            activarAreaProfundizacion = false;
        } else {
            parametroAreaProfundizacion = new AreaProfundizacion();
            activarAreaProfundizacion = true;
            listaAreasProfundizacion = null;
        }
        context.update("formT:form:parametroAreaProfundizacion");
    }

    public void actualizarSedes() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroSede)) {
            parametroEdificio = new Edificio();
            listaEdificios = gestionarPlantaSalasBO.consultarEdificiosPorIDSede(parametroSede.getIdsede());
            activarEdificio = false;
        } else {
            parametroEdificio = new Edificio();
            activarEdificio = true;
            listaEdificios = null;
        }
        context.update("formT:form:parametroEdificio");
    }

    public void actualizarNuevoDepartamentos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(nuevoDepartamentoSala)) {
            nuevoLaboratorioSala = null;
            listaLaboratorios = gestionarPlantaSalasBO.consultarLaboratoriosPorIDDepartamento(nuevoDepartamentoSala.getIddepartamento());
            activarNuevoLaboratorio = false;
            nuevoAreaProfundizacionSala = null;
            listaAreasProfundizacion = null;
            activarNuevoAreaProfundizacion = true;
        } else {
            activarNuevoLaboratorio = true;
            listaLaboratorios = null;
            nuevoLaboratorioSala = null;
            nuevoAreaProfundizacionSala = null;
            listaAreasProfundizacion = null;
            activarNuevoAreaProfundizacion = true;
        }
        context.update("formT:formularioDialogos:nuevoLaboratorioSalaLaboratorio");
        context.update("formT:formularioDialogos:nuevoAreaProfundizacionSalaLaboratorio");
    }

    public void actualizarNuevoLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(nuevoLaboratorioSala)) {
            nuevoAreaProfundizacionSala = null;
            listaAreasProfundizacion = gestionarPlantaSalasBO.consultarAreasProfundizacionPorIDLaboratorio(nuevoLaboratorioSala.getIdlaboratorio());
            activarNuevoAreaProfundizacion = false;
        } else {
            nuevoAreaProfundizacionSala = null;
            listaAreasProfundizacion = null;
            activarNuevoAreaProfundizacion = true;
        }
        context.update("formT:formularioDialogos:nuevoAreaProfundizacionSalaLaboratorio");
    }

    public void actualizarNuevoSede() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(nuevoSedeSala)) {
            nuevoEdificioSala = null;
            listaEdificios = gestionarPlantaSalasBO.consultarEdificiosPorIDSede(nuevoSedeSala.getIdsede());
            activarNuevoEdificio = false;
        } else {
            nuevoEdificioSala = null;
            listaEdificios = null;
            activarNuevoEdificio = true;
        }
        context.update("formT:formularioDialogos:nuevoEdificioSalaLaboratorio");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Plata_Salas_Laboratorio_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Plata_Salas_Laboratorio_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void modificacionTodosSalas() {
        RequestContext.getCurrentInstance().update("formT:form:parametroEstado");
    }
    
    public String cambiarPaginaDetalles(){
        limpiarProcesoBusqueda();
        return "detallesplantasala";
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

    public String getParametroCapacidad() {
        return parametroCapacidad;
    }

    public void setParametroCapacidad(String parametroCapacidad) {
        this.parametroCapacidad = parametroCapacidad;
    }

    public boolean isParametroEstado() {
        return parametroEstado;
    }

    public void setParametroEstado(boolean parametroEstado) {
        this.parametroEstado = parametroEstado;
    }

    public boolean isActivarTodosSala() {
        return activarTodosSala;
    }

    public void setActivarTodosSala(boolean activarTodosSala) {
        this.activarTodosSala = activarTodosSala;
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

    public List<AreaProfundizacion> getListaAreasProfundizacion() {
        return listaAreasProfundizacion;
    }

    public void setListaAreasProfundizacion(List<AreaProfundizacion> listaAreasProfundizacion) {
        this.listaAreasProfundizacion = listaAreasProfundizacion;
    }

    public AreaProfundizacion getParametroAreaProfundizacion() {
        return parametroAreaProfundizacion;
    }

    public void setParametroAreaProfundizacion(AreaProfundizacion parametroAreaProfundizacion) {
        this.parametroAreaProfundizacion = parametroAreaProfundizacion;
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

    public List<Edificio> getListaEdificios() {
        return listaEdificios;
    }

    public void setListaEdificios(List<Edificio> listaEdificios) {
        this.listaEdificios = listaEdificios;
    }

    public Edificio getParametroEdificio() {
        return parametroEdificio;
    }

    public void setParametroEdificio(Edificio parametroEdificio) {
        this.parametroEdificio = parametroEdificio;
    }

    public boolean isActivarLaboratorio() {
        return activarLaboratorio;
    }

    public void setActivarLaboratorio(boolean activarLaboratorio) {
        this.activarLaboratorio = activarLaboratorio;
    }

    public boolean isActivarAreaProfundizacion() {
        return activarAreaProfundizacion;
    }

    public void setActivarAreaProfundizacion(boolean activarAreaProfundizacion) {
        this.activarAreaProfundizacion = activarAreaProfundizacion;
    }

    public boolean isActivarEdificio() {
        return activarEdificio;
    }

    public void setActivarEdificio(boolean activarEdificio) {
        this.activarEdificio = activarEdificio;
    }

    public boolean isActivarNuevoLaboratorio() {
        return activarNuevoLaboratorio;
    }

    public void setActivarNuevoLaboratorio(boolean activarNuevoLaboratorio) {
        this.activarNuevoLaboratorio = activarNuevoLaboratorio;
    }

    public boolean isActivarNuevoAreaProfundizacion() {
        return activarNuevoAreaProfundizacion;
    }

    public void setActivarNuevoAreaProfundizacion(boolean activarNuevoAreaProfundizacion) {
        this.activarNuevoAreaProfundizacion = activarNuevoAreaProfundizacion;
    }

    public boolean isActivarNuevoEdificio() {
        return activarNuevoEdificio;
    }

    public void setActivarNuevoEdificio(boolean activarNuevoEdificio) {
        this.activarNuevoEdificio = activarNuevoEdificio;
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

    public List<SalaLaboratorio> getListaSalasLaboratorios() {
        return listaSalasLaboratorios;
    }

    public void setListaSalasLaboratorios(List<SalaLaboratorio> listaSalasLaboratorios) {
        this.listaSalasLaboratorios = listaSalasLaboratorios;
    }

    public List<SalaLaboratorio> getFiltrarListaSalasLaboratorios() {
        return filtrarListaSalasLaboratorios;
    }

    public void setFiltrarListaSalasLaboratorios(List<SalaLaboratorio> filtrarListaSalasLaboratorios) {
        this.filtrarListaSalasLaboratorios = filtrarListaSalasLaboratorios;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoNombreSala() {
        return nuevoNombreSala;
    }

    public void setNuevoNombreSala(String nuevoNombreSala) {
        this.nuevoNombreSala = nuevoNombreSala;
    }

    public String getNuevoCodigoSala() {
        return nuevoCodigoSala;
    }

    public void setNuevoCodigoSala(String nuevoCodigoSala) {
        this.nuevoCodigoSala = nuevoCodigoSala;
    }

    public String getNuevoDescripcionSala() {
        return nuevoDescripcionSala;
    }

    public void setNuevoDescripcionSala(String nuevoDescripcionSala) {
        this.nuevoDescripcionSala = nuevoDescripcionSala;
    }

    public String getNuevoUbicacionSala() {
        return nuevoUbicacionSala;
    }

    public void setNuevoUbicacionSala(String nuevoUbicacionSala) {
        this.nuevoUbicacionSala = nuevoUbicacionSala;
    }

    public String getNuevoCapacidadSala() {
        return nuevoCapacidadSala;
    }

    public void setNuevoCapacidadSala(String nuevoCapacidadSala) {
        this.nuevoCapacidadSala = nuevoCapacidadSala;
    }

    public String getNuevoCostoSala() {
        return nuevoCostoSala;
    }

    public void setNuevoCostoSala(String nuevoCostoSala) {
        this.nuevoCostoSala = nuevoCostoSala;
    }

    public String getNuevoInversionSala() {
        return nuevoInversionSala;
    }

    public void setNuevoInversionSala(String nuevoInversionSala) {
        this.nuevoInversionSala = nuevoInversionSala;
    }

    public Departamento getNuevoDepartamentoSala() {
        return nuevoDepartamentoSala;
    }

    public void setNuevoDepartamentoSala(Departamento nuevoDepartamentoSala) {
        this.nuevoDepartamentoSala = nuevoDepartamentoSala;
    }

    public Laboratorio getNuevoLaboratorioSala() {
        return nuevoLaboratorioSala;
    }

    public void setNuevoLaboratorioSala(Laboratorio nuevoLaboratorioSala) {
        this.nuevoLaboratorioSala = nuevoLaboratorioSala;
    }

    public AreaProfundizacion getNuevoAreaProfundizacionSala() {
        return nuevoAreaProfundizacionSala;
    }

    public void setNuevoAreaProfundizacionSala(AreaProfundizacion nuevoAreaProfundizacionSala) {
        this.nuevoAreaProfundizacionSala = nuevoAreaProfundizacionSala;
    }

    public Sede getNuevoSedeSala() {
        return nuevoSedeSala;
    }

    public void setNuevoSedeSala(Sede nuevoSedeSala) {
        this.nuevoSedeSala = nuevoSedeSala;
    }

    public Edificio getNuevoEdificioSala() {
        return nuevoEdificioSala;
    }

    public void setNuevoEdificioSala(Edificio nuevoEdificioSala) {
        this.nuevoEdificioSala = nuevoEdificioSala;
    }

}

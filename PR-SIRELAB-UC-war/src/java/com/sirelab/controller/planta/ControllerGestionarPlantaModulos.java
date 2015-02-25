package com.sirelab.controller.planta;

import com.sirelab.bo.interfacebo.GestionarPlantaModulosBOInterface;
import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.Edificio;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.ModuloLaboratorio;
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
public class ControllerGestionarPlantaModulos implements Serializable {

    @EJB
    GestionarPlantaModulosBOInterface gestionarPlantaModulosBO;

    private String parametroCodigo, parametroDetalle;
    private boolean parametroEstado;
    private boolean activarTodosModulo;
    private List<Laboratorio> listaLaboratorios;
    private Laboratorio parametroLaboratorio;
    private List<AreaProfundizacion> listaAreasProfundizacion;
    private AreaProfundizacion parametroAreaProfundizacion;
    private List<Sede> listaSedes;
    private Sede parametroSede;
    private List<Edificio> listaEdificios;
    private Edificio parametroEdificio;
    private List<SalaLaboratorio> listaSalasLaboratorios;
    private SalaLaboratorio parametroSalaLaboratorio;
    private boolean activarAreaProfundizacion;
    private boolean activarEdificio;
    private boolean activarSala;
    private boolean activarNuevoAreaProfundizacion;
    private boolean activarNuevoEdificio;
    private boolean activarNuevoSala;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<ModuloLaboratorio> listaModulosLaboratorios;
    private List<ModuloLaboratorio> filtrarListaModulosLaboratorios;
    //
    private Column nombreTabla, detalleTabla, estadoTabla, laboratorioTabla, areaTabla, sedeTabla, edificioTabla, salaTabla;
    //
    private String altoTabla;
    //
    private String nuevoCodigoModulo, nuevoDetalleModulo, nuevoCapacidadModulo, nuevoCostoModulo, nuevoInversionModulo;
    private Laboratorio nuevoLaboratorioModulo;
    private AreaProfundizacion nuevoAreaProfundizacionModulo;
    private Sede nuevoSedeModulo;
    private Edificio nuevoEdificioModulo;
    private SalaLaboratorio nuevoSalaLaboratorioModulo;

    public ControllerGestionarPlantaModulos() {
    }

    @PostConstruct
    public void init() {
        activarTodosModulo = false;
        activarEdificio = true;
        activarNuevoSala = true;
        activarSala = true;
        activarAreaProfundizacion = true;
        activarNuevoAreaProfundizacion = true;
        activarNuevoEdificio = true;
        activarExport = true;
        parametroCodigo = null;
        parametroDetalle = null;
        parametroAreaProfundizacion = new AreaProfundizacion();
        parametroLaboratorio = new Laboratorio();
        parametroEdificio = new Edificio();
        parametroSede = new Sede();
        parametroSalaLaboratorio = new SalaLaboratorio();
        listaSedes = gestionarPlantaModulosBO.consultarSedesRegistradas();
        listaLaboratorios = gestionarPlantaModulosBO.consultarLaboratoriosRegistrados();
        altoTabla = "150";
        inicializarFiltros();
        listaAreasProfundizacion = null;
        listaEdificios = null;
        filtrarListaModulosLaboratorios = null;
        listaSalasLaboratorios = null;
        parametroEstado = true;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroCodigo", null);
        filtros.put("parametroDetalle", null);
        filtros.put("parametroEstado", null);
        filtros.put("parametroAreaProfundizacion", null);
        filtros.put("parametroLaboratorio", null);
        filtros.put("parametroEdificio", null);
        filtros.put("parametroSede", null);
        filtros.put("parametroSalaLaboratorioLaboratorio", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if (activarTodosModulo == false) {
            if (parametroEstado == true) {
                filtros.put("parametroEstado", "true");
            } else {
                filtros.put("parametroEstado", "false");
            }
        }
        if ((Utilidades.validarNulo(parametroCodigo) == true) && (!parametroCodigo.isEmpty())) {
            filtros.put("parametroCodigo", parametroCodigo.toString());
        }
        if ((Utilidades.validarNulo(parametroDetalle) == true) && (!parametroDetalle.isEmpty())) {
            filtros.put("parametroDetalle", parametroDetalle.toString());
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
        if (parametroSalaLaboratorio.getIdsalalaboratorio() != null) {
            filtros.put("parametroSalaLaboratorio", parametroSalaLaboratorio.getIdsalalaboratorio().toString());
        }
    }

    public void buscarModulosLaboratorioPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaModulosLaboratorios = null;
            listaModulosLaboratorios = gestionarPlantaModulosBO.consultarModulosLaboratorioPorParametro(filtros);
            if (listaModulosLaboratorios != null) {
                if (listaModulosLaboratorios.size() > 0) {
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
            System.out.println("Error ControllerGestionarPlantaModulos buscarModulosLaboratorioPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarAreaProfundizacion = true;
        activarEdificio = true;
        activarSala = true;
        parametroEstado = true;
        activarTodosModulo = false;
        if (null != listaModulosLaboratorios) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroCodigo = null;
        parametroDetalle = null;
        parametroAreaProfundizacion = new AreaProfundizacion();
        parametroSalaLaboratorio = new SalaLaboratorio();
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
        detalleTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:detalleTabla");
        detalleTabla.setFilterStyle("width: 80px");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("width: 80px");

        areaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:areaTabla");
        areaTabla.setFilterStyle("width: 80px");
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("width: 80px");
        salaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:salaTabla");
        salaTabla.setFilterStyle("width: 80px");
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
        detalleTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:detalleTabla");
        detalleTabla.setFilterStyle("display: none; visibility: hidden;");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("display: none; visibility: hidden;");

        areaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:areaTabla");
        areaTabla.setFilterStyle("display: none; visibility: hidden;");
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("display: none; visibility: hidden;");
        salaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:salaTabla");
        salaTabla.setFilterStyle("display: none; visibility: hidden;");
        sedeTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:sedeTabla");
        sedeTabla.setFilterStyle("display: none; visibility: hidden;");
        edificioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:edificioTabla");
        edificioTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaModulosLaboratorios = null;
    }

    public void dispararDialogoNuevoModuloLaboratorio() {
        limpiarRegistroModuloLaboratorio();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroModuloLaboratorio");
        context.execute("NuevoRegistroModuloLaboratorio.show()");
    }

    public void limpiarRegistroModuloLaboratorio() {
        activarSala = true;
        activarNuevoAreaProfundizacion = true;
        activarNuevoEdificio = true;
        nuevoCodigoModulo = null;
        nuevoDetalleModulo = null;
        nuevoCostoModulo = null;
        nuevoCapacidadModulo = null;
        nuevoInversionModulo = null;
        nuevoLaboratorioModulo = null;
        nuevoSalaLaboratorioModulo = null;
        nuevoAreaProfundizacionModulo = null;
        nuevoSedeModulo = null;
        nuevoEdificioModulo = null;
        listaAreasProfundizacion = null;
        listaEdificios = null;
        listaSalasLaboratorios = null;
    }

    public boolean validarStringModuloLaboratorio() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nuevoCodigoModulo)) && (Utilidades.validarNulo(nuevoDetalleModulo))) {
            if (!Utilidades.validarCaracterString(nuevoCodigoModulo)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarEstructuraModuloLaboratorio() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(nuevoSalaLaboratorioModulo)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosModuloLaboratorio() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoInversionModulo)) {
            if (!Utilidades.isNumber(nuevoInversionModulo)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(nuevoCapacidadModulo)) {
            if (!Utilidades.isNumber(nuevoCapacidadModulo)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(nuevoCostoModulo)) {
            if (!Utilidades.isNumber(nuevoCostoModulo)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoModuloLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringModuloLaboratorio() == true) {
            if (validarEstructuraModuloLaboratorio() == true) {
                if (validarDatosNumericosModuloLaboratorio() == true) {
                    almacenaNuevoModuloEnSistema();
                } else {
                    context.execute("errorNumerosModuloLaboratorio.show()");
                }
            } else {
                context.execute("errorEstructuraModuloLaboratorio.show()");
            }
        } else {
            context.execute("errorInformacionModuloLaboratorio.show()");
        }
    }

    public void almacenaNuevoModuloEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroModuloLaboratorio.hide()");
        try {
            ModuloLaboratorio salaNuevo = new ModuloLaboratorio();
            salaNuevo.setCodigomodulo(nuevoCodigoModulo);
            salaNuevo.setDetallemodulo(nuevoDetalleModulo);
            salaNuevo.setCostoalquiler(new BigInteger(nuevoCostoModulo));
            salaNuevo.setCostomodulo(new BigInteger(nuevoInversionModulo));
            salaNuevo.setCapacidadmodulo(Integer.valueOf(nuevoCapacidadModulo));
            salaNuevo.setEstadomodulo(true);
            salaNuevo.setSalalaboratorio(parametroSalaLaboratorio);
            gestionarPlantaModulosBO.crearNuevoModuloLaboratorio(salaNuevo);
            context.execute("registroExitosoModuloLaboratorio.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlantaModulos almacenaNuevoModuloEnSistema : " + e.toString());
            context.execute("registroFallidoModuloLaboratorio.show()");
        }
    }

    public void actualizarLaboratorios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroLaboratorio)) {
            parametroAreaProfundizacion = new AreaProfundizacion();
            listaAreasProfundizacion = gestionarPlantaModulosBO.consultarAreasProfundizacionPorIDLaboratorio(parametroLaboratorio.getIdlaboratorio());
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
            listaEdificios = gestionarPlantaModulosBO.consultarEdificiosPorIDSede(parametroSede.getIdsede());
            activarEdificio = false;
        } else {
            parametroEdificio = new Edificio();
            activarEdificio = true;
            listaEdificios = null;
        }
        context.update("formT:form:parametroEdificio");
    }

    public void actualizarNuevoLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(nuevoLaboratorioModulo)) {
            nuevoAreaProfundizacionModulo = null;
            listaAreasProfundizacion = gestionarPlantaModulosBO.consultarAreasProfundizacionPorIDLaboratorio(nuevoLaboratorioModulo.getIdlaboratorio());
            activarNuevoAreaProfundizacion = false;
        } else {
            nuevoAreaProfundizacionModulo = null;
            listaAreasProfundizacion = null;
            activarNuevoAreaProfundizacion = true;
        }
        context.update("formT:formularioDialogos:nuevoAreaProfundizacionModuloLaboratorio");
    }

    public void actualizarNuevoSede() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(nuevoSedeModulo)) {
            nuevoEdificioModulo = null;
            listaEdificios = gestionarPlantaModulosBO.consultarEdificiosPorIDSede(nuevoSedeModulo.getIdsede());
            activarNuevoEdificio = false;
        } else {
            nuevoEdificioModulo = null;
            listaEdificios = null;
            activarNuevoEdificio = true;
        }
        context.update("formT:formularioDialogos:nuevoEdificioModuloLaboratorio");
    }

    public void buscarSalasPorParametros(int tipoRegistro) {
        RequestContext context = RequestContext.getCurrentInstance();
        Map<String, String> filtrosSala = filtrosSala = new HashMap<String, String>();
        filtrosSala.put("parametroEdificio", null);
        filtrosSala.put("parametroSede", null);
        filtrosSala.put("parametroLaboratorio", null);
        filtrosSala.put("parametroAreaProfundizacion", null);
        listaSalasLaboratorios = null;
        if (tipoRegistro == 0) {
            if (parametroLaboratorio.getIdlaboratorio() != null) {
                filtrosSala.put("parametroLaboratorio", parametroLaboratorio.getIdlaboratorio().toString());
            }
            if (parametroEdificio.getIdedificio() != null) {
                filtrosSala.put("parametroEdificio", parametroEdificio.getIdedificio().toString());
            }
            if (parametroSede.getIdsede() != null) {
                filtrosSala.put("parametroSede", parametroSede.getIdsede().toString());
            }
            if (parametroAreaProfundizacion.getIdareaprofundizacion() != null) {
                filtrosSala.put("parametroAreaProfundizacion", parametroAreaProfundizacion.getIdareaprofundizacion().toString());
            }
            parametroSalaLaboratorio = new SalaLaboratorio();
        } else {
            if (Utilidades.validarNulo(parametroLaboratorio)) {
                filtrosSala.put("parametroLaboratorio", parametroLaboratorio.getIdlaboratorio().toString());
            }
            if (Utilidades.validarNulo(parametroEdificio)) {
                filtrosSala.put("parametroEdificio", parametroEdificio.getIdedificio().toString());
            }
            if (Utilidades.validarNulo(parametroSede)) {
                filtrosSala.put("parametroSede", parametroSede.getIdsede().toString());
            }
            if (Utilidades.validarNulo(parametroAreaProfundizacion)) {
                filtrosSala.put("parametroAreaProfundizacion", parametroAreaProfundizacion.getIdareaprofundizacion().toString());
            }
            nuevoSalaLaboratorioModulo = null;
        }
        listaSalasLaboratorios = gestionarPlantaModulosBO.consultarSalasLaboratorioPorParametroFiltrado(filtrosSala);
        if (null != listaSalasLaboratorios) {
            if (tipoRegistro == 0) {
                activarSala = false;
            } else {
                activarNuevoSala = false;
            }
        } else {
            if (tipoRegistro == 0) {
                activarSala = true;
            } else {
                activarNuevoSala = true;
            }
        }
        if (tipoRegistro == 0) {
            context.update("formT:form:parametroSalaLaboratorio");
        } else {
            context.update("formT:formularioDialogos:nuevoSalaModuloLaboratorio");
        }

    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Plata_Modulos_Laboratorio_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Plata_Modulos_Laboratorio_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void modificacionTodosModulos() {
        RequestContext.getCurrentInstance().update("formT:form:parametroEstado");
    }

    public String cambiarPaginaDetalles() {
        limpiarProcesoBusqueda();
        return "detallesplantamodulo";
    }

    // GET - SET
    public String getParametroCodigo() {
        return parametroCodigo;
    }

    public void setParametroCodigo(String parametroCodigo) {
        this.parametroCodigo = parametroCodigo;
    }

    public String getParametroDetalle() {
        return parametroDetalle;
    }

    public void setParametroDetalle(String parametroDetalle) {
        this.parametroDetalle = parametroDetalle;
    }

    public boolean isParametroEstado() {
        return parametroEstado;
    }

    public void setParametroEstado(boolean parametroEstado) {
        this.parametroEstado = parametroEstado;
    }

    public boolean isActivarTodosModulo() {
        return activarTodosModulo;
    }

    public void setActivarTodosModulo(boolean activarTodosModulo) {
        this.activarTodosModulo = activarTodosModulo;
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

    public List<SalaLaboratorio> getListaSalasLaboratorios() {
        return listaSalasLaboratorios;
    }

    public void setListaSalasLaboratorios(List<SalaLaboratorio> listaSalasLaboratorios) {
        this.listaSalasLaboratorios = listaSalasLaboratorios;
    }

    public SalaLaboratorio getParametroSalaLaboratorio() {
        return parametroSalaLaboratorio;
    }

    public void setParametroSalaLaboratorio(SalaLaboratorio parametroSalaLaboratorio) {
        this.parametroSalaLaboratorio = parametroSalaLaboratorio;
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

    public boolean isActivarSala() {
        return activarSala;
    }

    public void setActivarSala(boolean activarSala) {
        this.activarSala = activarSala;
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

    public boolean isActivarNuevoSala() {
        return activarNuevoSala;
    }

    public void setActivarNuevoSala(boolean activarNuevoSala) {
        this.activarNuevoSala = activarNuevoSala;
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

    public List<ModuloLaboratorio> getListaModulosLaboratorios() {
        return listaModulosLaboratorios;
    }

    public void setListaModulosLaboratorios(List<ModuloLaboratorio> listaModulosLaboratorios) {
        this.listaModulosLaboratorios = listaModulosLaboratorios;
    }

    public List<ModuloLaboratorio> getFiltrarListaModulosLaboratorios() {
        return filtrarListaModulosLaboratorios;
    }

    public void setFiltrarListaModulosLaboratorios(List<ModuloLaboratorio> filtrarListaModulosLaboratorios) {
        this.filtrarListaModulosLaboratorios = filtrarListaModulosLaboratorios;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getNuevoCodigoModulo() {
        return nuevoCodigoModulo;
    }

    public void setNuevoCodigoModulo(String nuevoCodigoModulo) {
        this.nuevoCodigoModulo = nuevoCodigoModulo;
    }

    public String getNuevoDetalleModulo() {
        return nuevoDetalleModulo;
    }

    public void setNuevoDetalleModulo(String nuevoDetalleModulo) {
        this.nuevoDetalleModulo = nuevoDetalleModulo;
    }

    public String getNuevoCapacidadModulo() {
        return nuevoCapacidadModulo;
    }

    public void setNuevoCapacidadModulo(String nuevoCapacidadModulo) {
        this.nuevoCapacidadModulo = nuevoCapacidadModulo;
    }

    public String getNuevoCostoModulo() {
        return nuevoCostoModulo;
    }

    public void setNuevoCostoModulo(String nuevoCostoModulo) {
        this.nuevoCostoModulo = nuevoCostoModulo;
    }

    public String getNuevoInversionModulo() {
        return nuevoInversionModulo;
    }

    public void setNuevoInversionModulo(String nuevoInversionModulo) {
        this.nuevoInversionModulo = nuevoInversionModulo;
    }

    public Laboratorio getNuevoLaboratorioModulo() {
        return nuevoLaboratorioModulo;
    }

    public void setNuevoLaboratorioModulo(Laboratorio nuevoLaboratorioModulo) {
        this.nuevoLaboratorioModulo = nuevoLaboratorioModulo;
    }

    public AreaProfundizacion getNuevoAreaProfundizacionModulo() {
        return nuevoAreaProfundizacionModulo;
    }

    public void setNuevoAreaProfundizacionModulo(AreaProfundizacion nuevoAreaProfundizacionModulo) {
        this.nuevoAreaProfundizacionModulo = nuevoAreaProfundizacionModulo;
    }

    public Sede getNuevoSedeModulo() {
        return nuevoSedeModulo;
    }

    public void setNuevoSedeModulo(Sede nuevoSedeModulo) {
        this.nuevoSedeModulo = nuevoSedeModulo;
    }

    public Edificio getNuevoEdificioModulo() {
        return nuevoEdificioModulo;
    }

    public void setNuevoEdificioModulo(Edificio nuevoEdificioModulo) {
        this.nuevoEdificioModulo = nuevoEdificioModulo;
    }

    public SalaLaboratorio getNuevoSalaLaboratorioModulo() {
        return nuevoSalaLaboratorioModulo;
    }

    public void setNuevoSalaLaboratorioModulo(SalaLaboratorio nuevoSalaLaboratorioModulo) {
        this.nuevoSalaLaboratorioModulo = nuevoSalaLaboratorioModulo;
    }

}

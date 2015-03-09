package com.sirelab.controller.planta;

import com.sirelab.bo.interfacebo.GestionarPlantaEquiposElementosBOInterface;
import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.EquipoElemento;
import com.sirelab.entidades.EstadoEquipo;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.ModuloLaboratorio;
import com.sirelab.entidades.Proveedor;
import com.sirelab.entidades.SalaLaboratorio;
import com.sirelab.entidades.TipoActivo;
import com.sirelab.exporter.ExportarPDF;
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
public class ControllerGestionarPlantaEquiposElementos implements Serializable {

    @EJB
    GestionarPlantaEquiposElementosBOInterface gestionarPlantaEquiposElementosBO;

    private String parametroNombre, parametroInventario, parametroMarca, parametroModelo, parametroSerie;
    private boolean activarTodosEquipos;
    private List<Laboratorio> listaLaboratorios;
    private Laboratorio parametroLaboratorio;
    private List<AreaProfundizacion> listaAreasProfundizacion;
    private AreaProfundizacion parametroAreaProfundizacion;
    private List<SalaLaboratorio> listaSalasLaboratorios;
    private SalaLaboratorio parametroSalaLaboratorio;
    private List<ModuloLaboratorio> listaModulosLaboratorios;
    private ModuloLaboratorio parametroModuloLaboratorio;
    private List<TipoActivo> listaTiposActivos;
    private TipoActivo parametroTipoActivo;
    private List<Proveedor> listaProveedores;
    private Proveedor parametroProveedor;
    private List<EstadoEquipo> listaEstadosEquipos;
    private EstadoEquipo parametroEstadoEquipo;
    private boolean activarAreaProfundizacion;
    private boolean activarSalaLaboratorio;
    private boolean activarModuloLaboratorio;
    //
    private Map<String, String> filtros;
    //
    private boolean activarExport;
    //
    private List<EquipoElemento> listaEquiposElementos;
    private List<EquipoElemento> filtrarListaEquiposElementos;
    //
    private Column nombreTabla, inventarioTabla, marcaTabla, modeloTabla, serieTabla, moduloTabla, salaTabla, areaTabla, laboratorioTabla, tipoTabla, proveedorTabla, estadoTabla;
    //
    private String altoTabla;

    public ControllerGestionarPlantaEquiposElementos() {
    }

    @PostConstruct
    public void init() {
        activarTodosEquipos = false;
        activarModuloLaboratorio = true;
        activarSalaLaboratorio = true;
        activarAreaProfundizacion = true;
        activarExport = true;
        parametroNombre = null;
        parametroInventario = null;
        parametroMarca = null;
        parametroModelo = null;
        parametroSerie = null;
        parametroAreaProfundizacion = new AreaProfundizacion();
        parametroLaboratorio = new Laboratorio();
        parametroSalaLaboratorio = new SalaLaboratorio();
        parametroModuloLaboratorio = new ModuloLaboratorio();
        parametroEstadoEquipo = new EstadoEquipo();
        parametroTipoActivo = new TipoActivo();
        parametroProveedor = new Proveedor();
        listaTiposActivos = gestionarPlantaEquiposElementosBO.consultarTiposActivosRegistrador();
        listaEstadosEquipos = gestionarPlantaEquiposElementosBO.consultarEstadosEquiposRegistrados();
        listaProveedores = gestionarPlantaEquiposElementosBO.consultarProveedoresRegistrados();
        listaLaboratorios = gestionarPlantaEquiposElementosBO.consultarLaboratoriosRegistrados();
        altoTabla = "150";
        inicializarFiltros();
        listaAreasProfundizacion = null;
        listaModulosLaboratorios = null;
        listaSalasLaboratorios = null;
        filtrarListaEquiposElementos = null;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroInventario", null);
        filtros.put("parametroMarca", null);
        filtros.put("parametroSerie", null);
        filtros.put("parametroModelo", null);
        filtros.put("parametroEstado", null);
        filtros.put("parametroModuloLaboratorio", null);
        filtros.put("parametroSalaLaboratorio", null);
        filtros.put("parametroAreaProfundizacion", null);
        filtros.put("parametroLaboratorio", null);
        filtros.put("parametroProveedor", null);
        filtros.put("parametroTipoActivo", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if (activarTodosEquipos == false) {
            if (parametroEstadoEquipo.getIdestadoequipo() != null) {
                filtros.put("parametroEstado", parametroEstadoEquipo.getIdestadoequipo().toString());
            }
        }
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroInventario) == true) && (!parametroInventario.isEmpty())) {
            filtros.put("parametroInventario", parametroInventario.toString());
        }
        if ((Utilidades.validarNulo(parametroMarca) == true) && (!parametroMarca.isEmpty())) {
            filtros.put("parametroMarca", parametroMarca.toString());
        }
        if ((Utilidades.validarNulo(parametroModelo) == true) && (!parametroModelo.isEmpty())) {
            filtros.put("parametroModelo", parametroModelo.toString());
        }
        if ((Utilidades.validarNulo(parametroSerie) == true) && (!parametroSerie.isEmpty())) {
            filtros.put("parametroSerie", parametroSerie.toString());
        }
        if (parametroLaboratorio.getIdlaboratorio() != null) {
            filtros.put("parametroLaboratorio", parametroLaboratorio.getIdlaboratorio().toString());
        }
        if (parametroAreaProfundizacion.getIdareaprofundizacion() != null) {
            filtros.put("parametroAreaProfundizacion", parametroAreaProfundizacion.getIdareaprofundizacion().toString());
        }
        if (parametroSalaLaboratorio.getIdsalalaboratorio() != null) {
            filtros.put("parametroSalaLaboratorio", parametroSalaLaboratorio.getIdsalalaboratorio().toString());
        }
        if (parametroModuloLaboratorio.getIdmodulolaboratorio() != null) {
            filtros.put("parametroModuloLaboratorio", parametroModuloLaboratorio.getIdmodulolaboratorio().toString());
        }
        if (parametroProveedor.getIdproveedor() != null) {
            filtros.put("parametroProveedor", parametroProveedor.getIdproveedor().toString());
        }
        if (parametroTipoActivo.getIdtipoactivo() != null) {
            filtros.put("parametroTipoActivo", parametroTipoActivo.getIdtipoactivo().toString());
        }
    }

    public void buscarEquiposElementosPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaEquiposElementos = null;
            listaEquiposElementos = gestionarPlantaEquiposElementosBO.consultarEquiposElementosPorParametro(filtros);
            if (listaEquiposElementos != null) {
                if (listaEquiposElementos.size() > 0) {
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
            System.out.println("Error ControllerGestionarPlantaEquiposElementos buscarEquiposElementosPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarAreaProfundizacion = true;
        activarSalaLaboratorio = true;
        activarModuloLaboratorio = true;
        activarTodosEquipos = false;
        if (null != listaEquiposElementos) {
            desactivarFiltrosTabla();
        }
        activarExport = true;
        parametroNombre = null;
        parametroInventario = null;
        parametroMarca = null;
        parametroModelo = null;
        parametroSerie = null;
        parametroAreaProfundizacion = new AreaProfundizacion();
        parametroSalaLaboratorio = new SalaLaboratorio();
        parametroLaboratorio = new Laboratorio();
        parametroModuloLaboratorio = new ModuloLaboratorio();
        parametroTipoActivo = new TipoActivo();
        parametroEstadoEquipo = new EstadoEquipo();
        parametroProveedor = new Proveedor();
        inicializarFiltros();
        listaAreasProfundizacion = null;
        listaSalasLaboratorios = null;
        listaModulosLaboratorios = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("width: 80px");
        inventarioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:inventarioTabla");
        inventarioTabla.setFilterStyle("width: 80px");
        marcaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:marcaTabla");
        marcaTabla.setFilterStyle("width: 80px");
        serieTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:serieTabla");
        serieTabla.setFilterStyle("width: 80px");
        modeloTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:modeloTabla");
        modeloTabla.setFilterStyle("width: 80px");
        salaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:salaTabla");
        salaTabla.setFilterStyle("width: 80px");
        moduloTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:moduloTabla");
        moduloTabla.setFilterStyle("width: 80px");
        areaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:areaTabla");
        areaTabla.setFilterStyle("width: 80px");
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("width: 80px");
        tipoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:tipoTabla");
        tipoTabla.setFilterStyle("width: 80px");
        proveedorTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:proveedorTabla");
        proveedorTabla.setFilterStyle("width: 80px");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("display: none; visibility: hidden;");
        inventarioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:inventarioTabla");
        inventarioTabla.setFilterStyle("display: none; visibility: hidden;");
        marcaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:marcaTabla");
        marcaTabla.setFilterStyle("display: none; visibility: hidden;");
        serieTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:serieTabla");
        serieTabla.setFilterStyle("display: none; visibility: hidden;");
        modeloTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:modeloTabla");
        modeloTabla.setFilterStyle("display: none; visibility: hidden;");
        salaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:salaTabla");
        salaTabla.setFilterStyle("display: none; visibility: hidden;");
        moduloTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:moduloTabla");
        moduloTabla.setFilterStyle("display: none; visibility: hidden;");
        areaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:areaTabla");
        areaTabla.setFilterStyle("display: none; visibility: hidden;");
        laboratorioTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:laboratorioTabla");
        laboratorioTabla.setFilterStyle("display: none; visibility: hidden;");
        tipoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:tipoTabla");
        tipoTabla.setFilterStyle("display: none; visibility: hidden;");
        proveedorTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:proveedorTabla");
        proveedorTabla.setFilterStyle("display: none; visibility: hidden;");
        estadoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:estadoTabla");
        estadoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaEquiposElementos = null;
    }

    public String dispararPaginaNuevoRegistro() {
        limpiarProcesoBusqueda();
        return "adicionarplantaequipo";
    }

    public void actualizarLaboratorios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroLaboratorio)) {
            parametroAreaProfundizacion = new AreaProfundizacion();
            listaAreasProfundizacion = gestionarPlantaEquiposElementosBO.consultarAreasProfundizacionPorIDLaboratorio(parametroLaboratorio.getIdlaboratorio());
            activarAreaProfundizacion = false;
            activarModuloLaboratorio = true;
            listaModulosLaboratorios = null;
            parametroModuloLaboratorio = new ModuloLaboratorio();
            activarSalaLaboratorio = true;
            listaSalasLaboratorios = null;
            parametroSalaLaboratorio = new SalaLaboratorio();
        } else {
            parametroAreaProfundizacion = new AreaProfundizacion();
            activarAreaProfundizacion = true;
            listaAreasProfundizacion = null;
            activarModuloLaboratorio = true;
            listaModulosLaboratorios = null;
            parametroModuloLaboratorio = new ModuloLaboratorio();
            activarSalaLaboratorio = true;
            listaSalasLaboratorios = null;
            parametroSalaLaboratorio = new SalaLaboratorio();
        }
        context.update("formT:form:parametroAreaProfundizacion");
        context.update("formT:form:parametroSalaLaboratorio");
        context.update("formT:form:parametroModuloLaboratorio");
    }

    public void actualizarAreasProfundizacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroAreaProfundizacion)) {
            parametroSalaLaboratorio = new SalaLaboratorio();
            listaSalasLaboratorios = gestionarPlantaEquiposElementosBO.consultarSalasLaboratorioPorIDAreaProfundizacion(parametroAreaProfundizacion.getIdareaprofundizacion());
            activarSalaLaboratorio = false;
            activarModuloLaboratorio = true;
            listaModulosLaboratorios = null;
            parametroModuloLaboratorio = new ModuloLaboratorio();
        } else {
            activarModuloLaboratorio = true;
            listaModulosLaboratorios = null;
            parametroModuloLaboratorio = new ModuloLaboratorio();
            activarSalaLaboratorio = true;
            listaSalasLaboratorios = null;
            parametroSalaLaboratorio = new SalaLaboratorio();
        }
        context.update("formT:form:parametroSalaLaboratorio");
        context.update("formT:form:parametroModuloLaboratorio");
    }

    public void actualizarSalasLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(parametroSalaLaboratorio)) {
            parametroModuloLaboratorio = new ModuloLaboratorio();
            listaModulosLaboratorios = gestionarPlantaEquiposElementosBO.consultarModulosLaboratorioPorIDSalaLaboratorio(parametroSalaLaboratorio.getIdsalalaboratorio());
            activarModuloLaboratorio = false;
        } else {
            activarModuloLaboratorio = true;
            listaModulosLaboratorios = null;
            parametroModuloLaboratorio = new ModuloLaboratorio();
        }
        context.update("formT:form:parametroModuloLaboratorio");
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Plata_Equipos_de_Modulo_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Plata_Equipos_de_Modulo_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public String cambiarPaginaDetalles() {
        limpiarProcesoBusqueda();
        return "detallesplantaequipo";
    }

    public void modificacionTodosEquipos() {
        RequestContext.getCurrentInstance().update("formT:form:parametroEstado");
    }

    // GET - SET
    public String getParametroNombre() {
        return parametroNombre;
    }

    public void setParametroNombre(String parametroNombre) {
        this.parametroNombre = parametroNombre;
    }

    public String getParametroInventario() {
        return parametroInventario;
    }

    public void setParametroInventario(String parametroInventario) {
        this.parametroInventario = parametroInventario;
    }

    public String getParametroMarca() {
        return parametroMarca;
    }

    public void setParametroMarca(String parametroMarca) {
        this.parametroMarca = parametroMarca;
    }

    public String getParametroModelo() {
        return parametroModelo;
    }

    public void setParametroModelo(String parametroModelo) {
        this.parametroModelo = parametroModelo;
    }

    public boolean isActivarTodosEquipos() {
        return activarTodosEquipos;
    }

    public void setActivarTodosEquipos(boolean activarTodosEquipos) {
        this.activarTodosEquipos = activarTodosEquipos;
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

    public List<ModuloLaboratorio> getListaModulosLaboratorios() {
        return listaModulosLaboratorios;
    }

    public void setListaModulosLaboratorios(List<ModuloLaboratorio> listaModulosLaboratorios) {
        this.listaModulosLaboratorios = listaModulosLaboratorios;
    }

    public ModuloLaboratorio getParametroModuloLaboratorio() {
        return parametroModuloLaboratorio;
    }

    public void setParametroModuloLaboratorio(ModuloLaboratorio parametroModuloLaboratorio) {
        this.parametroModuloLaboratorio = parametroModuloLaboratorio;
    }

    public List<TipoActivo> getListaTiposActivos() {
        return listaTiposActivos;
    }

    public void setListaTiposActivos(List<TipoActivo> listaTiposActivos) {
        this.listaTiposActivos = listaTiposActivos;
    }

    public List<Proveedor> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedor> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public Proveedor getParametroProveedor() {
        return parametroProveedor;
    }

    public void setParametroProveedor(Proveedor parametroProveedor) {
        this.parametroProveedor = parametroProveedor;
    }

    public List<EstadoEquipo> getListaEstadosEquipos() {
        return listaEstadosEquipos;
    }

    public void setListaEstadosEquipos(List<EstadoEquipo> listaEstadosEquipos) {
        this.listaEstadosEquipos = listaEstadosEquipos;
    }

    public EstadoEquipo getParametroEstadoEquipo() {
        return parametroEstadoEquipo;
    }

    public void setParametroEstadoEquipo(EstadoEquipo parametroEstadoEquipo) {
        this.parametroEstadoEquipo = parametroEstadoEquipo;
    }

    public boolean isActivarAreaProfundizacion() {
        return activarAreaProfundizacion;
    }

    public void setActivarAreaProfundizacion(boolean activarAreaProfundizacion) {
        this.activarAreaProfundizacion = activarAreaProfundizacion;
    }

    public boolean isActivarSalaLaboratorio() {
        return activarSalaLaboratorio;
    }

    public void setActivarSalaLaboratorio(boolean activarSalaLaboratorio) {
        this.activarSalaLaboratorio = activarSalaLaboratorio;
    }

    public boolean isActivarModuloLaboratorio() {
        return activarModuloLaboratorio;
    }

    public void setActivarModuloLaboratorio(boolean activarModuloLaboratorio) {
        this.activarModuloLaboratorio = activarModuloLaboratorio;
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

    public List<EquipoElemento> getListaEquiposElementos() {
        return listaEquiposElementos;
    }

    public void setListaEquiposElementos(List<EquipoElemento> listaEquiposElementos) {
        this.listaEquiposElementos = listaEquiposElementos;
    }

    public List<EquipoElemento> getFiltrarListaEquiposElementos() {
        return filtrarListaEquiposElementos;
    }

    public void setFiltrarListaEquiposElementos(List<EquipoElemento> filtrarListaEquiposElementos) {
        this.filtrarListaEquiposElementos = filtrarListaEquiposElementos;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public TipoActivo getParametroTipoActivo() {
        return parametroTipoActivo;
    }

    public void setParametroTipoActivo(TipoActivo parametroTipoActivo) {
        this.parametroTipoActivo = parametroTipoActivo;
    }

    public String getParametroSerie() {
        return parametroSerie;
    }

    public void setParametroSerie(String parametroSerie) {
        this.parametroSerie = parametroSerie;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.controller.recursoslab;

import com.sirelab.bo.interfacebo.GestionarRecursoInsumosBOInterface;
import com.sirelab.entidades.Insumo;
import com.sirelab.entidades.Proveedor;
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
public class ControllerGestionarRecursoInsumo implements Serializable {

    @EJB
    GestionarRecursoInsumosBOInterface gestionarRecursoInsumosBO;

    private String parametroNombre, parametroCodigo, parametroMarca, parametroModelo;
    private Map<String, String> filtros;
    private List<Proveedor> listaProveedores;
    private Proveedor parametroProveedor;
    //
    private List<Insumo> listaInsumos;
    private List<Insumo> filtrarListaInsumos;
    //
    private Column nombreTabla, codigoTabla, marcaTabla, modeloTabla, proveedorTabla;
    //
    private String altoTabla;
    //
    private boolean activarExport;
    //
    private String nuevoNombreInsumo, nuevoCodigoInsumo, nuevoMarcaInsumo, nuevoModeloInsumo;
    private String nuevoCantidadMinInsumo, nuevoCantidadExistenciaInsumo;
    private String nuevoDescripcionInsumo;
    private Proveedor nuevoProveedorInsumo;
    //
    private String editarNombre, editarCodigo, editarMarca, editarModelo;
    private String editarCantidadMin, editarCantidadExistencia;
    private String editarDescripcion;
    private Proveedor editarProveedor;
    private Insumo insumoEditar;

    public ControllerGestionarRecursoInsumo() {
    }

    @PostConstruct
    public void init() {
        parametroNombre = null;
        parametroCodigo = null;
        parametroModelo = null;
        parametroMarca = null;
        parametroProveedor = null;
        altoTabla = "150";
        inicializarFiltros();
        listaInsumos = null;
        filtrarListaInsumos = null;
        activarExport = true;
        listaProveedores = gestionarRecursoInsumosBO.consultarProveedoresRegistrados();
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroMarca", null);
        filtros.put("parametroModelo", null);
        filtros.put("parametroCodigo", null);
        filtros.put("parametroProveedor", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroCodigo) == true) && (!parametroCodigo.isEmpty())) {
            filtros.put("parametroCodigo", parametroCodigo.toString());
        }
        if ((Utilidades.validarNulo(parametroMarca) == true) && (!parametroMarca.isEmpty())) {
            filtros.put("parametroMarca", parametroMarca.toString());
        }
        if ((Utilidades.validarNulo(parametroModelo) == true) && (!parametroModelo.isEmpty())) {
            filtros.put("parametroModelo", parametroModelo.toString());
        }
        if ((Utilidades.validarNulo(parametroProveedor) == true)) {
            filtros.put("parametroProveedor", parametroProveedor.getIdproveedor().toString());
        }
    }

    public void buscarInsumosPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaInsumos = null;
            listaInsumos = gestionarRecursoInsumosBO.consultarInsumosPorParametro(filtros);
            if (listaInsumos != null) {
                if (listaInsumos.size() > 0) {
                    activarFiltrosTabla();
                    activarExport = false;
                } else {
                    activarExport = true;
                    context.execute("consultaSinDatos.show();");
                }
            } else {
                activarExport = true;
                context.execute("consultaSinDatos.show();");
            }
            context.update("form:datosBusqueda");
            context.update("form:exportarXLS");
            context.update("form:exportarXML");
            context.update("form:exportarPDF");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarInsumos buscarInsumosPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarExport = true;
        if (null != listaInsumos) {
            desactivarFiltrosTabla();
        }
        parametroNombre = null;
        parametroCodigo = null;
        parametroModelo = null;
        parametroMarca = null;
        parametroProveedor = null;
        inicializarFiltros();
        listaInsumos = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("width: 80px");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("width: 80px");
        marcaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:marcaTabla");
        marcaTabla.setFilterStyle("width: 80px");
        modeloTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:modeloTabla");
        modeloTabla.setFilterStyle("width: 80px");
        proveedorTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:proveedorTabla");
        proveedorTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("display: none; visibility: hidden;");
        codigoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:codigoTabla");
        codigoTabla.setFilterStyle("display: none; visibility: hidden;");
        marcaTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:marcaTabla");
        marcaTabla.setFilterStyle("display: none; visibility: hidden;");
        modeloTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:modeloTabla");
        modeloTabla.setFilterStyle("display: none; visibility: hidden;");
        proveedorTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:proveedorTabla");
        proveedorTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaInsumos = null;
    }

    public void dispararDialogoNuevoInsumo() {
        limpiarRegistroInsumo();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroInsumo");
        context.execute("NuevoRegistroInsumo.show()");
    }

    public void dispararDialogoEditarInsumo(BigInteger idInsumo) {
        editarCodigo = null;
        editarNombre = null;
        editarModelo = null;
        editarCantidadExistencia = null;
        editarCantidadMin = null;
        editarMarca = null;
        editarProveedor = new Proveedor();
        editarDescripcion = null;
        cargarInformacionUsuarioEditar(idInsumo);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroInsumo");
        context.execute("EditarRegistroInsumo.show()");
    }

    public void cargarInformacionUsuarioEditar(BigInteger idInsumo) {
        try {
            insumoEditar = gestionarRecursoInsumosBO.obtenerInsumoPorIDInsumo(idInsumo);
            if (insumoEditar != null) {
                editarNombre = insumoEditar.getNombreinsumo();
                editarCodigo = insumoEditar.getCodigoinsumo();
                editarModelo = insumoEditar.getModeloinsumo();
                editarCantidadExistencia = insumoEditar.getCantidadexistencia().toString();
                editarCantidadMin = insumoEditar.getCantidadminimia().toString();
                editarMarca = insumoEditar.getMarcainsumo();
                editarDescripcion = insumoEditar.getDescripcioninsumo();
                editarProveedor = insumoEditar.getProveedor();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarInsumos cargarInformacionUsuarioEditar : " + e.toString());
        }
    }

    public void limpiarRegistroInsumo() {
        nuevoCodigoInsumo = null;
        nuevoNombreInsumo = null;
        nuevoModeloInsumo = null;
        nuevoCantidadMinInsumo = null;
        nuevoMarcaInsumo = null;
        nuevoCantidadExistenciaInsumo = null;
        nuevoDescripcionInsumo = null;
        nuevoProveedorInsumo = new Proveedor();
    }

    public boolean validarInformacionObligatoriaInsumo(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoNombreInsumo)) && (Utilidades.validarNulo(nuevoCodigoInsumo))) {
                if (!Utilidades.validarCaracterString(nuevoNombreInsumo)) {
                    retorno = false;
                }
                if (!Utilidades.isNumber(nuevoCodigoInsumo)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
            if (nuevoProveedorInsumo.getIdproveedor() == null) {
                retorno = false;
            }
        } else {
            if ((Utilidades.validarNulo(editarNombre)) && (Utilidades.validarNulo(editarCodigo))) {
                if (!Utilidades.validarCaracterString(editarNombre)) {
                    retorno = false;
                }
                if (!Utilidades.isNumber(editarCodigo)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
            if (editarProveedor.getIdproveedor() == null) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarInformacionOpcionalInsumo(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoCantidadMinInsumo)) && (Utilidades.validarNulo(nuevoCantidadExistenciaInsumo))
                    && (Utilidades.validarNulo(nuevoMarcaInsumo)) && (Utilidades.validarNulo(nuevoModeloInsumo))
                    && (Utilidades.validarNulo(nuevoDescripcionInsumo))) {
                if (!Utilidades.isNumber(nuevoCantidadMinInsumo)) {
                    retorno = false;
                }
                if (!Utilidades.isNumber(nuevoCantidadExistenciaInsumo)) {
                    retorno = false;
                }
            }
        } else {
            if ((Utilidades.validarNulo(editarCantidadExistencia)) && (Utilidades.validarNulo(editarCantidadMin))
                    && (Utilidades.validarNulo(editarMarca)) && (Utilidades.validarNulo(editarModelo))
                    && (Utilidades.validarNulo(editarDescripcion))) {
                if (!Utilidades.isNumber(editarCantidadExistencia)) {
                    retorno = false;
                }
                if (!Utilidades.isNumber(editarCantidadMin)) {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void registrarNuevoInsumo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarInformacionObligatoriaInsumo(0) == true) {
            if (validarInformacionOpcionalInsumo(0) == true) {
                almacenarNuevoInsumoEnSistema();
            } else {
                context.execute("errorInformacionOpcionalInsumo.show()");
            }
        } else {
            context.execute("errorInformacionObligatoriaInsumo.show()");
        }
    }

    public void almacenarNuevoInsumoEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroInsumo.hide()");
        try {
            Insumo nuevaInsumo = new Insumo();
            nuevaInsumo.setCodigoinsumo(nuevoCodigoInsumo);
            nuevaInsumo.setNombreinsumo(nuevoNombreInsumo);
            nuevaInsumo.setModeloinsumo(nuevoModeloInsumo);
            nuevaInsumo.setMarcainsumo(nuevoMarcaInsumo);
            nuevaInsumo.setCantidadexistencia(Integer.valueOf(nuevoCantidadMinInsumo));
            nuevaInsumo.setCantidadminimia(Integer.valueOf(nuevoCantidadMinInsumo));
            nuevaInsumo.setDescripcioninsumo(nuevoDescripcionInsumo);
            nuevaInsumo.setProveedor(nuevoProveedorInsumo);
            gestionarRecursoInsumosBO.crearNuevoInsumo(nuevaInsumo);
            context.execute("registroExitosoInsumo.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarInsumos almacenarNuevoInsumoEnSistema : " + e.toString());
            context.execute("registroFallidoInsumo.show()");
        }
    }

    public void modificarInformacionInsumo() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarInformacionObligatoriaInsumo(1) == true) {
            if (validarInformacionOpcionalInsumo(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorInformacionOpcionalInsumo.show()");
            }
        } else {
            context.execute("errorInformacionObligatoriaInsumo.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroInsumo.hide()");
        try {
            insumoEditar.setCodigoinsumo(editarCodigo);
            insumoEditar.setNombreinsumo(editarNombre);
            insumoEditar.setModeloinsumo(editarModelo);
            insumoEditar.setMarcainsumo(editarMarca);
            insumoEditar.setDescripcioninsumo(editarDescripcion);
            insumoEditar.setCantidadexistencia(Integer.valueOf(editarCantidadExistencia));
            insumoEditar.setCantidadminimia(Integer.valueOf(editarCantidadMin));
            insumoEditar.setProveedor(editarProveedor);
            gestionarRecursoInsumosBO.modificarInformacionInsumo(insumoEditar);
            limpiarEditarInsumo();
            context.execute("registroExitosoInsumo.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarInsumos almacenarModificacion : " + e.toString());
            context.execute("registroFallidoInsumo.show()");
        }
    }

    public void limpiarEditarInsumo() {
        insumoEditar = null;
        editarCodigo = null;
        editarNombre = null;
        editarModelo = null;
        editarCantidadExistencia = null;
        editarMarca = null;
        editarCantidadMin = null;
        editarDescripcion = null;
        editarProveedor = new Proveedor();
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Recursos_Insumo_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Recursos_Insumo_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
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

    public Map<String, String> getFiltros() {
        return filtros;
    }

    public void setFiltros(Map<String, String> filtros) {
        this.filtros = filtros;
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

    public List<Insumo> getListaInsumos() {
        return listaInsumos;
    }

    public void setListaInsumos(List<Insumo> listaInsumos) {
        this.listaInsumos = listaInsumos;
    }

    public List<Insumo> getFiltrarListaInsumos() {
        return filtrarListaInsumos;
    }

    public void setFiltrarListaInsumos(List<Insumo> filtrarListaInsumos) {
        this.filtrarListaInsumos = filtrarListaInsumos;
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

    public String getNuevoNombreInsumo() {
        return nuevoNombreInsumo;
    }

    public void setNuevoNombreInsumo(String nuevoNombreInsumo) {
        this.nuevoNombreInsumo = nuevoNombreInsumo;
    }

    public String getNuevoCodigoInsumo() {
        return nuevoCodigoInsumo;
    }

    public void setNuevoCodigoInsumo(String nuevoCodigoInsumo) {
        this.nuevoCodigoInsumo = nuevoCodigoInsumo;
    }

    public String getNuevoMarcaInsumo() {
        return nuevoMarcaInsumo;
    }

    public void setNuevoMarcaInsumo(String nuevoMarcaInsumo) {
        this.nuevoMarcaInsumo = nuevoMarcaInsumo;
    }

    public String getNuevoModeloInsumo() {
        return nuevoModeloInsumo;
    }

    public void setNuevoModeloInsumo(String nuevoModeloInsumo) {
        this.nuevoModeloInsumo = nuevoModeloInsumo;
    }

    public String getNuevoCantidadMinInsumo() {
        return nuevoCantidadMinInsumo;
    }

    public void setNuevoCantidadMinInsumo(String nuevoCantidadMinInsumo) {
        this.nuevoCantidadMinInsumo = nuevoCantidadMinInsumo;
    }

    public String getNuevoCantidadExistenciaInsumo() {
        return nuevoCantidadExistenciaInsumo;
    }

    public void setNuevoCantidadExistenciaInsumo(String nuevoCantidadExistenciaInsumo) {
        this.nuevoCantidadExistenciaInsumo = nuevoCantidadExistenciaInsumo;
    }

    public String getNuevoDescripcionInsumo() {
        return nuevoDescripcionInsumo;
    }

    public void setNuevoDescripcionInsumo(String nuevoDescripcionInsumo) {
        this.nuevoDescripcionInsumo = nuevoDescripcionInsumo;
    }

    public Proveedor getNuevoProveedorInsumo() {
        return nuevoProveedorInsumo;
    }

    public void setNuevoProveedorInsumo(Proveedor nuevoProveedorInsumo) {
        this.nuevoProveedorInsumo = nuevoProveedorInsumo;
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

    public String getEditarMarca() {
        return editarMarca;
    }

    public void setEditarMarca(String editarMarca) {
        this.editarMarca = editarMarca;
    }

    public String getEditarModelo() {
        return editarModelo;
    }

    public void setEditarModelo(String editarModelo) {
        this.editarModelo = editarModelo;
    }

    public String getEditarCantidadMin() {
        return editarCantidadMin;
    }

    public void setEditarCantidadMin(String editarCantidadMin) {
        this.editarCantidadMin = editarCantidadMin;
    }

    public String getEditarCantidadExistencia() {
        return editarCantidadExistencia;
    }

    public void setEditarCantidadExistencia(String editarCantidadExistencia) {
        this.editarCantidadExistencia = editarCantidadExistencia;
    }

    public String getEditarDescripcion() {
        return editarDescripcion;
    }

    public void setEditarDescripcion(String editarDescripcion) {
        this.editarDescripcion = editarDescripcion;
    }

    public Proveedor getEditarProveedor() {
        return editarProveedor;
    }

    public void setEditarProveedor(Proveedor editarProveedor) {
        this.editarProveedor = editarProveedor;
    }

    public Insumo getInsumoEditar() {
        return insumoEditar;
    }

    public void setInsumoEditar(Insumo insumoEditar) {
        this.insumoEditar = insumoEditar;
    }

}

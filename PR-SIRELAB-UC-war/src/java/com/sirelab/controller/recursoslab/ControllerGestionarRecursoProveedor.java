package com.sirelab.controller.recursoslab;

import com.sirelab.bo.interfacebo.GestionarRecursoProveedoresBOInterface;
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
public class ControllerGestionarRecursoProveedor implements Serializable {

    @EJB
    GestionarRecursoProveedoresBOInterface gestionarRecursoProveedoresBO;

    private String parametroNombre, parametroNIT, parametroTelefono, parametroDireccion;
    private Map<String, String> filtros;
    //
    private List<Proveedor> listaProveedores;
    private List<Proveedor> filtrarListaProveedores;
    //
    private Column nombreTabla, nitTabla, direccionTabla, telefonoTabla;
    //
    private String altoTabla;
    //
    private boolean activarExport;
    //
    private String nuevoNombreProveedor, nuevoNITProveedor, nuevoTelefonoProveedor, nuevoDireccionProveedor;
    private String nuevoVendedorProveedor, nuevoTelVendedorProveedor;
    //
    private String editarNombre, editarNIT, editarTelefono, editarDireccion;
    private String editarVendedor, editarTelVendedor;
    private Proveedor proveedorEditar;

    public ControllerGestionarRecursoProveedor() {
    }

    @PostConstruct
    public void init() {
        parametroNombre = null;
        parametroNIT = null;
        parametroDireccion = null;
        parametroTelefono = null;
        altoTabla = "150";
        inicializarFiltros();
        listaProveedores = null;
        filtrarListaProveedores = null;
        activarExport = true;
    }

    private void inicializarFiltros() {
        filtros = new HashMap<String, String>();
        filtros.put("parametroNombre", null);
        filtros.put("parametroTelefono", null);
        filtros.put("parametroDireccion", null);
        filtros.put("parametroNIT", null);
        agregarFiltrosAdicionales();
    }

    private void agregarFiltrosAdicionales() {
        if ((Utilidades.validarNulo(parametroNombre) == true) && (!parametroNombre.isEmpty())) {
            filtros.put("parametroNombre", parametroNombre.toString());
        }
        if ((Utilidades.validarNulo(parametroNIT) == true) && (!parametroNIT.isEmpty())) {
            filtros.put("parametroNIT", parametroNIT.toString());
        }
        if ((Utilidades.validarNulo(parametroTelefono) == true) && (!parametroTelefono.isEmpty())) {
            filtros.put("parametroTelefono", parametroTelefono.toString());
        }
        if ((Utilidades.validarNulo(parametroDireccion) == true) && (!parametroDireccion.isEmpty())) {
            filtros.put("parametroDireccion", parametroDireccion.toString());
        }
    }

    public void buscarProveedoresPorParametros() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            inicializarFiltros();
            listaProveedores = null;
            listaProveedores = gestionarRecursoProveedoresBO.consultarProveedoresPorParametro(filtros);
            if (listaProveedores != null) {
                if (listaProveedores.size() > 0) {
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
            System.out.println("Error ControllerGestionarProveedores buscarProveedoresPorParametros : " + e.toString());
        }
    }

    public void limpiarProcesoBusqueda() {
        activarExport = true;
        if (null != listaProveedores) {
            desactivarFiltrosTabla();
        }
        parametroNombre = null;
        parametroNIT = null;
        parametroDireccion = null;
        parametroTelefono = null;
        inicializarFiltros();
        listaProveedores = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void activarFiltrosTabla() {
        altoTabla = "128";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("width: 80px");
        nitTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nitTabla");
        nitTabla.setFilterStyle("width: 80px");
        direccionTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:direccionTabla");
        direccionTabla.setFilterStyle("width: 80px");
        telefonoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:telefonoTabla");
        telefonoTabla.setFilterStyle("width: 80px");
    }

    public void desactivarFiltrosTabla() {
        altoTabla = "150";
        FacesContext c = FacesContext.getCurrentInstance();
        nombreTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nombreTabla");
        nombreTabla.setFilterStyle("display: none; visibility: hidden;");
        nitTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:nitTabla");
        nitTabla.setFilterStyle("display: none; visibility: hidden;");
        direccionTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:direccionTabla");
        direccionTabla.setFilterStyle("display: none; visibility: hidden;");
        telefonoTabla = (Column) c.getViewRoot().findComponent("formT:form:datosBusqueda:telefonoTabla");
        telefonoTabla.setFilterStyle("display: none; visibility: hidden;");
        filtrarListaProveedores = null;
    }

    public void dispararDialogoNuevoProveedor() {
        limpiarRegistroProveedor();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:NuevoRegistroProveedor");
        context.execute("NuevoRegistroProveedor.show()");
    }

    public void dispararDialogoEditarProveedor(BigInteger idProveedor) {
        editarNIT = null;
        editarNombre = null;
        editarDireccion = null;
        editarTelVendedor = null;
        editarVendedor = null;
        editarTelefono = null;
        cargarInformacionUsuarioEditar(idProveedor);
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formT:formularioDialogos:EditarRegistroProveedor");
        context.execute("EditarRegistroProveedor.show()");
    }

    public void cargarInformacionUsuarioEditar(BigInteger idProveedor) {
        try {
            proveedorEditar = gestionarRecursoProveedoresBO.obtenerProveedorPorIDProveedor(idProveedor);
            if (proveedorEditar != null) {
                editarNombre = proveedorEditar.getNombreproveedor();
                editarDireccion = proveedorEditar.getDireccionproveedor();
                editarTelVendedor = proveedorEditar.getTelefonovendedor();
                editarVendedor = proveedorEditar.getVendedorproveedor();
                editarTelefono = proveedorEditar.getTelefonoproveedor();
                editarNIT = proveedorEditar.getNitproveedor();
            }
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarProveedores cargarInformacionUsuarioEditar : " + e.toString());
        }
    }

    public void limpiarRegistroProveedor() {
        nuevoNITProveedor = null;
        nuevoNombreProveedor = null;
        nuevoDireccionProveedor = null;
        nuevoVendedorProveedor = null;
        nuevoTelefonoProveedor = null;
        nuevoTelVendedorProveedor = null;
    }

    public boolean validarInformacionObligatoriaProveedor(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if ((Utilidades.validarNulo(nuevoNombreProveedor)) && (Utilidades.validarNulo(nuevoNITProveedor))
                    && (Utilidades.validarNulo(nuevoDireccionProveedor)) && (Utilidades.validarNulo(nuevoTelefonoProveedor))) {
                if (!Utilidades.validarCaracterString(nuevoNombreProveedor)) {
                    retorno = false;
                }
                if (!Utilidades.isNumber(nuevoTelefonoProveedor)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        } else {
            if ((Utilidades.validarNulo(editarNombre)) && (Utilidades.validarNulo(editarNIT))
                    && (Utilidades.validarNulo(editarDireccion)) && (Utilidades.validarNulo(editarTelefono))) {
                if (!Utilidades.validarCaracterString(editarNombre)) {
                    retorno = false;
                }
                if (!Utilidades.isNumber(editarTelefono)) {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarInformacionOpcionalProveedor(int tipoRegistro) {
        boolean retorno = true;
        if (tipoRegistro == 0) {
            if (Utilidades.validarNulo(nuevoVendedorProveedor)) {
                if (!Utilidades.validarCaracterString(nuevoVendedorProveedor)) {
                    retorno = false;
                }
            }
            if ((Utilidades.validarNulo(nuevoTelVendedorProveedor))) {
                if (!Utilidades.isNumber(nuevoTelVendedorProveedor)) {
                    retorno = false;
                }
            }
        } else {
            if (Utilidades.validarNulo(editarTelVendedor)) {
                if (!Utilidades.validarCaracterString(editarTelVendedor)) {
                    retorno = false;
                }
            }
            if ((Utilidades.validarNulo(editarTelVendedor))) {
                if (!Utilidades.isNumber(editarTelVendedor)) {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void registrarNuevoProveedor() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarInformacionObligatoriaProveedor(0) == true) {
            if (validarInformacionOpcionalProveedor(0) == true) {
                almacenarNuevoProveedorEnSistema();
            } else {
                context.execute("errorInformacionOpcionalProveedor.show()");
            }
        } else {
            context.execute("errorInformacionObligatoriaProveedor.show()");
        }
    }

    public void almacenarNuevoProveedorEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("NuevoRegistroProveedor.hide()");
        try {
            Proveedor nuevaProveedor = new Proveedor();
            nuevaProveedor.setNitproveedor(nuevoNITProveedor);
            nuevaProveedor.setNombreproveedor(nuevoNombreProveedor);
            nuevaProveedor.setDireccionproveedor(nuevoDireccionProveedor);
            nuevaProveedor.setTelefonoproveedor(nuevoTelefonoProveedor);
            if (Utilidades.validarNulo(editarTelVendedor)) {
                nuevaProveedor.setVendedorproveedor(nuevoVendedorProveedor);
            } else {
                nuevaProveedor.setVendedorproveedor("");
            }
            if (Utilidades.validarNulo(editarTelVendedor)) {
                nuevaProveedor.setTelefonovendedor(nuevoTelVendedorProveedor);
            } else {
                nuevaProveedor.setTelefonovendedor("");
            }
            gestionarRecursoProveedoresBO.crearNuevoProveedor(nuevaProveedor);
            context.execute("registroExitosoProveedor.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarProveedores almacenarNuevoProveedorEnSistema : " + e.toString());
            context.execute("registroFallidoProveedor.show()");
        }
    }

    public void modificarInformacionProveedor() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarInformacionObligatoriaProveedor(1) == true) {
            if (validarInformacionOpcionalProveedor(1) == true) {
                almacenarModificacion();
            } else {
                context.execute("errorInformacionOpcionalProveedor.show()");
            }
        } else {
            context.execute("errorInformacionObligatoriaProveedor.show()");
        }
    }

    public void almacenarModificacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("EditarRegistroProveedor.hide()");
        try {
            proveedorEditar.setNitproveedor(editarNIT);
            proveedorEditar.setNombreproveedor(editarNombre);
            proveedorEditar.setDireccionproveedor(editarDireccion);
            proveedorEditar.setTelefonoproveedor(editarTelefono);
            proveedorEditar.setVendedorproveedor(editarVendedor);
            proveedorEditar.setTelefonovendedor(editarTelVendedor);
            gestionarRecursoProveedoresBO.modificarInformacionProveedor(proveedorEditar);
            limpiarEditarProveedor();
            context.execute("registroExitosoProveedor.show()");
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarProveedores almacenarModificacion : " + e.toString());
            context.execute("registroFallidoProveedor.show()");
        }
    }

    public void limpiarEditarProveedor() {
        proveedorEditar = null;
        editarNIT = null;
        editarNombre = null;
        editarDireccion = null;
        editarTelVendedor = null;
        editarTelefono = null;
        editarVendedor = null;
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Recursos_Proveedor_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formT:form:datosBusqueda");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Recursos_Proveedor_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    // GET - SET
    public String getParametroNombre() {
        return parametroNombre;
    }

    public void setParametroNombre(String parametroNombre) {
        this.parametroNombre = parametroNombre;
    }

    public String getParametroNIT() {
        return parametroNIT;
    }

    public void setParametroNIT(String parametroNIT) {
        this.parametroNIT = parametroNIT;
    }

    public String getParametroTelefono() {
        return parametroTelefono;
    }

    public void setParametroTelefono(String parametroTelefono) {
        this.parametroTelefono = parametroTelefono;
    }

    public String getParametroDireccion() {
        return parametroDireccion;
    }

    public void setParametroDireccion(String parametroDireccion) {
        this.parametroDireccion = parametroDireccion;
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

    public List<Proveedor> getFiltrarListaProveedores() {
        return filtrarListaProveedores;
    }

    public void setFiltrarListaProveedores(List<Proveedor> filtrarListaProveedores) {
        this.filtrarListaProveedores = filtrarListaProveedores;
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

    public String getNuevoNombreProveedor() {
        return nuevoNombreProveedor;
    }

    public void setNuevoNombreProveedor(String nuevoNombreProveedor) {
        this.nuevoNombreProveedor = nuevoNombreProveedor;
    }

    public String getNuevoNITProveedor() {
        return nuevoNITProveedor;
    }

    public void setNuevoNITProveedor(String nuevoNITProveedor) {
        this.nuevoNITProveedor = nuevoNITProveedor;
    }

    public String getNuevoTelefonoProveedor() {
        return nuevoTelefonoProveedor;
    }

    public void setNuevoTelefonoProveedor(String nuevoTelefonoProveedor) {
        this.nuevoTelefonoProveedor = nuevoTelefonoProveedor;
    }

    public String getNuevoDireccionProveedor() {
        return nuevoDireccionProveedor;
    }

    public void setNuevoDireccionProveedor(String nuevoDireccionProveedor) {
        this.nuevoDireccionProveedor = nuevoDireccionProveedor;
    }

    public String getNuevoVendedorProveedor() {
        return nuevoVendedorProveedor;
    }

    public void setNuevoVendedorProveedor(String nuevoVendedorProveedor) {
        this.nuevoVendedorProveedor = nuevoVendedorProveedor;
    }

    public String getNuevoTelVendedorProveedor() {
        return nuevoTelVendedorProveedor;
    }

    public void setNuevoTelVendedorProveedor(String nuevoTelVendedorProveedor) {
        this.nuevoTelVendedorProveedor = nuevoTelVendedorProveedor;
    }

    public String getEditarNombre() {
        return editarNombre;
    }

    public void setEditarNombre(String editarNombre) {
        this.editarNombre = editarNombre;
    }

    public String getEditarNIT() {
        return editarNIT;
    }

    public void setEditarNIT(String editarNIT) {
        this.editarNIT = editarNIT;
    }

    public String getEditarTelefono() {
        return editarTelefono;
    }

    public void setEditarTelefono(String editarTelefono) {
        this.editarTelefono = editarTelefono;
    }

    public String getEditarDireccion() {
        return editarDireccion;
    }

    public void setEditarDireccion(String editarDireccion) {
        this.editarDireccion = editarDireccion;
    }

    public String getEditarVendedor() {
        return editarVendedor;
    }

    public void setEditarVendedor(String editarVendedor) {
        this.editarVendedor = editarVendedor;
    }

    public String getEditarTelVendedor() {
        return editarTelVendedor;
    }

    public void setEditarTelVendedor(String editarTelVendedor) {
        this.editarTelVendedor = editarTelVendedor;
    }

    public Proveedor getProveedorEditar() {
        return proveedorEditar;
    }

    public void setProveedorEditar(Proveedor proveedorEditar) {
        this.proveedorEditar = proveedorEditar;
    }

}

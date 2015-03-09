/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.sirelab.utilidades.Utilidades;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ANDRES PINEDA
 */
@ManagedBean
@SessionScoped
public class ControllerAdicionarPlantaEquipo implements Serializable {

    @EJB
    GestionarPlantaEquiposElementosBOInterface gestionarPlantaEquiposElementosBO;

    private List<Laboratorio> listaLaboratorios;
    private List<SalaLaboratorio> listaSalasLaboratorios;
    private List<ModuloLaboratorio> listaModulosLaboratorios;
    private List<TipoActivo> listaTiposActivos;
    private List<Proveedor> listaProveedores;
    private List<EstadoEquipo> listaEstadosEquipos;
    private boolean activarNuevoSalaLaboratorio;
    private boolean activarNuevoModuloLaboratorio;

    private String nuevoNombreEquipo, nuevoInventarioEquipo, nuevoMarcaEquipo, nuevoModeloEquipo, nuevoSerieEquipo;
    private String nuevoCostoAlquilerEquipo, nuevoEspecificacionEquipo, nuevoCostoInversionEquipo;
    private Date nuevoFechaAdquisicionEquipo;
    private Laboratorio nuevoLaboratorioEquipo;
    private SalaLaboratorio nuevoSalaLaboratorioEquipo;
    private ModuloLaboratorio nuevoModuloLaboratorioEquipo;
    private TipoActivo nuevoTipoActivoEquipo;
    private EstadoEquipo nuevoEstadoEquipoEquipo;
    private Proveedor nuevoProveedorEquipo;

    public ControllerAdicionarPlantaEquipo() {
    }

    @PostConstruct
    public void init() {
        activarNuevoModuloLaboratorio = true;
        activarNuevoSalaLaboratorio = true;
        listaTiposActivos = gestionarPlantaEquiposElementosBO.consultarTiposActivosRegistrador();
        listaEstadosEquipos = gestionarPlantaEquiposElementosBO.consultarEstadosEquiposRegistrados();
        listaProveedores = gestionarPlantaEquiposElementosBO.consultarProveedoresRegistrados();
        listaLaboratorios = gestionarPlantaEquiposElementosBO.consultarLaboratoriosRegistrados();
        listaModulosLaboratorios = null;
        listaSalasLaboratorios = null;
    }

    public void limpiarRegistroEquipoElemento() {
        activarNuevoModuloLaboratorio = true;
        activarNuevoSalaLaboratorio = true;
        nuevoNombreEquipo = null;
        nuevoInventarioEquipo = null;
        nuevoModeloEquipo = null;
        nuevoMarcaEquipo = null;
        nuevoSerieEquipo = null;
        nuevoCostoAlquilerEquipo = null;
        nuevoEspecificacionEquipo = null;
        nuevoCostoInversionEquipo = null;
        nuevoFechaAdquisicionEquipo = null;
        nuevoLaboratorioEquipo = null;
        nuevoSalaLaboratorioEquipo = null;
        nuevoModuloLaboratorioEquipo = null;
        nuevoTipoActivoEquipo = null;
        nuevoEstadoEquipoEquipo = null;
        nuevoProveedorEquipo = null;
        listaSalasLaboratorios = null;
        listaModulosLaboratorios = null;
    }

    public boolean validarStringEquipo() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nuevoNombreEquipo)) && (Utilidades.validarNulo(nuevoInventarioEquipo))
                && (Utilidades.validarNulo(nuevoMarcaEquipo))) {
            if (!Utilidades.validarCaracterString(nuevoNombreEquipo)) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarEstructuraEquipo() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(nuevoModuloLaboratorioEquipo)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(nuevoEstadoEquipoEquipo)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(nuevoTipoActivoEquipo)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(nuevoProveedorEquipo)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosEquipo() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoCostoAlquilerEquipo)) {
            if (!Utilidades.isNumber(nuevoCostoAlquilerEquipo)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(nuevoCostoInversionEquipo)) {
            if (!Utilidades.isNumber(nuevoCostoInversionEquipo)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechaEquipo() {
        boolean retorno = true;
        if (Utilidades.validarNulo(nuevoFechaAdquisicionEquipo)) {
            if (!Utilidades.fechaIngresadaCorrecta(nuevoFechaAdquisicionEquipo)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public void registrarNuevoEquipoElemento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarStringEquipo() == true) {
            if (validarEstructuraEquipo() == true) {
                if (validarDatosNumericosEquipo() == true) {
                    if (validarFechaEquipo() == true) {
                        almacenarNuevoEquipoEnSistema();
                    } else {
                        context.execute("errorFechaEquipoElemento.show()");
                    }
                } else {
                    context.execute("errorNumerosEquipoElemento.show()");
                }
            } else {
                context.execute("errorEstructuraEquipoElemento.show()");
            }
        } else {
            context.execute("errorInformacionEquipoElemento.show()");
        }
    }

    public void almacenarNuevoEquipoEnSistema() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            EquipoElemento salaNuevo = new EquipoElemento();
            salaNuevo.setNombreequipo(nuevoNombreEquipo);
            salaNuevo.setInventarioequipo(nuevoInventarioEquipo);
            salaNuevo.setMarcaequipo(nuevoMarcaEquipo);
            salaNuevo.setSeriequipo(nuevoSerieEquipo);
            salaNuevo.setModeloequipo(nuevoModeloEquipo);

            salaNuevo.setCantidadequipo(Integer.valueOf("1").intValue());
            salaNuevo.setCostoadquisicion(Integer.valueOf(nuevoCostoInversionEquipo));
            salaNuevo.setCostoalquiler(Integer.valueOf(nuevoCostoAlquilerEquipo));
            salaNuevo.setFechaadquisicion(nuevoFechaAdquisicionEquipo);
            salaNuevo.setEspecificacionestecnicas(nuevoEspecificacionEquipo);

            salaNuevo.setModulolaboratorio(nuevoModuloLaboratorioEquipo);
            salaNuevo.setTipoactivo(nuevoTipoActivoEquipo);
            salaNuevo.setEstadoequipo(nuevoEstadoEquipoEquipo);
            salaNuevo.setProveedor(nuevoProveedorEquipo);
            gestionarPlantaEquiposElementosBO.crearNuevoEquipoElemento(salaNuevo);
            context.execute("registroExitosoEquipoElemento.show()");
            context.update("formT:form:panelMenu");
            System.out.println("Proceso OK !");
            limpiarRegistroEquipoElemento();
        } catch (Exception e) {
            System.out.println("Error ControllerGestionarPlantaEquipoElemento almacenarNuevoEquipoEnSistema : " + e.toString());
            context.execute("registroFallidoModuloLaboratorio.show()");
        }
    }

    public void actualizarNuevoLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Here");
        if (Utilidades.validarNulo(nuevoLaboratorioEquipo)) {
            System.out.println("Validacion OK");
            nuevoSalaLaboratorioEquipo = null;
            listaSalasLaboratorios = gestionarPlantaEquiposElementosBO.consultarSalasLaboratorioPorIDLaboratorio(nuevoLaboratorioEquipo.getIdlaboratorio());
            activarNuevoSalaLaboratorio = false;

            nuevoModuloLaboratorioEquipo = null;
            activarNuevoModuloLaboratorio = true;
            listaModulosLaboratorios = null;
        } else {
            nuevoSalaLaboratorioEquipo = null;
            activarNuevoSalaLaboratorio = true;
            listaSalasLaboratorios = null;

            nuevoModuloLaboratorioEquipo = null;
            activarNuevoModuloLaboratorio = true;
            listaModulosLaboratorios = null;
        }
        System.out.println("Here");
        context.update("formT:form:nuevoSalaLaboratorioEquipo");
        context.update("formT:form:nuevoModuloLaboratorioEquipo");
    }

    public void actualizarNuevoSalaLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (Utilidades.validarNulo(nuevoSalaLaboratorioEquipo)) {
            nuevoModuloLaboratorioEquipo = null;
            listaModulosLaboratorios = gestionarPlantaEquiposElementosBO.consultarModulosLaboratorioPorIDSalaLaboratorio(nuevoSalaLaboratorioEquipo.getIdsalalaboratorio());
            activarNuevoModuloLaboratorio = false;
        } else {
            nuevoModuloLaboratorioEquipo = null;
            activarNuevoModuloLaboratorio = true;
            listaModulosLaboratorios = null;
        }
        context.update("formT:form:nuevoModuloLaboratorioEquipo");
    }

    //GET - SET
    public List<Laboratorio> getListaLaboratorios() {
        return listaLaboratorios;
    }

    public void setListaLaboratorios(List<Laboratorio> listaLaboratorios) {
        this.listaLaboratorios = listaLaboratorios;
    }

    public List<SalaLaboratorio> getListaSalasLaboratorios() {
        return listaSalasLaboratorios;
    }

    public void setListaSalasLaboratorios(List<SalaLaboratorio> listaSalasLaboratorios) {
        this.listaSalasLaboratorios = listaSalasLaboratorios;
    }

    public List<ModuloLaboratorio> getListaModulosLaboratorios() {
        return listaModulosLaboratorios;
    }

    public void setListaModulosLaboratorios(List<ModuloLaboratorio> listaModulosLaboratorios) {
        this.listaModulosLaboratorios = listaModulosLaboratorios;
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

    public List<EstadoEquipo> getListaEstadosEquipos() {
        return listaEstadosEquipos;
    }

    public void setListaEstadosEquipos(List<EstadoEquipo> listaEstadosEquipos) {
        this.listaEstadosEquipos = listaEstadosEquipos;
    }

    public boolean isActivarNuevoSalaLaboratorio() {
        return activarNuevoSalaLaboratorio;
    }

    public void setActivarNuevoSalaLaboratorio(boolean activarNuevoSalaLaboratorio) {
        this.activarNuevoSalaLaboratorio = activarNuevoSalaLaboratorio;
    }

    public boolean isActivarNuevoModuloLaboratorio() {
        return activarNuevoModuloLaboratorio;
    }

    public void setActivarNuevoModuloLaboratorio(boolean activarNuevoModuloLaboratorio) {
        this.activarNuevoModuloLaboratorio = activarNuevoModuloLaboratorio;
    }

    public String getNuevoNombreEquipo() {
        return nuevoNombreEquipo;
    }

    public void setNuevoNombreEquipo(String nuevoNombreEquipo) {
        this.nuevoNombreEquipo = nuevoNombreEquipo;
    }

    public String getNuevoInventarioEquipo() {
        return nuevoInventarioEquipo;
    }

    public void setNuevoInventarioEquipo(String nuevoInventarioEquipo) {
        this.nuevoInventarioEquipo = nuevoInventarioEquipo;
    }

    public String getNuevoMarcaEquipo() {
        return nuevoMarcaEquipo;
    }

    public void setNuevoMarcaEquipo(String nuevoMarcaEquipo) {
        this.nuevoMarcaEquipo = nuevoMarcaEquipo;
    }

    public String getNuevoModeloEquipo() {
        return nuevoModeloEquipo;
    }

    public void setNuevoModeloEquipo(String nuevoModeloEquipo) {
        this.nuevoModeloEquipo = nuevoModeloEquipo;
    }

    public String getNuevoSerieEquipo() {
        return nuevoSerieEquipo;
    }

    public void setNuevoSerieEquipo(String nuevoSerieEquipo) {
        this.nuevoSerieEquipo = nuevoSerieEquipo;
    }

    public String getNuevoCostoAlquilerEquipo() {
        return nuevoCostoAlquilerEquipo;
    }

    public void setNuevoCostoAlquilerEquipo(String nuevoCostoAlquilerEquipo) {
        this.nuevoCostoAlquilerEquipo = nuevoCostoAlquilerEquipo;
    }

    public String getNuevoEspecificacionEquipo() {
        return nuevoEspecificacionEquipo;
    }

    public void setNuevoEspecificacionEquipo(String nuevoEspecificacionEquipo) {
        this.nuevoEspecificacionEquipo = nuevoEspecificacionEquipo;
    }

    public String getNuevoCostoInversionEquipo() {
        return nuevoCostoInversionEquipo;
    }

    public void setNuevoCostoInversionEquipo(String nuevoCostoInversionEquipo) {
        this.nuevoCostoInversionEquipo = nuevoCostoInversionEquipo;
    }

    public Date getNuevoFechaAdquisicionEquipo() {
        return nuevoFechaAdquisicionEquipo;
    }

    public void setNuevoFechaAdquisicionEquipo(Date nuevoFechaAdquisicionEquipo) {
        this.nuevoFechaAdquisicionEquipo = nuevoFechaAdquisicionEquipo;
    }

    public Laboratorio getNuevoLaboratorioEquipo() {
        return nuevoLaboratorioEquipo;
    }

    public void setNuevoLaboratorioEquipo(Laboratorio nuevoLaboratorioEquipo) {
        this.nuevoLaboratorioEquipo = nuevoLaboratorioEquipo;
    }

    public SalaLaboratorio getNuevoSalaLaboratorioEquipo() {
        return nuevoSalaLaboratorioEquipo;
    }

    public void setNuevoSalaLaboratorioEquipo(SalaLaboratorio nuevoSalaLaboratorioEquipo) {
        this.nuevoSalaLaboratorioEquipo = nuevoSalaLaboratorioEquipo;
    }

    public ModuloLaboratorio getNuevoModuloLaboratorioEquipo() {
        return nuevoModuloLaboratorioEquipo;
    }

    public void setNuevoModuloLaboratorioEquipo(ModuloLaboratorio nuevoModuloLaboratorioEquipo) {
        this.nuevoModuloLaboratorioEquipo = nuevoModuloLaboratorioEquipo;
    }

    public TipoActivo getNuevoTipoActivoEquipo() {
        return nuevoTipoActivoEquipo;
    }

    public void setNuevoTipoActivoEquipo(TipoActivo nuevoTipoActivoEquipo) {
        this.nuevoTipoActivoEquipo = nuevoTipoActivoEquipo;
    }

    public EstadoEquipo getNuevoEstadoEquipoEquipo() {
        return nuevoEstadoEquipoEquipo;
    }

    public void setNuevoEstadoEquipoEquipo(EstadoEquipo nuevoEstadoEquipoEquipo) {
        this.nuevoEstadoEquipoEquipo = nuevoEstadoEquipoEquipo;
    }

    public Proveedor getNuevoProveedorEquipo() {
        return nuevoProveedorEquipo;
    }

    public void setNuevoProveedorEquipo(Proveedor nuevoProveedorEquipo) {
        this.nuevoProveedorEquipo = nuevoProveedorEquipo;
    }

}

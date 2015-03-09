package com.sirelab.controller.planta;

import com.sirelab.bo.interfacebo.GestionarPlantaEquiposElementosBOInterface;
import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.EquipoElemento;
import com.sirelab.entidades.EstadoEquipo;
import com.sirelab.entidades.ModuloLaboratorio;
import com.sirelab.entidades.Proveedor;
import com.sirelab.entidades.SalaLaboratorio;
import com.sirelab.entidades.TipoActivo;
import com.sirelab.utilidades.UsuarioLogin;
import com.sirelab.utilidades.Utilidades;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ANDRES PINEDA
 */
@ManagedBean
@SessionScoped
public class ControllerDetallesPlantaEquipo implements Serializable {

    @EJB
    GestionarPlantaEquiposElementosBOInterface gestionarPlantaEquiposElementosBO;

    private EquipoElemento equipoElementoDetalles;
    private BigInteger idEquipoElemento;
    private boolean activarEditar, disabledEditar;
    private boolean modificacionRegistro;
    private boolean visibleGuardar;
    private List<Laboratorio> listaLaboratorios;
    private Laboratorio laboratorioEquipoElemento;
    private List<AreaProfundizacion> listaAreasProfundizacion;
    private AreaProfundizacion areaEquipoElemento;
    private boolean activarAreaProfundizacion;
    private List<SalaLaboratorio> listaSalasLaboratorio;
    private SalaLaboratorio salaEquipoElemento;
    private boolean activarSalaLaboratorio;
    private List<ModuloLaboratorio> listaModulosLaboratorio;
    private ModuloLaboratorio moduloEquipoElemento;
    private boolean activarModuloLaboratorio;
    private List<TipoActivo> listaTiposActivos;
    private TipoActivo tipoActivoEquipoElemento;
    private List<EstadoEquipo> listaEstadosEquipos;
    private EstadoEquipo estadoEquipoElemento;
    private List<Proveedor> listaProveedores;
    private Proveedor proveedorEquipoElemento;
    private String nombreEquipoElemento, inventarioEquipoElemento;
    private String marcaEquipoElemento, modeloEquipoElemento, serieEquipoElemento;
    private String alquilerEquipoElemento, cantidadEquipoElemento, inversionEquipoElemento;
    private String especificacionEquipoElemento;
    private Date fechaEquipoElemento;

    public ControllerDetallesPlantaEquipo() {
    }

    @PostConstruct
    public void init() {
        activarAreaProfundizacion = true;
        activarSalaLaboratorio = true;
        activarModuloLaboratorio = true;
        //
        activarEditar = true; 
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        FacesContext faceContext = FacesContext.getCurrentInstance();
        HttpServletRequest httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        UsuarioLogin usuarioLoginSistema = (UsuarioLogin) httpServletRequest.getSession().getAttribute("sessionUsuario");
/*        if ("ADMINISTRADOR".equalsIgnoreCase(usuarioLoginSistema.getNombreTipoUsuario())) {
            disabledEditar = false;
        }*/
    }

    public void recibirIDEquiposElementoDetalles(BigInteger idEquipoElemento) {
        this.idEquipoElemento = idEquipoElemento;
        equipoElementoDetalles = gestionarPlantaEquiposElementosBO.obtenerEquipoElementoPorIDEquipoElemento(idEquipoElemento);
        asignarValoresVariablesEquipoElemento();
    }

    public void asignarValoresVariablesEquipoElemento() {
        nombreEquipoElemento = equipoElementoDetalles.getNombreequipo();
        inventarioEquipoElemento = equipoElementoDetalles.getInventarioequipo();
        alquilerEquipoElemento = equipoElementoDetalles.getCostoalquiler().toString();
        cantidadEquipoElemento = String.valueOf(equipoElementoDetalles.getCantidadequipo());
        inversionEquipoElemento = equipoElementoDetalles.getCostoadquisicion().toString();

        marcaEquipoElemento = equipoElementoDetalles.getMarcaequipo();
        modeloEquipoElemento = equipoElementoDetalles.getModeloequipo();
        serieEquipoElemento = equipoElementoDetalles.getSeriequipo();

        especificacionEquipoElemento = equipoElementoDetalles.getEspecificacionestecnicas();
        fechaEquipoElemento = equipoElementoDetalles.getFechaadquisicion();

        areaEquipoElemento = equipoElementoDetalles.getModulolaboratorio().getSalalaboratorio().getAreaprofundizacion();
        laboratorioEquipoElemento = equipoElementoDetalles.getModulolaboratorio().getSalalaboratorio().getAreaprofundizacion().getLaboratorio();
        salaEquipoElemento = equipoElementoDetalles.getModulolaboratorio().getSalalaboratorio();
        moduloEquipoElemento = equipoElementoDetalles.getModulolaboratorio();
        tipoActivoEquipoElemento = equipoElementoDetalles.getTipoactivo();
        estadoEquipoElemento = equipoElementoDetalles.getEstadoequipo();
        proveedorEquipoElemento = equipoElementoDetalles.getProveedor();
    }

    public void activarEditarRegistro() {
        activarEditar = false;
        disabledEditar = true;
        modificacionRegistro = false;
        visibleGuardar = true;
        listaLaboratorios = gestionarPlantaEquiposElementosBO.consultarLaboratoriosRegistrados();
        listaTiposActivos = gestionarPlantaEquiposElementosBO.consultarTiposActivosRegistrador();
        listaEstadosEquipos = gestionarPlantaEquiposElementosBO.consultarEstadosEquiposRegistrados();
        listaProveedores = gestionarPlantaEquiposElementosBO.consultarProveedoresRegistrados();
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void restaurarInformacionEquipoElemento() {
        equipoElementoDetalles = new EquipoElemento();
        equipoElementoDetalles = gestionarPlantaEquiposElementosBO.obtenerEquipoElementoPorIDEquipoElemento(idEquipoElemento);
        asignarValoresVariablesEquipoElemento();
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        activarAreaProfundizacion = true;
        activarSalaLaboratorio = true;
        activarModuloLaboratorio = true;
        listaLaboratorios = null;
        listaAreasProfundizacion = null;
        listaSalasLaboratorio = null;
        listaModulosLaboratorio = null;
        listaTiposActivos = null;
        listaEstadosEquipos = null;
        listaProveedores = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void actualizarLaboratorios() {
        try {
            if (Utilidades.validarNulo(laboratorioEquipoElemento)) {
                activarAreaProfundizacion = false;
                areaEquipoElemento = null;
                listaAreasProfundizacion = gestionarPlantaEquiposElementosBO.consultarAreasProfundizacionPorIDLaboratorio(laboratorioEquipoElemento.getIdlaboratorio());

                activarSalaLaboratorio = true;
                listaSalasLaboratorio = null;
                salaEquipoElemento = null;

                activarModuloLaboratorio = true;
                listaModulosLaboratorio = null;
                moduloEquipoElemento = null;

                modificacionesRegistroEquipoElemento();
            } else {
                areaEquipoElemento = null;
                listaAreasProfundizacion = null;
                activarAreaProfundizacion = true;

                activarSalaLaboratorio = true;
                listaSalasLaboratorio = null;
                salaEquipoElemento = null;

                activarModuloLaboratorio = true;
                listaModulosLaboratorio = null;
                moduloEquipoElemento = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:areaEquipoElemento");
            context.update("formT:form:salaEquipoElemento");
            context.update("formT:form:moduloEquipoElemento");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaEquipo actualizarLaboratorios : " + e.toString());
        }
    }

    public void actualizarAreasProfundizacion() {
        try {
            if (Utilidades.validarNulo(areaEquipoElemento)) {
                activarSalaLaboratorio = false;
                salaEquipoElemento = null;
                listaSalasLaboratorio = gestionarPlantaEquiposElementosBO.consultarSalasLaboratorioPorIDAreaProfundizacion(areaEquipoElemento.getIdareaprofundizacion());

                activarModuloLaboratorio = true;
                listaModulosLaboratorio = null;
                moduloEquipoElemento = null;

                modificacionesRegistroEquipoElemento();
            } else {
                activarSalaLaboratorio = true;
                listaSalasLaboratorio = null;
                salaEquipoElemento = null;

                activarModuloLaboratorio = true;
                listaModulosLaboratorio = null;
                moduloEquipoElemento = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:salaEquipoElemento");
            context.update("formT:form:moduloEquipoElemento");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaEquipo actualizarAreasProfundizacion : " + e.toString());
        }
    }

    public void actualizarSalasLaboratorio() {
        try {
            if (Utilidades.validarNulo(salaEquipoElemento)) {
                activarModuloLaboratorio = false;
                moduloEquipoElemento = null;
                listaModulosLaboratorio = gestionarPlantaEquiposElementosBO.consultarModulosLaboratorioPorIDSalaLaboratorio(salaEquipoElemento.getIdsalalaboratorio());
                modificacionesRegistroEquipoElemento();
            } else {
                activarModuloLaboratorio = true;
                listaModulosLaboratorio = null;
                moduloEquipoElemento = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:moduloEquipoElemento");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaEquipo actualizarSalasLaboratorio : " + e.toString());
        }
    }

    public void almacenarModificacionesEquipoElemento() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionRegistro == true) {
            if (validarInformacionEquipoElemento() == true) {
                if (validarDatosNumericosEquipoElemento() == true) {
                    if (validarEstructuraEquipoElemento() == true) {
                        if (validarFechaEquipoElemento() == true) {
                            modificarInformacionEquipoElemento();
                        } else {
                            context.execute("errorFechaEquipoElemento.show()");
                        }
                    } else {
                        context.execute("errorEstructuraEquipoElemento.show()");
                    }
                } else {
                    context.execute("errorNumerosEquipoElemento.show()");
                }
            } else {
                context.execute("errorInformacionEquipoElemento.show()");
            }
        } else {
            context.execute("mensajeNoCambiosReg.show()");
            restaurarInformacionEquipoElemento();
        }
    }

    public void modificarInformacionEquipoElemento() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            equipoElementoDetalles.setNombreequipo(inventarioEquipoElemento);
            equipoElementoDetalles.setInventarioequipo(nombreEquipoElemento);
            equipoElementoDetalles.setMarcaequipo(nombreEquipoElemento);
            equipoElementoDetalles.setModeloequipo(nombreEquipoElemento);
            equipoElementoDetalles.setSeriequipo(nombreEquipoElemento);

            equipoElementoDetalles.setEspecificacionestecnicas(especificacionEquipoElemento);
            equipoElementoDetalles.setFechaadquisicion(fechaEquipoElemento);

            equipoElementoDetalles.setCantidadequipo(Integer.valueOf(cantidadEquipoElemento).intValue());
            equipoElementoDetalles.setCostoadquisicion(Integer.valueOf(inversionEquipoElemento));
            equipoElementoDetalles.setCostoalquiler(Integer.valueOf(alquilerEquipoElemento));

            equipoElementoDetalles.setModulolaboratorio(moduloEquipoElemento);
            equipoElementoDetalles.setEstadoequipo(estadoEquipoElemento);
            equipoElementoDetalles.setTipoactivo(tipoActivoEquipoElemento);
            equipoElementoDetalles.setProveedor(proveedorEquipoElemento);

            gestionarPlantaEquiposElementosBO.modificarInformacionEquipoElemento(equipoElementoDetalles);
            context.execute("registroExitosoEquipoElemento.show()");
            restaurarInformacionEquipoElemento();
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaEquipo almacenarNuevoEquipoElementoEnSistema : " + e.toString());
            context.execute("registroFallidoEquipoElemento.show()");
        }
    }

    public boolean validarInformacionEquipoElemento() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nombreEquipoElemento)) && (Utilidades.validarNulo(inventarioEquipoElemento))
                && (Utilidades.validarNulo(marcaEquipoElemento))) {
            if (Utilidades.validarCaracterString(nombreEquipoElemento) == false) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosEquipoElemento() {
        boolean retorno = true;
        if (Utilidades.validarNulo(alquilerEquipoElemento)) {
            if (!Utilidades.isNumber(alquilerEquipoElemento)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(cantidadEquipoElemento)) {
            if (!Utilidades.isNumber(cantidadEquipoElemento)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(inversionEquipoElemento)) {
            if (!Utilidades.isNumber(inversionEquipoElemento)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechaEquipoElemento() {
        boolean retorno = true;
        if (!Utilidades.fechaIngresadaCorrecta(fechaEquipoElemento)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarEstructuraEquipoElemento() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(modeloEquipoElemento)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(estadoEquipoElemento)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(proveedorEquipoElemento)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(tipoActivoEquipoElemento)) {
            retorno = false;
        }
        return retorno;
    }

    public void modificacionesRegistroEquipoElemento() {
        if (modificacionRegistro == false) {
            modificacionRegistro = true;
        }
    }

    //GET - SET
    public EquipoElemento getEquipoElementoDetalles() {
        return equipoElementoDetalles;
    }

    public void setEquipoElementoDetalles(EquipoElemento equipoElementoDetalles) {
        this.equipoElementoDetalles = equipoElementoDetalles;
    }

    public BigInteger getIdEquipoElemento() {
        return idEquipoElemento;
    }

    public void setIdEquipoElemento(BigInteger idEquipoElemento) {
        this.idEquipoElemento = idEquipoElemento;
    }

    public boolean isActivarEditar() {
        return activarEditar;
    }

    public void setActivarEditar(boolean activarEditar) {
        this.activarEditar = activarEditar;
    }

    public boolean isDisabledEditar() {
        return disabledEditar;
    }

    public void setDisabledEditar(boolean disabledEditar) {
        this.disabledEditar = disabledEditar;
    }

    public boolean isModificacionRegistro() {
        return modificacionRegistro;
    }

    public void setModificacionRegistro(boolean modificacionRegistro) {
        this.modificacionRegistro = modificacionRegistro;
    }

    public boolean isVisibleGuardar() {
        return visibleGuardar;
    }

    public void setVisibleGuardar(boolean visibleGuardar) {
        this.visibleGuardar = visibleGuardar;
    }

    public List<Laboratorio> getListaLaboratorios() {
        return listaLaboratorios;
    }

    public void setListaLaboratorios(List<Laboratorio> listaLaboratorios) {
        this.listaLaboratorios = listaLaboratorios;
    }

    public Laboratorio getLaboratorioEquipoElemento() {
        return laboratorioEquipoElemento;
    }

    public void setLaboratorioEquipoElemento(Laboratorio laboratorioEquipoElemento) {
        this.laboratorioEquipoElemento = laboratorioEquipoElemento;
    }

    public List<AreaProfundizacion> getListaAreasProfundizacion() {
        return listaAreasProfundizacion;
    }

    public void setListaAreasProfundizacion(List<AreaProfundizacion> listaAreasProfundizacion) {
        this.listaAreasProfundizacion = listaAreasProfundizacion;
    }

    public AreaProfundizacion getAreaEquipoElemento() {
        return areaEquipoElemento;
    }

    public void setAreaEquipoElemento(AreaProfundizacion areaEquipoElemento) {
        this.areaEquipoElemento = areaEquipoElemento;
    }

    public boolean isActivarAreaProfundizacion() {
        return activarAreaProfundizacion;
    }

    public void setActivarAreaProfundizacion(boolean activarAreaProfundizacion) {
        this.activarAreaProfundizacion = activarAreaProfundizacion;
    }

    public List<SalaLaboratorio> getListaSalasLaboratorio() {
        return listaSalasLaboratorio;
    }

    public void setListaSalasLaboratorio(List<SalaLaboratorio> listaSalasLaboratorio) {
        this.listaSalasLaboratorio = listaSalasLaboratorio;
    }

    public SalaLaboratorio getSalaEquipoElemento() {
        return salaEquipoElemento;
    }

    public void setSalaEquipoElemento(SalaLaboratorio salaEquipoElemento) {
        this.salaEquipoElemento = salaEquipoElemento;
    }

    public boolean isActivarSalaLaboratorio() {
        return activarSalaLaboratorio;
    }

    public void setActivarSalaLaboratorio(boolean activarSalaLaboratorio) {
        this.activarSalaLaboratorio = activarSalaLaboratorio;
    }

    public List<ModuloLaboratorio> getListaModulosLaboratorio() {
        return listaModulosLaboratorio;
    }

    public void setListaModulosLaboratorio(List<ModuloLaboratorio> listaModulosLaboratorio) {
        this.listaModulosLaboratorio = listaModulosLaboratorio;
    }

    public ModuloLaboratorio getModuloEquipoElemento() {
        return moduloEquipoElemento;
    }

    public void setModuloEquipoElemento(ModuloLaboratorio moduloEquipoElemento) {
        this.moduloEquipoElemento = moduloEquipoElemento;
    }

    public boolean isActivarModuloLaboratorio() {
        return activarModuloLaboratorio;
    }

    public void setActivarModuloLaboratorio(boolean activarModuloLaboratorio) {
        this.activarModuloLaboratorio = activarModuloLaboratorio;
    }

    public List<TipoActivo> getListaTiposActivos() {
        return listaTiposActivos;
    }

    public void setListaTiposActivos(List<TipoActivo> listaTiposActivos) {
        this.listaTiposActivos = listaTiposActivos;
    }

    public TipoActivo getTipoActivoEquipoElemento() {
        return tipoActivoEquipoElemento;
    }

    public void setTipoActivoEquipoElemento(TipoActivo tipoActivoEquipoElemento) {
        this.tipoActivoEquipoElemento = tipoActivoEquipoElemento;
    }

    public List<EstadoEquipo> getListaEstadosEquipos() {
        return listaEstadosEquipos;
    }

    public void setListaEstadosEquipos(List<EstadoEquipo> listaEstadosEquipos) {
        this.listaEstadosEquipos = listaEstadosEquipos;
    }

    public EstadoEquipo getEstadoEquipoElemento() {
        return estadoEquipoElemento;
    }

    public void setEstadoEquipoElemento(EstadoEquipo estadoEquipoElemento) {
        this.estadoEquipoElemento = estadoEquipoElemento;
    }

    public List<Proveedor> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedor> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public Proveedor getProveedorEquipoElemento() {
        return proveedorEquipoElemento;
    }

    public void setProveedorEquipoElemento(Proveedor proveedorEquipoElemento) {
        this.proveedorEquipoElemento = proveedorEquipoElemento;
    }

    public String getNombreEquipoElemento() {
        return nombreEquipoElemento;
    }

    public void setNombreEquipoElemento(String nombreEquipoElemento) {
        this.nombreEquipoElemento = nombreEquipoElemento;
    }

    public String getInventarioEquipoElemento() {
        return inventarioEquipoElemento;
    }

    public void setInventarioEquipoElemento(String inventarioEquipoElemento) {
        this.inventarioEquipoElemento = inventarioEquipoElemento;
    }

    public String getMarcaEquipoElemento() {
        return marcaEquipoElemento;
    }

    public void setMarcaEquipoElemento(String marcaEquipoElemento) {
        this.marcaEquipoElemento = marcaEquipoElemento;
    }

    public String getModeloEquipoElemento() {
        return modeloEquipoElemento;
    }

    public void setModeloEquipoElemento(String modeloEquipoElemento) {
        this.modeloEquipoElemento = modeloEquipoElemento;
    }

    public String getSerieEquipoElemento() {
        return serieEquipoElemento;
    }

    public void setSerieEquipoElemento(String serieEquipoElemento) {
        this.serieEquipoElemento = serieEquipoElemento;
    }

    public String getAlquilerEquipoElemento() {
        return alquilerEquipoElemento;
    }

    public void setAlquilerEquipoElemento(String alquilerEquipoElemento) {
        this.alquilerEquipoElemento = alquilerEquipoElemento;
    }

    public String getCantidadEquipoElemento() {
        return cantidadEquipoElemento;
    }

    public void setCantidadEquipoElemento(String cantidadEquipoElemento) {
        this.cantidadEquipoElemento = cantidadEquipoElemento;
    }

    public String getInversionEquipoElemento() {
        return inversionEquipoElemento;
    }

    public void setInversionEquipoElemento(String inversionEquipoElemento) {
        this.inversionEquipoElemento = inversionEquipoElemento;
    }

    public String getEspecificacionEquipoElemento() {
        return especificacionEquipoElemento;
    }

    public void setEspecificacionEquipoElemento(String especificacionEquipoElemento) {
        this.especificacionEquipoElemento = especificacionEquipoElemento;
    }

    public Date getFechaEquipoElemento() {
        return fechaEquipoElemento;
    }

    public void setFechaEquipoElemento(Date fechaEquipoElemento) {
        this.fechaEquipoElemento = fechaEquipoElemento;
    }

}

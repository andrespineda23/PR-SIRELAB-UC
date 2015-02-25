package com.sirelab.controller.planta;

import com.sirelab.bo.interfacebo.GestionarPlantaSalasBOInterface;
import com.sirelab.entidades.AreaProfundizacion;
import com.sirelab.entidades.Departamento;
import com.sirelab.entidades.Edificio;
import com.sirelab.entidades.SalaLaboratorio;
import com.sirelab.entidades.Laboratorio;
import com.sirelab.entidades.Sede;
import com.sirelab.utilidades.UsuarioLogin;
import com.sirelab.utilidades.Utilidades;
import java.io.Serializable;
import java.math.BigInteger;
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
public class ControllerDetallesPlantaSala implements Serializable {

    @EJB
    GestionarPlantaSalasBOInterface gestionarPlantaSalasBO;

    private SalaLaboratorio salaLaboratorioDetalles;
    private BigInteger idSalaLaboratorio;
    private boolean activarEditar, disabledEditar;
    private boolean modificacionRegistro;
    private boolean disabledActivar, disabledInactivar;
    private boolean visibleGuardar;
    private List<Departamento> listaDepartamentos;
    private Departamento departamentoSalaLaboratorio;
    private List<Laboratorio> listaLaboratorios;
    private Laboratorio laboratorioSalaLaboratorio;
    private boolean activarLaboratorio;
    private List<AreaProfundizacion> listaAreasProfundizacion;
    private AreaProfundizacion areaSalaLaboratorio;
    private boolean activarAreaProfundizacion;
    private List<Sede> listaSedes;
    private Sede sedeSalaLaboratorio;
    private List<Edificio> listaEdificios;
    private Edificio edificioSalaLaboratorio;
    private boolean activarEdificio;
    private String nombreSalaLaboratorio, codigoSalaLaboratorio, ubicacionSalaLaboratorio, descripcionSalaLaboratorio;
    private String costoSalaLaboratorio, capacidadSalaLaboratorio, inversionSalaLaboratorio;

    public ControllerDetallesPlantaSala() {
    }

    @PostConstruct
    public void init() {
        activarAreaProfundizacion = true;
        activarEdificio = true;
        activarLaboratorio = true;
        //
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        FacesContext faceContext = FacesContext.getCurrentInstance();
        HttpServletRequest httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        UsuarioLogin usuarioLoginSistema = (UsuarioLogin) httpServletRequest.getSession().getAttribute("sessionUsuario");
        if ("ADMINISTRADOR".equalsIgnoreCase(usuarioLoginSistema.getNombreTipoUsuario())) {
            disabledEditar = false;
        }
    }

    public void recibirIDSalasLaboratorioDetalles(BigInteger idSalaLaboratorio) {
        this.idSalaLaboratorio = idSalaLaboratorio;
        salaLaboratorioDetalles = gestionarPlantaSalasBO.obtenerSalaLaboratorioPorIDSalaLaboratorio(idSalaLaboratorio);
        if (salaLaboratorioDetalles.getEstadosala() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesSalaLaboratorio();
    }

    public void asignarValoresVariablesSalaLaboratorio() {
        nombreSalaLaboratorio = salaLaboratorioDetalles.getNombresala();
        codigoSalaLaboratorio = salaLaboratorioDetalles.getCodigosala();
        ubicacionSalaLaboratorio = salaLaboratorioDetalles.getPisoubicacion();
        descripcionSalaLaboratorio = salaLaboratorioDetalles.getDescripcionsala();
        costoSalaLaboratorio = String.valueOf(salaLaboratorioDetalles.getCostoalquiler());
        capacidadSalaLaboratorio = String.valueOf(salaLaboratorioDetalles.getCapacidadsala());
        inversionSalaLaboratorio = salaLaboratorioDetalles.getValorinversion().toString();

        areaSalaLaboratorio = salaLaboratorioDetalles.getAreaprofundizacion();
        laboratorioSalaLaboratorio = salaLaboratorioDetalles.getAreaprofundizacion().getLaboratorio();
        departamentoSalaLaboratorio = salaLaboratorioDetalles.getAreaprofundizacion().getLaboratorio().getDepartamento();
        edificioSalaLaboratorio = salaLaboratorioDetalles.getEdificio();
        sedeSalaLaboratorio = salaLaboratorioDetalles.getEdificio().getSede();

    }

    public void activarEditarRegistro() {
        activarEditar = false;
        disabledEditar = true;
        modificacionRegistro = false;
        visibleGuardar = true;
        listaSedes = gestionarPlantaSalasBO.consultarSedesRegistradas();
        listaDepartamentos = gestionarPlantaSalasBO.consultarDepartamentosRegistrados();
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void restaurarInformacionSalaLaboratorio() {
        salaLaboratorioDetalles = new SalaLaboratorio();
        salaLaboratorioDetalles = gestionarPlantaSalasBO.obtenerSalaLaboratorioPorIDSalaLaboratorio(idSalaLaboratorio);
        if (salaLaboratorioDetalles.getEstadosala() == true) {
            disabledActivar = true;
            disabledInactivar = false;
        } else {
            disabledActivar = false;
            disabledInactivar = true;
        }
        asignarValoresVariablesSalaLaboratorio();
        activarEditar = true;
        disabledEditar = false;
        modificacionRegistro = false;
        visibleGuardar = false;
        activarAreaProfundizacion = true;
        activarEdificio = true;
        activarLaboratorio = true;
        listaAreasProfundizacion = null;
        listaDepartamentos = null;
        listaEdificios = null;
        listaLaboratorios = null;
        listaSedes = null;
        RequestContext.getCurrentInstance().update("formT:form:panelMenu");
    }

    public void actualizarDepartamentos() {
        try {
            if (Utilidades.validarNulo(departamentoSalaLaboratorio)) {
                activarLaboratorio = false;
                laboratorioSalaLaboratorio = null;
                listaLaboratorios = gestionarPlantaSalasBO.consultarLaboratoriosPorIDDepartamento(departamentoSalaLaboratorio.getIddepartamento());
                areaSalaLaboratorio = null;
                listaAreasProfundizacion = null;
                activarAreaProfundizacion = true;
                modificacionesRegistroSalaLaboratorio();
            } else {
                areaSalaLaboratorio = null;
                listaAreasProfundizacion = null;
                activarAreaProfundizacion = true;
                activarLaboratorio = true;
                laboratorioSalaLaboratorio = null;
                listaLaboratorios = null;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:laboratorioSalaLaboratorio");
            context.update("formT:form:areaSalaLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaSala actualizarDepartamentos : " + e.toString());
        }
    }

    public void actualizarLaboratorios() {
        try {
            if (Utilidades.validarNulo(laboratorioSalaLaboratorio)) {
                activarAreaProfundizacion = false;
                areaSalaLaboratorio = null;
                listaAreasProfundizacion = gestionarPlantaSalasBO.consultarAreasProfundizacionPorIDLaboratorio(laboratorioSalaLaboratorio.getIdlaboratorio());
                modificacionesRegistroSalaLaboratorio();
            } else {
                areaSalaLaboratorio = null;
                listaAreasProfundizacion = null;
                activarAreaProfundizacion = true;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:areaSalaLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaSala actualizarLaboratorios : " + e.toString());
        }
    }

    public void actualizarSedes() {
        try {
            if (Utilidades.validarNulo(sedeSalaLaboratorio)) {
                activarEdificio = false;
                edificioSalaLaboratorio = null;
                listaEdificios = gestionarPlantaSalasBO.consultarEdificiosPorIDSede(sedeSalaLaboratorio.getIdsede());
                modificacionesRegistroSalaLaboratorio();
            } else {
                edificioSalaLaboratorio = null;
                listaEdificios = null;
                activarEdificio = true;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("formT:form:edificioSalaLaboratorio");
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaSala actualizarSedes : " + e.toString());
        }
    }

    public void almacenarModificacionesSalaLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (modificacionRegistro == true) {
            if (validarInformacionSalaLaboratorio() == true) {
                if (validarDatosNumericosSalaLaboratorio() == true) {
                    if (validarEstructuraSalaLaboratorio() == true) {
                        modificarInformacionSalaLaboratorio();
                    } else {
                        context.execute("errorEstructuraSalaLaboratorio.show()");
                    }
                } else {
                    context.execute("errorNumerosSalaLaboratorio.show()");
                }
            } else {
                context.execute("errorInformacionSalaLaboratorio.show()");
            }
        } else {
            context.execute("mensajeNoCambiosReg.show()");
            restaurarInformacionSalaLaboratorio();
        }
    }

    public void modificarInformacionSalaLaboratorio() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            salaLaboratorioDetalles.setCodigosala(codigoSalaLaboratorio);
            salaLaboratorioDetalles.setValorinversion(new BigInteger(inversionSalaLaboratorio));
            salaLaboratorioDetalles.setPisoubicacion(ubicacionSalaLaboratorio);
            salaLaboratorioDetalles.setDescripcionsala(descripcionSalaLaboratorio);
            salaLaboratorioDetalles.setNombresala(nombreSalaLaboratorio);
            salaLaboratorioDetalles.setCostoalquiler(Long.valueOf(costoSalaLaboratorio).longValue());
            salaLaboratorioDetalles.setCapacidadsala(Integer.valueOf(capacidadSalaLaboratorio).intValue());
            salaLaboratorioDetalles.setAreaprofundizacion(areaSalaLaboratorio);
            salaLaboratorioDetalles.setEdificio(edificioSalaLaboratorio);
            gestionarPlantaSalasBO.modificarInformacionSalaLaboratorio(salaLaboratorioDetalles);
            context.execute("registroExitosoSalaLaboratorio.show()");
            restaurarInformacionSalaLaboratorio();
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesPlantaSala almacenarNuevoSalaLaboratorioEnSistema : " + e.toString());
            context.execute("registroFallidoSalaLaboratorio.show()");
        }
    }

    public boolean validarInformacionSalaLaboratorio() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(nombreSalaLaboratorio)) && (Utilidades.validarNulo(codigoSalaLaboratorio))
                && (Utilidades.validarNulo(ubicacionSalaLaboratorio)) && (Utilidades.validarNulo(descripcionSalaLaboratorio))) {
            if (Utilidades.validarCaracterString(nombreSalaLaboratorio) == false) {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean validarDatosNumericosSalaLaboratorio() {
        boolean retorno = true;
        if ((Utilidades.validarNulo(costoSalaLaboratorio)) && (Utilidades.validarNulo(capacidadSalaLaboratorio))) {
            if (!Utilidades.isNumberLong(costoSalaLaboratorio)) {
                retorno = false;
            }
            if (!Utilidades.isNumberLong(capacidadSalaLaboratorio)) {
                retorno = false;
            }
        }
        if (Utilidades.validarNulo(inversionSalaLaboratorio)) {
            if (!Utilidades.isNumber(inversionSalaLaboratorio)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarEstructuraSalaLaboratorio() {
        boolean retorno = true;
        if (!Utilidades.validarNulo(areaSalaLaboratorio)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(laboratorioSalaLaboratorio)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(departamentoSalaLaboratorio)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(sedeSalaLaboratorio)) {
            retorno = false;
        }
        if (!Utilidades.validarNulo(edificioSalaLaboratorio)) {
            retorno = false;
        }
        return retorno;
    }

    public void modificacionesRegistroSalaLaboratorio() {
        if (modificacionRegistro == false) {
            modificacionRegistro = true;
        }
    }

    public void activarSalaLaboratorio() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(true);
                salaLaboratorioDetalles.setEstadosala(bool);
                gestionarPlantaSalasBO.modificarInformacionSalaLaboratorio(salaLaboratorioDetalles);
                restaurarInformacionSalaLaboratorio();
                context.execute("registroExitosoSalaLaboratorio.show()");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesSalasLaboratorio activarSalaLaboratorio : " + e.toString());
        }
    }

    public void inactivarSalaLaboratorio() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (modificacionRegistro == false) {
                Boolean bool = new Boolean(false);
                salaLaboratorioDetalles.setEstadosala(bool);
                gestionarPlantaSalasBO.modificarInformacionSalaLaboratorio(salaLaboratorioDetalles);
                salaLaboratorioDetalles = new SalaLaboratorio();
                context.update("formT:form:panelMenu");
                restaurarInformacionSalaLaboratorio();
                context.execute("registroExitosoSalaLaboratorio.show();");
                context.update("formT:form:panelMenu");
            } else {
                context.execute("confirmarGuardar.show()");
            }
        } catch (Exception e) {
            System.out.println("Error ControllerDetallesSalasLaboratorio inactivarSalaLaboratorio : " + e.toString());
        }
    }

    //GET - SET
    public SalaLaboratorio getSalaLaboratorioDetalles() {
        return salaLaboratorioDetalles;
    }

    public void setSalaLaboratorioDetalles(SalaLaboratorio salaLaboratorioDetalles) {
        this.salaLaboratorioDetalles = salaLaboratorioDetalles;
    }

    public BigInteger getIdSalaLaboratorio() {
        return idSalaLaboratorio;
    }

    public void setIdSalaLaboratorio(BigInteger idSalaLaboratorio) {
        this.idSalaLaboratorio = idSalaLaboratorio;
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

    public boolean isDisabledActivar() {
        return disabledActivar;
    }

    public void setDisabledActivar(boolean disabledActivar) {
        this.disabledActivar = disabledActivar;
    }

    public boolean isDisabledInactivar() {
        return disabledInactivar;
    }

    public void setDisabledInactivar(boolean disabledInactivar) {
        this.disabledInactivar = disabledInactivar;
    }

    public boolean isVisibleGuardar() {
        return visibleGuardar;
    }

    public void setVisibleGuardar(boolean visibleGuardar) {
        this.visibleGuardar = visibleGuardar;
    }

    public List<Departamento> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamento> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public Departamento getDepartamentoSalaLaboratorio() {
        return departamentoSalaLaboratorio;
    }

    public void setDepartamentoSalaLaboratorio(Departamento departamentoSalaLaboratorio) {
        this.departamentoSalaLaboratorio = departamentoSalaLaboratorio;
    }

    public List<Laboratorio> getListaLaboratorios() {
        return listaLaboratorios;
    }

    public void setListaLaboratorios(List<Laboratorio> listaLaboratorios) {
        this.listaLaboratorios = listaLaboratorios;
    }

    public Laboratorio getLaboratorioSalaLaboratorio() {
        return laboratorioSalaLaboratorio;
    }

    public void setLaboratorioSalaLaboratorio(Laboratorio laboratorioSalaLaboratorio) {
        this.laboratorioSalaLaboratorio = laboratorioSalaLaboratorio;
    }

    public boolean isActivarLaboratorio() {
        return activarLaboratorio;
    }

    public void setActivarLaboratorio(boolean activarLaboratorio) {
        this.activarLaboratorio = activarLaboratorio;
    }

    public List<AreaProfundizacion> getListaAreasProfundizacion() {
        return listaAreasProfundizacion;
    }

    public void setListaAreasProfundizacion(List<AreaProfundizacion> listaAreasProfundizacion) {
        this.listaAreasProfundizacion = listaAreasProfundizacion;
    }

    public AreaProfundizacion getAreaSalaLaboratorio() {
        return areaSalaLaboratorio;
    }

    public void setAreaSalaLaboratorio(AreaProfundizacion areaSalaLaboratorio) {
        this.areaSalaLaboratorio = areaSalaLaboratorio;
    }

    public boolean isActivarAreaProfundizacion() {
        return activarAreaProfundizacion;
    }

    public void setActivarAreaProfundizacion(boolean activarAreaProfundizacion) {
        this.activarAreaProfundizacion = activarAreaProfundizacion;
    }

    public List<Sede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<Sede> listaSedes) {
        this.listaSedes = listaSedes;
    }

    public Sede getSedeSalaLaboratorio() {
        return sedeSalaLaboratorio;
    }

    public void setSedeSalaLaboratorio(Sede sedeSalaLaboratorio) {
        this.sedeSalaLaboratorio = sedeSalaLaboratorio;
    }

    public List<Edificio> getListaEdificios() {
        return listaEdificios;
    }

    public void setListaEdificios(List<Edificio> listaEdificios) {
        this.listaEdificios = listaEdificios;
    }

    public Edificio getEdificioSalaLaboratorio() {
        return edificioSalaLaboratorio;
    }

    public void setEdificioSalaLaboratorio(Edificio edificioSalaLaboratorio) {
        this.edificioSalaLaboratorio = edificioSalaLaboratorio;
    }

    public boolean isActivarEdificio() {
        return activarEdificio;
    }

    public void setActivarEdificio(boolean activarEdificio) {
        this.activarEdificio = activarEdificio;
    }

    public String getNombreSalaLaboratorio() {
        return nombreSalaLaboratorio;
    }

    public void setNombreSalaLaboratorio(String nombreSalaLaboratorio) {
        this.nombreSalaLaboratorio = nombreSalaLaboratorio;
    }

    public String getCodigoSalaLaboratorio() {
        return codigoSalaLaboratorio;
    }

    public void setCodigoSalaLaboratorio(String codigoSalaLaboratorio) {
        this.codigoSalaLaboratorio = codigoSalaLaboratorio;
    }

    public String getUbicacionSalaLaboratorio() {
        return ubicacionSalaLaboratorio;
    }

    public void setUbicacionSalaLaboratorio(String ubicacionSalaLaboratorio) {
        this.ubicacionSalaLaboratorio = ubicacionSalaLaboratorio;
    }

    public String getDescripcionSalaLaboratorio() {
        return descripcionSalaLaboratorio;
    }

    public void setDescripcionSalaLaboratorio(String descripcionSalaLaboratorio) {
        this.descripcionSalaLaboratorio = descripcionSalaLaboratorio;
    }

    public String getCostoSalaLaboratorio() {
        return costoSalaLaboratorio;
    }

    public void setCostoSalaLaboratorio(String costoSalaLaboratorio) {
        this.costoSalaLaboratorio = costoSalaLaboratorio;
    }

    public String getCapacidadSalaLaboratorio() {
        return capacidadSalaLaboratorio;
    }

    public void setCapacidadSalaLaboratorio(String capacidadSalaLaboratorio) {
        this.capacidadSalaLaboratorio = capacidadSalaLaboratorio;
    }

    public String getInversionSalaLaboratorio() {
        return inversionSalaLaboratorio;
    }

    public void setInversionSalaLaboratorio(String inversionSalaLaboratorio) {
        this.inversionSalaLaboratorio = inversionSalaLaboratorio;
    }

}

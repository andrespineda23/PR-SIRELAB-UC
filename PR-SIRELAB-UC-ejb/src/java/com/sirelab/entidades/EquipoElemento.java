/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sirelab.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "equipoelemento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquipoElemento.findAll", query = "SELECT e FROM EquipoElemento e"),
    @NamedQuery(name = "EquipoElemento.findByIdequipoelemento", query = "SELECT e FROM EquipoElemento e WHERE e.idequipoelemento = :idequipoelemento"),
    @NamedQuery(name = "EquipoElemento.findByNombreequipo", query = "SELECT e FROM EquipoElemento e WHERE e.nombreequipo = :nombreequipo"),
    @NamedQuery(name = "EquipoElemento.findByMarcaequipo", query = "SELECT e FROM EquipoElemento e WHERE e.marcaequipo = :marcaequipo"),
    @NamedQuery(name = "EquipoElemento.findByInventarioequipo", query = "SELECT e FROM EquipoElemento e WHERE e.inventarioequipo = :inventarioequipo"),
    @NamedQuery(name = "EquipoElemento.findByModeloequipo", query = "SELECT e FROM EquipoElemento e WHERE e.modeloequipo = :modeloequipo"),
    @NamedQuery(name = "EquipoElemento.findBySeriequipo", query = "SELECT e FROM EquipoElemento e WHERE e.seriequipo = :seriequipo"),
    @NamedQuery(name = "EquipoElemento.findByEspecificacionestecnicas", query = "SELECT e FROM EquipoElemento e WHERE e.especificacionestecnicas = :especificacionestecnicas"),
    @NamedQuery(name = "EquipoElemento.findByCantidadequipo", query = "SELECT e FROM EquipoElemento e WHERE e.cantidadequipo = :cantidadequipo"),
    @NamedQuery(name = "EquipoElemento.findByCostoalquiler", query = "SELECT e FROM EquipoElemento e WHERE e.costoalquiler = :costoalquiler"),
    @NamedQuery(name = "EquipoElemento.findByCostoadquisicion", query = "SELECT e FROM EquipoElemento e WHERE e.costoadquisicion = :costoadquisicion"),
    @NamedQuery(name = "EquipoElemento.findByFechaadquisicion", query = "SELECT e FROM EquipoElemento e WHERE e.fechaadquisicion = :fechaadquisicion")})
public class EquipoElemento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idequipoelemento")
    private BigInteger idequipoelemento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreequipo")
    private String nombreequipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "marcaequipo")
    private String marcaequipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "inventarioequipo")
    private String inventarioequipo;
    @Size(max = 45)
    @Column(name = "modeloequipo")
    private String modeloequipo;
    @Size(max = 45)
    @Column(name = "seriequipo")
    private String seriequipo;
    @Size(max = 500)
    @Column(name = "especificacionestecnicas")
    private String especificacionestecnicas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidadequipo")
    private int cantidadequipo;
    @Column(name = "costoalquiler")
    private Integer costoalquiler;
    @Column(name = "costoadquisicion")
    private Integer costoadquisicion;
    @Column(name = "fechaadquisicion")
    @Temporal(TemporalType.DATE)
    private Date fechaadquisicion;
    @JoinColumn(name = "tipoactivo", referencedColumnName = "idtipoactivo")
    @ManyToOne(optional = false)
    private TipoActivo tipoactivo;
    @JoinColumn(name = "proveedor", referencedColumnName = "idproveedor")
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    @JoinColumn(name = "modulolaboratorio", referencedColumnName = "idmodulolaboratorio")
    @ManyToOne(optional = false)
    private ModuloLaboratorio modulolaboratorio;
    @JoinColumn(name = "estadoequipo", referencedColumnName = "idestadoequipo")
    @ManyToOne(optional = false)
    private EstadoEquipo estadoequipo;

    public EquipoElemento() {
    }

    public EquipoElemento(BigInteger idequipoelemento) {
        this.idequipoelemento = idequipoelemento;
    }

    public EquipoElemento(BigInteger idequipoelemento, String nombreequipo, String marcaequipo, String inventarioequipo, int cantidadequipo) {
        this.idequipoelemento = idequipoelemento;
        this.nombreequipo = nombreequipo;
        this.marcaequipo = marcaequipo;
        this.inventarioequipo = inventarioequipo;
        this.cantidadequipo = cantidadequipo;
    }

    public BigInteger getIdequipoelemento() {
        return idequipoelemento;
    }

    public void setIdequipoelemento(BigInteger idequipoelemento) {
        this.idequipoelemento = idequipoelemento;
    }

    public String getNombreequipo() {
        if(null != nombreequipo){
            return nombreequipo.toUpperCase();
        }
        return nombreequipo;
    }

    public void setNombreequipo(String nombreequipo) {
        this.nombreequipo = nombreequipo.toUpperCase();
    }

    public String getMarcaequipo() {
        if(null != marcaequipo){
            return marcaequipo.toUpperCase();
        }
        return marcaequipo;
    }

    public void setMarcaequipo(String marcaequipo) {
        this.marcaequipo = marcaequipo.toUpperCase();
    }

    public String getInventarioequipo() {
        if(null != inventarioequipo){
            return inventarioequipo.toUpperCase();
        }
        return inventarioequipo;
    }

    public void setInventarioequipo(String inventarioequipo) {
        this.inventarioequipo = inventarioequipo.toUpperCase();
    }

    public String getModeloequipo() {
        if(null != modeloequipo){
            return modeloequipo.toUpperCase();
        }
        return modeloequipo;
    }

    public void setModeloequipo(String modeloequipo) {
        this.modeloequipo = modeloequipo.toUpperCase();
    }

    public String getSeriequipo() {
        if(null != seriequipo){
            return seriequipo.toUpperCase();
        }
        return seriequipo;
    }

    public void setSeriequipo(String seriequipo) {
        this.seriequipo = seriequipo.toUpperCase();
    }

    public String getEspecificacionestecnicas() {
        if(null != especificacionestecnicas){
            return especificacionestecnicas.toUpperCase();
        }
        return especificacionestecnicas;
    }

    public void setEspecificacionestecnicas(String especificacionestecnicas) {
        this.especificacionestecnicas = especificacionestecnicas.toUpperCase();
    }

    public int getCantidadequipo() {
        return cantidadequipo;
    }

    public void setCantidadequipo(int cantidadequipo) {
        this.cantidadequipo = cantidadequipo;
    }

    public Integer getCostoalquiler() {
        return costoalquiler;
    }

    public void setCostoalquiler(Integer costoalquiler) {
        this.costoalquiler = costoalquiler;
    }

    public Integer getCostoadquisicion() {
        return costoadquisicion;
    }

    public void setCostoadquisicion(Integer costoadquisicion) {
        this.costoadquisicion = costoadquisicion;
    }

    public Date getFechaadquisicion() {
        return fechaadquisicion;
    }

    public void setFechaadquisicion(Date fechaadquisicion) {
        this.fechaadquisicion = fechaadquisicion;
    }

    public TipoActivo getTipoactivo() {
        return tipoactivo;
    }

    public void setTipoactivo(TipoActivo tipoactivo) {
        this.tipoactivo = tipoactivo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public ModuloLaboratorio getModulolaboratorio() {
        return modulolaboratorio;
    }

    public void setModulolaboratorio(ModuloLaboratorio modulolaboratorio) {
        this.modulolaboratorio = modulolaboratorio;
    }

    public EstadoEquipo getEstadoequipo() {
        return estadoequipo;
    }

    public void setEstadoequipo(EstadoEquipo estadoequipo) {
        this.estadoequipo = estadoequipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idequipoelemento != null ? idequipoelemento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquipoElemento)) {
            return false;
        }
        EquipoElemento other = (EquipoElemento) object;
        if ((this.idequipoelemento == null && other.idequipoelemento != null) || (this.idequipoelemento != null && !this.idequipoelemento.equals(other.idequipoelemento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.EquipoElemento[ idequipoelemento=" + idequipoelemento + " ]";
    }
    
}

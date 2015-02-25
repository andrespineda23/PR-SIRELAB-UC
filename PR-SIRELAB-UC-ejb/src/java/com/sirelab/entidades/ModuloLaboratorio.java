/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "modulolaboratorio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModuloLaboratorio.findAll", query = "SELECT m FROM ModuloLaboratorio m"),
    @NamedQuery(name = "ModuloLaboratorio.findByIdmodulolaboratorio", query = "SELECT m FROM ModuloLaboratorio m WHERE m.idmodulolaboratorio = :idmodulolaboratorio"),
    @NamedQuery(name = "ModuloLaboratorio.findByCodigomodulo", query = "SELECT m FROM ModuloLaboratorio m WHERE m.codigomodulo = :codigomodulo"),
    @NamedQuery(name = "ModuloLaboratorio.findByDetallemodulo", query = "SELECT m FROM ModuloLaboratorio m WHERE m.detallemodulo = :detallemodulo"),
    @NamedQuery(name = "ModuloLaboratorio.findByEstadomodulo", query = "SELECT m FROM ModuloLaboratorio m WHERE m.estadomodulo = :estadomodulo"),
    @NamedQuery(name = "ModuloLaboratorio.findByCapacidadmodulo", query = "SELECT m FROM ModuloLaboratorio m WHERE m.capacidadmodulo = :capacidadmodulo"),
    @NamedQuery(name = "ModuloLaboratorio.findByCostomodulo", query = "SELECT m FROM ModuloLaboratorio m WHERE m.costomodulo = :costomodulo"),
    @NamedQuery(name = "ModuloLaboratorio.findByCostoalquiler", query = "SELECT m FROM ModuloLaboratorio m WHERE m.costoalquiler = :costoalquiler")})
public class ModuloLaboratorio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmodulolaboratorio")
    private BigInteger idmodulolaboratorio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigomodulo")
    private String codigomodulo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "detallemodulo")
    private String detallemodulo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estadomodulo")
    private boolean estadomodulo;
    @Column(name = "capacidadmodulo")
    private Integer capacidadmodulo;
    @Column(name = "costomodulo")
    private BigInteger costomodulo;
    @Column(name = "costoalquiler")
    private BigInteger costoalquiler;
    @JoinColumn(name = "salalaboratorio", referencedColumnName = "idsalalaboratorio")
    @ManyToOne(optional = false)
    private SalaLaboratorio salalaboratorio;
    @Transient
    private String strEstado;

    public ModuloLaboratorio() {
    }

    public ModuloLaboratorio(BigInteger idmodulolaboratorio) {
        this.idmodulolaboratorio = idmodulolaboratorio;
    }

    public ModuloLaboratorio(BigInteger idmodulolaboratorio, String codigomodulo, String detallemodulo, boolean estadomodulo) {
        this.idmodulolaboratorio = idmodulolaboratorio;
        this.codigomodulo = codigomodulo;
        this.detallemodulo = detallemodulo;
        this.estadomodulo = estadomodulo;
    }

    public BigInteger getIdmodulolaboratorio() {
        return idmodulolaboratorio;
    }

    public void setIdmodulolaboratorio(BigInteger idmodulolaboratorio) {
        this.idmodulolaboratorio = idmodulolaboratorio;
    }

    public String getCodigomodulo() {
        if (null != codigomodulo) {
            return codigomodulo.toUpperCase();
        }
        return codigomodulo;
    }

    public void setCodigomodulo(String codigomodulo) {
        this.codigomodulo = codigomodulo.toUpperCase();
    }

    public String getDetallemodulo() {
        if (null != detallemodulo) {
            return detallemodulo.toUpperCase();
        }
        return detallemodulo;
    }

    public void setDetallemodulo(String detallemodulo) {
        this.detallemodulo = detallemodulo.toUpperCase();
    }

    public boolean getEstadomodulo() {
        return estadomodulo;
    }

    public void setEstadomodulo(boolean estadomodulo) {
        this.estadomodulo = estadomodulo;
    }

    public String getStrEstado() {
        getEstadomodulo();
        if (estadomodulo == true) {
            strEstado = "ACTIVO";
        } else {
            strEstado = "INACTIVO";
        }
        return strEstado;
    }

    public void setStrEstado(String strEstado) {
        this.strEstado = strEstado;
    }

    public Integer getCapacidadmodulo() {
        return capacidadmodulo;
    }

    public void setCapacidadmodulo(Integer capacidadmodulo) {
        this.capacidadmodulo = capacidadmodulo;
    }

    public BigInteger getCostomodulo() {
        return costomodulo;
    }

    public void setCostomodulo(BigInteger costomodulo) {
        this.costomodulo = costomodulo;
    }

    public BigInteger getCostoalquiler() {
        return costoalquiler;
    }

    public void setCostoalquiler(BigInteger costoalquiler) {
        this.costoalquiler = costoalquiler;
    }

    public SalaLaboratorio getSalalaboratorio() {
        return salalaboratorio;
    }

    public void setSalalaboratorio(SalaLaboratorio salalaboratorio) {
        this.salalaboratorio = salalaboratorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmodulolaboratorio != null ? idmodulolaboratorio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModuloLaboratorio)) {
            return false;
        }
        ModuloLaboratorio other = (ModuloLaboratorio) object;
        if ((this.idmodulolaboratorio == null && other.idmodulolaboratorio != null) || (this.idmodulolaboratorio != null && !this.idmodulolaboratorio.equals(other.idmodulolaboratorio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.ModuloLaboratorio[ idmodulolaboratorio=" + idmodulolaboratorio + " ]";
    }

}

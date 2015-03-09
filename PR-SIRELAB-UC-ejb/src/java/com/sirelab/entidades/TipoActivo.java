/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sirelab.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "tipoactivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoActivo.findAll", query = "SELECT t FROM TipoActivo t"),
    @NamedQuery(name = "TipoActivo.findByIdtipoactivo", query = "SELECT t FROM TipoActivo t WHERE t.idtipoactivo = :idtipoactivo"),
    @NamedQuery(name = "TipoActivo.findByNombretipoactivo", query = "SELECT t FROM TipoActivo t WHERE t.nombretipoactivo = :nombretipoactivo")})
public class TipoActivo implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoactivo")
    private Collection<EquipoElemento> equipoElementoCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtipoactivo")
    private BigInteger idtipoactivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombretipoactivo")
    private String nombretipoactivo;

    public TipoActivo() {
    }

    public TipoActivo(BigInteger idtipoactivo) {
        this.idtipoactivo = idtipoactivo;
    }

    public TipoActivo(BigInteger idtipoactivo, String nombretipoactivo) {
        this.idtipoactivo = idtipoactivo;
        this.nombretipoactivo = nombretipoactivo;
    }

    public BigInteger getIdtipoactivo() {
        return idtipoactivo;
    }

    public void setIdtipoactivo(BigInteger idtipoactivo) {
        this.idtipoactivo = idtipoactivo;
    }

    public String getNombretipoactivo() {
        if (null != nombretipoactivo) {
            return nombretipoactivo.toUpperCase();
        }
        return nombretipoactivo;
    }

    public void setNombretipoactivo(String nombretipoactivo) {
        this.nombretipoactivo = nombretipoactivo.toUpperCase();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoactivo != null ? idtipoactivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoActivo)) {
            return false;
        }
        TipoActivo other = (TipoActivo) object;
        if ((this.idtipoactivo == null && other.idtipoactivo != null) || (this.idtipoactivo != null && !this.idtipoactivo.equals(other.idtipoactivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.TipoActivo[ idtipoactivo=" + idtipoactivo + " ]";
    }

    @XmlTransient
    public Collection<EquipoElemento> getEquipoElementoCollection() {
        return equipoElementoCollection;
    }

    public void setEquipoElementoCollection(Collection<EquipoElemento> equipoElementoCollection) {
        this.equipoElementoCollection = equipoElementoCollection;
    }

}

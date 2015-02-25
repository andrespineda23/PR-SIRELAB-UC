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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "insumo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Insumo.findAll", query = "SELECT i FROM Insumo i"),
    @NamedQuery(name = "Insumo.findByIdinsumo", query = "SELECT i FROM Insumo i WHERE i.idinsumo = :idinsumo"),
    @NamedQuery(name = "Insumo.findByNombreinsumo", query = "SELECT i FROM Insumo i WHERE i.nombreinsumo = :nombreinsumo"),
    @NamedQuery(name = "Insumo.findByCodigoinsumo", query = "SELECT i FROM Insumo i WHERE i.codigoinsumo = :codigoinsumo"),
    @NamedQuery(name = "Insumo.findByDescripcioninsumo", query = "SELECT i FROM Insumo i WHERE i.descripcioninsumo = :descripcioninsumo"),
    @NamedQuery(name = "Insumo.findByCantidadminimia", query = "SELECT i FROM Insumo i WHERE i.cantidadminimia = :cantidadminimia"),
    @NamedQuery(name = "Insumo.findByCantidadexistencia", query = "SELECT i FROM Insumo i WHERE i.cantidadexistencia = :cantidadexistencia"),
    @NamedQuery(name = "Insumo.findByMarcainsumo", query = "SELECT i FROM Insumo i WHERE i.marcainsumo = :marcainsumo"),
    @NamedQuery(name = "Insumo.findByModeloinsumo", query = "SELECT i FROM Insumo i WHERE i.modeloinsumo = :modeloinsumo")})
public class Insumo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idinsumo")
    private BigInteger idinsumo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombreinsumo")
    private String nombreinsumo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigoinsumo")
    private String codigoinsumo;
    @Size(max = 500)
    @Column(name = "descripcioninsumo")
    private String descripcioninsumo;
    @Column(name = "cantidadminimia")
    private Integer cantidadminimia;
    @Column(name = "cantidadexistencia")
    private Integer cantidadexistencia;
    @Size(max = 45)
    @Column(name = "marcainsumo")
    private String marcainsumo;
    @Size(max = 45)
    @Column(name = "modeloinsumo")
    private String modeloinsumo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "insumo")
    private Collection<MovimientoInsumo> movimientoInsumoCollection;
    @JoinColumn(name = "proveedor", referencedColumnName = "idproveedor")
    @ManyToOne(optional = false)
    private Proveedor proveedor;

    public Insumo() {
    }

    public Insumo(BigInteger idinsumo) {
        this.idinsumo = idinsumo;
    }

    public Insumo(BigInteger idinsumo, String nombreinsumo, String codigoinsumo) {
        this.idinsumo = idinsumo;
        this.nombreinsumo = nombreinsumo;
        this.codigoinsumo = codigoinsumo;
    }

    public BigInteger getIdinsumo() {
        return idinsumo;
    }

    public void setIdinsumo(BigInteger idinsumo) {
        this.idinsumo = idinsumo;
    }

    public String getNombreinsumo() {
        if (null != nombreinsumo) {
            return nombreinsumo.toUpperCase();
        }
        return nombreinsumo;
    }

    public void setNombreinsumo(String nombreinsumo) {
        this.nombreinsumo = nombreinsumo.toUpperCase();
    }

    public String getCodigoinsumo() {
        if(null != codigoinsumo){
            return codigoinsumo.toUpperCase();
        }
        return codigoinsumo;
    }

    public void setCodigoinsumo(String codigoinsumo) {
        this.codigoinsumo = codigoinsumo.toUpperCase();
    }

    public String getDescripcioninsumo() {
        if(null != descripcioninsumo){
            return descripcioninsumo.toUpperCase();
        }
        return descripcioninsumo;
    }

    public void setDescripcioninsumo(String descripcioninsumo) {
        this.descripcioninsumo = descripcioninsumo.toUpperCase();
    }

    public Integer getCantidadminimia() {
        return cantidadminimia;
    }

    public void setCantidadminimia(Integer cantidadminimia) {
        this.cantidadminimia = cantidadminimia;
    }

    public Integer getCantidadexistencia() {
        return cantidadexistencia;
    }

    public void setCantidadexistencia(Integer cantidadexistencia) {
        this.cantidadexistencia = cantidadexistencia;
    }

    public String getMarcainsumo() {
        if(null != marcainsumo){
            return marcainsumo.toUpperCase();
        }
        return marcainsumo;
    }

    public void setMarcainsumo(String marcainsumo) {
        this.marcainsumo = marcainsumo.toUpperCase();
    }

    public String getModeloinsumo() {
        if(null != modeloinsumo){
            return modeloinsumo.toUpperCase();
        }
        return modeloinsumo;
    }

    public void setModeloinsumo(String modeloinsumo) {
        this.modeloinsumo = modeloinsumo.toUpperCase();
    }

    @XmlTransient
    public Collection<MovimientoInsumo> getMovimientoInsumoCollection() {
        return movimientoInsumoCollection;
    }

    public void setMovimientoInsumoCollection(Collection<MovimientoInsumo> movimientoInsumoCollection) {
        this.movimientoInsumoCollection = movimientoInsumoCollection;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idinsumo != null ? idinsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Insumo)) {
            return false;
        }
        Insumo other = (Insumo) object;
        if ((this.idinsumo == null && other.idinsumo != null) || (this.idinsumo != null && !this.idinsumo.equals(other.idinsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.Insumo[ idinsumo=" + idinsumo + " ]";
    }

}

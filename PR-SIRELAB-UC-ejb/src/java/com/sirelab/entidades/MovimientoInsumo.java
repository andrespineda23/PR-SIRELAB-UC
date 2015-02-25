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
@Table(name = "movimientoinsumo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimientoInsumo.findAll", query = "SELECT m FROM MovimientoInsumo m"),
    @NamedQuery(name = "MovimientoInsumo.findByIdmovimientoinsumo", query = "SELECT m FROM MovimientoInsumo m WHERE m.idmovimientoinsumo = :idmovimientoinsumo"),
    @NamedQuery(name = "MovimientoInsumo.findByTipomovimiento", query = "SELECT m FROM MovimientoInsumo m WHERE m.tipomovimiento = :tipomovimiento"),
    @NamedQuery(name = "MovimientoInsumo.findByFechamovimiento", query = "SELECT m FROM MovimientoInsumo m WHERE m.fechamovimiento = :fechamovimiento"),
    @NamedQuery(name = "MovimientoInsumo.findByCantidadmovimiento", query = "SELECT m FROM MovimientoInsumo m WHERE m.cantidadmovimiento = :cantidadmovimiento"),
    @NamedQuery(name = "MovimientoInsumo.findByCostomovimiento", query = "SELECT m FROM MovimientoInsumo m WHERE m.costomovimiento = :costomovimiento")})
public class MovimientoInsumo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmovimientoinsumo")
    private BigInteger idmovimientoinsumo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipomovimiento")
    private String tipomovimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechamovimiento")
    @Temporal(TemporalType.DATE)
    private Date fechamovimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidadmovimiento")
    private int cantidadmovimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costomovimiento")
    private long costomovimiento;
    @JoinColumn(name = "insumo", referencedColumnName = "idinsumo")
    @ManyToOne(optional = false)
    private Insumo insumo;

    public MovimientoInsumo() {
    }

    public MovimientoInsumo(BigInteger idmovimientoinsumo) {
        this.idmovimientoinsumo = idmovimientoinsumo;
    }

    public MovimientoInsumo(BigInteger idmovimientoinsumo, String tipomovimiento, Date fechamovimiento, int cantidadmovimiento, long costomovimiento) {
        this.idmovimientoinsumo = idmovimientoinsumo;
        this.tipomovimiento = tipomovimiento;
        this.fechamovimiento = fechamovimiento;
        this.cantidadmovimiento = cantidadmovimiento;
        this.costomovimiento = costomovimiento;
    }

    public BigInteger getIdmovimientoinsumo() {
        return idmovimientoinsumo;
    }

    public void setIdmovimientoinsumo(BigInteger idmovimientoinsumo) {
        this.idmovimientoinsumo = idmovimientoinsumo;
    }

    public String getTipomovimiento() {
        if(null != tipomovimiento){
            return tipomovimiento.toUpperCase();
        }
        return tipomovimiento;
    }

    public void setTipomovimiento(String tipomovimiento) {
        this.tipomovimiento = tipomovimiento.toUpperCase();
    }

    public Date getFechamovimiento() {
        return fechamovimiento;
    }

    public void setFechamovimiento(Date fechamovimiento) {
        this.fechamovimiento = fechamovimiento;
    }

    public int getCantidadmovimiento() {
        return cantidadmovimiento;
    }

    public void setCantidadmovimiento(int cantidadmovimiento) {
        this.cantidadmovimiento = cantidadmovimiento;
    }

    public long getCostomovimiento() {
        return costomovimiento;
    }

    public void setCostomovimiento(long costomovimiento) {
        this.costomovimiento = costomovimiento;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmovimientoinsumo != null ? idmovimientoinsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientoInsumo)) {
            return false;
        }
        MovimientoInsumo other = (MovimientoInsumo) object;
        if ((this.idmovimientoinsumo == null && other.idmovimientoinsumo != null) || (this.idmovimientoinsumo != null && !this.idmovimientoinsumo.equals(other.idmovimientoinsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.MovimientoInsumo[ idmovimientoinsumo=" + idmovimientoinsumo + " ]";
    }
    
}

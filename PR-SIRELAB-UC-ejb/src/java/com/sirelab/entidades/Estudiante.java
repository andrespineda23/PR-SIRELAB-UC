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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ANDRES PINEDA
 */
@Entity
@Table(name = "estudiante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e"),
    @NamedQuery(name = "Estudiante.findByIdestudiante", query = "SELECT e FROM Estudiante e WHERE e.idestudiante = :idestudiante"),
    @NamedQuery(name = "Estudiante.findBySemestreestudiante", query = "SELECT e FROM Estudiante e WHERE e.semestreestudiante = :semestreestudiante")})
public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idestudiante")
    private BigInteger idestudiante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "semestreestudiante")
    private int semestreestudiante;
    @JoinColumn(name = "planestudio", referencedColumnName = "idplanestudios")
    @ManyToOne(optional = false)
    private PlanEstudios planestudio;
    @JoinColumn(name = "persona", referencedColumnName = "idpersona")
    @ManyToOne(optional = false)
    private Persona persona;

    public Estudiante() {
    }

    public Estudiante(BigInteger idestudiante) {
        this.idestudiante = idestudiante;
    }

    public Estudiante(BigInteger idestudiante, int semestreestudiante) {
        this.idestudiante = idestudiante;
        this.semestreestudiante = semestreestudiante;
    }

    public BigInteger getIdestudiante() {
        return idestudiante;
    }

    public void setIdestudiante(BigInteger idestudiante) {
        this.idestudiante = idestudiante;
    }

    public int getSemestreestudiante() {
        return semestreestudiante;
    }

    public void setSemestreestudiante(int semestreestudiante) {
        this.semestreestudiante = semestreestudiante;
    }

    public PlanEstudios getPlanestudio() {
        return planestudio;
    }

    public void setPlanestudio(PlanEstudios planestudio) {
        this.planestudio = planestudio;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idestudiante != null ? idestudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.idestudiante == null && other.idestudiante != null) || (this.idestudiante != null && !this.idestudiante.equals(other.idestudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sirelab.entidades.Estudiante[ idestudiante=" + idestudiante + " ]";
    }
    
}

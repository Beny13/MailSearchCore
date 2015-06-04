/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author paul
 */
@Entity
@Table(name = "campaigns")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Campaign.findAll", query = "SELECT c FROM Campaign c"),
    @NamedQuery(name = "Campaign.findById", query = "SELECT c FROM Campaign c WHERE c.id = :id"),
    @NamedQuery(name = "Campaign.findByKeyword", query = "SELECT c FROM Campaign c WHERE c.keyword = :keyword"),
    @NamedQuery(name = "Campaign.findByStatus", query = "SELECT c FROM Campaign c WHERE c.status = :status"),
    @NamedQuery(name = "Campaign.findByMailObject", query = "SELECT c FROM Campaign c WHERE c.mailObject = :mailObject"),
    @NamedQuery(name = "Campaign.findByMailFileName", query = "SELECT c FROM Campaign c WHERE c.mailFileName = :mailFileName")})
public class Campaign implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "keyword")
    private String keyword;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "mail_object")
    private String mailObject;
    @Lob
    @Column(name = "mail_content")
    private String mailContent;
    @Column(name = "mail_file_name")
    private String mailFileName;
    @Lob
    @Column(name = "mail_file_content")
    private byte[] mailFileContent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campaignId")
    private Collection<Email> emailCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public Campaign() {
    }

    public Campaign(Integer id) {
        this.id = id;
    }

    public Campaign(Integer id, String keyword, String status) {
        this.id = id;
        this.keyword = keyword;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMailObject() {
        return mailObject;
    }

    public void setMailObject(String mailObject) {
        this.mailObject = mailObject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String getMailFileName() {
        return mailFileName;
    }

    public void setMailFileName(String mailFileName) {
        this.mailFileName = mailFileName;
    }

    public byte[] getMailFileContent() {
        return mailFileContent;
    }

    public void setMailFileContent(byte[] mailFileContent) {
        this.mailFileContent = mailFileContent;
    }

    @XmlTransient
    public Collection<Email> getEmailCollection() {
        return emailCollection;
    }

    public void setEmailCollection(Collection<Email> emailCollection) {
        this.emailCollection = emailCollection;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Campaign)) {
            return false;
        }
        Campaign other = (Campaign) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Campaign[ id=" + id + " ]";
    }
    
}

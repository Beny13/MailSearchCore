/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailsearchcore;

import entities.Campaign;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author paul
 */
public class CampaignManager {
    public EntityManager em;
    
    public boolean done;
    
    public CampaignManager() {
        em = Persistence.createEntityManagerFactory("MailSearchCorePU").createEntityManager();
    }
    
    public synchronized String getKeyword() {
        Campaign campaign = getCampaign();
        
        if (campaign != null)
            return campaign.getKeyword();
        else 
            return null;
    }
    
    public synchronized Campaign getCampaign() {
        List<Campaign> result = em.createNamedQuery("Campaign.findByStatus")
                                    .setParameter("status", "SCRAPPING_PENDING")
                                    .setMaxResults(1)
                                    .getResultList();
        
        if (result.size() > 0) {
            Campaign campaign = result.get(0);
            
            em.getTransaction().begin();
            campaign.setStatus("SCRAPPING_STARTED");
            em.getTransaction().commit();
            
            return campaign;
        } else {
            return null;
        }
    }
}

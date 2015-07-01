package mailsearchcore;

import entities.Campaign;
import entities.Email;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author paul
 */
public class CampaignManager {
    public EntityManager em;

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
                                    .setParameter("status", Campaign.SCRAPPING_PENDING)
                                    .setMaxResults(1)
                                    .getResultList();

        if (result.size() > 0) {
            Campaign campaign = result.get(0);

            em.getTransaction().begin();
            campaign.setStatus(Campaign.SCRAPPING_STARTED);
            em.getTransaction().commit();

            return campaign;
        } else {
            return null;
        }
    }
    
    public synchronized Campaign getMailingCampaign() {
        List<Campaign> result = em.createNamedQuery("Campaign.findByStatus")
                                    .setParameter("status", Campaign.MAILING_PENDING)
                                    .setMaxResults(1)
                                    .getResultList();

        if (result.size() > 0) {
            Campaign campaign = result.get(0);

            em.getTransaction().begin();
            campaign.setStatus(Campaign.MAILING_STARTED);
            em.getTransaction().commit();

            return campaign;
        } else {
            return null;
        }
    }

    public synchronized void insertAddressesForCampaign(Campaign campaign, ArrayList<String> addresses) {
        if (addresses.size() > 0){
            em.getTransaction().begin();
            for (String address : addresses) {
                Email newEmail = new Email();
                newEmail.setCampaign(campaign);
                newEmail.setEmail(address);
                newEmail.setSelected(true);
                em.persist(newEmail);
            }

            em.getTransaction().commit();
        }
    }

    public synchronized void noticeScrappingDone(Campaign campaign) {
        em.getTransaction().begin();
        campaign.setStatus(Campaign.SCRAPPING_DONE);
        em.getTransaction().commit();
    }
    
    public synchronized void noticeMailingDone(Campaign campaign) {
        em.getTransaction().begin();
        campaign.setStatus(Campaign.MAILING_DONE);
        em.getTransaction().commit();
    }
    
    public synchronized void declareCampaignAsFailed(Campaign campaign) {
        em.getTransaction().begin();
        campaign.setStatus(Campaign.ERROR);
        em.getTransaction().commit();
    }
}

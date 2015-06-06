package mailsearchcore;

import entities.Campaign;
import entities.Email;
import java.util.ArrayList;
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

    public synchronized void insertAddressesForCampaign(int campaingId, ArrayList<String> addresses) {
        for (String addresse : addresses) {
            em.persist(new Email(campaingId, addresse, true));
        }

        em.flush();
    }
}

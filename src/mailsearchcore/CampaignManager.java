/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailsearchcore;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paul
 */
public class CampaignManager {
    public boolean done;
    
    public synchronized String tryGetKeyword() {
        // ------------- INSERT HERE QUERY TO GET CAMPAIGN ENTITY -------------
        
        //Campaign campaign = getCampaign();
        //return campaign.keyword;
        
        
        // ------------- CLEAR THE CODE BELOW -------------
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CampaignManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "brouette";
        // ------------- END CLEAR -------------
    }
    
    public synchronized String tryGetCampaign() {
        // ------------- INSERT HERE QUERY TO GET CAMPAIGN ENTITY -------------
        
        //Campaign campaign = getCampaign();
        //return campaign;
        
        
        // ------------- CLEAR THE CODE BELOW -------------
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CampaignManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
        // ------------- END CLEAR -------------
    }
}

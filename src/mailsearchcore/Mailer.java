/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailsearchcore;

import entities.Campaign;

/**
 *
 * @author paul
 */
public class Mailer extends Thread {
    private final CampaignManager campaignManager;
    private final int threadNumber;
    
    public Mailer(CampaignManager campaignManager,int threadNumber) {
        this.campaignManager = campaignManager;
        this.threadNumber = threadNumber;
    }
    
    public int getThreadNumber() {
        return threadNumber;
    }
    
    public void run() {
        while(!campaignManager.done){
            Campaign campaign = campaignManager.getCampaign();
        }
    }
}
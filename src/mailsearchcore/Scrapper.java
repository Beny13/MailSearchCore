/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mailsearchcore;

/**
 *
 * @author paul
 */
public class Scrapper extends Thread {
    private final CampaignManager campaignManager;
    private final int threadNumber;
    
    public Scrapper(CampaignManager campaignManager,int threadNumber) {
        this.campaignManager = campaignManager;
        this.threadNumber = threadNumber;
    }
    
    public int getThreadNumber() {
        return threadNumber;
    }
    
    public void run() {
        while(!campaignManager.done){
            String keyword = campaignManager.getKeyword();
            
            if (keyword == null){
                System.out.println("Scrapper "+threadNumber+": No more keywords to process...");
            }
        }
    }
}

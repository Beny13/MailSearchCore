package mailsearchcore;

import entities.Campaign;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author paul
 */
public class Scrapper extends Thread {
    private final CampaignManager campaignManager;
    private final int threadNumber;
    private final SearchApiCaller apiCaller;
    private final WebPageParser parser;
    
    private boolean interrputFlag = false;

    public Scrapper(CampaignManager campaignManager,int threadNumber) {
        this.campaignManager = campaignManager;
        this.threadNumber = threadNumber;
        this.apiCaller = new SearchApiCaller();
        this.parser = new WebPageParser();
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public void shutdown() {
        interrputFlag = true;
        System.out.println("Scrapper "+threadNumber+" shutting down...");
    }
    
    public boolean isShuttingDown() {
        return interrputFlag;
    }
    
    @Override
    public void run() {
        while(!interrputFlag) {
            Campaign campaign = campaignManager.getCampaign();
            if (campaign == null || campaign.getKeyword() == null) {
                System.out.println("Scrapper "+threadNumber+": No more keywords to process...");
                try {
                    sleep(2000);
                } catch (InterruptedException ex) {
                    System.out.println("Scrapper "+threadNumber+": Awaken during sleep...");
                    continue;
                }
                continue;
            }
            
            String keyword = campaign.getKeyword();

            ArrayList<String> urls;
            try {
                System.out.println("Scrapper "+threadNumber+": Scrapping "+keyword);
                urls = this.apiCaller.findURLFromKeyword(keyword);
            } catch (Exception e) {
                System.out.println("Scrapper "+threadNumber+": Error during URL scrapping...");
                continue;
            }

            ArrayList<String> addresses = new ArrayList<>();
            for (String url : urls) {
                try {
                    System.out.println("Scrapper "+threadNumber+": Scrapping "+url);
                    addresses.addAll(this.parser.findAddressesFromURL(url));
                } catch (IOException | InterruptedException e) {
                    System.out.println("Scrapper "+threadNumber+": Error during address scrapping...");
                    continue;
                }
            }

            System.out.println("addresses:"+addresses);
            this.campaignManager.insertAddressesForCampaign(campaign, addresses);
            System.out.println("Scrapping done");
            this.campaignManager.noticeScrappingDone(campaign);
        }
    }
}

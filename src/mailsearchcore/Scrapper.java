package mailsearchcore;

import entities.Campaign;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paul
 */
public class Scrapper extends Thread {
    private final CampaignManager campaignManager;
    private final int threadNumber;
    private final SearchApiCaller apiCaller;
    private final WebPageParser parser;

    public Scrapper(CampaignManager campaignManager,int threadNumber) {
        this.campaignManager = campaignManager;
        this.threadNumber = threadNumber;
        this.apiCaller = new SearchApiCaller();
        this.parser = new WebPageParser();
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    @Override
    public void run() {
        while(!campaignManager.done) {
            Campaign campaign = campaignManager.getCampaign();
            if (campaign == null || campaign.getKeyword() == null) {
                System.out.println("Scrapper "+threadNumber+": No more keywords to process...");
                try {
                    this.sleep(500);
                } catch (InterruptedException ex) {
                    System.out.println("Scrapper "+threadNumber+": Awaken during sleep...");
                    continue;
                }
                continue;
            }
            String keyword = campaign.getKeyword();

            ArrayList<String> urls;
            try {
                urls = this.apiCaller.findURLFromKeyword(keyword);
            } catch (Exception e) {
                System.out.println("Scrapper "+threadNumber+": Error during URL scrapping...");
                continue;
            }

            ArrayList<String> addresses = new ArrayList<>();
            for (String url : urls) {
                try {
                    addresses.addAll(this.parser.findAddressesFromURL(url));
                } catch (IOException | InterruptedException e) {
                    System.out.println("Scrapper "+threadNumber+": Error during address scrapping...");
                    continue;
                }
            }

            this.campaignManager.insertAddressesForCampaign(campaign, addresses);
            this.campaignManager.noticeScrappingDone(campaign);
        }
    }
}

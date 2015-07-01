package mailsearchcore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MailSearchCore {
    boolean running = false;

    CampaignManager cm;

    public int scrappersNumber;
    public int mailersNumber;

    private ArrayList<Scrapper> scrappers;
    private ArrayList<Mailer> mailers;

    public MailSearchCore() {
    }

    public MailSearchCore(int scrappersNumber, int mailersNumber) {
        this.scrappersNumber = scrappersNumber;
        this.mailersNumber = mailersNumber;
    }

    public void start() {
        System.out.println("Core started with "+scrappersNumber+" scrappers and "+mailersNumber+" mailers");

        cm = new CampaignManager();

        scrappers = new ArrayList<>();
        for (int i = 0; i < scrappersNumber; i++){
            Scrapper scrapper = new Scrapper(cm, i);
            scrapper.start();
            scrappers.add(scrapper);
        }

        mailers = new ArrayList<>();
        for (int i = 0; i < mailersNumber; i++){
            Mailer mailer = new Mailer(cm, i);
            mailer.start();
            mailers.add(mailer);
        }

        running = true;
    }

    public void stop() {
        System.out.println("Core stopping...");
        
        while (scrappers.size() > 0){
            if (!scrappers.get(0).isShuttingDown())
                scrappers.get(0).shutdown();
            
            if (!scrappers.get(0).isAlive()){
                System.out.println("Scrapper "+scrappers.get(0).getThreadNumber()+" ended");
                scrappers.remove(0);
            }
        }

        while (mailers.size() > 0){
            if (!mailers.get(0).isShuttingDown())
                mailers.get(0).shutdown();
            if (!mailers.get(0).isAlive()){
                System.out.println("Mailer "+mailers.get(0).getThreadNumber()+" ended");
                mailers.remove(0);
            }
        }

        running = false;
        System.out.println("Core stopped");
    }

    public static void banner() {
        BufferedReader br = null;

        try {
            String sCurrentLine;
            br = new BufferedReader(new InputStreamReader(MailSearchCore.class.getResourceAsStream("/resources/banner")));

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        banner();

        MailSearchCore core = new MailSearchCore(2,2);

        String input = "";

        while (!input.equals("exit")){
            System.out.print("#: ");
            Scanner in = new Scanner(System.in);
            input = in.nextLine();

            switch (input) {
                case "start":
                    core.start();
                    break;
                case "stop":
                    core.stop();
                    break;
            }
        }

        if (core.running){
            core.stop();
        }
    }
}

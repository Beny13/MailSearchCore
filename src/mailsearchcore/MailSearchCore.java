package mailsearchcore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MailSearchCore {
    CampaignManager cm;
    
    public int mailersNumber = 2;
    public int scrappersNumber = 2;
    
    private ArrayList<Mailer> mailers;
    
    public MailSearchCore() {
    }
    
    public MailSearchCore(int mailersNumber, int scrappersNumber) {
        this.mailersNumber = mailersNumber;
        this.scrappersNumber = scrappersNumber;
    }
    
    public void start() {
        System.out.println("Core started with "+mailersNumber+" mailers and "+scrappersNumber+" scrappers");
        
        cm = new CampaignManager();
        
        mailers = new ArrayList<>();
        
        for (int i = 0; i < mailersNumber; i++){
            Mailer mailer = new Mailer(cm, i);
            mailer.start();
            mailers.add(mailer);
        }
    }
    
    public void stop() {
        System.out.println("Core stopping...");
        cm.done = true;
        
        while (mailers.size() > 0){
            if (!mailers.get(0).isAlive()){
                System.out.println("Mailer "+mailers.get(0).getThreadNumber()+" ended");
                mailers.remove(0);
            }
        }
        
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
        
        MailSearchCore core = new MailSearchCore(4,4);
        
        String input = "";
        
        while (!input.equals("exit")){
            System.out.print("#: ");
            Scanner in = new Scanner(System.in);
            input = in.nextLine();
            
            if (input.equals("start")){
                core.start();
            } else if (input.equals("stop")){
                core.stop();
            }
        }
    }
}

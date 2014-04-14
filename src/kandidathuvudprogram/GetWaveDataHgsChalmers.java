/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kandidathuvudprogram;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.joda.time.LocalDate;

/**
 *
 * @author Markus, David
 *
 */
public class GetWaveDataHgsChalmers {
    
    public static void main(String[] arg){
        downloadWaveData("2014-01-05","2014-01-06", false);
    }
    
    /**
     * Klipper ut och laddar ner vågdata från startdatum(ex: 2012-10-15) till slutdatum. Sparas: datum_tid.tsv ex: 110514_06.tsv
     * @param startDate
     * @param endDate 
     * @param cut Om man ska klippa bort koordinaterna i utdatan. 
     
     */
    public static void downloadWaveData(String startDate, String endDate, boolean cut){
        
        String[] dateArray = generateDateString(startDate, endDate);
        String[] dateHrArray = new String[dateArray.length*4];
        String[] hrs = new String[]{"00","06","12","18"};
        for (int n=0; n<dateArray.length; n++){                // skapar en ny String array med datum och klockslag
            for (int m=0; m<4; m++){
                dateHrArray[n*4+m] = dateArray[n]+ "_" + hrs[m];
            }
        }
 
        String exec;      
        String dataCo = new String("-70/15/35/75"); // Koordinaterna som plockas ut. 
        String fixPath = new String("export PATH=.:$HOME/bin:/home/hgs/bin:/usr/local/GMT/bin:$PATH "
                + "\nexport GMTHOME=/usr/local/GMT "); // Så bash hittar GMT
        String filePath = new String("/home/hgs/ECMWF/WAVEH");
        String cutCo = new String(" ");
        if(cut){
            cutCo = " | cut -f 3 ";
        }
  
        ChannelSftp channelSftp = null;
        Channel channel = null;
        Session sesh = null;
        
        try {
            String user = "karb", host = "holt.oso.chalmers.se";
            JSch jsch = new JSch();
            sesh = jsch.getSession(user, host, 22);     
            sesh.setPassword(JOptionPane.showInputDialog("Enter password"));          
            sesh.setConfig("StrictHostKeyChecking", "no");
            sesh.connect();
            channel = sesh.openChannel("sftp");      
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            ChannelExec channelExec;
     
            System.out.println(dateHrArray.length);
            // Laddar ned alla datafiler för aktuella datum
            for (int i=0; i<dateHrArray.length; i++){
                System.out.println(dateHrArray[i]);
                
                //Open shell channel
                channelExec = (ChannelExec) sesh.openChannel("exec");
                

                exec = fixPath +" \n" + "if [ -e " + filePath + "/GRD/wvh_20" + dateHrArray[i]
                    + ".grd ]; then " + "grd2xyz " + filePath + "/GRD/wvh_20" + dateHrArray[i]
                    + ".grd -N-1 -R" + dataCo + cutCo + " >  " + filePath + "/kandData/raw_temp.tsv ; fi";               
                System.out.println(exec);
                channelExec.setCommand(exec);
                //channelExec.setOutputStream(System.out);
                 channelExec.connect();
                    
                    
                try { // Väntar på att kommando skall köras på servern
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GetWaveDataHgsChalmers.class.getName()).log(Level.SEVERE, null, ex);
                
                }
                
                try {
                    System.out.println("3");
                    channelSftp.get( filePath + "/kandData/raw_temp.tsv", "wavedata/20" + dateHrArray[i] + ".tsv" );
                    System.out.println("4");
                }catch(SftpException e){
                    System.out.println(e.getMessage() + " Totally failed to get: " + dateHrArray[i]);
                }
                
                channelExec.disconnect();
            }
            
            // kommando som skall köras:
            // tslist /home/hgs/TD/d/G1_garb_111111-1s.mc -L'G|B' -Jf14.8,f12.4 -D -qqq -w /home/hgs/bin/kandidatArbetsMappen/gravidata/111111.tsf
            // Jordbäver:
            // tslist-app -qq -C3 -L'G|B' -w suspect.list ++ -Eeq.tse,E + /home/hgs/TD/d/G1_garb_13090[1-8]-1s.mc

            
        } catch (JSchException ex) {
            Logger.getLogger(GetWaveDataHgsChalmers.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (channelSftp.isConnected()) {
                try {
                    sesh.disconnect();
                    channel.disconnect();
                    channelSftp.quit();
                } catch (Exception ioe) {
                }
            }
        }
    } 
    
    // Hämtar en lista av datum mellan det valda start- och slutdatum
    public static String[] generateDateString(String start, String end){
        String[] dates;
        int count = 0;       
        
        LocalDate dateStart = new LocalDate(start);
        LocalDate dateEnd = new LocalDate(end);

        // Räknar antal dagar
        while(dateStart.compareTo(dateEnd)!=1){
            count++;
            dateStart = dateStart.plusDays(1);
        }
        dates = new String[count];
        count = 0;
        
        // Samma loop fast sätter värde i "dates" fältet
        dateStart = new LocalDate(start);
        while(dateStart.compareTo(dateEnd)!=1){     
            dates[count] = dateStart.toString().substring(2).replaceAll("-", "");
            count++;
            dateStart = dateStart.plusDays(1);
        }
   
        return dates;
    }
    public static void shell(){
        try{
            JSch jsch=new JSch();

            //jsch.setKnownHosts("/home/foo/.ssh/known_hosts");

            String host=null;
            host=JOptionPane.showInputDialog("Enter username@hostname",
                                               System.getProperty("user.name")+
                                               "@localhost"); 
            String user=host.substring(0, host.indexOf('@'));
            host=host.substring(host.indexOf('@')+1);

            Session session=jsch.getSession(user, host, 22);

            String passwd = JOptionPane.showInputDialog("Enter password");
            session.setPassword(passwd);

            UserInfo ui = new MyUserInfo(){
              public void showMessage(String message){
                JOptionPane.showMessageDialog(null, message);
              }
              public boolean promptYesNo(String message){
                Object[] options={ "yes", "no" };
                int foo=JOptionPane.showOptionDialog(null, 
                                                     message,
                                                     "Warning", 
                                                     JOptionPane.DEFAULT_OPTION, 
                                                     JOptionPane.WARNING_MESSAGE,
                                                     null, options, options[0]);
                return foo==0;
              }

              // If password is not given before the invocation of Session#connect(),
              // implement also following methods,
              //   * UserInfo#getPassword(),
              //   * UserInfo#promptPassword(String message) and
              //   * UIKeyboardInteractive#promptKeyboardInteractive()

            };

            session.setUserInfo(ui);

            // It must not be recommended, but if you want to skip host-key check,
            // invoke following,
            // session.setConfig("StrictHostKeyChecking", "no");

            //session.connect();
            session.connect(30000);   // making a connection with timeout.

            Channel channel=session.openChannel("shell");

            // Enable agent-forwarding.
            //((ChannelShell)channel).setAgentForwarding(true);

            channel.setInputStream(System.in);
            /*
            // a hack for MS-DOS prompt on Windows.
            channel.setInputStream(new FilterInputStream(System.in){
                public int read(byte[] b, int off, int len)throws IOException{
                  return in.read(b, off, (len>1024?1024:len));
                }
              });
             */

            channel.setOutputStream(System.out);

            /*
            // Choose the pty-type "vt102".
            ((ChannelShell)channel).setPtyType("vt102");
            */

            /*
            // Set environment variable "LANG" as "ja_JP.eucJP".
            ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
            */

            //channel.connect();
            channel.connect(3*1000);
      }
      catch(Exception e){
        System.out.println(e);
      }
  }
 
    public static abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive{
    public String getPassword(){ return null; }
    public boolean promptYesNo(String str){ return false; }
    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){ return false; }
    public boolean promptPassword(String message){ return false; }
    public void showMessage(String message){ }
    public String[] promptKeyboardInteractive(String destination,
                                              String name,
                                              String instruction,
                                              String[] prompt,
                                              boolean[] echo){
      return null;
    }
  }
}

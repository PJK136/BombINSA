package game;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio implements GameListener
{
     private Clip bombe, bonus, coup, suddenDeath;
  
    public Audio()
    {
        try {
            bombe = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
            Audio.class.getResourceAsStream("/audio/Explosion.wav"));
            bombe.open(inputStream); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          } 
        try {
            bonus = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              Audio.class.getResourceAsStream("/audio/Bonus.wav"));
            bonus.open(inputStream); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
        try {
            coup = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              Audio.class.getResourceAsStream("/audio/Hit.wav"));
            coup.open(inputStream); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
        try {
            suddenDeath = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
              Audio.class.getResourceAsStream("/audio/SuddenDeath.wav"));
            suddenDeath.open(inputStream); 
          } catch (Exception e) {
            System.err.println(e.getMessage());
          }
    }
     
    public void playExplosion(){
        bombe.setFramePosition(0);
        bombe.start();
        }
    
    public void playBonus(){
        bonus.setFramePosition(0);
        bonus.start();
        }
    
    public void playHit(){
        coup.setFramePosition(0);
        coup.start();
        }
    
    public void playSuddenDeath(){
        suddenDeath.setFramePosition(0);
        suddenDeath.start();
    }

    @Override
    public void processEvent(Event e) {
        // TODO Auto-generated method stub
        int i = e.ordinal();
        switch(i){
        case 0 :
            playHit();
            break;
        case 1 :
            playExplosion();
            break;
        case 2 :
            playBonus();
            break;
        case 3 :
            playSuddenDeath();
        }
        
    }

}
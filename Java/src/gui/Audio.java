package gui;

import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import game.Event;
import game.GameListener;

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
    
    void stop() {
        bombe.stop();
        bonus.stop();
        coup.stop();
        suddenDeath.stop();
    }
    
    void play(Clip clip) {
        if (clip.isRunning()) {
            clip.stop();
            clip.flush();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void processEvent(Event e) {
        switch(e){
        case Hit:
            play(coup);
            break;
        case Explosion:
            play(bombe);
            break;
        case PickUp:
            play(bonus);
            break;
        case SuddenDeath:
            play(suddenDeath);
            break;
        }
        
    }

}
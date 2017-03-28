package gui;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Audio extends Thread
{
    private URL bombeURL ;
    private AudioClip bombe;
  
    public Audio()
    {
        bombeURL = this.getClass().getClassLoader().getResource("/audio/Explosion.wav");
        bombe = Applet.newAudioClip(bombeURL);
    }
     
    public void jouerBombe()
    {
        bombe.play();
    }
     
    public void jouerBombeEnBoucle()
    {
        bombe.loop();
    }
     
    public void arreterBombe()
    {
        bombe.stop();
    }
}
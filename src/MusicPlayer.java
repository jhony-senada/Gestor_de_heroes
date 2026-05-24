import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicPlayer {
    private Clip clip;

    public void playBackgroundMusic(String filePath) {
        try {
            // ¡NUEVO! Detener la música anterior antes de cargar una nueva
            stopMusic();

            File musicPath = new File(filePath);
            
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                
                clip.loop(Clip.LOOP_CONTINUOUSLY); 
                clip.start();
            } else {
                System.out.println("No se encontró el archivo de música: " + filePath);
            }
        } catch (Exception ex) {
            System.out.println("Error al reproducir el audio.");
            ex.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close(); // Liberamos el recurso para que no se sature la memoria
        }
    }
}
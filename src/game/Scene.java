
package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 *
 * @author Rostiku
 */
public class Scene {
    // the image of the scene
    private BufferedImage image;
    // image name of the scene
    private String imageName;
    // direction of the scene
    private String direction;
    // next location of the scene (if there is one)
    private String nextLocation;
    // next direction of the scene (if there is one)
    private String nextDirection;
    // if front is blocked
    private boolean frontBlocked;
    
    /**
     * Stores the each scene from the text file
     * @param input the point at where the scanner is in the file
     */
    public Scene(Scanner input){
        // direction of the scene
        this.direction = input.next();
        // image name of the scene
        this.imageName = input.next();
        
        // if front is clear
        if(input.next().equals("false")){
            this.frontBlocked = false;
            // read the next location and direction
            this.nextLocation = input.next();
            this.nextDirection = input.next();
            
            // skip to the next line
        } else {
            this.frontBlocked = true;
            input.nextLine();
        }
        
        // try assigning the image of the scene to a variable
        try{
            this.image = ImageIO.read(new File("src/images/" + imageName));
        } catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    // GETTERS AND SETTERS
    
    public BufferedImage getImage() {
        return image;
    }

    public void loadImage(String imageName){
        try{
            this.image = ImageIO.read(new File("src/images/" + imageName));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getImageName() {
        return imageName;
    }

    public String getDirection() {
        return direction;
    }

    public String getNextLocation() {
        return nextLocation;
    }

    public String getNextDirection() {
        return nextDirection;
    }

    public boolean isFrontBlocked() {
        return frontBlocked;
    }
}

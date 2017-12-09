package game;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Rostiku
 */
public class Location {
    // array list with 4 scenes for location
    private ArrayList<Scene> scenes;
    // name of the location
    private String name;
    // array for each direction
    private String[] directions;
    
    /**
     * Location that contains 4 scenes
     * @param input scanner from the file
     */
    public Location(Scanner input){
        // initializes directions
        directions = new String[]{"N", "E", "S", "W"};
        // first word in the scanner - name of the location
        name = input.next();
        // initializes scenes arraylist
        scenes = new ArrayList<>();
        
        // stores next 4 lines as scenes
        for (int i = 0; i < 4; i++) {
            Scene s = new Scene(input);
            System.out.println(s.getImageName());
            scenes.add(s);
        }
    }

    // GETTERS
    
    public Scene getScenes(String direction) {
        for(Scene s : scenes){
            if(s.getDirection().equals(direction)){
                return s;
            }
        }
        
        return null;
    }

    public String getName() {
        return name;
    }
    
    
    
}

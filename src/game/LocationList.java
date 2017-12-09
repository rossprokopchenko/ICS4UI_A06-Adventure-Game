package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Rostiku
 */
public class LocationList {
    // arraylist with all locations taken from the text file
    private ArrayList<Location> locations;
    // start location and direction
    private String startLocation;
    private String startDirection;
    
    /**
     * Links all location into 1 arraylist
     * @param file 
     */
    public LocationList(String file){
        // initializes the arraylist
        locations = new ArrayList<>();
        
        // try reading the file
        try{
            Scanner input = new Scanner(new File(file));
            
            // read first 2 lines
            // start location and start direction
            startLocation = input.next();
            startDirection = input.next();
            
            // until the last line
            while(input.hasNext()){
                // make a location
                Location l = new Location(input);
                // add the location to the arraylist
                locations.add(l);
            }
            
            // if did not find file - print errors
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
    }
    
    // GETTERS
    
    public Scene getLocation(String location, String direction){
        for(Location l : locations){
            if(l.getName().equals(location)){
                return l.getScenes(direction);
            }
        }
        
        return null;
    }
    
    public String getStartLocation(){
        return startLocation;
    }
    
    public String getStartDirection(){
        return startDirection;
    }
}

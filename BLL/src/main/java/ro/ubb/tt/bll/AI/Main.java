package ro.ubb.tt.bll.AI;

import ro.ubb.tt.model.Location;
import ro.ubb.tt.model.User;
import ro.ubb.tt.model.enums.Type;

import java.util.*;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args){
        System.out.println("Start! ");

        List<Location> locations = new ArrayList<>();
        List<Location> userLocations = new ArrayList<>();
        User us = new User();
        User uss = new User();
        us.setId(1);
        uss.setId(2);

        locations.add(new Location("","A", Type.Sea,10f,us));
        locations.add(new Location("","A", Type.City,10f,us));
        locations.add(new Location("","B", Type.Mountain,9f,us));
        locations.add(new Location("","C", Type.City,8f,us));
        locations.add(new Location("","D", Type.Mountain,7f,us));
        locations.add(new Location("","E", Type.Sea,6f,us));
        locations.add(new Location("","F", Type.Mountain,5f,us));
        locations.add(new Location("","G", Type.Sea,4f,us));
        locations.add(new Location("","H", Type.Mountain,3f,us));
        locations.add(new Location("","I", Type.Sea,2f,us));
        locations.add(new Location("","J", Type.City,1f,us));
        locations.add(new Location("","K", Type.Mountain,10f,us));
        locations.add(new Location("","L", Type.Mountain,9f,us));
        locations.add(new Location("","M", Type.Sea,7f,us));
        locations.add(new Location("","N", Type.City,4f,us));
        locations.add(new Location("","O", Type.Mountain,2f,us));
        locations.add(new Location("","P", Type.Sea,10f,us));
        locations.add(new Location("","Q", Type.Mountain,9f,us));
        locations.add(new Location("","R", Type.Mountain,5f,us));
        locations.add(new Location("","S", Type.Sea,6f,us));
        locations.add(new Location("","T", Type.Mountain,6f,us));
        locations.add(new Location("","U", Type.City,1f,us));
        locations.add(new Location("","V", Type.Mountain,2f,us));
        locations.add(new Location("","AAA", Type.Sea,9f,uss));

        userLocations.add(new Location("","AAA", Type.Sea,9f,uss));


        List<Location> locationTop10 = KMeans.getTop10(locations, userLocations, 2);

        System.out.println("Top10!");
        for(Location loc : locationTop10)
            System.out.println(loc);

        System.out.println("Finish!");
    }
}

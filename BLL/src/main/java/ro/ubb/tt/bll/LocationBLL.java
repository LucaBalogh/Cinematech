package ro.ubb.tt.bll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.tt.bll.AI.KMeans;
import ro.ubb.tt.bll.exceptions.InternalServerException;
import ro.ubb.tt.dal.LocationsRepository;
import ro.ubb.tt.model.Location;

import java.util.List;

@Component
public class LocationBLL {

    private LocationsRepository locationsRepository;

    @Autowired
    public void setLocationsRepository(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    public List<Location> getAllLocationsForAUser(int userId){
        return locationsRepository.findAllByUserId(userId);
    }

    public List<Location> getAllLocations(){return  locationsRepository.findAll();}

    public List<Location> getBestLocations(int user_id){
        return KMeans.getTop10(locationsRepository.findAll(), locationsRepository.findAllByUserId(user_id), user_id);
    }

    @Transactional
    public Location addLocation(Location loc){
        Location l = loc;
        l = locationsRepository.save(l);
        loc.setId(l.getId());
        return loc;
    }

    @Transactional
    public Location updateLocation(Location loc) throws InternalServerException {
        locationsRepository.update(loc.getId(), loc.getCity(), loc.getCountry(), loc.getTip(), loc.getRating(), loc.getUser());
        return locationsRepository.findById(loc.getId()).orElseThrow(() -> new InternalServerException("Update went wrong"));
    }

    @Transactional
    public void deleteLocation(int id){
        locationsRepository.deleteById(id);
    }
}

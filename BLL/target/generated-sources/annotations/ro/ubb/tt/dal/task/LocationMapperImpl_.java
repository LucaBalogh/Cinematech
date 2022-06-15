package ro.ubb.tt.dal.task;

import javax.annotation.processing.Generated;

import ro.ubb.tt.bll.task.TaskMapper;
import ro.ubb.tt.model.User;
import ro.ubb.tt.model.dtos.LocationDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-11-26T16:35:00+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
public class LocationMapperImpl_ implements TaskMapper {

    @Override
    public LocationDTO taskToTaskDTO(Task task) {
        if ( task == null ) {
            return null;
        }

        LocationDTO locationDTO = new LocationDTO();

        locationDTO.setAssignedToId( taskAssignedToId( task ) );
        locationDTO.setCreatedById( taskCreatedById( task ) );
        locationDTO.setUserStoryId( taskUserStoryId( task ) );
        locationDTO.setId( task.getId() );
        locationDTO.setTitle( task.getTitle() );
        locationDTO.setDescription( task.getDescription() );
        locationDTO.setCreated( task.getCreated() );

        return locationDTO;
    }

    @Override
    public Task taskDTOToTask(LocationDTO locationDTO) {
        if ( locationDTO == null ) {
            return null;
        }

        Task task = new Task();

        if ( locationDTO.getId() != null ) {
            task.setId( locationDTO.getId() );
        }
        task.setTitle( locationDTO.getTitle() );
        task.setDescription( locationDTO.getDescription() );
        task.setCreated( locationDTO.getCreated() );

        return task;
    }

    private Integer taskAssignedToId(Task task) {
        if ( task == null ) {
            return null;
        }
        User assignedTo = task.getAssignedTo();
        if ( assignedTo == null ) {
            return null;
        }
        int id = assignedTo.getId();
        return id;
    }

    private Integer taskCreatedById(Task task) {
        if ( task == null ) {
            return null;
        }
        User createdBy = task.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        int id = createdBy.getId();
        return id;
    }

    private Integer taskUserStoryId(Task task) {
        if ( task == null ) {
            return null;
        }
        UserStory userStory = task.getUserStory();
        if ( userStory == null ) {
            return null;
        }
        int id = userStory.getId();
        return id;
    }
}

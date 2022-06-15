package ro.ubb.tt.bll.task;

import javax.annotation.processing.Generated;

import ro.ubb.tt.model.dtos.LocationDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-11-26T16:41:41+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
public class LocationMapperImpl extends TaskMapperDecorator implements TaskMapper {

    private final TaskMapper delegate;

    public LocationMapperImpl() {
        this( new LocationMapperImpl_() );
    }

    private LocationMapperImpl(LocationMapperImpl_ delegate) {
        this.delegate = delegate;
    }

    @Override
    public LocationDTO taskToTaskDTO(Task task)  {
        return delegate.taskToTaskDTO( task );
    }

    @Override
    public Task taskDTOToTask(LocationDTO locationDTO)  {
        return delegate.taskDTOToTask(locationDTO);
    }
}

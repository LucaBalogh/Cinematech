package ro.ubb.tt.bll.user;

import javax.annotation.processing.Generated;
import ro.ubb.tt.model.User;
import ro.ubb.tt.model.dtos.UserDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-11-26T16:41:41+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
public class UserMapperImpl extends UserMapperDecorator implements UserMapper {

    private final UserMapper delegate;

    public UserMapperImpl() {
        this( new UserMapperImpl_() );
    }

    private UserMapperImpl(UserMapperImpl_ delegate) {
        super( delegate );
        this.delegate = delegate;
    }

    @Override
    public UserDTO userToUserDTO(User user)  {
        return delegate.userToUserDTO( user );
    }
}

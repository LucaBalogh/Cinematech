package ro.ubb.tt.dal.user;

import javax.annotation.processing.Generated;

import ro.ubb.tt.bll.user.UserMapper;
import ro.ubb.tt.model.Location;
import ro.ubb.tt.model.User;
import ro.ubb.tt.model.dtos.UserDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-11-26T16:35:01+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 14.0.2 (Oracle Corporation)"
)
public class UserMapperImpl_ implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setRoleId( userRoleId( user ) );
        userDTO.setRoleTitle( userRoleTitle( user ) );
        userDTO.setId( user.getId() );
        userDTO.setFirstName( user.getFirstName() );
        userDTO.setLastName( user.getLastName() );
        userDTO.setEmail( user.getEmail() );
        userDTO.setPassword( user.getPassword() );

        return userDTO;
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        if ( userDTO.getId() != null ) {
            user.setId( userDTO.getId() );
        }
        user.setLastName( userDTO.getLastName() );
        user.setFirstName( userDTO.getFirstName() );
        user.setEmail( userDTO.getEmail() );
        user.setPassword( userDTO.getPassword() );

        return user;
    }

    private Integer userRoleId(User user) {
        if ( user == null ) {
            return null;
        }
        Location location = user.getRole();
        if ( location == null ) {
            return null;
        }
        int id = location.getId();
        return id;
    }

    private String userRoleTitle(User user) {
        if ( user == null ) {
            return null;
        }
        Location location = user.getRole();
        if ( location == null ) {
            return null;
        }
        String title = location.getTitle();
        if ( title == null ) {
            return null;
        }
        return title;
    }
}

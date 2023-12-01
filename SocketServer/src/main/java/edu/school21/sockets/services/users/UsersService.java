package edu.school21.sockets.services.users;

import edu.school21.sockets.models.User;

public interface UsersService {
    boolean signUp(User user);
     boolean authenticate(User user);
     User getByName(String name);
    User getById(long id);


}


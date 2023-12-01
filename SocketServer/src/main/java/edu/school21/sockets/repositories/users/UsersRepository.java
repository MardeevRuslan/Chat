package edu.school21.sockets.repositories.users;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User> {
    Optional<User> findByName(String name);
}



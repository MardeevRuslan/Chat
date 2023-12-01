package edu.school21.sockets.services.users;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.users.UsersRepository;
import edu.school21.sockets.services.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;


    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean signUp(User user) {
        return usersRepository.save(user);
    }

    @Override
    public boolean authenticate(User user) {
        Optional<User> byName = usersRepository.findByName(user.getName());
        if (!byName.isPresent()) {
            return false;
        }
        String hashedPassword = byName.get().getPassword();
        String password = user.getPassword();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isMatch = passwordEncoder.matches(password, hashedPassword);
        return isMatch;
    }

    @Override
    public User getByName(String name) {
        return (User) usersRepository.findByName(name).get();
    }

    @Override
    public User getById(long id) {
        return null;
    }


}


package pl.sda.register.repository;

import org.springframework.stereotype.Repository;
import pl.sda.register.exception.DuplicatedUsernameException;
import pl.sda.register.exception.UserNotFoundException;
import pl.sda.register.model.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    private Set<User> users = initializeUsers();

    private Set<User> initializeUsers() {
        return new HashSet<>(Arrays.asList(new User("login", "Captain", "Jack")));
    }

    public Set<String> findAllUserNames(String firstName, boolean matchExact) {
        return Optional.ofNullable(firstName)
                .map(name -> users.stream()
                        .filter(firstNamePredicate(firstName, matchExact))
                        .map(User::getUsername)
                        .collect(Collectors.toSet()))
                .orElse(users.stream()
                        .map(User::getUsername)
                        .collect(Collectors.toSet()));
    }

    private Predicate<User> firstNamePredicate(String firstName, boolean matchExact) {
        return matchExact ?
                user -> user.getFirstName().equals(firstName) :
                user -> user.getFirstName().contains(firstName);
    }

    public User findUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny()
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found"));
    }

    public void addUser(User user) {
        users.stream()
                .filter(existingUser -> existingUser.getUsername().equals(user.getUsername()))
                .findAny()
                .ifPresent(u -> throwDuplicatedUserException(u.getUsername()));
        users.add(user);
    }

    public void removeUser(String username) {
        users.stream()
                .filter(user -> user.getUsername().equals(username))
                .forEach(user -> users.remove(user));
    }

    public void updateUser(User user) {
        User userToUpdate = findUserByUsername(user.getUsername());
        users.remove(userToUpdate);
        users.add(user);
    }

    private void throwDuplicatedUserException(String username) {
        throw new DuplicatedUsernameException("User " + username + " already exists.");
    }

    private void throwUserNotFoundException(String username) {
        throw new UserNotFoundException("User " + username + " not found.");
    }
}

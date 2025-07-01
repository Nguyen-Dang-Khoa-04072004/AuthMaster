package fpt.backend.MasterAuth.user;

import fpt.backend.MasterAuth.requests.AddUserRequest;
import fpt.backend.MasterAuth.requests.UpdateUserRequest;


public interface UserService {
    User login(String email, String password);
    User getUser(Long userId);
    User createUser(AddUserRequest request);
    void addRole(Long userId, Role role);
    void updateUser(Long userId, UpdateUserRequest request);
    void deleteUser(Long userId);
}

package tlp.client.service;

import tlp.client.model.UserModel;

public interface UserService
{
        public Boolean existsUsername(UserModel.Username username);

        public UserModel.Id getUidOf(UserModel.Username username);

        public Boolean authenticate(UserModel.Username username, UserModel.Password password);

        public UserService newUser(UserModel model);
}

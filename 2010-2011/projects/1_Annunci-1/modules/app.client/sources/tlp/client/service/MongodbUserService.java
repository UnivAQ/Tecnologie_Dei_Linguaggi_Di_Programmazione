package tlp.client.service;

import com.mongodb.DBCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tlp.client.dao.UsersDao;
import tlp.client.model.UserModel;

@Service
@Backend(BackendType.MongoDB)
public class MongodbUserService implements UserService
{
        private UsersDao _dao;

        @Autowired
        public MongodbUserService(UsersDao dao)
        {
                this._dao = dao;
        }

        @Override
        public Boolean existsUsername(UserModel.Username username)
        {
                return this._dao.exists(username);
        }

        @Override
        public UserModel.Id getUidOf(UserModel.Username username)
        {
                return new UserModel.Id(
                        this._dao.getBy(username).next().get("_id").toString()
                );
        }

        @Override
        public Boolean authenticate(UserModel.Username username, UserModel.Password password)
        {
                DBCursor result = this._dao.getBy(username);

                if (result.count() == 1) {
                        if (result.next().get(password.key()).equals(password.val())) {
                                return true;
                        }
                }

                return false;
        }

        @Override
        public MongodbUserService newUser(UserModel model)
        {
                //model.set(new UserModel.Uid<String>(
                //        UUID.randomUUID().toString()
                //));

                this._dao.newOne(model).save();

                return this;
        }
}

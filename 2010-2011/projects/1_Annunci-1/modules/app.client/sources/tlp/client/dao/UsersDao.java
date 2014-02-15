package tlp.client.dao;

import org.springframework.stereotype.Repository;
import tlp.client.model.Model;
import tlp.client.model.UserModel;

@Repository
public class UsersDao extends MongodbDao
{
        @Override
        public String getCollectionName() { return "users"; }

        @Override
        public Class<? extends Model> getModelClass() { return UserModel.class; }
}

package tlp.client.dao;

import org.springframework.stereotype.Repository;
import tlp.client.model.AdModel;
import tlp.client.model.Field;
import tlp.client.model.Model;

@Repository
public class AdsDao extends MongodbDao
{
        @Override
        public String getCollectionName() { return "ads"; }

        @Override
        public Class<? extends Model> getModelClass() { return AdModel.class; }
}

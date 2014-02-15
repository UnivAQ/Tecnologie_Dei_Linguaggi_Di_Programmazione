package tlp.client.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tlp.client.dao.AdsDao;
import tlp.client.model.AdModel;
import tlp.client.model.AdModel.TypeType;
import tlp.client.model.UserModel;

@Service
@Backend(BackendType.MongoDB)
public class MongodbAdService implements AdService
{
        private AdsDao _dao;

        @Autowired
        public MongodbAdService(AdsDao dao)
        {
                this._dao = dao;
        }

        @Override
        public MongodbAdService save(AdModel model)
        {
                this._dao.newOne(model).save();

                return this;
        }

        @Override
        public AdService remove(AdModel.Id adId)
        {
                this._dao.remove(adId);

                return this;
        }

        @Override
        public AdModel one(AdModel.Id adId)
        {
                DBObject record = this._dao.getBy(adId).next();

                AdModel model = new AdModel();
                for (String key: record.keySet()) {
                        this._map(model, key, record.get(key));
                }

                return model;
        }

        @Override
        public List<AdModel> all(UserModel.Id userId)
        {
                ArrayList<AdModel> list = new ArrayList<AdModel>();

                DBCursor records = this._dao.getBy(new AdModel.Uid(userId.val()));

                AdModel m;
                for (DBObject record: records) {
                        m = new AdModel();
                        for (String key: record.keySet()) {
                                this._map(m, key, record.get(key));
                        }
                        list.add(m);
                }

                return list;
        }

        @Override
        public List<AdModel> match(AdModel.Type type, AdModel.Tags tags)
        {
                ArrayList<AdModel> list = new ArrayList<AdModel>();

                BasicDBObject query = new BasicDBObject();

                TypeType altType = type.val().equals(TypeType.SALE) ?
                                TypeType.WANTED
                        :       TypeType.SALE
                ;

                query.put(type.key(), altType.toString());
                query.put(new AdModel.Tags().key(), new BasicDBObject("$in", tags.val()));

                DBCursor records = this._dao.find(query);

                AdModel m;
                for (DBObject record: records) {
                        m = new AdModel();

                        for (String key: record.keySet()) {
                                this._map(m, key, record.get(key));
                        }

                        // We want only the ads not yet expired.
                        if (m.get(new AdModel.Expiry()).after(Calendar.getInstance().getTime())) {
                                list.add(m);
                        }
                }

                return list;
        }

        protected MongodbAdService _map(AdModel model, String key, Object val)
        {
                if (key.equals("_id")) {
                        model.set(new AdModel.Id(val.toString()));

                        return this;
                }

                AdModel.Type type = new AdModel.Type();
                if (key.equals(type.key())) {
                        model.set((AdModel.Type) type.set(
                                TypeType.valueOf(((String) val).toUpperCase())
                        ));

                        return this;
                }

                model.set(key, val);

                return this;
        }
}

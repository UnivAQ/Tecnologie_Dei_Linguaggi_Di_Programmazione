package tlp.client.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import tlp.client.model.AbstractModel;
import tlp.client.model.Field;
import tlp.client.model.Model;

public abstract class MongodbDao
{
        private DB _db;
        private DBCollection _col;

        protected HashMap<String, Object> _serializeData(HashMap<String, Object> data)
        {
                HashMap<String, Object> serializedData = new HashMap<String, Object>();

                String key;
                Object val;
                for (Map.Entry<String, Object> e: data.entrySet()) {
                        key = e.getKey();
                        val = e.getValue();

                        if (key.equals("id")) {
                                key = "_id";
                                val = new ObjectId((String) val);
                        }

                        if (val instanceof java.lang.Enum) {
                               val = val.toString();
                        }

                        serializedData.put(key, val);
                }

                return serializedData;
        }

        @Autowired
        public MongodbDao injectDb(DB db)
        {
                this._db = db;

                return this;
        }

        public DBCollection collection()
        {
                if (this._col == null) {
                        this._col = this._db.getCollection(this.getCollectionName());
                }

                return this._col;
        }

        public MongodbDao save(DBObject obj)
        {
                this.collection().save(obj);

                return this;
        }

        public MongodbDao save(Model model)
        {
                BasicDBObject obj = new BasicDBObject();
                obj.putAll(this._serializeData(model.data()));
                this.save(obj);

                model.set(new AbstractModel.Id(obj.get("_id").toString()));

                return this;
        }

        public MongodbDao remove(DBObject obj)
        {
                this.collection().remove(obj);

                return this;
        }

        public MongodbDao remove(Model.Id field)
        {
                BasicDBObject query = new BasicDBObject("_id", new ObjectId(field.val()));

                return this.remove(query);
        }

        public <T> MongodbDao remove(Field<T> field)
        {
                BasicDBObject query = new BasicDBObject(field.key(), field.val());

                return this.remove(query);
        }

        public DBCursor find(DBObject obj)
        {
                return this.collection().find(obj);
        }

        public DBCursor getBy(Model.Id field)
        {
                BasicDBObject query = new BasicDBObject("_id", new ObjectId(field.val()));

                return this.collection().find(query);
        }

        public <T> DBCursor getBy(Field<T> field)
        {
                BasicDBObject query = new BasicDBObject(field.key(), field.val());

                return this.collection().find(query);
        }

        public <T> Boolean exists(Field<T> field)
        {
                return this.getBy(field).count() == 1;
        }

        public PersistentModel newOne()
        {
                PersistentModel model = null;

                try {
                        model = new PersistentModel(this.getModelClass().newInstance());

                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                }

                return model;
        }

        public PersistentModel newOne(Model model)
        {
                return new PersistentModel(model);
        }

        public class PersistentModel implements Model
        {
                private Model _model;

                public PersistentModel(Model model)
                {
                        this._model = model;
                }

                @Override
                public HashMap<String, Object> data()
                {
                        return this._model.data();
                }

                @Override
                public PersistentModel set(String key, Object value)
                {
                        this._model.set(key, value);

                        return this;
                }

                @Override
                public <T> PersistentModel set(Field<T> field)
                {
                        this._model.set(field);

                        return this;
                }

                @Override
                public Object get(String key)
                {
                        return this._model.get(key);
                }

                @Override
                public <T> T get(Field<T> field)
                {
                        return this._model.get(field);
                }

                @Override
                public PersistentModel each(Visitor visitor)
                {
                        this._model.each(visitor);

                        return this;
                }

                public String save()
                {
                        MongodbDao.this.save(this);

                        return this.get(new AbstractModel.Id());
                }
        }

        public abstract String getCollectionName();

        public abstract Class<? extends Model> getModelClass();
}

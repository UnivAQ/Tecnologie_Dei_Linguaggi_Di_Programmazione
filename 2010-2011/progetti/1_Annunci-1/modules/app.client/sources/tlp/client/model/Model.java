package tlp.client.model;

import java.util.HashMap;

public interface Model
{
        public HashMap<String, Object> data();

        public Model set(String key, Object value);

        public <T> Model set(Field<T> field);

        public Object get(String key);

        public <T> T get(Field<T> field);

        public Model each(Visitor visitor);

        public interface Visitor
        {
                public void visit(String key, Object value);
        }

        public static interface Id extends Field<String>
        {}
}

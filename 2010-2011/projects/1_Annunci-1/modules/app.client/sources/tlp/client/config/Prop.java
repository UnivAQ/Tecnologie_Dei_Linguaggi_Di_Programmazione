package tlp.client.config;

public interface Prop<T>
{
        public String key();

        public Prop<T> key(T value);

        public Prop<T> key(String value);

        public T val();

        public String string();
}

package tlp.client.config;

public class UserIdProp extends StringProp
{
        public UserIdProp()             { super(); }
        public UserIdProp(String value) { super(value); }
        @Override public String key()   { return "user.id"; }
}

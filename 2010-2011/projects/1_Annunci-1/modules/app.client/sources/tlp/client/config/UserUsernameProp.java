package tlp.client.config;

public class UserUsernameProp extends StringProp
{
        public UserUsernameProp()             { super(); }
        public UserUsernameProp(String value) { super(value); }
        @Override public String key()         { return "user.username"; }
}

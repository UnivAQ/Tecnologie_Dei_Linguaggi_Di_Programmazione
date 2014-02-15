package tlp.client.util;

public class TextUtil
{
        public static String upperCaseFirst(String s)
        {
                return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
        }
}

/**
 * Created by andrew on 2017/7/17.
 */
public class Ognl {
    public static boolean isEmpty(Object o)
    {
        if (o == null) {
            return true;
        }
        if ((o instanceof String))
        {
            String str = (String)o;
            if (str.length() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(Object o)
    {
        return !isEmpty(o);
    }

    public static boolean isNotBlank(Object o)
    {
        return !isBlank(o);
    }

    public static boolean isBlank(Object o)
    {
        if (o == null) {
            return true;
        }
        if ((o instanceof String))
        {
            String str = (String)o;
            return isBlank(str);
        }
        return false;
    }

    public static boolean isBlank(String str)
    {
        if ((str == null) || (str.length() == 0)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

package util;

import java.util.Set;

public class CommonUtil {


    public static boolean checkTwoSetIfEqual(Set<String> list1, Set<String> list2) {
        if (list1 == null && list2 == null) return true;
        if (list1 == null && list2 != null) return false;
        if (list1 != null && list2 == null) return false;
        if (list1.size() != list2.size()) return false;
        list1.retainAll(list2);
        return list1.size() == list2.size();
    }


}

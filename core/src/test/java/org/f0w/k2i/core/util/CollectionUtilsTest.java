package org.f0w.k2i.core.util;

import org.f0w.k2i.TestHelper;
import org.junit.Test;

import java.util.*;

import static org.f0w.k2i.core.util.CollectionUtils.combineLists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollectionUtilsTest {
    @Test
    public void constructorIsPrivate() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(CollectionUtils.class));

        TestHelper.callPrivateConstructor(CollectionUtils.class);
    }

    @Test
    public void combineNormalLists() throws Exception {
        List<String> firstList = Arrays.asList("1", "2", "3");
        List<Integer> secondList = Arrays.asList(4, 5, 6);

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("1", 4);
        expected.put("2", 5);
        expected.put("3", 6);

        assertEquals(expected, combineLists(firstList, secondList));
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionOnCombineListsWithDissimilarSize() throws Exception {
        List<String> firstList = Arrays.asList("1", "2", "3", "4");
        List<Integer> secondList = Arrays.asList(4, 5, 6);

        combineLists(firstList, secondList);
    }

    @Test
    public void combineEmptyLists() throws Exception {
        List<?> firstList = Collections.emptyList();
        List<?> secondList = Collections.emptyList();

        assertEquals(Collections.emptyMap(), combineLists(firstList, secondList));
    }
}
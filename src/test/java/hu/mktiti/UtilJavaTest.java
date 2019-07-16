package hu.mktiti;

import org.junit.Test;
import static org.junit.Assert.*;

import static hu.mktiti.Util.*;

public class UtilJavaTest {

    @Test
    public void test_var_name_null() {
        assertFalse(isValidJavaName(null));
    }

    @Test
    public void test_var_name_empty() {
        assertFalse(isValidJavaName(""));
    }

    @Test
    public void test_var_name_starts_with_digit() {
        assertFalse(isValidJavaName("5"));
    }

    @Test
    public void test_var_name_contains_whitespace() {
        assertFalse(isValidJavaName("foo\tbar"));
    }

    @Test
    public void test_var_name_java_keyword() {
        assertFalse(isValidJavaName("strictfp"));
    }

    @Test
    public void test_var_name_kotlin_keyword() {
        assertTrue(isValidJavaName("when"));
    }

    @Test
    public void test_var_name_contains_dash() {
        assertFalse(isValidJavaName("foo-bar"));
    }

    @Test
    public void test_var_name_contains_underscore() {
        assertTrue(isValidJavaName("_foo_bar"));
    }

    @Test
    public void test_var_name_contains_dollar_sign() {
        assertTrue(isValidJavaName("$foo$bar"));
    }

    @Test
    public void test_var_name_non_english() {
        // Discouraged, but allowed
        assertTrue(isValidJavaName("árvíztűrőTükörfúró"));
    }

    @Test
    public void test_var_name_chinese() {
        // Discouraged, but allowed
        assertTrue(isValidJavaName("你好"));
    }

}
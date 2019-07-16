package hu.mktiti;

import org.junit.Test;

import static hu.mktiti.Util.isValidKotlinName;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilKotlinTest {

    @Test
    public void test_var_name_null() {
        assertFalse(isValidKotlinName(null));
    }

    @Test
    public void test_var_name_empty() {
        assertFalse(isValidKotlinName(""));
    }

    @Test
    public void test_var_name_starts_with_digit() {
        assertFalse(isValidKotlinName("5"));
    }

    @Test
    public void test_var_name_contains_whitespace() {
        assertFalse(isValidKotlinName("foo\tbar"));
    }

    @Test
    public void test_var_name_java_keyword() {
        assertTrue(isValidKotlinName("strictfp"));
    }

    @Test
    public void test_var_name_kotlin_keyword() {
        assertFalse(isValidKotlinName("when"));
    }

    @Test
    public void test_var_name_contains_dash() {
        assertFalse(isValidKotlinName("foo-bar"));
    }

    @Test
    public void test_var_name_contains_underscore() {
        assertTrue(isValidKotlinName("_foo_bar"));
    }

    @Test
    public void test_var_name_contains_dollar_sign() {
        assertFalse(isValidKotlinName("$foo$bar"));
    }

    @Test
    public void test_var_name_non_english() {
        // Discouraged, but allowed
        assertTrue(isValidKotlinName("árvíztűrőTükörfúró"));
    }

    @Test
    public void test_var_name_chinese() {
        // Discouraged, but allowed
        assertTrue(isValidKotlinName("你好"));
    }

}
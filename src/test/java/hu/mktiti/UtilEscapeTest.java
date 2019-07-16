package hu.mktiti;

import org.junit.Test;
import static org.junit.Assert.*;

import static hu.mktiti.Util.*;

public class UtilEscapeTest {

    @SuppressWarnings("all")
    @Test(expected = NullPointerException.class)
    public void test_null() {
        escapeString(null);
    }

    @Test
    public void test_empty() {
        assertEquals("\"\"", escapeString(""));
    }

    @Test
    public void test_simple() {
        assertEquals("\"hello world\"", escapeString("hello world"));
    }

    @Test
    public void test_backslash() {
        assertEquals("\"hello\\\\world\"", escapeString("hello\\world"));
    }

    @Test
    public void test_newline() {
        assertEquals("\"hello\\nworld\"", escapeString("hello\nworld"));
    }

    @Test
    public void test_tab() {
        assertEquals("\"hello\\tworld\"", escapeString("hello\tworld"));
    }

    @Test
    public void test_quote() {
        assertEquals("\"hello\\\"world\"", escapeString("hello\"world"));
    }

    @Test
    public void test_apostrophe() {
        assertEquals("\"hello\\\'world\"", escapeString("hello\'world"));
    }

    @Test
    public void test_backspace() {
        assertEquals("\"hello\\bworld\"", escapeString("hello\bworld"));
    }

    @Test
    public void test_form_feed() {
        assertEquals("\"hello\\fworld\"", escapeString("hello\fworld"));
    }

    @Test
    public void test_carriage_return() {
        assertEquals("\"hello\\rworld\"", escapeString("hello\rworld"));
    }

}
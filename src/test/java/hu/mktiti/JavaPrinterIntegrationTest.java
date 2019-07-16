package hu.mktiti;

import com.itranswarp.compiler.JavaStringCompiler;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JavaPrinterIntegrationTest extends PrinterIntegrationTest {

    private final static Map<String, String> TEST_MAP = new HashMap<>();
    static {
        TEST_MAP.put("version", "1.0");
        TEST_MAP.put("foo", "bar");
    }

    private final Logger logger = Logger.getLogger("JavaPrinterIntegrationTest");

    @Test
    public void test_values() throws Exception {
        testGetters();
        testFields();
    }

    private void testFields() throws Exception {
        final Class<?> resClass = result(new PrinterConf(
                "hu.mktiti.test", "MyObject", "whatever", PrinterConf.Visibility.PUBLIC, false
        ), TEST_MAP);

        testField(resClass, "version", "1.0");
        testField(resClass, "foo", "bar");
    }

    private void testField(final Class<?> resClass, final String fieldName, final String value) throws Exception {
        final Field field = resClass.getField(fieldName);
        logger.info("Testing field [" + fieldName + "] static modifier...");
        assertTrue("Field not static", Modifier.isStatic(field.getModifiers()));

        assertEquals(value, field.get(null));
    }

    private void testGetters() throws Exception {
        final Class<?> resClass = result(new PrinterConf(
                "hu.mktiti.test", "MyObject", "whatever", PrinterConf.Visibility.PUBLIC, true
        ), TEST_MAP);

        testGetter(resClass, "getVersion", "1.0");
        testGetter(resClass, "getFoo", "bar");
    }

    private void testGetter(final Class<?> resClass, final String getterName, final String value) throws Exception {
        final Method getter = resClass.getMethod(getterName);
        logger.info("Testing getter [" + getterName + "] static modifier...");
        assertTrue("Getter not static", Modifier.isStatic(getter.getModifiers()));

        assertEquals(value, getter.invoke(null));
    }

    @Test
    public void test_modifiers() throws Exception {
        testAllVisibility(PrinterConf.Visibility.PUBLIC, Modifier::isPublic);
        testAllVisibility(PrinterConf.Visibility.PROTECTED, Modifier::isProtected);

        testAllVisibility(PrinterConf.Visibility.PACKAGE_PRIVATE, mod ->
            !Modifier.isPrivate(mod) && !Modifier.isProtected(mod) && !Modifier.isPublic(mod)
        );
        testAllVisibility(PrinterConf.Visibility.DEFAULT, mod ->
                !Modifier.isPrivate(mod) && !Modifier.isProtected(mod) && !Modifier.isPublic(mod)
        );
    }

    private void testAllVisibility(final PrinterConf.Visibility visibility, final IntPredicate check) throws Exception {
        testVisibility(true, visibility, check);
        testVisibility(false, visibility, check);
    }

    private void testVisibility(final boolean useGetters, final PrinterConf.Visibility visibility, final IntPredicate check) throws Exception {
        final Class<?> resClass = result(new PrinterConf(
                "hu.mktiti.test", "MyObject", "whatever", visibility, useGetters
        ), TEST_MAP);

        logger.info("Testing class modifier...");
        assertEquals("Class visibility modifier error",
                Modifier.isPublic(resClass.getModifiers()), visibility == PrinterConf.Visibility.PUBLIC);

        if (useGetters) {
            assertTrue("getVersion visibility modifier error", check.test(resClass.getDeclaredMethod("getVersion").getModifiers()));
            assertTrue("getFoo visibility modifier error", check.test(resClass.getDeclaredMethod("getFoo").getModifiers()));
        } else {
            assertTrue("version member visibility modifier error", check.test(resClass.getDeclaredField("version").getModifiers()));
            assertTrue("foo member visibility modifier error", check.test(resClass.getDeclaredField("foo").getModifiers()));
        }
    }

    @Override
    protected ConstPrinter createPrinter(final PrinterConf conf) {
        return new JavaPrinter(conf);
    }

    @Override
    protected Class<?> compile(final String filename, final String qualifiedName, final String content) throws Exception {
        final JavaStringCompiler compiler = new JavaStringCompiler();
        final Map<String, byte[]> results = compiler.compile(filename, content);

        return compiler.loadClass(qualifiedName, results);
    }

}
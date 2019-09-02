package hu.mktiti;

import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

abstract class PrinterIntegrationTestBase {

    private final Logger logger = Logger.getLogger("PrinterIntegrationTestBase");

    final static Map<String, String> TEST_MAP = new HashMap<>();
    static {
        TEST_MAP.put("version", "1.0");
        TEST_MAP.put("foo", "bar");
    }

    protected Class<?> result(final PrinterConf conf) throws Exception {
        return result(conf, TEST_MAP);
    }

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

    abstract boolean isClassModifierValid(final PrinterConf.Visibility visibility, final int modifiers);

    abstract boolean isMemberModifierValid(final PrinterConf.Visibility visibility, final int modifiers);

    @Test
    public void test_modifiers() throws Exception {
        for (PrinterConf.Visibility visibility : PrinterConf.Visibility.values()) {
            testModifiersForVisibilityMember(visibility);
        }
    }

    private void testModifiersForVisibilityMember(final PrinterConf.Visibility visibility) throws Exception {
        testModifiersForVisibility(true, visibility);
        testModifiersForVisibility(false, visibility);
    }

    private void testModifiersForVisibility(final boolean useGetters, final PrinterConf.Visibility visibility) throws Exception {
        final Class<?> resClass = result(new PrinterConf(
                "hu.mktiti.test", "MyObject", "whatever", visibility, useGetters
        ), TEST_MAP);

        logger.info("Testing class modifier...");
        assertTrue("Class visibility modifier error", isClassModifierValid(visibility, resClass.getModifiers()));

        if (useGetters) {
            for (String getterName : Arrays.asList("getVersion", "getFoo")) {
                assertTrue(getterName + " getter visibility modifier error",
                        isMemberModifierValid(visibility, resClass.getDeclaredMethod(getterName).getModifiers()));
                assertTrue(getterName + " getter static modifier error",
                        Modifier.isStatic(resClass.getDeclaredMethod(getterName).getModifiers()));
            }
        } else {
            for (String fieldName : Arrays.asList("version", "foo")) {
                assertTrue(fieldName + " member visibility modifier error",
                        isMemberModifierValid(visibility, resClass.getDeclaredField(fieldName).getModifiers()));
                assertTrue(fieldName + " member static modifier error",
                        Modifier.isStatic(resClass.getDeclaredField(fieldName).getModifiers()));
            }
        }
    }

    Class<?> result(final PrinterConf conf, final Map<String, String> variables) throws Exception {
        final ConstPrinter printer = createPrinter(conf);
        final StringWriter writer = new StringWriter();

        final String filename = printer.getFilename();
        final String qualifiedName = conf.packageName + "." + conf.className;

        try (final PrintWriter target = new PrintWriter(writer)) {
            printer.print(variables, new PrintWriter(writer));
        }

        final String content = writer.toString();

        return compile(filename, qualifiedName, content);
    }

    abstract protected ConstPrinter createPrinter(final PrinterConf conf);

    abstract protected Class<?> compile(final String filename, final String qualifiedName, final String content) throws Exception;

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utils;

import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author lcabraja
 */
public class ReflectionUtils {

    private static final String NEWLINE_DELIMITER = "\n";

    private ReflectionUtils() {
    }

    public static void readMembersInfo(Class<?> clazz, StringBuilder sb, String newLineCharacter) {
        readClassInfo(clazz, sb, newLineCharacter);
        appendFields(clazz, sb, newLineCharacter);
        appendMethod(clazz, sb, newLineCharacter);
        appendConstructors(clazz, sb, newLineCharacter);
    }

    public static void readMembersInfo(Class<?> clazz, StringBuilder sb) {
        readMembersInfo(clazz, sb, NEWLINE_DELIMITER);
    }

    private static void appendFields(Class<?> clazz, StringBuilder sb, String newLineCharacter) {
        Field[] fields = clazz.getDeclaredFields();
        sb.append(newLineCharacter).append(newLineCharacter);
        sb.append(
                Arrays
                        .stream(fields)
                        .map(Objects::toString)
                        .collect(Collectors.joining(newLineCharacter))
        );
    }

    private static void appendMethod(Class<?> clazz, StringBuilder sb, String newLineCharacter) {
        Method[] list = clazz.getDeclaredMethods();
        for (Method m : list) {
            sb.append(newLineCharacter);
            appendAnnotation(m, sb, newLineCharacter);
            sb.append(newLineCharacter);
            sb.append(Modifier.toString(m.getModifiers()));
            sb.append(" ");
            sb.append(m.getReturnType());
            sb.append(" ");
            sb.append(m.getName());
            appendParameters(m, sb);
            appendExceptions(m, sb);
        }
    }

    private static void appendConstructors(Class<?> clazz, StringBuilder sb, String newLineCharacter) {
        Executable[] list = clazz.getDeclaredConstructors();
        for (Executable c : list) {
            sb.append(newLineCharacter).append(newLineCharacter);
            appendAnnotation(c, sb, newLineCharacter);
            sb.append(newLineCharacter);
            sb.append(Modifier.toString(c.getModifiers()));
            sb.append(" ");
            sb.append(c.getName());
            appendParameters(c, sb);
            appendExceptions(c, sb);
        }
    }

    private static void appendAnnotation(Executable m, StringBuilder sb, String newLineCharacter) {
        sb.append(Arrays
                .stream(m.getAnnotations())
                .map(Objects::toString)
                .collect(Collectors.joining(newLineCharacter))
        );
    }

    private static void appendParameters(Executable m, StringBuilder sb) {
        sb.append(Arrays
                .stream(m.getParameters())
                .map(Objects::toString)
                .collect(Collectors.joining(", ", "(", ")"))
        );
    }

    private static void appendExceptions(Executable m, StringBuilder sb) {
        if (m.getExceptionTypes().length > 0) {
            sb.append(" throws ");
            sb.append(Arrays
                    .stream(m.getExceptionTypes())
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(" "))
            );
        }
    }

    private static void readClassInfo(Class<?> clazz, StringBuilder sb, String newLineCharacter) {
        appendPackage(clazz, sb, newLineCharacter);
        appendModifier(clazz, sb);
        sb.append(" ").append(clazz.getSimpleName());
        appendParent(clazz, sb, true, newLineCharacter);
        appendInterfaces(clazz, sb, newLineCharacter);
    }

    private static void appendPackage(Class<?> clazz, StringBuilder sb, String newLineCharacter) {
        sb.append(clazz.getPackage()).append(newLineCharacter).append(newLineCharacter);
    }

    private static void appendModifier(Class<?> clazz, StringBuilder sb) {
        int mod = clazz.getModifiers();
        sb.append(Modifier.toString(mod));
    }

    private static void appendParent(Class<?> clazz, StringBuilder sb, Boolean first, String newLineCharacter) {
        Class<?> parent = clazz.getSuperclass();
        if (parent == null) {
            return;
        }
        if (first) {
            sb.append("extends ");
        }
        sb.append(" ").append(parent.getSimpleName());
        appendParent(parent, sb, false, newLineCharacter);
    }

    private static void appendInterfaces(Class<?> clazz, StringBuilder sb, String newLineCharacter) {
        if (clazz.getInterfaces().length > 0) {
            sb.append(newLineCharacter).append("implements ");
            sb.append(Arrays
                    .stream(clazz.getInterfaces())
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(" "))
            );
        }
    }
}

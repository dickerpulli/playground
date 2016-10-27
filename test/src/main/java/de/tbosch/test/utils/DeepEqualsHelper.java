package com.provinzial.protago.allsparten.plattform.testutil;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * Prüft die Gleichheit von Objekten und das rekursiv über alle Attribute bis eine Stoppklasse erreicht wird.
 *
 * @author Thomas Bosch (thomas.bosch@codecentric.de)
 */
public class DeepEqualsHelper<T> {

    private int recursionCounter;

    private static final EqualityChecker<Object> DEFAULT = new EqualityChecker<Object>() {
        @Override
        public boolean isEqual(Object a, Object b) {
            if (a == null && b == null) {
                return true;
            } else if (a == null || b == null) {
                return false;
            }
            return a.equals(b);
        }
    };

    private static final Map<Class, EqualityChecker> CUSTOMS = Maps.newHashMap();

    {
        CUSTOMS.put(Set.class, new EqualityChecker<Set>() {
            @Override
            public boolean isEqual(Set a, Set b) {
                if (a == null && b == null) {
                    return true;
                } else if (a == null || b == null) {
                    return false;
                }
                return new DeepEqualsSet(a).equals(new DeepEqualsSet(b));
            }
        });
        CUSTOMS.put(List.class, new EqualityChecker<List>() {
            @Override
            public boolean isEqual(List a, List b) {
                if (a == null && b == null) {
                    return true;
                } else if (a == null || b == null) {
                    return false;
                }
                return new DeepEqualsList(a).equals(new DeepEqualsList(b));
            }
        });
        CUSTOMS.put(Map.class, new EqualityChecker<Map>() {
            @Override
            public boolean isEqual(Map a, Map b) {
                if (a == null && b == null) {
                    return true;
                } else if (a == null || b == null) {
                    return false;
                }
                return new DeepEqualsMap(a).equals(new DeepEqualsMap(b));
            }
        });
    }

    private static final Set<Class<?>> STOPCLASS = Sets.newHashSet();

    {
        STOPCLASS.add(String.class);
        STOPCLASS.add(Integer.class);
        STOPCLASS.add(int.class);
        STOPCLASS.add(Long.class);
        STOPCLASS.add(long.class);
        STOPCLASS.add(Short.class);
        STOPCLASS.add(short.class);
        STOPCLASS.add(Boolean.class);
        STOPCLASS.add(boolean.class);
        STOPCLASS.add(BigDecimal.class);
        STOPCLASS.add(Money.class);
        STOPCLASS.add(LocalDate.class);
        STOPCLASS.add(DateTime.class);
        STOPCLASS.add(UUID.class);
    }

    /**
     * Vom Vergleich ausgenommene Felder.
     */
    protected Map<Class<?>, Set<String>> excludedAttributes = Maps.newHashMap();

    /**
     * Vergleicht die zwei übergebenen Objekte rekursiv.
     *
     * @param a Das erste Objekt.
     * @param b Das zweite Objekt.
     * @return Gleich?
     */
    public <T> boolean isEqual(T a, T b) {
        try {
            recursionCounter = 0;
            return isEqualInternal(a, b);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private <T> boolean isEqualInternal(T a, T b) throws IllegalArgumentException, IllegalAccessException {
        if (a == null && b == null) {
            return true;
        } else if (a == null || b == null) {
            if (a == null) {
                System.err.println("Diff: null != " + b.getClass());
            } else {
                System.err.println("Diff: " + a.getClass() + " != null");
            }
            return false;
        } else if (!a.getClass().equals(b.getClass())) {
            return false;
        } else if (STOPCLASS.contains(a.getClass()) || a.getClass().isEnum()) {
            boolean equal = DEFAULT.isEqual(a, b);
            if (!equal) {
                System.err.println("Diff: " + a + " != " + b);
            }
            return equal;
        }
        boolean allFieldEqual = true;
        Set<Field> fields = ReflectionUtils.getAllFields(a.getClass(), new Predicate<Field>() {
            @Override
            public boolean apply(Field input) {
                int modifiers = input.getModifiers();
                return Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers);
            }
        });
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            recursionCounter++;
            if (recursionCounter > 100) {
                throw new IllegalStateException("Too deep recusion - maybe field type must be a STOPCLASS: " + type);
            }
            boolean thisFieldEqual = true;
            if (isFieldExcluded(a.getClass(), field.getName())) {
                thisFieldEqual = true;
            } else if (STOPCLASS.contains(type)) {
                thisFieldEqual = DEFAULT.isEqual(field.get(a), field.get(b));
            } else if (CUSTOMS.containsKey(type)) {
                thisFieldEqual = CUSTOMS.get(type).isEqual(field.get(a), field.get(b));
            } else {
                thisFieldEqual = isEqualInternal(field.get(a), field.get(b));
            }
            if (!thisFieldEqual) {
                System.err.println("Diff: " + field + " != " + field);
            }
            allFieldEqual &= thisFieldEqual;
            recursionCounter--;
        }
        return allFieldEqual;
    }

    /**
     * Ist das Feld mit dem Namen in der gegebenen oder einer der Vater-Klassen exkludiert.
     */
    private boolean isFieldExcluded(Class<?> clazz, String fieldName) {
        return excludedAttributes.containsKey(clazz) && excludedAttributes.get(clazz).contains(fieldName)
                || clazz.getSuperclass() != null && isFieldExcluded(clazz.getSuperclass(), fieldName);
    }

    private class DeepEqualsSet<E> extends HashSet<E> {
        public DeepEqualsSet(Collection<E> c) {
            super(c);
        }

        @Override
        public boolean contains(Object o) {
            Iterator<E> it = iterator();
            if (o == null) {
                while (it.hasNext()) {
                    if (it.next() == null) {
                        return true;
                    }
                }
            } else {
                while (it.hasNext()) {
                    if (isEqual(o, it.next())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private class DeepEqualsList<E> extends ArrayList<E> {
        public DeepEqualsList(Collection<E> c) {
            super(c);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof List)) {
                return false;
            }

            ListIterator<E> e1 = listIterator();
            ListIterator e2 = ((List) o).listIterator();
            while (e1.hasNext() && e2.hasNext()) {
                E o1 = e1.next();
                Object o2 = e2.next();
                if (!(o1 == null ? o2 == null : isEqual(o1, o2))) {
                    return false;
                }
            }
            return !(e1.hasNext() || e2.hasNext());
        }
    }

    private class DeepEqualsMap<K, V> extends HashMap<K, V> {
        public DeepEqualsMap(Map<K, V> m) {
            super(m);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof Map)) {
                return false;
            }
            Map<K, V> m = (Map<K, V>) o;
            if (m.size() != size()) {
                return false;
            }

            try {
                Iterator<Entry<K, V>> i = entrySet().iterator();
                while (i.hasNext()) {
                    Entry<K, V> e = i.next();
                    K key = e.getKey();
                    V value = e.getValue();
                    if (value == null) {
                        if (!(m.get(key) == null && m.containsKey(key))) {
                            return false;
                        }
                    } else {
                        if (!isEqual(value, m.get(key))) {
                            return false;
                        }
                    }
                }
            } catch (ClassCastException unused) {
                return false;
            } catch (NullPointerException unused) {
                return false;
            }

            return true;
        }

    }

    // /**
    // * Test-Main.
    // * @param args ein Array der Parameter der Kommandozeile
    // */
    // public static void main(String[] args) {
    // A a1 = new A();
    // B b1 = new B();
    // b1.t = "a";
    // a1.list.add(b1);
    //
    // A a2 = new A();
    // B b2 = new B();
    // b2.t = "a";
    // a2.list.add(b2);
    //
    // System.out.println(DeepEqualsHelper.isEqual(a1, a2));
    // }
    //
    // private static class A {
    // private List<B> list = Lists.newArrayList();
    // }
    //
    // private static class B {
    // private String t;
    // }

}

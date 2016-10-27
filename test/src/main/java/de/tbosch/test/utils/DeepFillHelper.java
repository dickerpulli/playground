package de.tbosch.test.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.provinzial.protago.allsparten.plattform.common.ProtagoException;
import org.apache.commons.collections.KeyValue;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * Füllt ein Objekt rekursiv mit Dummywerten auf. Die Rekursion endet, wenn ein Objekt aus dem Package "java.lang" erreicht ist,
 * oder die Logik zieht, die in {@link #isFillableRekursively(Object)} implementiert ist.
 *
 * @param <A>
 * @author Thomas Bosch (thomas.bosch@codecentric.de)
 */
public class DeepFillHelper<A> {

    /**
     * Defaults für alle Attribute.
     * <pre>
     * Antragsnummer.class, new Antragsnummer("1")
     * </pre>
     */
    protected Map<Class<?>, Object> defaults = Maps.newHashMap();

    {
        defaults.put(String.class, "text");
        defaults.put(Integer.class, 1);
        defaults.put(int.class, 1);
        defaults.put(Long.class, 1L);
        defaults.put(long.class, 1L);
        defaults.put(Boolean.class, true);
        defaults.put(boolean.class, true);
        defaults.put(BigDecimal.class, BigDecimal.ONE);
        defaults.put(Money.class, Money.of(CurrencyUnit.EUR, 1));
        defaults.put(LocalDate.class, LocalDate.now());
        defaults.put(DateTime.class, DateTime.now());

    }

    /**
     * Vom Befüllen des Objekts ausgenommene Felder.
     */
    protected Map<Class<?>, Set<String>> excludedAttributes = Maps.newHashMap();

    /**
     * Defaults für Collections, die aufgrund von Generics nicht generisch erzeugt werden können.
     * <pre>
     * new ClassAndFieldName(SachVersicherungsort.class, "bereiche"), new SachBereich()
     * </pre>
     */
    protected Map<ClassAndFieldName, Object> collectionDefaults = Maps.newHashMap();

    /**
     * Defaults für einfachen Attributen, die aufgrund von Generics nicht generisch erzeugt werden können.
     * <pre>
     * new ClassAndFieldName(WagnisNachlass.class, "art"), WagnisNachlassArt.SAISON
     * </pre>
     */
    protected Map<ClassAndFieldName, Object> simpleDefaults = Maps.newHashMap();

    /**
     * Defaults für Maps, die nicht sinnvoll generisch erzeugt werden können.
     * <pre>
     * new ClassAndFieldName(SachBeitragsinformation.class, "positionBeitraege"), new DefaultKeyValue(
     *             BereichPositionArt.BETRIEBSEINRICHTUNG_FEST, new PositionBeitrag())
     * </pre>
     */
    protected Map<ClassAndFieldName, KeyValue> mapDefaults = Maps.newHashMap();

    /**
     * Prüft, ob das Objekt noch tiefer rekursiv generisch gefüllt werden kann.
     *
     * @param objekt Das Objekt.
     * @return Rekursiv füllbar?
     */
    protected boolean isFillableRekursively(Object objekt) {
        return !objekt.getClass().getPackage().getName().equals("java.lang");
    }

    /**
     * Fülle das Objekt rekursiv bis zum Ende - definiert in {@link #isFillableRekursively(Object)}.
     *
     * @param objekt Das zu füllende Objekt.
     * @throws Exception
     */
    public void fill(A objekt) {
        try {
            fillInternal(objekt);
        } catch (Exception exception) {
            throw new ProtagoException("Kann nicht füllen", exception);
        }
    }

    private void fillInternal(Object objekt) throws Exception {
        Set<Field> fields = ReflectionUtils.getAllFields(objekt.getClass(), new Predicate<Field>() {
            @Override
            public boolean apply(Field input) {
                int modifiers = input.getModifiers();
                return Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers);
            }
        });
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();

            if (field.get(objekt) != null && !Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type)) {
                continue;
            }
            if (objekt != null && field != null && isFieldExcluded(objekt.getClass(), field.getName())) {
                continue;
            }

            if (UUID.class.isAssignableFrom(type)) {
                field.set(objekt, UUID.randomUUID());
            } else if (defaults.containsKey(type)) {
                field.set(objekt, defaults.get(type));
            } else if (simpleDefaults.containsKey(new ClassAndFieldName(objekt, field))) {
                field.set(objekt, simpleDefaults.get(new ClassAndFieldName(objekt, field)));
            } else if (Enum.class.isAssignableFrom(type)) {
                field.set(objekt, type.getEnumConstants()[0]);
            } else if (Collection.class.isAssignableFrom(type)) {
                fillCollection(objekt, field, type);
            } else if (Map.class.isAssignableFrom(type)) {
                Map map = (Map) field.get(objekt);
                if (mapDefaults.containsKey(new ClassAndFieldName(objekt, field))) {
                    fuegeElementHinzu(map, mapDefaults.get(new ClassAndFieldName(objekt, field)).getKey(),
                            mapDefaults.get(new ClassAndFieldName(objekt, field)).getValue());
                } else {
                    throw new ProtagoException("Sonderfall nicht betrachtet: " + field);
                }
            } else if (isFillableRekursively(objekt)) {
                Object object = field.get(objekt);
                if (object == null) {
                    object = type.newInstance();
                    field.set(objekt, object);
                }
                fillInternal(object);
            } else {
                continue;
            }
        }
    }

    private void fillCollection(Object objekt, Field field, Class<?> type) throws Exception {
        Collection collection = (Collection) field.get(objekt);
        if (collection == null) {
            if (Set.class.isAssignableFrom(type)) {
                collection = new HashSet();
            } else if (List.class.isAssignableFrom(type)) {
                collection = new ArrayList();
            }
            field.set(objekt, collection);
        }
        Type elementType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        if (elementType instanceof Class) {
            Class<?> clazz = (Class<?>) elementType;
            int modifiers = clazz.getModifiers();
            if (Modifier.isAbstract(modifiers)) {
                Set<? extends Class> subTypes =
                        new Reflections(objekt.getClass().getPackage().getName()).getSubTypesOf(clazz);
                for (Class subType : subTypes) {
                    int subTypeModifiers = subType.getModifiers();
                    if (Modifier.isPrivate(subTypeModifiers)//
                            || Modifier.isStatic(subTypeModifiers)//
                            || Modifier.isAbstract(subTypeModifiers)//
                            || !Modifier.isPublic(subTypeModifiers)) {
                        continue;
                    }
                    addElement(collection, subType.newInstance());
                }
            } else {
                if (UUID.class.isAssignableFrom(clazz)) {
                    addElement(collection, UUID.randomUUID());
                } else if (defaults.containsKey(clazz)) {
                    addElement(collection, defaults.get(clazz));
                } else {
                    addElement(collection, clazz.newInstance());
                }
            }
        } else if (collectionDefaults.containsKey(new ClassAndFieldName(objekt, field))) {
            addElement(collection, collectionDefaults.get(new ClassAndFieldName(objekt, field)));
        } else {
            throw new ProtagoException("Sonderfall nicht betrachtet: " + field);
        }
    }

    /**
     * Ist das Feld mit dem Namen in der gegebenen oder einer der Vater-Klassen exkludiert.
     */
    private boolean isFieldExcluded(Class<?> clazz, String fieldName) {
        return excludedAttributes.containsKey(clazz) && excludedAttributes.get(clazz).contains(fieldName)
                || clazz.getSuperclass() != null && isFieldExcluded(clazz.getSuperclass(), fieldName);
    }

    private void fuegeElementHinzu(Map map, Object key, Object value) throws Exception {
        if (isFillableRekursively(key)) {
            fillInternal(key);
        }
        if (isFillableRekursively(value)) {
            fillInternal(value);
        }
        map.put(key, value);
    }

    private void addElement(Collection collection, Object element) throws Exception {
        if (isFillableRekursively(element)) {
            fillInternal(element);
        }
        collection.add(element);
    }

}

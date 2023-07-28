package dev.nebulamc.inject.internal;

import dev.nebulamc.inject.ServiceFinder;
import org.jspecify.nullness.NullMarked;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The main implementation of {@link ParameterResolver}.
 * <p>
 * Supports the following collection types:
 * <ul>
 *     <li>{@link Iterable}</li>
 *     <li>{@link Collection}</li>
 *     <li>{@link List}</li>
 *     <li>{@link Set}</li>
 *     <li>Array</li>
 * </ul>
 *
 * @author Sparky983
 */
@NullMarked
public final class ParameterResolverImpl implements ParameterResolver {

    @Override
    public Object resolveParameter(final Parameter parameter, final ServiceFinder serviceFinder) {

        final Type type = parameter.getParameterizedType();

        if (type instanceof final ParameterizedType parameterizedType) {
            final Type rawType = parameterizedType.getRawType();
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments.length == 1 &&
                    typeArguments[0] instanceof final Class<?> elementClass) {
                if (rawType.equals(Iterable.class) ||
                        rawType.equals(Collection.class) ||
                        rawType.equals(List.class)) {
                        return serviceFinder.findServices(elementClass);
                } else if (rawType.equals(Set.class)) {
                    return Set.copyOf(serviceFinder.findServices(elementClass));
                }
            }
        } else if (type instanceof final Class<?> cls && cls.isArray()) {
            final List<?> services = serviceFinder.findServices(cls.componentType());
            final Object array = Array.newInstance(cls.componentType(), services.size());
            for (int i = 0; i < services.size(); i++) {
                Array.set(array, i, services.get(i));
            }
            return array;
        }

        return serviceFinder.findService(parameter.getType());
    }
}

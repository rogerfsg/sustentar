package puc.sustentar.stores;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;
import org.jooby.funzy.Throwing;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;


import static java.util.Objects.requireNonNull;


public class JdbiStore {


    private Jdbi jdbi;

    private Handle handle;


    protected JdbiStore() {
        // TODO: remove me once we kill Hibernate
    }

    public JdbiStore(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public JdbiStore(Handle handle) {
        this.handle = handle;
    }

    /**
     * A convenience function which manages the lifecycle of a handle and yields it to a callback
     * for use by clients.
     *
     * @param callback A callback which will receive an open Handle
     * @param <R> type returned by the callback
     * @param <X> exception type thrown by the callback, if any.
     *
     * @return the value returned by callback
     *
     * @throws X any exception thrown by the callback
     */
    public  <R, X extends Exception> R withHandle(HandleCallback<R, X> callback) throws X {
        if (handle == null) {
            requireNonNull(jdbi, "Jdbi or handle required");
            return jdbi.withHandle(callback);
        } else {
            // attach to existing handle
            return callback.withHandle(handle);
        }
    }

    /**
     * A convenience function which manages the lifecycle of a handle and yields it to a callback
     * for use by clients.
     *
     * @param callback A callback which will receive an open Handle
     * @param <X> exception type thrown by the callback, if any.
     *
     * @throws X any exception thrown by the callback
     */
    public <X extends Exception> void useHandle(final HandleConsumer<X> callback) throws X {
        withHandle(h -> {
            callback.useHandle(h);
            return null;
        });
    }

    /**
     * A convenience function which manages the lifecycle of a handle and yields it to a callback
     * for use by clients. The handle will be in a transaction when the callback is invoked, and
     * that transaction will be committed if the callback finishes normally, or rolled back if the
     * callback raises an exception.
     *
     * @param callback A callback which will receive an open Handle, in a transaction
     * @param <R> type returned by the callback
     * @param <X> exception type thrown by the callback, if any.
     *
     * @return the value returned by callback
     *
     * @throws X any exception thrown by the callback
     */
    public <R, X extends Exception> R withTransaction(HandleCallback<R, X> callback) throws X {
        if (handle == null) {
            requireNonNull(jdbi, "Jdbi or handle required");
            return jdbi.inTransaction(callback);
        } else {
            // attach to existing handle
            return handle.inTransaction(callback);
        }
    }

    /**
     * A convenience function which manages the lifecycle of a handle and yields it to a callback
     * for use by clients. The handle will be in a transaction when the callback is invoked, and
     * that transaction will be committed if the callback finishes normally, or rolled back if the
     * callback raises an exception.
     *
     * @param callback A callback which will receive an open Handle, in a transaction
     * @param <X> exception type thrown by the callback, if any.
     *
     * @throws X any exception thrown by the callback
     */
    public <X extends Exception> void useTransaction(final HandleConsumer<X> callback) throws X {
        withTransaction(h -> {
            callback.useHandle(h);
            return null;
        });
    }

    /**
     * Get the first element of the given list. Empty or null list produces a null output,
     * list with more than one element produces a not unique result exception.
     *
     * @param list List to check.
     * @param <T> Generic type.
     * @return Unique element or <code>null</code>.
     */
    protected <T> T uniqueResult(List<T> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        throw new IllegalStateException("Not unique result found");
    }

    /**
     * Generates a SQL insert statement for the given entity and returns the parameter binding
     * using the parameter argument.
     *
     * NOTE: This method works for simple entity by reading {@link Column} annotation metadata.
     * This method is NOT intended to read/map complex data or relationships.
     *
     * NOTE: This method uses reflection to get column metadata and values.
     *
     * @param entity Entity to insert.
     * @param parameters Parameters to fill.
     * @return SQL Insert statement.
     */
    protected static String sqlInsert(Object entity, Map<String, Object> parameters) {
        StringBuilder sql = new StringBuilder();
        Class<?> type = entity.getClass();
        String table = tableName(type);

        sql.append("insert into ").append(table).append(" ");

        // columns
        sql.append(sqlInsertColumns(entity.getClass())).append(" ");

        sql.append("values ");

        // parameters
        sql.append(sqlInsertParameters(entity, parameters));
        return sql.toString();
    }

    /**
     * Generates a SQL update statement for the given entity and returns the parameter binding
     * using the parameter argument.
     *
     * NOTE: This method works for simple entity by reading {@link Column} annotation metadata.
     * This method is NOT intended to read/map complex data or relationships.
     *
     * NOTE: This method uses reflection to get column metadata and values.
     *
     * @param entity Entity to update.
     * @param parameters Parameters to fill.
     * @return SQL Update statement.
     */
    protected static String sqlUpdate(Object entity, Map<String, Object> parameters) {
        StringBuilder sql = new StringBuilder();
        Class<?> type = entity.getClass();
        String table = tableName(type);

        sql.append("update ").append(table).append(" ");

        // columns
        sql.append(sqlUpdateColumns(entity, parameters)).append(" ");

        sql.append("where id = :id");

        return sql.toString();
    }

    /**
     * Get table name from entity class. Class must be annotated with {@link Table} annotation.
     *
     * @param type Entity type.
     * @return Table's name.
     */
    protected static String tableName(Class<?> type) {
        try {
            return type.getAnnotation(Table.class).name();
        } catch (NullPointerException x) {
            throw new IllegalArgumentException("Missing " + Table.class.getName() + " annotation on " + type.getName());
        }
    }

    private static String sqlInsertParameters(Object entity, Map<String, Object> parameters) {
        StringJoiner sql = new StringJoiner(", ", "(", ")");
        withFields(entity.getClass(), field -> {
            if (isNotGenerated(field)) {
                sql.add(":" + field.getName());
                field.setAccessible(true);
                parameters.put(field.getName(), field.get(entity));
            }
        });
        return sql.toString();
    }

    private static boolean isNotGenerated(Field field) {
        return field.getAnnotation(GeneratedValue.class) == null;
    }

    private static String sqlInsertColumns(Class entity) {
        StringJoiner columns = new StringJoiner(", ", "(", ")");
        withFields(entity, field -> {
            // no Id for insert
            if (isNotGenerated(field)) {
                columns.add(columnName(field));
            }
        });
        return columns.toString();
    }

    private static String sqlUpdateColumns(Object entity, Map<String, Object> parameters) {
        StringJoiner columns = new StringJoiner(", ", "set ", "");
        withFields(entity.getClass(), field -> {
            if (isNotGenerated(field)) {
                columns.add(columnName(field) + "= :" + field.getName());
            }
            field.setAccessible(true);
            parameters.put(field.getName(), field.get(entity));
        });
        return columns.toString();
    }

    private static String columnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column == null || column.name().length() == 0) {
            return field.getName();
        }
        return column.name();
    }

    private static void withFields(Class type, Throwing.Consumer<Field> consumer) {
        Class<?> c = type;
        while (c != Object.class) {
            for (Field field : c.getDeclaredFields()) {
                if (field.getAnnotation(Column.class) != null) {
                    consumer.accept(field);
                } else if (field.getAnnotation(Id.class) != null) {
                    consumer.accept(field);
                }
            }
            c = c.getSuperclass();
        }
    }
}

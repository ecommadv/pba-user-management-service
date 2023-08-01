package com.pba.authservice.persistance.repository;

import com.pba.authservice.exceptions.AuthDaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.*;

public abstract class JdbcRepository<ObjectT, IdT> {
    private final RowMapper<ObjectT> rowMapper;
    private final SqlProvider sqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public JdbcRepository(RowMapper<ObjectT> rowMapper, SqlProvider sqlProvider, JdbcTemplate jdbcTemplate) {
        this.rowMapper = rowMapper;
        this.sqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<Object> extractAttributes(Object obj) {
        List<Field> fields = Arrays.stream(obj.getClass().getDeclaredFields()).toList();
        fields.forEach((field) -> field.setAccessible(true));
        List<Object> attributes = fields.stream()
                    .map((field) -> {
                            try {
                                return field.get(obj);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                    }).toList();
        List<Object> mutableAttributes = new ArrayList<>(attributes);
        mutableAttributes.remove(0); // remove id
        return mutableAttributes;
    }

    public ObjectT save(ObjectT obj, IdT id) {
        String sql = sqlProvider.insert();
        List<Object> attributes = extractAttributes(obj);
        jdbcTemplate.update(sql, attributes.toArray());
        return getById(id).get();
    }

    public Optional<ObjectT> getById(IdT id) {
        String sql = sqlProvider.selectById();
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public List<ObjectT> getAll() {
        String sql = sqlProvider.selectAll();
        return jdbcTemplate.query(sql, rowMapper);
    }

    public ObjectT deleteById(IdT id) throws AuthDaoException, IllegalAccessException {
        Optional<ObjectT> obj = getById(id);
        if (obj.isEmpty()) {
            throw new AuthDaoException(String.format("Object with id %s is not stored!", id.toString()));
        }
        String sql = sqlProvider.deleteById();
        jdbcTemplate.update(sql, id);
        return obj.get();
    }

    public ObjectT update(ObjectT obj, IdT id) throws AuthDaoException, IllegalAccessException {
        Optional<ObjectT> objFound = getById(id);
        if (objFound.isEmpty()) {
            throw new AuthDaoException(String.format("Object with id %s is not stored!", id.toString()));
        }
        String sql = sqlProvider.update();
        List<Object> args = extractAttributes(obj);
        args.add(id);
        jdbcTemplate.update(sql, args.toArray());
        return obj;
    }
}

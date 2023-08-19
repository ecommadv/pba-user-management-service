package com.pba.authservice.persistance.repository;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.exceptions.AuthDaoNotFoundException;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public abstract class JdbcRepository<ObjectT, IdT> {
    private final RowMapper<ObjectT> rowMapper;
    private final SqlProvider sqlProvider;
    private final JdbcTemplate jdbcTemplate;
    private final UtilsFactory utilsFactory;

    public JdbcRepository(RowMapper<ObjectT> rowMapper, SqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        this.rowMapper = rowMapper;
        this.sqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
        this.utilsFactory = utilsFactory;
    }

    public ObjectT save(ObjectT obj) {
        String sql = sqlProvider.insert();
        List<Object> attributes = extractAttributes(obj);
        KeyHolder keyHolder = utilsFactory.keyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            for (Object field : attributes) {
                ps.setObject(index++, field);
            }
            return ps;
        }, keyHolder);

        Map<String, Object> objectMap = keyHolder.getKeys();
        Object id = objectMap.get("id");
        return getById((IdT) id).get();
    }

    public Optional<ObjectT> getById(IdT id) {
        String sql = sqlProvider.selectById();
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public List<ObjectT> getAll() {
        String sql = sqlProvider.selectAll();
        return jdbcTemplate.query(sql, rowMapper);
    }

    public ObjectT deleteById(IdT id) throws AuthDaoException {
        Optional<ObjectT> obj = getById(id);
        String sql = sqlProvider.deleteById();
        int rowCount = jdbcTemplate.update(sql, id);
        if (rowCount == 0) {
            throw new AuthDaoNotFoundException(ErrorCodes.OBJ_NOT_FOUND, "Object not found");
        }
        return obj.get();
    }

    public ObjectT update(ObjectT obj, IdT id) throws AuthDaoException {
        String sql = sqlProvider.update();
        List<Object> args = extractAttributes(obj);
        args.add(id);
        int rowCount = jdbcTemplate.update(sql, args.toArray());
        if (rowCount == 0) {
            throw new AuthDaoNotFoundException(ErrorCodes.OBJ_NOT_FOUND, "Object not found");
        }
        return obj;
    }
    private List<Object> extractAttributes(Object obj) {
        List<Field> fields = Arrays.stream(obj.getClass().getDeclaredFields()).toList();
        fields.forEach((field) -> field.setAccessible(true));
        return fields.stream()
                .map((field) -> getFieldValue(field, obj))
                .skip(1)
                .collect(Collectors.toList());
    }

    private Object getFieldValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new AuthDaoException(ErrorCodes.FIELD_EXTRACTION, "Error when extracting field of object", HttpStatus.BAD_REQUEST);
        }
    }
}

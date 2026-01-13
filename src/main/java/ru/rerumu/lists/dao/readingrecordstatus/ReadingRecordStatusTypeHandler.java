package ru.rerumu.lists.dao.readingrecordstatus;

import com.jcabi.aspects.Loggable;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.readingrecordstatus.ReadingRecordStatuses;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@MappedTypes(ReadingRecordStatuses.class)
public class ReadingRecordStatusTypeHandler implements TypeHandler<ReadingRecordStatuses> {

    private final ReadingRecordStatusRepository readingRecordStatusRepository;

    @Autowired
    public ReadingRecordStatusTypeHandler(ReadingRecordStatusRepository readingRecordStatusRepository) {
        this.readingRecordStatusRepository = readingRecordStatusRepository;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, ReadingRecordStatuses parameter, JdbcType jdbcType) throws SQLException {
        Integer value;
        if (parameter != null) {
            value = Math.toIntExact(parameter.getId());
        } else {
            value = null;
        }
        ps.setObject(i, value);
    }

    @Override
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public ReadingRecordStatuses getResult(ResultSet rs, String columnName) throws SQLException {
        Integer value = rs.getObject(columnName, Integer.class);
        if (value != null) {
            return readingRecordStatusRepository.findById(Long.valueOf(value));
        } else {
            return null;
        }
    }

    @Override
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public ReadingRecordStatuses getResult(ResultSet rs, int columnIndex) throws SQLException {
        Integer value = rs.getObject(columnIndex, Integer.class);
        if (value != null) {
            return readingRecordStatusRepository.findById(Long.valueOf(value));
        } else {
            return null;
        }
    }

    @Override
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
    public ReadingRecordStatuses getResult(CallableStatement cs, int columnIndex) throws SQLException {
        Integer value = cs.getObject(columnIndex, Integer.class);
        if (value != null) {
            return readingRecordStatusRepository.findById(Long.valueOf(value));
        } else {
            return null;
        }
    }
}

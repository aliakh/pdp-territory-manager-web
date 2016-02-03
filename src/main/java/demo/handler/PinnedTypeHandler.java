package demo.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Type handler for 'pinned' region-zip relation.
 */
public class PinnedTypeHandler extends BaseTypeHandler<Boolean> {

    private static final String PINNED = "H";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
        throws SQLException {
        ps.setString(i, parameter ? PINNED : null);
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return convert(value);
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return convert(value);
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return convert(value);
    }

    private Boolean convert(String value) {
        if (value == null) {
            return false;
        } else if (value.equals(PINNED)) {
            return true;
        } else {
            throw new IllegalArgumentException(value);
        }
    }
}

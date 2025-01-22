package com.dlz.framework.db.convertor;

import com.dlz.framework.db.modal.result.ResultMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JdbcUtil
 */
public class JdbcUtil {

	public static ResultSet getResultSet(Connection connection,String sql,Object[] paras) throws SQLException {
        PreparedStatement pst=connection.prepareStatement(sql);
        if(paras!=null){
        	for (int i=0, size=paras.length; i<size; i++) {
    			Object value = paras[i];
    			if (value instanceof java.sql.Date) {
    				pst.setDate(i + 1, (java.sql.Date)value);
    			} else if (value instanceof java.sql.Timestamp) {
    				pst.setTimestamp(i + 1, (java.sql.Timestamp)value);
    			} else {
    				pst.setObject(i + 1, value);
    			}
    		}
		}
       return pst.executeQuery();
    }
	/**
	 * from jFinal
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static List<ResultMap> buildResultMapList(ResultSet rs) throws SQLException{
		List<ResultMap> result = new ArrayList<ResultMap>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		String[] labelNames = new String[columnCount + 1];
		int[] types = new int[columnCount + 1];
		buildLabelNamesAndTypes(rsmd, labelNames, types);
		while (rs.next()) {
			result.add(buildResultMap(rs,columnCount, labelNames, types));
		}
		return result;
	}
	
	/**
	 * from jFinal
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static ResultMap buildResultMap(ResultSet rs,int columnCount,String[] labelNames,int[] types) throws SQLException{
		ResultMap ar = new ResultMap();
		for (int i = 1; i <= columnCount; i++) {
			Object value;
			if (types[i] == Types.CLOB)
				value = handleClob(rs.getClob(i));
			else if (types[i] == Types.NCLOB)
				value = handleClob(rs.getNClob(i));
			else if (types[i] == Types.BLOB)
				value = handleBlob(rs.getBlob(i));
			else
				value = rs.getObject(i);
			ar.put(labelNames[i], value);
		}
		return ar;
	}
	
	
	/**
	 * from springJdbc
	 * @param rs
	 * @param index
	 * @return
	 * @throws SQLException
	 */
	public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}
		if (obj instanceof Blob) {
			Blob blob = (Blob) obj;
			obj = blob.getBytes(1, (int) blob.length());
		}
		else if (obj instanceof Clob) {
			Clob clob = (Clob) obj;
			obj = clob.getSubString(1, (int) clob.length());
		}
		else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
			obj = rs.getTimestamp(index);
		}
		else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(index);
			}
			else {
				obj = rs.getDate(index);
			}
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		return obj;
	}
	
	public static void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
		for (int i=1; i<labelNames.length; i++) {
//			labelNames[i] = CovertUtil.clumn2Str(rsmd.getColumnLabel(i));
			labelNames[i] = rsmd.getColumnLabel(i);
			types[i] = rsmd.getColumnType(i);
		}
	}
	
	public static byte[] handleBlob(Blob blob) throws SQLException {
		if (blob == null)
			return null;
		InputStream is = null;
		try {
			is = blob.getBinaryStream();
			if (is == null)
				return null;
			byte[] data = new byte[(int)blob.length()];		// byte[] data = new byte[is.available()];
			if (data.length == 0)
				return null;
			is.read(data);
			return data;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (is != null)
				try {is.close();} catch (IOException e) {throw new RuntimeException(e);}
		}
	}
	
	public static String handleClob(Clob clob) throws SQLException {
		if (clob == null)
			return null;
		
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			if (reader == null)
				return null;
			char[] buffer = new char[(int)clob.length()];
			if (buffer.length == 0)
				return null;
			reader.read(buffer);
			return new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			if (reader != null)
				try {reader.close();} catch (IOException e) {throw new RuntimeException(e);}
		}
	}
}
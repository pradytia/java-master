package model.Query;

import java.util.ArrayList;
import java.util.List;

public class mdlQueryTransaction {
	public String sql;
	public List<model.Query.mdlQueryExecute> listParam = new ArrayList<model.Query.mdlQueryExecute>();
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<model.Query.mdlQueryExecute> getListParam() {
		return listParam;
	}
	public void setListParam(List<model.Query.mdlQueryExecute> listParam) {
		this.listParam = listParam;
	}
	
}

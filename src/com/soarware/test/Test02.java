package com.soarware.test;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;

public class Test02 extends Object {
	static final String POOL_NAME = "Pool";
	JCO.Client mConnection;
	JCO.Repository mRepository;
	public Test02() {
		try {
			
			JCO.Pool pool = JCO.getClientPoolManager().getPool(POOL_NAME);
			if (pool == null) {
				//OrderedProperties logonProperties = OrderedProperties.load("/logon.properties");
				JCO.addClientPool(POOL_NAME, 10, "", "", "", "zf", "", "");
				/*
				JCO.addClientPool(POOL_NAME, // pool name
						5, // maximum number of connections
						logonProperties); // properties
				*/
			}
			mConnection = JCO.getClient(POOL_NAME);
			System.out.println(mConnection.getAttributes());
			mRepository = new JCO.Repository("SAPJCo", mConnection);
			
			JCO.Function function = null;
			JCO.Table codes = null;
			function = this.createFunction("ZBPM_CFC_OVERDUE");
			function = this.createFunction("ZBPM_CFC_OVERDUE");
			if (function == null) {
				System.out.println("ZBPM_CFC_OVERDUE" +
						" not found in SAP.");
				System.exit(1);
			}
			
			function.getImportParameterList().setValue("123",   "IM_KUNNR");
			mConnection.execute(function);
			System.out.println("ZBPM_CFC_OVERDUE result=" + function.getExportParameterList().getString("EX_MESSAGE") );
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			JCO.releaseClient(mConnection);
		}
	}
	
	public JCO.Function createFunction(String name) throws Exception {
		try {
			IFunctionTemplate ft =
					mRepository.getFunctionTemplate(name.toUpperCase());
			if (ft == null)
				return null;
			return ft.getFunction();
		}
		catch (Exception ex) {
			throw new Exception("Problem retrieving JCO.Function object.");
		}
	}
	
	public static void main (String args[]) {
		Test02 app = new Test02();
	}
}
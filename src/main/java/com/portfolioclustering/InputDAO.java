package com.portfolioclustering;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.StringTokenizer;

import au.com.bytecode.opencsv.CSVReader;


public class InputDAO {
	
	public List<InputDataModel> lstData;
	

	InputDAO(String csvFile)
	{
		lstData = new ArrayList<InputDataModel>();
		LoadCSV(csvFile);
		
	}
	
	//Read CSV and Load into the List
	public void LoadCSV(String csvFile)
	{
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(csvFile),',','"',1);
			List<?> content;
			content = csvReader.readAll();
			 
			String[] row = null;
			
			for (Object object : content) {
			    row = (String[]) object;
			     
			    //Create DataModel
				InputDataModel dataModel = new InputDataModel();
				dataModel.customerID = Long.parseLong(row[Constants.CUSTOMERID]);
				dataModel.portfoliID = Long.parseLong(row[Constants.PORTFOLIOID]);
				dataModel.fundName = row[Constants.FUNDNAME];
				dataModel.fundFamily = ""; //sLines[Constants.];
				dataModel.ticker = row[Constants.TICKER];
				dataModel.fundCategory = row[Constants.FUNDCATEGORY];
				dataModel.instrumentType = row[Constants.INSTRUMENTTYPE];
				
				//Add it to the list
				lstData.add(dataModel);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//Update the Tokens with Instrument Type eg: Technology_Stock
	public void UpdateTokensWithInstrumentType()
	{
		for(InputDataModel data:lstData)
		{
			//Tokenize Fund Category
			StringTokenizer st ;
			String token ="";
			st= new StringTokenizer(data.fundCategory);
			String instrumentTypeAppended ="";
		     while (st.hasMoreTokens()) {
		    	 token = st.nextToken();
		    	 if (token.trim() != "")
		    		 instrumentTypeAppended = instrumentTypeAppended + token+"_"+data.instrumentType + " ";
	         
		     }
		     data.fundCategory = instrumentTypeAppended.trim();

		     //Tokenize Fund Name
		     st= new StringTokenizer(data.fundName);
		     instrumentTypeAppended ="";
			     while (st.hasMoreTokens()) {
			    	 token = st.nextToken();
			    	 if (token.trim() != "")
			    		 instrumentTypeAppended = instrumentTypeAppended + token+"_"+data.instrumentType + " ";
		         
			     }
			 data.fundName = instrumentTypeAppended.trim();
			
		}
	}
}

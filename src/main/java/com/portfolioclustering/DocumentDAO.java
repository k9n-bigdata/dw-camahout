package com.portfolioclustering;

import java.util.ArrayList;
import java.util.List;

//Class to Store the List of Documents
public class DocumentDAO {
	
	public List<DocumentDataModel> lstData;
	
	DocumentDAO()
	{
		lstData = new ArrayList<DocumentDataModel>();
	}
	
	public void addDocument(DocumentDataModel document)
	{
		lstData.add(document);
	}

}

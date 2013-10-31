package com.portfolioclustering;

//Data Structure of the Documents
public class DocumentDataModel {

	public String documentName;
	public String documentText;
	
	DocumentDataModel(String _documentName)
	{
		documentName = _documentName;
		documentText = "";
	}
	
	public void Append(String _documentText)
	{
		if (_documentText != null && _documentText.trim() != "")
			documentText = documentText + " " + _documentText;
		
	}
	
}

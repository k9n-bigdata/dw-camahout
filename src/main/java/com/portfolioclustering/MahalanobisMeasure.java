package com.portfolioclustering;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;


public class MahalanobisMeasure {
		public static void main(String[] args) throws Exception{
			
			String inputPath = args[0];
			
			String outputPath = args[1];
			
			//Set the InputFile Path
			InputDAO inputdata = new InputDAO(inputPath+Config.csvPath());
			
			DocumentDAO docDAO = new DocumentDAO();
			
			//Load the Document DAO
			for( InputDataModel ipdataModel : inputdata.lstData)
			{
				String documentName;
				//Create unique document Name
				documentName = ipdataModel.customerID+ "_" + ipdataModel.portfoliID+ "_"+ ipdataModel.ticker;
				
				//Build the document
				DocumentDataModel dm = new DocumentDataModel(documentName);
				dm.Append(ipdataModel.fundName);
				dm.Append(ipdataModel.fundCategory);
				docDAO.addDocument(dm);
			}
			
			//Compute Term Frequency
			TermFrequency tf = new TermFrequency(docDAO);
			Map<String,Vector> tfVector = tf.ComputeTermFrequency();
			
			
			Util.WriteStringVectorToFile(outputPath+Config.GetMDTermDocPath(), tfVector);
			Util.WriteStringIntegerToFile(outputPath+Config.GetMDDictPath(), tf.GetDictionary());
			
			Map<String,Vector> tfCompressedVector = new TreeMap<String,Vector>();
			
			Iterator<Entry<String, Vector>> it = tfVector.entrySet().iterator();
			String prevKey="";
			
			Vector compressedVector = new SequentialAccessSparseVector();
			
		    while (it.hasNext()) {
		        Entry<String, Vector> pairs = it.next();
		        
		        String key = pairs.getKey().toString();
		        String[] parts = key.split("_", 3);
		        String newKey= parts[0]+"_"+parts[1];
		        
		        if (!newKey.equals(prevKey))
		        {
		        	//Compressed Vector
		        	tfCompressedVector.put(prevKey, compressedVector);
		        	
		        	//Reset : Make it Zero vector for next iteration
		        	compressedVector = ((Vector)pairs.getValue()).like();
		        	
		        	prevKey = newKey;
		        }
		        
		        compressedVector = MathHelper.Combine(((Vector)pairs.getValue()), compressedVector);
		        
		    }
		    
		    //Add the Last one
		    if (!tfCompressedVector.containsKey(prevKey) && prevKey!="")
		    	tfCompressedVector.put(prevKey, compressedVector);

		    //Remove the invalid record added first
		    tfCompressedVector.remove("");
		    
		    Util.WriteStringVectorToFile(outputPath+Config.GetMDCompressedTermDocPath(), tfCompressedVector);
		    
		    //Loop thru the Compressed Vector and Create a Sparse Matrix
		    
		    /*Matrix compMatrix = new SparseMatrix( tfCompressedVector.size(), compressedVector.size() );
		    int index=0;
		    
		    it = tfCompressedVector.entrySet().iterator();
		    while (it.hasNext()) {
		    	Entry<String, Vector> pairs = it.next();
			    Vector value = ((Vector)pairs.getValue());
			    compMatrix.assignRow(index, value);
			    index++;
			    
		    }*/
		   
		    //SingularValueDecomposition svd = new SingularValueDecomposition(compMatrix);
		    //Matrix coVarMatrix = svd.getCovariance(0.0);
		    //Vector meanVector = Util.computeMeanColumn(compMatrix);
		    //Matrix diffMatrix = Util.computeDiffMatrix(compMatrix, meanVector);
		    //System.out.println(diffMatrix.asFormatString());			
			
		}
}


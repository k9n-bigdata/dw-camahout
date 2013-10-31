package com.portfolioclustering;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;


public class CityBlockMeasure {
	
//    static Logger logger = Logger.getLogger(CityBlockMeasure.class);
	
		public static void main(String[] args) throws Exception{
			
	        /*if(logger.isDebugEnabled()){
	            logger.debug("Testing");
	         }*/
			
			String inputPath = args[0];
			
			String outputPath = args[1];
			
			InputDAO inputdata = new InputDAO(inputPath+Config.csvPath());
			
			
			inputdata.UpdateTokensWithInstrumentType();
			
			DocumentDAO docDAO = new DocumentDAO();
					
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
			
			TermFrequency tf = new TermFrequency(docDAO);
			Map<String,Vector> tfVector = tf.ComputeTermFrequency();
			
			Util.WriteStringVectorToFile(outputPath+Config.GetCBTermDocPath(), tfVector);
			Util.WriteStringIntegerToFile(outputPath+Config.GetCBDictPath(), tf.GetDictionary());
			
			
			Map<String,Vector> tfCompressedVector = new TreeMap<String,Vector>();
			
			Iterator<Entry<String, Vector>> it = tfVector.entrySet().iterator();
			String prevKey="";
			
			Vector compressedVector = new SequentialAccessSparseVector();
			
		    while (it.hasNext()) {
		    	Entry<String, Vector> pairs = it.next();
		        
		        String key = pairs.getKey().toString();

		        //Extract Customer ID and Portfolio ID to form new key
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
		    
		    if (!tfCompressedVector.containsKey(prevKey) && prevKey!="")
		    	tfCompressedVector.put(prevKey, compressedVector);
	
		    tfCompressedVector.remove("");
		    
		    Util.WriteStringVectorToFile(outputPath+Config.GetCBCompressedTermDocPath(), tfCompressedVector);
		    
		    //Compute City Block Distance
		    /*List<String> distanceResult = new ArrayList<String>();
		    ManhattanDistanceMeasure distanceMeasure = new ManhattanDistanceMeasure();
			
			for(String keyi:tfCompressedVector.keySet()){
				for(String keyj:tfCompressedVector.keySet()){
				Double dValue = distanceMeasure.distance(tfCompressedVector.get(keyi),tfCompressedVector.get(keyj));
				distanceResult.add(keyi + ":" + keyj + ":" + dValue);
				}
			}*/
			
			//Util.WriteListToFile(Config.GetCBDistancePath(), distanceResult);
			
		}
}


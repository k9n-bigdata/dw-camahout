package com.portfolioclustering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.DocumentProcessor;
import org.apache.mahout.vectorizer.common.PartialVectorMerger;

public class TermFrequency {

	public DocumentDAO docDAO;
	public Configuration config;
	public FileSystem fs;
	public List<Integer> stopIndex;
	
    static String outputDir = "output/";
    
    TermFrequency(DocumentDAO _docDAO) throws IOException
    {
    	docDAO = _docDAO;
    	config = new Configuration();
    	fs = FileSystem.get(config);
    	stopIndex = new ArrayList<Integer>();
    }
    
    /* Compute Term Frequency for the Documents in DocumentDAO */
    public Map<String,Vector> ComputeTermFrequency() throws Exception
    {
    	
    	//Set the Output Path
        Path sequencePath = new Path(outputDir, "sequence");
        Path tokenizedPath = new Path(outputDir, DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER );
 
        //Tokenize the document
        tokenizeDocuments(sequencePath, tokenizedPath);
        
        //Once Tokenized, Identify the Term Frequency.
        processTf(tokenizedPath);        
        
        //Term Frequency is created in Sequencefile Format by Mahout
        //Convert it to Vector
        Map<String,Vector> tfVector = ConvertTermFrequencyFileToVector();
        

        return tfVector;
        
    }
    
    public void UpdateStopKeys() throws Exception
    {
    	//Add null
    	Map<String,Integer> dictMap = GetDictionary();
    	stopIndex.add(dictMap.get("null"));
    }
    
    /* Get Dictionary File */
    /* This should be called after ComputeTermFrequenct\y */
    public Map<String,Integer> GetDictionary() throws Exception
    {
        //Dictionary File is created in Sequencefile format
    	//Convert it to Vector 
        Map<String,Integer> dictMap = LoadDictionaryToVector();
        return dictMap;
    }
    
    /* Convert Term Frequency File to Map */
    public Map<String,Vector> ConvertTermFrequencyFileToVector() 
            throws Exception {
    	
    	//Set TF sequence File Path
        Path TermFrequencyDir = new Path( outputDir, "tf-vectors");
        Path TermFrequencyFile = new Path(TermFrequencyDir, "part-r-00000");
        
        //Read the Sequence File 
        SequenceFile.Reader reader = new SequenceFile.Reader(fs,
        		TermFrequencyFile, config);
        
        //Tree Map used to Sort on the key
        Map<String,Vector> map = new TreeMap<String, Vector>();
        
        //Loop thru the Sequence file and load into Tree Map
        Text key = new Text();
        Writable value = new VectorWritable();
        while (reader.next(key, value)) {
        	Vector v = ((VectorWritable)value).get();
        	for(Integer index:stopIndex)
        		v.set(index,0.0);
        	map.put(key.toString(),v) ;
        }
        
        reader.close();
        
        return map;
    }
    
    /* Convert Dictionary File to Map */
    public  Map<String,Integer> LoadDictionaryToVector() 
            throws Exception {
	  
    	//Set Dictionary sequence File Path
    	Path dictionaryPath = new Path(outputDir, "dictionary.file-0");
    	
    	//Read the Sequence File
    	SequenceFile.Reader reader = new SequenceFile.Reader(fs,
                 dictionaryPath, config);
    	
    	//Tree Map used to Sort on the key
        Map<String,Integer> map = new TreeMap<String, Integer>();
        
       //Loop thru the Sequence file and load into Tree Map
        Text key = new Text();
        Writable value = new IntWritable();
        while (reader.next(key, value)) {
         	map.put(key.toString(), ((IntWritable)value).get());
         }
        
        reader.close();
        
        return map;
	  }
	  
    /* Tokenize the Document */
    /* sequencePath  : Path to Document sequence file */
    /* tokenizedPath : Path to tokenized output */
	public void tokenizeDocuments(Path sequencePath, Path tokenizedPath)
            throws Exception {
 
		//Create a Sequence file to write the documents
        SequenceFile.Writer writer = new SequenceFile.Writer(fs, config, sequencePath, Text.class, Text.class);
 
        //Write the document to Sequence file
        for(DocumentDataModel doc: docDAO.lstData)
        	writer.append(new Text(doc.documentName), new Text(doc.documentText));
        
        //Close the Writer
        writer.close();
 
        //Call Mahout tokenizer to tokenize the documents.
        DocumentProcessor.tokenizeDocuments(sequencePath, StandardAnalyzer.class, tokenizedPath, config);
    }
	
	/* Read the tokenized data and create Term Frequency Sequence File */
	/* tokenizedPath : Path to tokenized file */
	public void processTf(Path tokenizedPath) 
            throws Exception {
        boolean sequential    = false;
        boolean named         = false;
 
        Path wordCount = new Path( outputDir );
 
        // The tokenized documents must be in SequenceFile format
        DictionaryVectorizer.createTermFrequencyVectors(tokenizedPath,
                wordCount,
                DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER,
                config,
                1,                // the minimum frequency of the feature in the entire corpus to be considered for inclusion in the sparse vector
                1,                // maxNGramSize 1 = unigram, 2 = unigram and bigram, 3 = unigram, bigram and trigram
                0.0f,             // minValue of log likelihood ratio to used to prune ngrams
                PartialVectorMerger.NO_NORMALIZING,    // normPower L_p norm to be computed.
                false,            // whether to use log normalization
                1,                // numReducers
                100,              // chunkSizeInMegabytes
                sequential,        
                named);
 
    }
	
}

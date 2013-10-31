package com.portfolioclustering;

import org.apache.mahout.math.Vector;
import org.apache.mahout.math.function.DoubleDoubleFunction;

public class MathHelper {

	// Compress Vectors to form a binary matrix
	public static Vector Combine(Vector v1, Vector v2)
	{
		
		Vector v3;
		
		//Return 1 if any of the two vector has value for a particular index
		//else Return 0
		v3 = v1.assign(v2, new DoubleDoubleFunction() {
			           @Override
			            public double apply(double arg1, double arg2) {
			        	    if ( arg1 > 0 || arg2 > 0)
			        	    	return 1;
			        	    else
			        	    	return 0;
			            }
			        });
		 
		return v3;
		
	}
		
	/*
	 public static Vector computeMeanColumn(Matrix m) {
         int rowSize = m.rowSize();
         Vector meanVector = new DenseVector(m.rowSize());

         for(int i = 0 ; i < rowSize ; i++) {
                 int sum = 0;
                 for(int j = 0 ; j < m.columnSize() ; j++) {
                         sum += m.get(i, j);
                 }
                 meanVector.set(i, ( sum * 1.0) / m.columnSize());
                 
         }
         return meanVector;                
	 }
	 
	 
	 public static Matrix computeDiffMatrix(Matrix m, Vector meanVector) {
         int rowCount = m.rowSize();
         int columnCount = m.columnSize();
         
         Matrix diffMatrix = new DenseMatrix(rowCount,columnCount);
         for(int i = 0 ; i < rowCount ; i++) {
                 for(int j = 0 ; j < columnCount ; j++) {
                	 diffMatrix.set(i, j, m.get(i, j) - meanVector.get(i));
                 }
         }
         
         return diffMatrix;
	 }	*/
}

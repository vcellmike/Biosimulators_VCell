/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.plot;
import java.awt.geom.Point2D;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class PlotData implements java.io.Serializable {
	private double[] independent = null;
	private double[] dependent = null;
	private double independentMin;
	private double independentMax;
	private double dependentMin;
	private double dependentMax;
	private boolean bInvalidNumericValues = false;

/**
 * This method was created by a SmartGuide.
 * @param independent double[]
 * @param dependent double[]
 * @exception java.lang.Exception The exception description.
 */
public PlotData (double independent[], double dependent[]) {
	if (independent.length != dependent.length){
		throw new IllegalArgumentException("arrays must have same length");
	}	
	this.independent = independent;
	this.dependent = dependent;
	refreshStatistics();
}


/**
 * This method was created by a SmartGuide.
 * @return double[]
 */
public double[] getDependent() {
	return dependent;
}


/**
 * This method was created by a SmartGuide.
 * @return double
 */
public double getDependentMax() {
	return dependentMax;
}


/**
 * This method was created by a SmartGuide.
 * @return double
 */
public double getDependentMin() {
	return dependentMin;
}


/**
 * This method was created by a SmartGuide.
 * @return double[]
 */
public double[] getIndependent() {
	return independent;
}


/**
 * This method was created by a SmartGuide.
 * @return double
 */
public double getIndependentMax() {
	return independentMax;
}


/**
 * This method was created by a SmartGuide.
 * @return double
 */
public double getIndependentMin() {
	return independentMin;
}


/**
 * This method was created in VisualAge.
 * @return double
 * @param sampleX double
 */
public int getNearestIndex(double sampleX) {
	double currError = 10e10;
	int currIndex = 0;
	
	for (int i=0;i<independent.length;i++){
		double error = Math.abs(sampleX-independent[i]);
		if (error<currError){
			currIndex=i;
			currError = error;
		}
	}
	return currIndex;
}


/**
 * This method was created in VisualAge.
 * @return double
 * @param sampleX double
 */
public int getNearestIndex(double sampleX, double sampleY) {
	double currDistanceSquared = 10e50;
	int currIndex = 0;
	double scaleX = getIndependentMax()-getIndependentMin();
	if (scaleX==0){
		scaleX = 1.0;
	}
	double scaleY = getDependentMax()-getDependentMin();
	if (scaleY==0){
		scaleY = 1.0;
	}
	for (int i=0;i<independent.length;i++){
		double distanceX = (sampleX-independent[i])/scaleX;
		double distanceY = (sampleY-dependent[i])/scaleY;
		double distanceSquared = distanceX*distanceX + distanceY*distanceY;
		if (distanceSquared<currDistanceSquared){
			currIndex=i;
			currDistanceSquared = distanceSquared;
		}
	}
//System.out.println("scale=("+scaleX+","+scaleY+")   closes point to ("+sampleX+","+sampleY+") is ("+independent[currIndex]+","+dependent[currIndex]+")");
	return currIndex;
}


/**
 * This method was created in VisualAge.
 * @return double
 * @param sampleX double
 */
public int getNearestIndexOnScreen(double sampleX, double sampleY, double scaleX, double scaleY) {
	double currDistanceSquared = 10e50;
	int currIndex = 0;
	for (int i=0;i<independent.length;i++){
		double distanceX = (sampleX-independent[i])/scaleX;
		double distanceY = (sampleY-dependent[i])/scaleY;
		double distanceSquared = distanceX*distanceX + distanceY*distanceY;
		if (distanceSquared<currDistanceSquared){
			currIndex=i;
			currDistanceSquared = distanceSquared;
		}
	}
//System.out.println("scale=("+scaleX+","+scaleY+")   closes point to ("+sampleX+","+sampleY+") is ("+independent[currIndex]+","+dependent[currIndex]+")");
	return currIndex;
}


/**
 * This method was created by a SmartGuide.
 * @return Point2D.Double[]
 */
public Point2D.Double[] getPoints() {
	Point2D.Double points[] = new Point2D.Double[independent.length];
	for (int i=0;i<independent.length;i++) {
		points[i] = new Point2D.Double(independent[i], dependent[i]);
	}
	return points;
}


/**
 * This method was created by a SmartGuide.
 * @return int
 */
public int getSize() {
	return dependent.length;
}


/**
 * Insert the method's description here.
 * Creation date: (5/23/2005 2:34:22 PM)
 */
public boolean hasInvalidNumericValues() {

	return bInvalidNumericValues;
}


/**
 * This method was created in VisualAge.
 */
private void refreshStatistics() {
	//
	//Calculate min,max using only valid numeric values
	//set flag if Invalid numeric values exist
	//
	independentMax = -10e10;
	independentMin =  10e10;
	for (int i=0;i<independent.length;i++){
		if(Double.isInfinite(independent[i]) || Double.isNaN(independent[i])){
			bInvalidNumericValues = true;
		}else{
			independentMax = Math.max(independentMax,independent[i]);
			independentMin = Math.min(independentMin,independent[i]);
		}
	}	
	dependentMax = -10e10;
	dependentMin =  10e10;
	for (int i=0;i<dependent.length;i++){
		if(Double.isInfinite(dependent[i]) || Double.isNaN(dependent[i])){
			bInvalidNumericValues = true;
		}else{
			dependentMax = Math.max(dependentMax,dependent[i]);
			dependentMin = Math.min(dependentMin,dependent[i]);
		}
	}	
}


/**
 * This method was created in VisualAge.
 * @return java.lang.String
 */
public String toString() {
	
	StringBuffer buffer = new StringBuffer();
	
	for (int i=0;i<independent.length;i++){
		buffer.append(independent[i]+"\t"+dependent[i]+"\n");
	}	

	return buffer.toString();
}
}
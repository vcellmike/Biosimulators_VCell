/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.simdata.gui;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.vcell.util.ClientTaskStatusSupport;
import org.vcell.util.Coordinate;
import org.vcell.util.CoordinateIndex;
import org.vcell.util.UserCancelException;

import cbit.image.ImageException;
import cbit.vcell.geometry.Line;
import cbit.vcell.geometry.RegionImage;
import cbit.vcell.geometry.SampledCurve;
import cbit.vcell.solvers.CartesianMesh;
import cbit.vcell.solvers.CartesianMeshChombo;
import cbit.vcell.solvers.CartesianMeshChombo.Segment2D;
import cbit.vcell.solvers.ContourElement;
import cbit.vcell.solvers.MembraneElement;

/**
 * This class was generated by a SmartGuide.
 * 
 */
public class MeshDisplayAdapter {
	public static final int ORDER_PREPEND = 0x01;
	public static final int ORDER_P0 = 0x02;
	private CartesianMesh mesh = null;

	private class ConstructCurveHelper{
		MembraneElement firstInCurve;
		SampledCurve curve;
		MembraneElement currentMembraneElement;
		MembraneElement connectedMembraneElement;
		boolean bPrepend;
		boolean[] bMembraneElementChecked;
		int normalAxis;
		int slice;
		Vector<Integer> resolvedMembraneIndexes;

		ConstructCurveHelper(
			MembraneElement firstInCurve,
			SampledCurve curve, 
			MembraneElement currentMembraneElement, 
			MembraneElement connectedMembraneElement, 
			boolean bPrepend, 
			boolean[] bMembraneElementChecked, 
			int normalAxis, 
			int slice, 
			Vector<Integer> resolvedMembraneIndexes) {
				this.firstInCurve = firstInCurve;
				this.curve = curve;
				this.currentMembraneElement = currentMembraneElement;
				this.connectedMembraneElement = connectedMembraneElement;
				this.bPrepend = bPrepend;
				this.bMembraneElementChecked = bMembraneElementChecked;
				this.normalAxis =normalAxis;
				this.slice = slice;
				this.resolvedMembraneIndexes = resolvedMembraneIndexes;
		}

	};
	private class ParamHolder{
		int validMembraneSegmentCount;
		int visibleMembraneCount;
		int unCheckedMembraneCount;
		MembraneElement membraneElements[];
		MembraneElement[] membraneElementsValid ;
		boolean visibleNeighborConnectFirstInCurve;
	};

	public static class MeshRegionSurfaces {
		private cbit.vcell.geometry.surface.SurfaceCollection allMembraneSurfaces;
		private int[][] surface_polygon_MembraneIndexes;
		
		public MeshRegionSurfaces(cbit.vcell.geometry.surface.SurfaceCollection argSurfCollection,int[][] argSPMembraneIndexes){
			
			allMembraneSurfaces = argSurfCollection;
			surface_polygon_MembraneIndexes = argSPMembraneIndexes;
		}
		public cbit.vcell.geometry.surface.SurfaceCollection getSurfaceCollection(){
			return allMembraneSurfaces;
		}
		public int[][] getSurfaceMembraneIndexes(){
			return surface_polygon_MembraneIndexes;
		}
		public int getMembraneIndexForPolygon(int surfaceIndex,int polygonIndex){
			return surface_polygon_MembraneIndexes[surfaceIndex][polygonIndex];
		}
	};

/**
 * This method was created by a SmartGuide.
 */
public MeshDisplayAdapter (cbit.vcell.solvers.CartesianMesh argCartesianMesh) {
	this.mesh = argCartesianMesh;
}


/**
 * Insert the method's description here.
 * Creation date: (8/31/00 12:07:40 PM)
 * @return boolean
 * @param from cbit.vcell.solvers.MembraneElement
 * @param to cbit.vcell.solvers.MembraneElement
 */
private void addPointToCurve(SampledCurve curve, boolean bPrepend, Line toProjection, int order) {
	if (bPrepend) {
		if ((order & ORDER_P0) != 0) {
			curve.prependControlPoint(toProjection.getBeginningCoordinate());
		} else {
			curve.prependControlPoint(toProjection.getEndingCoordinate());
		}
	} else {
		if ((order & ORDER_P0) != 0) {
			curve.appendControlPoint(toProjection.getBeginningCoordinate());
		} else {
			curve.appendControlPoint(toProjection.getEndingCoordinate());
		}
	}
}


/**
 * Insert the method's description here.
 * Creation date: (8/31/00 12:07:40 PM)
 * @return boolean
 * @param from cbit.vcell.solvers.MembraneElement
 * @param to cbit.vcell.solvers.MembraneElement
 */
private void addSegmentToCurve(SampledCurve curve, MembraneElement from, MembraneElement to, int normalAxis, boolean bPrepend,Vector<Integer> resolvedMembraneIndexes) {
	Line toProjection = getProjectedSegment(to, normalAxis);
	if (from == null) {
		addPointToCurve(curve, bPrepend, toProjection, ORDER_P0);
		addPointToCurve(curve, bPrepend, toProjection, ORDER_PREPEND);
	} else {
		int order = determineOrder(from, to, normalAxis);
		addPointToCurve(curve, bPrepend, toProjection, order);
	}
	//
	// add membrane index for this segment
	//
	if (bPrepend && resolvedMembraneIndexes.size()>0) {
		resolvedMembraneIndexes.add(0, new Integer(to.getMembraneIndex()));
	} else {
		resolvedMembraneIndexes.add(new Integer(to.getMembraneIndex()));
	}
}


/**
 * Insert the method's description here.
 * Creation date: (8/30/00 6:16:35 PM)
 * @return cbit.vcell.geometry.SampledCurve
 */
private ConstructCurveHelper[] constructCurve(ConstructCurveHelper cch){

	if (cch.bMembraneElementChecked[cch.currentMembraneElement.getMembraneIndex()] == true) {
		return null;
	}
	
	ParamHolder paramHolder = doCheck(cch.currentMembraneElement,cch.normalAxis,cch.slice,cch.firstInCurve,mesh,cch.bMembraneElementChecked);

	if (paramHolder.validMembraneSegmentCount == 0) {
		if ((paramHolder.visibleMembraneCount == 2) && paramHolder.visibleNeighborConnectFirstInCurve) {
			//
			// Traversing membranes, Head caught up with tail
			//
			cch.curve.setClosed(true);
			//
			// add last membrane index for this segment of a closed curve (note: don't add coordinates, they are already there)
			//
			cch.resolvedMembraneIndexes.add(new Integer(cch.currentMembraneElement.getMembraneIndex()));
		} else {
			//otherwise we must be at an edge
			addSegmentToCurve(cch.curve, cch.connectedMembraneElement, cch.currentMembraneElement, cch.normalAxis, cch.bPrepend,cch.resolvedMembraneIndexes);
		}
		return null;
	}
	//
	addSegmentToCurve(cch.curve, cch.connectedMembraneElement, cch.currentMembraneElement, cch.normalAxis, cch.bPrepend,cch.resolvedMembraneIndexes);
	//
	if (paramHolder.validMembraneSegmentCount == 1) {
		if(cch.connectedMembraneElement == null){
			//
			// beginning a new "open" curve on the edge of the world
			//
			boolean bStartPrepend = ((ORDER_PREPEND & determineOrder(cch.currentMembraneElement, paramHolder.membraneElementsValid[0], cch.normalAxis)) != 0);
			return new ConstructCurveHelper[] {new ConstructCurveHelper(cch.firstInCurve,cch.curve, paramHolder.membraneElementsValid[0], cch.currentMembraneElement, bStartPrepend, cch.bMembraneElementChecked, cch.normalAxis, cch.slice,cch.resolvedMembraneIndexes)};
		}else{
			//
			// traversing normally (only one neighbor ahead ... neighbor behind already checked).
			//
			return new ConstructCurveHelper[] {new ConstructCurveHelper(cch.firstInCurve,cch.curve, paramHolder.membraneElementsValid[0], cch.currentMembraneElement, cch.bPrepend, cch.bMembraneElementChecked, cch.normalAxis, cch.slice,cch.resolvedMembraneIndexes)};
		}
	}
	if (paramHolder.validMembraneSegmentCount == 2) { 
		//
		// We are beginning a new curve in the middle of a curve
		//
		boolean bStartPrepend = ((ORDER_PREPEND & determineOrder(cch.currentMembraneElement, paramHolder.membraneElementsValid[0], cch.normalAxis)) != 0);
		return new ConstructCurveHelper[] {
			new ConstructCurveHelper(cch.firstInCurve,cch.curve, paramHolder.membraneElementsValid[0], cch.currentMembraneElement, bStartPrepend, cch.bMembraneElementChecked, cch.normalAxis, cch.slice,cch.resolvedMembraneIndexes),
			new ConstructCurveHelper(cch.firstInCurve,cch.curve, paramHolder.membraneElementsValid[1], cch.currentMembraneElement, !bStartPrepend, cch.bMembraneElementChecked, cch.normalAxis, cch.slice,cch.resolvedMembraneIndexes)
		};
	}
	throw new RuntimeException("Error: Unexpected number of Visible, Unchecked Membrane neighbors in slice");
}


/**
 * Insert the method's description here.
 * Creation date: (9/2/00 4:44:31 PM)
 * @return boolean
 */
private int determineOrder(MembraneElement from, MembraneElement to, int normalAxis) {

	CoordinateIndex ciFrom = null;
	CoordinateIndex ciTo = null;
	if (from.getOutsideVolumeIndex() == to.getOutsideVolumeIndex()) { //Inside Corner
		ciFrom = mesh.getCoordinateIndexFromVolumeIndex(from.getInsideVolumeIndex());
		ciTo = mesh.getCoordinateIndexFromVolumeIndex(to.getInsideVolumeIndex());
	} else if (from.getInsideVolumeIndex() == to.getInsideVolumeIndex()) { //Outside Corner
		ciFrom = mesh.getCoordinateIndexFromVolumeIndex(from.getOutsideVolumeIndex());
		ciTo = mesh.getCoordinateIndexFromVolumeIndex(to.getOutsideVolumeIndex());
	} else { //Edge, can be either inside or outside, doesn't matter
		ciFrom = mesh.getCoordinateIndexFromVolumeIndex(from.getInsideVolumeIndex());
		ciTo = mesh.getCoordinateIndexFromVolumeIndex(to.getInsideVolumeIndex());
	}
	int[] deltaXYZ = new int[3];
	deltaXYZ[Coordinate.X_AXIS] = ciTo.x - ciFrom.x;
	deltaXYZ[Coordinate.Y_AXIS] = ciTo.y - ciFrom.y;
	deltaXYZ[Coordinate.Z_AXIS] = ciTo.z - ciFrom.z;
	int toMembranePlane = getParalellAxis(to, normalAxis);
	boolean bP0 = (deltaXYZ[toMembranePlane] < 0);
	int fromMembranePlane = getParalellAxis(from, normalAxis);
	boolean bPrepend = (deltaXYZ[fromMembranePlane] < 0);
	return (0x00 | (bPrepend ? ORDER_PREPEND : 0) | (bP0 ? ORDER_P0 : 0));
}


/**
 * Insert the method's description here.
 * Creation date: (8/30/00 6:16:35 PM)
 * @return cbit.vcell.geometry.SampledCurve
 */
private ParamHolder doCheck(MembraneElement currentMembraneElement,int normalAxis,int slice,MembraneElement firstInCurve,cbit.vcell.solvers.CartesianMesh mesh,boolean[] bMembraneElementChecked) {

	ParamHolder paramHolder = new ParamHolder();
	bMembraneElementChecked[currentMembraneElement.getMembraneIndex()] = true;
	paramHolder.validMembraneSegmentCount = 0;
	paramHolder.visibleMembraneCount = 0;
	paramHolder.unCheckedMembraneCount = 0;
	paramHolder.membraneElements = mesh.getMembraneElements();
	paramHolder.membraneElementsValid = new MembraneElement[4]; //4 neighbors possible
	paramHolder.visibleNeighborConnectFirstInCurve = false;
	for (int c = 0; c < currentMembraneElement.getMembraneNeighborIndexes().length; c += 1) {
		MembraneElement membraneElementNeighbor = paramHolder.membraneElements[currentMembraneElement.getMembraneNeighborIndexes()[c]];
		boolean bChecked = bMembraneElementChecked[currentMembraneElement.getMembraneNeighborIndexes()[c]];
		if (!bChecked) {
			paramHolder.unCheckedMembraneCount += 1;
		}
		boolean bVisible = isMembraneElementVisible(membraneElementNeighbor, normalAxis, slice);
		if (bVisible) {
			paramHolder.visibleNeighborConnectFirstInCurve = (paramHolder.visibleNeighborConnectFirstInCurve || (membraneElementNeighbor == firstInCurve));
			paramHolder.visibleMembraneCount += 1;
		}
		if (!bChecked && bVisible) {
			paramHolder.membraneElementsValid[paramHolder.validMembraneSegmentCount] = membraneElementNeighbor;
			paramHolder.validMembraneSegmentCount += 1;
		}
	}

	return paramHolder;
}

public static RegionImage generateRegionImage(CartesianMesh mesh,ClientTaskStatusSupport clientTaskStatusSupport) throws ImageException,UserCancelException{
	int[] subVolumeIDField = new int[mesh.getNumVolumeElements()];
	for(int i=0;i< subVolumeIDField.length;i+= 1){
		subVolumeIDField[i] = mesh.getSubVolumeFromVolumeIndex(i);
	}
	
	RegionImage meshRegionImage =
		new RegionImage(
			new cbit.image.VCImageUncompressed(
				null,
				subVolumeIDField,
				mesh.getExtent(),
				mesh.getSizeX(),mesh.getSizeY(),mesh.getSizeZ()
			),
			mesh.getGeometryDimension(),mesh.getExtent(),mesh.getOrigin(),RegionImage.NO_SMOOTHING,
			clientTaskStatusSupport
	);
	if(clientTaskStatusSupport != null && clientTaskStatusSupport.isInterrupted()){
		throw UserCancelException.CANCEL_GENERIC;
	}
	return meshRegionImage;
}
/**
 * Insert the method's description here.
 * Creation date: (9/18/2005 10:42:24 AM)
 */
public MeshRegionSurfaces generateMeshRegionSurfaces(ClientTaskStatusSupport clientTaskStatusSupport) throws cbit.image.ImageException,UserCancelException{

	if(clientTaskStatusSupport != null){
		clientTaskStatusSupport.setMessage("Generating region image...");
	}
	RegionImage meshRegionImage = generateRegionImage(getMesh(),clientTaskStatusSupport);

	cbit.vcell.geometry.surface.SurfaceCollection surfaceCollection = meshRegionImage.getSurfacecollection();
	
	MembraneElement[] membraneElements = (MembraneElement[])getMesh().getMembraneElements().clone();

	int[][] surface_polygon_MembraneIndexes = new int[surfaceCollection.getSurfaceCount()][];

	if(clientTaskStatusSupport != null){
		clientTaskStatusSupport.setMessage("Generating surfaces...");
	}
	int totalPolygons = surfaceCollection.getTotalPolygonCount();
	int counter = 0;
	//Assign membraneIndexes to Polygons
	for(int i=0;i<surfaceCollection.getSurfaceCount();i+= 1){
		cbit.vcell.geometry.surface.Surface surface = surfaceCollection.getSurfaces(i);
		surface_polygon_MembraneIndexes[i] = new int[surface.getPolygonCount()];
		for(int j=0;j<surface.getPolygonCount();j+= 1){
			if(clientTaskStatusSupport != null && (j % 1000 == 0)){
				if(clientTaskStatusSupport.isInterrupted()){
					throw UserCancelException.CANCEL_GENERIC;
				}
				clientTaskStatusSupport.setProgress(counter*100/totalPolygons);
			}
			counter++;
			
			cbit.vcell.geometry.surface.Quadrilateral quad = (cbit.vcell.geometry.surface.Quadrilateral)surface.getPolygons(j);
			int membraneIndex = -1;
			for(int k=0;k<membraneElements.length;k+= 1){
				if(membraneElements[k] == null){
					continue;
				}
				if(		(membraneElements[k].getInsideVolumeIndex() == quad.getVolIndexNeighbor1()
						&&
						membraneElements[k].getOutsideVolumeIndex() == quad.getVolIndexNeighbor2())
					||
						(membraneElements[k].getInsideVolumeIndex() == quad.getVolIndexNeighbor2()
						&&
						membraneElements[k].getOutsideVolumeIndex() == quad.getVolIndexNeighbor1())
				){
					membraneIndex = k;
					break;
				}
			}
			if(membraneIndex == -1){
				throw new RuntimeException("Couldn't find membraneIndex for quad="+quad);
			}else if(membraneElements[membraneIndex] == null){
				throw new RuntimeException("More than 1 MembraneElement found for quad="+quad);
			}
			surface_polygon_MembraneIndexes[i][j] = membraneIndex;
			membraneElements[membraneIndex] = null;
		}
	}
	//All MembraneElements should have been assigned
	for(int i=0;i<membraneElements.length;i+= 1){
		if(membraneElements[i] != null){
			throw new RuntimeException("Some MembraneElements were not assigned to surface");
		}
	}

	return new MeshRegionSurfaces(surfaceCollection,surface_polygon_MembraneIndexes);	
}

private Hashtable<SampledCurve, int[]> constructChomboCurves(int normalAxis, int slice)
{
	Hashtable<SampledCurve, int[]> curvesAndValues = null;
	if (mesh.isChomboMesh() && mesh.getMembraneElements()!=null)
	{
		CartesianMeshChombo chomboMesh = (CartesianMeshChombo)mesh;
		curvesAndValues = new Hashtable<SampledCurve, int[]>();
		if (chomboMesh.getDimension() == 2)
		{
			MembraneElement membraneElements[] = mesh.getMembraneElements();
			List<MembraneElement> melist = new ArrayList<MembraneElement>();
			Segment2D[] segments = chomboMesh.get2DSegments();
			List<MembraneElement> openCurveStartingPointList = new ArrayList<MembraneElement>();
			for (MembraneElement me : membraneElements)
			{
				melist.add(me);
				Segment2D segment = segments[me.getMembraneIndex()];
				if (segment.prevNeigbhor < 0)
				{
					openCurveStartingPointList.add(me);
				}
			}
			MembraneElement me = null;
			Coordinate[] vertices = chomboMesh.getVertices();
			SampledCurve curve = null;
			List<Integer> indexList = new ArrayList<Integer>();
			int startingIndex = -1;
			boolean bOpen = false;
			while (melist.size() > 0)
			{
				if (me == null)
				{
					if (openCurveStartingPointList.size() > 0)
					{
						me = openCurveStartingPointList.remove(0);
						bOpen = true;
					}
					else
					{
						me = melist.get(0);
						bOpen = false;
					}
					startingIndex = me.getMembraneIndex();
					curve = new SampledCurve();
					indexList.clear();
				}
				melist.remove(me);
				
				Segment2D segment = segments[me.getMembraneIndex()];
				indexList.add(me.getMembraneIndex());
				
				int pcnt = curve.getControlPointCount();
				if (pcnt == 0 && segment.prevVertex >= 0)
				{
					Coordinate p1 = vertices[segment.prevVertex];
					curve.appendControlPoint(p1);
				}
				if (segment.nextVertex >= 0 && segment.nextNeigbhor != startingIndex)
				{
					Coordinate p2 = vertices[segment.nextVertex];				
					curve.appendControlPoint(p2);
				}
				
				int nextIndex = segment.nextNeigbhor;
				if (nextIndex == startingIndex || nextIndex < 0)
				{
					// curve complete
					int[] rmi = new int[indexList.size()];
					for(int i = 0; i < indexList.size(); ++ i)
					{
						rmi[i] = indexList.get(i);
					}
					curve.setClosed(!bOpen);
					curvesAndValues.put(curve, rmi);
					// start a new curve
					me = null;
				}
				else
				{
					me = membraneElements[nextIndex];
				}
			}
		}
		else
		{
			// 3D
		}
	}
	return curvesAndValues;
}

/**
 * Insert the method's description here.
 * Creation date: (8/30/00 5:52:19 PM)
 * @return cbit.vcell.geometry.SampledCurve[]
 * @param normalAxis int
 */
public Hashtable<SampledCurve, int[]> getCurvesAndMembraneIndexes(int normalAxis, int slice) {
	Hashtable<SampledCurve, int[]> curvesAndValues = null;
	if (mesh.isChomboMesh())
	{
		curvesAndValues = constructChomboCurves(normalAxis, slice);
	}
	else
	{
		//
		//Finds individual curves given a normalAxis and slice
		//Also associates membrane indexes with each curve segment
		//
		MembraneElement membraneElements[] = mesh.getMembraneElements();
		if (membraneElements == null) {
			return null;
		}
		if(mesh.isMembraneConnectivityOK() == false){
			System.out.println("CartesianMesh.getCurvesFromMembranes(), MESH ERROR: membrane connectivity is bad");
			//throw new RuntimeException("membrane connectivity bad");
		}
		boolean[] bMembraneElementChecked = new boolean[membraneElements.length];
		for (int c = 0; c < membraneElements.length; c += 1) {
			bMembraneElementChecked[c] = false;
		}
		SampledCurve newCurve = null;
		//Stores (Integer) membrane indexes corresponding to each segment of curve
		Vector<Integer> resolvedMembraneIndexes = null;
		int c = 0;
		while (true) {
			for (; c < membraneElements.length; c += 1) {
				if (bMembraneElementChecked[c] == false && isMembraneElementVisible(membraneElements[c], normalAxis, slice)) {
					newCurve = new SampledCurve();
					resolvedMembraneIndexes = new Vector<Integer>();
					ConstructCurveHelper pendingCCH = null;
					ConstructCurveHelper currentCCH =
						new ConstructCurveHelper(membraneElements[c],newCurve, membraneElements[c], null, false, bMembraneElementChecked, normalAxis, slice,resolvedMembraneIndexes);
					while(true){
						ConstructCurveHelper[] currentCCHArr = constructCurve(currentCCH);
						if(currentCCHArr == null){
							if(pendingCCH == null){
								break;
							}else{
								currentCCH = pendingCCH;
								pendingCCH = null;
							}
						}else if(currentCCHArr.length == 1){
							currentCCH = currentCCHArr[0];
						}else if(currentCCHArr.length == 2){
							if(pendingCCH != null){
								throw new RuntimeException("MeshDisplayAdapter.getCurvesAndMembraneIndexes Error -- Only 1 Pending CurveConstructionHelper allowed.");
							}
							currentCCH = currentCCHArr[0];
							pendingCCH = currentCCHArr[1];
						}
					}
					break;
				}
			}
			if (newCurve == null) {
				break;
			}
			if (curvesAndValues == null) {
				curvesAndValues = new Hashtable<SampledCurve, int[]>();
			}
			//convert Vector to int[]
			int[] rmi = new int[resolvedMembraneIndexes.size()];
			for(int i = 0;i < resolvedMembraneIndexes.size();i+= 1){
				rmi[i] = ((Integer)(resolvedMembraneIndexes.elementAt(i))).intValue();
			}
			curvesAndValues.put(newCurve, rmi);
			newCurve = null;
		}
	}
	return curvesAndValues;
}


/**
 * Insert the method's description here.
 * Creation date: (11/9/2000 4:36:26 PM)
 * @return java.util.Hashtable
 * @param normalAxis int
 * @param slice int
 * @param countourValues double[]
 */
public Hashtable<SampledCurve, Vector<Double>> getCurvesFromContours(double[] countourValues) {
	ContourElement contourElements[] = mesh.getContourElements();
	if (contourElements == null) {
		return null;
	}
	if (countourValues != null && countourValues.length != contourElements.length) {
		throw new RuntimeException("countourValues.length != contourElements.length");
	}
	Hashtable<SampledCurve, Vector<Double>> curvesAndValues = new Hashtable<SampledCurve, Vector<Double>>();
	SampledCurve newCurve = null;
	Vector<Double> resolvedContourValues = null;
	for (int c = 0; c < contourElements.length; c += 1) {
		if (contourElements[c].isBegin()) {
			newCurve = new SampledCurve();
			resolvedContourValues = new Vector<Double>();
		}
		newCurve.appendControlPoint(contourElements[c].getBeginCoordinate());
		if(countourValues != null){
			resolvedContourValues.addElement(new Double(countourValues[c]));
		}
		if (contourElements[c].isEnd()) {
			newCurve.appendControlPoint(contourElements[c].getEndCoordinate());
			curvesAndValues.put(newCurve, resolvedContourValues);
		}
	}
	return curvesAndValues;
}


/**
 * Insert the method's description here.
 * Creation date: (7/12/2001 3:08:24 PM)
 * @return double[]
 * @param membraneIndexes java.util.Vector
 * @param varType cbit.vcell.simdata.VariableType
 */
public double[] getDataValuesForMembraneIndexes(int[] membraneIndexes, double[] allMembraneDataValues, cbit.vcell.math.VariableType varType) {
	if(varType.equals(cbit.vcell.math.VariableType.VOLUME)){
		return null;
	}else if(varType.equals(cbit.vcell.math.VariableType.VOLUME_REGION)){
		return null;
	}else if(varType.equals(cbit.vcell.math.VariableType.MEMBRANE)){
		double[] membraneDataValues = new double[membraneIndexes.length];
		for(int i = 0;i < membraneIndexes.length;i+= 1){
			//int mIndex = ((Integer)membraneIndexes.elementAt(i)).intValue();
			membraneDataValues[i] = allMembraneDataValues[membraneIndexes[i]];
		}
		return membraneDataValues;
	}else if(varType.equals(cbit.vcell.math.VariableType.MEMBRANE_REGION)){
		double[] regionDataValues = new double[membraneIndexes.length];
		for(int i = 0;i < membraneIndexes.length;i+= 1){
			//int mIndex = ((Integer)membraneIndexes.elementAt(i)).intValue();
			regionDataValues[i] = allMembraneDataValues[getMesh().getMembraneRegionIndex(membraneIndexes[i])];
		}
		return regionDataValues;
	}else if(varType.equals(cbit.vcell.math.VariableType.CONTOUR)){
		return null;
	}else if(varType.equals(cbit.vcell.math.VariableType.CONTOUR_REGION)){
		return null;
	}else if(varType.equals(cbit.vcell.math.VariableType.NONSPATIAL)){
		return null;
	}
	return null;
}


/**
 * Insert the method's description here.
 * Creation date: (7/10/01 3:52:45 PM)
 * @return cbit.vcell.solvers.CartesianMesh
 */
public cbit.vcell.solvers.CartesianMesh getMesh() {
	return mesh;
}


/**
 * Insert the method's description here.
 * Creation date: (8/29/00 3:47:20 PM)
 * @return int
 * @param offset int
 */
private int getNormalAxis(MembraneElement membraneElement) {
	int offset = Math.abs(membraneElement.getInsideVolumeIndex() - membraneElement.getOutsideVolumeIndex());
	if (offset == 1) {
		return Coordinate.X_AXIS;
	}
	if (offset == mesh.getSizeX()) {
		return Coordinate.Y_AXIS;
	}
	if (offset == mesh.getSizeX()*mesh.getSizeY()) {
		return Coordinate.Z_AXIS;
	}
	throw new RuntimeException("Unknown Plane for mesh Dimensions");
}


/**
 * Insert the method's description here.
 * Creation date: (8/29/00 3:47:20 PM)
 * @return int
 * @param offset int
 */
private int getParalellAxis(MembraneElement membraneElement,int viewNormalAxis) {
	int membraneNormalAxis = getNormalAxis(membraneElement);
	if (membraneNormalAxis == Coordinate.X_AXIS) {
		if(viewNormalAxis == Coordinate.Y_AXIS){
			return Coordinate.Z_AXIS;
		}else if(viewNormalAxis == Coordinate.Z_AXIS){
			return Coordinate.Y_AXIS;
		}
	}
	if (membraneNormalAxis == Coordinate.Y_AXIS) {
		if(viewNormalAxis == Coordinate.X_AXIS){
			return Coordinate.Z_AXIS;
		}else if(viewNormalAxis == Coordinate.Z_AXIS){
			return Coordinate.X_AXIS;
		}
	}
	if (membraneNormalAxis == Coordinate.Z_AXIS) {
		if(viewNormalAxis == Coordinate.Y_AXIS){
			return Coordinate.X_AXIS;
		}else if(viewNormalAxis == Coordinate.X_AXIS){
			return Coordinate.Y_AXIS;
		}
	}
	throw new IllegalArgumentException("Unknown Axis for viewAxis="+viewNormalAxis+" and membraneNormalAxis="+membraneNormalAxis);
}


/**
 * Insert the method's description here.
 * Creation date: (8/30/00 6:25:14 PM)
 * @return cbit.vcell.geometry.Line
 */
private Line getProjectedSegment(MembraneElement membraneElement,int normalAxis) {
	//Find midpoint between the 2 volume elements for this MembraneElement
	Coordinate c0 = getMesh().getCoordinateFromMembraneIndex(membraneElement.getMembraneIndex());
	double x0 = c0.getX(), y0 = c0.getY(), z0 = c0.getZ();
	double x1 = c0.getX(), y1 = c0.getY(), z1 = c0.getZ();
	//Stretch endpoints for the line segment based on membranePlane and size of volumeElement
	int membranePlane = getParalellAxis(membraneElement,normalAxis);
	if (membranePlane == Coordinate.Z_AXIS) {
		double elementZSize = (1.0 / (double) (mesh.getSizeZ()-1)) * mesh.getExtent().getZ();
		z0 -= (elementZSize / 2.0);
		z1 += (elementZSize / 2.0);
	} else if (membranePlane == Coordinate.Y_AXIS) {
		double elementYSize = (1.0 / (double) (mesh.getSizeY()-1)) * mesh.getExtent().getY();
		y0 -= (elementYSize / 2.0);
		y1 += (elementYSize / 2.0);
	} else if (membranePlane == Coordinate.X_AXIS) {
		double elementXSize = (1.0 / (double) (mesh.getSizeX()-1)) * mesh.getExtent().getX();
		x0 -= (elementXSize / 2.0);
		x1 += (elementXSize / 2.0);
	}
	//Clip to edge of world
	x0 = (x0 < mesh.getOrigin().getX()?mesh.getOrigin().getX():x0);
	y0 = (y0 < mesh.getOrigin().getY()?mesh.getOrigin().getY():y0);
	z0 = (z0 < mesh.getOrigin().getZ()?mesh.getOrigin().getZ():z0);
	x1 = (x1 > (mesh.getOrigin().getX() + mesh.getExtent().getX())?mesh.getOrigin().getX() + mesh.getExtent().getX():x1);
	y1 = (y1 > (mesh.getOrigin().getY() + mesh.getExtent().getY())?mesh.getOrigin().getY() + mesh.getExtent().getY():y1);
	z1 = (z1 > (mesh.getOrigin().getZ() + mesh.getExtent().getZ())?mesh.getOrigin().getZ() + mesh.getExtent().getZ():z1);
	//
	return new Line(new Coordinate(x0, y0, z0), new Coordinate(x1, y1, z1));
}


/**
 * Insert the method's description here.
 * Creation date: (8/30/00 6:25:14 PM)
 * @return cbit.vcell.geometry.Line
 */
private boolean isMembraneElementVisible(MembraneElement membraneElement, int normalAxis, int slice) {
	int membranePlane = getNormalAxis(membraneElement);
	if (membranePlane == normalAxis) {
		//MembraneElement perpendicular to our view
		return false;
	}
	CoordinateIndex insideCI = mesh.getCoordinateIndexFromVolumeIndex(membraneElement.getInsideVolumeIndex());
	//MembraneElement not visible in slice
	if (normalAxis == Coordinate.Z_AXIS && insideCI.z != slice) {
		return false;
	} else
		if (normalAxis == Coordinate.Y_AXIS && insideCI.y != slice) {
			return false;
		} else
			if (normalAxis == Coordinate.X_AXIS && insideCI.x != slice) {
				return false;
			}
	return true;
}
}
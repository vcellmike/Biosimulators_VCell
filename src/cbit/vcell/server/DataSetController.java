package cbit.vcell.server;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import cbit.vcell.math.*;
import cbit.plot.*;
import java.rmi.*;
import cbit.vcell.solvers.*;
import cbit.vcell.simdata.*;
import cbit.vcell.simdata.gui.SpatialSelection;
import cbit.vcell.export.server.*;
import cbit.vcell.field.FieldDataFileOperationResults;
import cbit.vcell.field.FieldDataFileOperationSpec;
import cbit.vcell.solver.*;
import cbit.rmi.event.*;
/**
 * This interface was generated by a SmartGuide.
 * 
 */
public interface DataSetController extends Remote {
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 6:21:10 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
void addFunction(VCDataIdentifier vcdataID, AnnotatedFunction function) throws DataAccessException, RemoteException;
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 6:21:10 PM)
 * @param function cbit.vcell.math.Function[]
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
void addFunctions(VCDataIdentifier vcdID, AnnotatedFunction function[]) throws DataAccessException, RemoteException;


public FieldDataFileOperationResults fieldDataFileOperation(FieldDataFileOperationSpec fieldDataFileOperationSpec) throws RemoteException, DataAccessException;


/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public DataIdentifier[] getDataIdentifiers(VCDataIdentifier vcdataID) throws RemoteException, DataAccessException;
/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public double[] getDataSetTimes(VCDataIdentifier vcdataID) throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 6:21:10 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
 AnnotatedFunction[] getFunctions(VCDataIdentifier vcdataID) throws DataAccessException, RemoteException; 
/**
 * Insert the method's description here.
 * Creation date: (1/16/00 11:38:06 PM)
 * @return boolean
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
boolean getIsODEData(VCDataIdentifier vcdataID) throws DataAccessException, RemoteException;
/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param variable java.lang.String
 * @param time double
 * @param begin cbit.vcell.math.CoordinateIndex
 * @param end cbit.vcell.math.CoordinateIndex
 * @exception java.rmi.RemoteException The exception description.
 */
public PlotData getLineScan(VCDataIdentifier vcdataID, String variable, double time, CoordinateIndex begin, CoordinateIndex end) throws RemoteException, DataAccessException;
/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param variable java.lang.String
 * @param time double
 * @param spatialSelection cbit.vcell.simdata.gui.SpatialSelection
 * @exception java.rmi.RemoteException The exception description.
 */
public PlotData getLineScan(VCDataIdentifier vcdataID, String variable, double time, SpatialSelection spatialSelection) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return CartesianMesh
 */
CartesianMesh getMesh(VCDataIdentifier vcdataID) throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (1/13/00 6:21:10 PM)
 * @param odeSimData cbit.vcell.export.data.ODESimData
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
cbit.vcell.solver.ode.ODESimData getODEData(VCDataIdentifier vcdataID) throws DataAccessException, RemoteException;
/**
 * This method was created in VisualAge.
 * @return ParticleData
 * @param time double
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
cbit.vcell.simdata.ParticleDataBlock getParticleDataBlock(VCDataIdentifier vcdataID, double time) throws DataAccessException, RemoteException;
/**
 * This method was created in VisualAge.
 * @return boolean
 */
public boolean getParticleDataExists(VCDataIdentifier vcdataID) throws DataAccessException, RemoteException;
/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */
public cbit.vcell.simdata.SimDataBlock getSimDataBlock(VCDataIdentifier vcdataID, String varName, double time) throws RemoteException, DataAccessException;
/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param x int
 * @param y int
 * @param z int
 * @exception java.rmi.RemoteException The exception description.
 */
public cbit.util.TimeSeriesJobResults getTimeSeriesValues(VCDataIdentifier vcdataID, cbit.util.TimeSeriesJobSpec timeSeriesJobSpec) throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (10/22/2001 2:53:44 PM)
 * @return cbit.rmi.event.ExportEvent
 * @param exportSpecs cbit.vcell.export.server.ExportSpecs
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
ExportEvent makeRemoteFile(ExportSpecs exportSpecs) throws DataAccessException, java.rmi.RemoteException;
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 6:21:10 PM)
 * @param function cbit.vcell.math.Function
 * @exception cbit.vcell.server.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
void removeFunction(VCDataIdentifier vcdataID, AnnotatedFunction function) throws DataAccessException, RemoteException;
}

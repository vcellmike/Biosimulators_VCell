/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.solvers;
import static org.vcell.util.BeanUtils.notNull;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Vector;

import org.vcell.chombo.ChomboSolverSpec;
import org.vcell.util.BeanUtils;
import org.vcell.util.DataAccessException;
import org.vcell.util.ISize;
import org.vcell.util.NullSessionLog;
import org.vcell.util.PropertyLoader;
import org.vcell.util.SessionLog;
import org.vcell.util.document.SimResampleInfoProvider;

import cbit.vcell.field.FieldDataIdentifierSpec;
import cbit.vcell.field.FieldFunctionArguments;
import cbit.vcell.field.FieldUtilities;
import cbit.vcell.geometry.Geometry;
import cbit.vcell.geometry.surface.GeometrySurfaceDescription;
import cbit.vcell.math.Variable;
import cbit.vcell.messaging.server.SimulationTask;
import cbit.vcell.parser.Expression;
import cbit.vcell.parser.ExpressionException;
import cbit.vcell.resource.ResourceUtil;
import cbit.vcell.simdata.DataSetControllerImpl;
import cbit.vcell.simdata.SimDataConstants;
import cbit.vcell.solver.AnnotatedFunction;
import cbit.vcell.solver.Simulation;
import cbit.vcell.solver.SimulationJob;
import cbit.vcell.solver.SolverDescription;
import cbit.vcell.solver.SolverException;
import cbit.vcell.solver.SolverTaskDescription;
import cbit.vcell.solver.VCSimulationDataIdentifier;
import cbit.vcell.solver.server.SimulationMessage;
import cbit.vcell.solver.server.SolverStatus;

/**
 * This interface was generated by a SmartGuide.
 * 
 */
public class FVSolverStandalone extends AbstractCompiledSolver {
	//protected CppCoderVCell cppCoderVCell = null;
	private String baseName = null;
	private SimResampleInfoProvider simResampleInfoProvider;
	private Geometry resampledGeometry = null;
	/**
	 * for now, until classes are split
	 */
	private final boolean isChombo;
	/**
	 * lazily evaluated first command to support FiniteVolume quick-run
	 * (pending a better refactoring)
	 */
	private ExecutableCommand primaryCommand;
	private File destinationDirectory = null;
	
	private boolean unixMode = false;
	
	public static final int HESM_KEEP_AND_CONTINUE = 0;
	public static final int HESM_THROW_EXCEPTION = 1;
	public static final int HESM_OVERWRITE_AND_CONTINUE = 2;
	
	public FVSolverStandalone (SimulationTask simTask, File userDir, SessionLog sessionLog, boolean bMsging) throws SolverException {
		this(simTask, userDir, userDir, sessionLog, bMsging);
	}

/**
 * This method was created by a SmartGuide.
 * @param mathDesc cbit.vcell.math.MathDescription
 * @param platform cbit.vcell.solvers.Platform
 * @param workingUserDir output files are generated into this directory during simulation
 * @param primaryUserDir in parallel, output files need to copy to this directory for users to see
 * @param simID java.lang.String
 * @param clientProxy cbit.vcell.solvers.ClientProxy
 */
public FVSolverStandalone (SimulationTask simTask, File userDir, File destinationDirectory, SessionLog sessionLog, boolean bMsging) throws SolverException {
	super(simTask, userDir, sessionLog, bMsging);
	this.destinationDirectory = destinationDirectory;
	if (! simTask.getSimulation().isSpatial()) {
		throw new SolverException("Cannot use FVSolver on non-spatial simulation");
	}
	this.simResampleInfoProvider = (VCSimulationDataIdentifier)simTask.getSimulationJob().getVCDataIdentifier();
	baseName = new File(getBaseName()).getName();
	primaryCommand = null;
	
	notNull(SimulationTask.class,simTask);
	Simulation s = notNull(Simulation.class,simTask.getSimulation());
	SolverTaskDescription sts = notNull(SolverTaskDescription.class, s.getSolverTaskDescription());
	SolverDescription sd = notNull(SolverDescription.class,sts.getSolverDescription());
	isChombo = sd.isChomboSolver(); 
}


@Override
public void setUnixMode() {
	unixMode = true;
}



/**
 * no-op
 */
public void cleanup() {
}


/**
 * Insert the method's description here.
 * Creation date: (6/27/01 3:25:11 PM)
 * @return cbit.vcell.solvers.ApplicationMessage
 * @param message java.lang.String
 */
protected ApplicationMessage getApplicationMessage(String message) {
	//
	// "data:iteration:time"  .... sent every time data written for FVSolver
	// "progress:xx.x%"        .... sent every 1% for FVSolver
	//
	//
	if (message.startsWith(DATA_PREFIX)){
		double timepoint = Double.parseDouble(message.substring(message.lastIndexOf(SEPARATOR)+1));
		setCurrentTime(timepoint);
		return new ApplicationMessage(ApplicationMessage.DATA_MESSAGE,getProgress(),timepoint,null,message);
	}else if (message.startsWith(PROGRESS_PREFIX)){
		String progressString = message.substring(message.lastIndexOf(SEPARATOR)+1,message.indexOf("%"));
		double progress = Double.parseDouble(progressString)/100.0;
		double startTime = simTask.getSimulation().getSolverTaskDescription().getTimeBounds().getStartingTime();
		double endTime = simTask.getSimulation().getSolverTaskDescription().getTimeBounds().getEndingTime();
		setCurrentTime(startTime + (endTime-startTime)*progress);
		return new ApplicationMessage(ApplicationMessage.PROGRESS_MESSAGE,progress,-1,null,message);
	}else{
		throw new RuntimeException("unrecognized message");
	}
}

public Geometry getResampledGeometry() throws SolverException {
	if (resampledGeometry == null) {
		// clone and resample geometry
		try {
			resampledGeometry = (Geometry) BeanUtils.cloneSerializable(simTask.getSimulation().getMathDescription().getGeometry());
			GeometrySurfaceDescription geoSurfaceDesc = resampledGeometry.getGeometrySurfaceDescription();
			ISize newSize = simTask.getSimulation().getMeshSpecification().getSamplingSize();
			geoSurfaceDesc.setVolumeSampleSize(newSize);
			geoSurfaceDesc.updateAll();		
		} catch (Exception e) {
			e.printStackTrace();
			throw new SolverException(e.getMessage());
		}
	}
	return resampledGeometry;
}
		
protected void writeVCGAndResampleFieldData() throws SolverException {
	fireSolverStarting(SimulationMessage.MESSAGE_SOLVEREVENT_STARTING_PROC_GEOM);
	
	try {
		// write subdomains file
		SubdomainInfo.write(new File(getSaveDirectory(), baseName + SimDataConstants.SUBDOMAINS_FILE_SUFFIX), simTask.getSimulation().getMathDescription());
		
		PrintWriter pw = new PrintWriter(new FileWriter(new File(getSaveDirectory(), baseName+SimDataConstants.VCG_FILE_EXTENSION)));
		GeometryFileWriter.write(pw, getResampledGeometry());
		pw.close();
				
		FieldDataIdentifierSpec[] argFieldDataIDSpecs = simTask.getSimulationJob().getFieldDataIdentifierSpecs();
		if(argFieldDataIDSpecs != null && argFieldDataIDSpecs.length > 0){
			fireSolverStarting(SimulationMessage.MESSAGE_SOLVEREVENT_STARTING_RESAMPLE_FD);
			
			FieldFunctionArguments psfFieldFunc = null;
			Variable var = simTask.getSimulationJob().getSimulationSymbolTable().getVariable(Simulation.PSF_FUNCTION_NAME);
			if (var != null) {
				FieldFunctionArguments[] ffas = FieldUtilities.getFieldFunctionArguments(var.getExpression());
				if (ffas == null || ffas.length == 0) {
					throw new DataAccessException("Point Spread Function " + Simulation.PSF_FUNCTION_NAME + " can only be a single field function.");
				} else {				
					Expression newexp;
					try {
						newexp = new Expression(ffas[0].infix());
						if (!var.getExpression().compareEqual(newexp)) {
							throw new DataAccessException("Point Spread Function " + Simulation.PSF_FUNCTION_NAME + " can only be a single field function.");
						}
						psfFieldFunc = ffas[0];
					} catch (ExpressionException e) {
						e.printStackTrace();
						throw new DataAccessException(e.getMessage());
					}
				}
			}			
			
			boolean bResample[] = new boolean[argFieldDataIDSpecs.length];
			Arrays.fill(bResample, true);
			for (int i = 0; i < argFieldDataIDSpecs.length; i++) {
				argFieldDataIDSpecs[i].getFieldFuncArgs().getTime().bindExpression(simTask.getSimulationJob().getSimulationSymbolTable());
				if (argFieldDataIDSpecs[i].getFieldFuncArgs().equals(psfFieldFunc)) {
					bResample[i] = false;
				}
			}
			
			int numMembraneElements = getResampledGeometry().getGeometrySurfaceDescription().getSurfaceCollection().getTotalPolygonCount();
			CartesianMesh simpleMesh = CartesianMesh.createSimpleCartesianMesh(getResampledGeometry().getOrigin(), 
					getResampledGeometry().getExtent(),
					simTask.getSimulation().getMeshSpecification().getSamplingSize(),
					getResampledGeometry().getGeometrySurfaceDescription().getRegionImage());
			String secondarySimDataDir = PropertyLoader.getProperty(PropertyLoader.secondarySimDataDirProperty, null);			
			DataSetControllerImpl dsci = new DataSetControllerImpl(new NullSessionLog(), null, getSaveDirectory().getParentFile(),
					secondarySimDataDir == null ? null : new File(secondarySimDataDir));
			dsci.writeFieldFunctionData(null,argFieldDataIDSpecs, bResample, simpleMesh, simResampleInfoProvider, 
					numMembraneElements, HESM_OVERWRITE_AND_CONTINUE);
		}
	} catch(Exception e){
		throw new SolverException(e.getMessage());
	}
}
//
//
///**
// * Insert the method's description here.
// * Creation date: (6/27/2001 2:33:03 PM)
// */
//public void propertyChange(java.beans.PropertyChangeEvent event) {
//	super.propertyChange(event);
//	
//	if (event.getSource() == getMathExecutable() && event.getPropertyName().equals("applicationMessage")) {
//		String messageString = (String)event.getNewValue();
//		if (messageString==null || messageString.length()==0){
//			return;
//		}
//		ApplicationMessage appMessage = getApplicationMessage(messageString);
//		if (appMessage!=null && appMessage.getMessageType() == ApplicationMessage.DATA_MESSAGE) {
//			fireSolverPrinted(appMessage.getTimepoint());
//		}
//	}
//}


/**
 * This method was created by a SmartGuide.
 */
protected void initialize() throws SolverException {
	try {
		Simulation sim = simTask.getSimulation();
		if (sim.isSerialParameterScan()) {
			//write functions file for all the simulations in the scan
			for (int scan = 0; scan < sim.getScanCount(); scan ++) {
				SimulationJob simJob = new SimulationJob(sim, scan, simTask.getSimulationJob().getFieldDataIdentifierSpecs());
				// ** Dumping the functions of a simulation into a '.functions' file.
				String basename = new File(getSaveDirectory(), simJob.getSimulationJobID()).getPath();
				String functionFileName = basename + FUNCTIONFILE_EXTENSION;
				
				Vector<AnnotatedFunction> funcList = simJob.getSimulationSymbolTable().createAnnotatedFunctionsList(simTask.getSimulation().getMathDescription());				
				//Try to save existing user defined functions	
				try{
					File existingFunctionFile = new File(functionFileName);
					if (existingFunctionFile.exists()){
						Vector<AnnotatedFunction> oldFuncList = FunctionFileGenerator.readFunctionsFile(existingFunctionFile, simTask.getSimulationJobID());
						for(AnnotatedFunction func : oldFuncList){
							if(func.isOldUserDefined()){
								funcList.add(func);
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					//ignore
				}
				
				//Try to save existing user defined functions
				FunctionFileGenerator functionFileGenerator = new FunctionFileGenerator(functionFileName, funcList);

				try {
					functionFileGenerator.generateFunctionFile();
				}catch (Exception e){
					e.printStackTrace(System.out);
					throw new RuntimeException("Error creating .function file for "+functionFileGenerator.getBasefileName()+e.getMessage());
				}
			}
			
		} else {
			writeFunctionsFile();
		}
		
		// not for Chombo solver
		if (!isChombo) {
			writeVCGAndResampleFieldData();
		}
	
		setSolverStatus(new SolverStatus(SolverStatus.SOLVER_RUNNING, SimulationMessage.MESSAGE_SOLVER_RUNNING_INIT));
		fireSolverStarting(SimulationMessage.MESSAGE_SOLVEREVENT_STARTING_INIT);
	
		setSolverStatus(new SolverStatus(SolverStatus.SOLVER_RUNNING, SimulationMessage.MESSAGE_SOLVER_RUNNING_INPUT_FILE));
			
		File fvinputFile = new File(getInputFilename());
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(fvinputFile));
			new FiniteVolumeFileWriter(pw, simTask, getResampledGeometry(), getSaveDirectory(), destinationDirectory, bMessaging).write();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	} catch (Exception ex) {
		ex.printStackTrace(System.out);
		throw new SolverException(ex.getMessage());
	}
}


/**
 * @return full path of baseName + ".fvinput" 
 */
private String getInputFilename() {
	String ipf =  new File(getSaveDirectory(),baseName + ".fvinput").getAbsolutePath();
	if (!unixMode) {
		return ipf;
	}
	return ResourceUtil.forceUnixPath(ipf);
}

@Override
public MathExecutable getMathExecutable() {
	if (isChombo) {
		//throw new UnsupportedOperationException("Chombo does not support Quick Run");
	}
	MathExecutable me = super.getMathExecutable();
	if (me != null) {
		return me;
	}
	
	if (primaryCommand == null) {
		getCommands(); //sets primaryCommand as sideEffect
	}
	assert(primaryCommand != null);
	String[] carray = primaryCommand.getCommands().toArray(new String[0]);
	File workingDir = getSaveDirectory();
	setMathExecutable(new MathExecutable(carray, workingDir));
	me= super.getMathExecutable();
	assert(me != null);
	return me;
}


@Override
public Collection<ExecutableCommand> getCommands() {
	if (!isChombo) {
		return getFVCommands();
	}
	return getChomboCommands( );
}

private Collection<ExecutableCommand> getFVCommands() {
	assert (!isChombo);
	Simulation simulation = getSimulationJob().getSimulation();
	final boolean isParallel = simulation.getSolverTaskDescription().isParallel();
	String executableName =  PropertyLoader.getRequiredProperty(PropertyLoader.finiteVolumeExecutableProperty);
	if (isParallel) {
		throw new UnsupportedOperationException(executableName + " does not support parallel");
	}
	String inputFilename = getInputFilename();
	primaryCommand = new ExecutableCommand(true, false, executableName, inputFilename );
	return Arrays.asList(primaryCommand);
}

private Collection<ExecutableCommand> getChomboCommands() {
	assert (isChombo);
	ArrayList<ExecutableCommand> commands = new ArrayList<ExecutableCommand>(2);
	String inputFilename = getInputFilename();

	String executableName = null;
	Simulation simulation = getSimulationJob().getSimulation();
	SolverTaskDescription sTaskDesc = simulation.getSolverTaskDescription();
	final boolean isParallel = sTaskDesc.isParallel();
	int dimension = simulation.getMeshSpecification().getGeometry().getDimension();
	switch (dimension) {
	case 2:
		executableName = PropertyLoader.getRequiredProperty(PropertyLoader.VCellChomboExecutable2D);
		break;
	case 3:
		executableName = PropertyLoader.getRequiredProperty(PropertyLoader.VCellChomboExecutable3D);
		break;
	default:
		throw new IllegalArgumentException("VCell Chombo solver does not support " + dimension + "problems");
	}
	
	if (isParallel) {
		String parallelName = executableName + "parallel";
		ExecutableCommand solve = new ExecutableCommand(true, true, parallelName, inputFilename );
		commands.add(solve);
		ChomboSolverSpec css = sTaskDesc.getChomboSolverSpec();
		Objects.requireNonNull(css);
		if (css.isSaveVCellOutput()) {
			ExecutableCommand convertChomboData = new ExecutableCommand(true, false,executableName, "-ccd", inputFilename );
			commands.add(convertChomboData);
		}
	}
	else {
		ExecutableCommand ec = new ExecutableCommand(true, false, executableName, inputFilename );
		commands.add(ec);
		primaryCommand = ec;
	}
	
	return commands; 
}


}

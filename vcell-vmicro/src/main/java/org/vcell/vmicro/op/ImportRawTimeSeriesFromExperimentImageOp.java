package org.vcell.vmicro.op;

import java.io.File;

import org.vcell.util.ClientTaskStatusSupport;
import org.vcell.vcellij.ImageDatasetReaderService;
import org.vcell.vmicro.workflow.data.ImageTimeSeries;

import cbit.vcell.VirtualMicroscopy.ImageDataset;
import cbit.vcell.VirtualMicroscopy.UShortImage;

public class ImportRawTimeSeriesFromExperimentImageOp {
	
	public ImageTimeSeries<UShortImage> importRawTimeSeries(File expTimeSeriesFile) throws Exception {
		ClientTaskStatusSupport clientTaskStatusSupport = null;
		ImageDataset rawTimeData = ImageDatasetReaderService.getInstance().getImageDatasetReader().readImageDataset(expTimeSeriesFile.getAbsolutePath(), clientTaskStatusSupport);
		
		ImageTimeSeries<UShortImage> imageTimeSeries = new ImageTimeSeries<UShortImage>(UShortImage.class,rawTimeData.getAllImages(),rawTimeData.getImageTimeStamps(),1);
		
		return imageTimeSeries;
	}

}

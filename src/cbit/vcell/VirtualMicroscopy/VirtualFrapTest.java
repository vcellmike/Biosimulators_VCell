package cbit.vcell.VirtualMicroscopy;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import cbit.image.ImageException;
import cbit.sql.KeyValue;
import cbit.util.Extent;
import cbit.util.xml.XmlUtil;
import cbit.vcell.server.PropertyLoader;
import cbit.vcell.server.User;
//import cbit.vcell.virtualmicroscopy.ROI.RoiType;
//import cbit.vcell.virtualmicroscopy.gui.FRAPDataPanel;
//import cbit.vcell.virtualmicroscopy.gui.FRAPStudyPanel;
//import cbit.vcell.virtualmicroscopy.gui.LocalWorkspace;
import cbit.vcell.xml.XmlReader;

import loci.formats.FormatException;
import loci.formats.FormatReader;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.ImageTools;
import loci.formats.OMEXMLMetadataStore;
import loci.formats.in.ZeissLSMReader;

public class VirtualFrapTest {
	public static void main(String[] args){
//		try {
//			PropertyLoader.loadProperties();
//			final int NumArgTypes = 5;
//			final int DataFileIndex = 0;
//			final int BleachMaskIndex = 1;
//			final int CellMaskIndex = 2;
//			final int BackgroundMaskIndex = 3;
//			final int XMLFileIndex = 4;
//			final String[] argKeys = { "-dataset", "-bleachmask", "-cellmask", "-background", "-xml" };
//			String[] argValues = new String[NumArgTypes];
//			for (int i = 0; i < args.length; i+=2) {
//				boolean bProcessedArgument = false;
//				for (int j = 0; j < NumArgTypes; j++) {
//					if (args[i].equalsIgnoreCase(argKeys[j])){
//						if (i<args.length-1){
//							argValues[j] = args[i+1];
//							bProcessedArgument = true;
//							break;
//						}else{
//							throw new Exception("expected value for "+argKeys[i]);
//						}
//					}
//				}
//				if (!bProcessedArgument){
//					throw new Exception("unexpected argument "+args[i]);
//				}
//			}
//			FRAPStudy frapStudy = null;
//			String workspaceDir = null;
//			String originalImageFilePath = null;
//			String xmlFileName = argValues[XMLFileIndex];
//			if (xmlFileName!=null){
//				workspaceDir = (new File(xmlFileName)).getParent();
//				XmlReader xmlReader = new XmlReader(true);
//				frapStudy = xmlReader.getFrapStudy(XmlUtil.stringToXML(XmlUtil.getXMLString(xmlFileName),null));
//				frapStudy.setXmlFilename(xmlFileName);
//			}else{
//				ImageReader imageReader = new ImageReader();
//				String imageID = argValues[DataFileIndex];
//				if (imageID==null){
//					JFileChooser box = imageReader.getFileChooser();
//					int rval = box.showOpenDialog(null);
//					if (rval == JFileChooser.APPROVE_OPTION){
//						File file = box.getSelectedFile();
//						if (file != null){
//							workspaceDir = file.getParent();
//							originalImageFilePath = file.getAbsolutePath();
//							imageID = file.getPath();
//						}
//					}
//				}else{
//					File file = new File(imageID);
//					workspaceDir = file.getParent();
//					originalImageFilePath = file.getAbsolutePath();
//				}
//				ImageDataset imageDataset = readImageDataset(imageID);
//				double[] timeArray = imageDataset.getImageTimeStamps();
//				Extent extent = imageDataset.getImages()[0].getExtent();
//				int numX = imageDataset.getImages()[0].getNumX();
//				int numY = imageDataset.getImages()[0].getNumY();
//				int numZ = imageDataset.getSizeZ();
//				UShortImage cellRoiImage = null;
//				if (argValues[CellMaskIndex]!=null){
//					ImageDataset roiImageDataset = VirtualFrapTest.readImageDataset(argValues[CellMaskIndex]);
//					cellRoiImage = roiImageDataset.getImages()[0];
//				}else{
//					cellRoiImage = new UShortImage(new short[numX*numY*numZ],extent,numX,numY,numZ);
//				}
//				UShortImage bleachedRoiImage = null;
//				if (argValues[BleachMaskIndex]!=null){
//					ImageDataset roiImageDataset = VirtualFrapTest.readImageDataset(argValues[BleachMaskIndex]);
//					bleachedRoiImage = roiImageDataset.getImages()[0];
//				}else{
//					bleachedRoiImage = new UShortImage(new short[numX*numY*numZ],extent,numX,numY,numZ);
//				}
//				UShortImage backgroundRoiImage = null;
//				if (argValues[BackgroundMaskIndex]!=null){
//					ImageDataset roiImageDataset = VirtualFrapTest.readImageDataset(argValues[BackgroundMaskIndex]);
//					backgroundRoiImage = roiImageDataset.getImages()[0];
//				}else{
//					backgroundRoiImage = new UShortImage(new short[numX*numY*numZ],extent,numX,numY,numZ);
//				}
//				FRAPData frapData = new FRAPData(imageDataset, new ROI[] { 
//						new ROI(bleachedRoiImage,RoiType.ROI_BLEACHED),
//						new ROI(cellRoiImage,RoiType.ROI_CELL),
//						new ROI(backgroundRoiImage,RoiType.ROI_BACKGROUND),
//						});
//				frapStudy = new FRAPStudy();
//				frapStudy.setFrapData(frapData);
//				frapStudy.setOriginalImageFilePath(originalImageFilePath);
//			}
//			JFrame frame = new JFrame();
//			frame.addWindowListener(new WindowAdapter(){
//				public void windowClosing(java.awt.event.WindowEvent e) {
//					System.exit(0);
//				};
//			});
//			FRAPStudyPanel frapStudyPanel = new FRAPStudyPanel();
//			User owner = new User("schaff",new KeyValue("17"));
//			String simDataDir = PropertyLoader.getRequiredProperty(PropertyLoader.localSimDataDirProperty);
//			frapStudyPanel.setLocalWorkspace(new LocalWorkspace(workspaceDir,owner,simDataDir));
//			frapStudyPanel.setFrapStudy(frapStudy);
//			frame.add(frapStudyPanel);
//			frame.setSize(640,480);
//			frame.setVisible(true);
//
//		} catch (Exception e) {
//			e.printStackTrace(System.out);
//		}

	}
	
	public static ImageDataset readImageDataset(String imageID) throws FormatException, IOException, ImageException {
		FormatReader.setDebugLevel(3);
		FormatReader.setDebug(true);
		ImageReader imageReader = new ImageReader();
	    OMEXMLMetadataStore store = new OMEXMLMetadataStore();
	    store.createRoot();
	    imageReader.setMetadataStore(store);
		IFormatReader formatReader = imageReader.getReader(imageID);
		try{
			System.out.println("before series, image size from imageReader("+
					formatReader.getSizeX(imageID)+","+
					formatReader.getSizeY(imageID)+","+
					formatReader.getSizeZ(imageID)+","+
					formatReader.getSizeC(imageID)+","+
					formatReader.getSizeT(imageID)+")");
			System.out.println("imagecount = "+formatReader.getImageCount(imageID));
			int numImages = formatReader.getImageCount(imageID);
			int numChannels = formatReader.getSizeC(imageID);
			if (numChannels>1){
				throw new RuntimeException("multi-channel images not yet supported");
			}
			UShortImage[] images = new UShortImage[numImages];
			for (int i = 0; i < numImages; i++) {
				BufferedImage origBufferedImage = formatReader.openImage(imageID, i);
				System.out.println("original image is type "+FormatReader.getPixelTypeString(ImageTools.getPixelType(origBufferedImage)));
				int seriesCount = formatReader.getSeriesCount(imageID);
				BufferedImage ushortBufferedImage = ImageTools.makeType(origBufferedImage, DataBuffer.TYPE_USHORT);
				System.out.println("ushort image is type "+FormatReader.getPixelTypeString(ImageTools.getPixelType(ushortBufferedImage)));
				int zct[] = formatReader.getZCTCoords(imageID, i);
				int pixelType = ImageTools.getPixelType(ushortBufferedImage);
				short[][] pixels = null;
				Object pixData = ImageTools.getPixels(origBufferedImage);
				if(pixData instanceof short[][]){
					pixels = ImageTools.getShorts(ushortBufferedImage);
				}else if(pixData instanceof byte[][]){
					byte[][] bytePixData = (byte[][])pixData;
					pixels = new short[bytePixData.length][bytePixData[0].length];
					for(int j=0;j<bytePixData.length;j+= 1){
						for (int k = 0; k < bytePixData[j].length; k++) {
							pixels[j][k] = (short)(0x000000FF & bytePixData[j][k]);
						}
					}
				}
				
				int minValue = pixels[0][0];
				int maxValue = pixels[0][0];
				for (int j = 0; j < pixels[0].length; j++) {
					minValue = Math.min(minValue,pixels[0][i]);
					maxValue = Math.max(maxValue,pixels[0][i]);
				}
				Float pixelSizeX_m = store.getPixelSizeX(0);
				Float pixelSizeY_m = store.getPixelSizeY(0);
				Float pixelSizeZ_m = store.getPixelSizeZ(0);
				int sizeX = store.getSizeX(0);
				int sizeY = store.getSizeY(0);
				int sizeZ = store.getSizeZ(0);
				if (sizeZ > 1){
					throw new RuntimeException("3D images not yet supported");
				}
				Extent extent = null;
				if (pixelSizeX_m!=null && pixelSizeY_m!=null && pixelSizeZ_m!=null && pixelSizeX_m>0 && pixelSizeY_m>0 && pixelSizeZ_m>0){
					extent = new Extent(pixelSizeX_m*sizeX*1e6,pixelSizeY_m*sizeY*1e6,pixelSizeZ_m*sizeZ*1e6);
				}
				System.out.println("reading image "+i+", z="+zct[0]+", channel="+zct[1]+", time="+zct[2]+", pixelType="+pixelType+", numSeries="+seriesCount+", size=("+(pixelSizeX_m*1e6)+","+(pixelSizeY_m*1e6)+","+(pixelSizeZ_m*1e6)+") um, dim=("+sizeX+","+sizeY+","+sizeZ+"), value in ["+minValue+","+maxValue+"]");
				images[i] = new UShortImage(pixels[0],extent,sizeX,sizeY,sizeZ);
			}
			
			System.out.println("before series, image size from Metadata Store("+
					store.getSizeX(0)+","+
					store.getSizeY(0)+","+
					store.getSizeZ(0)+","+
					store.getSizeC(0)+","+
					store.getSizeT(0)+")");
			
			Integer ii = new Integer(0);			
			System.out.println("creationDate: "+store.getCreationDate(ii));
			System.out.println("description: "+store.getDescription("description??", ii));
			System.out.println("dimension order: "+store.getDimensionOrder(ii));
			System.out.println("image name: "+store.getImageName(ii));
			System.out.println("pixel type: "+store.getPixelType(ii));
			System.out.println("stage name: "+store.getStageName("stage name??", ii));
			System.out.println("big endian: "+store.getBigEndian(ii));
			System.out.println("pixel size X: "+store.getPixelSizeX(ii));
			System.out.println("pixel size Y: "+store.getPixelSizeY(ii));
			System.out.println("pixel size Z: "+store.getPixelSizeZ(ii));
			System.out.println("pixel size C: "+store.getPixelSizeC(ii));
			System.out.println("pixel size T: "+store.getPixelSizeT(ii));
			Float pixelSizeX = store.getPixelSizeX(ii);
			if (pixelSizeX!=null){
				System.out.println("   image Size X: "+(store.getSizeX(ii)*pixelSizeX.floatValue()*1e6)+" microns");
			}
			Float pixelSizeY = store.getPixelSizeX(ii);
			if (pixelSizeY!=null){
				System.out.println("   image Size Y: "+(store.getSizeY(ii)*pixelSizeY.floatValue()*1e6)+" microns");
			}
			System.out.println("size X: "+store.getSizeX(ii));
			System.out.println("size Y: "+store.getSizeY(ii));
			System.out.println("size Z: "+store.getSizeZ(ii));
			System.out.println("size C: "+store.getSizeC(ii));
			System.out.println("size T: "+store.getSizeT(ii));
			System.out.println("stage X: "+store.getStageX(ii));
			System.out.println("stage Y: "+store.getStageY(ii));
			System.out.println("stage Z: "+store.getStageZ(ii));
			
			for (int i=0; i<formatReader.getSeriesCount(imageID); i++) {
				formatReader.setSeries(imageID, i);
	
				System.out.println("image size from imageReader("+
						formatReader.getSizeX(imageID)+","+
						formatReader.getSizeY(imageID)+","+
						formatReader.getSizeZ(imageID)+","+
						formatReader.getSizeC(imageID)+","+
						formatReader.getSizeT(imageID)+")");
				System.out.println("image size from Metadata Store("+
						store.getSizeX(new Integer(i))+","+
						store.getSizeY(new Integer(i))+","+
						store.getSizeZ(new Integer(i))+","+
						store.getSizeC(new Integer(i))+","+
						store.getSizeT(new Integer(i))+")");
				
			}
			
			// Read in the time stamps for individual time series images from formatReader.
			double[] times = null;
			Double firstTimeStamp = (Double)formatReader.getMetadataValue(imageID, "TimeStamp0");
			if (firstTimeStamp != null) {
				times = new double[images.length];
				double firstTimeStampVal = firstTimeStamp.doubleValue();
				for (int i = 0; i < times.length; i++) {
					Double timeStamp = (Double)formatReader.getMetadataValue(imageID, "TimeStamp"+i);
					times[i] = timeStamp.doubleValue() - firstTimeStampVal;
				}
			} else{
				System.out.println(" Specified image file format does not have time stamp values.");
			}
	
			ImageDataset imageDataset = new ImageDataset(images,times);
	
			return imageDataset;
		}finally{
			if(formatReader != null){
				formatReader.close();
			}
		}
	}

}

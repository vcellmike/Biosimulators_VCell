/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.client.desktop.biomodel.pathway;

import java.util.List;

import org.vcell.pathway.BioPaxObject;

import cbit.vcell.biomodel.BioModel;
import cbit.vcell.client.desktop.biomodel.SelectionManager;

public interface PathwayEditor {
	
	public SelectionManager getSelectionManager();
	public BioModel getBioModel();
	public List<BioPaxObject> getSelectedBioPaxObjects();

}

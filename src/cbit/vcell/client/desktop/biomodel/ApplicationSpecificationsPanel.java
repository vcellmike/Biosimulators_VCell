/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.client.desktop.biomodel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.vcell.model.rbm.NetworkConstraintsPanel;

import cbit.vcell.client.constants.GuiConstants;
import cbit.vcell.client.desktop.biomodel.DocumentEditorTreeModel.DocumentEditorTreeFolderClass;
import cbit.vcell.client.desktop.biomodel.SelectionManager.ActiveView;
import cbit.vcell.client.desktop.biomodel.SelectionManager.ActiveViewID;
import cbit.vcell.mapping.SimulationContext;
import cbit.vcell.mapping.gui.InitialConditionsPanel;
import cbit.vcell.mapping.gui.MembraneConditionsPanel;
import cbit.vcell.mapping.gui.ReactionSpecsPanel;

@SuppressWarnings("serial")
public class ApplicationSpecificationsPanel extends ApplicationSubPanel {
	public static interface Specifier {
		ActiveViewID getActiveView( );
		void setSelectionManager(SelectionManager s);
		void setIssueManager(IssueManager i);
		void setSearchText(String s);
		void setSimulationContext(SimulationContext newValue);
	}
	
	/*
	private InitialConditionsPanel initialConditionsPanel;
	private ReactionSpecsPanel reactionSpecsPanel;	
	*/
	private NetworkConstraintsPanel networkConstraintsPanel;	
	//private MembraneConditionsPanel membraneConditionsPanel; 
	private JTextField textField_1;
	private static class SpecifierComponent {
		final Specifier setter;
		final JComponent component;
		public SpecifierComponent(Specifier setter, JComponent component) {
			super();
			this.setter = setter;
			this.component = component;
		}
		
		
	}
	private ArrayList<SpecifierComponent> subPanels;
	
	public ApplicationSpecificationsPanel() {
		super();
		subPanels = new ArrayList<>();
		initialize();
	}
	
	/**
	 * setup tab component
	 * @param id for looking up later 
	 * @param cmpt must implement {@link ApplicationSpecificationsPanel.Specifier}
	 */
	void setupTab(String title, ActiveViewID id, JComponent cmpt) {
		cmpt.setBorder(GuiConstants.TAB_PANEL_BORDER);
		tabbedPane.add(title,cmpt);
		subPanels.add( new SpecifierComponent( (Specifier) cmpt, cmpt)  );
	}

	@Override
	protected void initialize(){
		super.initialize();	
		InitialConditionsPanel initialConditionsPanel = new InitialConditionsPanel();
		ReactionSpecsPanel reactionSpecsPanel = new ReactionSpecsPanel();
		networkConstraintsPanel = new NetworkConstraintsPanel();
		MembraneConditionsPanel membraneConditionsPanel = new MembraneConditionsPanel();
		
		setupTab("Species",ActiveViewID.species_settings,initialConditionsPanel);
		setupTab("Reaction",ActiveViewID.reaction_setting,reactionSpecsPanel);
		setupTab("Membrane",ActiveViewID.membrane_setting,membraneConditionsPanel);
		setupTab("Network",ActiveViewID.network_setting,networkConstraintsPanel);
		
		
		JPanel searchPanel = new JPanel();
		GridBagLayout gbl_searchPanel = new GridBagLayout();
		gbl_searchPanel.columnWeights = new double[]{0.0, 1.0};
		searchPanel.setLayout(gbl_searchPanel);
		
		JLabel lblNewLabel = new JLabel("Search");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		searchPanel.add(lblNewLabel, gbc_lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				searchTable();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				searchTable();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				searchTable();
			}
		});
		textField_1.putClientProperty("JTextField.variant", "search");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(5, 5, 5, 5);
		gbc_textField_1.weightx = 1.0;
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 0;
		searchPanel.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		
		add(searchPanel, BorderLayout.SOUTH);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				searchTable();
			}
		});
		
		networkConstraintsPanel.setVisible(false);
	}
	
	private void searchTable() {		
		final int idx = tabbedPane.getSelectedIndex();
		for (SpecifierComponent spc : subPanels) {
			if (idx == tabbedPane.indexOfComponent(spc.component)) {
				spc.setter.setSearchText(textField_1.getText());
				return;
			}
		}
	}

	@Override
	public void setSimulationContext(SimulationContext newValue) {
		super.setSimulationContext(newValue);
		
		for (SpecifierComponent spc : subPanels) {
			spc.setter.setSimulationContext(newValue);
		}
		if(simulationContext.isRuleBased()) {
			networkConstraintsPanel.setVisible(false);
		} else {	// this panel only for flattened rule based applications
			final int indexOfNetworkTab = tabbedPane.indexOfComponent(networkConstraintsPanel);
			networkConstraintsPanel.setVisible(true);
			 if(simulationContext.getModel().getRbmModelContainer().isEmpty()) {
				 // TODO: here is should be initialized to false if rbm model container is empty...
				 // but we should monitor the container and enable the panel as soon as a molecular type is created
				 tabbedPane.setEnabledAt(indexOfNetworkTab, true);
			 } else {
				 tabbedPane.setEnabledAt(indexOfNetworkTab, true);
			 }
		}
	}

	@Override
	public void setSelectionManager(SelectionManager selectionManager) {
		super.setSelectionManager(selectionManager);
		for (SpecifierComponent spc : subPanels) {
			spc.setter.setSelectionManager(selectionManager);
		}
	}
	
	@Override
	public void setIssueManager(IssueManager issueManager) {
		super.setIssueManager(issueManager);
		for (SpecifierComponent spc : subPanels) {
			spc.setter.setIssueManager(issueManager);
		}
	}
	
	@Override
	protected void onActiveViewChange(ActiveView activeView) {
		super.onActiveViewChange(activeView);
		if (DocumentEditorTreeFolderClass.SPECIFICATIONS_NODE.equals(activeView.getDocumentEditorTreeFolderClass())) {
			ActiveViewID id = activeView.getActiveViewID();
			if (id != null) {
				for (SpecifierComponent spc : subPanels) {
					if (spc.setter.getActiveView() == id) {
						tabbedPane.setSelectedComponent(spc.component);
						return;
					}
				}
			}
		}
	}

	@Override
	public ActiveView getActiveView() {
		Component selectedComponent = tabbedPane.getSelectedComponent();
		ActiveViewID activeViewID = null;
		for (SpecifierComponent spc : subPanels) {
			if (selectedComponent == spc.component) {
				activeViewID = spc.setter.getActiveView();
				break;
			}
		}
		return new ActiveView(simulationContext, DocumentEditorTreeFolderClass.SPECIFICATIONS_NODE, activeViewID);
	}
}

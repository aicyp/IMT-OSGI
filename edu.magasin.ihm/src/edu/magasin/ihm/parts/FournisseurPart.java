/*******************************************************************************
 * Copyright (c) 2010 - 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <lars.Vogel@gmail.com> - Bug 419770
 *******************************************************************************/
package edu.magasin.ihm.parts;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import edu.magasin.api.ClientMagasin;
import edu.magasin.api.FournisseurMagasin;
import edu.magasin.api.ListProduitObservable;
import edu.magasin.api.MagasinAchat;
import edu.magasin.ihm.labels.CountColumnLabelProvider;
import edu.magasin.ihm.labels.LabelColumnLabelProvider;

/**
 * Part of a connected User
 * 
 * @author rolucas
 */
public class FournisseurPart {

	// ID of the part
	public static final String FOURNISSEUR_PART_ID = "edu.magasin.ihm.parts.fournisseur";

	private Text txtProductName; // ProductName Input
	private Text txtProductPrice; // ProductPrice Input
	private Text txtProductCount; // ProductCount Input
	private Button btnAddProduct; // Buy button

	// ID of the add product command
	public static final String ADD_PRODUCT_COMMAND_ID = "edu.magasin.ihm.addproduct";

	@Inject
	private MDirtyable dirty; // Used to know if there's unseen message

	@Inject
	ECommandService commandService; // Service used to retrieve the login
									// command
	@Inject
	EHandlerService handlerService; // Service used to launch the Command

	private MPart linkedPart;
	
	private static List<ClientMagasin> clients=new ArrayList<ClientMagasin>();

	/**
	 * Part creation
	 * 
	 * @param parent
	 *            of the Part
	 */
	@PostConstruct
	private void createComposite(Composite parent, @Active MPart activePart) {

		// Store the current MPart object to later be able to check if it is
		// visible
		this.linkedPart = activePart;

		// Create the IHM

		// 1. Set the Layout with a one column rendering
		parent.setLayout(new GridLayout(1, false));

		// create ProductName input field
		txtProductName = new Text(parent, SWT.BORDER);
		txtProductName.setText("Nom du produit... (String)");
		txtProductName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// create ProductPrice input field
		txtProductPrice = new Text(parent, SWT.BORDER);
		txtProductPrice.setText("Prix du produit... (double)");
		txtProductPrice.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// create ProductCount input field
		txtProductCount = new Text(parent, SWT.BORDER);
		txtProductCount.setText("Nombre d'unit�s du produit... (int)");
		txtProductCount.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// 2. Create the buy button
		btnAddProduct = new Button(parent, SWT.BUTTON1);
		btnAddProduct.setText("Ajouter");
		// Add a Listener to launch the Login action (Handler) on use
		btnAddProduct.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// No action
				// Retrieve the add product command
				ParameterizedCommand addProductCommand = commandService.createCommand(ADD_PRODUCT_COMMAND_ID, null);
				// Activate the new client command (the commande is linked with
				// the ClientHandler)
				handlerService.executeHandler(addProductCommand);
				txtProductName.setText("Nom du produit...");
				txtProductPrice.setText("Prix du produit...");
				txtProductCount.setText("Nombre d'unit�s du produit...");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
	

	}

	@PreDestroy
	private void dispose(Composite parent) {
		
	}

	/**
	 * Used to refresh view when it get the focus
	 */
	@Focus
	public void setFocus() {
		dirty.setDirty(false);
		// txtInput.setFocus();
	}

	public void setDirty(@Active MPart activePart) {
		dirty.setDirty(true);
	}

	public String getProductName() {
		return txtProductName.getText();
	}
	
	public String getProductPrice() {
		return txtProductPrice.getText();
	}

	public String getProductCount() {
		return txtProductCount.getText();
	}

}
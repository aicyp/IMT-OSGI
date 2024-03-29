package edu.magasin.ihm.handlers;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import edu.magasin.api.ClientMagasin;
import edu.magasin.api.FournisseurMagasin;
import edu.magasin.api.MagasinAchat;
import edu.magasin.api.ProductCreationException;
import edu.magasin.api.ProductPurchaseException;
import edu.magasin.api.Produit;
import edu.magasin.ihm.parts.ClientPart;
import edu.magasin.ihm.parts.ClientsCreationPart;
import edu.magasin.ihm.parts.FournisseurPart;

/**
 * Handler g�rant l'achat d'un produit.
 * 
 * Se base sur la Part active pour savoir quel client demande l'achat
 * 
 * @author RL
 */
public class BuyProductHandler {

	
	/**
	 * Action du handler, Achat d'un produit au Magasin
	 * 
	 * @param modelService
	 *            Used to create the UserPart model element
	 * @param app
	 *            Used to to retrieve the PartStack
	 * @param partService
	 *            Used to retrieve the LoginPart
	 */
	@Execute
	public void execute(final Shell shell, @Active MPart activePart) {

		// Retrieve the selected products via Transient Part Data
		StructuredSelection selected= (StructuredSelection)activePart.getTransientData().get("selectedProducts");
		
		// Retrieve the Customer via Transient Part Data
		ClientMagasin client= (ClientMagasin)activePart.getTransientData().get("clientComponent");
		
		
		for(Object aSelectedItem : selected.toList()){
			Produit aProduct=(Produit)aSelectedItem;
		
			try {
				client.addAchat(aProduct,1);
			} catch (ProductPurchaseException e) {
				// create a dialog 
				MessageBox dialog =
				    new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				dialog.setText(e.getClass().toString());
				dialog.setMessage(e.getMessage());

				// open dialog
				dialog.open();
			}
		}
	
	}
}

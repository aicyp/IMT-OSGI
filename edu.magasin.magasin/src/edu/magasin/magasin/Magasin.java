package edu.magasin.magasin;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import edu.magasin.api.ListProduitObservable;
import edu.magasin.api.MagasinAchat;
import edu.magasin.api.MagasinApprovisionnement;
import edu.magasin.api.ProductPurchaseException;
import edu.magasin.api.Produit;
import edu.magasin.api.ClientMagasin;

@Component
public class Magasin implements MagasinAchat, MagasinApprovisionnement{

	private ListProduitObservable listeProduits=new ListProduitObservable();

	private List<ClientMagasin> clients=new ArrayList<ClientMagasin>();
	
	@Reference(service = ClientMagasin.class, 
			cardinality = ReferenceCardinality.MULTIPLE, 
			policy = ReferencePolicy.DYNAMIC)
	protected void bindClientMagasin(final ClientMagasin aClient) {
		clients.add(aClient);
		System.out.println("Un nouveau client vient d'arriver : "+aClient.getName());
	}
	protected void unbindClientMagasin(final ClientMagasin aClient) {
		clients.add(aClient);
		System.out.println("Le client '" +aClient.getName()+"' a ferm� sa session et a achet� pour : "+aClient.getMontantAchats());
	}
	
	public ListProduitObservable getListeProduits() {
		return listeProduits;
	}

	@Override
	public void addProduct(Produit aProduct) {
		listeProduits.addProduit(aProduct);
	}

	@Override
	public synchronized void achatProduit(Produit theProduct, int quantity) throws ProductPurchaseException{
		//Seek the product
		Produit foundProduct=null;
		for(Produit aProduit : listeProduits.getProduits()){
			if(theProduct.getId()==aProduit.getId()){
				foundProduct=aProduit;
				break;
			}
		}
		if(foundProduct==null || foundProduct.getQuantite()<quantity){
			throw new ProductPurchaseException("Produit inexistant ou quantit� insuffisante");
		}
		foundProduct.setQuantite(foundProduct.getQuantite()-quantity);
		listeProduits.change();
	}

}

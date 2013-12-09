package tk.nomis_tech.ppimapbuilder.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.hupo.psi.mi.psicquic.wsclient.PsicquicSimpleClient;

import psidev.psi.mi.tab.PsimiTabException;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.model.BinaryInteraction;

public class PMBCreateNetworkTask extends AbstractTask{

	
    private final CyNetworkManager netMgr;
    private final CyNetworkFactory cnf;
    private final CyNetworkNaming namingUtil;
    
    //For the view
    private final CyNetworkViewFactory cnvf;
    private final CyNetworkViewManager networkViewManager;
    
    public PMBCreateNetworkTask(final CyNetworkManager netMgr, final CyNetworkNaming namingUtil, final CyNetworkFactory cnf,
    			CyNetworkViewFactory cnvf, final CyNetworkViewManager networkViewManager){
            this.netMgr = netMgr;
            this.cnf = cnf;
            this.namingUtil = namingUtil;
            
            //For the view
            this.cnvf = cnvf;
            this.networkViewManager = networkViewManager;
    }
    
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		String id = "brca2";
		Collection<BinaryInteraction> binaryInteractions = getBinaryInteractionsFromPsicquicQuery(id);
		createNetworkFromBinaryInteractions (binaryInteractions);
	}
	
	public void createNetworkFromBinaryInteractions (Collection<BinaryInteraction> binaryInteractions) {
		// Create an empty network
        CyNetwork myNet = cnf.createNetwork();
        myNet.getRow(myNet).set(CyNetwork.NAME, namingUtil.getSuggestedNetworkTitle("My Network"));
        
        // Add nodes        
        HashMap<String, CyNode> nodeNameMap = new HashMap<String, CyNode>();
        
        
        for (BinaryInteraction interaction : binaryInteractions) { // For each interaction
        	
        	System.out.println(interaction.getInteractorA().getIdentifiers().get(0).getIdentifier()+
        			"\t"+interaction.getInteractorB().getIdentifiers().get(0).getIdentifier());
        	
        	// Retrieve or create the first node
        	CyNode node1 = null;
        	String name1 = interaction.getInteractorA().getIdentifiers().get(0).getIdentifier();
	        if (nodeNameMap.containsKey(name1)){
	            node1 = nodeNameMap.get(name1);
	        }
	        else {
	            node1 = myNet.addNode();
	            CyRow attributes = myNet.getRow(node1);
	            attributes.set("name", name1);
	            nodeNameMap.put(name1, node1);
	        }
	        // Retrieve or create the second node
	        CyNode node2 = null;
        	String name2 = interaction.getInteractorB().getIdentifiers().get(0).getIdentifier();
	        if (nodeNameMap.containsKey(name2)){
	            node2 = nodeNameMap.get(name2);
	        }
	        else {
	            node2 = myNet.addNode();
	            CyRow attributes = myNet.getRow(node2);
	            attributes.set("name", name2);
	            nodeNameMap.put(name2, node2);
	        }
        	
        	// Add edges
        	myNet.addEdge(node1, node2, true);
        }
        
        //Creation on the view
        if (myNet == null)
            return;
        this.netMgr.addNetwork(myNet);

        final Collection<CyNetworkView> views = networkViewManager.getNetworkViews(myNet);
        CyNetworkView myView = null;
	    if(views.size() != 0)
	            myView = views.iterator().next();
	    
	    if (myView == null) {
	            // create a new view for my network
	            myView = cnvf.createNetworkView(myNet);
	            networkViewManager.addNetworkView(myView);
	    } else {
	            System.out.println("networkView already existed.");
	    }

	}
	
	public Collection<BinaryInteraction> getBinaryInteractionsFromPsicquicQuery(String id) {
		Collection<BinaryInteraction> binaryInteractions = null;
		
		try {
			PsicquicSimpleClient client = new PsicquicSimpleClient(
					"http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/");

			PsimiTabReader mitabReader = new PsimiTabReader();

			InputStream result = client.getByQuery(id);

			binaryInteractions = mitabReader.read(result);

			System.out.println("Interactions found: " + binaryInteractions.size());
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (PsimiTabException e1) {
			e1.printStackTrace();
		}
		
		return binaryInteractions;
	}
	
}

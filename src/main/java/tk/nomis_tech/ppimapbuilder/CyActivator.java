package tk.nomis_tech.ppimapbuilder;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

import tk.nomis_tech.ppimapbuilder.network.PMBCreateNetworkTaskFactory;

import java.util.Properties;

/**
 * The starting point of the plug-in
 */
public class CyActivator extends AbstractCyActivator {
	
	public static BundleContext context;
	
	public CyActivator() {
		super();
	}

	/**
	 * This methods register all services of PPiMapBuilder
	 */
	public void start(BundleContext bc) {
		context = bc;		
		PMBMenuFactory factory = new PMBMenuFactory();		
		Properties props = new Properties();
		props.setProperty("preferredMenu", "Apps.PPiMapBuilder");
		props.setProperty("title", "Query");
		registerService(bc, factory, TaskFactory.class, props);

		
		//Create a simple network
        CyNetworkNaming cyNetworkNamingServiceRef = getService(bc,CyNetworkNaming.class);
        
        CyNetworkFactory cyNetworkFactoryServiceRef = getService(bc,CyNetworkFactory.class);
        CyNetworkManager cyNetworkManagerServiceRef = getService(bc,CyNetworkManager.class);

        //Crate the view
        CyNetworkViewFactory cyNetworkViewFactoryServiceRef = getService(bc,CyNetworkViewFactory.class);
        CyNetworkViewManager cyNetworkViewManagerServiceRef = getService(bc,CyNetworkViewManager.class);
        
		
		PMBCreateNetworkTaskFactory CreateNetworkfactory = new PMBCreateNetworkTaskFactory(cyNetworkNamingServiceRef, cyNetworkFactoryServiceRef,cyNetworkManagerServiceRef, cyNetworkViewFactoryServiceRef,cyNetworkViewManagerServiceRef);

		Properties NetworkProps = new Properties();
		NetworkProps.setProperty("preferredMenu", "Apps.PPiMapBuilder");
		NetworkProps.setProperty("title", "Network");
		registerService(bc, CreateNetworkfactory, TaskFactory.class, NetworkProps);
	}
}


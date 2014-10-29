package ch.picard.ppimapbuilder.data.client;

import ch.picard.ppimapbuilder.data.interaction.client.web.PsicquicService;
import ch.picard.ppimapbuilder.data.interaction.client.web.ThreadedPsicquicClient;
import ch.picard.ppimapbuilder.data.protein.client.web.UniProtEntryClient;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.ProteinOrthologWebCachedClient;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.ThreadedProteinOrthologClientDecorator;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.cache.PMBProteinOrthologCacheClient;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.web.InParanoidClient;
import ch.picard.ppimapbuilder.util.concurrency.ExecutorServiceManager;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Manages all threaded web service clients used in the querying of the network.
 */
public class ThreadedClientManager {

    private final ExecutorServiceManager executorServiceManager;

	private final PMBProteinOrthologCacheClient proteinOrthologCacheClient;
	private final InParanoidClient inParanoidClient;
	private final List<PsicquicService> selectedDatabases;

	private final Set<AbstractThreadedClient> inUseClients;
	private final Set<AbstractThreadedClient> notInUseClients;

	public ThreadedClientManager(ExecutorServiceManager executorServiceManager, List<PsicquicService> selectedDatabases) {
        this.executorServiceManager = executorServiceManager;
        this.selectedDatabases = selectedDatabases;

		// InParanoid Client
		inParanoidClient = new InParanoidClient();
		inParanoidClient.setCache(PMBProteinOrthologCacheClient.getInstance()); //XML response cache

		// PMB ortholog cache client
		proteinOrthologCacheClient = PMBProteinOrthologCacheClient.getInstance();

		this.inUseClients = new HashSet<AbstractThreadedClient>();
		this.notInUseClients = new HashSet<AbstractThreadedClient>();
	}

	private <T> T getUnUsedClientByClass(Class<T> clientClass) {
		for (AbstractThreadedClient client : notInUseClients) {
			if (clientClass.isInstance(client))
				return clientClass.cast(client);
		}
		return null;
	}

	private <T extends AbstractThreadedClient> T register(T client) {
		notInUseClients.remove(client);
		inUseClients.add(client);
		return client;
	}

    public ExecutorService getOrCreateThreadPool() {
        return executorServiceManager.getOrCreateThreadPool();
    }

    public synchronized ThreadedProteinOrthologClientDecorator getOrCreateProteinOrthologClient() {
		ThreadedProteinOrthologClientDecorator client = getUnUsedClientByClass(ThreadedProteinOrthologClientDecorator.class);
		if (client == null) {
			client = new ThreadedProteinOrthologClientDecorator(
					new ProteinOrthologWebCachedClient(inParanoidClient, proteinOrthologCacheClient),
					executorServiceManager
			);
		}
		return register(client);
	}

	public synchronized UniProtEntryClient getOrCreateUniProtClient() {
		UniProtEntryClient client = getUnUsedClientByClass(UniProtEntryClient.class);
		if (client == null) client = new UniProtEntryClient(executorServiceManager);
		return register(client);
	}

	public synchronized ThreadedPsicquicClient getOrCreatePsicquicClient() {
		ThreadedPsicquicClient client = getUnUsedClientByClass(ThreadedPsicquicClient.class);
		if (client == null) client = new ThreadedPsicquicClient(selectedDatabases, executorServiceManager);
		return register(client);
	}

	public ExecutorServiceManager getExecutorServiceManager() {
		return executorServiceManager;
	}

	public synchronized void unRegister(AbstractThreadedClient client) {
		inUseClients.remove(client);
		notInUseClients.add(client);
	}

	public void clear() {
		proteinOrthologCacheClient.clearMemoryCache();
		inUseClients.clear();
		notInUseClients.clear();
		executorServiceManager.clear();
	}

	public void shutdown() {
		executorServiceManager.shutdown();
	}
}
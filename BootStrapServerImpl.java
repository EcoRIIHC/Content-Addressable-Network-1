import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class BootStrapServerImpl extends java.rmi.server.UnicastRemoteObject
		implements BootStrapServer, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bssIP;

	// private static int port = 56546;

	/**
	 * 
	 * @throws java.rmi.RemoteException
	 */
	protected BootStrapServerImpl() throws java.rmi.RemoteException {
		super();
	}

	/**
	 * Set the ipAddress of bss
	 */
	@Override
	public void setBSS(String ipAddress) throws java.rmi.RemoteException {
		System.out.println("bss is: " + ipAddress);
		bssIP = ipAddress;
	}

	/**
	 * Returns the ipAddress of bss
	 */
	@Override
	public String getBSS() throws java.rmi.RemoteException {
		System.out.println("Peer is connecting.");
		if (bssIP != null)
			return bssIP;
		else
			return null;
	}

	/**
	 * Creates the bss and binds it to the registry
	 * 
	 * @param args
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws RemoteException,
			MalformedURLException {
		BootStrapServerImpl bss = new BootStrapServerImpl();
		Naming.rebind("bss", bss);
		// Registry r = LocateRegistry.createRegistry(port);
		// r.rebind("bss", bss);

		System.out.println("bss bound to registry");
	}

}

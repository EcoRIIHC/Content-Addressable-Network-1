
public interface BootStrapServer extends java.rmi.Remote {

	/**
	 * Interface method to set the BootStrapping Server/Node
	 * 
	 * @param ipAddress
	 * @throws java.rmi.RemoteException
	 */
	public void setBSS(String ipAddress) throws java.rmi.RemoteException;

	/**
	 * Interface method to get the BootStrapping Server/Node *
	 * 
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public String getBSS() throws java.rmi.RemoteException;
}
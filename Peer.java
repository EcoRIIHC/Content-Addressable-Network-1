import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface Peer extends java.rmi.Remote {
	public double getX1() throws java.rmi.RemoteException;

	public double getX2() throws java.rmi.RemoteException;

	public double getY1() throws java.rmi.RemoteException;

	public double getY2() throws java.rmi.RemoteException;

	public void setX1(double x1) throws java.rmi.RemoteException;

	public void setX2(double x2) throws java.rmi.RemoteException;

	public void setY1(double y1) throws java.rmi.RemoteException;

	public void setY2(double y2) throws java.rmi.RemoteException;

	public void setArea(double x1, double x2, double y1, double y2)
			throws java.rmi.RemoteException;

	public double getArea() throws java.rmi.RemoteException;

	public String getIPAddress() throws java.rmi.RemoteException;

	public String getHostname(String ipAddress) throws java.rmi.RemoteException;

	public void setIPAddress(String ipAddress) throws java.rmi.RemoteException;

	public List<Neighbor> getNeighbors() throws java.rmi.RemoteException;

	public void addNeighbor(Neighbor neighbor) throws java.rmi.RemoteException;

	public void removeNeighbor(Neighbor neighbor)
			throws java.rmi.RemoteException;

	public void route(int x, int y, String ipAddress)
			throws java.rmi.RemoteException, MalformedURLException,
			NotBoundException;

	public void findZoneToInsertFile(double x, double y, String ipAddress,
			String keyword) throws MalformedURLException, RemoteException,
			NotBoundException;

	public void placeFileInThisZone(String ipAddress, String keyword)
			throws MalformedURLException, RemoteException, NotBoundException;

	public void addFile(String keyword) throws java.rmi.RemoteException;

	public void findZoneToSearchFile(double x, double y, String ipAddress,
			String keyword) throws MalformedURLException, RemoteException,
			NotBoundException;

	public void getFileFromThisZone(String ipAddress, String keyword)
			throws MalformedURLException, RemoteException, NotBoundException;

	public String getFile(String keyword) throws java.rmi.RemoteException;

	public Set<String> getFiles() throws java.rmi.RemoteException;
}

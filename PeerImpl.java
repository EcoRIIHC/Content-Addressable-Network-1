import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
import java.util.Set;


public class PeerImpl extends java.rmi.server.UnicastRemoteObject implements
		Peer, Serializable {

	double fileX1 = 0, fileX2 = 10, fileY1 = 0, fileY2 = 10;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String serverIP = "129.21.30.37";
	// private int port = 56546;
	private double x1;
	private double x2;
	private double y1;
	private double y2;
	private double area;
	private String ipAddress;
	private String hostname;
	private Map<String, String> nodeID = new HashMap<String, String>();
	private List<Neighbor> neighbors;
	private Set<String> files;

	protected PeerImpl() throws RemoteException, UnknownHostException {
		super();
		this.x1 = 0;
		this.x2 = 0;
		this.y1 = 0;
		this.y2 = 0;
		this.ipAddress = InetAddress.getLocalHost().getHostAddress();
		this.hostname = InetAddress.getByName(this.ipAddress).getHostName();
		nodeID.put(this.ipAddress, this.hostname);
		System.out.println("ipAddress of NOT first node: " + ipAddress);
		this.neighbors = new ArrayList<Neighbor>();
		this.files = new HashSet<String>();
	}

	private static Scanner scan;

	public PeerImpl(double x1, double x2, double y1, double y2)
			throws java.rmi.RemoteException, UnknownHostException {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.setArea(x1, x2, y1, y2);
		this.ipAddress = InetAddress.getLocalHost().getHostAddress();
		this.hostname = InetAddress.getByName(this.ipAddress).getHostName();
		nodeID.put(this.ipAddress, this.hostname);
		System.out.println("ipAddres of first node: " + this.ipAddress);
		this.neighbors = new ArrayList<Neighbor>();
		this.files = new HashSet<String>();
	}

	/**
	 * 
	 */
	@Override
	public double getX1() throws RemoteException {
		// TODO Auto-generated method stub
		return this.x1;
	}

	/**
	 * 
	 */
	@Override
	public double getX2() throws RemoteException {
		// TODO Auto-generated method stub
		return this.x2;
	}

	/**
	 * 
	 */
	@Override
	public double getY1() throws RemoteException {
		// TODO Auto-generated method stub
		return this.y1;
	}

	/**
	 * 
	 */
	@Override
	public double getY2() throws RemoteException {
		// TODO Auto-generated method stub
		return this.y2;
	}

	/**
	 * 
	 */
	@Override
	public void setX1(double x1) throws RemoteException {
		// TODO Auto-generated method stub
		this.x1 = x1;
	}

	/**
	 * 
	 */
	@Override
	public void setX2(double x2) throws RemoteException {
		// TODO Auto-generated method stub
		this.x2 = x2;
	}

	/**
	 * 
	 */
	@Override
	public void setY1(double y1) throws RemoteException {
		// TODO Auto-generated method stub
		this.y1 = y1;
	}

	/**
	 * 
	 */
	@Override
	public void setY2(double y2) throws RemoteException {
		// TODO Auto-generated method stub
		this.y2 = y2;
	}

	/**
	 * 
	 */
	@Override
	public String getIPAddress() throws RemoteException {
		// TODO Auto-generated method stub
		return this.ipAddress;
	}

	/**
	 * 
	 */
	@Override
	public void setIPAddress(String ipAddress) throws RemoteException {
		// TODO Auto-generated method stub
		this.ipAddress = ipAddress;
	}

	/**
	 * 
	 */
	@Override
	public void removeNeighbor(Neighbor neighbor) throws RemoteException {
		// TODO Auto-generated method stub
		this.neighbors.remove(neighbor);
	}

	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException, UnknownHostException {
		scan = new Scanner(System.in);

		while (true) {
			System.out.println("1. insert keyword");
			System.out.println("2. search keyword");
			System.out.println("3. view");
			System.out.println("4. join");
			System.out.println("5. leave");

			int choice = scan.nextInt();

			if (choice == 1) {
				insertFile();

			} else if (choice == 2) {
				searchFile();

			} else if (choice == 3) {
				System.out.println("Please enter IP Address.");
				scan = new Scanner(System.in);
				String ipAddress = scan.nextLine();
				view(ipAddress);

			} else if (choice == 4) {
				join();

			} else if (choice == 5) {
				leave();

			} else if (choice == 0) {
				System.exit(0);

			} else
				System.out
						.println("Please enter choice 1 through 5 only or 0 to exit the distributed system");
		}
	}

	private static void leave() throws MalformedURLException, RemoteException,
			NotBoundException {
		System.out.println("Please enter the IP address");
		scan = new Scanner(System.in);
		String ipAddress = scan.nextLine();
		Peer leavePeer = (Peer) Naming.lookup("rmi://" + ipAddress + "/peer");
		List<Neighbor> leavePeerNeighbors = new ArrayList<Neighbor>();
		leavePeerNeighbors = leavePeer.getNeighbors();
		double area = 9999999;
		String ip = null;
		String ipAdd = null;

		for (Neighbor peer : leavePeerNeighbors) {
			if (peer.area < area) {
				area = peer.area;
				ip = peer.ipAddress;
			}
		}

		for (Neighbor peer : leavePeerNeighbors) {
			ipAdd = peer.ipAddress;
			Peer removeLeavePeer = (Peer) Naming.lookup("rmi://" + ipAdd
					+ "/peer");
			List<Neighbor> removeLeavePeerNeighbors = new ArrayList<Neighbor>();
			removeLeavePeerNeighbors = removeLeavePeer.getNeighbors();
			for (Neighbor eachNeighbor : removeLeavePeerNeighbors) {
				if (eachNeighbor.ipAddress == leavePeer.getIPAddress()) {
					removeLeavePeerNeighbors.remove(leavePeer);
					removeLeavePeer.removeNeighbor(eachNeighbor);
				}
			}

		}

		Peer substitutePeer = (Peer) Naming.lookup("rmi://" + ip + "/peer");
		System.out.println("Substitute neighbor takes leaving node's zone");
		double leaveX1 = leavePeer.getX1();
		double leaveX2 = leavePeer.getX2();
		double leaveY1 = leavePeer.getY1();
		double leaveY2 = leavePeer.getY2();

		double subsX1 = substitutePeer.getX1();
		double subsX2 = substitutePeer.getX2();
		double subsY1 = substitutePeer.getY1();
		double subsY2 = substitutePeer.getY2();

		if (leaveX1 < subsX1) {
			substitutePeer.setX1(leaveX1);
		}

		if (leaveX2 > subsX2) {
			substitutePeer.setX2(leaveX2);
		}

		if (leaveY1 < subsY1) {
			substitutePeer.setY1(leaveY1);
		}

		if (leaveY2 > subsY2) {
			substitutePeer.setY2(leaveY2);
		}

	}

	private static void join() throws MalformedURLException, RemoteException,
			NotBoundException, UnknownHostException {

		BootStrapServer bss = (BootStrapServer) Naming.lookup("rmi://"
				+ serverIP + "/bss");
		// Registry r = LocateRegistry.getRegistry(serverIP, port);
		// BootStrapServer bss = (BootStrapServer) r.lookup("bss");
		String bssIP = bss.getBSS();
		System.out.println("bssIP: " + bssIP);
		// PeerImpl peer = new PeerImpl();

		if (bssIP == null) {
			PeerImpl peer = new PeerImpl(0, 10, 0, 10);
			// peer.initPeer();
			bss.setBSS(peer.ipAddress);
			Naming.rebind("peer", peer);
			// Registry r1 = LocateRegistry.createRegistry(port);
			// r1.rebind("peer", peer);
			System.out.println("bss bound to registry");
			System.out.println("x1: " + peer.x1);
			System.out.println("x2: " + peer.x2);
			System.out.println("y1: " + peer.y1);
			System.out.println("y2: " + peer.y2);
			// Peer bsNode = (Peer) Naming.lookup("rmi://" + "192.168.56.1" +
			// "/peer");
			// bsNode.route(5, 5, peer.ipAddress);
		}

		else {
			PeerImpl peer = new PeerImpl();
			Peer bsNode = (Peer) Naming.lookup("rmi://" + bssIP + "/peer");
			// Registry r2 = LocateRegistry.getRegistry(bssIP, port);
			// Peer bsNode = (Peer) r2.lookup("peer");

			Random r = new Random();
			int low = 1;
			int high = 10;
			int rX = r.nextInt(high - low) + low;
			int rY = r.nextInt(high - low) + low;

			System.out.println("rX: " + rX);
			System.out.println("rY: " + rY);

			Naming.rebind("peer", peer);
			// Registry r3 = LocateRegistry.createRegistry(port);
			// r3.rebind("peer", peer);

			System.out.println("peer.ipAddress: " + peer.ipAddress);
			bsNode.route(rX, rY, peer.ipAddress);
		}
	}

	/**
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 * 
	 */
	@Override
	public void route(int x, int y, String newPeerIPAddress)
			throws RemoteException, MalformedURLException, NotBoundException {
		if (this.x1 <= x && this.x2 >= x && this.y1 <= y && this.y2 >= y) {
			try {
				split(newPeerIPAddress);
			} catch (MalformedURLException | NotBoundException
					| UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Neighbor neighbor = new Neighbor();
			String neighborIPAddress = null;
			double xMid = (neighbors.get(0).x1 + neighbors.get(0).x2) / 2;
			double yMid = (neighbors.get(0).y1 + neighbors.get(0).y2) / 2;
			double distance = Math.sqrt(Math.pow(yMid - y, 2)
					+ Math.pow(xMid - x, 2));

			for (int i = 0; i < neighbors.size(); i++) {
				double xiMid = (neighbors.get(i).x1 + neighbors.get(i).x2) / 2;
				double yiMid = (neighbors.get(i).y1 + neighbors.get(i).y2) / 2;
				double idistance = Math.sqrt(Math.pow(yiMid - y, 2)
						+ Math.pow(xiMid - x, 2));

				if (idistance <= distance) {
					distance = idistance;
					neighbor = neighbors.get(i);
					neighborIPAddress = neighbor.ipAddress;
				}
			}

			Peer neighborPeer = (Peer) Naming.lookup("rmi://"
					+ neighborIPAddress + "/peer");
			neighborPeer.route(x, y, newPeerIPAddress);
		}
	}

	private void split(String ipAddress) throws MalformedURLException,
			RemoteException, NotBoundException, UnknownHostException {

		// TODO Auto-generated method stub

		System.out.println("Split this node");
		System.out.println("this.ip: " + this.ipAddress);
		System.out.println("this.x1: " + this.x1);
		System.out.println("this.x2: " + this.x2);
		System.out.println("this.y1: " + this.y1);
		System.out.println("this.y2: " + this.y2);

		List<Neighbor> nodesDivided = new ArrayList<Neighbor>();
		nodesDivided = this.neighbors;

		Peer newPeer = (Peer) Naming.lookup("rmi://" + ipAddress + "/peer");
		// Registry r = LocateRegistry.getRegistry(ipAddress, port);
		// Peer peer = (Peer) r.lookup("peer");
		double dx = this.x2 - this.x1;
		double dy = this.y2 - this.y1;

		if (dx >= dy) {
			double oldX2 = this.x2;
			// this.x1 = this.x1;
			this.x2 = (this.x1 + this.x2) / 2;
			// this.y1 = this.y1;
			// this.y2 = this.y2;
			double xMid = this.x2;

			newPeer.setX1(this.x2);
			newPeer.setX2(oldX2);
			newPeer.setY1(this.y1);
			newPeer.setY2(this.y2);

			double x1 = newPeer.getX1();
			double x2 = newPeer.getX2();
			double y1 = newPeer.getY1();
			double y2 = newPeer.getY2();

			newPeer.setArea(x1, x2, y1, y2);

			/**
			 * This is where the file remove will be placed
			 */

			if (nodesDivided.size() == 0) {
				this.addEachOther(newPeer);
			} else {
				List<Neighbor> leftPeers = new ArrayList<Neighbor>();
				List<Neighbor> rightPeers = new ArrayList<Neighbor>();

				for (int i = 0; i < nodesDivided.size(); i++) {
					Neighbor neighbor = nodesDivided.get(i);
					double neighborX1 = neighbor.x1;
					double neighborX2 = neighbor.x2;
					String neighborIPAddress = neighbor.ipAddress;

					if (neighborX1 <= xMid && neighborX2 <= xMid) {
						leftPeers.add(neighbor);
						addNewNeighbor(neighborIPAddress);

					} else if (neighborX1 >= xMid && neighborX2 >= xMid) {
						rightPeers.add(neighbor);
						addNewNeighbor(neighborIPAddress);
					}
				}
			}
		} else if (dy > dx) {
			double oldY2 = this.y2;
			// this.x1 = this.x1;
			// this.x2 = this.x2;
			// this.y1 = this.y1;
			this.y2 = (this.y1 + this.y2) / 2;
			double yMid = this.y2;

			newPeer.setX1(this.x1);
			newPeer.setX2(this.x2);
			newPeer.setY1(this.y2);
			newPeer.setY2(oldY2);

			double x1 = newPeer.getX1();
			double x2 = newPeer.getX2();
			double y1 = newPeer.getY1();
			double y2 = newPeer.getY2();

			newPeer.setArea(x1, x2, y1, y2);

			/**
			 * This is where the file remove will be placed
			 */

			if (nodesDivided.size() == 0) {
				this.addEachOther(newPeer);
			} else {
				List<Neighbor> topPeers = new ArrayList<Neighbor>();
				List<Neighbor> bottomPeers = new ArrayList<Neighbor>();

				for (int i = 0; i < nodesDivided.size(); i++) {
					Neighbor neighbor = nodesDivided.get(i);
					double neighborY1 = neighbor.y1;
					double neighborY2 = neighbor.y2;
					String neighborIPAddress = neighbor.ipAddress;

					if (neighborY1 <= yMid && neighborY2 <= yMid) {
						bottomPeers.add(neighbor);
						addNewNeighbor(neighborIPAddress);

					} else if (neighborY1 >= yMid && neighborY2 >= yMid) {
						topPeers.add(neighbor);
						addNewNeighbor(neighborIPAddress);
					} else {

					}
				}
			}
		}
	}

	public void addEachOther(Peer newPeer) throws RemoteException {
		Neighbor newNeighbor = new Neighbor();
		newNeighbor.x1 = newPeer.getX1();
		newNeighbor.x2 = newPeer.getX2();
		newNeighbor.y1 = newPeer.getY1();
		newNeighbor.y2 = newPeer.getY2();
		newNeighbor.ipAddress = newPeer.getIPAddress();
		newNeighbor.area = newPeer.getArea();
		System.out.println("newPeer.x1 " + newNeighbor.x1);
		System.out.println("newPeer.x2 " + newNeighbor.x2);
		System.out.println("newPeer.y1 " + newNeighbor.y1);
		System.out.println("newPeer.y2 " + newNeighbor.y2);
		System.out.println("newPeer.ip " + newNeighbor.ipAddress);

		this.neighbors.add(newNeighbor);

		Neighbor splitThisPeer = new Neighbor();
		splitThisPeer.x1 = this.x1;
		splitThisPeer.x2 = this.x2;
		splitThisPeer.y1 = this.y1;
		splitThisPeer.y2 = this.y2;
		splitThisPeer.ipAddress = this.ipAddress;
		splitThisPeer.area = this.area;

		System.out.println("splitPeer.x1 " + splitThisPeer.x1);
		System.out.println("splitPeer.x2 " + splitThisPeer.x2);
		System.out.println("splitPeer.y1 " + splitThisPeer.y1);
		System.out.println("splitPeer.y2 " + splitThisPeer.y2);
		System.out.println("splitPeer.ip " + splitThisPeer.ipAddress);

		try {
			newPeer.addNeighbor(splitThisPeer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addNewNeighbor(String neighborIPAddress)
			throws RemoteException, MalformedURLException, NotBoundException {
		Neighbor neighbor2 = new Neighbor();
		neighbor2.x1 = getX1(); // p3
		neighbor2.x2 = getX2();
		neighbor2.y1 = getY1();
		neighbor2.y2 = getY2();
		neighbor2.ipAddress = getIPAddress();
		neighbor2.area = getArea();

		System.out.println("p3.x1 " + neighbor2.x1);
		System.out.println("p3.x2 " + neighbor2.x2);
		System.out.println("p3.y1 " + neighbor2.y1);
		System.out.println("p3.y2 " + neighbor2.y2);
		System.out.println("p3.ip " + neighbor2.ipAddress);

		Peer neighborPeer = (Peer) Naming.lookup("rmi://" + neighborIPAddress
				+ "/peer");
		neighborPeer.addNeighbor(neighbor2);
	}

	/**
	 * 
	 */
	@Override
	public void addNeighbor(Neighbor neighbor) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Finally, I'm here.");
		this.neighbors.add(neighbor);
	}

	private static void view(String ipAddress) throws MalformedURLException,
			RemoteException, NotBoundException {
		Peer peer = (Peer) Naming.lookup("rmi://" + ipAddress + "/peer");
		// Registry r = LocateRegistry.getRegistry(ipAddress, port);
		// Peer peer = (Peer) r.lookup("peer");
		System.out.println("NodeID: " + peer.getHostname(ipAddress));
		System.out.println("ipAddress: " + peer.getIPAddress());
		System.out.println("Coordinates: ");
		System.out.println("x1: " + peer.getX1());
		System.out.println("x2: " + peer.getX2());
		System.out.println("y1: " + peer.getY1());
		System.out.println("y2: " + peer.getY2());

		List<Neighbor> myNeighbors = new ArrayList<Neighbor>();
		myNeighbors = peer.getNeighbors();

		System.out.println("Neighbors: ");
		for (Neighbor name : myNeighbors) {
			System.out.println(name.ipAddress + ": " + name.x1 + " " + name.x2
					+ " " + name.y1 + " " + name.y2);
		}

		Set<String> myFiles = new HashSet<String>();
		myFiles = peer.getFiles();
		System.out.println("Data Items: ");
		for (String file : myFiles) {
			System.out.println(file);
		}

	}

	public static double determineHashFx(String keyword) {
		double sum = 0;
		char hashFx[] = keyword.toCharArray();

		int i = 1;
		while (i < hashFx.length) {
			sum += hashFx[i];
			i = i + 2;
		}
		return sum % 10;
	}

	public static double determineHashFy(String keyword) {
		double sum = 0;
		char hashFy[] = keyword.toCharArray();

		int i = 0;
		while (i < hashFy.length) {
			sum += hashFy[i];
			i = i + 2;
		}
		return sum % 10;
	}

	public static void insertFile() throws MalformedURLException,
			RemoteException, NotBoundException {
		scan = new Scanner(System.in);
		System.out.println("Please enter the file name");
		String keyword = scan.nextLine();
		double xHash = determineHashFx(keyword);
		double yHash = determineHashFy(keyword);

		BootStrapServer bss = (BootStrapServer) Naming.lookup("rmi://"
				+ serverIP + "/bss");
		// Registry r = LocateRegistry.getRegistry(serverIP, port);
		// BootStrapServer bss = (BootStrapServer) r.lookup("bss");
		String bssIP = bss.getBSS();
		insert(keyword, xHash, yHash, bssIP);
	}

	private static void insert(String keyword, double x, double y, String bssIP)
			throws MalformedURLException, RemoteException, NotBoundException {
		Peer bsNode = (Peer) Naming.lookup("rmi://" + bssIP + "/peer");
		// Registry r2 = LocateRegistry.getRegistry(bssIP, port);
		// Peer bsNode = (Peer) r2.lookup("peer");
		bsNode.findZoneToInsertFile(x, y, bssIP, keyword);
	}

	@Override
	public void findZoneToInsertFile(double x, double y, String ipAddress,
			String keyword) throws MalformedURLException, RemoteException,
			NotBoundException {
		// TODO Auto-generated method stub
		if (this.x1 <= x && this.x2 >= x && this.y1 <= y && this.y2 >= y) {
			this.placeFileInThisZone(ipAddress, keyword);
		} else {
			Neighbor neighbor = new Neighbor();
			String neighborIPAddress = null;
			double xMid = (neighbors.get(0).x1 + neighbors.get(0).x2) / 2;
			double yMid = (neighbors.get(0).y1 + neighbors.get(0).y2) / 2;
			double distance = Math.sqrt(Math.pow(yMid - y, 2)
					+ Math.pow(xMid - x, 2));

			for (int i = 0; i < neighbors.size(); i++) {
				double xiMid = (neighbors.get(i).x1 + neighbors.get(i).x2) / 2;
				double yiMid = (neighbors.get(i).y1 + neighbors.get(i).y2) / 2;
				double idistance = Math.sqrt(Math.pow(yiMid - y, 2)
						+ Math.pow(xiMid - x, 2));

				if (idistance <= distance) {
					distance = idistance;
					neighbor = neighbors.get(i);
					neighborIPAddress = neighbor.ipAddress;
				}
			}

			Peer neighborPeer = (Peer) Naming.lookup("rmi://"
					+ neighborIPAddress + "/peer");
			neighborPeer.findZoneToInsertFile(x, y, neighborIPAddress, keyword);
		}
	}

	@Override
	public void placeFileInThisZone(String ipAddress, String keyword)
			throws MalformedURLException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		Peer peer = (Peer) Naming.lookup("rmi://" + ipAddress + "/peer");
		peer.addFile(keyword);
	}

	/**
	 * 
	 */
	@Override
	public List<Neighbor> getNeighbors() throws RemoteException {
		// TODO Auto-generated method stub
		return this.neighbors;
	}

	public static void searchFile() throws MalformedURLException,
			RemoteException, NotBoundException {
		scan = new Scanner(System.in);
		System.out.println("Please enter the file name search");
		String keyword = scan.nextLine();
		double xHash = determineHashFx(keyword);
		double yHash = determineHashFy(keyword);

		BootStrapServer bss = (BootStrapServer) Naming.lookup("rmi://"
				+ serverIP + "/bss");
		// Registry r = LocateRegistry.getRegistry(serverIP, port);
		// BootStrapServer bss = (BootStrapServer) r.lookup("bss");
		String bssIP = bss.getBSS();
		search(keyword, xHash, yHash, bssIP);
	}

	private static void search(String keyword, double x, double y, String bssIP)
			throws MalformedURLException, RemoteException, NotBoundException {
		Peer bsNode = (Peer) Naming.lookup("rmi://" + bssIP + "/peer");
		// Registry r2 = LocateRegistry.getRegistry(bssIP, port);
		// Peer bsNode = (Peer) r2.lookup("peer");
		bsNode.findZoneToSearchFile(x, y, bssIP, keyword);
	}

	@Override
	public void findZoneToSearchFile(double x, double y, String ipAddress,
			String keyword) throws MalformedURLException, RemoteException,
			NotBoundException {
		// TODO Auto-generated method stub
		if (this.x1 <= x && this.x2 >= x && this.y1 <= y && this.y2 >= y) {
			this.getFileFromThisZone(ipAddress, keyword);
		} else {
			Neighbor neighbor = new Neighbor();
			String neighborIPAddress = null;
			double xMid = (neighbors.get(0).x1 + neighbors.get(0).x2) / 2;
			double yMid = (neighbors.get(0).y1 + neighbors.get(0).y2) / 2;
			double distance = Math.sqrt(Math.pow(yMid - y, 2)
					+ Math.pow(xMid - x, 2));

			for (int i = 0; i < neighbors.size(); i++) {
				double xiMid = (neighbors.get(i).x1 + neighbors.get(i).x2) / 2;
				double yiMid = (neighbors.get(i).y1 + neighbors.get(i).y2) / 2;
				double idistance = Math.sqrt(Math.pow(yiMid - y, 2)
						+ Math.pow(xiMid - x, 2));

				if (idistance <= distance) {
					distance = idistance;
					neighbor = neighbors.get(i);
					neighborIPAddress = neighbor.ipAddress;
				}
			}

			Peer neighborPeer = (Peer) Naming.lookup("rmi://"
					+ neighborIPAddress + "/peer");
			neighborPeer.findZoneToSearchFile(x, y, neighborIPAddress, keyword);
		}
	}

	@Override
	public void getFileFromThisZone(String ipAddress, String keyword)
			throws MalformedURLException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		Peer peer = (Peer) Naming.lookup("rmi://" + ipAddress + "/peer");
		peer.getFile(keyword);
	}

	@Override
	public void addFile(String keyword) {
		// TODO Auto-generated method stub
		System.out.println("File added");
		files.add(keyword);
	}

	@Override
	public String getFile(String keyword) {
		// TODO Auto-generated method stub
		if (files.contains(keyword)) {
			System.out.println("File found.");
			return keyword;
		}
		return "File not found";
	}

	@Override
	public Set<String> getFiles() {
		// TODO Auto-generated method stub
		return files;
	}

	@Override
	public String getHostname(String ipAddress) throws RemoteException {
		// TODO Auto-generated method stub
		return nodeID.get(ipAddress);
	}

	@Override
	public void setArea(double x1, double x2, double y1, double y2)
			throws RemoteException {
		// TODO Auto-generated method stub
		double xMid = x2 - x1;
		double yMid = y2 - y1;
		this.area = xMid * yMid;
	}

	@Override
	public double getArea() throws RemoteException {
		// TODO Auto-generated method stub
		return this.area;
	}

}

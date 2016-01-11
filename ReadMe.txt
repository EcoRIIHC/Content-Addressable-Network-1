Akansha Sharma
Distributed Systems - Project1

The implementation covers:
1. File insert (with a simple String keyword)

2. File retrieve (with a simple String keyword)

3. View peer information (node ID, IP Address, coordinates, data items)

4. Peer join (with random x and y coordinates)

5. Peer leave. Leave is only partially implemented. I wrote functions to find the neighbors of the peer that wishes to leave, where in the neighbor with the smallest area takes the area of the leaving peer, as mentioned in the paper. Furthermore, I iterate through all the neighbors of the peer that wishes to leave and remove the leaving peer from the neighbors' list that include this leaving peer.

To start the distributed system:
1. javac *.java (on each system)
2. rmiregistry & (on each system)
3. java BootStrapServerImpl (runs on queeg.cs.rit.edu)
4. java PeerImpl (runs on any server)
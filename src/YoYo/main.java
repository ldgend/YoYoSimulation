package YoYo;
import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jbotsim.Link;
import jbotsim.Message;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.TopologyListener;
import jbotsim.ui.JTopology;
import jbotsim.ui.JViewer;

public class main {

	public static Topology t = new Topology(); 
	public static TopologyListener tl;
	public static int messageCount = 0;
	public static int numberOfNodes;
	public static double time;
	 
	
	
	public static List<StateNodes> sn = new ArrayList();
	
	 public static void main(String[] args) {

	      
	       time = System.currentTimeMillis();
	       t.disableWireless();
	       t.pause(); 
	       t.setClockSpeed(1);
	       numberOfNodes = 50;
		   NamingNodes n = new NamingNodes();
		   List<Integer> name = n.Naming(numberOfNodes);
		   for(int i=0;i<numberOfNodes;i++){
		   System.out.println(name.get(i));
		   double x = Math.random() *1000;
		   double y = Math.random() *1000;		   
		   sn.add(i, new StateNodes(name.get(i)));
		   t.addNode(x,y,sn.get(i));
		   
		   }
		   
		   
		   for(int i=0;i<numberOfNodes;i++){
			   for(int k=0;k<numberOfNodes;k++){
				 if(t.findNodeById(sn.get(i).getID()).distance(t.findNodeById(sn.get(k).getID()))<180&&(sn.get(i).getID()<sn.get(k).getID())){
				 t.addLink(new Link(t.findNodeById(sn.get(i).getID()),t.findNodeById(sn.get(k).getID()),Link.Type.DIRECTED));
				 }
			 	}
		   }
		   
		   for(int i=1;i<(numberOfNodes-1);i++){
			   if(sn.get(i).getID()<sn.get(i-1).getID()){
			   t.addLink(new Link(t.findNodeById(sn.get(i).getID()),t.findNodeById(sn.get(i-1).getID()),Link.Type.DIRECTED));
			   }
			   else{
				   t.addLink(new Link(t.findNodeById(sn.get(i-1).getID()),t.findNodeById(sn.get(i).getID()),Link.Type.DIRECTED));
			   }
		   }
		   
		   //t.getNodes()
		//   for(int i=0;i<30;i++){
		//	   if(t.getNodes().get(i))
		//   }
		  
		   /* Random Links
		    *   for(int i=0;i<30;i++){
			   int t1 = (int) Math.round(Math.random()*29); 
			   int t2 = (int) Math.round(Math.random()*29); 
	
			   if(t.findNodeById(sn.get(i).getID()).getID()<t.findNodeById(sn.get(t1).getID()).getID()){
			   t.addLink(new Link(t.findNodeById(sn.get(i).getID()), t.findNodeById(sn.get(t1).getID()), Link.Type.DIRECTED));
			   }
			   else{
				   t.addLink(new Link(t.findNodeById(sn.get(t1).getID()), t.findNodeById(sn.get(i).getID()), Link.Type.DIRECTED));
			   }
			   
			   if(t.findNodeById(sn.get(i).getID()).getID()<t.findNodeById(sn.get(t2).getID()).getID()){
				   t.addLink(new Link(t.findNodeById(sn.get(i).getID()), t.findNodeById(sn.get(t2).getID()), Link.Type.DIRECTED));
				   }
				   else{
					   t.addLink(new Link(t.findNodeById(sn.get(t2).getID()), t.findNodeById(sn.get(i).getID()), Link.Type.DIRECTED));
				   }
			   
		   }
		 */
		
		   
		   
	       JViewer j = new JViewer(t);
	       j.setSize(1000, 1000);
	      
	 }
	 
	 public static void flipLink(Node node1, Node node2){
		 t.removeLink(t.getLink(node1, node2, true));
		 t.addLink(new Link(node2,node1,Link.Type.DIRECTED));
		 checkState(node1);
		 checkState(node2);
	 }
	 
 
	 public static void linkPrune(Node node1, Node node2){
		 t.removeLink(t.getLink(node1, node2, true));
	 }	
	 
	 public static void linkConnect(Node node1, Node node2){
		 if(node1.getID()<node2.getID()){
			 t.addLink(new Link(node1, node2, Link.Type.DIRECTED));
		 }
	 }
	 
	 public static void checkState(Node node){
		 if(t.getNodes().size()==1){
		 		node.setState(node.getID()+": LEADER");	
		 		node.setColor(Color.orange);
		 		return;
		 	}

		 if(node.getState()==("DEAD")){
			 t.removeNode(node);
			 return;
		 	}
			if(node.getOutNeighbors().size()==node.getNeighbors().size()){
				node.setState(node.getID() + ": SINK");
				node.setColor(Color.gray);
				return;
			}
			else{
				if(node.getInNeighbors().size()==node.getNeighbors().size()){
					node.setState(node.getID() + ": SOURCE");
					node.setColor(Color.red);
					return;
				}
				else{
					node.setState(node.getID() + ": INTERNAL");
					node.setColor(Color.blue);
					return;
				}
			}
	 }
	 
	

}

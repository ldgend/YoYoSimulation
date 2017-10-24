package YoYo;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import jbotsim.Link;
import jbotsim.Message;
import jbotsim.Node;
import jbotsim.event.ClockListener;
import jbotsim.event.MessageListener;

public class StateNodes extends Node implements ClockListener {

	int MinID = 9999;
	int minMessageCount = 0;
	int minMessageFrom = 0;
	int neighborsCount = 0;
	MessageListener listen;
	YoYoMessage yo = new YoYoMessage();
	
	public String State = "SLEEP";
	public List<Node> pruneNeighbors = new ArrayList();
	public List<Node> flipNeighbors = new ArrayList();
	public List<Node> IncomingNeighbors = new ArrayList();
	public List<Node> OutcomingNeighbors = new ArrayList();
	
	
	public StateNodes(int id){
		this.setID(id);
	}
	
	public List<Node> getNodeNeighbors(){
		return this.getNeighbors();
	}
	
	
	public void countNeighbors(){
		this.neighborsCount = this.getNeighbors().size();
	}
	
	public void pruneNeighbor(Node node){
		YoYoMessage content = new YoYoMessage(); 
		content.prune = true;
		flipLink(node);
		this.send(node, new Message(content));
	}
	
	public void flipNeighbor(Node node){
		YoYoMessage content = new YoYoMessage(); 
		content.flip = true;
		flipLink(node);
		this.send(node, new Message(content));
	}
	
	public void flipLink(Node sender){
		main.flipLink(sender, this);
	}
	
	public void pruneLink(Node sender){
		main.linkPrune(sender, this);
		this.neighborsCount--;
	}

	public boolean findPrune(){
		if(this.getColor()==Color.gray && this.getInLinks().size()==1)
			return true;
		return false;
	}

	public void onMessage(Message msg){
		main.messageCount++;
		YoYoMessage content = (YoYoMessage) msg.getContent();
		YoYoMessage sendContent = new YoYoMessage();
		sendContent.MinID = this.MinID;
		
		if(this.MinID==content.MinID && this.minMessageFrom!=msg.getSender().getID()){
			main.t.removeLink(main.t.getLink(msg.getSender(), this, true));
		}
		
		if(this.getColor()==Color.gray && this.getInLinks().size()==1){
			this.setState("DEAD");
			main.t.removeLink(main.t.getLink(this.getInNeighbors().get(0), this, true));
		}
		
		if(this.MinID>content.MinID){
			this.minMessageCount = 0;
			this.minMessageFrom = msg.getSender().getID();
			this.MinID = content.MinID;
			return;
		}
		if(this.MinID<content.MinID){
			flipLink(msg.getSender());
			return;
		}
		
		if(main.t.getNodes().size()==1){
			this.setState(this.getID()+" :LEADER");
			this.setColor(Color.orange);
			return;
		}
		
		
	}
	
	public void sendYoYoMessage(Node node){
	            YoYoMessage content = new YoYoMessage(); 
	            content.MinID = this.MinID;
	            this.send(node, new Message(content));
	    }
	
	@Override
	public void onClock(){
		YoYoMessage content = new YoYoMessage();
		main.checkState(this);
		if(this.getID()<this.MinID){
			this.MinID = this.getID();
		}
		content.MinID = this.MinID;
		this.sendAll(new Message(content));
		if(main.t.getNodes().size()==1){
			System.out.println("Total Message Count: "+ main.messageCount);
		}
		   if(main.t.getNodes().size()==1){
	    	   main.time = System.currentTimeMillis() - main.time;
	    	   System.out.println("Total time used :" + main.time);
	       }
	}
	
	private class YoYoMessage {
        public boolean flip = false; 
        public boolean prune = false;
        public int MinID = 99999;
        public boolean dag = true;

    }
	
	

}

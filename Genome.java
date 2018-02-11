
import java.util.Random;
import java.util.LinkedList;
import java.util.ArrayList;

import java.util.List;
import java.util.LinkedList;

class Genome{
	int h_nodes = 0,o_nodes,i_nodes,innovationNum;
	LinkedList<ConnectionGene> connectionGenes = new LinkedList<ConnectionGene>();
	NeuralNet body;
	//random Genome generation
	Genome(int input_nodes, int output_nodes, int iNum){//iNum = 1
		i_nodes = input_nodes;
		o_nodes = output_nodes;
		innovationNum = iNum;
		body = new NeuralNet(this);
		Mutate();
	}
	//assigned stats generation(breeding)
	Genome(int input_nodes, int output_nodes, int iNum, LinkedList<ConnectionGene> g){
		connectionGenes = g;
		i_nodes = input_nodes;
		o_nodes = output_nodes;
		int n = i_nodes;
		for(ConnectionGene e : connectionGenes){
			if(e.inID > n){
				n = e.inID;
			}
			if(e.outID > n){
				n = e.outID;
			}
		}
		h_nodes = n - i_nodes;
		innovationNum = iNum;
		body = new NeuralNet(this);
	}
	
	void Mutate(){
		Random rand = new Random();
		int m = rand.nextInt(4);
		int i;
		switch(m){
			case 0://point mutate
				if(connectionGenes.size() != 0){
					i = rand.nextInt(connectionGenes.size());
					connectionGenes.get(i).MutateWeight();
				}
				break;
			case 1://link mutate
				//link generation creates assumption that first n nodes are input nodes
				i = rand.nextInt(h_nodes+i_nodes);
				int o = rand.nextInt(h_nodes+o_nodes)+i_nodes;//chance that i and o specify same node, or a node that already points to it, creating a cycle
				//does not check if nodes already connected.
				connectionGenes.add(new ConnectionGene(innovationNum,i,o));
				break;
			case 2://node mutate
				if(connectionGenes.size() != 0){
					i = rand.nextInt(connectionGenes.size());
					ConnectionGene ci = connectionGenes.get(i);
					ci.enabled = false;
					connectionGenes.add(new ConnectionGene(innovationNum++,ci.inID,h_nodes+1,1.0));
					h_nodes++;
					connectionGenes.add(new ConnectionGene(innovationNum++,h_nodes+1,ci.outID,ci.weight));
				}
				break;
			case 3://enable/disable mutate
				if(connectionGenes.size() != 0){
					i = rand.nextInt(connectionGenes.size());
					connectionGenes.get(i).MutateFunction();
				}
				break;
		}
	}
	
	void setNodeValues(double[] i){//up to dumb programmer to remember input node amount
		for(int x = 0; x < i.length; x++){
			body.net[x].value = i[x];
		}
		//following loop for inputs of multiple stages + making recurrent networks redundant 
		for(int x = i.length; x < body.net.length; x++){
			body.net[x].value = 0;
		}
		body.run();
	}
	
	double[] getNodeValues(int l){// last 'l' nodes
		double[] o = new double[l];
		for(int x = body.net.length - l; x < body.net.length; x++){
			o[x+l-body.net.length] = body.net[x].value;
		}
		return o;
	}
}
/*class Gene{
	
}*/
class NodeGene /*extends Gene*/{
	int id;
	boolean output;
	NodeGene(int id, boolean out){
		this.id = id;
		output = out;
	}
}
class ConnectionGene /*extends Gene*/{
	int inID, outID, innovationNumber;
	double weight;
	boolean enabled = true;
	ConnectionGene(int iNum, int i, int o){
		innovationNumber = iNum;
		inID = i;
		outID = o;
		MutateWeight();
	}
	ConnectionGene(int iNum, int i, int o, double w){
		innovationNumber = iNum;
		inID = i;
		outID = o;
		weight = w;
	}
	void MutateWeight(){
		weight = Math.random() * 4 - 2; // new weight -2.0 to 2.0
	}
	void MutateFunction(){
		enabled = !enabled;
	}
}

/*
Planned implementation
 - iterate through nodes using connections as directors (BFS node exploration), using visited array
 - recurrent networks will have recurring node/connection values 'saved' for next part of full iteration
 
CONS:
 - for iterations with one input, networks with additional recurring connections become redundant
 - no solution implemented for discouraging unnecessary connections 




*/


//PHENOTYPE
class NeuralNet{
	Node[] net;
	boolean[] v;
	int gin,gon;
	NeuralNet(){
		
	}
	NeuralNet(Genome g){
		net = new Node[g.h_nodes + g.o_nodes + g.i_nodes];
		for(int x = 0; x < net.length; x++){
			net[x] = new Node(x);
		}
		for(int x = 0; x < g.connectionGenes.size(); x++){
			if(g.connectionGenes.get(x).enabled){
				net[g.connectionGenes.get(x).outID].addDependency(
					net[g.connectionGenes.get(x).inID],
					g.connectionGenes.get(x).weight
				);
			}
		}	
		gin = g.i_nodes;
		gon = g.o_nodes;
	}
	void run(){
		/*boolean[]*/ v = new boolean[net.length];
		for(int x = net.length - gon; x < net.length; x++){
			calculate(x);
		}
	}
	void calculate(int i){
		if(!v[i]){
			v[i] = true;
			//"calculate" dependencies and apply to summation
			for(int x = 0; x < net[i].dependencies.size(); x++){
				calculate(net[i].dependencies.get(x).giver.label);
				net[i].value += net[i].dependencies.get(x).apply();
			}
		}
	}
}
class Node{
	double value = 0;
	//double sum;
	int label;
	ArrayList<Connection> dependencies = new ArrayList<Connection>(); //(input) <--points [output]
	Node(int x){label = x;}
	void addDependency(Node n, double w){
		dependencies.add(new Connection(n,w));
	}
}
class Connection{
	Node giver;
	double weight;
	Connection(Node n, double w){
		giver = n;
		weight = w;
	}
	double apply(){
		return giver.value * weight;
	}
}
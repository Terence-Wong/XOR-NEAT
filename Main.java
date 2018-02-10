
public class Main{
	final static int GENOME_POOL_NUMBER = 100;
	final static int INPUT_NODES = 2;
	final static int OUTPUT_NODES = 1;
	public static void main(String[]args){
		Genome[] genomePool = new Genome[GENOME_POOL_NUMBER];
		for(int x = 0; x < GENOME_POOL_NUMBER; x++){
			genomePool[x] = new Genome(INPUT_NODES,OUTPUT_NODES,1);
		}
		/*
		int x = 99;
		System.out.println("I am Genome number " + x);
		System.out.println("I have " + genomePool[x].i_nodes + " input nodes, " + genomePool[x].h_nodes 
			+ " hidden nodes, and " + genomePool[x].o_nodes + " output nodes.");
		System.out.println("I have " + genomePool[x].connectionGenes.size() + " connections between them.");
		if(genomePool[x].connectionGenes.size() != 0){
			ConnectionGene g = genomePool[x].connectionGenes.get(0);
			System.out.println("This connects node " + g.inID + " to " + g.outID + " with weight " + g.weight);
		}
		
		Early Genome Generation Testing
		*/
		double[] fitnessRating = new double[GENOME_POOL_NUMBER];
		//measured by error for XOR problem
		
		
	}
}


public class Main{
	final static int GENOME_POOL_NUMBER = 100;
	final static int INPUT_NODES = 2;
	final static int OUTPUT_NODES = 1;
	
	static Genome[] genomePool;
	static double[] fitnessRating;
	
	static double[][] inputs = {
		{0.0,0.0},
		{0.0,1.0},
		{1.0,0.0},
		{1.0,1.0}
	};
	static double[] outputs = {
		0.0,
		1.0,
		1.0,
		0.0
	};
	
	public static void main(String[]args){
		//genome generation
		genomePool = new Genome[GENOME_POOL_NUMBER];
		for(int x = 0; x < GENOME_POOL_NUMBER; x++){
			genomePool[x] = new Genome(INPUT_NODES,OUTPUT_NODES,1);
		}
		
		
		//fitness evaluation
		fitnessRating = new double[GENOME_POOL_NUMBER];
		//measured by error for XOR problem
		for(int x = 0; x < GENOME_POOL_NUMBER; x++){
			double sum = 4.0;
			for(int y = 0; y < inputs.length; y++){
				genomePool[x].setNodeValues(inputs[y]);
				double[] o = genomePool[x].getNodeValues(OUTPUT_NODES);
				//for(int z = 0; z < OUTPUT_NODES; z++){   infrastructure for 2d output array
					sum -= Math.abs(o[0] - outputs[y]); // sum += o[z] - outputs[y][z] 
				//}
			}
			fitnessRating[x] = sum;
		}
		
		//speciation
		
		
		testing();
		
		
	}
	public static void testing(){
		//Early Genome Generation Testing
		int x = 99;
		System.out.println("I am Genome number " + x);
		System.out.println("I have " + genomePool[x].i_nodes + " input nodes, " + genomePool[x].h_nodes 
			+ " hidden nodes, and " + genomePool[x].o_nodes + " output nodes.");
		System.out.println("I have " + genomePool[x].connectionGenes.size() + " connections between them.");
		if(genomePool[x].connectionGenes.size() != 0){
			ConnectionGene g = genomePool[x].connectionGenes.get(0);
			System.out.println("This connects node " + g.inID + " to " + g.outID + " with weight " + g.weight);
		}
		
		//First Genome evaluation Testing
		System.out.println("");
		
		System.out.println("I have a " + fitnessRating[x] + " fitness rating.");
		genomePool[x].setNodeValues(inputs[3]);
		double[] o = genomePool[x].getNodeValues();
		for(int q = 0; q < o.length; q++){
			System.out.println(o[q]);
		}
		
		
	}
}


/*

 - Create a genome pool with n random genomes
 - Take each genome and apply to problem / simulation and calculate the genome fitness
	~ input values using setNodeValues 
	~ receive values using getNodeValues, compare for fitness
	
 - Assign each genome to a species
 - In each species cull the genomes removing some of the weaker genomes
 - Breed each species (randomly select genomes in the species to either crossover or mutate)
 - Repeat all of the above



*/
import java.util.ArrayList;

public class Main{
	final static int GENOME_POOL_NUMBER = 100;
	final static int INPUT_NODES = 2;
	final static int OUTPUT_NODES = 1;
	
	final static double COMPATABILITY_THRESHOLD = 3.0;
	final static double EXCESS_FACTOR = 1.0;
	final static double DISJOINT_FACTOR = 1.0;
	final static double WEIGHT_FACTOR = 0.4;
	final static double NORMALIZER = 1.0;
	
	final static double CULL_PERCENTAGE = 0.5;
	
	static Genome[] genomePool;
	static double[] fitnessRating;
	
	static int[] speciesNumber;
	static ArrayList<Genome> species = new ArrayList<Genome>();
	
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
		speciesNumber = new int[GENOME_POOL_NUMBER];
		for(int x = 0; x < GENOME_POOL_NUMBER; x++){
			speciesNumber[x] = speciate(genomePool[x]);
		}
		
		testing();
		
		
	}
	public static int speciate(Genome i){
		for(int x = 0; x < species.size(); x++){
			if(compatabilityDistance(species.get(x),i) <= COMPATABILITY_THRESHOLD){
				return x;
			}
		}
		species.add(i);
		return species.size()-1;
	}
	public static double compatabilityDistance(Genome a, Genome b){
		int excess 	 = Math.abs(a.innovationNum - b.innovationNum);
		int disjoint = 0;
		int matching = 0;
		double diffW = 0.0;
		
		int q = Math.min(a.innovationNum,b.innovationNum);
		Iterator aIt = a.connectionGenes.listIterator();
		Iterator bIt = b.connectionGenes.listIterator();
		for(int x = 0; x < q; x++){
			ConnectionGene ag = aIt.next();
			ConnectionGene bg = bIt.next();
			if(ag.equals(bg)){
				diffW += ag.weightDifference(bg);
				matching++;
			}else{
				disjoint++;
			}
		}
		
		diffW /= matching;
		return (EXCESS_FACTOR * excess / NORMALIZER)
			+  (DISJOINT_FACTOR * excess / NORMALIZER)
			+  WEIGHT_FACTOR * diffW;
		
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
DISJOINT: different gene that occurs inside of innovation number
EXCESS:	  different gene that occurs outside of innovation number


 - Create a genome pool with n random genomes
 - Take each genome and apply to problem / simulation and calculate the genome fitness
	~ input values using setNodeValues 
	~ receive values using getNodeValues, compare for fitness
	
 - Assign each genome to a species
	
		  c1*E  	 c2*D 		  _				?: Compatibility Distance
	? =  ------  +  ------  +  c3*W				E: Excess Genes
		   N		  N							D: Disjoint Genes
												W: Average weight difference of matching genes (including disabled genes)
												N: "Normalizer", set to # of genes in the larger genome (can be set to 1 for small phenotypes)
											   c#: coefficients to adjust
											   
	Use a compatibility threshold(?t) to speciate.
		
		"Each existing species is represented by a random genome inside
		 the species from the previous generation. A given genome g in the current generation is
		 placed in the first species in which g is compatible with the representative genome of
		 that species. This way, species do not overlap.
		 If g is not comp"
		
	Not using explicit Fitness sharing right now****
	
	Use "explicit fitness sharing" preventing species from overtaking the population
		adjusted fitness F for organism i is calculated according to its compatibility distance from every other organism j in the
		population
		
										 f = initial fitness, F = new fitness
				   f				?(i,j) = compatibility distance from g i to g j
		F = ----------------		sh(  ) = sharing function; set to 0 when input is
			E * sh( ?(i,j) )				 above threshold set(?t), else, 1
										 E = summation of the following
										 
		**Note that denominator reduces to the number of g's in the same species.
		
		Every species is assigned a potentially different number of offspring in proportion to the sum of adjusted fitnesses of its member organisms
	
 - In each species cull the genomes removing some of the weaker genomes
	~eliminate the lowest performing members from the population, then replace the
	 entire population with offspring from remaining members
	
 - Breed each species (randomly select genomes in the species to either crossover or mutate)
 
	In composing the offspring, genes are randomly chosen from either parent at matching genes,
	whereas all excess or disjoint genes are always included from the more fit parent.

 - Repeat all of the above



 
 c1 = 1.0
 c2 = 1.0
 c3 = 0.4
 ?t = 3.0
 
*/
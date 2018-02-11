import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class NetworkVisualizer extends JPanel{
	final static int UPS = 20;//Updates per Second
	
	final static int inputNeuronX = 50;
	final static int inputNeuronY = 50;
	
	JFrame frame;
	Neuron[][] network;
	public NetworkVisualizer(Neuron[][] n){
		network = n;
		frame = new JFrame("Brain Visualizer");
		frame.setSize(800,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.repaint();
		
	}
	@Override
	public void paint(Graphics g){
		super.paint(g);//clears screen
        Graphics2D g2 = (Graphics2D) g;
        //g2.setStroke(new BasicStroke(5));
		g.setColor(Color.gray);
		g.fillRect(0,0,1000,1000);
		
		//seperate inputs, 1d inputs assumed
		for(int x = 0; x < network[0].length; x++){
			g.setColor(Color.black);
			g.fillOval(inputNeuronX + x*30, inputNeuronY, 10, 10);//x,y,w,h
			if(neuronIndicator(network[0][x].value)){
				g.setColor(Color.white);
				g.fillOval(inputNeuronX + x*30 + 1, inputNeuronY + 1, 9, 9);//x,y,w,h
			}
		}
		//outputs
		for(int y = 1; y < network.length; y++){
			for(int x = 0; x < network[y].length; x++){
				g.setColor(Color.black);
				g.fillRect(inputNeuronX + x*30, inputNeuronY + y*30, 10, 10);//x,y,w,h
				if(neuronIndicator(network[y][x].value)){
					g.setColor(Color.white);
					g.fillRect(inputNeuronX + x*30 + 1, inputNeuronY + y*30 + 1, 9, 9);//x,y,w,h
				}
				
				
				//weights
				for(int z = 0; z < network[y-1].length; z++){
					double i = network[y][x].weights[z];
					if(weightIndicator(i)){
						g.setColor(new Color(0, 255, 0, (int)(255*(sigmoid(i)      )) )); // 0.1 minimum
					}else{
						g.setColor(new Color(255, 0, 0, (int)(255*(sigmoid(i)      )) ));
					}
					g.drawLine(inputNeuronX + z*30, inputNeuronY + (y-1)*30, inputNeuronX + x*30, inputNeuronY + y*30);
				}
			}
		}
		
	}
	public boolean neuronIndicator(double value){
		return value > 0.5;
	}
	public boolean weightIndicator(double value){
		return value > 0.0;
	}
	public static double sigmoid(double i){
		double returnValue = 1/(1+Math.exp(-i));
		if(Double.isInfinite(returnValue)){
			return 1.0;
		}
		return returnValue;
	}
	
	/*public static void main(String[]args){
		NetworkVisualizer nv = new NetworkVisualizer();
		
	}*/
}

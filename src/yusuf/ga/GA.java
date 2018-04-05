package yusuf.ga;

import java.util.ArrayList;
import java.util.Random;
import jdk.nashorn.internal.runtime.arrays.ArrayData;

/* @author yusuf */

public class GA {

    private static final double P = Math.PI;           //pi sayısı
    private final double E = Math.E;            // e sayısı
    private final long SEED = 654321;           //rastgelelik için seed sayısı
    private final int MAX_ITERATION = 200;      //algoritmanın calısma sayısı
    private final int PROBLEM = 1;              //işlem yapılacak problem no
    private final int p_size = 10;              // parents sayısı
    private final int c_size = 30;              // childs sayısı
    private double GAMA = 1.1;                  //gama sabiti
    
    private ArrayList<Kromozom> p;              //parents olarak
    private ArrayList<Kromozom> c;              //child olarak
    private Random rand;                        //rastgele üretilecek
        
    public static void main(String[] args) {
    GA ga = new GA();
    ga.initialize();
    ga.run();
    
    System.out.println("problem: 1 "+ ga.fitnessValue(1, -0.0898, 0.7126)); //=-1.316(-0.0898, 0.7126)
    System.out.println("problem: 2 "+ ga.fitnessValue(2, -P, 12.275));  // =0.397887(-π , 12.275), (π , 2.275), (9.42478, 2.475)
    System.out.println("problem: 3 "+ ga.fitnessValue(3, 0.0 ,0.0)); // (0.0, 0.0)=0.0
    }   
    
    private void run(){
        rand = new Random();
        rand.setSeed(SEED);
        
        int t=0;
        while(t<MAX_ITERATION){
            int k=0;        //yeni kromozom indeksi
            double SR=0.0;  //Success Rate
            
            for(int i=0;i<p_size;i++){      //parent üzerinden child üretimi
                for(int j=0; j<(c_size/p_size) ;j++){
                    double moving_x = rand.nextGaussian()*GAMA;
                    double moving_y = rand.nextGaussian()*GAMA;
                    c.get(k).setX(p.get(i).getX() + moving_x);
                    c.get(k).setY(p.get(i).getY() + moving_y);
                    c.get(k).setFitness( fitnessValue(PROBLEM, c.get(k).getX(), c.get(k).getY()));
                    if(c.get(k).getFitness() < p.get(i).getFitness()){
                        SR = SR + 1.0;
                    }
                    k++;                    //c_size'a ulaşacak
                }
            }            
            if(SR/c_size > 0.2)            // mutasyon
                GAMA = GAMA*( 1/0.85);
                else
                GAMA = GAMA*( 0.85 );
            chooseKromozom();               //Kromozom seçimi
            t++;                            // Bir iterasyon sonu
        }
    }
    
    private void chooseKromozom(){
        
        for(int i=0;i<p_size;i++){
            for(int j=0;j<c_size;j++){
                if(c.get(j).getFitness() < p.get(i).getFitness()){
                    p.get(i).setFitness( c.get(j).getFitness());
                    p.get(i).setX(c.get(j).getX());
                    p.get(i).setY(c.get(j).getY());
                } else
                    p.get(i).setFitness( p.get(i).getFitness());            
            }
        }
    }
    
    private void initialize(){       
        p = new ArrayList<>();
        for(int i=0;i<p_size;i++){
            Kromozom kromozom = new Kromozom();
            if(PROBLEM == 1){
                kromozom.setX(randomInterval(-3, 3));
                kromozom.setY(randomInterval(-2, 2));
                kromozom.setFitness(fitnessValue(PROBLEM,kromozom.getX(), kromozom.getY()));
            } else if(PROBLEM == 2){
                kromozom.setX(randomInterval(-5, 10));
                kromozom.setY(randomInterval(0, 15));
                kromozom.setFitness(fitnessValue(PROBLEM,kromozom.getX(),kromozom.getY()));
            } else if(PROBLEM == 3){
                kromozom.setX(randomInterval(-100, 100));
                kromozom.setY(randomInterval(-100, 100));
                kromozom.setFitness(fitnessValue(PROBLEM,kromozom.getX(),kromozom.getY()));
            }            
            p.add(kromozom);
        }
        
        c = new ArrayList<>();
        for (int i = 0; i < c_size; i++) {
            Kromozom kromozom = new Kromozom();
            kromozom.setX(0.0);
            kromozom.setY(0.0);
            kromozom.setFitness(0.0);
            c.add(kromozom);            
        }
    }
        
    private double randomInterval(double low, double high){
        rand = new Random();
        return (high-low)*rand.nextDouble()+low;
    }
    
    private double fitnessValue(int _problem, double x, double y ){
        switch (_problem){
            case 1 : return (4-(2.1*x*x)+(x*x*x*x)/3)*x*x + (x*y) + (-4+4*y*y)*y*y;
            case 2 : return Math.pow((y - (5.1/(4*P*P))*x*x + (5*x)/P - 6), 2)
                    + 10*(1 - 1/(8*P))*Math.cos(x) + 10;
            case 3: return 1 + (1/200)*(x*x + y*y) - Math.cos(x)*(Math.cos(y/Math.sqrt(2)));
            default: return 0.0;
        }       
    }
}

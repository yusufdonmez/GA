package yusuf.ga;

import java.util.ArrayList;
import java.util.Random;
import jdk.nashorn.internal.runtime.arrays.ArrayData;

/* @author yusuf */

public class GA {

    private static long SEED = 654321;           //rastgelelik için seed sayısı
    private static int MAX_ITERATION = 200;      //algoritmanın calısma sayısı
    private final int PROBLEM = 1;              //işlem yapılacak problem no
    private static final double P = Math.PI;
    private static final double E = Math.E;
    private double GAMA = 1.1; //gama sabiti
    
    private ArrayList<Kromozom> p;  //parents olarak
    private static ArrayList<Kromozom> c;  //child olarak
    private static Random rand;
    
    private static int p_size = 10; //m = 10 # parents
    private static int c_size = 30; //l = 30 # childs

        
    public static void main(String[] args) {
    GA ga = new GA();
    ga.initialize();
    ga.run();
    }   
    
    private void run(){
        rand = new Random();
        rand.setSeed(SEED);
        
        int t=0;
        while(t<MAX_ITERATION){
            int k=0;        //yeni kromozom indeksi
            double SR=0.0;  //Success Rate
            
            for(int i=0;i<p_size;i++){
                for(int j=0; j<(c_size/p_size) ;j++){
                    double moving_x = rand.nextGaussian()*GAMA;
                    double moving_y = rand.nextGaussian()*GAMA;
                    c.get(k).setX(p.get(i).getX() + moving_x);
                    c.get(k).setY(p.get(i).getY() + moving_y);
                    c.get(k).setFitness( fitnessValue(PROBLEM, c.get(k).getX(), c.get(k).getY()));
                    if(c.get(k).getFitness() < p.get(i).getFitness()){
                        SR = SR + 1.0;
                    }
                    k++;
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
    
    private double fitness1(double x,double y){
        return (4-(2.1*x*x)+Math.pow(x,4)/3)*x*x + (x*y) + (-4+4*y*y)*y*y;
    }
    
    private double fitness2( double x, double y){
        return Math.abs(x) +Math.abs(y)
                * Math.pow(E, -(Math.sin(x*x)+Math.sin(y*y)) );
    }
    
    private double fitness3( double x, double y){
        return 0.0;
    }
    
    private double fitnessValue(int _problem, double x, double y ){
        switch (_problem){
            case 1 : return (4-(2.1*x*x)+Math.pow(x,4)/3)*x*x + (x*y) + (-4+4*y*y)*y*y;
            case 2 : return Math.abs(x) +Math.abs(y)
                * Math.pow(E, -(Math.sin(x*x)+Math.sin(y*y)) );
            case 3: return 0.0;
            default: return 0.0;
        }       
    }
}

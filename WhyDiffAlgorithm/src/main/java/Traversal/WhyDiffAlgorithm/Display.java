package Traversal.WhyDiffAlgorithm;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Display {
    public void display(){
        EventQueue.invokeLater(new Runnable()
        {
            public void run(){
                ImageFrame frame = new ImageFrame();
               
                /*JScrollPane js = new JScrollPane();
                JPanel jp = new JPanel();
                js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                jp.add(js);
                frame.setContentPane(jp);*/
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        }
        );
    }
}

class ImageFrame extends JFrame{
	
   
    public ImageFrame(){
       	
    	ImageComponent component = new ImageComponent();
        add(component);
        setTitle("ImageTest");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
//        ImageComponent1 component1 = new ImageComponent1();
//        add(component1);
//        setTitle("ImageTest");
//        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        //JPanel container = new JPanel();
        //JScrollPane scrPane = new JScrollPane(container);
      
        //add(scrPane);
       /* JScrollPane scroller = new JScrollPane(component);
        scroller.setAutoscrolls(true);*/
    }

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 500;
}


class ImageComponent extends JComponent{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Image image;
    public ImageComponent(){
        try{
            File image2 = new File("out.gif");
            image = ImageIO.read(image2);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void paintComponent (Graphics g){
        if(image == null) return;

        g.drawImage(image, 860, 0, this);
    }

}
class ImageComponent1 extends JComponent{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Image image;
    public ImageComponent1(){
        try{
            File image2 = new File("out1.gif");
            image = ImageIO.read(image2);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void paintComponent (Graphics g){
        if(image == null) return;

        g.drawImage(image, 860, 0, this);
    }
}
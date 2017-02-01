package javagames.input;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javagames.tools.*;
import javagames.util.*;

import javax.swing.*;


@SuppressWarnings("serial")
public class Paint extends JFrame implements Runnable {
   
   private enum colors{
	   blue,
	   red,
	   green,
	   black
   }
   private enum tools{
	   line,
	   rect,
	   poly,
	   free
   }
   private ShapeTool[] shapes;
   private colors color = colors.black;
   private tools tool = tools.line;
   private FrameRate frameRate;
   private BufferStrategy bs;
   private volatile boolean running;
   private Thread gameThread;
   private Canvas canvas;
   private RelativeMouseInput mouse;
   private KeyboardInput keyboard;
   private Point point = new Point( 0, 0 );
   
   public Paint() {
      frameRate = new FrameRate();
   }
   
   protected void createAndShowGUI() {
      
      canvas = new Canvas();
      canvas.setSize( 1280, 720 );
      canvas.setBackground( Color.WHITE );
      canvas.setIgnoreRepaint( true );
      getContentPane().add( canvas );
      setTitle( "Java Paint" );
      setIgnoreRepaint( true );
      pack();
      
      // Add key listeners
      keyboard = new KeyboardInput();
      canvas.addKeyListener( keyboard );

      // Add mouse listeners
      // For full screen : mouse = new RelativeMouseInput( this );
      mouse = new RelativeMouseInput( canvas );
      canvas.addMouseListener( mouse );
      canvas.addMouseMotionListener( mouse );
      canvas.addMouseWheelListener( mouse );
      
      setVisible( true );
      
      canvas.createBufferStrategy( 2 );
      bs = canvas.getBufferStrategy();
      canvas.requestFocus();
      
      gameThread = new Thread( this );
      gameThread.start();
   }
   
   public void run() {
      running = true;
      frameRate.initialize();
      while( running ) {
         gameLoop();
      }
   }
   
   private void gameLoop() {
      processInput();
      renderFrame();
      sleep( 10L );
   }

   private void renderFrame() {
      do {
         do {
            Graphics g = null;
            try {
               g = bs.getDrawGraphics();
               g.clearRect( 0, 0, getWidth(), getHeight() );
               render( g );
            } finally {
               if( g != null ) {
                  g.dispose();
               }
            }
         } while( bs.contentsRestored() );
         bs.show();
      } while( bs.contentsLost() );
   }
   
   private void sleep( long sleep ) {
      try {
         Thread.sleep( sleep );
      } catch( InterruptedException ex ) { }
   }
   
   private void processInput() {
      
      keyboard.poll();
      mouse.poll();
      
      //Clear the screen
      
      
      
      
      //Select tools and color from menu
      
      
      
      
      
      
      Point p = mouse.getPosition();
      if( mouse.isRelative() ) {
         point.x += p.x;
         point.y += p.y;
      } else {
         point.x = p.x;
         point.y = p.y;
      }
     
      disableCursor();
   }
   
   private void render( Graphics g ) {
      //Draw all shapes
	   
	   
	   
	  //Draw Menu
      g.drawRect(0, 0, 50, 330);
      //Line tool
      g.setColor((tool == tools.line)? Color.black: Color.LIGHT_GRAY);
      g.drawLine(10, 40, 40, 10);
      g.drawRect(10, 10, 30, 30);
      //Rectangle Tool
      g.setColor((tool == tools.rect)? Color.black: Color.LIGHT_GRAY);
      g.drawRect(15, 55, 20, 20);
      g.drawRect(10, 50, 30, 30);
      //PolyLine Tool
      g.setColor((tool == tools.poly)? Color.black: Color.LIGHT_GRAY);
      g.drawRect(10, 90, 30, 30);
      g.drawLine(13, 115, 20, 105);
      g.drawLine(20, 105, 28, 112);
      g.drawLine(28, 112, 37, 93);
      //Free Hand Tool
      g.setColor((tool == tools.free)? Color.black: Color.LIGHT_GRAY);
      g.drawRect(10, 130, 30, 30);
      g.drawLine(14, 135, 14, 155);
      g.drawLine(14, 135, 34, 135);
      g.drawLine(14, 145, 25, 145);
      //Color Selection
      g.setColor(Color.BLUE);
      g.fillRect(10, 170, 30, 30);
      g.setColor(Color.RED);
      g.fillRect(10, 210, 30, 30);
      g.setColor(Color.GREEN);
      g.fillRect(10, 250, 30, 30);
      g.setColor(Color.BLACK);
      g.fillRect(10, 290, 30, 30);
      
      //cycle through colors
      if(mouse.getNotches() > 0){
    	  if(color == colors.black)
    		  color = colors.blue;
    	  else
    		  color = colors.values()[color.ordinal()+1];
      }
      //cycle through tools
      if(mouse.getNotches() < 0 ){
    	  if(tool == tools.free)
    		  tool = tools.line;
    	  else
    		  tool = tools.values()[tool.ordinal()+1];
      }
      
      switch (color){
  		case blue: g.setColor(Color.BLUE);
  			break;
  		case red: g.setColor(Color.RED);
  			break;
  		case green: g.setColor(Color.GREEN);
  			break;
  		case black: g.setColor(Color.BLACK);
  			break;
	  }
      g.drawLine(point.x-10, point.y, point.x+10, point.y);
      g.drawLine(point.x, point.y-10, point.x, point.y+10);
   }
   
   private void disableCursor() {
      Toolkit tk = Toolkit.getDefaultToolkit();
      Image image = tk.createImage( "" );
      Point point = new Point( 0, 0 );
      String name = "CrossHair";
      Cursor cursor = tk.createCustomCursor( image, point, name );
      setCursor( cursor );
   }
   
   protected void onWindowClosing() {
      try {
         running = false;
         gameThread.join();
      } catch( InterruptedException e ) {
         e.printStackTrace();
      }
      System.exit( 0 );
   }
   
   public static void main( String[] args ) {
      final Paint app = new Paint();
      app.addWindowListener( new WindowAdapter() {
         public void windowClosing( WindowEvent e ) {
            app.onWindowClosing();
         }
      });
      SwingUtilities.invokeLater( new Runnable() {
         public void run() {
            app.createAndShowGUI();
         }
      });
   }

}
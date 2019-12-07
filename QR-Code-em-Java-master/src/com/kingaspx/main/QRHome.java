package com.kingaspx.main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.QRCodeDetector;
import org.opencv.videoio.VideoCapture;

import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.detector.WhiteRectangleDetector;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class QRHome extends JFrame {

	private JPanel contentPane;
	
	JButton btnStop = new JButton("STOP");
	JButton btnSTART = new JButton("START");
	JPanel webpanel = new JPanel();
	JLabel qrslbl = new JLabel("");
	
	private DaemonThread mythread = null;
	VideoCapture webSource = null;
	Mat vframe = new Mat();
	MatOfByte mem = new MatOfByte();
	MatOfRect QRCODEDetection = new MatOfRect();
	
	Dimension size = WebcamResolution.QVGA.getSize();
	
 
	
	CascadeClassifier QRDetection=new CascadeClassifier(
		  QRHome.class.getResource("QRHaar.xml").getPath().substring(1).replaceAll("%20"," "));

  
  class DaemonThread implements Runnable
  {
  protected volatile boolean runnable = false;

  @Override
  public  void run()
  {
      synchronized(this)
      {
          while(runnable)
          {
              if(webSource.grab())
              {
		    	try
                      {
		    		
		    		Result[] results=null;
		    		String result_arr = "";
		    		
                          webSource.retrieve(vframe);
//                          QRCODEfun();
                          webpanel.setPreferredSize(size);
                          Graphics g=webpanel.getGraphics();
                          ResultPoint[] cornerPoints=null;
          			    
                       
                          Imgcodecs.imencode(".bmp", vframe, mem);
                       
			    Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));

			    BufferedImage buff = (BufferedImage) im;
//			    Graphics2D g2=(Graphics2D)g.create();
			    Graphics2D g2=buff.createGraphics();
  			    g2.setColor(Color.red);
  			    g2.setStroke(new BasicStroke(5));
			    
			    LuminanceSource source = new BufferedImageLuminanceSource(buff);
			    
	            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	            try {
	            	  QRCodeMultiReader multiReader = new QRCodeMultiReader();
	            	    results = multiReader.decodeMultiple(bitmap);
	            	    BitMatrix bitMatrix = bitmap.getBlackMatrix();
	            	     cornerPoints = new WhiteRectangleDetector(bitMatrix).detect();
//	            	     g2.drawRect((int)(cornerPoints[1].getX()), (int)(cornerPoints[1].getY()), 100, 100);
//	            	    System.out.print("Points");
//	            	    System.out.print("x1:"+cornerPoints[0]);
//	            	    System.out.print(" y1:"+cornerPoints[1].getY());
//	            	    System.out.print(" x2:"+cornerPoints[2]);
//	            	    System.out.print(" y2"+cornerPoints[3]);
//	            	    System.out.println();
//	                result = new MultiFormatReader().decode(bitmap);
	            } catch (NotFoundException e) {
	                //No result...
//	            	System.out.println(e);
	            }
	            if (results != null) {
//	            	if(results.length != 2) {
////	            		result_field.setText("You have incorrect QR Code size");
//	            		System.out.println("You have incorrect QR Code size");
//	            	}else {
	            		for(int i=0;i<results.length;i++)
	                	{
	                		if(i == results.length - 1) {
	                			result_arr += results[i].getText()+"  ";
	                		}else {
	                			result_arr += results[i].getText()+" , ";
	                		}
	                	}
	            		qrslbl.setText(result_arr);

//	            	}
	            		
	            		 for(int i=0;i<results.length;i++)
	            	 	  {
//	            	 		 System.out.println("Data read from QR Code" + (i+1) +": "+results[i]);
	            	 		ResultPoint[] resultPoints = results[i].getResultPoints();
	            	 		
	            	 		for (int j = 0; j < resultPoints.length; j++) {
//	            	 	    System.out.print("  [" + j + "]:");
//	            	 	    System.out.print(" x = " + resultPoints[j].getX());
//	            	 	    System.out.print(", y = " + resultPoints[j].getY());
//	            	 	   System.out.println();
	            	 	
//	            	 			Imgproc.rectangle(vframe,new Point(resultPoints[i].getX(),resultPoints[i].getY()),new Point(resultPoints[i].getX()+resultPoints[i].width,resultPoints[i].getY()+resultPoints[i].height), new Scalar(0,255,0), 2);
	            	 			
	            	 	  }
//	            	 		Imgproc.rectangle(vframe,new Point(resultPoints[0].getX(),resultPoints[0].getY()),new Point(resultPoints[2].getX(),resultPoints[2].getY()), new Scalar(0,255,0), 2);
	            	 		
//	            	 		if (g.drawImage(buff, 0, 0, getWidth(), getHeight() , 0, 0, buff.getWidth(), buff.getHeight(), null))
//	            	 		g2.drawRect((int)(resultPoints[1].getX()), (int)(resultPoints[1].getY()), (int)((resultPoints[2].getX()-resultPoints[1].getX())), (int)((resultPoints[0].getY()-resultPoints[1].getY())));
	            	 		g2.drawRect((int)(resultPoints[1].getX()-20), (int)(resultPoints[1].getY()-20),100,100);
//	            	 		g2.drawRect((int)(resultPoints[1].getX()), (int)(cornerPoints[1].getY()), 100, 100);
//	            	 		System.out.println(cornerPoints[1].getY());
//	            	 		System.out.println(resultPoints[2].getX()-resultPoints[1].getX());
//	            	 		System.out.println(resultPoints[0].getX()-resultPoints[1].getX());
	            	 	  }
            		
	            	
	            }
	            else
	            	qrslbl.setText("NO QR IN IMAGE");
	            
	            
			    

			    if (g.drawImage(buff, 0, 0, getWidth(), getHeight() , 0, 0, buff.getWidth(), buff.getHeight(), null))	    	
			    if(runnable == false)
                          {
			    	System.out.println("Going to wait()");
			    	this.wait();
			    }
			 }
			 catch(Exception ex)
                       {
			    System.out.println(ex);
                       }
              }
          }
      }
   }
 }
  
  public void QRCODEfun()
	{	
		
		Mat grayframe=new Mat();
		
      Imgproc.cvtColor(this.vframe, grayframe, Imgproc.COLOR_BGR2GRAY);
     
      Imgproc.equalizeHist(grayframe, grayframe);   ///histogram equilization

		
		this.QRDetection.detectMultiScale(grayframe, QRCODEDetection); // object of rectofbyte
		
		Rect[] humansarray=QRCODEDetection.toArray();
//		people=humansarray.length;

				for (Rect recte : QRCODEDetection.toArray()) {
							Imgproc.rectangle(vframe, new Point(recte.x, recte.y),
							new Point(recte.x + recte.width, recte.y + recte.height),
							new Scalar(255, 0, 255),3);
				}

				
	}
  
  public void QRCodecv()
  {
	 
  }
  
	/**
	 * Launch the application.
	 */
  
  
  
	public static void main(String[] args) {
		System.load("C:\\opencv\\build\\x64\\vc14\\bin\\opencv_videoio_ffmpeg412_64.dll");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QRHome frame = new QRHome();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public QRHome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 803, 712);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		webpanel.setBounds(10, 143, 767, 519);
		contentPane.add(webpanel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(61, 36, 716, 81);
		contentPane.add(panel_1);
		
		
		btnSTART.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnSTART.setEnabled(false);
				webSource = new VideoCapture(0);
				mythread =   new DaemonThread();
				Thread th = new Thread(mythread);
				th.setDaemon(true);
				mythread.runnable = true;
				th.start();
				
				btnStop.setEnabled(true);
			}
		});
		btnSTART.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnSTART.setBounds(28, 21, 89, 36);
		panel_1.add(btnSTART);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mythread.runnable=false;
				webSource.release();
				btnStop.setEnabled(false);
				btnSTART.setEnabled(true);
			}
		});
		
		
		btnStop.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnStop.setBounds(127, 21, 100, 36);
		panel_1.add(btnStop);
		
		JLabel lblQrCodes = new JLabel("QR CODES :");
		lblQrCodes.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblQrCodes.setBounds(237, 30, 127, 27);
		panel_1.add(lblQrCodes);
		
		
		qrslbl.setFont(new Font("Times New Roman", Font.BOLD, 12));
		qrslbl.setBounds(374, 30, 332, 27);
		panel_1.add(qrslbl);
	}

}

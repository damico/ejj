package org.ejj.java.java3d.runtime;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Launch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JButton btnBg = new JButton();
		final JButton goARbtn =  new JButton();
		final ButtonGroup group = new ButtonGroup();
		
		final ARJava3D frame;
		
		final JRadioButton geom1 = new JRadioButton("Cube");
		final JRadioButton geom2 = new JRadioButton("Cone");
		final JRadioButton geom3 = new JRadioButton("Sphere");
		final JRadioButton geom4 = new JRadioButton("Cylinder");
		
		final JTextField radiusText = new JTextField();
		final JTextField heightText = new JTextField();
		final JTextField zText = new JTextField();
		//final Insets ins = frame.getInsets();
		
		try {
			frame =  new ARJava3D(0,"0","0","0",null);
			frame.setLayout(null);
			frame.setVisible(true);
			
			
			frame.setBackground(Color.gray);
			
			
			frame.setSize(660, 60);
			
			JLabel radiusLabel = new JLabel();
			radiusLabel.setBounds(15, 10, 20, 20);
			radiusLabel.setText("x:");
			
			
			radiusText.setBounds(30, 10, 40,20);
			
			frame.add(radiusLabel);
			frame.add(radiusText);
			
			JLabel heightLabel = new JLabel();
			heightLabel.setBounds(75, 10, 30, 20);
			heightLabel.setText("y:");
			
			
			heightText.setBounds(90, 10, 40,20);
			
			frame.add(heightLabel);
			frame.add(heightText);
			
			JLabel zLabel = new JLabel();
			zLabel.setBounds(135, 10, 20, 20);
			zLabel.setText("z:");
			
			
			zText.setBounds(150, 10, 40,20);
			
			frame.add(zLabel);
			frame.add(zText);
			
			
			geom1.setBounds(200, 10, 60,20);
			
			
			
			geom2.setBounds(270, 10, 58,20);
			
			
			
			geom3.setBounds(328, 10, 68,20);
			//frame.add(geom3);
			
			
			geom4.setBounds(392, 10, 78,20);
			
			
			
		    group.add(geom1);
		    group.add(geom2);
		    group.add(geom3);
		    group.add(geom4);
		    
		  frame.add(geom1);
		  frame.add(geom2);
		  frame.add(geom3);
		  frame.add(geom4);
		  
		  
		  
		 
		  
		  
		  btnBg.setText("BG");
		  btnBg.setBounds(478, 10, 110, 20);
		  
		  btnBg.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int res = fc.showOpenDialog(null);
				if(res == JFileChooser.APPROVE_OPTION){
					File path = fc.getSelectedFile();
					btnBg.setText(path.getAbsolutePath());
				}
				
			}
		});
		  
		  frame.add(btnBg);
		  
		  goARbtn.setText("AR");
		  goARbtn.setBounds(590, 10, 59, 20);
		  frame.add(goARbtn);
		  
		  goARbtn.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					
					frame.dispose();
					try {
						
						int geom;
						 if(geom1.isSelected()) geom = 1;
						  else if(geom2.isSelected()) geom = 2;
						  else if(geom3.isSelected()) geom = 3;
						  else if(geom4.isSelected()) geom = 4;
						  else geom = 0;
						
						String bgPath = btnBg.getText();
						
						String x = radiusText.getText();
						String  y = heightText.getText();
						String  z = zText.getText();
						
						
						
						
						ARJava3D ar = new ARJava3D(geom, x,y,z, bgPath);
						ar.getAR(ar);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				}
			});
		  
		  
		  
		    
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

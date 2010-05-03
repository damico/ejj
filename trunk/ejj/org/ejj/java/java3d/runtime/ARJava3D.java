/* 
 * PROJECT: EJJ AR Java3d sample program.
 * --------------------------------------------------------------------------------
 * The MIT License
 * Copyright (c) 2010 ejj
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */
package org.ejj.java.java3d.runtime;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JFrame;
import javax.vecmath.Vector3d;

import jp.nyatla.nyartoolkit.core.NyARCode;
import jp.nyatla.nyartoolkit.java3d.utils.J3dNyARParam;
import jp.nyatla.nyartoolkit.java3d.utils.NyARSingleMarkerBehaviorHolder;
import jp.nyatla.nyartoolkit.java3d.utils.NyARSingleMarkerBehaviorListener;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * Java3D
 * Behavior
 * TransformGroup
 *
 */
public class ARJava3D extends JFrame implements NyARSingleMarkerBehaviorListener
{
	private static final long serialVersionUID = -8472866262481865377L;

	
	
	private final String CARCODE_FILE = "/opt/jmf/Data/ejj";

	private final String PARAM_FILE = "/opt/jmf/Data/camera_para.dat";

	//NyARToolkit
	private NyARSingleMarkerBehaviorHolder nya_behavior;
	
	private J3dNyARParam ar_param;

	//universe
	private Canvas3D canvas;

	private Locale locale;

	private VirtualUniverse universe;

	
	public void getAR(ARJava3D frame){
		try {
			

			frame.setVisible(true);
			Insets ins = frame.getInsets();
			frame.setSize(640 + ins.left + ins.right, 480 + ins.top + ins.bottom);
			frame.startCapture();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void onUpdate(boolean i_is_marker_exist, Transform3D i_transform3d)
	{
		/*
		 * TODO:Please write your behavior operation code here.
		 */

	}

	public void startCapture() throws Exception
	{
		nya_behavior.start();
	}

	public ARJava3D(int geom, String x, String y, String z, String bgPath) throws Exception
	{
		super("EJJ Java3D AR");
		
		System.out.println(x+" , "+y+" , "+z);
		System.out.println(geom);
		System.out.println(bgPath);

		//NyARToolkit
		NyARCode ar_code = new NyARCode(16, 16);
		ar_code.loadARPattFromFile(CARCODE_FILE);
		ar_param = new J3dNyARParam();
		ar_param.loadARParamFromFile(PARAM_FILE);
		ar_param.changeScreenSize(640, 480);

		
		universe = new VirtualUniverse();
		locale = new Locale(universe);
		canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		View view = new View();
		ViewPlatform viewPlatform = new ViewPlatform();
		view.attachViewPlatform(viewPlatform);
		view.addCanvas3D(canvas);
		view.setPhysicalBody(new PhysicalBody());
		view.setPhysicalEnvironment(new PhysicalEnvironment());

		
		Transform3D camera_3d = ar_param.getCameraTransform();
		view.setCompatibilityModeEnable(true);
		view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
		view.setLeftProjection(camera_3d);

		
		TransformGroup viewGroup = new TransformGroup();
		Transform3D viewTransform = new Transform3D();
		viewTransform.rotY(Math.PI);
		viewTransform.setTranslation(new Vector3d(0.0, 0.0, 0.0));
		viewGroup.setTransform(viewTransform);
		viewGroup.addChild(viewPlatform);
		BranchGroup viewRoot = new BranchGroup();
		viewRoot.addChild(viewGroup);
		locale.addBranchGraph(viewRoot);

		
		Background background = new Background();
		BoundingSphere bounds = new BoundingSphere();
		bounds.setRadius(10.0);
		background.setApplicationBounds(bounds);
		background.setImageScaleMode(Background.SCALE_FIT_ALL);
		background.setCapability(Background.ALLOW_IMAGE_WRITE);
		BranchGroup root = new BranchGroup();
		root.addChild(background);

		//TransformGroup
		TransformGroup transform = new TransformGroup();
		transform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transform.addChild(createSceneGraph(geom, x,y,z, bgPath));
		root.addChild(transform);

		//NyARToolkit & Behavior
		nya_behavior = new NyARSingleMarkerBehaviorHolder(ar_param, 30f, ar_code, 0.08);
		//Behavior
		nya_behavior.setTransformGroup(transform);
		nya_behavior.setBackGround(background);

		//behavior
		root.addChild(nya_behavior.getBehavior());
		nya_behavior.setUpdateListener(this);

		
		locale.addBranchGraph(root);

		
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
	}

	/**
	 
	 * @param bgPath 
	 * @param z 
	 * @param y 
	 * @param x 
	 * @param geom 
	 * @return
	 */
	private Node createSceneGraph(int geom, String x, String y, String z, String bgPath)
	{

		
		if(bgPath == null) bgPath = "/home/jdamico/workspace/ejj/resources/images/earth.jpg";
		
		
		Appearance apBg= new Appearance();
        Material mm = new Material();
        mm.setLightingEnable(true);
        apBg.setMaterial(mm);
		
		
		TextureAttributes texAttr = new TextureAttributes();
	 	texAttr.setTextureMode(TextureAttributes.REPLACE);
		
	 	
	 	
		TextureLoader bgTex = new TextureLoader(bgPath, new String("RGB"),
        TextureLoader.BY_REFERENCE | TextureLoader.Y_UP, this);
        if (bgTex != null) apBg.setTexture(bgTex.getTexture()); apBg.setTextureAttributes(texAttr);
		
		Transform3D object3dMat = new Transform3D();
		TransformGroup object3dTrans = new TransformGroup(object3dMat);
		object3dMat.set(new Vector3d(0.00, 0.0, 20 * 0.001));
		object3dTrans.setTransform(object3dMat);
		
		
		Float xf = Float.valueOf(x);
		Float yf = Float.valueOf(y);
		Float zf = Float.valueOf(z);
		
		if(geom == 1){
			object3dTrans.addChild(new Box(xf, yf, zf, Cylinder.GENERATE_NORMALS | Cylinder.GENERATE_TEXTURE_COORDS | Cylinder.GENERATE_TEXTURE_COORDS_Y_UP, apBg));
		}else if(geom == 2){
			object3dTrans.addChild(new Cone(xf, yf, Cylinder.GENERATE_NORMALS | Cylinder.GENERATE_TEXTURE_COORDS | Cylinder.GENERATE_TEXTURE_COORDS_Y_UP, apBg));	
		}else if(geom == 3){
			object3dTrans.addChild(new Sphere(xf, Cylinder.GENERATE_NORMALS | Cylinder.GENERATE_TEXTURE_COORDS | Cylinder.GENERATE_TEXTURE_COORDS_Y_UP, apBg));
		}else{
			object3dTrans.addChild(new Cylinder(xf, yf, Cylinder.GENERATE_NORMALS | Cylinder.GENERATE_TEXTURE_COORDS | Cylinder.GENERATE_TEXTURE_COORDS_Y_UP, apBg));
		}			
		
		return object3dTrans;
	}
}

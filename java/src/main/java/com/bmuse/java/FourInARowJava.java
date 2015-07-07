package com.bmuse.java;

import playn.java.LWJGLPlatform;

import com.bmuse.core.FourInARow;

public class FourInARowJava {

  public static void main (String[] args) {
    LWJGLPlatform.Config config = new LWJGLPlatform.Config();
    // use config to customize the Java platform, if needed
    //applying IPhone resoulution downscaled with 2x in each dimension
    config.width = 640/2;
    config.height = 1136/2;
    
    LWJGLPlatform plat = new LWJGLPlatform(config);
    new FourInARow(plat);
    plat.start();
  }
}

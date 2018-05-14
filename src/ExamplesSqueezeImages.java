import java.awt.Color;

import javalib.worldimages.FromFileImage;
import tester.Tester;

class ExamplesSqueezeImages {
  
  SqueezeImage image1;
  int image1Width;
  int image1Height;
  
  Pixel a1;
  Pixel b1;
  Pixel c1;
  Pixel a2;
  Pixel b2;
  Pixel c2;
  Pixel a3;
  Pixel b3;
  Pixel c3;
  
  ExamplesSqueezeImages() {
    image1 = new SqueezeImage("Balloons.jpg");
    image1Width = (int) new FromFileImage("Balloons.jpg").getWidth();
    image1Height = (int) new FromFileImage("Balloons.jpg").getHeight();  
  }
  
  void setUpData() {
    a1 = new Pixel(Color.blue);
    b1 = new Pixel(Color.red);
    c1 = new Pixel(Color.green);
    a2 = new Pixel(Color.black);
    b2 = new Pixel(Color.white);
    c2 = new Pixel(Color.gray);
    a3 = new Pixel(Color.yellow);
    b3 = new Pixel(Color.cyan);
    c3 = new Pixel(Color.orange);
    
    a1.right = b1;
    a1.downRight = b2;
    a1.down = a2;
    b1.left = a1;
    b1.downLeft = a2;
    b1.down = b2;
    b1.downRight = c2;
    b1.right = c1;
    c1.left = b1;
    c1.downLeft = b2;
    c1.down = c2;
    a2.top = a1;
    a2.topRight = b1;
    a2.right = b2;
    a2.downRight = b3;
    a2.down = a3;
    b2.top = b1;
    b2.topRight = c1;
    b2.right = c2;
    b2.downRight = c3;
    b2.down = b3;
    b2.downLeft = a3;
    b2.left = a2;
    b2.topLeft = a1;
    c2.top = c1;
    c2.down = c3;
    c2.downLeft = b3;
    c2.left = b2;
    c2.topLeft = b1;
    a3.top = a2;
    a3.topRight = b2;
    a3.right = b3;
    b3.top = b2;
    b3.topRight = c2;
    b3.right = c3;
    b3.left = a3;
    b3.topLeft = a2;
    c3.top = c2;
    c3.left = b3;
    c3.topLeft = b2;
    
  }


  void testGame(Tester t) {
     image1.bigBang(image1Width, image1Height, .5);
  }
  
  
  //test wellFormed pixel
  void testWellFormed(Tester t) {
    
    setUpData();
    t.checkExpect(image1.pixelWellFormed(a1), true);
    t.checkExpect(image1.pixelWellFormed(b1), true);
    t.checkExpect(image1.pixelWellFormed(c1), true);//
    t.checkExpect(image1.pixelWellFormed(a2), true);
    t.checkExpect(image1.pixelWellFormed(b2), true);
    t.checkExpect(image1.pixelWellFormed(c2), true);
    t.checkExpect(image1.pixelWellFormed(a3), true);
    t.checkExpect(image1.pixelWellFormed(b3), true);
    t.checkExpect(image1.pixelWellFormed(c3), true);
    
    b3.top = a2;
    c3.right = a1;
    t.checkExpect(image1.pixelWellFormed(b3), false);
    t.checkExpect(image1.pixelWellFormed(c3), false);
    
  }
  

  //test remove vertical seam
  void testVertSeamRemove(Tester t) {
    
    setUpData();
    
    //straight down
    SeamInfo seam1 = new SeamInfo(b3, 15, new SeamInfo(b2, 10, new SeamInfo(b1, 5, null)));
    //uses only topLeft
    SeamInfo seam2 = new SeamInfo(c3, 15, new SeamInfo(b2, 10, new SeamInfo(a1, 5, null)));
    //uses only topRight
    SeamInfo seam3 = new SeamInfo(a3, 15, new SeamInfo(b2, 10, new SeamInfo(c1, 5, null)));
    //mixed 1
    SeamInfo seam4 = new SeamInfo(b3, 15, new SeamInfo(a2, 10, new SeamInfo(b1, 5, null)));
    
    
    seam1.removeSeamVert();
    
    Pixel current = a1;
    
    while (current != null) {
      Pixel temp = current.down;
      
      while (current != null) {
        System.out.println(current.color);
        current = current.right;
      }
      
      current = temp;
      
    }
    t.checkExpect(a1.right, c1);
    t.checkExpect(a1.downRight, c2);
    t.checkExpect(a2.topRight, c1);
    t.checkExpect(a2.right, c2);
    t.checkExpect(a2.downRight, c3);
    t.checkExpect(a3.topRight, c2);
    t.checkExpect(a3.right, c3);
    t.checkExpect(c1.left, a1);
    t.checkExpect(c1.downLeft, a2);
    t.checkExpect(c2.topLeft, a1);
    t.checkExpect(c2.left, a2);
    t.checkExpect(c2.downLeft, a3);
    t.checkExpect(c3.topLeft, a2);
    t.checkExpect(c3.left, a3);
    
    setUpData();
    
    this.testInitConditions(t);
    
    
    seam2.removeSeamVert();
    //c3, b2, a1
    t.checkExpect(b1.left, null); //r0 g0 b255
    t.checkExpect(a2.right, c2); //r255 g255 b255
    t.checkExpect(c2.left, a2); //r255 g255 b255
    t.checkExpect(c2.downLeft, a2); //r0 g255 b255
    t.checkExpect(b3.right, null); //r255 g200 b0
    t.checkExpect(a1.downRight, c2);
    t.checkExpect(c2.topLeft, a1);
    
    current = b1;
    
    while (current != null) {
      Pixel temp = current.down;
      
      while (current != null) {
        System.out.println(current.color);
        current = current.right;
      }
      
      current = temp;
      
    }
    
    setUpData();
    
    this.testInitConditions(t);
    
    seam3.removeSeamVert();
    //a3, b2, c1
    t.checkExpect(b1.right, null);
    t.checkExpect(a2.right, c2);
    t.checkExpect(c2.left, a2);
    t.checkExpect(b3.left, null);
    t.checkExpect(a1.downRight, c2);
    t.checkExpect(c2.topLeft, a1);
    t.checkExpect(c2.down, b3);
    
    current = a1;
    
    while (current != null) {
      Pixel temp = current.down;
      
      while (current != null) {
        System.out.println(current.color);
        current = current.right;
      }
      
      current = temp;
      
    }
    
    
    setUpData();
    
    this.testInitConditions(t);
    
    seam4.removeSeamVert();
    //b3, a2, b1
    t.checkExpect(a1.right, c1);
    t.checkExpect(c1.left, a1);
    t.checkExpect(a1.down, b2);
    t.checkExpect(b2.top, a1);
    t.checkExpect(a3.right, c3);
    t.checkExpect(c3.left, a3);

  }
  
  void testRemoveSeamHoriz(Tester t) {
    
    //straight horizontal
    SeamInfo seam1 = new SeamInfo(c2, 15, new SeamInfo(b2, 10, new SeamInfo(a2, 5, null)));
    //uses only topLeft
    SeamInfo seam2 = new SeamInfo(c3, 15, new SeamInfo(b2, 10, new SeamInfo(a1, 5, null)));
    //uses only downLeft
    SeamInfo seam3 = new SeamInfo(c1, 15, new SeamInfo(b2, 10, new SeamInfo(a3, 5, null)));
    //mixed 1
    SeamInfo seam4 = new SeamInfo(c2, 15, new SeamInfo(b1, 10, new SeamInfo(a2, 5, null)));
    
    setUpData();
    this.testInitConditions(t);
    
    seam1.removeSeamHoriz();
    t.checkExpect(a1.down, a3);
    t.checkExpect(b1.down, b3);
    t.checkExpect(c1.down, c3);
    t.checkExpect(a3.top, a1);
    t.checkExpect(b3.top, b1);
    t.checkExpect(c3.top, c1);
    t.checkExpect(a1.downRight, b3);
    t.checkExpect(b1.downLeft, a3);
    t.checkExpect(b1.downRight, c3);
    t.checkExpect(c3.topLeft, b1);
    
    
    setUpData();
    this.testInitConditions(t);
    
    seam2.removeSeamHoriz();
    //c1, b2, a1
    t.checkExpect(b3.top, b1);
    t.checkExpect(b1.down, b3);
    t.checkExpect(c2.down, null);
    t.checkExpect(a2.top, null);
    t.checkExpect(b3.top, b1);
    t.checkExpect(a2.down, a3);
    t.checkExpect(c1.downLeft, b3);
    t.checkExpect(a2.downRight, b3);
    t.checkExpect(c2.left, b3);
    
    setUpData();
    this.testInitConditions(t);
    
    seam3.removeSeamHoriz();
    //c3, b2, a3
    t.checkExpect(a2.down, null);
    t.checkExpect(b1.down, b3);
    t.checkExpect(c2.down, null);
    t.checkExpect(a2.left, b3);
    t.checkExpect(b3.top, b1);
    t.checkExpect(a1.downRight, b3);
    t.checkExpect(c1.downLeft, b3);
    t.checkExpect(a2.downRight, null);
    t.checkExpect(c2.left, b3);
    
    setUpData();
    this.testInitConditions(t);
    
    seam4.removeSeamHoriz();
    //c2, b1, a2
    t.checkExpect(b2.top, null);
    t.checkExpect(a1.down, a3);
    t.checkExpect(c1.down, c3);
    t.checkExpect(a1.right, b2);
    t.checkExpect(b2.right, c3);
    t.checkExpect(b2.left, a3);
    t.checkExpect(c1.downLeft, b3);
    t.checkExpect(a1.downRight, b3);
    t.checkExpect(c1.left, b2);
    
  }
  
  void testInitConditions(Tester t) {
    setUpData();
    
    t.checkExpect(a1.right, b1);
    t.checkExpect(a1.downRight, b2);
    t.checkExpect(a1.down, a2);
    t.checkExpect(b1.left, a1);
    t.checkExpect(b1.downLeft, a2);
    t.checkExpect(b1.down, b2);
    t.checkExpect(b1.downRight, c2);
    t.checkExpect(b1.right, c1);
    t.checkExpect(c1.left, b1);
    t.checkExpect(c1.downLeft, b2);
    t.checkExpect(c1.down, c2);
    t.checkExpect(a2.top, a1);
    t.checkExpect(a2.topRight, b1);
    t.checkExpect(a2.right, b2);
    t.checkExpect(a2.downRight, b3);
    t.checkExpect(a2.down, a3);
    t.checkExpect(b2.top, b1);
    t.checkExpect(b2.topRight, c1);
    t.checkExpect(b2.right, c2);
    t.checkExpect(b2.downRight, c3);
    t.checkExpect(b2.down, b3);
    t.checkExpect(b2.downLeft, a3);
    t.checkExpect(b2.left, a2);
    t.checkExpect(b2.topLeft, a1);
    t.checkExpect(c2.top, c1);
    t.checkExpect(c2.down, c3);
    t.checkExpect(c2.downLeft, b3);
    t.checkExpect(c2.left, b2);
    t.checkExpect(c2.topLeft, b1);
    t.checkExpect(a3.top, a2);
    t.checkExpect(a3.topRight, b2);
    t.checkExpect(a3.right, b3);
    t.checkExpect(b3.top, b2);
    t.checkExpect(b3.topRight, c2);
    t.checkExpect(b3.right, c3);
    t.checkExpect(c3.top, c2);
    t.checkExpect(c3.left, b3);
    t.checkExpect(c3.topLeft, b2);
    
  }
  
  
  void testUpdateEnergy(Tester t) {
    
    setUpData();
    
    Pixel current = a1;
    
    while (current != null) {
      Pixel temp = current.down;
      
      while (current != null) {
        current.updateEnergy();
        current = current.right;
      }
      
      current = temp;
      
    }
    
    t.checkInexact(a1.energy, 1.943, .001);
    t.checkInexact(b1.energy, 2.551, .001);
    t.checkInexact(c1.energy, 2.606, .001);
    t.checkInexact(a2.energy, 3.162, .001);
    t.checkInexact(b2.energy, 1.568, .001);
    t.checkInexact(c2.energy, 3.119, .001);
    t.checkInexact(a3.energy, 2.538, .001);
    t.checkInexact(b3.energy, 2.527, .001);
    t.checkInexact(c3.energy, 3.075, .001);
    /*
     * 1.9436506316151
       2.5518174690272426
       2.6064304001810754
       3.1622776601683795
       1.5684063024937929
       3.1197907155934894
       2.5385910352879693
       2.52746779768292
       3.0757350499766307
     * 
     */
    
    t.checkExpect(current, null);
    
    
  }
  
  void testUpdateSeamVert(Tester t) {
    
    setUpData();
    
    Pixel current = a1;
    
    while (current != null) {
      Pixel temp = current.down;
      
      while (current != null) {
        current.updateEnergy();
        current = current.right;
      }
      
      current = temp;
      
    }
    
    t.checkExpect(a1.seam, null);
    a1.updateSeamVert();
    t.checkExpect(a1.seam, new SeamInfo(a1, a1.energy, null));
    
    t.checkExpect(b1.seam, null);
    b1.updateSeamVert();
    t.checkExpect(b1.seam, new SeamInfo(b1, b1.energy, null));
    
    t.checkExpect(c1.seam, null);
    c1.updateSeamVert();
    t.checkExpect(c1.seam, new SeamInfo(c1, c1.energy, null));
    
    t.checkExpect(a2.seam, null);
    a2.updateSeamVert();
    t.checkExpect(a2.seam, new SeamInfo(a2, a2.energy + a1.energy, a1.seam)); //5.1
    
    t.checkExpect(b2.seam, null);
    b2.updateSeamVert();
    t.checkExpect(b2.seam, new SeamInfo(b2, b2.energy + a1.energy, a1.seam)); //3.503
    
    t.checkExpect(c2.seam, null);
    c2.updateSeamVert();
    t.checkExpect(c2.seam, new SeamInfo(c2, c2.energy + b1.energy, b1.seam)); //5.67
    
    t.checkExpect(a3.seam, null);
    a3.updateSeamVert();
    t.checkExpect(a3.seam, new SeamInfo(a3, a3.energy + b2.energy + a1.energy, b2.seam));
    
    t.checkExpect(b3.seam, null);
    b3.updateSeamVert();
    t.checkExpect(b3.seam, new SeamInfo(b3, b3.energy + b2.energy + a1.energy, b2.seam));
    
    t.checkExpect(c3.seam, null);
    c3.updateSeamVert();
    t.checkExpect(c3.seam, new SeamInfo(c3, c3.energy + b2.energy + a1.energy, b2.seam));
    
    
    
    
  }
  
  
void testUpdateSeamHoriz(Tester t) {
    
    setUpData();
    
    Pixel current = a1;
    
    while (current != null) {
      Pixel temp = current.down;
      
      while (current != null) {
        current.updateEnergy();
        current = current.right;
      }
      
      current = temp;
      
    }
    
    /*
     * a1 1.9436506316151
       b1 2.5518174690272426
       c1 2.6064304001810754
       a2 3.1622776601683795
       b2 1.5684063024937929
       c2 3.1197907155934894
       a3 2.5385910352879693
       b3 2.52746779768292
       c3 3.0757350499766307
     * 
     */
    
    t.checkExpect(a1.seam, null);
    a1.updateSeamHoriz();
    t.checkExpect(a1.seam, new SeamInfo(a1, a1.energy + b2.energy + c1.energy, b2.seam));
    
    t.checkExpect(b1.seam, null);
    b1.updateSeamHoriz();
    t.checkExpect(b1.seam, new SeamInfo(b1, b1.energy + c1.energy, c1.seam)); //5.157
    
    t.checkExpect(c1.seam, null);
    c1.updateSeamHoriz();
    t.checkExpect(c1.seam, new SeamInfo(c1, c1.energy, null));
    
    t.checkExpect(a2.seam, null);
    a2.updateSeamHoriz();
    t.checkExpect(a2.seam, new SeamInfo(a2, a2.energy + b2.energy + c1.energy, b2.seam)); 
    
    t.checkExpect(b2.seam, null);
    b2.updateSeamHoriz();
    t.checkExpect(b2.seam, new SeamInfo(b2, b2.energy + c1.energy, c1.seam)); //4.174
    
    t.checkExpect(c2.seam, null);
    c2.updateSeamHoriz();
    t.checkExpect(c2.seam, new SeamInfo(c2, c2.energy, null)); 
    
    t.checkExpect(a3.seam, null);
    a3.updateSeamHoriz();
    t.checkExpect(a3.seam, new SeamInfo(a3, a3.energy + b2.energy + c3.energy, b2.seam));
    
    t.checkExpect(b3.seam, null);
    b3.updateSeamHoriz();
    t.checkExpect(b3.seam, new SeamInfo(b3, b3.energy + c3.energy, c3.seam)); //5.602
    
    t.checkExpect(c3.seam, null);
    c3.updateSeamHoriz();
    t.checkExpect(c3.seam, new SeamInfo(c3, c3.energy, null));
    
    
    
    
  }
  
  
  
  
}




















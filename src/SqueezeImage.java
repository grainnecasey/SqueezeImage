import java.awt.Color;
import java.util.ArrayList;
import javalib.impworld.*;
import javalib.worldimages.*;

public class SqueezeImage extends World {

  FromFileImage origImg;
  Pixel curPixel;
  ComputedPixelImage newImg;
  int width;
  int height;
  int maxColIdx;
  int maxRowIdx;
  ArrayList<SeamInfo> removedSeams;
  boolean highlight = true;
  SeamInfo curLowest;
  boolean continueRemoving = false;
  String removeType = "";
  String showType = "r";

  SqueezeImage(String fileName) {
    origImg = new FromFileImage(fileName);
    width = (int) origImg.getWidth();
    height = (int) origImg.getHeight();

    newImg = new ComputedPixelImage(width, height);

    ArrayList<ArrayList<Pixel>> pixels = new ArrayList<ArrayList<Pixel>>();
    for (int x = 0; x < width; x += 1) {
      ArrayList<Pixel> col = new ArrayList<Pixel>();
      for (int y = 0; y < height; y += 1) {
        Pixel temp = new Pixel(origImg.getColorAt(x, y));
        col.add(temp);
      }
      pixels.add(col);
    }

    this.maxColIdx = pixels.size() - 1;
    this.maxRowIdx = pixels.get(0).size() - 1;
    this.curPixel = pixels.get(0).get(0);

    this.connectNeighbors(pixels);
    this.updateEnergy();

    for (int x = 0; x < width; x += 1) {
      for (int y = 0; y < height; y += 1) {
        newImg.setPixel(x, y, pixels.get(x).get(y).color);
      }
    }

    this.removedSeams = new ArrayList<SeamInfo>();

    this.updateCurLowest();

    if (!(this.allWellFormed())) {
      throw new RuntimeException("Is not well formed");
    }

  }

  void connectNeighbors(ArrayList<ArrayList<Pixel>> pixels) {
    for (ArrayList<Pixel> col : pixels) {
      for (Pixel pixel : col) {
        int curRowIdx = col.indexOf(pixel);
        int curColIdx = pixels.indexOf(col);

        // top left
        if ((curColIdx >= 1) && (curRowIdx >= 1)) {
          pixel.topLeft = pixels.get(curColIdx - 1).get(curRowIdx - 1);
        }
        // up
        if (curRowIdx >= 1) {
          pixel.top = pixels.get(curColIdx).get(curRowIdx - 1);
        }
        // top right
        if ((curColIdx + 1 <= maxColIdx) && (curRowIdx >= 1)) {
          pixel.topRight = pixels.get(curColIdx + 1).get(curRowIdx - 1);
        }
        // left
        if (curColIdx >= 1) {
          pixel.left = pixels.get(curColIdx - 1).get(curRowIdx);
        }
        // right
        if (curColIdx + 1 <= maxColIdx) {
          pixel.right = pixels.get(curColIdx + 1).get(curRowIdx);
        }
        // downleft
        if ((curColIdx >= 1) && (curRowIdx + 1 <= maxRowIdx)) {
          pixel.downLeft = pixels.get(curColIdx - 1).get(curRowIdx + 1);
        }
        // down
        if (curRowIdx + 1 <= maxRowIdx) {
          pixel.down = pixels.get(curColIdx).get(curRowIdx + 1);
        }
        // downright
        if ((curColIdx + 1 <= maxColIdx) && (curRowIdx + 1 <= maxRowIdx)) {
          pixel.downRight = pixels.get(curColIdx + 1).get(curRowIdx + 1);
        }
      }
    }
  }

  boolean allWellFormed() {

    Pixel current = this.curPixel;
    Pixel temp;

    current = this.curPixel;

    while (current.down != null) {
      temp = current.down;
      while (current.right != null) {
        if (!(this.pixelWellFormed(current))) {
          return false;
        }
        current = current.right;
      }
      current = temp;
    }

    return true;
  }

  boolean pixelWellFormed(Pixel p) {

    Pixel topLeft = p.topLeft;
    Pixel top = p.top;
    Pixel topRight = p.topRight;
    Pixel left = p.left;
    Pixel right = p.right;
    Pixel downLeft = p.downLeft;
    Pixel down = p.down;
    Pixel downRight = p.downRight;

    boolean result = true;

    if (topLeft != null) {
      result = result && topLeft.downRight == p;
    }
    if (top != null) {
      result = result && top.down == p;
    }
    if (topRight != null) {
      result = result && topRight.downLeft == p;
    }
    if (left != null) {
      result = result && left.right == p;
    }
    if (right != null) {
      result = result && right.left == p;
    }
    if (downLeft != null) {
      result = result && downLeft.topRight == p;
    }
    if (downRight != null) {
      result = result && downRight.topLeft == p;
    }
    if (down != null) {
      result = result && down.top == p;
    }

    return result;

  }

  // creates the world scene for this world object
  public WorldScene makeScene() {
    // WorldCanvas c = new WorldCanvas(300, 300);
    WorldScene s = new WorldScene(this.width, this.height);
    s.placeImageXY(newImg, this.width / 2, this.height / 2);
    return s;
  }

  public void onTick() {
    if (this.continueRemoving) {
      if (this.removeType.equals("v")) {
        if (highlight) {
          this.highlight = false;
          this.highlightNextSeam();
        }
        else {
          this.highlight = true;
          this.removeNextSeamVert();
          this.updateCurLowest();
        }
      }
      else if (this.removeType.equals("h")) {
        if (highlight) {
          this.highlight = false;
          this.highlightNextSeam();
        }
        else {
          this.highlight = true;
          this.removeNextSeamHoriz();
          this.updateCurLowest();
        }
      }
    }
  }

  public void onKeyEvent(String key) {
    if (key.equals(" ")) {
      this.continueRemoving = !this.continueRemoving;
    }
    else if (key.equals("v") || key.equals("h")) {
      if (!this.removeType.equals(key)) {
        this.removeType = key;
        this.updateCurLowest();
      }
    }
    else if (key.equals("e") || key.equals("r") || key.equals("w")) {
      this.showType = key;
    }
    else if (key.equals("a")) {
      this.continueRemoving = false;
      this.addBackLastSeam();
    }
  }

  void updateCurLowest() {
    if (this.removeType.equals("v")) {
      this.curLowest = this.lowestSeamVert();
    }
    else if (this.removeType.equals("h")) {
      this.curLowest = this.lowestSeamHoriz();
    }
  }

  // draws the new image after seam removal
  // EFFECT: updates new img to be the seam removed image
  void draw() {

    // SeamInfo lowestSeam = this.lowestSeamVert();
    // lowestSeam.changeColor();

    ComputedPixelImage seamRemovedImg = new ComputedPixelImage(this.newImg.width,
        this.newImg.height);
    int countRow = 0;
    int countCol = 0;

    Pixel current = this.curPixel;
    Pixel temp;

    while (current.down != null) {
      temp = current.down;
      while (current.right != null) {
        Color c = Color.MAGENTA;
        if (current.highlighted) {
          c = Color.RED;
        }
        else {
          c = current.color;
        }
        if (this.showType.equals("e")) {
          int energy = (int) (current.energy * 100);
          if (energy > 255) {
            System.out.println("energy: " + energy + " to  255");
            energy = 255;
          }
          c = new Color(energy, energy, energy);
        }
        else if (this.showType.equals("w")) {
          int weight = (int) (current.seam.totalWeight);
          if (weight > 255) {
            System.out.println("weight: " + weight + " to  255");
            weight = 255;
          }
          c = new Color(weight, weight, weight);
        }

        seamRemovedImg.setColorAt(countCol, countRow, c);
        countCol += 1;
        current = current.right;
      }
      countCol = 0;
      countRow += 1;
      current = temp;
    }
    countCol = 0;

    this.newImg = seamRemovedImg;

  }

  // updates the energies of all the pixels relating to p
  void updateEnergy() {
    Pixel current = this.curPixel;
    Pixel temp;

    current = this.curPixel;

    while (current.down != null) {
      temp = current.down;
      while (current.right != null) {
        current.updateEnergy();
        current = current.right;
      }
      current = temp;
    }

  }

  SeamInfo lowestSeamVert() {

    Pixel current = this.curPixel;
    SeamInfo result;
    Pixel temp;
    int count = 0;

    while (current.down != null) {
      current = current.down;
      count += 1;
    }
    result = new SeamInfo(current, 5000000, null);

    current = this.curPixel;

    int count2 = 0;

    while (current != null) {
      count2 += 1;
      temp = current.down;
      while (current != null) {
        current.updateEnergy();
        current.updateSeamVert();
        current = current.right;
      }
      current = temp;
    }

    current = this.curPixel;
    int counter = 0;
    while (current.down != null) {
      current = current.down;
      counter += 1;
    }

    while (current != null) {
      current.updateSeamVert();
      if (current.seam.totalWeight < result.totalWeight && !this.removedSeams.contains(current.seam)
          && current.seam.length() == counter) {
        result = current.seam;
      }
      current = current.right;
    }

    return result;

  }

  SeamInfo lowestSeamHoriz() {

    Pixel current = this.curPixel;
    SeamInfo result;
    Pixel temp;
    int count = 0;

    while (current.right != null) {
      current = current.right;
      count += 1;
    }
    result = new SeamInfo(current, 5000000, null);

    current = this.curPixel;

    int count2 = 0;

    while (current != null) {
      count2 += 1;
      temp = current.right;
      while (current != null) {
        current.updateEnergy();
        current.updateSeamHoriz();
        current = current.down;
      }
      current = temp;
    }

    current = this.curPixel;
    int counter = 0;
    while (current.right != null) {
      current = current.right;
      counter += 1;
    }

    while (current != null) {
      current.updateSeamHoriz();
      if (current.seam.totalWeight < result.totalWeight
          && !this.removedSeams.contains(current.seam) 
          && current.seam.length() == counter) {
        result = current.seam;
      }
      current = current.down;
    }

    return result;

  }

  // removes the lowest seam in the current image and adds the removed seam to
  // removedSeams
  void removeNextSeamVert() {
    SeamInfo toRemove = this.curLowest;

    toRemove.removeSeamVert();

    if (toRemove.containsPixel(this.curPixel)) {

      this.curPixel = this.curPixel.right;
    }

    this.removedSeams.add(toRemove);

    this.draw();
  }

  // removes the lowest horizontal seam in the current image and adds the removed
  // seam to
  // removedSeams
  void removeNextSeamHoriz() {

    SeamInfo toRemove = this.curLowest;

    toRemove.removeSeamHoriz();

    if (toRemove.containsPixel(this.curPixel)) {
      this.curPixel = this.curPixel.down;
    }

    this.removedSeams.add(toRemove);

    this.draw();

  }

  void highlightNextSeam() {
    SeamInfo toRemove = this.curLowest;
    toRemove.changeHighlight(true);
    this.draw();
  }

  // returns the last seam in removedSeams and removes it from this list
  void returnLastSeamVert() {
    SeamInfo toReturn = this.removedSeams.remove(this.removedSeams.size() - 1);

    toReturn.returnSeamVert();
  }

  void addBackLastSeam() {
    if (this.removedSeams.size() > 0) {
      SeamInfo lastRemoved = this.removedSeams.get(this.removedSeams.size() - 1);

      lastRemoved.addBackSeam();

      lastRemoved.changeHighlight(false);

      if (this.curPixel.left != null) {
        this.curPixel = this.curPixel.left;
      }

      this.removedSeams.remove(this.removedSeams.size() - 1);
      this.draw();
    }
  }

}

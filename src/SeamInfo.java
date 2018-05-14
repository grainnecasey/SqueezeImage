import java.awt.Color;

public class SeamInfo {

  Pixel curPixel;
  double totalWeight;
  SeamInfo cameFrom;

  SeamInfo(Pixel curPixel, double totalWeight, SeamInfo cameFrom) {
    this.curPixel = curPixel;
    this.totalWeight = totalWeight;
    this.cameFrom = cameFrom;
  }

  // removes the lowest seam from the image
  // starts from the bottom of the image b/c that's most recent pixel in the seam
  // essentially moves curPixel.right into curPixel's spot
  // sets seam.pixel.left.right to seam.pixel.right
  // sets seam.pixel.right.left to seam.pixel.left
  // same with topLeft, topRight, downLeft, downRight
  // then as you move up the seam it will correct if any of these end up being
  // wrong
  // repeats the process with seam.cameFrom

  // called for first row of pixels only - no down's necessary
  // then delegates to the helper functions depending on what pixel is next
  void removeSeamVert() {

    int next = 0;
    if (this.cameFrom != null) {
      if (this.cameFrom.curPixel == this.curPixel.topLeft) {
        next = 1;
      }
      else if (this.cameFrom.curPixel == this.curPixel.top) {
        next = 2;
      }
      else {
        next = 3;
      }
    }

    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel.right;
      if (this.curPixel.right == null) System.out.println("this.cur.right == null");
    } else {
      System.out.println("this.cur.left == null");
    }

    if (this.curPixel.right != null) {
      this.curPixel.right.left = this.curPixel.left;
      this.curPixel.right.topLeft = this.curPixel.topLeft;
      this.curPixel.right.top = this.curPixel.top;
      this.curPixel.right.topRight = this.curPixel.topRight;
    }

    if (this.curPixel.topLeft != null) {
      this.curPixel.topLeft.downRight = this.curPixel.right;
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel.right;
    }

    if (this.curPixel.topRight != null) {
      this.curPixel.topRight.downLeft = this.curPixel.right;
    }

    if (this.cameFrom != null) {
      if (next == 1) {
        this.cameFrom.removeSeamVertTopLeft();
      }
      else if (next == 2) {
        this.cameFrom.removeSeamVertTop();
      }
      else {
        this.cameFrom.removeSeamVertTopRight();
      }
    }

  }

  // called if the next pixel to remove was the previous pixel's top
  void removeSeamVertTop() {
    int next = 0;
    if (this.cameFrom != null) {
      if (this.cameFrom.curPixel == this.curPixel.topLeft) {
        next = 1;
      }
      else if (this.cameFrom.curPixel == this.curPixel.top) {
        next = 2;
      }
      else {
        next = 3;
      }
    }

    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel.right; //
    }

    if (this.curPixel.right != null) {
      this.curPixel.right.left = this.curPixel.left; //
      this.curPixel.right.down = this.curPixel.down; //
      this.curPixel.right.downLeft = this.curPixel.downLeft; //
      this.curPixel.right.topLeft = this.curPixel.topLeft; //
      this.curPixel.right.top = this.curPixel.top; //
      this.curPixel.right.topRight = this.curPixel.topRight;
    }

    if (this.curPixel.down != null) {
      this.curPixel.down.top = this.curPixel.right; //
      if (this.curPixel.right != null) {
        this.curPixel.down.topRight = this.curPixel.right.right;
      }
    }

    if (this.curPixel.downLeft != null) {
      this.curPixel.downLeft.topRight = this.curPixel.right; //
    }

    if (this.curPixel.topRight != null) {
      this.curPixel.topRight.downLeft = this.curPixel.right;
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel.right; //
    }

    if (this.curPixel.topLeft != null) {
      this.curPixel.topLeft.downRight = this.curPixel.right; //
    }

    if (this.cameFrom != null) {
      if (next == 1) {
        this.cameFrom.removeSeamVertTopLeft();
      }
      else if (next == 2) {
        this.cameFrom.removeSeamVertTop();
      }
      else {
        this.cameFrom.removeSeamVertTopRight();
      }
    }

  }

  // called if the next pixel to remove was the previous pixel's topLeft
  void removeSeamVertTopLeft() {
    int next = 0;
    if (this.cameFrom != null) {
      if (this.cameFrom.curPixel == this.curPixel.topLeft) {
        next = 1;
      }
      else if (this.cameFrom.curPixel == this.curPixel.top) {
        next = 2;
      }
      else {
        next = 3;
      }
    }

    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel.right; //
    }

    if (this.curPixel.right != null) {
      this.curPixel.right.left = this.curPixel.left; //
      this.curPixel.right.topLeft = this.curPixel.topLeft; //
      this.curPixel.right.top = this.curPixel.top; //
      this.curPixel.right.topRight = this.curPixel.topRight; //
      this.curPixel.right.downLeft = this.curPixel.downLeft; //
      this.curPixel.right.down = this.curPixel.down; //
      this.curPixel.right.downRight = this.curPixel.downRight;
      if (this.curPixel.right.right != null) {
        this.curPixel.right.right.down = this.curPixel.downRight; //
        this.curPixel.right.right.downLeft = this.curPixel.down; //
        if (this.curPixel.right.right.right != null) {
          this.curPixel.right.right.right.downLeft = this.curPixel.downRight; //
        }
      }
    }

    if (this.curPixel.topLeft != null) {
      this.curPixel.topLeft.downRight = this.curPixel.right; //
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel.right; //
    }

    if (this.curPixel.topRight != null) {
      this.curPixel.topRight.downLeft = this.curPixel.right; //
    }

    if (this.curPixel.downLeft != null) {
      this.curPixel.downLeft.topRight = this.curPixel.right; //
    }

    if (this.curPixel.down != null) {
      this.curPixel.down.top = this.curPixel.right; //
      if (this.curPixel.right != null)
      this.curPixel.down.topRight = this.curPixel.right.right; //
    }

    if (this.curPixel.downRight != null) {

      this.curPixel.downRight.topLeft = this.curPixel.right; //
      if (this.curPixel.right != null) {
        this.curPixel.downRight.top = this.curPixel.right.right; //
        if (this.curPixel.right.right != null) {
          this.curPixel.downRight.topRight = this.curPixel.right.right.right; //
        }
      }

    }

    if (this.cameFrom != null) {
      if (next == 1) {
        this.cameFrom.removeSeamVertTopLeft();
      }
      else if (next == 2) {
        this.cameFrom.removeSeamVertTop();
      }
      else {
        this.cameFrom.removeSeamVertTopRight();
      }
    }

  }

  // called if the next pixel to remove was the previous pixel's topRight
  void removeSeamVertTopRight() {
    int next = 0;
    if (this.cameFrom != null) {
      if (this.cameFrom.curPixel == this.curPixel.topLeft) {
        next = 1;
      }
      else if (this.cameFrom.curPixel == this.curPixel.top) {
        next = 2;
      }
      else {
        next = 3;
      }
    }

    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel.right; //
      this.curPixel.left.downRight = this.curPixel.downRight; //
    }

    if (this.curPixel.right != null) {
      this.curPixel.right.left = this.curPixel.left; //
      this.curPixel.right.down = this.curPixel.downRight; //
      this.curPixel.right.top = this.curPixel.top; //
      this.curPixel.right.downLeft = this.curPixel.downLeft; //
      this.curPixel.right.downLeft = this.curPixel.downLeft; //
      this.curPixel.right.topLeft = this.curPixel.topLeft;
      this.curPixel.right.topRight = this.curPixel.topRight;
    }

    if (this.curPixel.downRight != null) {
      this.curPixel.downRight.topLeft = this.curPixel.left; //
      this.curPixel.downRight.top = this.curPixel.right; //
    }

    if (this.curPixel.downLeft != null) {
      this.curPixel.downLeft.topRight = this.curPixel.right; //
      this.curPixel.downLeft.topRight = this.curPixel.right; //
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel.right; //
    }

    if (this.curPixel.topRight != null) {
      this.curPixel.topRight.downLeft = this.curPixel.right;
    }

    if (this.curPixel.topLeft != null) {
      this.curPixel.topLeft.downRight = this.curPixel.right;
    }

    if (this.cameFrom != null) {
      if (next == 1) {
        this.cameFrom.removeSeamVertTopLeft();
      }
      else if (next == 2) {
        this.cameFrom.removeSeamVertTop();
      }
      else {
        this.cameFrom.removeSeamVertTopRight();
      }
    }

  }

  // removes a horizontal seam
  // only called for first column, so right's don't apply
  // then delegates to other method depending on what the previous pixel in seam
  // is
  void removeSeamHoriz() {

    int next = 0;

    if (this.cameFrom != null) {
      if (this.cameFrom.curPixel == this.curPixel.topLeft) {
        next = 1;
      }
      else if (this.cameFrom.curPixel == this.curPixel.left) {
        next = 2;
      }
      else {
        next = 3;
      }
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel.down;
    }

    if (this.curPixel.down != null) {
      this.curPixel.down.top = this.curPixel.top;
      this.curPixel.down.topLeft = this.curPixel.topLeft;
      this.curPixel.down.left = this.curPixel.left;
      this.curPixel.down.downLeft = this.curPixel.downLeft;
    }

    if (this.curPixel.topLeft != null) {
      this.curPixel.topLeft.downRight = this.curPixel.down;
    }

    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel.down;
    }

    if (this.curPixel.downLeft != null) {
      this.curPixel.downLeft.topRight = this.curPixel.down;
      if (this.curPixel.down != null) {
        this.curPixel.downLeft.right = this.curPixel.down.down;
      }

    }

    if (this.cameFrom != null) {
      if (next == 1) {
        this.cameFrom.removeSeamHorizTopLeft();
      }
      else if (next == 1) {
        this.cameFrom.removeSeamHorizLeft();
      }
      else {
        this.cameFrom.removeSeamHorizDownLeft();
      }
    }
  }

  void removeSeamHorizTopLeft() {

    int next = 0;

    if (this.cameFrom != null) {
      if (this.cameFrom.curPixel == this.curPixel.topLeft) {
        next = 1;
      }
      else if (this.cameFrom.curPixel == this.curPixel.left) {
        next = 2;
      }
      else {
        next = 3;
      }
    }

    if (this.curPixel.down != null) {
      this.curPixel.down.top = this.curPixel.top;
      this.curPixel.down.topLeft = this.curPixel.topLeft;
      this.curPixel.down.left = this.curPixel.left;
      this.curPixel.down.downLeft = this.curPixel.downLeft;
      this.curPixel.down.topRight = this.curPixel.topRight;
      this.curPixel.down.right = this.curPixel.right;
      this.curPixel.down.downRight = this.curPixel.downRight;

      if (this.curPixel.down.down != null) {
        this.curPixel.down.down.right = this.curPixel.downRight;
        this.curPixel.down.down.topRight = this.curPixel.right;
        if (this.curPixel.down.down.down != null) {
          this.curPixel.down.down.down.topRight = this.curPixel.downRight;
        }
      }
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel.down;
    }

    if (this.curPixel.topLeft != null) {
      this.curPixel.topLeft.downRight = this.curPixel.down;
    }

    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel.down;
    }

    if (this.curPixel.downLeft != null) {
      this.curPixel.downLeft.topRight = this.curPixel.down;
      if (this.curPixel.down != null) {
        this.curPixel.downLeft.right = this.curPixel.down.down;
      }
    }

    if (this.curPixel.topRight != null) {
      this.curPixel.topRight.downLeft = this.curPixel.down;
    }

    if (this.curPixel.downRight != null) {
      this.curPixel.downRight.topLeft = this.curPixel.down;
      if (this.curPixel.down != null) {
        this.curPixel.downRight.left = this.curPixel.down.down;
        if (this.curPixel.down.down != null) {
          this.curPixel.downRight.downLeft = this.curPixel.down.down.down;
        }
      }
    }

    if (this.curPixel.right != null) {
      this.curPixel.right.left = this.curPixel.down;
      if (this.curPixel.down != null) {
        this.curPixel.right.downLeft = this.curPixel.down.down;
      }
    }

    if (this.cameFrom != null) {
      if (next == 1) {
        this.cameFrom.removeSeamHorizTopLeft();
      }
      else if (next == 1) {
        this.cameFrom.removeSeamHorizLeft();
      }
      else {
        this.cameFrom.removeSeamHorizDownLeft();
      }
    }
  }

  void removeSeamHorizLeft() {

    int next = 0;

    if (this.cameFrom != null) {
      if (this.cameFrom.curPixel == this.curPixel.topLeft) {
        next = 1;
      }
      else if (this.cameFrom.curPixel == this.curPixel.left) {
        next = 2;
      }
      else {
        next = 3;
      }
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel.down;
    }

    if (this.curPixel.down != null) {
      this.curPixel.down.top = this.curPixel.top;
      this.curPixel.down.right = this.curPixel.right;
      this.curPixel.down.topRight = this.curPixel.topRight;
      this.curPixel.down.topLeft = this.curPixel.topLeft;
      this.curPixel.down.left = this.curPixel.left;
      this.curPixel.down.downLeft = this.curPixel.downLeft;
    }

    if (this.curPixel.right != null) {
      this.curPixel.right.left = this.curPixel.down;
      if (this.curPixel.down.down != null) {
        this.curPixel.right.downLeft = this.curPixel.down.down;
      }
    }

    if (this.curPixel.topRight != null) {
      this.curPixel.topRight.downLeft = this.curPixel.down;
    }

    if (this.curPixel.downLeft != null) {
      this.curPixel.downLeft.topRight = this.curPixel.down;
      if (this.curPixel.down != null) {
        this.curPixel.downLeft.right = this.curPixel.down.down;
      }
    }

    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel.down;
    }

    if (this.curPixel.topLeft != null) {
      this.curPixel.downRight.topLeft = this.curPixel.down;
    }

    if (this.cameFrom != null) {
      if (next == 1) {
        this.cameFrom.removeSeamHorizTopLeft();
      }
      else if (next == 1) {
        this.cameFrom.removeSeamHorizLeft();
      }
      else {
        this.cameFrom.removeSeamHorizDownLeft();
      }
    }

  }

  void removeSeamHorizDownLeft() {

    int next = 0;

    if (this.cameFrom != null) {
      if (this.cameFrom.curPixel == this.curPixel.topLeft) {
        next = 1;
      }
      else if (this.cameFrom.curPixel == this.curPixel.left) {
        next = 2;
      }
      else {
        next = 3;
      }
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel.down;
      this.curPixel.top.downRight = this.curPixel.downRight;
    }

    if (this.curPixel.down != null) {
      this.curPixel.down.top = this.curPixel.top;
      this.curPixel.down.right = this.curPixel.downRight;
      this.curPixel.down.left = this.curPixel.left;
      this.curPixel.down.topRight = this.curPixel.topRight;
      this.curPixel.down.topLeft = this.curPixel.topLeft;
      this.curPixel.down.downLeft = this.curPixel.downLeft;
    }

    if (this.curPixel.downRight != null) {
      this.curPixel.downRight.topLeft = this.curPixel.top;
      this.curPixel.downRight.left = this.curPixel.down;
    }

    if (this.curPixel.topRight != null) {
      this.curPixel.topRight.downLeft = this.curPixel.down;
    }

    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel.down;
    }

    if (this.curPixel.downLeft != null) {
      this.curPixel.downLeft.topRight = this.curPixel.down;
      if (this.curPixel.down != null) {
        this.curPixel.downLeft.right = this.curPixel.down.down;
      }
    }

    if (this.curPixel.topLeft != null) {
      this.curPixel.topLeft.downRight = this.curPixel.down;
    }

    if (this.cameFrom != null) {
      if (next == 1) {
        this.cameFrom.removeSeamHorizTopLeft();
      }
      else if (next == 1) {
        this.cameFrom.removeSeamHorizLeft();
      }
      else {
        this.cameFrom.removeSeamHorizDownLeft();
      }
    }

  }

  // assumes that seam has already been removed
  // just restores this.curPixel's connections
  void returnSeamVert() {

    this.curPixel.topLeft.downRight = this.curPixel;
    this.curPixel.top.down = this.curPixel;
    this.curPixel.topRight.downLeft = this.curPixel;

    this.curPixel.right.left = this.curPixel;
    this.curPixel.left.right = this.curPixel;

    this.curPixel.downLeft.topRight = this.curPixel;
    this.curPixel.down.top = this.curPixel;
    this.curPixel.downRight.topLeft = this.curPixel;

    this.cameFrom.returnSeamVert();
  }

  void changeHighlight(boolean highlighted) {
    this.curPixel.highlighted = highlighted;
    if (this.cameFrom != null)
      this.cameFrom.changeHighlight(highlighted);
  }

  int length() {
    if (this.cameFrom == null) {
      return 0;
    }
    else {
      return 1 + this.cameFrom.length();
    }
  }

  boolean containsPixel(Pixel p) {
    if (this.curPixel == p) {
      return true;
    }
    else if (this.cameFrom != null) {
      return this.cameFrom.containsPixel(p);
    }
    else {
      return false;
    }
  }

  void addBackSeam() {
    if (this.curPixel.left != null) {
      this.curPixel.left.right = this.curPixel;
    }

    if (this.curPixel.right != null) {
      this.curPixel.right.left = this.curPixel;
    }

    if (this.curPixel.top != null) {
      this.curPixel.top.down = this.curPixel;
    }

    if (this.cameFrom != null) {
      this.cameFrom.addBackSeam();
    }

  }

}

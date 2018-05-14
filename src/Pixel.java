import java.awt.Color;

public class Pixel {
  Pixel topLeft;
  Pixel top;
  Pixel topRight;
  Pixel left;
  Pixel right;
  Pixel downLeft;
  Pixel down;
  Pixel downRight;

  Color color;
  double brightness;
  double horizontalEnergy;
  double verticalEnergy;
  double energy;
  boolean highlighted;

  SeamInfo seam;

  Pixel(Color color) {
    topLeft = null;
    top = null;
    topRight = null;
    left = null;
    right = null;
    downLeft = null;
    down = null;
    downRight = null;
    this.color = color;
    double red = this.color.getRed();
    double blue = this.color.getBlue();
    double green = this.color.getGreen();
    double sum = red + green + blue;
    this.brightness = sum / 255;
    this.updateEnergy();
    seam = new SeamInfo(this, energy, null);
    this.highlighted = false;
  }

  void updateEnergy() {
    double topLeftB = 0;
    double topB = 0;
    double topRightB = 0;
    double leftB = 0;
    double rightB = 0;
    double downLeftB = 0;
    double downB = 0;
    double downRightB = 0;

    if (this.topLeft != null) {
      topLeftB = topLeft.brightness;
    }
    if (this.top != null) {
      topB = top.brightness;
    }
    if (this.topRight != null) {
      topRightB = topRight.brightness;
    }
    if (this.left != null) {
      leftB = left.brightness;
    }
    if (this.right != null) {
      rightB = right.brightness;
    }
    if (this.downLeft != null) {
      downLeftB = downLeft.brightness;
    }
    if (this.down != null) {
      downB = down.brightness;
    }
    if (this.downRight != null) {
      downRightB = downRight.brightness;
    }

    double red = this.color.getRed();
    double blue = this.color.getBlue();
    double green = this.color.getGreen();

    double sum = red + green + blue;

    double avg = sum / 3;

    this.brightness = avg / 255;

    double horizontalEnergy = (topLeftB + (2 * leftB) + downLeftB)
        - (topRightB + (2 * rightB) + downRightB);

    double verticalEnergy = (topLeftB + (2 * topB) + topRightB)
        - (downLeftB + (2 * downB) + downRightB);

    this.energy = Math
        .sqrt((horizontalEnergy * horizontalEnergy) + (verticalEnergy * verticalEnergy));
  }

  void updateSeamVert() {
    double topLeftWeight = 0;
    double topRightWeight = 0;
    double topWeight = 0;
    if (this.topLeft != null) {
      topLeftWeight = this.topLeft.seam.totalWeight;
    }
    if (this.top != null) {
      topWeight = this.top.seam.totalWeight;
    }
    if (this.topRight != null) {
      topRightWeight = this.topRight.seam.totalWeight;
    }

    if (this.topLeft == null && this.top == null && this.topRight == null) {
      this.seam = new SeamInfo(this, this.energy, null);
    }
    else if (this.topLeft == null) {
      if (topWeight <= topRightWeight) {
        this.seam = new SeamInfo(this, this.energy + topWeight, this.top.seam);
      }
      else {
        this.seam = new SeamInfo(this, this.energy + topRightWeight, this.topRight.seam);
      }
    }
    else if (this.topRight == null) {
      if (topWeight <= topLeftWeight) {
        this.seam = new SeamInfo(this, this.energy + topWeight, this.top.seam);
      }
      else {
        this.seam = new SeamInfo(this, this.energy + topLeftWeight, this.topLeft.seam);
      }
    }
    else {
      if (topWeight <= topWeight && topWeight <= topRightWeight) {
        this.seam = new SeamInfo(this, this.energy + topWeight, this.top.seam);
      }
      else if (topLeftWeight <= topWeight && topLeftWeight <= topRightWeight) {
        this.seam = new SeamInfo(this, this.energy + topLeftWeight, this.topLeft.seam);
      }
      else {
        this.seam = new SeamInfo(this, this.energy + topRightWeight, this.topRight.seam);
      }
    }

  }

  void updateSeamHoriz() {
    double topLeftWeight = 0;
    double leftWeight = 0;
    double downLeftWeight = 0;

    if (this.topLeft != null) {
      topLeftWeight = this.topLeft.seam.totalWeight;
    }
    if (this.left != null) {
      leftWeight = this.left.seam.totalWeight;
    }
    if (this.downLeft != null) {
      downLeftWeight = this.downLeft.seam.totalWeight;
    }

    if (this.topLeft == null && this.left == null) {
      this.seam = new SeamInfo(this, this.energy, null);
    }
    else if (this.topLeft == null) {
      this.seam = new SeamInfo(this, this.energy + leftWeight, this.left.seam);
    }
    else if (this.left == null) {
      this.seam = new SeamInfo(this, this.energy + topLeftWeight, this.topLeft.seam);
    }
    else {
      if (leftWeight <= topLeftWeight) {
        this.seam = new SeamInfo(this, this.energy + leftWeight, this.left.seam);
      }
      else {
        this.seam = new SeamInfo(this, this.energy + topLeftWeight, this.topLeft.seam);
      }
    }
  }
}

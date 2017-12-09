package game;

/**
 *
 * @author Rostiku
 */
public class Controls {
    // all needed controls (pressed + released)
    public boolean turnLeft, turnRight, moveForward, leftRelease, rightRelease, forwardRelease, moveBackward, backwardRelease;
    
    /**
     * Initializes all controls
     */
    public Controls(){
        this.turnLeft = false;
        this.turnRight = false;
        this.moveForward = false;
        this.leftRelease = true;
        this.rightRelease = true;
        this.forwardRelease = true;
        this.moveBackward = false;
        this.backwardRelease = true;
    }
    
    // GETTERS AND SETTERS FOR EVERY CONTROL
    
    public boolean isTurnLeft() {
        return turnLeft;
    }

    public void setTurnLeft(boolean turnLeft) {
        this.turnLeft = turnLeft;
    }

    public boolean isTurnRight() {
        return turnRight;
    }

    public void setTurnRight(boolean turnRight) {
        this.turnRight = turnRight;
    }

    public boolean isMoveForward() {
        return moveForward;
    }

    public void setMoveForward(boolean moveForward) {
        this.moveForward = moveForward;
    }
    
    public boolean isLeftRelease() {
        return leftRelease;
    }

    public void setLeftRelease(boolean leftRelease) {
        this.leftRelease = leftRelease;
    }

    public boolean isRightRelease() {
        return rightRelease;
    }

    public void setRightRelease(boolean rightRelease) {
        this.rightRelease = rightRelease;
    }

    public boolean isForwardRelease() {
        return forwardRelease;
    }

    public void setForwardRelease(boolean forwardRelease) {
        this.forwardRelease = forwardRelease;
    }

    public boolean isMoveBackward() {
        return moveBackward;
    }

    public void setMoveBackward(boolean moveBackward) {
        this.moveBackward = moveBackward;
    }

    public boolean isBackwardRelease() {
        return backwardRelease;
    }

    public void setBackwardRelease(boolean backwardRelease) {
        this.backwardRelease = backwardRelease;
    }
}

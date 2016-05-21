import javafx.scene.canvas.GraphicsContext;


/**
 * Created by Paul on 5/25/2015.
 */
public class Actor {



    private int life = 100;

    private int x;
    private int y;

    private int width;
    private int height;

    private int spriteWidth;
    private int spriteHeight;

    private int attackY;
    private int attackX;

    private double xVelocity = 0;
    private double yVelocity = 0;

    private double maxXVelocity = 5;

    private double xAcceleration = .5;

    private double jumpAcceleration = 8;

    private double jumpHoldBoost = .4;

    private int delayBeforeSecondJump = 12;

    private int delayBeforeSecondJumpCounter = 0;

    private boolean hasAirborneJump = true;

    private boolean grounded = false;

    private boolean facingLeft = true;

    private boolean attackLock = false;

    private int attackDelay = 0;

    private double gravity = .8;

    private double frictionReduction = .8;

    private int rateOfFire = 40;

    private int rateOfFireCounter = 0;



    private Sprite sprite;

    private TargetBox targetBox;




    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean jumping = false;
    private boolean jumpJustPressed = false;
    private boolean attacking = false;



    public Actor(int x, int y, int width, int height, String spritePathName) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        spriteWidth = width * 3;
        spriteHeight = height;

        attackX = width / 2;
        attackY = height / 2;

        sprite = Constants.getSprite(spritePathName);
        targetBox = new TargetBox(this);
        targetBox.addArea(0, 0, width, height);
    }



    public void step(GraphicsShell g, Map map) {


        if (map.isLegalActorPosition(x, y + 1, width, height)) {
            grounded = false;
        } else {
            grounded = true;
        }


        if (movingRight && (!attackLock || !grounded)) {
            xVelocity += xAcceleration;
        } else if (movingLeft && (!attackLock || !grounded)) {
            xVelocity -= xAcceleration;
        } else {
            xVelocity *= frictionReduction;

            if (xVelocity < 1 && xVelocity > -1) {
                xVelocity = 0;
            }
        }

        if (yVelocity == 0) {
            hasAirborneJump = true;
        }

        if (jumpJustPressed && !attackLock) {
            if (yVelocity != 0) {

                if (hasAirborneJump && delayBeforeSecondJumpCounter == 0) {

                    yVelocity = -jumpAcceleration;
                    hasAirborneJump = false;
                    delayBeforeSecondJumpCounter = delayBeforeSecondJump;
                    sprite.animateOnce("#jump");

                    if (movingLeft) {
                        xVelocity -= xAcceleration * 4;   //Boost sideways motion on double jump
                    }
                    if (movingRight) {
                        xVelocity += xAcceleration * 4;   //Boost sideways motion on double jump
                    }
                }
            } else {
                yVelocity -= jumpAcceleration;
                delayBeforeSecondJumpCounter = delayBeforeSecondJump;
                sprite.animateOnce("#jump");
            }
            jumpJustPressed = false;
        }

        if (jumping) {
            if (yVelocity != 0) {
                yVelocity -= jumpHoldBoost;
            }
        }

        if (delayBeforeSecondJumpCounter > 0) {
            delayBeforeSecondJumpCounter--;
        }




        if (!grounded) {
            yVelocity += gravity;
            sprite.changeAnimation("#air");
        } else {
            if (xVelocity == 0) {
                sprite.changeAnimation("#idle");
            } else {
                sprite.changeAnimation("#run");
            }
        }



        if (xVelocity > maxXVelocity) {
            xVelocity = maxXVelocity;
        }
        if (xVelocity < -maxXVelocity) {
            xVelocity = -maxXVelocity;
        }

        if (xVelocity > 0) {
            setFacingLeft(false);
        }
        if (xVelocity < 0) {
            setFacingLeft(true);
        }


        if (Math.abs(xVelocity) >= 1 || Math.abs(yVelocity) >= 1) {
            move(map);
        }

        if (rateOfFireCounter > 0) {
            rateOfFireCounter --;
        }


        if (attacking && rateOfFireCounter == 0 && !attackLock) {
            attack(map);
            rateOfFireCounter = rateOfFire;
            sprite.animateOnce("#attack");
        }

        if (attackLock) {
            attack(map);
        }




        draw(g);
    }


    public void draw(GraphicsShell g) {

        int drawX = x + width / 2 - spriteWidth / 2;
        int drawY = y + height / 2 - spriteHeight / 2;



        sprite.step(g, drawX, drawY, spriteWidth, spriteHeight, facingLeft);
    }


    public void move(Map map) {


        int xDistanceToMove = (int)Math.abs(xVelocity) * (int)(xVelocity / Math.abs(xVelocity));
        int yDistanceToMove = (int)Math.abs(yVelocity) * (int)(yVelocity / Math.abs(yVelocity));




        if (map.isLegalActorPosition(x + xDistanceToMove, y + yDistanceToMove, width, height)) {
            x += xDistanceToMove;
            y += yDistanceToMove;
            return;
        }



        if (xDistanceToMove > yDistanceToMove) {
            if (map.isLegalActorPosition(x + xDistanceToMove, y, width, height)) {
                x += xDistanceToMove;
                xDistanceToMove = 0;
            } else {
                if (map.isLegalActorPosition(x, y + yDistanceToMove, width, height)) {
                    y += yDistanceToMove;
                    yDistanceToMove = 0;
                }
            }
        } else {
            if (map.isLegalActorPosition(x, y + yDistanceToMove, width, height)) {
                y += yDistanceToMove;
                yDistanceToMove = 0;
            } else {
                if (map.isLegalActorPosition(x + xDistanceToMove, y, width, height)) {
                    x += xDistanceToMove;
                    xDistanceToMove = 0;
                }
            }
        }



        if (xDistanceToMove > yDistanceToMove) {



            for (int i = 0; i != xDistanceToMove; i += xDistanceToMove / Math.abs(xDistanceToMove)) {
                if (map.isLegalActorPosition(x + (xDistanceToMove / Math.abs(xDistanceToMove)), y, width, height)) {
                    x += xDistanceToMove / Math.abs(xDistanceToMove);
                } else {
                    xDistanceToMove = 0;
                    xVelocity = 0;
                    break;
                }
            }

            for (int i = 0; i != yDistanceToMove; i += yDistanceToMove / Math.abs(yDistanceToMove)) {
                if (map.isLegalActorPosition(x, y + (yDistanceToMove / Math.abs(yDistanceToMove)), width, height)) {
                    y += yDistanceToMove / Math.abs(yDistanceToMove);
                } else {
                    yDistanceToMove = 0;
                    yVelocity = 0;
                    break;
                }
            }

        } else {

            for (int i = 0; i != yDistanceToMove; i += yDistanceToMove / Math.abs(yDistanceToMove)) {
                if (map.isLegalActorPosition(x, y + (yDistanceToMove / Math.abs(yDistanceToMove)), width, height)) {
                    y += yDistanceToMove / Math.abs(yDistanceToMove);
                } else {
                    yDistanceToMove = 0;
                    yVelocity = 0;
                    break;
                }
            }

            for (int i = 0; i != xDistanceToMove; i += xDistanceToMove / Math.abs(xDistanceToMove)) {
                if (map.isLegalActorPosition(x + (xDistanceToMove / Math.abs(xDistanceToMove)), y, width, height)) {
                    x += xDistanceToMove / Math.abs(xDistanceToMove);
                } else {
                    xDistanceToMove = 0;
                    xVelocity = 0;
                    break;
                }
            }

        }





    }

    public void jump() {

        if (!jumping) {
            jumpJustPressed = true;
        }


        jumping = true;


    }

    public void releaseJump() {
        jumping = false;

    }

    public void hitByWeapon(Weapon weapon) {

        life -= weapon.getDamage();
        System.out.println(life);

    }


    public void attack(Map map) {

        if (!attackLock) {
            attackLock = true;
            attackDelay = 40;
            return;
        }

        if (attackDelay > 20) {
            attackDelay--;
        } else if (attackDelay == 20) {


            Weapon w = new Weapon(x + attackX, y + attackY, 50, 15, 1, "Animations/InkPics");

            w.setDisappearAfter(1000);

            int direction = 1;

            if (facingLeft) {
                direction = -1;
            }


            w.setxVelocity(10 * direction);
            w.setxAcceleration(.05 * direction);

            map.getGoodWeapons().add(w);

            attackDelay--;
        } else if (attackDelay > 0) {
            attackDelay--;
        } else {
            attackLock = false;
        }


    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double getxAcceleration() {
        return xAcceleration;
    }

    public void setxAcceleration(double xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public TargetBox getTargetBox() {
        return targetBox;
    }

    public void setTargetBox(TargetBox targetBox) {
        this.targetBox = targetBox;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public void setSpriteWidth(int spriteWidth) {
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight) {
        this.spriteHeight = spriteHeight;
    }

    public int getAttackY() {
        return attackY;
    }

    public void setAttackY(int attackY) {
        this.attackY = attackY;
    }

    public int getAttackX() {
        return attackX;
    }

    public void setAttackX(int attackX) {
        this.attackX = attackX;
    }

    public double getMaxXVelocity() {
        return maxXVelocity;
    }

    public void setMaxXVelocity(double maxXVelocity) {
        this.maxXVelocity = maxXVelocity;
    }

    public double getJumpAcceleration() {
        return jumpAcceleration;
    }

    public void setJumpAcceleration(double jumpAcceleration) {
        this.jumpAcceleration = jumpAcceleration;
    }

    public double getJumpHoldBoost() {
        return jumpHoldBoost;
    }

    public void setJumpHoldBoost(double jumpHoldBoost) {
        this.jumpHoldBoost = jumpHoldBoost;
    }

    public int getDelayBeforeSecondJump() {
        return delayBeforeSecondJump;
    }

    public void setDelayBeforeSecondJump(int delayBeforeSecondJump) {
        this.delayBeforeSecondJump = delayBeforeSecondJump;
    }

    public int getDelayBeforeSecondJumpCounter() {
        return delayBeforeSecondJumpCounter;
    }

    public void setDelayBeforeSecondJumpCounter(int delayBeforeSecondJumpCounter) {
        this.delayBeforeSecondJumpCounter = delayBeforeSecondJumpCounter;
    }

    public boolean isHasAirborneJump() {
        return hasAirborneJump;
    }

    public void setHasAirborneJump(boolean hasAirborneJump) {
        this.hasAirborneJump = hasAirborneJump;
    }
}

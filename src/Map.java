import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Paul on 5/25/2015.
 */
public class Map {


    public ArrayList<Actor> bags = new ArrayList<>();

    public ArrayList<Actor> goodGuys = new ArrayList<>();


    public ArrayList<Weapon> bagWeapons = new ArrayList<>();

    public ArrayList<Weapon> goodWeapons = new ArrayList<>();


    public ArrayList<Block> terrain = new ArrayList<>();


    public Map() {


        Actor spriteDelete = new Actor(0,0,33,75,"Animations/Picturesdsfdsf");
        Block blockDelete = new Block(0, 600, 6000, 30, "TileImages/Delete tile.png");

        Actor spriteDeleteBag = new Actor(500,0,33,75,"Animations/Picturesdsfdsf");

        goodGuys.add(spriteDelete);

        bags.add(spriteDeleteBag);

        terrain.add(blockDelete);

        terrain.add(new Block(200, 550, 50, 50, "TileImages/Delete tile.png"));

        terrain.add(new Block(200, 470, 50, 50, "TileImages/Delete tile.png"));


        bagWeapons.add(new Weapon(350, 400, 25, 25, 50, "Animations/Picturesdsfdsf"));

    }


    public void step(GraphicsShell g) {

        Constants.mainPlayerX = goodGuys.get(0).getX();
        Constants.mainPlayerY = goodGuys.get(0).getY();
        Constants.mainPlayerLife = goodGuys.get(0).getLife();

        for (Block b : terrain) {
            b.step(g);
        }

        for (Actor a : goodGuys) {
            a.step(g, this);
        }

        for (Actor a : bags) {
            a.step(g, this);
        }

        for (Weapon w : goodWeapons) {
            w.step(g);

            for (Actor a : bags) {
                w.checkContact(a.getTargetBox());
            }

            for (Block b : terrain) {
                w.checkContact(b);
            }
        }


        for (Weapon w : bagWeapons) {
            w.step(g);

            for (Actor a : goodGuys) {
                w.checkContact(a.getTargetBox());
            }


            for (Block b : terrain) {
                w.checkContact(b);
            }

        }


        for (Iterator<Weapon> iterator = goodWeapons.iterator(); iterator.hasNext();) {
            Weapon weapon = iterator.next();
            if (!weapon.getActive()) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }


        for (Iterator<Weapon> iterator = bagWeapons.iterator(); iterator.hasNext();) {
            Weapon weapon = iterator.next();
            if (!weapon.getActive()) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }





    }

    public ArrayList<Actor> getBags() {
        return bags;
    }

    public ArrayList<Actor> getGoodGuys() {
        return goodGuys;
    }

    public ArrayList<Weapon> getBagWeapons() {
        return bagWeapons;
    }

    public ArrayList<Weapon> getGoodWeapons() {
        return goodWeapons;
    }

    public ArrayList<Block> getTerrain() {
        return terrain;
    }

    public void setMainPlayerMovingLeft(boolean bool) {
        goodGuys.get(0).setMovingLeft(bool);
    }

    public void setMainPlayerMovingRight(boolean bool) {
        goodGuys.get(0).setMovingRight(bool);
    }

    public void mainPlayerJump() {
        goodGuys.get(0).jump();
    }

    public void mainPlayerReleaseJump() {
        goodGuys.get(0).releaseJump();
    }

    public void setAttacking(boolean b) {
        goodGuys.get(0).setAttacking(b);
    }


    public boolean isLegalActorPosition(int x, int y, int width, int height) {


        for (Block block : terrain) {
            if (Constants.overlap(x, y, width, height, block.getX(), block.getY(), block.getWidth(), block.getHeight())) {
                return false;
            }
        }



        return true;
    }


}

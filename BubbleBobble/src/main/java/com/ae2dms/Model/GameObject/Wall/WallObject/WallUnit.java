package com.ae2dms.Model.GameObject.Wall.WallObject;

import com.ae2dms.Model.GameObject.Sprite.SpriteObject;
import com.ae2dms.Model.GameObject.Wall.CollideStrategy.CollideWithWall;
import com.ae2dms.Model.Scene.GameScene;

/**
 * The WallUnit class creates wall units to be used for the world.
 * A wall unit is an unit shaped like a square that is treated as a wall,
 * with collision on all four sides.
 * The wall collides with any kind of game object.
 */
public class WallUnit extends WallObject {

	/**
	 * Instantiates a new Wall unit.
	 *
	 * @param gameScene  the gameScene
	 * @param colNum the col num
	 * @param rowNum the row num
	 */
	public WallUnit(GameScene gameScene, int colNum, int rowNum) {
		super(gameScene, colNum, rowNum);
		strategy = new CollideWithWall();
	}

	public void collideWith(SpriteObject obj) {
		strategy.collideWith(obj, this);
	}

}

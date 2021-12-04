package com.ae2dms.Scene;

import com.ae2dms.Controller.GameScene.GameSceneController;
import com.ae2dms.GameObject.Award.Award;
import com.ae2dms.GameObject.Sprite.SpriteObject;
import com.ae2dms.GameObject.Sprite.*;
import com.ae2dms.GameObject.Wall.WallObject.CeilingUnit;
import com.ae2dms.GameObject.Wall.WallObject.FloorUnit;
import com.ae2dms.GameObject.Wall.WallObject.WallUnit;
import com.ae2dms.GameObject.Sprite.CollectEffect;
import com.ae2dms.Main;
import com.ae2dms.Util.GameStatus;
import com.ae2dms.Util.Theme;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import static com.ae2dms.GamePanel.*;

/**
 * InteractableWorld handles all of the game's operations:
 * updating positions, checking for collisions, and removing objects.
 */
public class GameScene {
	private ArrayList<CeilingUnit> ceilingUnits;
	private ArrayList<FloorUnit> floorUnits;
	private ArrayList<WallUnit> wallUnits;
	private ArrayList<Hero> heroes;
	private ArrayList<Enemy> enemies;
	private ArrayList<HeroProjectile> heroProjectiles;
	private ArrayList<EnemyProjectile> enemyProjectiles;
	private ArrayList<Award> fruits;
	private ArrayList<SpriteObject> toBeRemoved;
	private ArrayList<Bubble> bubbles;
	private ArrayList<CollectEffect> collectEffects;

//	public Stage stage;
	private GraphicsContext graphicsContext;

	private IntegerProperty bonus = new SimpleIntegerProperty(0);

	public IntegerProperty bonusProperty() {
		return bonus;
	}

	public void incrementBonus(int bonus) {
		this.bonus.set(this.bonus.get()+bonus);
	}

	private Theme theme = Theme.RED;

	private Stage stage;
	public GameScene() {
		//initializes interactableworld
		ceilingUnits = new ArrayList<CeilingUnit>();
		floorUnits = new ArrayList<FloorUnit>();
		wallUnits = new ArrayList<WallUnit>();
		heroes = new ArrayList<Hero>();
		enemies = new ArrayList<Enemy>();
		heroProjectiles = new ArrayList<HeroProjectile>();
		enemyProjectiles = new ArrayList<EnemyProjectile>();
		fruits = new ArrayList<Award>();
		toBeRemoved = new ArrayList<SpriteObject>();
		bubbles = new ArrayList<Bubble>();
		collectEffects = new ArrayList<CollectEffect>();


//		readMap("/world/World3.txt");
	}

	public void paintComponent() {
		//paints everything on the world
//		GraphicsContext g2 = (GraphicsContext) g;
		graphicsContext.clearRect(0, 0, 1280, 720);

		for (CeilingUnit ceilingUnit : ceilingUnits) {
			ceilingUnit.drawOn(graphicsContext);
		}
		for (FloorUnit floorUnit : floorUnits) {
			floorUnit.drawOn(graphicsContext);
		}
		for (WallUnit wallUnit : wallUnits) {
			wallUnit.drawOn(graphicsContext);
		}
		for (Hero hero : heroes) {
			hero.drawOn(graphicsContext);
		}
		for (Enemy enemy : enemies) {
			enemy.drawOn(graphicsContext);
		}
		for (EnemyProjectile enemyProjectile : enemyProjectiles) {
			enemyProjectile.drawOn(graphicsContext);
		}
		for (HeroProjectile heroProjectile : heroProjectiles) {
			heroProjectile.drawOn(graphicsContext);
		}
		for (Award fruit : fruits) {
			fruit.drawOn(graphicsContext);
		}
		for (Bubble bubble : bubbles) {
			bubble.drawOn(graphicsContext);
		}
		for (CollectEffect collectEffect : collectEffects) {
			collectEffect.drawOn(graphicsContext);
		}
	}

	public void updatePosition() {
		//updates positions of everything on screen
		for (Hero hero : heroes) {
			hero.update();
		}
		for (Enemy enemy : enemies) {
			enemy.update();
			if(enemy.canRemove) {
				toBeRemoved.add(enemy);
			}
		}
		for (EnemyProjectile enemyProjectile : enemyProjectiles) {
			enemyProjectile.update();
			if (enemyProjectile.canRemove) {
				toBeRemoved.add(enemyProjectile);
			}
		}
		for (HeroProjectile heroProjectile : heroProjectiles) {
			heroProjectile.update();
			if (heroProjectile.canRemove) {
				toBeRemoved.add(heroProjectile);
			}
		}
		for (Award fruit : fruits) {
			fruit.update();
			if (fruit.canRemove) {
				toBeRemoved.add(fruit);
			}
		}
		for (Bubble bubble : bubbles) {
			//charge = 0;
			bubble.update();
			if (bubble.canRemove) {
				toBeRemoved.add(bubble);
			}
		}
		for (CollectEffect collectEffect : collectEffects) {
//			collectEffect.up
			collectEffect.update();
			if (collectEffect.canRemove) {
				toBeRemoved.add(collectEffect);
			}
		}

		// Colliding...
		// Units initiate collisions with Heroes, Enemies, and Fruits
		for (CeilingUnit ceilingUnit : ceilingUnits) {
			for (Hero hero : heroes) {
				ceilingUnit.collideWith(hero);
			}
			for (Enemy enemy : enemies) {
				ceilingUnit.collideWith(enemy);
				enemy.collideWith(ceilingUnit);
			}
			for (Award fruit : fruits) {
				ceilingUnit.collideWith(fruit);
			}
			for (EnemyProjectile enemyProjectile : enemyProjectiles) {
				ceilingUnit.collideWith(enemyProjectile);
			}
			for (HeroProjectile heroProjectile : heroProjectiles) {
				ceilingUnit.collideWith(heroProjectile);
			}
		}
		for (FloorUnit floorUnit: floorUnits) {
			for (Hero hero : heroes) {
				floorUnit.collideWith(hero);
			}
			for (Enemy enemy : enemies) {
				floorUnit.collideWith(enemy);
				enemy.collideWith(floorUnit);
			}
			for (Award fruit : fruits) {
				floorUnit.collideWith(fruit);
			}
			for (EnemyProjectile enemyProjectile : enemyProjectiles) {
				floorUnit.collideWith(enemyProjectile);
			}
			for (HeroProjectile heroProjectile : heroProjectiles) {
				floorUnit.collideWith(heroProjectile);
			}
		}
		for (WallUnit wallUnit : wallUnits) {
			for (Hero hero : heroes) {
				wallUnit.collideWith(hero);
			}
			for (Enemy enemy : enemies) {
				wallUnit.collideWith(enemy);
				enemy.collideWith(wallUnit);
			}
			for (Award fruit : fruits) {
				wallUnit.collideWith(fruit);
			}
			for (EnemyProjectile enemyProjectile : enemyProjectiles) {
				wallUnit.collideWith(enemyProjectile);
			}
			for (HeroProjectile heroProjectile : heroProjectiles) {
				wallUnit.collideWith(heroProjectile);
			}
		}
		// Enemies initiate collisions with Heroes
		for (Enemy enemy : enemies) {
			for (Hero hero : heroes) {
				enemy.collideWith(hero);
			}
		}
		// HeroProjectiles initiate collisions with Heroes and Enemies
		for (HeroProjectile heroProjectile : heroProjectiles) {
			for (Hero hero : heroes) {
				heroProjectile.collideWith(hero);
			}
			for (Enemy enemy : enemies) {
				heroProjectile.collideWith(enemy);
			}
		}
		for (EnemyProjectile enemyProjectile  : enemyProjectiles) {
			for (Hero hero : heroes) {
				enemyProjectile.collideWith(hero);
			}
			for (Enemy enemy : enemies) {
				enemyProjectile.collideWith(enemy);
			}
		}
		// Fruits intiate collisions with Heroes
		for (Award fruit : fruits) {
			for (Hero hero : heroes) {
				fruit.collideWith(hero);
			}
		}
		for (Bubble bubble : bubbles) {
			for (Enemy enemy : enemies) {
				bubble.collideWith(enemy);
			}
		}

		// Removing...
		for (SpriteObject obj : toBeRemoved) {
			remove(obj);
		}
		toBeRemoved.removeAll(toBeRemoved);

		if (enemies.isEmpty()) {
			GameSceneController.gameState = GameStatus.WIN;
		}


	}

	public void addCeilingUnit(CeilingUnit ceilingUnit) {
		ceilingUnits.add(ceilingUnit);
	}

	public void addFloorUnit(FloorUnit floorUnit) {
		floorUnits.add(floorUnit);
	}

	public void addWallUnit(WallUnit wallUnit) {
		wallUnits.add(wallUnit);
	}

	void addHero(Hero hero) {
		//adds a hero to the map
		heroes.add(hero);
	}

	void addEnemy(Enemy enemy) {
		//adds a mook to the map
		enemies.add(enemy);
	}

	public void addHeroProjectile(HeroProjectile heroProjectile) {
		//adds a projectile to where necessary
		heroProjectiles.add(heroProjectile);
	}

	public void addEnemyProjectile(EnemyProjectile enemyProjectile) {
		//adds a projectile to where necessary
		enemyProjectiles.add(enemyProjectile);
	}

	public void addFruit(Award fruit) {
		//adds fruit on bubble pop
		fruits.add(fruit);
	}

	public void addBubble(Bubble bubble) {
		//adds special bubble
		bubbles.add(bubble);
	}

	public void addCollectEffect(CollectEffect collectEffect) {
		collectEffects.add(collectEffect);
	}

	public void clearContents() {
		//clears everything from the screen
		ceilingUnits.removeAll(ceilingUnits);
		floorUnits.removeAll(floorUnits);
		wallUnits.removeAll(wallUnits);
		heroes.removeAll(heroes);
		enemies.removeAll(enemies);
		enemyProjectiles.removeAll(enemyProjectiles);
		heroProjectiles.removeAll(heroProjectiles);
		fruits.removeAll(fruits);
	}

	public void remove(SpriteObject obj) {
		//removes a single object from the screen
		ceilingUnits.remove(obj);
		floorUnits.remove(obj);
		wallUnits.remove(obj);
		heroes.remove(obj);
		enemies.remove(obj);
		enemyProjectiles.remove(obj);
		heroProjectiles.remove(obj);
		fruits.remove(obj);
		bubbles.remove(obj);
		collectEffects.remove(obj);
	}

//	public void markToReset() {
//		//sets boolean to make sure the world is ready to be reset
//		readyToReset = true;
//	}

	public void readMap(String mapName) {
		InputStream input = this.getClass().getResourceAsStream(mapName);
		Scanner scanner = new Scanner(input);

		clearContents();
		for (int row = 0; row < HEIGHT; row++) {
			String currentLine = scanner.next();
			for (int col = 0; col < WIDTH; col++) {
				if (currentLine.charAt(col) == '*') {
					addFloorUnit(new FloorUnit(this, col, row));
				} else if (currentLine.charAt(col) == 'H') {
					addHero(new Hero(this, col, row));
				} else if (currentLine.charAt(col) == '|') {
					addWallUnit(new WallUnit(this, col, row));
				} else if (currentLine.charAt(col) == '_') {
					addCeilingUnit(new CeilingUnit(this, col, row));
				} else if (currentLine.charAt(col) == 'M') {
					addEnemy(new Enemy(this, col, row));
				}
			}
			if (scanner.hasNextLine()) {
				scanner.nextLine();
			}
		}
		scanner.close();

	}

	public int getHeight() {
		return HEIGHT * UNIT_SIZE;
	}

	public int getWidth() {
		return WIDTH * UNIT_SIZE;
	}

//	public Scene getScene() { return stage.getScene(); }


	public static void load() {
		AnchorPane root = null;
        try {
		root = FXMLLoader.load(Menu.class.getResource("/fxml/GameScene/gameScene.fxml"));
		Main.stage.getScene().setRoot(root);
		} catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void getCanvas() {
		AnchorPane root = (AnchorPane) Main.stage.getScene().getRoot();
		System.out.println(root.getChildren());
		Group temp = (Group) root.getChildren().get(0);
		Canvas canvas = (Canvas) temp.getChildren().get(3);
		graphicsContext = canvas.getGraphicsContext2D();
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public Theme getTheme() {
		return theme;
	}
}

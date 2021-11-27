package com.ae2dms.GameObject.Objects;

import com.ae2dms.GameObject.GameObject;
import com.ae2dms.Scene.GameScene;
import com.ae2dms.SoundEffect;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import static com.ae2dms.GamePanel.UNIT_SIZE;


/**
 * A Hero is a com.ae2dms.GameObject.GameObject that is controllable by the player.
 * Of all the com.ae2dms.GameObject.GameObject, only Hero has KeyBindings.
 * Hero can shoot HeroProjectiles, shield from attacks, trigger a special attack and
 * collect Fruits for points.
 */
public class Hero extends GameObject {
	private static final int JUMP_SPEED = 22;
	private static final int TERMINAL_VELOCITY_X = 6;
	private static final int SIZE = 20;
	private static final int WALK = 5;
	private static final int RUN = 10;
	private static final double RUN_ACCEL = 5;
//	private static final double RUN_ACCEL = 15;
//	private static final double RUN_ACCEL = 20;
	private static final int SHIELD_TIME = 100;
	
	private boolean isShielding;
	private int shieldTimer;
	private boolean isStunned;
	private int stunTimer;
	private int shootDelay;
	private boolean readyToCharge;
	private boolean isOnAPlatform;
	private double jumpSpeed;
	
	public Hero(GameScene world, int colNum, int rowNum) {
		//initializes hero
		super(colNum * UNIT_SIZE, rowNum * UNIT_SIZE, SIZE, SIZE, world);
		isOnAPlatform = false;

		terminal_xVelocity = TERMINAL_VELOCITY_X;
		jumpSpeed = JUMP_SPEED;
		
		isShielding = false;
		shieldTimer = SHIELD_TIME;
		isStunned = false;
		stunTimer = 250;
		shootDelay = 0;
		readyToCharge = false;
		addKeyHandler(world.getScene());
//		addKeyBindings(world.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), world.getActionMap());
	}
	
	public void drawOn(GraphicsContext g) {
		//draws hero
		g.setFill(Color.RED);
		g.fillRect(x, y, SIZE, SIZE);
		if (isShielding) {
			g.setFill(new Color(1,0, (double) (shieldTimer * ((double) 255 / SHIELD_TIME))/255, (double) (shieldTimer * ((double) 255 / SHIELD_TIME))/255));
			g.fillOval(x - 10, y - 10, SIZE + 20, SIZE + 20);
		} else if (isStunned) {
			g.setFill(Color.MAGENTA);
			g.fillRect(x, y, SIZE, SIZE);
		}
		g.setFill(Color.BLACK);
		
	}
	
	public void shootProjectile() {
		//makes hero shoot projectile
		SoundEffect.play("/sound/jump.wav");
		world.addHeroProjectile(new HeroProjectile(world, x, y, direction));
	}

	void collideWithMook() {
		//handles colliding with a mook
		if (!isShielding) {
			die();
		}
	}

	private void jump() {
		//handles jumping
		if (isOnAPlatform) {
			y -= 1;
			yVelocity = -jumpSpeed;
			isOnAPlatform = false;
		}
	}

	public void addKeyHandler(Scene scene) {
		scene.setOnKeyPressed(event -> {
			KeyCode keyCode = event.getCode();
			switch (keyCode) {
				case RIGHT:
					if (!isShielding && !isStunned) {
						xAccel = RUN_ACCEL;
						direction = 1;
					}
					break;
				case LEFT:
					if (!isShielding && !isStunned) {
						xAccel = -RUN_ACCEL;
						direction = -1;
					}
					break;
				case UP:
					if (!isShielding && !isStunned) {
						jump();
						SoundEffect.play("/sound/jump.wav");
					}
					break;
				case E:
					if (!isShielding && !isStunned) {
						shootDelay -= 1;
						if (shootDelay <= 0) {
							shootProjectile();
							shootDelay = 10;
						}
					}
					break;
				case SPACE:
					terminal_xVelocity = RUN;
					break;
				case Q:
					if (!isStunned) {
						xVelocity = 0;
						xAccel = 0;
						isShielding = true;
					}
					break;
				case W:
					if (readyToCharge) {
						world.addBubble(new Bubble(world, x, y));
						SoundEffect.setToLoud();
						SoundEffect.play("/sound/explode.wav");
//						SoundEffect.EXPLODE.setToLoud();
//						SoundEffect.EXPLODE.play();
						readyToCharge = false;
					}
					break;
			}
		});

		scene.setOnKeyReleased(event -> {
			switch (event.getCode()) {
				case RIGHT:
				case LEFT:
					xAccel = 0;
					break;
				case E:
					shootDelay = 0;
					break;
				case SPACE:
					terminal_xVelocity = WALK;
					break;
				case Q:
					isShielding = false;
					break;
			}
		});
	}

//	private void addKeyBindings(InputMap inputMap, ActionMap actionMap) {
//		//maps all buttons with bindings
//
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "Move right");
//		actionMap.put("Move right", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				if (!isShielding && !isStunned) {
//					xAccel = RUN_ACCEL;
//					direction = 1;
//				}
//			}
//		});
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "Stop moving right");
//		actionMap.put("Stop moving right", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				xAccel = 0;
//			}
//		});
//
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "Move left");
//		actionMap.put("Move left", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				if (!isShielding && !isStunned) {
//					xAccel = -RUN_ACCEL;
//					direction = -1;
//				}
//			}
//		});
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "Stop moving left");
//		actionMap.put("Stop moving left", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				xAccel = 0;
//			}
//		});
//
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "Jump");
//		actionMap.put("Jump", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				if (!isShielding && !isStunned) {
//					jump();
//					SoundEffect.JUMP.play();
//				}
//			}
//		});
//
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "Dash");
//		actionMap.put("Dash", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				terminal_xVelocity = RUN;
//			}
//		});
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "Stop dash");
//		actionMap.put("Stop dash", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				terminal_xVelocity = WALK;
//			}
//		});
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false), "Shoot");
//		actionMap.put("Shoot", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				if (!isShielding && !isStunned) {
//					shootDelay -= 1;
//					if (shootDelay <= 0) {
//						shootProjectile();
//						shootDelay = 10;
//					}
//				}
//			}
//		});
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "StopShoot");
//		actionMap.put("StopShoot", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				shootDelay = 0;
//			}
//		});
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), "Shield");
//		actionMap.put("Shield", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				if (!isStunned) {
//					xVelocity = 0;
//					xAccel = 0;
//					isShielding = true;
//				}
//			}
//		});
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), "NoShield");
//		actionMap.put("NoShield", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				isShielding = false;
//			}
//		});
//		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "SpecialMove");
//		actionMap.put("SpecialMove", new AbstractAction() {
//			public void actionPerformed(ActionEvent arg0) {
//				if (readyToCharge) {
//					world.addBubble(new Bubble(world, x, y));
//					SoundEffect.EXPLODE.setToLoud();
//					SoundEffect.EXPLODE.play();
//					readyToCharge = false;
//				}
//			}
//		});
//
//
//	}

	@Override
	public void collideWithWall() {
		// Nothing happens
	}
	
	public void die() {
		//handles death
		SoundEffect.setToLoud();
		SoundEffect.play("/sound/death.wav");
		world.markToReset();
	}

	public void collideWithProjectile() {
		//handles collision with projectiles
		if (!isShielding) {
			die();
		}
	}
	@Override
	public void update() {
		//updates position of hero, according to many variables 
		//including whether or not the hero is shielding,
		//or if the hero is stunned
		super.update();
		if (isShielding) {
			shieldTimer -= 1;
			if (shieldTimer <= 0) {
				shieldTimer = 0;
				isShielding = false;
				isStunned = true;
			}
		} else {
			if (shieldTimer < SHIELD_TIME && !isStunned) {
				shieldTimer += 1;
			}
		}
		if (isStunned) {
			stunTimer -= 1;
			if (stunTimer <= 0) {
				isStunned = false;
				stunTimer = 250;
				shieldTimer = SHIELD_TIME;
			}
		}
	}
	
	@Override
	public void collideWithFloor() {
		//handles collision with floor
		yVelocity = 0;
		if (!isOnAPlatform) {
			isOnAPlatform = true;
			SoundEffect.play("/sound/land.wav");
		}
	}

	@Override
	public void collideWithCeiling() {

	}

	public boolean getShielding() {
		//gets whether or not the hero is shielding on this frame and returns it
		return isShielding;
	}
	
	public void setChargeToReady() {
		//sets whether or not the hero is ready to charge the charge shot
		readyToCharge = true;
	}
}
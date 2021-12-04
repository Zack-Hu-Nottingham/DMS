package com.ae2dms.Scene;

import com.ae2dms.Main;
import com.ae2dms.Util.GameTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class GameOver {
    public static StringProperty timeConsumed;
    public static int bonusEarned = 0;

    public GameOver() {}

    public void init(GameScene.GameStatus gameStatus, int timeConsumed, int bonusEarned) {
        this.timeConsumed = new SimpleStringProperty(GameTimer.parseToTimeFormat(timeConsumed));
        this.bonusEarned = bonusEarned;
        if (gameStatus == GameScene.GameStatus.WIN) {
            load("/fxml/GameOver/gameOverWin.fxml");
        } else {
            load("/fxml/GameOver/gameOverLose.fxml");
        }
    }

    public static void load(String FXMLsource) {
        BorderPane root = null;
        try {
            root = FXMLLoader.load(Menu.class.getResource(FXMLsource));
            Main.stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

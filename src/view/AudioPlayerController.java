package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import units.Army;
import units.Unit;

public interface AudioPlayerController {
	
	String archerPath = "resources/audio/archer.mp3";
	String buildPath = "resources/audio/build.mp3";
	String clickPath = "resources/audio/click.mp3";
	String horsePath = "resources/audio/horse.mp3";
	String lostPath = "resources/audio/lost.mp3";
	String soundtrackPath = "resources/audio/soundtrack.mp3";
	String swordPath = "resources/audio/sword.mp3";
	String winPath = "resources/audio/win.mp3";
	
	MediaPlayer audioPlayer(String path);
	void stopMainTrack();
	void stopBattle();
	void stopClick();
	void stopBuild();
	void stopAttack();
	void stopWon();
	void stopLost();
	
	void playMainTrack();
	void playBattle();
	void playClick();
	void playBuild();
	void playAttack(Unit unit);
	void playWon();
	void playLost();
	
	
	
	
}

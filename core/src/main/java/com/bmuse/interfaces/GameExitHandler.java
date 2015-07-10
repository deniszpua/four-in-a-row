package com.bmuse.interfaces;

public interface GameExitHandler {
  void onMenuPressed();
  void onBestTimePressed();
  void onGameFinished(long timeElapsed);
}

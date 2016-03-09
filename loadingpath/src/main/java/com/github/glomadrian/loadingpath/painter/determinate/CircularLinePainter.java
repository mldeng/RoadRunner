package com.github.glomadrian.loadingpath.painter.determinate;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.github.glomadrian.loadingpath.painter.PointPathPainter;
import com.github.glomadrian.loadingpath.painter.configuration.Direction;
import com.github.glomadrian.loadingpath.painter.indeterminate.IndeterminatePathPainter;
import com.github.glomadrian.loadingpath.path.PathContainer;

/**
 * @author Adrián García Lomas
 */
public class CircularLinePainter extends PointPathPainter
    implements DeterminatePathPainter, IndeterminatePathPainter {

  private ValueAnimator movementAnimator;
  private Paint paint;
  private int color = Color.WHITE;
  private float strokeWidth = 20;
  private float zone = 0f;
  private int movementLinePoints = 50;
  private Direction movementDirection = Direction.CLOCKWISE;
  private int movementLoopTime = 3000;
  private float movementLineSize = 0.03f;
  private float sideIncrementSize = 0f;

  public CircularLinePainter(PathContainer pathData, View view) {
    super(pathData, view);
    initConfiguration();
    init();
  }

  private void initConfiguration() {
    movementDirection = Direction.CLOCKWISE;
    color = Color.RED;
    strokeWidth = 15;
    movementLinePoints = getNumberOfLinePointsInRange(movementLineSize);
  }

  private void init() {
    initPaint();
    initLineMovement();
    initMovementAnimator();
  }

  public void initPaint() {
    paint = new Paint();
    paint.setColor(color);
    paint.setStrokeWidth(strokeWidth);
  }

  private void initLineMovement() {
    movementLinePoints = getNumberOfLinePointsInRange(movementLineSize);
  }

  private void initMovementAnimator() {
    if (movementDirection.equals(Direction.CLOCKWISE)) {
      movementAnimator = ValueAnimator.ofFloat(0, 1);
    } else {
      movementAnimator = ValueAnimator.ofFloat(1, 0);
    }
    movementAnimator.setDuration(movementLoopTime);
    movementAnimator.setInterpolator(new LinearInterpolator());
    movementAnimator.setRepeatCount(ValueAnimator.INFINITE);
    movementAnimator.addUpdateListener(new MovementLineAnimatorUpdateListener());
  }

  @Override
  public void paintPath(Canvas canvas) {
    int pointInRange = getNumberOfPointsInRange(sideIncrementSize) / 2;
    drawWithOffset(zone, pointInRange, pointInRange, movementLinePoints, canvas, paint);
  }

  @Override
  public void setProgress(float value) {
    if (value < 1F && value > 0) {
      sideIncrementSize = value;
    }
  }

  @Override
  public void start() {
    movementAnimator.start();
  }

  @Override
  public void stop() {
    movementAnimator.end();
  }

  @Override
  public void restart() {
    movementAnimator.end();
    movementAnimator.start();
  }

  private class MovementLineAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
      zone = (float) animation.getAnimatedValue();
      view.invalidate();
    }
  }
}

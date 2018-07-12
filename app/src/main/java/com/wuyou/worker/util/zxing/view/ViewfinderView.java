/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wuyou.worker.util.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.gs.buluo.common.utils.DensityUtils;
import com.wuyou.worker.R;
import com.wuyou.worker.util.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static int CORNER_LENGTH;
	private static int CORNER_WIDTH;
	private static final long ANIMATION_DELAY = 20L;
	private static final int OPAQUE = 0xFF;

	private int lastPos = 2;
	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;
    private Bitmap mBmpFrame;
    private Bitmap mBmpLaser;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
        CORNER_LENGTH = DensityUtils.dip2px(context,15);
        CORNER_WIDTH = DensityUtils.dip2px(context,3);
		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.mask);
		resultColor = resources.getColor(R.color.common_gray);
		frameColor = resources.getColor(R.color.main_blue);
		laserColor = resources.getColor(R.color.common_dark);
		resultPointColor = Color.YELLOW;
		scannerAlpha = 0;
		possibleResultPoints = new HashSet<ResultPoint>(5);
		lastPossibleResultPoints = new HashSet<ResultPoint>(5);

        mBmpFrame = BitmapFactory.decodeResource(getResources(), R.mipmap.qr_frame);
        mBmpLaser = BitmapFactory.decodeResource(getResources(), R.mipmap.qr_code_line);
	}

	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		// if (resultBitmap != null) {
		// // Draw the opaque result bitmap over the scanning rectangle
		// paint.setAlpha(OPAQUE);
		// canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		// } else {

		// Draw a two pixel solid black border inside the framing rect
//		paint.setColor(frameColor);
//		canvas.drawRect(frame.left, frame.top, frame.right + 1, frame.top + 2, paint);
//		canvas.drawRect(frame.left, frame.top + 2, frame.left + 2,
//				frame.bottom - 1, paint);
//		canvas.drawRect(frame.right - 1, frame.top, frame.right + 1, frame.bottom - 1, paint);
//		canvas.drawRect(frame.left, frame.bottom - 1, frame.right + 1,
//				frame.bottom + 1, paint);

//		// 画出四个角
//		paint.setColor(getResources().getColor(R.color.bg_titlebar));
//		// 左上角
//		canvas.drawRect(frame.left, frame.top, frame.left + CORNER_LENGTH,
//				frame.top + CORNER_WIDTH, paint);
//		canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH,
//				frame.top + CORNER_LENGTH, paint);
//		// 右上角
//		canvas.drawRect(frame.right - CORNER_LENGTH, frame.top, frame.right,
//				frame.top + CORNER_WIDTH, paint);
//		canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right,
//				frame.top + CORNER_LENGTH, paint);
//		// 左下角
//		canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
//				+ CORNER_LENGTH, frame.bottom, paint);
//		canvas.drawRect(frame.left, frame.bottom - CORNER_LENGTH, frame.left
//				+ CORNER_WIDTH, frame.bottom, paint);
//		// 右下角
//		canvas.drawRect(frame.right - CORNER_LENGTH, frame.bottom - CORNER_WIDTH, frame.right, frame.bottom, paint);
//		canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - CORNER_LENGTH, frame.right, frame.bottom, paint);

        Rect bmpSrc = new Rect(0,0,DensityUtils.dip2px(getContext(),450),DensityUtils.dip2px(getContext(),450));
        canvas.drawBitmap(mBmpFrame,bmpSrc,frame,paint);

		// Draw a red "laser scanner" line through the middle to show
		// decoding is active

		paint.setColor(laserColor);
		// paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
		scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
		// int middle = frame.height() / 2 + frame.top;
		int middle = lastPos + frame.top;
//		canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1,
//				middle + 2, paint);
        Rect laserSrc = new Rect(0,0,mBmpLaser.getWidth(),mBmpLaser.getHeight());
        Rect laserDst = new Rect(frame.left + 2, middle - 3, frame.right - 2, middle + 3);
        canvas.drawBitmap(mBmpLaser,laserSrc,laserDst,paint);
		lastPos = (lastPos + 6) % frame.height();

		Collection<ResultPoint> currentPossible = possibleResultPoints;
		Collection<ResultPoint> currentLast = lastPossibleResultPoints;

		if (currentLast != null) {
			paint.setAlpha(OPAQUE / 2);
			paint.setColor(resultPointColor);
			for (ResultPoint point : currentLast) {
				canvas.drawCircle(frame.left + point.getX(),
						frame.top + point.getY(), 3.0f, paint);
			}
		}
		lastPossibleResultPoints.clear();
		if (!currentPossible.isEmpty()) {
			paint.setAlpha(OPAQUE);
			paint.setColor(resultPointColor);
			for (ResultPoint point : currentPossible) {
				lastPossibleResultPoints.add(point);
				canvas.drawCircle(frame.left + point.getX(),
						frame.top + point.getY(), 6.0f, paint);
			}
		}
		possibleResultPoints.clear();

		// Request another update at the animation interval, but only
		// repaint the laser line,
		// not the entire viewfinder mask.
		postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
				frame.right, frame.bottom);
		// }
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}

package org.anddev.andengine.entity.scene.background;

import java.util.ArrayList;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.shape.IAreaShape;
import org.anddev.andengine.opengl.util.GLState;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:36:26 - 19.07.2010
 */
public class ParallaxBackground extends Background {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ParallaxEntity> mParallaxEntities = new ArrayList<ParallaxEntity>();
	private int mParallaxEntityCount;

	protected float mParallaxValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallaxBackground(final float pRed, final float pGreen, final float pBlue) {
		super(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParallaxValue(final float pParallaxValue) {
		this.mParallaxValue = pParallaxValue;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDraw(final Camera pCamera) {
		super.onDraw(pCamera);

		final float parallaxValue = this.mParallaxValue;
		final ArrayList<ParallaxEntity> parallaxEntities = this.mParallaxEntities;

		for(int i = 0; i < this.mParallaxEntityCount; i++) {
			parallaxEntities.get(i).onDraw(parallaxValue, pCamera);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void attachParallaxEntity(final ParallaxEntity pParallaxEntity) {
		this.mParallaxEntities.add(pParallaxEntity);
		this.mParallaxEntityCount++;
	}

	public boolean detachParallaxEntity(final ParallaxEntity pParallaxEntity) {
		this.mParallaxEntityCount--;
		final boolean success = this.mParallaxEntities.remove(pParallaxEntity);
		if(!success) {
			this.mParallaxEntityCount++;
		}
		return success;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class ParallaxEntity {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		final float mParallaxFactor;
		final IAreaShape mAreaShape;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ParallaxEntity(final float pParallaxFactor, final IAreaShape pAreaShape) {
			this.mParallaxFactor = pParallaxFactor;
			this.mAreaShape = pAreaShape;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onDraw(final float pParallaxValue, final Camera pCamera) {
			GLState.pushModelViewGLMatrix();
			{
				final float cameraWidth = pCamera.getWidth();
				final float shapeWidthScaled = this.mAreaShape.getWidthScaled();
				float baseOffset = (pParallaxValue * this.mParallaxFactor) % shapeWidthScaled;

				while(baseOffset > 0) {
					baseOffset -= shapeWidthScaled;
				}
				GLState.translateModelViewGLMatrixf(baseOffset, 0, 0);

				float currentMaxX = baseOffset;
				
				do {
					this.mAreaShape.onDraw(pCamera);
					GLState.translateModelViewGLMatrixf(shapeWidthScaled, 0, 0);
					currentMaxX += shapeWidthScaled;
				} while(currentMaxX < cameraWidth);
			}
			GLState.popModelViewGLMatrix();
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}

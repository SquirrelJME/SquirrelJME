// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a 2D Sprite object, which may be transformed, rotated, and checked
 * for collision against other Sprites and Tiles.
 *
 * @since 2026/08/09
 */
@Api
public class Sprite
	extends Layer
{
	/** A transformation that mirrors an image. */
	@Api
	public static final int TRANS_MIRROR =
		2;

	/** A transformation that mirrors and rotates 180 degrees clockwise. */
	@Api
	public static final int TRANS_MIRROR_ROT180 =
		1;

	/** A transformation that mirrors and rotates 270 degrees clockwise. */
	@Api
	public static final int TRANS_MIRROR_ROT270 =
		4;

	/** A transformation that mirrors and rotates 90 degrees clockwise. */
	@Api
	public static final int TRANS_MIRROR_ROT90 =
		7;

	/** Constant that defines that no transformation is to be done. */
	@Api
	public static final int TRANS_NONE =
		0;

	/** A transformation that rotates an image 180 degrees clockwise. */
	@Api
	public static final int TRANS_ROT180 =
		3;

	/** A transformation that rotates an image 270 degrees clockwise. */
	@Api
	public static final int TRANS_ROT270 =
		6;

	/** A transformation that rotates an image 90 degrees clockwise. */
	@Api
	public static final int TRANS_ROT90 =
		5;

	/** The image containing this Sprite's drawable data. */
	private Image _image;

	/** The amount of frames of animation this Sprite has. */
	private int _numFrames;

	/** An array containing all of this Sprite's frames X image coordinates. */
	private int[] _frameCoordX;

	/** An array containing all of this Sprite's frames Y image coordinates. */
	private int[] _frameCoordY;

	/** The width of each of this Sprite's frames of animation. */
	private int _frameWidth;

	/** The height of each of this Sprite's frames of animation. */
	private int _frameHeight;

	/** This Sprite's currently set sequence of frames. */
	private int[] _frameSequence;

	/** The current animation frame index this Sprite will draw. */
	private int _frameIndex;

	/** Whether this Sprite is using a custom frame sequence. */
	private boolean _isCustomSequence;

	/** This Sprite's reference pixel X position. */
	private int _refX;

	/** This Sprite's reference pixel Y position. */
	private int _refY;

	/** This Sprite's collision rect X position. */
	private int _collRectX;

	/** This Sprite's collision rect Y position. */
	private int _collRectY;

	/** This Sprite's collision rect width. */
	private int _collRectW;

	/** This Sprite's collision rect height. */
	private int _collRectH;

	/** The current transform in use by this Sprite. */
	@MagicConstant(valuesFromClass = Sprite.class)
	private int _curTrans;

	/**
	 * Initializes a {@link Sprite} from the given image data. As there is no
	 * frame size specifications, this {@link Sprite} will be a static image
	 * initially, as this constructor is equivalent to creating a {@link Sprite}
	 * with {@code Sprite(__image, __image.getWidth(), __image.getHeight())}.
	 *
	 * @param __image The image containing sprite data.
	 * @throws NullPointerException If {@code __image} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public Sprite(@NotNull Image __image)
		throws NullPointerException
	{
		this(__image, __image.getWidth(), __image.getHeight());
	}

	/**
	 * Initializes an animated {@link Sprite} from the given image data, where
	 * each frame of the Sprite's animation will have a size of
	 * {@code __frameWidth * __frameHeight}.
	 *
	 * @param __image The image containing sprite data.
	 * @param __frameWidth The width of each animation frame.
	 * @param __frameHeight The height of each animation frame.
	 * @throws NullPointerException If {@code __image} is {@code null}.
	 * @throws IllegalArgumentException If {@code __frameWidth} or
	 * {@code __frameHeight} are less than 1.
	 * @throws IllegalArgumentException If the {@code __image} size is not a
	 * multiple of {@code __frameWidth} and {@code __frameHeight}.
	 * @since 2026/08/09
	 */
	@Api
	public Sprite(@NotNull Image __image,
		@Range(from = 1, to = Integer.MAX_VALUE) int __frameWidth,
		@Range(from = 1, to = Integer.MAX_VALUE) int __frameHeight)
		throws NullPointerException, IllegalArgumentException
	{
		if (__image == null)
			throw new NullPointerException("NARG");

		if ((__frameWidth < 1 || __frameHeight < 1) ||
			((__image.getWidth() % __frameWidth) != 0) ||
			((__image.getHeight() % __frameHeight) != 0))
			throw new IllegalArgumentException("INVL");

		this._width = __frameWidth;
		this._height = __frameHeight;

		this.__prepareFrames(__image, __frameWidth, __frameHeight, false);
		this.setTransform(Sprite.TRANS_NONE);

		// setTransform sets this._width and this._height so we can use them
		this.defineCollisionRectangle(0, 0, this._width, this._height);
	}

	/**
	 * Initializes a new {@link Sprite} instance with data copied from another
	 * Sprite.
	 *
	 * @param __s The sprite instance to be copied into a new instance.
	 * @throws NullPointerException If {@code __s} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public Sprite(@NotNull Sprite __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");

		this._width = __s.getWidth();
		this._height = __s.getHeight();

		this._image = Image.createImage(__s._image);
		this._numFrames = __s._numFrames;
		this._frameCoordX = new int[this._numFrames];
		this._frameCoordY = new int[this._numFrames];

		// Copy base properties from the received sprite
		this._x = __s.getX();
		this._y = __s.getY();
		this._refX = __s._refX;
		this._refY = __s._refY;
		this._collRectX = __s._collRectX;
		this._collRectY = __s._collRectY;
		this._collRectW = __s._collRectW;
		this._collRectH = __s._collRectH;
		this._frameWidth = __s._frameWidth;
		this._frameHeight = __s._frameHeight;

		this.setTransform(__s._curTrans);
		this.setVisible(__s.isVisible());

		this.setRefPixelPosition(__s.getRefPixelX(),
			__s.getRefPixelY());

		// Copy frame coordinates and sequence status from the received sprite
		System.arraycopy(__s._frameCoordX, 0, this._frameCoordX, 0,
			__s.getRawFrameCount());
		System.arraycopy(__s._frameCoordY, 0, this._frameCoordY, 0,
			__s.getRawFrameCount());

		this._frameSequence = new int[__s.getFrameSequenceLength()];
		this.setFrameSequence(__s._frameSequence);
		this.setFrame(__s.getFrame());
	}

	/**
	 * Checks if this {@link Sprite} is colliding with the given {@link Image}
	 * either through basic collision rect bounds checking or per-pixel
	 * detection.
	 *
	 * Note that collision accounts for this Sprite's transforms, and only
	 * happens if this Sprite is visible.
	 *
	 * @param __image The {@link Image} to check collision against.
	 * @param __x The {@link Image} left corner's position.
	 * @param __y The {@link Image} top corner's position.
	 * @param __pixelLevel If {@code true}, means per-pixel check is requested.
	 * @return Whether this Sprite is colliding with the {@link Image}.
	 * @throws NullPointerException If {@code __image} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public final boolean collidesWith(@NotNull Image __image, int __x, int __y,
		boolean __pixelLevel)
		throws NullPointerException
	{
		if (__image == null)
			throw new NullPointerException("NARG");

		if (!this.isVisible())
			return false;

		int thisLeft = this._x + this._collRectX;
		int thisTop = this._y + this._collRectY;
		int thisRight = thisLeft + this._collRectW;
		int thisBottom = thisTop + this._collRectH;
		
		int otherRight = __x + __image.getWidth();
		int otherBottom = __y + __image.getHeight();

		if (this.__intersects(__x, __y, otherRight, otherBottom,
			thisLeft, thisTop, thisRight, thisBottom))
		{
			if (!__pixelLevel)
				return true;

			if (this._collRectX < 0)
				thisLeft = this._x;

			if (this._collRectY < 0)
				thisTop = this._y;

			if ((this._collRectX + this._collRectW) >
				this._width)
				thisRight = this._x + this._width;

			if ((this._collRectY + this._collRectH) >
				this._height)
				thisBottom = this._y + this._height;

			if (!this.__intersects(__x, __y, otherRight,
				otherBottom, thisLeft, thisTop, thisRight, thisBottom))
				return false;

			int intersectLeft = Math.max(thisLeft, __x);

			int intersectTop = Math.max(thisTop, __y);

			int intersectRight = Math.min(thisRight, otherRight);

			int intersectBottom = Math.min(thisBottom, otherBottom);

			int intersectWidth = Math.abs(intersectRight - intersectLeft);
			int intersectHeight = Math.abs(intersectBottom - intersectTop);

			int thisXOffset = this.__getDataTopLeft(intersectLeft,
				intersectTop,
				intersectRight,
				intersectBottom, true);

			int thisYOffset = this.__getDataTopLeft(intersectLeft,
				intersectTop,
				intersectRight,
				intersectBottom, false);

			int otherXOffset = intersectLeft - __x;
			int otherYOffset = intersectTop - __y;

			return this.__pixelCollision(thisXOffset, thisYOffset,
				otherXOffset, otherYOffset,
				this._image, this._curTrans,
				__image, Sprite.TRANS_NONE,
				intersectWidth, intersectHeight);
		}

		return false;
	}

	/**
	 * Checks if this {@link Sprite} is colliding with another Sprite, either
	 * through basic collision rect bounds checking or per-pixel detection.
	 *
	 * Note that collision accounts for each Sprite's transforms, and only
	 * happens if both Sprites are visible.
	 *
	 * @param __s The other Sprite to check collision against.
	 * @param __pixelLevel If {@code true}, means per-pixel check is requested.
	 * @return Whether this Sprite is colliding with the received one.
	 * @throws NullPointerException If {@code __s} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public final boolean collidesWith(@NotNull Sprite __s, boolean __pixelLevel)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");

		if (!(__s.isVisible() && this.isVisible()))
			return false;

		int otherLeft = __s._x + __s._collRectX;
		int otherTop = __s._y + __s._collRectY;
		int otherRight = otherLeft + __s._collRectW;
		int otherBottom = otherTop + __s._collRectH;

		int thisLeft = this._x + this._collRectX;
		int thisTop = this._y + this._collRectY;
		int thisRight = thisLeft + this._collRectW;
		int thisBottom = thisTop + this._collRectH;

		if (this.__intersects(otherLeft, otherTop, otherRight, otherBottom,
			thisLeft, thisTop, thisRight, thisBottom))
		{
			// We have a bounds collision, now we need to see if they collide
			// on a pixel level (if requested), or we can just return true.
			if (!__pixelLevel)
				return true;

			// For per-pixel checks, we only check valid areas on each
			// sprite data (no negative index access on any image), so we
			// must adjust anything that goes out of bounds.
			if (this._collRectX < 0)
				thisLeft = this._x;

			if (this._collRectY < 0)
				thisTop = this._y;

			if ((this._collRectX + this._collRectW) >
				this._width)
				thisRight = this._x + this._width;

			if ((this._collRectY + this._collRectH) >
				this._height)
				thisBottom = this._y + this._height;

			if (__s._collRectX < 0)
				otherLeft = __s._x;

			if (__s._collRectY < 0)
				otherTop = __s._y;

			if (__s._collRectX + __s._collRectW >
				__s._width)
				otherRight = __s._x + __s._width;

			if (__s._collRectY + __s._collRectH >
				__s._height)
				otherBottom = __s._y + __s._height;

			// Then we do another quick bounds check. If we have no
			// intersection after clipping both areas to valid image areas,
			// we can return no collision early.
			if (!this.__intersects(otherLeft, otherTop, otherRight,
				otherBottom, thisLeft, thisTop, thisRight, thisBottom))
				return false;

			// Otherwise, get only the intersecting area between this
			// sprite and the other one's in order to do less work by
			// having per-pixel checks over a smaller area.
			int intersectLeft = Math.max(thisLeft, otherLeft);

			int intersectTop = Math.max(thisTop, otherTop);

			int intersectRight = Math.min(thisRight, otherRight);

			int intersectBottom = Math.min(thisBottom, otherBottom);

			int intersectWidth = Math.abs(intersectRight - intersectLeft);
			int intersectHeight = Math.abs(intersectBottom - intersectTop);

			int thisXOffset = this.__getDataTopLeft(intersectLeft,
				intersectTop, intersectRight, intersectBottom, true);

			int thisYOffset = this.__getDataTopLeft(intersectLeft,
				intersectTop, intersectRight, intersectBottom, false);

			int otherXOffset = __s.__getDataTopLeft(intersectLeft,
				intersectTop, intersectRight, intersectBottom, true);

			int otherYOffset = __s.__getDataTopLeft(intersectLeft,
				intersectTop, intersectRight, intersectBottom, false);

			return this.__pixelCollision(thisXOffset, thisYOffset,
				otherXOffset, otherYOffset,
				this._image,
				this._curTrans,
				__s._image,
				__s._curTrans,
				intersectWidth, intersectHeight);
		}

		return false;
	}

	/**
	 * Checks if this {@link Sprite} is colliding with the given TiledLayer,
	 * either through basic collision rect bounds checking or per-pixel
	 * detection.
	 *
	 * Note that collision accounts for this Sprite's transforms, and only
	 * happens if both this Sprite and the TiledLayer are visible.
	 *
	 * @param __t The TiledLayer to check collision against.
	 * @param __pixelLevel If {@code true}, means per-pixel check is requested.
	 * @return Whether this Sprite is colliding with the TiledLayer.
	 * @throws NullPointerException If {@code __t} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public final boolean collidesWith(@NotNull TiledLayer __t,
		boolean __pixelLevel)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");

		if (!(__t.isVisible() && this.isVisible()))
			return false;

		int layerX = __t._x;
		int layerY = __t._y;
		int layerW = layerX + __t._width;
		int layerH = layerY + __t._height;

		int sx = this._x + this._collRectX;
		int sy = this._y + this._collRectY;
		int sw = sx + this._collRectW;
		int sh = sy + this._collRectH;

		// If there's no bounds intersection for the whole layer, we can return
		if (!this.__intersects(layerX, layerY, layerW, layerH, sx, sy, sw, sh))
			return false;

		// Otherwise, time to check the TiledLayer's cells
		int tileW = __t.getCellWidth();
		int tileH = __t.getCellHeight();
		int numCols = __t.getColumns();
		int numRows = __t.getRows();

		int startCol = (sx <= layerX ? 0 : (sx - layerX) / tileW);
		int startRow = (sy <= layerY ? 0 : (sy - layerY) / tileH);
		int endCol = (sw < layerW ? ((sw - 1 - layerX) / tileW) : numCols - 1);
		int endRow = (sh < layerH ? ((sh - 1 - layerY) / tileH) : numRows - 1);

		if (!__pixelLevel)
		{
			for (int row = startRow; row <= endRow; row++)
				for (int col = startCol; col <= endCol; col++)
					if (__t.getCell(col, row) != 0)
						return true;

			// The entire intersecting region is comprised of empty cells,
			// return false.
			return false;
		}

		// Like the other collidesWith() above, we need to make sure we're
		// only per-pixel checking this Sprite's region that falls within
		// its image.
		if (this._collRectX < 0)
			sx = this._x;

		if (this._collRectY < 0)
			sy = this._y;

		if ((this._collRectX + this._collRectW) >
			this._width)
			sw = this._x + this._width;

		if ((this._collRectY + this._collRectH) >
			this._height)
			sh = this._y + this._height;

		if (!this.__intersects(layerX, layerY, layerW, layerH, sx, sy, sw,
			sh))
			return false;

		startCol = (sx <= layerX ? 0 : (sx - layerX) / tileW);
		startRow = (sy <= layerY ? 0 : (sy - layerY) / tileH);
		endCol = (sw < layerW ? ((sw - 1 - layerX) / tileW) : numCols - 1);
		endRow = (sh < layerH ? ((sh - 1 - layerY) / tileH) : numRows - 1);

		int cellTop = startRow * tileH + layerY;
		int cellBottom = cellTop + tileH;

		for (int row = startRow; row <= endRow; row++)
		{
			int cellLeft = startCol * tileW + layerX;
			int cellRight = cellLeft + tileW;

			for (int col = startCol; col <= endCol; col++)
			{
				int tileIndex = __t.getCell(col, row);

				// Not a transparent tile, we can check collision
				if (tileIndex != 0)
				{
					int intersectLeft = Math.max(sx, cellLeft);
					int intersectTop = Math.max(sy, cellTop);
					int intersectRight = Math.min(sw, cellRight);
					int intersectBottom = Math.min(sh, cellBottom);

					int intersectWidth = Math.abs(intersectRight -
						intersectLeft);
					int intersectHeight = Math.abs(intersectBottom -
						intersectTop);

					int thisXOffset = this.__getDataTopLeft(intersectLeft,
						intersectTop, intersectRight, intersectBottom,
						true);
					int thisYOffset = this.__getDataTopLeft(intersectLeft,
						intersectTop, intersectRight, intersectBottom,
						false);
					int otherXOffset = __t.__getTileCoord(tileIndex)[0] +
						(intersectLeft - cellLeft);
					int otherYOffset = __t.__getTileCoord(tileIndex)[1] +
						(intersectTop - cellTop);

					if (this.__pixelCollision(thisXOffset, thisYOffset,
						otherXOffset, otherYOffset,
						this._image, this._curTrans,
						__t.__getImage(), Sprite.TRANS_NONE,
						intersectWidth, intersectHeight))
						return true;
				}

				cellLeft += tileW;
				cellRight += tileW;
			}

			cellTop += tileH;
			cellBottom += tileH;
		}

		return false;
	}

	/**
	 * Sets the bounds for this {@link Sprite}'s collision rectangle, which is
	 * used for standard collision detection (not per-pixel).
	 *
	 * Note that the rectangle will be considered to be defined in regards to
	 * an untransformed {@link Sprite}.
	 *
	 * @param __x The left X coordinate of the collision rectangle in relation
	 * to a non-transformed Sprite.
	 * @param __y The top X coordinate of the collision rectangle in relation
	 * to a non-transformed Sprite.
	 * @param __width The width of the collision rectangle in relation to a
	 * non-transformed Sprite.
	 * @param __height The height of the collision rectangle in relation to a
	 * non-transformed Sprite.
	 * @throws IllegalArgumentException If {@code __width} or {@code __height}
	 * are negative.
	 * @since 2026/08/09
	 */
	@Api
	public void defineCollisionRectangle(int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __width,
		@Range(from = 0, to = Integer.MAX_VALUE) int __height)
		throws IllegalArgumentException
	{
		if (__width < 0 || __height < 0)
			throw new IllegalArgumentException("INVL");

		this._collRectX = __x;
		this._collRectY = __y;
		this._collRectW = __width;
		this._collRectH = __height;

		this.setTransform(this._curTrans);
	}

	/**
	 * Defines the reference pixel to be used by subsequent transformations on
	 * this {@link Sprite}. This method does not account for transforms, and
	 * also only affects other methods that use the reference pixel.
	 *
	 * @param __x The new X coordinate of the reference pixel.
	 * @param __y The new Y coordinate of the reference pixel.
	 * @since 2026/08/09
	 */
	@Api
	public void defineReferencePixel(int __x, int __y)
	{
		this._refX = __x;
		this._refY = __y;
	}

	/**
	 * Returns the frame sequence index that is currently set for drawing.
	 *
	 * @return The current frame index.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 0, to = Integer.MAX_VALUE)
	public final int getFrame()
	{
		return this._frameIndex;
	}

	/**
	 * Returns the total amount of animation frames that this {@link Sprite}'s
	 * currently set frame sequence has.
	 *
	 * @return The amount frames in the current frame sequence.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int getFrameSequenceLength()
	{
		return this._frameSequence.length;
	}

	/**
	 * Returns the amount of animation frames that this {@link Sprite} has.
	 *
	 * This value _MAY_ be different from the one returned by calling
	 * {@link Sprite#getFrameSequenceLength()} if this Sprite is using a custom
	 * frame sequence.
	 * @return The amount of animation frames this Sprite has.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int getRawFrameCount()
	{
		return this._numFrames;
	}

	/**
	 * Returns the reference pixel's current Y coordinate, and any transforms
	 * are taken into account.
	 *
	 * @return The reference pixel's X coordinate.
	 * @since 2026/08/09
	 */
	@Api
	public int getRefPixelX()
	{
		return (this._x + this.__getTopLeftCorner(this._refX, this._refY,
			this._curTrans, true));
	}

	/**
	 * Returns the reference pixel's current Y coordinate, and any transforms
	 * are taken into account.
	 *
	 * @return The reference pixel's Y coordinate.
	 * @since 2026/08/09
	 */
	@Api
	public int getRefPixelY()
	{
		return (this._y + this.__getTopLeftCorner(this._refX, this._refY,
			this._curTrans, false));
	}

	/**
	 * Moves to the next frame in this {@link Sprite}'s currently set frame
	 * sequence, wrapping around to the first frame if this is called while
	 * at the end of the sequence.
	 *
	 * @since 2026/08/09
	 */
	@Api
	public void nextFrame()
	{
		this._frameIndex = (this._frameIndex + 1) %
			this._frameSequence.length;
	}

	/**
	 * Paints this {@link Sprite}'s currently set frame of animation.
	 *
	 * Drawing is subject to this {@link Sprite}'s position, transformation and
	 * area bounds, as well as the {@link Graphics} object's translation and
	 * clipping.
	 *
	 * Since sprites extend from {@link Layer}, the top-left corner of the
	 * sprite can be retrieved by calling {@link Layer#getX()} and
	 * {@link Layer#getY()}.
	 *
	 * @param __g The {@link Graphics} object to draw with.
	 * @throws NullPointerException If {@code __g} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	@Override
	public final void paint(@NotNull Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");

		if (!this.isVisible())
			return;

		int frame = this._frameSequence[this._frameIndex];

		__g.drawRegion(this._image,
			this._frameCoordX[frame], this._frameCoordY[frame],
			this._frameWidth, this._frameHeight,
			this._curTrans,
			this._x, this._y,
			Graphics.TOP | Graphics.LEFT);
	}

	/**
	 * Moves to the previous frame in this {@link Sprite}'s currently set frame
	 * sequence, wrapping around to the last frame if this is called while
	 * at the start of the sequence.
	 *
	 * @since 2026/08/09
	 */
	@Api
	public void prevFrame()
	{
		if (this._frameIndex == 0)
			this._frameIndex = this._frameSequence.length - 1;
		else
			this._frameIndex--;
	}

	/**
	 * Sets the alpha value to be used as a threshold for per-pixel collision
	 * detection. Sprite pixels with alpha values lower than this threshold will
	 * be treated as fully transparent, and thus discarded from detection.
	 *
	 * @param __alpha The new alpha threshold to use for collision.
	 * @throws IllegalArgumentException If {@code __alpha} is not in range of
	 * {@code [0, 255]}.
	 * @since 2026/06/28
	 */
	@Api
	public void setCollisionAlpha(@Range(from = 0, to = 255) int __alpha)
		throws IllegalArgumentException
	{
		if (__alpha < 0 || __alpha >= 255)
			throw new IllegalArgumentException("INVL");

		// TODO: Per-Pixel collision isn't implemented yet
		throw Debugging.todo();
	}

	/**
	 * Sets a specific frame index from the current frame sequence for drawing
	 * whenever {@link Sprite#paint(Graphics)} gets called.
	 *
	 * @param __sequenceIndex The frame index to be used for drawing.
	 * @throws IndexOutOfBoundsException if {@code __sequenceIndex} is out of
	 * range.
	 * @since 2026/08/09
	 */
	@Api
	public void setFrame(
		@Range(from = 0, to = Integer.MAX_VALUE) int __sequenceIndex)
		throws IndexOutOfBoundsException
	{
		if (__sequenceIndex < 0 || __sequenceIndex >=
			this._frameSequence.length)
			throw new IndexOutOfBoundsException("IOOB");

		this._frameIndex = __sequenceIndex;
	}

	/**
	 * Sets a new, custom frame sequence into this {@link Sprite}, allowing for
	 * only some of the frames to be accessible, and with different order.
	 *
	 * If {@code __sequence} is {@code null}, any custom frame sequence is
	 * removed, and this {@link Sprite} reverts back to using its default frame
	 * sequence.
	 *
	 * @param __sequence The new frame sequence to set.
	 * @throws ArrayIndexOutOfBoundsException If {@code __sequence} has a
	 * frame index that goes out of range of this Sprite's available frames.
	 * @throws IllegalArgumentException If {@code __sequence} has no frames.
	 * @since 2026/08/09
	 */
	@Api
	public void setFrameSequence(int[] __sequence)
		throws IllegalArgumentException, ArrayIndexOutOfBoundsException
	{
		// Revert to the default frame sequence and return.
		if (__sequence == null)
		{
			this._frameIndex = 0;
			this._isCustomSequence = false;
			this._frameSequence = new int[this._numFrames];
			for (int i = 0; i < this._numFrames; i++)
				this._frameSequence[i] = i;

			return;
		}

		int sequenceLength = __sequence.length;

		if (sequenceLength < 1)
			throw new IllegalArgumentException("INVL");
		
		for (int __i : __sequence)
			if (__i < 0 || __i >= this._numFrames)
				throw new ArrayIndexOutOfBoundsException("IOOB");

		// The sequence array must be copied so that further changes by the
		// application do not affect this Sprite's current sequence.
		this._isCustomSequence = true;
		this._frameSequence = new int[sequenceLength];
		System.arraycopy(__sequence, 0, this._frameSequence, 0,
			sequenceLength);
		this._frameIndex = 0;
	}

	/**
	 * Sets a new image containing the {@link Sprite}'s frame data, replacing
	 * its current set of frames for new ones.
	 *
	 * The currently set frame will remain the same if, and only if the new
	 * image and frame arguments result in equal or higher amount of frames.
	 * Otherwise, the current frame index will revert to 0, and any custom frame
	 * sequence will be discarded, with the {@link Sprite} reverting back to its
	 * default frame sequence.
	 *
	 * @param __img The new image data.
	 * @throws IllegalArgumentException If the image width is not a multiple of
	 * {@code __frameWidth}, if the image height is not a multiple of
	 * {@code __frameHeight}, or if {@code __sequence} has no frames.
	 * @throws NullPointerException If {@code __img} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	@SuppressWarnings("SuspiciousNameCombination")
	public void setImage(@NotNull Image __img,
		@Range(from = 1, to = Integer.MAX_VALUE) int __frameWidth,
		@Range(from = 1, to = Integer.MAX_VALUE) int __frameHeight)
		throws NullPointerException, IllegalArgumentException
	{
		// if image is null image.getWidth() will throw NullPointerException
		if ((__frameWidth < 1 || __frameHeight < 1) ||
			((__img.getWidth() % __frameWidth) != 0) ||
			((__img.getHeight() % __frameHeight) != 0))
			throw new IllegalArgumentException("INVL");

		boolean keepFrameSeq = true;

		int numFrames = (__img.getWidth() / __frameWidth) *
			(__img.getHeight() / __frameHeight);

		if (numFrames < this._numFrames)
		{
			keepFrameSeq = false;
			this._isCustomSequence = false;
		}

		// If the frame size has changed, we need to update the Sprite position
		// and reset the collision rect to the new Sprite bounds
		if (this._frameWidth != __frameWidth ||
			this._frameHeight != __frameHeight)
		{
			int oldXPos = this.getRefPixelX();
			int oldYPos = this.getRefPixelY();

			if (this._curTrans == Sprite.TRANS_MIRROR_ROT270 ||
				this._curTrans == Sprite.TRANS_MIRROR_ROT90 ||
				this._curTrans == Sprite.TRANS_ROT270 ||
				this._curTrans == Sprite.TRANS_ROT90)
			{
				this._width = __frameHeight;
				this._height = __frameWidth;
			}
			else
			{
				this._width = __frameWidth;
				this._height = __frameHeight;
			}

			this.__prepareFrames(__img, __frameWidth, __frameHeight,
				keepFrameSeq);
			this.defineCollisionRectangle(0, 0, this._width, this._height);

			this.setRefPixelPosition(oldXPos, oldYPos);
		}
		else
			this.__prepareFrames(__img, __frameWidth, __frameHeight,
				keepFrameSeq);
	}

	/**
	 * Changes this {@link Sprite}'s position so that the reference pixel is
	 * located on the given {@code [x, y]} coordinate.
	 *
	 * As opposed to {@link Sprite#defineReferencePixel(int, int)}, this method
	 * does account for the current transformation, and also affects the sprite
	 * itself.
	 *
	 * @param __x The new X coordinate of the reference pixel.
	 * @param __y The new Y coordinate of the reference pixel.
	 * @since 2026/08/09
	 */
	@Api
	public void setRefPixelPosition(int __x, int __y)
	{
		this._x = __x - this.__getTopLeftCorner(this._refX, this._refY,
			this._curTrans, true);
		this._y = __y - this.__getTopLeftCorner(this._refX, this._refY,
			this._curTrans, false);
	}

	/**
	 * Sets the transform for this {@link Sprite}, which includes rotations
	 * and mirroring.
	 *
	 * Note that beyond changing the Sprite's x and y coordinates, some
	 * transforms will also change its width and height. The x and y
	 * coordinates are altered so that the reference pixel's coordinate doesn't
	 * change.
	 *
	 * @param __transform The transformation to be applied, can be any of the
	 * valid {@link Sprite} transforms.
	 * @throws IllegalArgumentException If {@code __transform} is not a valid
	 * {@link Sprite} transform.
	 * @since 2026/08/09
	 */
	@Api
	@SuppressWarnings("SuspiciousNameCombination")
	public void setTransform(
		@MagicConstant(valuesFromClass = Sprite.class) int __transform)
		throws IllegalArgumentException
	{
		this._x = this._x + this.__getTopLeftCorner(this._refX, this._refY,
			this._curTrans, true) - this.__getTopLeftCorner(this._refX,
			this._refY, __transform, true);

		this._y = this._y + this.__getTopLeftCorner(this._refX, this._refY,
			this._curTrans, false) - this.__getTopLeftCorner(this._refX,
			this._refY, __transform, false);

		if (__transform == Sprite.TRANS_MIRROR_ROT270 ||
			__transform == Sprite.TRANS_MIRROR_ROT90 ||
			__transform == Sprite.TRANS_ROT270 ||
			__transform == Sprite.TRANS_ROT90)
		{
			this._width = this._frameHeight;
			this._height = this._frameWidth;
		}
		else
		{
			this._width = this._frameWidth;
			this._height = this._frameHeight;
		}

		this._curTrans = __transform;
	}

	/**
	 * Gets {@link Image} data's top left corner position based on this
	 * {@link Sprite}'s current transform.
	 *
	 * @param __x1 The image's original left corner.
	 * @param __y1 The image's original top corner.
	 * @param __x2 The image's original right corner.
	 * @param __y2 The image's original bottom corner.
	 * @param __isX Indicates whether the X or Y coordinate of the top/left
	 * corner should be returned.
	 * @return The X or Y coordinate that represents the top/left corner of
	 * the image data.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	private int __getDataTopLeft(int __x1, int __y1, int __x2, int __y2,
		boolean __isX)
	{
		int ret = 0;

		switch (this._curTrans)
		{
			case Sprite.TRANS_NONE:
			case Sprite.TRANS_MIRROR_ROT180:
				ret = (__isX ? __x1 - this._x : __y1 - this._y);
				break;

			case Sprite.TRANS_MIRROR:
			case Sprite.TRANS_ROT180:
				ret = (__isX ? (this._x + this._width) - __x2 :
					(this._y + this._height) - __y2);
				break;

			case Sprite.TRANS_ROT90:
			case Sprite.TRANS_MIRROR_ROT270:
				ret = (__isX ? __y1 - this._y : (this._x + this._width) - __x2);
				break;

			case Sprite.TRANS_ROT270:
			case Sprite.TRANS_MIRROR_ROT90:
				ret = (__isX ? (this._y + this._height) - __y2 :
					__x1 -this._x);
				break;

			default:
				/* {@squirreljme.error EB3k Invalid transform requested.} */
				throw new IllegalArgumentException("EB3k");
		}

		int seqIndex = this._frameSequence[this._frameIndex];

		if (__isX)
			return ret + this._frameCoordX[seqIndex];

		return ret + this._frameCoordY[seqIndex];
	}

	/**
	 * Gets the starting position of an {@link Image}'s data reads, and how
	 * its X and Y coordinates should increase each iteration.
	 *
	 * @param __transform The transformation that the image is using.
	 * @param __width The image's width.
	 * @param __height The image's height.
	 * @return An array disposed as {@code [startYPos, incrX, incrY]}.
	 * @throws IllegalArgumentException If {@code __width} or
	 * {@code __height} are less than 1, or {@code __transform} is invalid.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	private int[] __getIncrAndStartPos(
		@MagicConstant(valuesFromClass = Sprite.class) int __transform,
		@Range(from = 1, to = Integer.MAX_VALUE) int __width,
		@Range(from = 1, to = Integer.MAX_VALUE) int __height)
		throws IllegalArgumentException
	{
		if (__width < 1 || __height < 1)
			throw new IllegalArgumentException("INVL");
		
		if (__transform < 0 || __transform > Sprite.TRANS_MIRROR_ROT90)
			throw new IllegalArgumentException("INVL");

		boolean isRot180 = (__transform & Sprite.TRANS_MIRROR_ROT180) != 0;
		boolean isMirrorX = (__transform & Sprite.TRANS_MIRROR) != 0;
		int startYPos, incrX, incrY;

		// Is it mirrored vertically?
		if ((__transform & (Sprite.TRANS_MIRROR |
			Sprite.TRANS_MIRROR_ROT180)) != 0)
		{
			incrX = (isRot180 ? -__height : __height);
			startYPos = (isRot180 ? (__width * __height) - __height : 0);
			incrY = (isMirrorX ? -1 : 1);

			if (isMirrorX)
				startYPos = __height - 1;
		}
		else
		{
			incrY = (isRot180 ? -__width : __width);
			startYPos = (isRot180 ? (__width * __height) - __width : 0);
			incrX = (isMirrorX ? -1 : 1);

			if (isMirrorX)
				startYPos += (__width - 1);
		}

		return new int[] {startYPos, incrX, incrY};
	}

	/**
	 * Gets the transformed position of this {@link Sprite}'s top-left corner
	 * based on the given transformation.
	 *
	 * @param __coordX The Sprite's original left coordinate.
	 * @param __coordY The image's original top corner.
	 * @param __transform The transformation to use when calculating the new
	 * coordinate.
	 * @param __isX Indicates whether the X or Y coordinate of the top/left
	 * corner should be returned.
	 * @return The X or Y coordinate that represents the top/left corner of
	 * the transformed Sprite.
	 * @throws IllegalArgumentException If {@code __transform} is invalid.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	private int __getTopLeftCorner(int __coordX, int __coordY,
		@MagicConstant(valuesFromClass = Sprite.class) int __transform,
		boolean __isX)
		throws IllegalArgumentException
	{
		switch (__transform)
		{
			case Sprite.TRANS_NONE:
				return (__isX ? __coordX : __coordY);

			case Sprite.TRANS_MIRROR:
				return (__isX ? this._frameWidth - __coordX - 1 : __coordY);

			case Sprite.TRANS_MIRROR_ROT180:
				return (__isX ? __coordX : this._frameHeight - __coordY - 1);

			case Sprite.TRANS_ROT90:
				return (__isX ? this._frameHeight - __coordY - 1 : __coordX);

			case Sprite.TRANS_ROT180:
				return (__isX ? this._frameWidth - __coordX - 1 :
					this._frameHeight - __coordY - 1);

			case Sprite.TRANS_ROT270:
				return (__isX ? __coordY : this._frameWidth - __coordX - 1);

			case Sprite.TRANS_MIRROR_ROT90:
				return (__isX ? this._frameHeight - __coordY - 1 :
					this._frameWidth - __coordX - 1);

			case Sprite.TRANS_MIRROR_ROT270:
				return (__isX ? __coordY : __coordX);

			default:
				/* {@squirreljme.error EB3k Invalid transform requested.} */
				throw new IllegalArgumentException("EB3k");
		}
	}

	/**
	 * Checks if two rectangles intersect.
	 *
	 * @param __rect1x1 First rect's left coordinate.
	 * @param __rect1y1 First rect's top coordinate.
	 * @param __rect1x2 First rect's right coordinate.
	 * @param __rect1y2 First rect's bottom coordinate.
	 * @param __rect2x1 Second rect's left coordinate.
	 * @param __rect2y1 Second rect's top coordinate.
	 * @param __rect2x2 First rect's right coordinate.
	 * @param __rect2y2 First rect's bottom coordinate.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	private boolean __intersects(int __rect1x1, int __rect1y1, int __rect1x2,
		int __rect1y2, int __rect2x1, int __rect2y1, int __rect2x2,
		int __rect2y2)
	{
		// If one is to the left of the other = no collision
		if (__rect1x2 < __rect2x1 || __rect1x1 > __rect2x2)
			return false;

		// If one is above the other = also no collision
		// If none of the conditions were met, the two rects do intersect
		return __rect1y2 >= __rect2y1 && __rect1y1 <= __rect2y2;

	}

	/**
	 * Checks per-pixel collision between two distinct image areas by checking
	 * if they have any overlapping opaque pixels. Not fully implemented yet.
	 *
	 * @param __x1 First image's X offset to start reading data from.
	 * @param __y1 First image's Y offset to start reading data from.
	 * @param __x2 Second image's X offset to start reading data from.
	 * @param __y2 Second image's Y offset to start reading data from.
	 * @param __image1 First image to check collision with.
	 * @param __transform1 First image's transformation.
	 * @param __image2 Second image to check collision with.
	 * @param __transform2 Second image's transformation.
	 * @param __width Width of the data to read from each image.
	 * @param __height Height of the data to read from each image.
	 * @throws NullPointerException If {@code __image1} or {@code __image2} are
	 * {@code null}.
	 * @throws IllegalArgumentException If {@code __width} or {@code __height}
	 * are less than 1.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	private boolean __pixelCollision(int __x1, int __y1, int __x2,
		int __y2, @NotNull Image __image1,
		@MagicConstant(valuesFromClass = Sprite.class) int __transform1,
		@NotNull Image __image2,
		@MagicConstant(valuesFromClass = Sprite.class) int __transform2,
		@Range(from = 0, to = Integer.MAX_VALUE) int __width,
		@Range(from = 0, to = Integer.MAX_VALUE) int __height)
		throws NullPointerException, IllegalArgumentException
	{
		if (__image1 == null || __image2 == null)
			throw new NullPointerException("NARG");

		if (__width < 0 || __height < 0)
			throw new IllegalArgumentException("INVL");

		// This one will throw IllegalArgumentException depending if any of
		// __transform, __width and __height are invalid.
		int[] data1Pos = this.__getIncrAndStartPos(__transform1, __width,
			__height);

		int[] data2Pos = this.__getIncrAndStartPos(__transform2, __width,
			__height);

		// TODO here for the time being.
		// We could use getRGB for both images and iterate on them here, but
		// that would be terribly slow. Getting the pencil of each image and
		// doing a similar logic with native code would be preferable.
		throw Debugging.todo("Sprite per-pixel collision");
	}

	/**
	 * Extracts all animation frames from the given {@link Image} based on each
	 * frame's width and height.
	 *
	 * @param __image The {@link Image} to check collision against.
	 * @param __frameWidth The width of each animation frame.
	 * @param __frameHeight The height of each animation frame.
	 * @param __keepFrameSeq Whether the current frame sequence should be kept.
	 * @throws NullPointerException If {@code __image} is {@code null}.
	 * @throws IllegalArgumentException If either {@code __frameWidth} or
	 * {@code __frameHeight} are less than 1.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	private void __prepareFrames(@NotNull Image __image,
		@Range(from = 1, to = Integer.MAX_VALUE) int __frameWidth,
		@Range(from = 1, to = Integer.MAX_VALUE) int __frameHeight,
		boolean __keepFrameSeq)
		throws NullPointerException, IllegalArgumentException
	{
		if (__image == null)
			throw new NullPointerException("NARG");

		if (__frameWidth < 1 || __frameHeight < 1)
			throw new IllegalArgumentException("INVL");

		if (!__keepFrameSeq)
			this._frameIndex = 0;

		int imageWidth = __image.getWidth();
		int imageHeight = __image.getHeight();

		this._image = __image;

		this._frameWidth = __frameWidth;
		this._frameHeight = __frameHeight;

		this._numFrames = (imageWidth / __frameWidth) *
			(imageHeight / __frameHeight);

		this._frameCoordX = new int[this._numFrames];
		this._frameCoordY = new int[this._numFrames];

		if (!this._isCustomSequence)
			this._frameSequence = new int[this._numFrames];

		int currentFrame = 0;

		for (int y = 0; y < imageHeight; y += __frameHeight)
		{
			for (int x = 0; x < imageWidth; x += __frameWidth)
			{
				this._frameCoordX[currentFrame] = x;
				this._frameCoordY[currentFrame] = y;

				if (!this._isCustomSequence)
					this._frameSequence[currentFrame] = currentFrame;

				currentFrame++;
			}
		}
	}
}

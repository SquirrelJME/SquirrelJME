// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a tileset comprised of multiple {@link Layer} objects. May be
 * used for animations and backgrounds.
 *
 * @since 2026/08/09
 */
@Api
public class TiledLayer
	extends Layer
{
	/** Image that contains all tile data. */
	private Image _image;

	/** The amount of tile rows in this TiledLayer. */
	private final int _rows;

	/** The amount of tile rows in this TiledLayer. */
	private final int _cols;

	/** The width of each tile in this TiledLayer. */
	private int _tileWidth;

	/** The height of each tile in this TiledLayer. */
	private int _tileHeight;

	/** The total number of tiles in this TiledLayer. */
	private int _numTiles;

	/** Array containing each tile's [X,Y] coordinates in regards to _image. */
	private Map<Integer, int[]> _tileCoords;

	/** List of animated tile indices. */
	private ArrayList<Integer> _animatedTiles;

	/** Matrix containing the tile index for any given [row, column]. */
	private final int[][] _tiles;

	/**
	 * Initializes a {@link TiledLayer} with the given amount of columns and
	 * rows, the tiles' width and height, as well as an {@link Image}
	 * containing the set of tiles to be drawn.
	 *
	 * @param __cols The amount of columns this {@link TiledLayer} will have.
	 * @param __rows The amount of rows this {@link TiledLayer} will have.
	 * @param __baseImage Image containing tile data.
	 * @param __tileWidth The width of each tile in {@code __baseImage}.
	 * @param __tileHeight The height of each tile in {@code __baseImage}.
	 * @throws NullPointerException If {@code __baseImage} is {@code null}.
	 * @throws IllegalArgumentException If the {@code __baseImage}'s size is
	 * not a multiple of {@code __tileWidth} and {@code __tileHeight}.
	 * @throws IllegalArgumentException If either {@code __rows} or
	 * {@code __cols} is less than 1.
	 * @throws IllegalArgumentException If either {@code __tileWidth} or
	 * {@code __tileHeight} is less than 1.
	 * @since 2026/08/09
	 */
	@Api
	public TiledLayer(@Range(from = 1, to = Integer.MAX_VALUE) int __cols,
		@Range(from = 1, to = Integer.MAX_VALUE) int __rows,
		@NotNull Image __baseImage,
		@Range(from = 1, to = Integer.MAX_VALUE) int __tileWidth,
		@Range(from = 1, to = Integer.MAX_VALUE) int __tileHeight)
		throws NullPointerException, IllegalArgumentException
	{
		if (__baseImage == null)
			throw new NullPointerException("NARG");

		if (__cols < 1 || __rows < 1 ||
			((__baseImage.getWidth() % __tileWidth) != 0) ||
			((__baseImage.getHeight() % __tileHeight) != 0) ||
			__tileWidth < 1 || __tileHeight < 1)
			throw new IllegalArgumentException("INVL");

		this._tileWidth = __tileWidth;
		this._tileHeight = __tileHeight;
		this._cols = __cols;
		this._rows = __rows;

		this._tiles = new int[__rows][__cols];

		// Layer bounds for the entire tile set
		this._x = 0;
		this._y = 0;
		this._width = __tileWidth * __cols;
		this._height = __tileHeight * __rows;

		this._visible = true;

		// Now we can create the Tile Set
		this.__createTiles(__baseImage, (__baseImage.getWidth() / __tileWidth)
			* (__baseImage.getHeight() / __tileHeight) + 1, __tileWidth,
			__tileHeight);
	}

	/**
	 * Creates a new animated tile from a given {@code __staticTileIndex}
	 * depicting an existing static tile.
	 *
	 * Note that animated tiles are always negative and consecutive, beginning
	 * from -1 and moving further away from 0.
	 *
	 * @param __staticTileIndex Index of the static tile that will be used to
	 * create a new animated tile.
	 * @return The new animated tile's index from the {@code _animatedTiles}
	 * list.
	 * @throws IndexOutOfBoundsException If the {@code __staticTileIndex} is
	 * out of range.
	 * @since 2026/08/09
	 */
	@Api
	public int createAnimatedTile(
		@Range(from = 0, to = Integer.MAX_VALUE) int __staticTileIndex)
		throws IndexOutOfBoundsException
	{
		if (__staticTileIndex < 0 || __staticTileIndex >= this._numTiles)
			throw new IndexOutOfBoundsException("IOOB");

		if (this._animatedTiles == null)
		{
			this._animatedTiles = new ArrayList<Integer>();
			// Animated tiles start from index -1. Pad the first array position
			// so we don't have to constantly add + or -1 when managing it.
			this._animatedTiles.add(0);
		}

		this._animatedTiles.add(__staticTileIndex);

		return -(this._animatedTiles.size()-1);
	}

	/**
	 * Fills an entire region of the tile grid with the specified
	 * {@code __tileIndex}, beginning from the grid's {@code [__row, __col]}
	 * all the way to the tile grid position given by
	 * {@code [__row + __numRows, __col + __numCols]}.
	 *
	 * @param __col The starting tile grid's column position to fill.
	 * @param __row The starting tile grid's row position to fill.
	 * @param __numCols The amount of columns to fill.
	 * @param __numRows The amount of rows to fill.
	 * @param __tileIndex Tile index to fill the tile grid's positions with.
	 * @throws IllegalArgumentException If either {@code __numRows} or
	 * {@code __numCols} are less than 1.
	 * @throws IndexOutOfBoundsException If either {@code __row},
	 * {@code __col}, {@code __row + __numRows} or {@code __col + __numCols}
	 * are out of range.
	 * @throws IndexOutOfBoundsException If {@code __tileIndex} is out of
	 * range.
	 * @since 2026/08/09
	 */
	@Api
	public void fillCells(
		@Range(from = 0, to = Integer.MAX_VALUE) int __col,
		@Range(from = 0, to = Integer.MAX_VALUE) int __row,
		@Range(from = 1, to = Integer.MAX_VALUE) int __numCols,
		@Range(from = 1, to = Integer.MAX_VALUE) int __numRows,
		@Range(from = 0, to = Integer.MAX_VALUE) int __tileIndex)
		throws IllegalArgumentException, IndexOutOfBoundsException
	{
		if (__numCols < 1 || __numRows < 1)
			throw new IllegalArgumentException("INVL");

		if (__col < 0 || __col >= this._cols || __row < 0 ||
			__row >= this._rows || __col + __numCols > this._cols ||
			__row + __numRows > this._rows)
			throw new IndexOutOfBoundsException("IOOB");

		if (__tileIndex > 0)
			if (__tileIndex >= this._numTiles)
				throw new IndexOutOfBoundsException("IOOB");
		else if (__tileIndex < 0)
			if (this._animatedTiles == null ||
				-__tileIndex >= this._animatedTiles.size())
				throw new IndexOutOfBoundsException("IOOB");

		for (int curRow = __row; curRow < __row + __numRows; curRow++)
			ObjectShelf.arrayFill(this._tiles[curRow], __col, __col + __numCols,
				__tileIndex);
	}

	/**
	 * Gets an animated tile given by {@code __animatedTileIndex} from the
	 * animated tile list.
	 *
	 * @param __animatedTileIndex Index of the animated tile to retrieve, this
	 * will be a negative value.
	 * @throws IndexOutOfBoundsException If {@code __animatedTileIndex} is out
	 * of range.
	 * @since 2026/08/09
	 */
	@Api
	public int getAnimatedTile(
		@Range(from = Integer.MIN_VALUE, to = 0) int __animatedTileIndex)
		throws IndexOutOfBoundsException
	{
		// Normalize the animated tile index, as all animated tiles are
		// always negative values
		__animatedTileIndex = -__animatedTileIndex;
		
		ArrayList<Integer> animatedTiles = this._animatedTiles;
		if (animatedTiles == null || __animatedTileIndex < 0 ||
			__animatedTileIndex >= animatedTiles.size())
			throw new IndexOutOfBoundsException("EB3e");

		return animatedTiles.get(__animatedTileIndex);
	}

	/**
	 * Gets the current tile index of a cell given by {@code (__row, __col)}.
	 *
	 * @param __col The tile grid's column position to get contents from.
	 * @param __row The tile grid's row position to get contents from.
	 * @return The tile index at the grid's given {@code (__row, __col)}.
	 * @throws IndexOutOfBoundsException If either {@code __row} or
	 * {@code __col} are out of range.
	 * @since 2026/08/09
	 */
	@Api
	public int getCell(
		@Range(from = 0, to = Integer.MAX_VALUE) int __col,
		@Range(from = 0, to = Integer.MAX_VALUE) int __row)
		throws IndexOutOfBoundsException
	{
		if (__col < 0 || __col >= this._cols || __row < 0 ||
			__row >= this._rows)
			throw new IndexOutOfBoundsException("IOOB");

		return this._tiles[__row][__col];
	}

	/**
	 * Gets the height of each tile in this {@link TiledLayer}.
	 *
	 * @return The height of each tile cell.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 1, to = Integer.MAX_VALUE)
	public final int getCellHeight()
	{
		return this._tileHeight;
	}

	/**
	 * Gets the width of each tile in this {@link TiledLayer}.
	 *
	 * @return The width of each tile cell.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 1, to = Integer.MAX_VALUE)
	public final int getCellWidth()
	{
		return this._tileWidth;
	}

	/**
	 * Gets the amount of tile columns that this {@link TiledLayer} has.
	 *
	 * @return The amount of tile columns in this {@link TiledLayer}.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 1, to = Integer.MAX_VALUE)
	public final int getColumns()
	{
		return this._cols;
	}

	/**
	 * Gets the amount of tile rows that this {@link TiledLayer} has.
	 *
	 * @return The amount of tile rows in this {@link TiledLayer}.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 1, to = Integer.MAX_VALUE)
	public final int getRows()
	{
		return this._rows;
	}

	/**
	 * Draws this {@link TiledLayer} instance.
	 *
	 * The drawing operation is subject to the Graphics object's clip region
	 * and translation.
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

		int clipX = __g.getClipX();
		int clipY = __g.getClipY();
		int clipWidth = __g.getClipWidth();
		int clipHeight = __g.getClipHeight();

		int firstCol = Math.max(0, (clipX - this._x) /
			this._tileWidth);

		int lastCol = Math.min(this._cols,
			(clipX + clipWidth - this._x + this._tileWidth - 1) /
			this._tileWidth);

		int firstRow = Math.max(0,
			(clipY - this._y) / this._tileHeight);

		int lastRow = Math.min(this._rows,
			(clipY + clipHeight - this._y + this._tileHeight - 1) /
			this._tileHeight);

		for (int row = firstRow; row < lastRow; row++)
		{
			int tileY = this._y + (row * this._tileHeight);
			for (int col = firstCol; col < lastCol; col++)
			{
				int curTile = this._tiles[row][col];

				// Skip transparent tiles
				if (curTile == 0)
					continue;

				// Negative indices mean animated tiles
				if (curTile < 0)
					curTile = this.getAnimatedTile(curTile);

				int tileX = this._x + (col * this._tileWidth);

				__g.drawRegion(this._image, this._tileCoords.get(curTile)[0],
					this._tileCoords.get(curTile)[1], this._tileWidth,
					this._tileHeight, Sprite.TRANS_NONE, tileX, tileY,
					Graphics.TOP | Graphics.LEFT);
			}
		}
	}

	/**
	 * Binds a given {@code __animatedTileIndex} with a given
	 * {@code __staticTileIndex}.
	 *
	 * @param __animatedTileIndex Index of the animated tile that will be bound
	 * to {@code __staticTileIndex}.
	 * @param __staticTileIndex Index of the static tile that will be bound to
	 * {@code __animatedTileIndex}.
	 * @throws IndexOutOfBoundsException If the {@code __staticTileIndex} or
	 * {@code __animatedTileIndex} are out of range.
	 * @since 2026/08/09
	 */
	@Api
	public void setAnimatedTile(
		@Range(from = 0, to = Integer.MAX_VALUE) int __animatedTileIndex,
		@Range(from = 0, to = Integer.MAX_VALUE) int __staticTileIndex)
		throws IndexOutOfBoundsException
	{
		if (__staticTileIndex < 0 || __staticTileIndex >= this._numTiles)
			throw new IndexOutOfBoundsException("IOOB");

		ArrayList<Integer> animatedTiles = this._animatedTiles;

		if (animatedTiles == null || -__animatedTileIndex <= 0 ||
			-__animatedTileIndex >= animatedTiles.size())
			throw new IndexOutOfBoundsException("IOOB");

		animatedTiles.set(-__animatedTileIndex, __staticTileIndex);
	}

	/**
	 * Sets the contents of a cell given by {@code (__row, __col)} to the given
	 * {@code __tileIndex}.
	 *
	 * @param __col The tile grid's column position to set.
	 * @param __row The tile grid's row position to set.
	 * @param __tileIndex Tile index to set the tile grid's position to.
	 * @throws IndexOutOfBoundsException If either {@code __row} or
	 * {@code __col} are out of range.
	 * @throws IndexOutOfBoundsException If {@code __tileIndex} is out of
	 * range.
	 * @since 2026/08/09
	 */
	@Api
	public void setCell(
		@Range(from = 0, to = Integer.MAX_VALUE) int __col,
		@Range(from = 0, to = Integer.MAX_VALUE) int __row,
		@Range(from = 0, to = Integer.MAX_VALUE) int __tileIndex)
		throws IndexOutOfBoundsException
	{
		if (__col < 0 || __col >= this._cols || __row < 0 || __row >=
			this._rows)
			throw new IndexOutOfBoundsException("IOOB");

		if (__tileIndex > 0)
		{
			if (__tileIndex >= this._numTiles)
				throw new IndexOutOfBoundsException("IOOB");
		}
		else if (__tileIndex < 0)
		{
			if (this._animatedTiles == null ||
				-__tileIndex >= this._animatedTiles.size())
				throw new IndexOutOfBoundsException("IOOB");
		}

		this._tiles[__row][__col] = __tileIndex;
	}

	/**
	 * Changes this {@link TiledLayer}'s current static tile set for a new set.
	 *
	 * Note that if the amount of tiles from the received image and tile sizes
	 * is lesser than the current amount of tiles this instance had, all
	 * previously set animation tiles will be cleared and the tile indices
	 * will be reset.
	 *
	 * @param __baseImage Image containing new tile data.
	 * @param __tileWidth The new static tile set's width.
	 * @param __tileHeight The new static tile set's height.
	 * @throws NullPointerException If {@code __baseImage} is {@code null}.
	 * @throws IllegalArgumentException If the {@code __baseImage}'s size is
	 * not a multiple of {@code __tileWidth} and {@code __tileHeight}.
	 * @throws IllegalArgumentException If either {@code __tileWidth} or
	 * {@code __tileHeight} is less than 1.
	 * @since 2026/08/09
	 */
	@Api
	public void setStaticTileSet(@NotNull Image __baseImage,
		@Range(from = 1, to = Integer.MAX_VALUE) int __tileWidth,
		@Range(from = 1, to = Integer.MAX_VALUE) int __tileHeight)
		throws NullPointerException, IllegalArgumentException
	{
		if (__baseImage == null)
			throw new NullPointerException("NARG");

		if (((__baseImage.getWidth() % __tileWidth) != 0) ||
			((__baseImage.getHeight() % __tileHeight) != 0) ||
			__tileWidth < 1 || __tileHeight < 1)
			throw new IllegalArgumentException("INVL");

		this.__setWidth(this._cols * __tileWidth);
		this.__setHeight(this._rows * __tileHeight);

		int numTiles = (__baseImage.getWidth() / __tileWidth) *
			(__baseImage.getHeight() / __tileHeight);

		// If the current amount of tiles is equal or larger than the one we
		// currently have, we can keep the current animated tiles and tile
		// indices
		if (numTiles >= (this._numTiles - 1))
		{
			this.__createTiles(__baseImage, numTiles + 1, __tileWidth,
				__tileHeight);
		}
		else
		{
			// We can't keep the current indices, so remove any animated
			// tiles and zero out the tile matrix' tile indices before creating
			// the new set
			this._animatedTiles = null;

			for (int row = 0; row < this._tiles.length; row++)
				Arrays.fill(this._tiles[row], 0);

			this.__createTiles(__baseImage, numTiles + 1, __tileWidth,
				__tileHeight);
		}
	}

	/**
	 * Creates a new static tile set from the received image and tile
	 * arguments.
	 *
	 * @param __baseImage Image containing new tile data.
	 * @param __numTiles The amount of tiles to create.
	 * @param __tileWidth The new static tile set's width.
	 * @param __tileHeight The new static tile set's height.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	private void __createTiles(@NotNull Image __baseImage,
		@Range(from = 1, to = Integer.MAX_VALUE) int __numTiles,
		@Range(from = 1, to = Integer.MAX_VALUE) int __tileWidth,
		@Range(from = 1, to = Integer.MAX_VALUE) int __tileHeight)
	{
		if (__numTiles < 1)
			throw new IllegalArgumentException("EB3c");

		if (__tileWidth < 1 || __tileHeight < 1)
			throw new IllegalArgumentException("EB0b");

		this._tileWidth = __tileWidth;
		this._tileHeight = __tileHeight;

		this._image = __baseImage;
		this._numTiles = __numTiles;
		this._tileCoords = new HashMap<Integer, int[]>(__numTiles);

		// Add tiles to the tile matrix (skipping the transparent tile index 0)
		int currentTile = 1;

		for (int y = 0; y < __baseImage.getHeight(); y += __tileHeight)
			for (int x = 0; x < __baseImage.getWidth(); x += __tileWidth)
				this._tileCoords.put(currentTile++, new int[] {x, y});
	}

	/**
	 * Gets the internal image file that contains all tile data. Only really
	 * useful for {@link Sprite}'s per-pixel collision detection.
	 *
	 * @return The image that contains the tile data.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	@NotNull
	Image __getImage()
	{
		return this._image;
	}

	/**
	 * Gets an array containing the {@code [x, y]} coordinates of the requested
	 * tile index.
	 *
	 * @param __tileIndex The tile from which to return its translation.
	 * @return The array containing the tile's X and Y translation.
	 * @since 2026/08/09
	 */
	@KeepWhenCompacting
	@NotNull
	int[] __getTileCoord(int __tileIndex)
	{
		return this._tileCoords.get(__tileIndex);
	}
}

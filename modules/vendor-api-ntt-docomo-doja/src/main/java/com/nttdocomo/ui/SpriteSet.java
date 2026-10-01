// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Holds multiple Sprites' data for drawing and collision detection.
 *
 * @since 2026/10/01
 */
@Api
public class SpriteSet
{
	/** Collision flags for each sprite within this set. */
	private final int[] _collisionFlags;

	/** Sprites contained within this set. May contain null entries. */
	private final Sprite[] _sprites;

	/**
	 * Creates a new SpriteSet from an array of sprites, which are then
	 * displayed according to their priority, with the first array indices
	 * being the background, and latter ones being the foreground. The sprites
	 * are held by reference, and not copied. So any later changes to the
	 * sprite array should reflect on this set as well.
	 *
	 * Elements within this set that are {@code null} are not rendered nor
	 * considered for collision detection, and by default, all collision flags
	 * start as 0.
	 *
	 * @param __sprites The array of sprites to be referenced by this set.
	 * @throws NullPointerException if the sprite array is {@code null}.
	 * @throws IllegalArgumentException If the size of the sprite array is
	 * less than 1 or larger than 32.
	 * @since 2026/10/01
	 */
	@Api
	public SpriteSet(@NotNull Sprite[] __sprites)
		throws IllegalArgumentException, NullPointerException
	{
		if (__sprites == null)
			throw new NullPointerException("NARG");

		/* {@squirreljme.error AH2v Sets must have between 1 and 32
			sprites.} */
		if (__sprites.length < 1 || __sprites.length > 32)
			throw new IllegalArgumentException("AH2v");

		this._sprites = __sprites;
		this._collisionFlags = new int[__sprites.length];
	}

	/**
	 * Returns the sprites currently contained within this set.
	 *
	 * @return All the sprites in this set.
	 * @since 2026/10/01
	 */
	@Api
	@NotNull
	public Sprite[] getSprites()
	{
		return this._sprites;
	}

	/**
	 * Returns the number of sprites in this set.
	 *
	 * @return The amount of sprites in this set.
	 * @since 2026/10/01
	 */
	@Api
	@Range(from = 1, to = Integer.MAX_VALUE)
	public int getCount()
	{
		return this._sprites.length;
	}

	/**
	 * Returns the sprite currently in the specified index of this set.
	 *
	 * @param __index The index of the sprite to retrieve.
	 * @return The sprite present on the requested index.
	 * @throws ArrayIndexOutOfBoundsException If the index is negative; or
	 * outside the valid range for sprites within this set.
	 * @since 2026/10/01
	 */
	@Api
	@Nullable
	public Sprite getSprite(int __index)
		throws ArrayIndexOutOfBoundsException
	{
		Sprite[] sprites = this._sprites;

		if (__index < 0 || __index >= sprites.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		return sprites[__index];
	}

	/**
	 * Returns the collision flag of the requested sprite index
	 *
	 * @param __index The index of the sprite to retrieve the collision flag.
	 * @return The collision flag of the specified sprite.
	 * @throws ArrayIndexOutOfBoundsException If the index is negative; or
	 * outside the valid range for sprites within this set.
	 * @since 2026/10/01
	 */
	@Api
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int getCollisionFlag(int __index)
	{
		Sprite[] sprites = this._sprites;

		if (__index < 0 || __index >= sprites.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		return this._collisionFlags[__index];
	}

	/**
	 * Checks if two specific sprites are colliding by checking their detection
	 * flags.
	 *
	 * @param __index1 The index of the first sprite to check.
	 * @param __index2 The index of the second sprite to check.
	 * @return If {@code getCollisionFlag(index1) & (1 << index2) != 0}.
	 * @throws ArrayIndexOutOfBoundsException If the index is negative; or
	 * outside the valid range for sprites within this set.
	 * @since 2026/10/01
	 */
	@Api
	public boolean isCollision(int __index1, int __index2)
	{
		Sprite[] sprites = this._sprites;

		if (__index1 < 0 || __index1 >= sprites.length || __index2 < 0 ||
			__index2 >= sprites.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		return (this.getCollisionFlag(__index1) & (1 << __index2)) != 0;
	}

	/**
	 * Perform collision detection for the sprite contained in the requested
	 * index. Collision is checked against all other sprites, and the result
	 * is saved within its collision flag. Sprites that are {@code null}, are
	 * not visible, or contain images that were disposed are ignored.
	 *
	 * @param __index The index of the sprite to perform collision detection.
	 * @throws ArrayIndexOutOfBoundsException If the index is negative; or
	 * outside the valid range of sprites within this set.
	 * @since 2026/10/01
	 */
	@Api
	public void setCollisionOf(int __index)
		throws ArrayIndexOutOfBoundsException
	{
		Sprite[] sprites = this._sprites;
		int[] collisionFlags = this._collisionFlags;
		
		if (__index < 0 || __index >= sprites.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		Sprite sprite1 = sprites[__index];

		if (sprite1 == null || !sprite1.isVisible())
		{
			collisionFlags[__index] = 0;
			return;
		}

		int collisionFlag = 0;

		for (int i = 0; i < sprites.length; i++)
		{
			if (i != __index)
			{
				Sprite sprite2 = sprites[i];
				if (sprite2 != null && sprite2.isVisible())
				{
					if (this.__isOverlapping(sprite1, sprite2))
						collisionFlag |= (1 << i);
				}
			}
		}

		collisionFlags[__index] = collisionFlag;
	}

	/**
	 * Performs collision detection on all the sprites within this set. This
	 * works the same as calling {@link SpriteSet#setCollisionOf(int)} for
	 * all available sprites.
	 *
	 * @since 2026/10/01
	 */
	@Api
	public void setCollisionAll()
	{
		Sprite[] sprites = this._sprites;

		for (int i = 0; i < sprites.length; i++)
			this.setCollisionOf(i);
	}

	/**
	 * Checks if two sprites are colliding by checking if their bounding boxes
	 * overlap.
	 *
	 * @param __sprite1 The first sprite to check.
	 * @param __sprite2 The second sprite to check.
	 * @throws NullPointerException On null arguments.
	 * @return If the sprite bounding boxes do overlap.
	 * @since 2026/10/01
	 */
	private boolean __isOverlapping(@NotNull Sprite __sprite1,
		@NotNull Sprite __sprite2)
		throws NullPointerException
	{
		if (__sprite1 == null || __sprite2 == null)
			throw new NullPointerException("NARG");

		try
		{
			int x1 = __sprite1.getX();
			int y1 = __sprite1.getY();
			int width1 = __sprite1.getWidth();
			int height1 = __sprite1.getHeight();

			int x2 = __sprite2.getX();
			int y2 = __sprite2.getY();
			int width2 = __sprite2.getWidth();
			int height2 = __sprite2.getHeight();

			return (x1 < x2 + width2 && x1 + width1 > x2 &&
				y1 < y2 + height2 && y1 + height1 > y2);
		}
		catch (UIException e)
		{
			// UIException here means that at least one of the sprites' images
			// was disposed of, and in this case, we ignore it and return no
			// overlap since collisions must be ignored for these.
			return false;
		}
	}
}

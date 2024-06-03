// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

/**
 * The vector angle of path navigation.
 *
 * @since 2024/06/02
 */
public enum VectorAngle
{
	/** Left. */
	LEFT,
	
	/** Down. */
	DOWN,
	
	/** Right. */
	RIGHT,
	
	/** Up. */
	UP
	
	/* End. */
	;
	
	/**
	 * Moves the angle.
	 *
	 * @param __code The input code.
	 * @return The resultant angle.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/02
	 */
	public VectorAngle moveAngle(ChainCode __code)
		throws NullPointerException
	{
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Straight stays the same always
		if (__code == ChainCode.STRAIGHT)
			return this;
		
		switch (this)
		{
			case LEFT:
				if (__code == ChainCode.LEFT)
					return VectorAngle.DOWN;
				return VectorAngle.UP;
				
			case DOWN:
				if (__code == ChainCode.LEFT)
					return VectorAngle.RIGHT;
				return VectorAngle.LEFT;
				
			case RIGHT:
				if (__code == ChainCode.LEFT)
					return VectorAngle.UP;
				return VectorAngle.DOWN;
				
			case UP:
				if (__code == ChainCode.LEFT)
					return VectorAngle.LEFT;
				return VectorAngle.RIGHT;
		}
		
		throw new IllegalArgumentException(String.format(
			"Unknown %s.moveAngle(%s)",
			this, __code));
	}
	
	/**
	 * Moves the X coordinate.
	 *
	 * @param __x The input coordinate.
	 * @return The new coordinate.
	 * @since 2024/06/02
	 */
	public int moveX(int __x)
	{
		if (this == VectorAngle.LEFT)
			return __x - 1;
		else if (this == VectorAngle.RIGHT)
			return __x + 1;
		return __x;
	}
	
	/**
	 * Moves the Y coordinate.
	 *
	 * @param __y The input coordinate.
	 * @return The new coordinate.
	 * @since 2024/06/02
	 */
	public int moveY(int __y)
	{
		if (this == VectorAngle.UP)
			return __y - 1;
		else if (this == VectorAngle.DOWN)
			return __y + 1;
		return __y;
	}
}

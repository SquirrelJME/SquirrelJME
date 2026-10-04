// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.opt.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import com.nttdocomo.ui.Sprite;
import org.jetbrains.annotations.NotNull;

/**
 * Use {@link com.nttdocomo.ui.SpriteSet} instead.
 *
 * @deprecated Use {@link com.nttdocomo.ui.SpriteSet} instead.
 * @since 2026/10/01
 */
@Api
@ApiDefinedDeprecated
public class SpriteSet
	extends com.nttdocomo.ui.SpriteSet
{
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
	@ApiDefinedDeprecated
	public SpriteSet(@NotNull Sprite[] __sprites)
		throws IllegalArgumentException, NullPointerException
	{
		super(__sprites);
	}
}

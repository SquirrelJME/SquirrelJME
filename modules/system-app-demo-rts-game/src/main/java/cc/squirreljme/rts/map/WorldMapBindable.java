// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.map;

import java.lang.ref.Reference;

/**
 * This is used by anything that can be bound with a {@link WorldMap}.
 *
 * @param <B> The bindable type.
 * @since 2026/06/11
 */
public interface WorldMapBindable<B extends WorldMapBindable<B>>
{
	/**
	 * Binds the world map to this instance.
	 *
	 * @param __map The map to bind.
	 * @throws IllegalStateException If this has already been bound to a world
	 * map.
	 * @throws NullPointerException On null arguments
	 * @since 2026/06/11
	 */
	B bind(Reference<WorldMap> __map)
		throws IllegalStateException, NullPointerException;
}

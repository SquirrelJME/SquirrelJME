// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

/**
 * DESCRIBE THIS.
 *
 * @since 2019/07/01
 */
public enum MapSize
{
	/** Compact. */
	RIDICULOUSLY_TINY(10, 10),
	
	/** Tiny map size. */
	TINY(20, 20),
	
	/** Small. */
	SMALL(25, 25),
	
	/** Medium. */
	MEDIUM(30, 30),
	
	/** Large. */
	LARGE(40, 40),
	
	/** Ridiculous. */
	RIDICULOUSLY_LARGE(80, 80),
	
	/** End. */
	;
	
	/** The default map size to use. */
	public static final MapSize DEFAULT = MapSize.TINY;
	
	/** The map width. */
	public final int width;
	
	/** The map height. */
	public final int height;
	
	/**
	 * Initializes the map size.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2019/07/01
	 */
	MapSize(int __w, int __h)
	{
		this.width = __w;
		this.height = __h;
	}
}


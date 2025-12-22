// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.lis.realm;

/**
 * Represents a type of block.
 * 
 * Be sure to add new block types at the end, otherwise saves will break!
 *
 * @since 2025/12/21
 */
public enum BlockType
{
	/** Nothingness, vacuum, liquid, or air (depending on location/doors). */
	NOTHING()
	{
		
	},
	
	/** Dirty dirt. */
	DIRT()
	{
		
	},
	
	/** Hard stone. */
	STONE()
	{
		
	},
	
	/** Smooth sand. */
	SAND()
	{
		
	},
	
	/** Soft snow. */
	SNOW()
	{
		
	},
	
	/** Ice. */
	ICE()
	{
		
	},
	
	/** Gravel. */
	GRAVEL()
	{
		
	},
	
	/** Mantle, very core of planets, indestructible. */
	MANTLE()
	{
		
	},
	
	/** Asteroid material found in space. */
	ASTEROID()
	{
		
	},
	
	/** Raw wood, from trees. */
	RAW_WOOD()
	{
		
	},
	
	/** Leaves. */
	LEAVES()
	{
		
	},
	
	/** Starship hull. */
	SHIP_HULL()
	{
		
	},
	
	/** Starship interior. */
	SHIP_INTERIOR()
	{
		
	},
	
	/** Glass Window. */
	GLASS_WINDOW()
	{
		
	},
	
	/* End. */
	;
}

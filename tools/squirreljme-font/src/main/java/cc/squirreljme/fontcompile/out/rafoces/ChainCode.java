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
 * RaFoCES Vertex Chain Code, effectively this is a relative direction of
 * movement from a vector.
 *
 * @since 2024/05/26
 */
public enum ChainCode
{
	/** Turn left (Adjacent to one pixel). */
	LEFT,
	
	/** Straight (Adjacent to two pixels). */
	STRAIGHT,
	
	/** Turn right (Adjacent to three pixels). */
	RIGHT,
	
	/* End. */
	;
}

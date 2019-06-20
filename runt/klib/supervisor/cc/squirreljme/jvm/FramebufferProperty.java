// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This is used to get the property of the framebuffer.
 *
 * @since 2019/06/20
 */
public interface FramebufferProperty
{
	/** Returns the address of the framebuffer. */
	public static final byte ADDRESS =
		1;
	
	/** Returns the width of the framebuffer. */
	public static final byte WIDTH =
		2;
	
	/** Returns the height of the framebuffer. */
	public static final byte HEIGHT =
		3;
	
	/** Returns the scanline length. */
	public static final byte SCANLEN =
		4;
}


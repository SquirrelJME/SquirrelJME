// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

/**
 * Aliases for Java instructions.
 *
 * @since 2019/01/23
 */
@Deprecated
public interface JavaInstructionAlias
{
	/** Load zeroth local. */
	public static final int XLOAD_0 =
		-1000;
	
	/** Load first local. */
	public static final int XLOAD_1 =
		-999;
	
	/** Load second local. */
	public static final int XLOAD_2 =
		-998;
	
	/** Load third local. */
	public static final int XLOAD_3 =
		-997;
	
	/** Load some local variable. */
	public static final int XLOAD =
		-996;
}


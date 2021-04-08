// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.constants;

/**
 * Constants for the compiler.
 *
 * @since 2021/04/08
 */
public interface CompilerConstants
{
	/** Span of VTables. */
	byte VTABLE_SPAN =
		3;
	
	/** Pool index in the VTable. */
	byte VTABLE_POOL_INDEX =
		0;
	
	/** VTable method address index (lo/a). */
	byte VTABLE_METHOD_A_INDEX =
		1;
	
	/** VTable method address index (hi/b). */
	byte VTABLE_METHOD_B_INDEX =
		2;
}

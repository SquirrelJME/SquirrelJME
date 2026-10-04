// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Standard pipe descriptor identifiers.
 *
 * @since 2020/06/14
 */
public interface StandardPipeType
{
	/** Standard input. */
	byte STDIN =
		0;
	
	/** Standard output. */
	byte STDOUT =
		1;
	
	/** Standard error. */
	byte STDERR =
		2;
	
	/** The number of standard pipes. */
	byte NUM_STANDARD_PIPES =
		3;
}

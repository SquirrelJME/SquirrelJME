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
 * This represents the types of redirects that may occur for a launched task
 * when it is given a pipe.
 *
 * @since 2020/07/02
 */
public interface TaskPipeRedirectType
{
	/** Discard all program output. */
	byte DISCARD =
		0;
	
	/** Buffer the resultant program's output. */
	byte BUFFER =
		1;
	
	/** Send the output to the virtual machine's terminal output. */
	byte TERMINAL =
		2;
	
	/** The number of redirect types. */
	byte NUM_REDIRECT_TYPES =
		3;
}

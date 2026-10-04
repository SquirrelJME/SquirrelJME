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
 * Represents the type of error that occurred on a pipe.
 *
 * @since 2020/07/06
 */
public interface PipeErrorType
{
	/** No error. */
	byte NO_ERROR =
		0;
	
	/** End of file reached. */
	byte END_OF_FILE =
		-1;
	
	/** Read/write error. */
	byte IO_EXCEPTION =
		-2;
}

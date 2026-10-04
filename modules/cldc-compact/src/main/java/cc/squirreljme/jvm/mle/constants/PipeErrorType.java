// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Represents the type of error that occurred on a pipe.
 *
 * @since 2020/07/06
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface PipeErrorType
{
	/** No error. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NO_ERROR =
		0;
	
	/** End of file reached. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte END_OF_FILE =
		-1;
	
	/** Read/write error. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte IO_EXCEPTION =
		-2;
}

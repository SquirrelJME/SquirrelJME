// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public enum StandardCopyOption
	implements CopyOption
{
	@Api
	ATOMIC_MOVE,
	
	@Api
	COPY_ATTRIBUTES,
	
	@Api
	REPLACE_EXISTING,
	
	/** End. */
	;
}


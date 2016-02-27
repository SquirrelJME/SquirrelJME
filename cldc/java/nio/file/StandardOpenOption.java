// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.nio.file;

public enum StandardOpenOption
	implements OpenOption
{
	APPEND(),
	
	CREATE(),
	
	CREATE_NEW(),
	
	DELETE_ON_CLOSE(),
	
	DSYNC(),
	
	READ(),
	
	SPARSE(),
	
	SYNC(),
	
	TRUNCATE_EXISTING(),
	
	WRITE(),
	
	/** End. */
	;
	
	private StandardOpenOption()
	{
		throw new Error("TODO");
	}
}


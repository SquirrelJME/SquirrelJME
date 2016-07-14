// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.nio.file;

import java.io.IOException;

public abstract class FileStore
{
	protected FileStore()
	{
		throw new Error("TODO");
	}
	
	public abstract Object getAttribute(String __a)
		throws IOException;
	
	public abstract long getTotalSpace()
		throws IOException;
	
	public abstract long getUnallocatedSpace()
		throws IOException;
	
	public abstract long getUsableSpace()
		throws IOException;
	
	public abstract boolean isReadOnly();
	
	public abstract String name();
	
	public abstract boolean supportsFileAttributeView(String __a);
	
	public abstract String type();
}



// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import java.io.IOException;

public abstract class FileStore
{
	protected FileStore()
	{
		throw new todo.TODO();
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



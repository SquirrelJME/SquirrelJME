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

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

public abstract class FileSystem
	implements Closeable
{
	protected FileSystem()
	{
		throw new Error("TODO");
	}
	
	public abstract void close()
		throws IOException;
	
	public abstract Iterable<FileStore> getFileStores();
	
	public abstract Path getPath(String __a, String... __b);
	
	public abstract Iterable<Path> getRootDirectories();
	
	public abstract String getSeparator();
	
	public abstract boolean isOpen();
	
	public abstract boolean isReadOnly();
	
	public abstract Set<String> supportedFileAttributeViews();
}


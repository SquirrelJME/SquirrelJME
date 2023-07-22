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
import java.io.IOException;

@Api
public interface Path
	extends Comparable<Path>, Iterable<Path>
{
	
	@Api
	boolean endsWith(Path __a);
	
	@Api
	boolean endsWith(String __a);
	
	@Api
	@Override
	boolean equals(Object __a);
	
	@Api
	Path getFileName();
	
	@Api
	FileSystem getFileSystem();
	
	@Api
	Path getName(int __a);
	
	@Api
	int getNameCount();
	
	@Api
	Path getParent();
	
	@Api
	Path getRoot();
	
	@Api
	boolean isAbsolute();
	
	@Api
	Path normalize();
	
	@Api
	Path relativize(Path __a);
	
	@Api
	Path resolve(Path __a);
	
	@Api
	Path resolve(String __a);
	
	@Api
	Path resolveSibling(Path __a);
	
	@Api
	Path resolveSibling(String __a);
	
	@Api
	boolean startsWith(Path __a);
	
	@Api
	boolean startsWith(String __a);
	
	@Api
	Path subpath(int __a, int __b);
	
	@Api
	Path toAbsolutePath();
	
	@Api
	Path toRealPath(LinkOption... __a)
		throws IOException;
}


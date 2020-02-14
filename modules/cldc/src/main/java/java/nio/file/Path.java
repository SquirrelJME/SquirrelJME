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
import java.util.Iterator;

public interface Path
	extends Comparable<Path>, Iterable<Path>
{
	public abstract int compareTo(Path __a);
	
	public abstract boolean endsWith(Path __a);
	
	public abstract boolean endsWith(String __a);
	
	@Override
	public abstract boolean equals(Object __a);
	
	public abstract Path getFileName();
	
	public abstract FileSystem getFileSystem();
	
	public abstract Path getName(int __a);
	
	public abstract int getNameCount();
	
	public abstract Path getParent();
	
	public abstract Path getRoot();
	
	@Override
	public abstract int hashCode();
	
	public abstract boolean isAbsolute();
	
	public abstract Iterator<Path> iterator();
	
	public abstract Path normalize();
	
	public abstract Path relativize(Path __a);
	
	public abstract Path resolve(Path __a);
	
	public abstract Path resolve(String __a);
	
	public abstract Path resolveSibling(Path __a);
	
	public abstract Path resolveSibling(String __a);
	
	public abstract boolean startsWith(Path __a);
	
	public abstract boolean startsWith(String __a);
	
	public abstract Path subpath(int __a, int __b);
	
	public abstract Path toAbsolutePath();
	
	public abstract Path toRealPath(LinkOption... __a)
		throws IOException;
	
	@Override
	public abstract String toString();
}


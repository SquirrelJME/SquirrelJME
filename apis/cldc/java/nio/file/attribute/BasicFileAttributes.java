// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file.attribute;

public interface BasicFileAttributes
{
	public abstract FileTime creationTime();
	
	public abstract boolean isDirectory();
	
	public abstract boolean isOther();
	
	public abstract boolean isRegularFile();
	
	public abstract boolean isSymbolicLink();
	
	public abstract FileTime lastAccessTime();
	
	public abstract FileTime lastModifiedTime();
	
	public abstract long size();
}



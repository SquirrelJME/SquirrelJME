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
	FileTime creationTime();
	
	boolean isDirectory();
	
	boolean isOther();
	
	boolean isRegularFile();
	
	boolean isSymbolicLink();
	
	FileTime lastAccessTime();
	
	FileTime lastModifiedTime();
	
	long size();
}



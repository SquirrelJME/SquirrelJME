// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file.attribute;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface BasicFileAttributes
{
	@Api
	FileTime creationTime();
	
	@Api
	boolean isDirectory();
	
	@Api
	boolean isOther();
	
	@Api
	boolean isRegularFile();
	
	@Api
	boolean isSymbolicLink();
	
	@Api
	FileTime lastAccessTime();
	
	@Api
	FileTime lastModifiedTime();
	
	@Api
	long size();
}



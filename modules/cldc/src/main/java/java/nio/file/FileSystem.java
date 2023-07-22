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
import java.io.Closeable;
import java.util.Set;

@Api
public abstract class FileSystem
	implements Closeable
{
	/**
	 * Base constructor.
	 *
	 * @since 2019/12/22
	 */
	@Api
	protected FileSystem()
	{
	}
	
	@Api
	public abstract Iterable<FileStore> getFileStores();
	
	@Api
	public abstract Path getPath(String __a, String... __b);
	
	@Api
	public abstract Iterable<Path> getRootDirectories();
	
	@Api
	public abstract String getSeparator();
	
	@Api
	public abstract boolean isOpen();
	
	@Api
	public abstract boolean isReadOnly();
	
	@Api
	public abstract Set<String> supportedFileAttributeViews();
}


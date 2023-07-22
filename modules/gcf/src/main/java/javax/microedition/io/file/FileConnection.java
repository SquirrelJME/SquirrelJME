// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io.file;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.StreamConnection;

@Api
public interface FileConnection
	extends StreamConnection
{
	@Api
	long availableSize();
	
	@Api
	boolean canRead();
	
	@Api
	boolean canWrite();
	
	@Api
	void create()
		throws IOException;
	
	@Api
	void delete()
		throws IOException;
	
	@Api
	long directorySize(boolean __a)
		throws IOException;
	
	@Api
	boolean exists();
	
	@Api
	long fileSize()
		throws IOException;
	
	@Api
	String getName();
	
	@Api
	String getPath();
	
	@Api
	String getURL();
	
	@Api
	boolean isDirectory();
	
	@Api
	boolean isHidden();
	
	@Api
	boolean isOpen();
	
	@Api
	long lastModified();
	
	@SuppressWarnings("rawtypes")
	@Api
	Enumeration list()
		throws IOException;
	
	@SuppressWarnings("rawtypes")
	@Api
	Enumeration list(String __a, boolean __b)
		throws IOException;
	
	@Api
	void mkdir()
		throws IOException;
	
	@Api
	OutputStream openOutputStream(long __a)
		throws IOException;
	
	@Api
	void rename(String __a)
		throws IOException;
	
	@Api
	void setFileConnection(String __a)
		throws IOException;
	
	@Api
	void setHidden(boolean __a)
		throws IOException;
	
	@Api
	void setReadable(boolean __a)
		throws IOException;
	
	@Api
	void setWritable(boolean __a)
		throws IOException;
	
	@Api
	long totalSize();
	
	@Api
	void truncate(long __a)
		throws IOException;
	
	@Api
	long usedSize();
}



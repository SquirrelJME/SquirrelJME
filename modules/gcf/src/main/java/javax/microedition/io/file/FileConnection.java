// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.StreamConnection;

public interface FileConnection
	extends StreamConnection
{
	public abstract long availableSize();
	
	public abstract boolean canRead();
	
	public abstract boolean canWrite();
	
	public abstract void create()
		throws IOException;
	
	public abstract void delete()
		throws IOException;
	
	public abstract long directorySize(boolean __a)
		throws IOException;
	
	public abstract boolean exists();
	
	public abstract long fileSize()
		throws IOException;
	
	public abstract String getName();
	
	public abstract String getPath();
	
	public abstract String getURL();
	
	public abstract boolean isDirectory();
	
	public abstract boolean isHidden();
	
	public abstract boolean isOpen();
	
	public abstract long lastModified();
	
	public abstract Enumeration list()
		throws IOException;
	
	public abstract Enumeration list(String __a, boolean __b)
		throws IOException;
	
	public abstract void mkdir()
		throws IOException;
	
	public abstract DataInputStream openDataInputStream()
		throws IOException;
	
	public abstract DataOutputStream openDataOutputStream()
		throws IOException;
	
	public abstract InputStream openInputStream()
		throws IOException;
	
	public abstract OutputStream openOutputStream()
		throws IOException;
	
	public abstract OutputStream openOutputStream(long __a)
		throws IOException;
	
	public abstract void rename(String __a)
		throws IOException;
	
	public abstract void setFileConnection(String __a)
		throws IOException;
	
	public abstract void setHidden(boolean __a)
		throws IOException;
	
	public abstract void setReadable(boolean __a)
		throws IOException;
	
	public abstract void setWritable(boolean __a)
		throws IOException;
	
	public abstract long totalSize();
	
	public abstract void truncate(long __a)
		throws IOException;
	
	public abstract long usedSize();
}



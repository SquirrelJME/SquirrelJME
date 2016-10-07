// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.media.protocol;

import java.io.IOException;
import javax.microedition.media.Controllable;

public interface SourceStream
	extends Controllable
{
	public static final int NOT_SEEKABLE =
		0;
	
	public static final int RANDOM_ACCESSIBLE =
		2;
	
	public static final int SEEKABLE_TO_START =
		1;
	
	public abstract ContentDescriptor getContentDescriptor();
	
	public abstract long getContentLength();
	
	public abstract int getSeekType();
	
	public abstract int getTransferSize();
	
	public abstract int read(byte[] __a, int __b, int __c)
		throws IOException;
	
	public abstract long seek(long __a)
		throws IOException;
	
	public abstract long tell();
}



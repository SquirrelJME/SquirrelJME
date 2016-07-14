// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface SeekableByteChannel
	extends ByteChannel
{
	public abstract long position()
		throws IOException;
	
	public abstract SeekableByteChannel position(long __a)
		throws IOException;
	
	public abstract int read(ByteBuffer __a)
		throws IOException;
	
	public abstract long size()
		throws IOException;
	
	public abstract SeekableByteChannel truncate(long __a)
		throws IOException;
	
	public abstract int write(ByteBuffer __a)
		throws IOException;
}



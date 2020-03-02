// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.protocol;

import java.io.IOException;
import javax.microedition.media.Controllable;

public interface SourceStream
	extends Controllable
{
	int NOT_SEEKABLE =
		0;
	
	int RANDOM_ACCESSIBLE =
		2;
	
	int SEEKABLE_TO_START =
		1;
	
	ContentDescriptor getContentDescriptor();
	
	long getContentLength();
	
	int getSeekType();
	
	int getTransferSize();
	
	int read(byte[] __a, int __b, int __c)
		throws IOException;
	
	long seek(long __a)
		throws IOException;
	
	long tell();
}



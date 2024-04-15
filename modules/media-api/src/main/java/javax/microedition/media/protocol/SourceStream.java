// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.protocol;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import javax.microedition.media.Controllable;

@Api
public interface SourceStream
	extends Controllable
{
	@Api
	int NOT_SEEKABLE =
		0;
	
	@Api
	int RANDOM_ACCESSIBLE =
		2;
	
	@Api
	int SEEKABLE_TO_START =
		1;
	
	@Api
	ContentDescriptor getContentDescriptor();
	
	@Api
	long getContentLength();
	
	@Api
	int getSeekType();
	
	@Api
	int getTransferSize();
	
	@Api
	int read(byte[] __a, int __b, int __c)
		throws IOException;
	
	@Api
	long seek(long __a)
		throws IOException;
	
	@Api
	long tell();
}



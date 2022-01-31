// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.channels;

import cc.squirreljme.completion.Completion;
import cc.squirreljme.completion.CompletionState;
import cc.squirreljme.completion.Standard;
import java.io.IOException;

@Standard
public interface SeekableByteChannel
	extends ByteChannel
{
	@Completion(CompletionState.NOTHING)
	long position()
		throws IOException;
	
	@Completion(CompletionState.NOTHING)
	SeekableByteChannel position(long __a)
		throws IOException;
	
	@Completion(CompletionState.NOTHING)
	long size()
		throws IOException;
	
	@Completion(CompletionState.NOTHING)
	SeekableByteChannel truncate(long __a)
		throws IOException;
	
}



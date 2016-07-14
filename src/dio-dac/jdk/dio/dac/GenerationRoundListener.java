// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.dac;

import java.nio.IntBuffer;
import jdk.dio.OutputRoundListener;
import jdk.dio.RoundCompletionEvent;

public interface GenerationRoundListener
	extends OutputRoundListener<DACChannel, IntBuffer>
{
	public abstract void failed(Throwable __a, DACChannel __b);
	
	public abstract void outputRoundCompleted(RoundCompletionEvent<DACChannel
		, IntBuffer> __a);
}



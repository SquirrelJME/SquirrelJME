// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.adc;

import java.nio.IntBuffer;
import jdk.dio.InputRoundListener;
import jdk.dio.RoundCompletionEvent;

public interface AcquisitionRoundListener
	extends InputRoundListener<ADCChannel, IntBuffer>
{
	public abstract void failed(Throwable __a, ADCChannel __b);
	
	public abstract void inputRoundCompleted(RoundCompletionEvent<ADCChannel,
		IntBuffer> __a);
}



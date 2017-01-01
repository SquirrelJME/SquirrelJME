// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import java.nio.Buffer;

public interface InputRoundListener<P extends Device<? super P>, B extends 
	Buffer>
	extends DeviceEventListener, AsyncErrorHandler<P>
{
	public abstract void inputRoundCompleted(RoundCompletionEvent<P, B> __a);
}



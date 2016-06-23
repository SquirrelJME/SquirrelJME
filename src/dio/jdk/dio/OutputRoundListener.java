// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio;

import java.nio.Buffer;

public interface OutputRoundListener<P extends Device<? super P>, B extends 
	Buffer>
	extends DeviceEventListener, AsyncErrorHandler<P>
{
	public abstract void outputRoundCompleted(RoundCompletionEvent<P, B> __a
		);
}



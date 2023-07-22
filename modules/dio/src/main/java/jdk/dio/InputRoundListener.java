// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.nio.Buffer;

@Api
public interface InputRoundListener<P extends Device<? super P>, B extends 
	Buffer>
	extends DeviceEventListener, AsyncErrorHandler<P>
{
	@Api
	void inputRoundCompleted(RoundCompletionEvent<P, B> __a);
}



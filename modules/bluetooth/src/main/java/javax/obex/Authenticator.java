// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.obex;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface Authenticator
{
	@Api
	PasswordAuthentication onAuthenticationChallenge(String __a, boolean __b,
		boolean __c);
	
	@Api
	byte[] onAuthenticationResponse(byte[] __a);
}

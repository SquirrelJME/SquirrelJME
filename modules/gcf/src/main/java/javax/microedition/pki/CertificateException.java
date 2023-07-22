// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.pki;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

@Api
public class CertificateException
	extends IOException
{
	@Api
	public static final byte BAD_EXTENSIONS =
		1;
	
	@Api
	public static final byte BROKEN_CHAIN =
		11;
	
	@Api
	public static final byte CERTIFICATE_CHAIN_TOO_LONG =
		2;
	
	@Api
	public static final byte EXPIRED =
		3;
	
	@Api
	public static final byte INAPPROPRIATE_KEY_USAGE =
		10;
	
	@Api
	public static final byte MISSING_SIGNATURE =
		5;
	
	@Api
	public static final byte NOT_YET_VALID =
		6;
	
	@Api
	public static final byte ROOT_CA_EXPIRED =
		12;
	
	@Api
	public static final byte SITENAME_MISMATCH =
		7;
	
	@Api
	public static final byte UNAUTHORIZED_INTERMEDIATE_CA =
		4;
	
	@Api
	public static final byte UNRECOGNIZED_ISSUER =
		8;
	
	@Api
	public static final byte UNSUPPORTED_PUBLIC_KEY_TYPE =
		13;
	
	@Api
	public static final byte UNSUPPORTED_SIGALG =
		9;
	
	@Api
	public static final byte VERIFICATION_FAILED =
		14;
	
	@Api
	public CertificateException(Certificate __a, byte __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public CertificateException(String __a, Certificate __b, byte __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Certificate getCertificate()
	{
		throw Debugging.todo();
	}
	
	@Api
	public byte getReason()
	{
		throw Debugging.todo();
	}
}



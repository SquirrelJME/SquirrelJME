// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.pki;

import java.io.IOException;

public class CertificateException
	extends IOException
{
	public static final byte BAD_EXTENSIONS =
		1;
	
	public static final byte BROKEN_CHAIN =
		11;
	
	public static final byte CERTIFICATE_CHAIN_TOO_LONG =
		2;
	
	public static final byte EXPIRED =
		3;
	
	public static final byte INAPPROPRIATE_KEY_USAGE =
		10;
	
	public static final byte MISSING_SIGNATURE =
		5;
	
	public static final byte NOT_YET_VALID =
		6;
	
	public static final byte ROOT_CA_EXPIRED =
		12;
	
	public static final byte SITENAME_MISMATCH =
		7;
	
	public static final byte UNAUTHORIZED_INTERMEDIATE_CA =
		4;
	
	public static final byte UNRECOGNIZED_ISSUER =
		8;
	
	public static final byte UNSUPPORTED_PUBLIC_KEY_TYPE =
		13;
	
	public static final byte UNSUPPORTED_SIGALG =
		9;
	
	public static final byte VERIFICATION_FAILED =
		14;
	
	public CertificateException(Certificate __a, byte __b)
	{
		super();
		throw new Error("TODO");
	}
	
	public CertificateException(String __a, Certificate __b, byte __c)
	{
		super();
		throw new Error("TODO");
	}
	
	public Certificate getCertificate()
	{
		throw new Error("TODO");
	}
	
	public byte getReason()
	{
		throw new Error("TODO");
	}
}



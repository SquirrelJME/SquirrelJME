// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import javax.microedition.pki.Certificate;

public interface SecurityInfo
{
	public abstract String getCipherSuite();
	
	public abstract Certificate getClientCertificate();
	
	public abstract String getProtocolName();
	
	public abstract String getProtocolVersion();
	
	public abstract Certificate getServerCertificate();
}



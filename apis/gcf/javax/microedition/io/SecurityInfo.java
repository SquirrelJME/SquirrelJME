// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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



// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.pki.Certificate;

public interface SecurityInfo
{
	@Api
	String getCipherSuite();
	
	@Api
	Certificate getClientCertificate();
	
	@Api
	String getProtocolName();
	
	@Api
	String getProtocolVersion();
	
	@Api
	Certificate getServerCertificate();
}



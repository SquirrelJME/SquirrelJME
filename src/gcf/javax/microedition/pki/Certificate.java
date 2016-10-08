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


public interface Certificate
{
	public abstract String getIssuer();
	
	public abstract long getNotAfter();
	
	public abstract long getNotBefore();
	
	public abstract String getSerialNumber();
	
	public abstract String getSigAlgName();
	
	public abstract String getSubject();
	
	public abstract String getType();
	
	public abstract String getVersion();
}



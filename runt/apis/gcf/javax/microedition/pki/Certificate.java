// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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



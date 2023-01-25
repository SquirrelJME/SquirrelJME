// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.wireless.messaging;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.util.Date;

@Api
public interface Message
{
	@Api
	String getAddress();
	
	@Api
	Date getTimeStamp();
	
	@Api
	void setAddress(String __address);
}

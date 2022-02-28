// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.wireless.messaging;

import java.util.Date;

public interface Message
{
	String getAddress();
	
	Date getTimeStamp();
	
	void setAddress(String __address);
}

/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * RatufaCoat port to PalmOS!
 *
 * @since 2019/07/12
 */

#include <SystemMgr.h>

#include "sjmerc.h"

/**
 * Main PalmOS entry point.
 *
 * @param cmd Command used.
 * @param cmdpbp Command parameter block.
 * @param launchflags Flags used to launch the application.
 * @since 2019/07/12
 */
UInt32 PilotMain(UInt16 cmd, void* cmdpbp, UInt16 launchflags)
{
	/* Which Launch Code? */
	switch (cmd)
	{
			/* Normal application launch. */
		case sysAppLaunchCmdNormalLaunch:
			break;
			
			/* Unknown */
		default:
			break;
	}
	
	return 0;
}

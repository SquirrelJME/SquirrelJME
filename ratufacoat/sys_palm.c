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

#include <FeatureMgr.h>
#include <SystemMgr.h>
#include <StringMgr.h>
#include <Form.h>

#include "sjmerc.h"

/** Palm OS 3.5 marker. */
#define SJME_PALM_OS35 UINT32_C(0x03503000)

/** The current Palm OS ROM version. */
static UInt32 sjme_palm_romversion;

/** Is there a high density screen? */
static UInt32 sjme_palm_hashidens;

/** Palm OS Native functions. */
static sjme_nativefuncs sjme_palm_nativefuncs;

/**
 * Shows an error on the screen.
 *
 * @param aid The alert ID.
 * @param err The error value.
 * @since 2019/07/14
 */
void sjme_palm_error(UInt16 aid, sjme_error err)
{
	Char sa[maxStrIToALen];
	Char sb[maxStrIToALen];
	
	/* Convert to hex values. */
	StrIToH(sa, err.code);
	StrIToH(sb, err.value);
	
	/* Show alert. */
	FrmCustomAlert(aid, sa, sb, "");
}

/**
 * Real Main PalmOS entry point.
 *
 * @param cmd Command used.
 * @param cmdpbp Command parameter block.
 * @param launchflags Flags used to launch the application.
 * @since 2019/07/14
 */
UInt32 sjme_palm_pilotmain(UInt16 cmd, void* cmdpbp, UInt16 launchflags)
{
	sjme_jvmoptions options;
	sjme_jvm* jvm;
	Err perr;
	UInt32 v;
	sjme_error jerr;
	
	/* Get the ROM version, if that fails then use zero! */
	perr = FtrGet(sysFtrCreator, sysFtrNumROMVersion, &sjme_palm_romversion); 
	if (perr != 0)
		sjme_palm_romversion = 0;
	
	/* Is there the high density feature set? */
	perr = FtrGet(sysFtrCreator, sysFtrNumWinVersion, &v);
	if (perr != 0)
		sjme_palm_hashidens = 0;
	else
		sjme_palm_hashidens = (v >= 4 ? 1 : 0);
		
	/* Setup options. */
	MemSet(&options, sizeof(options), 0);
	
	/* Setup native functions. */
	MemSet(&sjme_palm_nativefuncs, sizeof(sjme_palm_nativefuncs), 0);
	
	/* Initialize the virtual machine. */
	jvm = sjme_jvmnew(&options, &sjme_palm_nativefuncs, &jerr);
	if (jvm == NULL)
	{
		/* Show error. */
		sjme_palm_error(1000, jerr);
		
		/* Send not initialized error. */
		return sysErrNotInitialized;
	}
	
	/* Destroy the JVM now. */
	sjme_jvmdestroy(jvm, &jerr);
	if (jerr.code != SJME_ERROR_NONE)
		sjme_palm_error(1002, jerr);
	
	/* Is Okay. */
	return errNone;
}

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
			return sjme_palm_pilotmain(cmd, cmdpbp, launchflags);
			
			/* Unknown */
		default:
			break;
	}
	
	return errNone;
}

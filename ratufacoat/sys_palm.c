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
#define SJME_PALM_OS35 sysMakeROMVersion(3, 5, 0, sysROMStageRelease, 0)

/** The current Palm OS ROM version. */
static UInt32 sjme_palm_romversion;

/** Is there a high density screen? */
static UInt32 sjme_palm_hashidens;

/** Palm OS Native functions. */
static sjme_nativefuncs sjme_palm_nativefuncs;

/** Frame-buffer information. */
static sjme_framebuffer sjme_palm_fbinfo;

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

/** Flushes the frame buffer, if the pixel format is ancient. */
void sjme_palm_fbinfo_flush(void)
{
}

/** Returns a framebuffer structure. */
sjme_framebuffer* sjme_palm_framebuffer(void)
{
	UInt32 val;
	sjme_jint numpixels, bytedepth;
	WinHandle drawingwindow;
	BitmapType* winbmp;
	
	/* If pixels were already set, use the same structure. */
	if (sjme_palm_fbinfo.pixels != NULL)
		return &sjme_palm_fbinfo;
	
	/* Use newer PalmOS windowing code. */
	if (sjme_palm_romversion >= SJME_PALM_OS35)
	{
		/* High-Density Screen Support. */
		if (sjme_palm_hashidens)
		{
			/* Read screen width. */
			WinScreenGetAttribute(winScreenWidth, &val);
			sjme_palm_fbinfo.width = val;
			
			/* Read screen height. */
			WinScreenGetAttribute(winScreenHeight, &val);
			sjme_palm_fbinfo.height = val;
			
			/* Read pixel format. */
			WinScreenGetAttribute(winScreenPixelFormat, &val);
			switch (val)
			{
				case pixelFormat565:
				case pixelFormat565LE:
					bytedepth = 1;
					sjme_palm_fbinfo.format =
						SJME_PIXELFORMAT_FORMAT_BYTE_INDEXED;
					break;
				
				default:
				case pixelFormatIndexed:
				case pixelFormatIndexedLE:
					bytedepth = 2;
					sjme_palm_fbinfo.format =
						SJME_PIXELFORMAT_FORMAT_SHORT_RGB565;
					break;
			}
			
			/* Scan-line length in bytes. */
			WinScreenGetAttribute(winScreenRowBytes, &val);
			sjme_palm_fbinfo.scanlenbytes = val;
			
			/* Determine pixel-based scan-line length. */
			sjme_palm_fbinfo.scanlen =
				sjme_palm_fbinfo.scanlenbytes / bytedepth;
			
			/* Get raw screen pixels. */
			drawingwindow = WinGetDisplayWindow();
			winbmp = WinGetBitmap(drawingwindow);
			sjme_palm_fbinfo.pixels = BmpGetBits(winbmp);
		}
		
		/* Palm OS 3.5 and up. */
		else
		{
			/* Width. */
			WinScreenMode(winScreenModeGet, &val, NULL, NULL, NULL);
			sjme_palm_fbinfo.width = val;
			
			/* Height. */
			WinScreenMode(winScreenModeGet, NULL, &val, NULL, NULL);
			sjme_palm_fbinfo.height = val;
			
			/* Set scan line properties, always the same. */
			sjme_palm_fbinfo.scanlen = sjme_palm_fbinfo.width;
			
			/* Depth. */
			WinScreenMode(winScreenModeGet, NULL, NULL, &val, NULL);
			switch (val)
			{
					/* Needs to be simulated. */
				case 1:
				case 2:
				case 4:
					sjme_palm_fbinfo.flush = sjme_palm_fbinfo_flush;
					
					/* Indexed color. */
					sjme_palm_fbinfo.format =
						SJME_PIXELFORMAT_FORMAT_BYTE_INDEXED;
					
					/* Scan line bytes. */
					sjme_palm_fbinfo.scanlenbytes =
						sjme_palm_fbinfo.width;
					
					/* Allocate a virtual frame-buffer instead. */
					sjme_palm_fbinfo.pixels =
						MemPtrNew(sjme_palm_fbinfo.height *
							sjme_palm_fbinfo.scanlenbytes);
					break;
				
					/* Indexed/Real Color Mode. */
				case 8:
				case 16:
					/* Which format? */
					sjme_palm_fbinfo.format = (val == 8 ?
						SJME_PIXELFORMAT_FORMAT_BYTE_INDEXED :
						SJME_PIXELFORMAT_FORMAT_SHORT_RGB565);
					
					/* Scan line bytes. */
					sjme_palm_fbinfo.scanlenbytes =
						sjme_palm_fbinfo.width * (val / 8);
					
					/* Setup drawing window. */
					drawingwindow = WinGetDrawWindow();
					winbmp = WinGetBitmap(drawingwindow);
					sjme_palm_fbinfo.pixels = BmpGetBits(winbmp);
					break;
			}
		}
	}
	
	/* Ancient Palm OS. */
	else
	{
		return NULL;
	}
	
	/* Determine pixel count. */
	sjme_palm_fbinfo.numpixels =
		sjme_palm_fbinfo.width * sjme_palm_fbinfo.height;
	
	/* Return static framebuffer. */
	return &sjme_palm_fbinfo;
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
	EventType event;
	
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
	sjme_palm_nativefuncs.framebuffer = sjme_palm_framebuffer;
	
	/* Initialize the virtual machine. */
	jvm = sjme_jvmnew(&options, &sjme_palm_nativefuncs, &jerr);
	if (jvm == NULL)
	{
		/* Show error. */
		sjme_palm_error(1000, jerr);
		
		/* Send not initialized error. */
		return sysErrNotInitialized;
	}
	
	/* Execute until termination. */
	perr = errNone;
	for (;;)
	{
		/* Poll events, quickly return to not block and burn CPU. */
		EvtGetEvent(&event, 0);
		
		/* Have the system handle events, then handle if it is not handled. */
		if (!SysHandleEvent(&event))
		{
		}
		
		/* Just execute the VM and disregard any cycles that remain. */
		sjme_jvmexec(jvm, &jerr, SJME_JINT_C(65536));
		
		/* The JVM hit some kind of error? */
		if (jerr.code != SJME_ERROR_NONE)
		{
			/* Normal JVM exit, not considered a true error. */
			if (jerr.code == SJME_ERROR_JVMEXIT_SUV_OKAY)
				break;
			
			/* Show error. */
			sjme_palm_error(1001, jerr);
			
			/* Stop loop always. */
			break;
		}
		
		/* Keep going! */
		continue;
	}
	
	/* Destroy the JVM now. */
	sjme_jvmdestroy(jvm, &jerr);
	if (jerr.code != SJME_ERROR_NONE)
		sjme_palm_error(1002, jerr);
	
	/* Is Okay. */
	return perr;
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

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
#include <SoundMgr.h>

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
	WinHandle drawingwindow;
	BitmapType* winbmp;
	uint8_t* screen;
	uint8_t* input;
	sjme_jint i, numpixels, v;
	UInt32 depth;
	sjme_error jerr;
	
	/* If there are no pixels, then ignore. */
	if (sjme_palm_fbinfo.pixels == NULL)
		return;
	
	/* Input pixels. */
	input = (uint8_t*)sjme_palm_fbinfo.pixels;
	
	/* Raw screen pixels on 3.5 and up. */
	if (sjme_palm_romversion >= SJME_PALM_OS35)
	{
		/* Get raw screen pixels. */
		drawingwindow = WinGetDisplayWindow();
		winbmp = WinGetBitmap(drawingwindow);
		screen = (uint8_t*)BmpGetBits(winbmp);
		
		/* Read the bit depth. */
		WinScreenMode(winScreenModeGet, NULL, NULL, &depth, NULL);
	}
	
	/* Old Palm PDAs. */
	else
	{
		return;
	}
	
	/* The number of pixels to copy. */
	numpixels = sjme_palm_fbinfo.numpixels;
	
	/* Depends on the depth. */
	switch (depth)
	{
			/* Monochrome. */
		case 1:
			for (i = 0; i < numpixels; i += 8)
			{
				v = 0;
				if (*input++ != 0)
					v |= 0x01;
				if (*input++ != 0)
					v |= 0x02;
				if (*input++ != 0)
					v |= 0x04;
				if (*input++ != 0)
					v |= 0x08;
				if (*input++ != 0)
					v |= 0x10;
				if (*input++ != 0)
					v |= 0x20;
				if (*input++ != 0)
					v |= 0x40;
				if (*input++ != 0)
					v |= 0x80;
				
				*screen++ = v;
			}
			break;
		
			/* Four shades of gray. */
		case 2:
			for (i = 0; i < numpixels; i += 4)
			{
				v = ((*input++) & 0x03);
				v |= ((*input++) & 0x03) << 2;
				v |= ((*input++) & 0x03) << 4;
				v |= ((*input++) & 0x03) << 6;
				
				*screen++ = v;
			}
			break;
		
			/* Sixteen shades of gray. */
		case 4:
			for (i = 0; i < numpixels; i += 2)
			{
				v = ((*input++) & 0x0F);
				v |= ((*input++) & 0x0F) << 4;
				
				*screen++ = v;
			}
			break;
	}
}

/** Returns a framebuffer structure. */
sjme_framebuffer* sjme_palm_framebuffer(void)
{
	UInt32 val, i;
	sjme_jint numpixels, bytedepth;
	WinHandle drawingwindow;
	BitmapType* winbmp;
	sjme_error jerr;
	
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
				case pixelFormatIndexed:
				case pixelFormatIndexedLE:
					bytedepth = 1;
					sjme_palm_fbinfo.format =
						SJME_PIXELFORMAT_BYTE_INDEXED;
					break;
				
				default:
				case pixelFormat565:
				case pixelFormat565LE:
					bytedepth = 2;
					sjme_palm_fbinfo.format =
						SJME_PIXELFORMAT_SHORT_RGB565;
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
			
			/* Handle Depth. */
			WinScreenMode(winScreenModeGet, NULL, NULL, &val, NULL);
			switch (val)
			{
				case 1:
					sjme_palm_fbinfo.format = SJME_PIXELFORMAT_PACKED_ONE;
					break;
					
				case 2:
					sjme_palm_fbinfo.format = SJME_PIXELFORMAT_PACKED_TWO;
					break;
					
				case 4:
					sjme_palm_fbinfo.format = SJME_PIXELFORMAT_PACKED_FOUR;
					break;
				
				case 8:
					sjme_palm_fbinfo.format = SJME_PIXELFORMAT_BYTE_INDEXED;
					break;
				
				case 16:
					sjme_palm_fbinfo.format = SJME_PIXELFORMAT_SHORT_RGB565;
					break;
			}
			
			/* Scan line bytes. */
			sjme_palm_fbinfo.scanlenbytes =
				(sjme_palm_fbinfo.scanlen * val) / 8;
			
			/* Setup drawing window. */
			drawingwindow = WinGetDisplayWindow();
			winbmp = WinGetBitmap(drawingwindow);
			sjme_palm_fbinfo.pixels = BmpGetBits(winbmp);
		}
	}
	
	/* Ancient Palm OS. */
	else
	{
		return NULL;
	}
	
	/* Determine pixel count. */
	sjme_palm_fbinfo.numpixels =
		sjme_palm_fbinfo.scanlen * sjme_palm_fbinfo.height;
	
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
	options.ramsize = SJME_JINT_C(4194304);
	
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

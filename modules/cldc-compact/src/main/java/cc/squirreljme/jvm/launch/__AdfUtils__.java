// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Utilities for handling ADF files.
 *
 * @since 2023/04/10
 */
final class __AdfUtils__
{
	/**
	 * Not used.
	 * 
	 * @since 2023/04/10
	 */
	private __AdfUtils__()
	{
	}
	
	/**
	 * Parses an ADF binary descriptor.
	 * 
	 * @param __outProps The output ADF properties. 
	 * @param __in The input data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/04/10
	 */
	static void __parseAdfBinary(Map<String, String> __outProps,
		InputStream __in)
		throws IOException, NullPointerException
	{
		if (__outProps == null || __in == null)
			throw new NullPointerException("NARG");
		
		// This is binary data
		DataInputStream in = new DataInputStream(__in);
		
		// TODO: Determine which index specifies start of application name
		Debugging.todoNote("AppName position is unknown?");
		int varDataOffsetAppName = 0;
		
		// 0x00: Unknown??? (0)
		int unknownHeader0x00Zero = in.readUnsignedShort();
		
		// 0x02: Unknown??? (1)
		int unknownHeader0x02Zero = in.readUnsignedShort();
		
		// 0x04: Unknown??? (2)
		int unknownHeader0x04Zero = in.readUnsignedShort();
		
		// 0x06: Unknown Size???
		// Calculated as part of "Used Storage"
		int unknownSize = in.readInt();
		
		// 0x0A: Jar Size (5)
		// Calculated as part of "Used Storage"
		int jarSize = in.readUnsignedShort();
		
		// 0x0C: Size of the .Sto file (SPsize) (6)
		// Calculated as part of "Used Storage"
		int stoSize = in.readInt();
		
		// 0x10: Unknown??? (7)
		int unknownHeader0x10Zero = in.readUnsignedShort();
		
		// 0x12: Unknown??? (8)
		int unknownHeader0x12Zero = in.readUnsignedShort();
		
		// 0x14: Unknown??? (9)
		int unknownHeader0x14Zero = in.readUnsignedShort();
		
		// 0x16: varData Offset to AppClass (10) [Base from 53?]
		int varDataOffsetAppClass = in.readUnsignedShort();
		
		// 0x18: varData Offset to AppParam (11) [Base from 53?]
		int varDataOffsetAppParam = in.readUnsignedShort();
		
		// 0x1A: varData Offset to PackageURL (12) [Base from 53?]
		int varDataOffsetPackageUrl = in.readUnsignedShort();
		
		// 0x1C: varData Offset to AppVer (13) [Base from 53?]
		int varDataOffsetAppVer = in.readUnsignedShort();
		
		// 0x1E: varData Offset to LastModified (14) [Base from 53?]
		int varDataOffsetLastModified = in.readUnsignedShort();
		
		// 0x20: Length of AppName (15)
		int lengthAppName = in.readUnsignedShort();
		
		// 0x22: Draw Area Width (16)
		int drawAreaWidth = in.readUnsignedShort();
		
		// 0x24: Draw Area Height (17)
		int drawAreaHeight = in.readUnsignedShort();
		
		// 0x26: Length of variable data section (18)
		// Calculated as part of "Used Storage"
		int lengthVarData = in.readUnsignedShort();
		
		// 0x28: Class name length (19)
		int lengthAppClass = in.readUnsignedByte();
		
		// 0x29: Length of AppParam (20)
		int lengthAppParam = in.readUnsignedByte();
		
		// 0x2A: Length of PackageURL (21)
		int lengthPackageUrl = in.readUnsignedByte();
		
		// 0x2B: Unknown 0x80??? (22)
		int unknown0x2BOneTwentyEight = in.readUnsignedByte();
		
		// 0x2C: Length of AppVer (23)
		int lengthAppVer = in.readUnsignedByte();
		
		// 0x2D: Length of LastModified (24)
		int lengthLastModified = in.readUnsignedByte();
		
		// 0x2E: Unknown??? (25) [1???]
		// 0x2E: Possible position of AppName?
		// 0x2E: Application Type? 0 = None? 1 = i-Appli?
		int unknown0x2EOne = in.readUnsignedByte();
		
		// 0x2F: Unknown Zero??? (26)
		// 0x2F: Possible position of AppName?
		// 0x2F: Application Type? 0 = None? 1 = i-Appli?
		int unknown0x2FZero = in.readUnsignedByte();
		
		// 0x30: Unknown 129??? (27)
		int unknown0x30OneTwentyNine = in.readUnsignedShort();
		
		// 0x32: Tracing is enabled (0x01) Maybe other flags?
		int enableTracing = in.readUnsignedByte();
		
		// 0x33: Unknown 0??? (31)
		// 0x33: Possible auto start time?
		int unknownHeader0x33Zero = in.readUnsignedByte();
		
		// 0x34: Auto Start Interval Hours (32)
		int autoStartIntervalHours = in.readUnsignedByte();
		
		// Variable data section
		byte[] varData = new byte[lengthVarData];
		in.readFully(varData);
		
		// Store application name
		if (lengthAppName > 0)
			__outProps.put(IModeApplication._APP_NAME,
				new String(varData, varDataOffsetAppName, lengthAppName,
					"shift-jis"));
		
		// Store application class
		if (lengthAppClass > 0)
			__outProps.put(IModeApplication._APP_CLASS,
				new String(varData, varDataOffsetAppClass, lengthAppClass,
					"shift-jis"));
		
		// Store application parameters
		if (lengthAppParam > 0)
			__outProps.put(IModeApplication._APP_PARAMS,
				new String(varData, varDataOffsetAppParam, lengthAppParam,
					"shift-jis"));
		
		// Store application version
		if (lengthAppVer > 0)
			__outProps.put(IModeApplication._APP_VERSION,
				new String(varData, varDataOffsetAppVer, lengthAppVer,
					"shift-jis"));
		
		// Store Package URL
		if (lengthPackageUrl > 0)
			__outProps.put(IModeApplication._PACKAGE_URL,
				new String(varData, varDataOffsetPackageUrl,
					lengthPackageUrl, "shift-jis"));
		
		// Last modified time
		if (lengthLastModified > 0)
			__outProps.put(IModeApplication._LAST_MODIFIED,
				new String(varData, varDataOffsetLastModified,
					lengthLastModified, "shift-jis"));
		
		// Store size of JAR
		if (jarSize > 0)
			__outProps.put(IModeApplication._APP_SIZE,
				Integer.toString(jarSize));
		
		// Store size of scratchpad
		if (stoSize > 0)
			__outProps.put(IModeApplication._SP_SIZE,
				Integer.toString(stoSize));
		
		// Store drawing area size
		if (drawAreaWidth > 0 && drawAreaHeight > 0)
			__outProps.put(IModeApplication._DRAW_AREA,
				String.format("%dx%d", drawAreaWidth, drawAreaHeight));
		
		// Store start interval
		if (autoStartIntervalHours > 0)
			__outProps.put(IModeApplication._LAUNCH_AT,
				String.format("I %d", autoStartIntervalHours));
		
		// Application tracing enabled?
		if (enableTracing == 1)
			__outProps.put(IModeApplication._APP_TRACE,
				"on");
		
		// Debugging
		Debugging.debugNote("Decoded ADF: %s%n", __outProps);
	}
	
	/**
	 * Parses text based ADF properties.
	 * 
	 * @param __outProps The output properties.
	 * @param __in The input data stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/04/10
	 */
	static void __parseAdfText(Map<String, String> __outProps,
		InputStream __in)
		throws IOException, NullPointerException
	{
		if (__outProps == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Load in the JAM (Is encoded in Japanese)
		try (BufferedReader jamBr = new BufferedReader(
			new InputStreamReader(__in, "shift-jis")))
		{
			for (;;)
			{
				// EOF?
				String ln = jamBr.readLine();
				if (ln == null)
					break;
				
				// No equal sign, ignore
				int eq = ln.indexOf('=');
				if (eq < 0)
					continue;
				
				// Split into key and value form
				String key = ln.substring(0, eq).trim();
				String val = ln.substring(eq + 1).trim();
				
				// Key might be polluted by NULs
				int keyNul = key.lastIndexOf(0);
				if (keyNul >= 0)
					key = key.substring(keyNul + 1).trim();
				
				// Value may as well be polluted by NULs
				int valNul = val.lastIndexOf(0);
				if (valNul >= 0)
					key = key.substring(valNul + 1).trim();
				
				// Store into if the key is valid
				if (!key.isEmpty())
					__outProps.put(key, val);
			}
		}
	}
}

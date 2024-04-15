// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.suite.EntryPoint;
import cc.squirreljme.jvm.suite.EntryPoints;
import cc.squirreljme.jvm.suite.InvalidSuiteException;
import cc.squirreljme.jvm.suite.SuiteInfo;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This contains specific parsers for various types of applications.
 *
 * @since 2024/01/06
 */
public enum ApplicationParser
{
	/** Java Applications. */
	JAVA
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/01/06
		 */
		@Override
		protected void parse(ApplicationParserState __state)
			throws NullPointerException
		{
			if (__state == null)
				throw new NullPointerException("NARG");
			
			// Try to read the manifest from the given JAR and process the
			// suite information
			SuiteInfo info;
			JavaManifest man;
			try (InputStream rc = __state.openResource(
				"META-INF/MANIFEST.MF"))
			{
				// If no manifest exists, might not be a JAR
				if (rc == null)
				{
					Debugging.debugNote(
						"No META-INF/MANIFEST.MF in %s...",
						__state.libraryPath());
					
					return;
				}
				
				man = new JavaManifest(rc);
				info = new SuiteInfo(man);
			}
			
			// Prevent bad JARs and files from messing things up
			catch (IOException | InvalidSuiteException | MLECallError e)
			{
				e.printStackTrace();
				return;
			}
			
			switch (info.type())
			{
				// Handle library
				case LIBLET:
				case SQUIRRELJME_API:
					__state.register(info);
					return;
				
				// Handle application
				case MIDLET:
					// Setup application information for all possible entry
					// points
					for (EntryPoint e : new EntryPoints(man))
					{
						// Load application
						JavaApplication app = __state.newJavaApplication(info,
							e);
						
						// Add it in
						__state.addApplication(app);
						
						// Indicate that it was scanned
						__state.scanned(app);
					}
					return;
				
				// Unknown?
				default:
					throw Debugging.oops();
			}
		}
	},
	
	/** I-mode/i-appli. */
	I_MODE
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/01/06
		 */
		@Override
		protected void parse(ApplicationParserState __state)
			throws NullPointerException
		{
			if (__state == null)
				throw new NullPointerException("NARG");
			
			// Try to determine what our JAM would be called
			String jarName = __state.libraryPath();
			
			// Determine the name of the JAM file to load
			JarPackageBracket jam = __state.findFirstSibling(jarName,
				".jam", ".adf");
			
			// If there is no JAM file, this cannot be an i-mode application
			if (jam == null)
			{
				Debugging.debugNote("No JAM found for %s.",
					jarName);
				return;
			}
			
			// Try to locate the scratchpad seed archive
			JarPackageBracket binarySto = __state.findIModeScratchPad(
				jarName);
			
			// Store where the scratchpad seed should be found
			Map<String, String> extraSysProps = new LinkedHashMap<>();
			if (binarySto != null)
				extraSysProps.put(
					IModeApplication.SEED_SCRATCHPAD_PREFIX + ".0",
					__state.libraryPath(binarySto));
			
			// Load the ADF/JAM descriptor that describes this application
			Map<String, String> adfProps = new LinkedHashMap<>();
			try (InputStream jamIn = __state.openResource(jam,
				ApplicationParser.DATA_RESOURCE))
			{
				// Missing? Cannot be an i-mode application
				if (jamIn == null)
					return;
				
				// Parse by text
				__AdfUtils__.__parseAdfText(adfProps, jamIn);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
			
			// Load application
			try
			{
				// Setup application
				IModeApplication app = __state.newIModeApplication(adfProps,
					extraSysProps);
				
				// Add it in
				__state.addApplication(app);
				
				// Indicate that it was scanned
				__state.scanned(app);
			}
			catch (InvalidSuiteException e)
			{
				e.printStackTrace();
			}
		}
	},
	
	/** I-Mode JV-Lite 2. */
	I_MODE_JV_LITE2
	{
		/**
		 * {@inheritDoc}
		 * @since 2024/01/06
		 */
		@Override
		protected void parse(ApplicationParserState __state)
			throws NullPointerException
		{
			if (__state == null)
				throw new NullPointerException("NARG");
			
			// We need to locate the binary form of the ADF
			String jarName = __state.libraryPath();
			
			// Determine the name of the JAM file to load
			JarPackageBracket binaryAdf = __state.findFirstSibling(
				jarName, ".adf");
			
			// If there is no ADF file, this cannot be an i-mode application
			if (binaryAdf == null)
			{
				Debugging.debugNote("No Binary ADF found for %s.",
					jarName);
				return;
			}
			
			// Decode the Binary ADF information
			Map<String, String> adfProps = new LinkedHashMap<>();
			try (InputStream binaryAdfIn = __state.openResource(
				binaryAdf, ApplicationParser.DATA_RESOURCE))
			{
				// Missing? Cannot be an i-mode application
				if (binaryAdfIn == null)
					return;
				
				// Parse using binary format
				__AdfUtils__.__parseAdfBinary(adfProps, binaryAdfIn);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
			
			// If no class is specified then we cannot launch this
			if (!adfProps.containsKey(IModeApplication._APP_CLASS))
				return;
			
			// Additional i-mode specific properties?
			Map<String, String> extraSysProps = new LinkedHashMap<>();
			
			// Try to locate the scratchpad seed archive
			JarPackageBracket binarySto = __state.findIModeScratchPad(
				jarName);
			
			// Store where the scratchpad seed should be found
			if (binarySto != null)
				extraSysProps.put(
					IModeApplication.SEED_SCRATCHPAD_PREFIX + ".0",
					__state.libraryPath(binarySto));
			
			// Load application
			try
			{
				IModeApplication app = __state.newIModeApplication(adfProps,
					extraSysProps);
				
				// Add it in
				__state.addApplication(app);
				
				// Indicate that it was scanned
				__state.scanned(app);
			}
			catch (InvalidSuiteException e)
			{
				e.printStackTrace();
			}
		}
	},
	
	/* End. */
	;
	
	/** Data resource name. */
	public static final String DATA_RESOURCE =
		"$DATA$";
	
	/**
	 * Parses the specified state.
	 *
	 * @param __state The state to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	protected abstract void parse(ApplicationParserState __state)
		throws NullPointerException;
}

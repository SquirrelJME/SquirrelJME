// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.AOTSettings;
import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.RomSettings;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.csv.ModuleCsvEntry;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.runtime.cldc.util.StringUtils;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Nanocoat support.
 *
 * @since 2023/05/28
 */
public class NanoCoatBackend
	implements Backend
{
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void compileClass(CompileSettings __settings, LinkGlob __glob,
		String __name, InputStream __in)
		throws IOException, NullPointerException
	{
		if (__settings == null || __glob == null || __name == null ||
			__in == null)
			throw new NullPointerException("NARG");
		
		NanoCoatLinkGlob glob = (NanoCoatLinkGlob)__glob;
		
		// Load input class
		/* {@squirreljme.error NC01 Mismatched class name.} */
		ClassFile classFile = ClassFile.decode(__in);
		if (!classFile.thisName().equals(new ClassName(__name)))
			throw new RuntimeException("NC01");
		
		// Setup and perform class processing, with state
		ClassProcessor processor = new ClassProcessor(glob, classFile);
		
		// Write header output
		processor.processHeader(glob._headerBlock);
		
		// Record the added file, keeping it sorted
		String baseName = Utils.basicFileName(
			processor.classIdentifier + ".c");
		String sourcePath = glob.inModuleDirectory(baseName);
		
		// Write class identifiers
		glob.registerClass(classFile.thisName(), processor.classInfo.name,
			glob.headerFilePath, sourcePath);
		
		// Process source code in single file
		try (CFile sourceOut = glob.archive.nextCFile(sourcePath))
		{
			// Do the actual include of ourselves
			sourceOut.preprocessorInclude(Constants.SJME_NVM_HEADER);
			sourceOut.preprocessorInclude(glob.headerFileName);
			
			// Process source code
			processor.processSource(sourceOut);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void compileResource(CompileSettings __settings, LinkGlob __glob,
		String __path, InputStream __in)
		throws IOException, NullPointerException
	{
		if (__settings == null || __glob == null || __path == null ||
			__in == null)
			throw new NullPointerException("NARG");
		
		NanoCoatLinkGlob glob = (NanoCoatLinkGlob)__glob;
		
		// Mangle path of this resource to name it and build type from it
		String rcIdentifier = Utils.symbolResourcePath(glob, __path);
		CVariable rcVar = CVariable.of(
			JvmTypes.STATIC_RESOURCE.type().constType(),
			rcIdentifier);
		
		// Store identifier for later usage
		glob.resourceIdentifiers.put(__path,
			CIdentifier.of(rcIdentifier));
		
		// Define in the header
		CSourceWriter headerOut = glob._headerBlock;
		headerOut.declare(rcVar.extern());
		
		// Process source code in single file
		String rcFileName = Utils.basicFileName(rcIdentifier + ".c");
		try (CFile sourceOut = glob.archive.nextCFile(
			glob.inModuleDirectory(rcFileName)))
		{
			// Do the actual include of ourselves
			sourceOut.preprocessorInclude(Constants.SJME_NVM_HEADER);
			sourceOut.preprocessorInclude(glob.headerFileName);
			
			// Load in byte values
			byte[] data = StreamUtils.readAll(__in);
			
			// Write values for the resource data
			try (CStructVariableBlock struct = sourceOut.define(
				CStructVariableBlock.class, rcVar))
			{
				struct.memberSet("path",
					CBasicExpression.string(__path));
				struct.memberSet("pathHash",
					CBasicExpression.number(__path.hashCode()));
				struct.memberSet("size",
					CBasicExpression.number(Constants.JINT_C, data.length));
				struct.memberSet("data",
					CExpressionBuilder.builder()
						.array(data)
						.build());
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void dumpGlob(AOTSettings __inGlob, byte[] __name,
		PrintStream __out)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public LinkGlob linkGlob(AOTSettings __aotSettings,
		CompileSettings __settings, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__aotSettings == null || __settings == null || __out == null)
			throw new NullPointerException("NARG");
		
		return new NanoCoatLinkGlob(__aotSettings, __out);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public String name()
	{
		return "nanocoat";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void rom(AOTSettings __aotSettings, RomSettings __settings,
		OutputStream __out, VMClassLibrary... __libs)
		throws IOException, NullPointerException
	{
		if (__settings == null || __out == null || __libs == null ||
			__libs.length == 0)
			throw new NullPointerException("NARG");
		
		// Target module strings
		Set<String> modulesCsv = new LinkedHashSet<>();
		Set<String> sharedCsv = new LinkedHashSet<>();
		Set<String> classesCsv = new LinkedHashSet<>();
		Set<String> rcsDataCsv = new LinkedHashSet<>();
			
		// The root directory name of the ROM
		String romDir = String.format("specific/%s_%s/",
			__aotSettings.sourceSet,
			__aotSettings.clutterLevel);
		
		// The output is just a ZIP where we copy all the input entries for
		// each library to... since NanoCoat is a collection of source code
		try (ZipStreamWriter zip = new ZipStreamWriter(__out); 
			ArchiveOutputQueue archive = new ArchiveOutputQueue(zip))
		{
			// Process each table
			for (VMClassLibrary library : __libs)
				for (String file : library.listResources())
					try (InputStream data = library.resourceAsStream(file))
					{
						byte[] copy = null;
						
						// Merge CSV tables
						if (file.endsWith(".csv"))
						{
							Set<String> targetCsv;
							if (file.endsWith("/modules.csv"))
								targetCsv = modulesCsv;
							else if (file.endsWith("/shared.csv"))
								targetCsv = sharedCsv;
							else if (file.endsWith("/classes.csv"))
								targetCsv = classesCsv;
							else if (file.endsWith("/resources.csv"))
								targetCsv = rcsDataCsv;
							else
								continue;
							
							// Load in copied data
							copy = StreamUtils.readAll(data);
							
							// Combine in all lines
							try (ByteArrayInputStream in =
								 new ByteArrayInputStream(copy))
							{
								targetCsv.addAll(StreamUtils.readAllLines(
									in, "utf-8"));
							}
						}
						
						// Shared files are copied to the output root and
						// are later merged together
						String outPath;
						if (file.startsWith("shared/"))
						{
							outPath = file;
							
							// Ignore duplicate shared files
							if (archive.hasOutput(outPath))
								continue;
						}
						
						// In specific, note that it is already in the form
						// of "modules/module_name/whatever.c" so it just
						// needs a modified prefix
						else
							outPath = romDir + file;
						
						// Write to the output
						try (OutputStream out = archive.nextEntry(outPath))
						{
							if (copy != null)
								out.write(copy, 0, copy.length);
							else
								StreamUtils.copy(data, out);
						}
					}
			
			// Parse modules
			Set<ModuleCsvEntry> modules = ModuleCsvEntry.fromCsv(modulesCsv);
			
			// Write ROM source
			String romSource = String.format("%s_%s.c",
				__aotSettings.sourceSet,
				__aotSettings.clutterLevel);
			try (CFile out = archive.nextCFile(romSource))
			{
				NanoCoatBackend.__writeRomC(__aotSettings, __settings,
					modules);
			}
			
			// Write merged tables
			NanoCoatBackend.__writeRomCsv(archive, modulesCsv,
				romDir + "modules.csv");
			NanoCoatBackend.__writeRomCsv(archive, sharedCsv,
				romDir + "shared.csv");
			NanoCoatBackend.__writeRomCsv(archive, classesCsv,
				romDir + "classes.csv");
			NanoCoatBackend.__writeRomCsv(archive, rcsDataCsv,
				romDir + "resources.csv");
			
			// Write CMake file to bridge everything together
			NanoCoatBackend.__writeRomCMake(__aotSettings, __settings,
				archive, modules, romDir + "CMakeLists.txt",
				romSource);
		}
		
		// And make sure the output is truly flushed
		__out.flush();
	}
	
	/**
	 * Writes the ROM C file.
	 *
	 * @param __aotSettings The AOT settings.
	 * @param __settings The ROM settings.
	 * @param __modulesCsv The modules to use.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	private static void __writeRomC(AOTSettings __aotSettings,
		RomSettings __settings, Set<ModuleCsvEntry> __modulesCsv)
		throws IOException, NullPointerException
	{
		if (__aotSettings == null || __settings == null ||
			__modulesCsv == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Writes the ROM {@code CMakeLists.txt} file.
	 *
	 * @param __aotSettings Ahead of time settings.
	 * @param __settings ROM settings.
	 * @param __archive The archive to write to.
	 * @param __csv The input CSV data.
	 * @param __fileName The file name to write to.
	 * @param __romSource The ROM source file.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	private static void __writeRomCMake(AOTSettings __aotSettings,
		RomSettings __settings, ArchiveOutputQueue __archive,
		Set<ModuleCsvEntry> __csv, String __fileName, String __romSource)
		throws IOException, NullPointerException
	{
		if (__aotSettings == null || __settings == null ||
			__archive == null || __csv == null || __fileName == null ||
			__romSource == null)
			throw new NullPointerException("NARG");
		
		// Setup output to write
		try (PrintStream out = __archive.nextPrintStream(__fileName))
		{
			// Write header
			Utils.headerCMake(out);
			
			// Include desired macros accordingly
			out.printf("include(\"%s/%s/%s\")",
				"${CMAKE_SOURCE_DIR}",
				"cmake",
				"rom-macros-and-functions.cmake");
			out.println();
			
			// Build ROM file list
			boolean first = true;
			for (ModuleCsvEntry entry : __csv)
			{
				// name,identifier,header,source
				String[] values = StringUtils.basicSplit(',', entry);
				
				// Skip first line
				if (first)
				{
					first = false;
					continue;
				}
				
				// Write result
				out.printf("squirreljme_romLibrary_include(" +
					"\"%s\" \"%s\" \"%s\" \"%s\")",
					__aotSettings.sourceSet,
					__aotSettings.clutterLevel,
					entry.name, entry.identifier);
				out.println();
			}
			
			// Write 
			out.printf("squirreljme_rom(\"%s\" \"%s\" \"%s\")",
				__aotSettings.sourceSet, __aotSettings.clutterLevel,
				__romSource);
			out.println();
		}
	}
	
	/**
	 * Writes the ROM CSV.
	 *
	 * @param __archive The archive to write to.
	 * @param __csv The CSV target.
	 * @param __fileName The name of the file.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	private static void __writeRomCsv(ArchiveOutputQueue __archive,
		Set<String> __csv, String __fileName)
		throws IOException, NullPointerException
	{
		if (__archive == null || __csv == null || __fileName == null)
			throw new NullPointerException("NARG");
		
		// Just write all target lines
		try (PrintStream ps = __archive.nextPrintStream(__fileName))
		{
			for (String string : __csv)
				ps.println(string);
			
			ps.println();
			ps.flush();
		}
	}
}

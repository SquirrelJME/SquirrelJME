// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CArrayBlock;
import cc.squirreljme.c.CArrayType;
import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CPPBlock;
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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
		
		// Store identifier for later usage
		glob.classIdentifiers.put(classFile.thisName().toString(),
			processor.classInfo.name);
		
		// Write header output
		processor.processHeader(glob._headerBlock);
		
		// Record the added file, keeping it sorted
		String baseName = Utils.basicFileName(
			processor.classIdentifier + ".c");
		glob.outputFiles.add(baseName);
		
		// Process source code in single file
		try (OutputStream out = glob.zip.nextEntry(
			glob.inDirectory(baseName));
			 CFile sourceOut = Utils.cFile(out))
		{
			// Write header
			Utils.headerC(sourceOut);
			
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
		
		// Record the added file, keeping it sorted
		String rcFileName = Utils.basicFileName(rcIdentifier + ".c");
		glob.outputFiles.add(rcFileName);
		
		// Process source code in single file
		try (OutputStream out = glob.zip.nextEntry(
			glob.inDirectory(rcFileName));
			 CFile sourceOut = Utils.cFile(out))
		{
			// Write header
			Utils.headerC(sourceOut);
			
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
					CExpressionBuilder.builder()
						.string(__path)
						.build());
				struct.memberSet("pathHash",
					CBasicExpression.number(__path.hashCode()));
				struct.memberSet("size",
					CExpressionBuilder.builder()
						.number(Constants.JINT_C, data.length)
						.build());
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
		
		// The output is just a ZIP where we copy all the input entries for
		// each library to... since NanoCoat is a collection of source code
		try (ZipStreamWriter zip = new ZipStreamWriter(__out))
		{
			// Directory set for CMakeLists.txt files
			Set<String> cmakeFiles = new SortedTreeSet<>();
			Set<String> libFiles = new SortedTreeSet<>();
			
			// Write each library
			for (VMClassLibrary library : __libs)
				for (String file : library.listResources())
				{
					// Ticker for debugging, there is lots
					System.err.print(".");
					
					// If this is a CMake file, add it
					if (file.endsWith("/CMakeLists.txt"))
						cmakeFiles.add(file);
					
					// Record library files, they are the same name as the
					// parent directory
					int sl = file.indexOf('/');
					int ld = file.lastIndexOf('.');
					if (sl >= 0 && ld > sl && file.substring(0, sl)
						.equals(file.substring(sl + 1, ld)))
						libFiles.add(file.substring(0, sl));
					
					// Do the actual copy
					try (InputStream data = library.resourceAsStream(
							file); 
						OutputStream entry = zip.nextEntry(file))
					{
						// Copy data
						StreamUtils.copy(data, entry);
						
						// Make sure entry is written
						entry.flush();
					}
				}
			
			// What is this ROM called?
			String romBaseName = "rom_" + __aotSettings.sourceSet + "_" +
				__aotSettings.clutterLevel;
			
			// Variable the ROM is defined under
			CVariable romVar = CVariable.of(JvmTypes.STATIC_ROM
				.type().constType(), romBaseName);
			
			// Setup ROM header, for inclusion
			CFileName romHeader = CFileName.of(romBaseName + ".h");
			
			// Write header that gives ROM info
			try (OutputStream rawRom =
					 zip.nextEntry(romHeader.toString()); 
				 CFile cFile = Utils.cFile(rawRom))
			{
				// Start with header
				Utils.headerC(cFile);
				
				// Header guard
				CIdentifier guard = CIdentifier.of("SJME_ROM_" +
					romBaseName.toUpperCase() + "_H");
				try (CPPBlock cpp = cFile.preprocessorIf(
					CExpressionBuilder.builder()
						.not(guard)
						.build()))
				{
					// Define it as included once
					cpp.preprocessorDefine(guard,
						CBasicExpression.number(1));
				}
				
				// Include main header
				cFile.preprocessorInclude(Constants.SJME_NVM_HEADER);
				
				// Declare the ROM variable
				cFile.declare(romVar.extern());
			}
			
			// Write root ROM file that refers to every library within
			try (OutputStream rawRom =
					 zip.nextEntry(romBaseName + ".c"); 
				 CFile cFile = Utils.cFile(rawRom))
			{
				// Start with header
				Utils.headerC(cFile);
				
				// Include headers accordingly
				cFile.preprocessorInclude(Constants.SJME_NVM_HEADER);
				cFile.preprocessorInclude(romHeader);
				
				// Include headers for all the libraries
				for (String lib : libFiles)
					cFile.preprocessorInclude(CFileName.of(
						String.format("%s/%s.h", lib, lib)));
				
				// Write ROM structure
				try (CStructVariableBlock struct = cFile.define(
					CStructVariableBlock.class, romVar))
				{
					// ROM name
					struct.memberSet("name",
						CBasicExpression.string(__aotSettings.clutterLevel));
					
					// The number of libraries within
					struct.memberSet("count",
						CBasicExpression.number(libFiles.size()));
					
					// And links to all the libraries
					try (CArrayBlock array = struct.memberArraySet(
						"libraries"))
					{
						for (String lib : libFiles)
							array.value(CBasicExpression.reference(
								CIdentifier.of(lib +
									"__library")));
					}
				}
			}
			
			// Output the CMake file accordingly
			try (OutputStream rawCMake =
					 zip.nextEntry("CMakeLists.txt");
				PrintStream cmake = new PrintStream(rawCMake, true,
					"utf-8"))
			{
				// Start with header
				Utils.headerCMake(cmake);
				
				// Include functions and macros header
				cmake.println("include(\"${CMAKE_SOURCE_DIR}/cmake/" +
					"rom-macros-and-functions.cmake\"\n\tNO_POLICY_SCOPE)");
				cmake.println();
				
				// Add all subdirectories
				for (String sub : cmakeFiles)
				{
					int ls = sub.indexOf('/');
					cmake.printf("add_subdirectory(%s)",
						(ls < 0 ? sub : sub.substring(0, ls)));
					cmake.println();
				}
				cmake.println();
				
				// Build generator expression for all the libraries within
				cmake.printf("set(%sRomObjects",
					romBaseName);
				cmake.println();
				for (String lib : libFiles)
				{
					cmake.printf("\t\"$<TARGET_OBJECTS:" +
						"SquirrelJMEROM%s_%s_%sObject>\"",
						__aotSettings.sourceSet,
						__aotSettings.clutterLevel, lib);
					cmake.println();
				}
				cmake.println("\t)");
				cmake.println();
				
				// Define ROM
				cmake.printf("squirreljme_rom(%s \"${%sRomObjects}\")",
					romBaseName, romBaseName);
				cmake.println();
				
				// Spacer
				cmake.println();
				
				// Make sure ZIP entry is written
				cmake.flush();
			}
			
			// Make sure this is flushed
			zip.flush();
		}
		
		// And make sure the output is truly flushed
		__out.flush();
	}
}

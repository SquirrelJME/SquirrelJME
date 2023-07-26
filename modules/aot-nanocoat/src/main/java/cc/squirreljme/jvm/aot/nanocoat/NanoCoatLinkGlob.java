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
import cc.squirreljme.c.CBasicExpression;
import cc.squirreljme.c.CBlock;
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CFileName;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CPPBlock;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.AOTSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.jvm.manifest.JavaManifestAttributes;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Stores the link glob information for NanoCoat.
 *
 * @since 2023/05/28
 */
public class NanoCoatLinkGlob
	implements LinkGlob
{
	/** The name of this glob. */
	protected final String name;
	
	/** The base name of the source file. */
	protected final String baseName;
	
	/** The name of this root source output file. */
	protected final CFileName rootSourceFileName;
	
	/** The name of this output file. */
	protected final CFileName headerFileName;
	
	/** The wrapped ZIP file. */
	protected final ZipStreamWriter zip;
	
	/** Raw header output data. */
	protected final ByteArrayOutputStream rawHeaderOut;
	
	/** The output for the C header. */
	protected final CFile headerOut;
	
	/** Raw root source output data. */
	protected final ByteArrayOutputStream rawRootSourceOut;
	
	/** The output for the C source root. */
	protected final CFile rootSourceOut;
	
	/** Files which have been output. */
	protected final Set<String> outputFiles =
		new SortedTreeSet<>();
	
	/** Identifiers to resources. */
	protected final Map<String, CIdentifier> resourceIdentifiers =
		new SortedTreeMap<>(new QuickSearchComparator());
	
	/** Identifiers to classes. */
	protected final Map<String, CIdentifier> classIdentifiers =
		new SortedTreeMap<>(new QuickSearchComparator());
	
	/** Library information. */
	protected final CVariable libraryInfo;
	
	/** Library classes. */
	protected final CVariable libraryClasses;
	
	/** Library resources. */
	protected final CVariable libraryResources;
	
	/** Header guard used. */
	protected final CIdentifier headerGuard;
	
	/** The settings used for the AOT call. */
	protected final AOTSettings aotSettings;
	
	/** The C header block. */
	volatile CBlock _headerBlock;
	
	/** The manifest for later handling. */
	private volatile JavaManifest _manifest;
	
	/** The list of tests. */
	private volatile List<String> _tests;
	
	/**
	 * Initializes the link glob.
	 * 
	 * @param __aotSettings The name of the glob.
	 * @param __headerOut The final output file.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public NanoCoatLinkGlob(AOTSettings __aotSettings,
		OutputStream __headerOut)
		throws NullPointerException
	{
		if (__aotSettings == null || __headerOut == null)
			throw new NullPointerException("NARG");
		
		// Determine output names
		this.aotSettings = __aotSettings;
		
		if (!"main".equals(this.aotSettings.sourceSet))
			this.name = __aotSettings.name + "-" + this.aotSettings.sourceSet;
		else
			this.name = __aotSettings.name;
		this.baseName = Utils.basicFileName(this.name);
		this.headerFileName = CFileName.of(this.baseName + ".h");
		this.rootSourceFileName = CFileName.of(this.baseName + ".c");
		this.headerGuard = CIdentifier.of("SJME_ROM_" +
			this.baseName.toUpperCase() + "_H");
		
		// Library information
		this.libraryInfo = CVariable.of(JvmTypes.STATIC_LIBRARY
				.type().constType(),
			this.baseName + "__library");
		this.libraryClasses = CVariable.of(JvmTypes.STATIC_LIBRARY_CLASSES
				.type().constType(),
			this.baseName + "__classes");
		this.libraryResources = CVariable.of(JvmTypes.STATIC_LIBRARY_RESOURCES
				.type().constType(),
			this.baseName + "__resources");
		
		// Setup ZIP output
		ZipStreamWriter zip = new ZipStreamWriter(__headerOut);
		this.zip = zip;
		
		// Setup output
		try
		{
			// Setup header writing
			this.rawHeaderOut = new ByteArrayOutputStream();
			this.headerOut = Utils.cFile(this.rawHeaderOut);
			
			// Setup source root writing
			this.rawRootSourceOut = new ByteArrayOutputStream();
			this.rootSourceOut = Utils.cFile(this.rawRootSourceOut);
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close out ZIP
		this.zip.flush();
		this.zip.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void finish()
		throws IOException
	{
		// Need to write the raw header output for this class
		ZipStreamWriter zip = this.zip;
		try (OutputStream out = zip.nextEntry(
			this.inDirectory(this.headerFileName)))
		{
			// Finish header block
			CBlock block = this._headerBlock;
			
			// Close block
			block.close();
			
			// Close out the header entry before we write it fully
			this.headerOut.flush();
			this.headerOut.close();
			
			// Copy to the ZIP
			out.write(this.rawHeaderOut.toByteArray());
			out.flush();
		}
		
		// Write root source as well
		CFile rootSourceOut = this.rootSourceOut;
		try (OutputStream out = zip.nextEntry(
			this.inDirectory(this.rootSourceFileName)))
		{
			// Write resources
			try (CStructVariableBlock struct = rootSourceOut.define(
				CStructVariableBlock.class, this.libraryResources))
			{
				Map<String, CIdentifier> ids = this.resourceIdentifiers;
				
				// Store count
				struct.memberSet("count",
					CBasicExpression.number(ids.size()));
				
				// Write references to each identifier
				try (CArrayBlock array = struct.memberArraySet(
					"resources"))
				{
					for (CIdentifier id : ids.values())
						array.value(CBasicExpression.reference(id));
				}
			}
			
			// Write classes
			try (CStructVariableBlock struct = rootSourceOut.define(
				CStructVariableBlock.class, this.libraryClasses))
			{
				Map<String, CIdentifier> ids = this.classIdentifiers;
				
				// Store count
				struct.memberSet("count",
					CBasicExpression.number(ids.size()));
				
				// Write references to each identifier
				try (CArrayBlock array = struct.memberArraySet(
					"classes"))
				{
					for (CIdentifier id : ids.values())
						array.value(CBasicExpression.reference(id));
				}
			}
			
			// Write library info
			try (CStructVariableBlock struct = rootSourceOut.define(
				CStructVariableBlock.class, this.libraryInfo))
			{
				struct.memberSet("name",
					CBasicExpression.string(this.name + ".jar"));
				struct.memberSet("nameHash",
					CBasicExpression.number(this.name.hashCode()));
				
				struct.memberSet("resources",
					CBasicExpression.reference(this.libraryResources));
				struct.memberSet("classes",
					CBasicExpression.reference(this.libraryClasses));
			}
			
			// Close out the header entry before we write it fully
			rootSourceOut.flush();
			rootSourceOut.close();
			
			// Copy to the ZIP
			out.write(this.rawRootSourceOut.toByteArray());
			out.flush();
		}
		
		// Output the CMake build file accordingly
		try (OutputStream rawCMake = zip.nextEntry(
			this.inDirectory("CMakeLists.txt"));
			PrintStream cmake = new PrintStream(rawCMake, true,
				"utf-8"))
		{
			// Start with header
			Utils.headerCMake(cmake);
			
			// Include functions and macros header
			cmake.println("include(\"${CMAKE_SOURCE_DIR}/cmake/" +
				"rom-macros-and-functions.cmake\"\n\tNO_POLICY_SCOPE)");
			cmake.println();
			
			// The true library name
			String libName = this.aotSettings.sourceSet + "_" +
				this.aotSettings.clutterLevel + "_" + this.baseName;
			
			// Generate list of files
			cmake.printf("set(%sFiles", libName);
			cmake.println();
			for (String outputFile : this.outputFiles)
			{
				cmake.printf("\t\"%s\"", outputFile);
				cmake.println();
			}
			cmake.println("\t)");
			cmake.println();
			
			// Use macro for all the files
			cmake.printf("squirreljme_romLibrary(%s \"${%sFiles}\")",
				libName, libName);
			cmake.println();
			cmake.println();
			
			// If these are tests, we need to add every test
			JavaManifest manifest = this._manifest;
			List<String> tests = this._tests;
			if ("test".equals(this.aotSettings.sourceSet) &&
				manifest != null && tests != null && !tests.isEmpty())
			{
				JavaManifestAttributes attrs =
					manifest.getMainAttributes();
				
				// Store class path in variable for later setting
				cmake.printf("set(%sClassPath \"%s\")", libName,
					attrs.getValue("X-SquirrelJME-Tests-ClassPath"));
				cmake.println();
				
				// Register tests
				for (String test : tests)
				{
					cmake.printf("squirreljme_romLibraryTest(" +
						"\"%s\" \"%s\" \"%sClassPath\")",
						this.aotSettings.name,
						test,
						libName);
					cmake.println();
				}
			}
			
			// Make sure ZIP entry is written
			cmake.flush();
		}
	}
	
	/**
	 * Returns the file name in the directory.
	 *
	 * @param __file The file to place.
	 * @return The file in the directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/17
	 */
	public String inDirectory(CFileName __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		return this.inDirectory(__file.toString());
	}
	
	/**
	 * Returns the file name in the directory.
	 *
	 * @param __file The file to place.
	 * @return The file in the directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/17
	 */
	public String inDirectory(String __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		return String.format("%s/%s", this.baseName, __file);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void initialize()
		throws IOException
	{
		CSourceWriter headerOut = this.headerOut;
		CSourceWriter rootSourceOut = this.rootSourceOut;
		
		// Write headers
		Utils.headerC(headerOut);
		Utils.headerC(rootSourceOut);
		
		// Include library header
		rootSourceOut.preprocessorInclude(this.headerFileName);
		
		// Make sure the output files has the actual root source
		this.outputFiles.add(this.rootSourceFileName.toString());
		
		// If we are compiling source, include ourselves via the header
		CPPBlock block = headerOut.preprocessorIf(
				CExpressionBuilder.builder()
					.not()
					.preprocessorDefined(this.headerGuard)
					.build());
		this._headerBlock = block;
		
		// Do not do this again
		block.preprocessorDefine(this.headerGuard,
			CExpressionBuilder.builder()
				.number(1)
			.build());
			
		// Include NanoCoat definitions
		block.preprocessorInclude(Constants.SJME_NVM_HEADER);
		
		// Declare library variables
		block.declare(this.libraryInfo.extern());
		block.declare(this.libraryClasses.extern());
		block.declare(this.libraryResources.extern());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/25
	 */
	@Override
	public void rememberManifest(JavaManifest __manifest)
	{
		this._manifest = __manifest;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/25
	 */
	@Override
	public void rememberTests(List<String> __tests)
	{
		this._tests = __tests;
	}
}

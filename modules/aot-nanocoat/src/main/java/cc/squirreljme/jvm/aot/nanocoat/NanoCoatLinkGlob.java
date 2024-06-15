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
import cc.squirreljme.csv.CsvWriter;
import cc.squirreljme.jvm.aot.AOTSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
import cc.squirreljme.jvm.aot.nanocoat.csv.ClassCsvEntry;
import cc.squirreljme.jvm.aot.nanocoat.csv.CsvType;
import cc.squirreljme.jvm.aot.nanocoat.csv.ModuleCsvEntry;
import cc.squirreljme.jvm.aot.nanocoat.csv.SharedCsvEntry;
import cc.squirreljme.jvm.aot.nanocoat.table.StaticTable;
import cc.squirreljme.jvm.aot.nanocoat.table.StaticTableManager;
import cc.squirreljme.jvm.aot.nanocoat.table.StaticTableType;
import net.multiphasicapps.zip.queue.ArchiveOutputQueue;
import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * Stores the link glob information for NanoCoat.
 *
 * @since 2023/05/28
 */
public class NanoCoatLinkGlob
	implements LinkGlob
{
	/** The module information. */
	protected final ModuleCsvEntry moduleEntry;
	
	/** The name of this glob. */
	protected final String name;
	
	/** The base name of the source file. */
	protected final CIdentifier baseName;
	
	/** The name of this root source output file. */
	protected final CFileName rootSourceFileName;
	
	/** The name of this output file. */
	protected final CFileName headerFileName;
	
	/** The wrapped ZIP file. */
	protected final ZipStreamWriter zip;
	
	/** The output for the C header. */
	protected final CFile headerOut;
	
	/** The output for the C source root. */
	protected final CFile rootSourceOut;
	
	/** Static tables. */
	protected final StaticTableManager tables;
	
	/** Identifiers to resources. */
	@Deprecated
	protected final Map<String, CIdentifier> resourceIdentifiers =
		new SortedTreeMap<>(new QuickSearchComparator());
	
	/** Identifiers to classes. */
	protected final Map<String, ClassCsvEntry> classes =
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
	
	/** The archive queue. */
	protected final CArchiveOutputQueue archive;
	
	/** The path to the header file. */
	protected final CFileName headerFilePath;
	
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
	 * @param __out The final output file.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public NanoCoatLinkGlob(AOTSettings __aotSettings,
		OutputStream __out)
		throws NullPointerException
	{
		if (__aotSettings == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Setup Zip output
		ZipStreamWriter zip = new ZipStreamWriter(__out);
		this.zip = zip;
		
		// Archive writer uses the Zip
		CArchiveOutputQueue archive = new CArchiveOutputQueue(zip);
		this.archive = archive;
		this.tables = new StaticTableManager(this.archive);
		
		// Determine output names
		this.aotSettings = __aotSettings;
		
		if (!"main".equals(this.aotSettings.sourceSet))
			this.name = __aotSettings.name + "-" + this.aotSettings.sourceSet;
		else
			this.name = __aotSettings.name;
		
		this.baseName = CIdentifier.of(Utils.basicFileName(this.name));
		this.headerFileName = CFileName.of(this.baseName + ".h");
		this.headerFilePath = CFileName.of(
			this.inModuleDirectory(this.headerFileName));
		this.rootSourceFileName = CFileName.of(this.baseName + ".c");
		this.headerGuard = CIdentifier.of("SJME_ROM_" +
			this.baseName.toString().toUpperCase() + "_H");
		
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
		
		// Initialize module information
		this.moduleEntry = new ModuleCsvEntry(this.name,
			this.baseName,
			this.headerFilePath.baseName(),
			this.rootSourceFileName.baseName());
		
		// Setup output
		try
		{
			// Setup header writing
			this.headerOut = archive.nextCFile(this.headerFilePath);
			
			// Setup source root writing
			this.rootSourceOut = archive.nextCFile(
				this.inModuleDirectory(this.rootSourceFileName));
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
		this.archive.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void finish()
		throws IOException
	{
		// Need to write final stuff to the archive
		ArchiveOutputQueue archive = this.archive;
		
		// Module information
		try (PrintStream ps = archive.nextPrintStream(
			this.inModuleDirectory("modules.csv")); 
			CsvWriter<ModuleCsvEntry> csv = new CsvWriter<ModuleCsvEntry>(
				CsvType.MODULE.serializer(ModuleCsvEntry.class), ps))
		{
			csv.writeAll(this.moduleEntry);
		}
		
		// Write all the shared input tables
		StaticTableManager tables = this.tables;
		try (PrintStream ps = archive.nextPrintStream(
			this.inModuleDirectory("shared.csv"));
			CsvWriter<SharedCsvEntry> csv = new CsvWriter<>(
				CsvType.SHARED.serializer(SharedCsvEntry.class), ps))
		{
			// Write all table types
			ps.println("prefix,identifier,header,source");
			for (StaticTableType type : StaticTableType.values())
			{
				// Skip empty tables
				if (!tables.hasEntries(type))
					continue;
				
				// Get table
				StaticTable<?, ?> table = tables.table(type.keyType,
					type.valueType, type);
				
				// Write table information
				csv.writeAll(table.csvEntries());
			}
		}
		
		// Finish off the class CSV
		try (PrintStream ps = archive.nextPrintStream(
			this.inModuleDirectory("classes.csv"));
			CsvWriter<ClassCsvEntry> csv = new CsvWriter<>(
				CsvType.CLASSES.serializer(ClassCsvEntry.class), ps))
		{
			csv.writeAll(this.classes.values());
		}
		
		// Finish the header output
		try (CFile headerOut = this.headerOut)
		{
			// Make sure it is written
			headerOut.flush();
		}
		
		// Finish the root source output
		try (CFile rootSourceOut = this.rootSourceOut)
		{
			// Write resources
			if (!this.resourceIdentifiers.isEmpty())
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
			if (!this.classes.isEmpty())
				try (CStructVariableBlock struct = rootSourceOut.define(
					CStructVariableBlock.class, this.libraryClasses))
				{
					Map<String, ClassCsvEntry> ids = this.classes;
					
					// Store count
					struct.memberSet("count",
						CBasicExpression.number(ids.size()));
					
					// Write references to each identifier
					try (CArrayBlock array = struct.memberArraySet(
						"classes"))
					{
						for (ClassCsvEntry id : ids.values())
							array.value(CBasicExpression.reference(
								id.identifier));
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
				
				if (this.aotSettings.originalLibHash == null ||
					this.aotSettings.originalLibHash.isEmpty())
					struct.memberSet("originalLibHash",
						CVariable.NULL);
				else
					struct.memberSet("originalLibHash",
						CBasicExpression.string(this.aotSettings
							.originalLibHash));
				
				if (this.resourceIdentifiers.isEmpty())
					struct.memberSet("resources",
						CVariable.NULL);
				else
					struct.memberSet("resources",
						CBasicExpression.reference(this.libraryResources));
				
				if (this.classes.isEmpty())
					struct.memberSet("classes",
						CVariable.NULL);
				else
					struct.memberSet("classes",
						CBasicExpression.reference(this.libraryClasses));
			}
			
			// Make sure it is written
			rootSourceOut.flush();
		}
		
		// Finish the archive
		archive.close();
	}
	
	/**
	 * Returns the file name in the directory.
	 *
	 * @param __file The file to place.
	 * @return The file in the directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/17
	 */
	public String inModuleDirectory(CFileName __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		return this.inModuleDirectory(__file.toString());
	}
	
	/**
	 * Returns the file name in the directory.
	 *
	 * @param __file The file to place.
	 * @return The file in the directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/17
	 */
	public String inModuleDirectory(String __file)
		throws NullPointerException
	{
		if (__file == null)
			throw new NullPointerException("NARG");
		
		return String.format("modules/%s/%s", this.baseName, __file);
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
		
		// Include library header
		rootSourceOut.preprocessorInclude(this.headerFileName);
		
		// If we are compiling source, include ourselves via the header
		CPPBlock block = headerOut.preprocessorIf(
				CExpressionBuilder.builder()
					.not()
					.preprocessorDefined(this.headerGuard)
					.build());
		this._headerBlock = block;
		
		// Do not do this again
		block.preprocessorDefine(this.headerGuard,
			CBasicExpression.number(1));
			
		// Include NanoCoat definitions
		block.preprocessorInclude(Constants.SJME_NVM_HEADER);
		
		// Declare library variables
		block.declare(this.libraryInfo.extern());
		block.declare(this.libraryClasses.extern());
		block.declare(this.libraryResources.extern());
	}
	
	/**
	 * Registers the given class.
	 *
	 * @param __thisName The name of this class.
	 * @param __identifier The identifier to the class.
	 * @param __headerPath The class header file.
	 * @param __sourcePath The class source file.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/12
	 */
	public void registerClass(ClassName __thisName,
		CIdentifier __identifier, CFileName __headerPath,
		CFileName __sourcePath)
		throws NullPointerException
	{
		if (__thisName == null || __identifier == null ||
			__headerPath == null || __sourcePath == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this.classes.put(__thisName.toString(), new ClassCsvEntry(
			__thisName, __identifier, __headerPath, __sourcePath));
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

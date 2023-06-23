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
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.linkage.ClassLinkTable;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.FieldNameAndType;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This processes the class and keeps track of any needed state.
 *
 * @since 2023/05/31
 */
public class ClassProcessor
{
	/** The input class file. */
	protected final ClassFile classFile;
	
	/** The glob this is being processed under. */
	protected final NanoCoatLinkGlob glob;
	
	/** The output source writer. */
	protected final CSourceWriter out;
	
	/** The C identifier for this class. */
	protected final String classIdentifier;
	
	/** Fields variable for the class. */
	protected final CVariable classFields;
	
	/** Methods variable for the class. */
	protected final CVariable classMethods;
	
	/** Class information table for the class. */
	protected final CVariable classInfo;
	
	/** The link table for the class. */
	protected final ClassLinkTable linkTable =
		new ClassLinkTable();
	
	/** Field processing. */
	private final Map<FieldNameAndType, FieldProcessor> _fields =
		new LinkedHashMap<>();
	
	/** Method processing. */
	private final Map<MethodNameAndType, MethodProcessor> _methods =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the class processor.
	 *
	 * @param __glob The owning glob.
	 * @param __out The output source writer.
	 * @param __classFile The class file being processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public ClassProcessor(NanoCoatLinkGlob __glob, CSourceWriter __out,
		ClassFile __classFile)
		throws NullPointerException
	{
		if (__glob == null || __out == null || __classFile == null)
			throw new NullPointerException("NARG");
		
		this.glob = __glob;
		this.out = __out;
		this.classFile = __classFile;
		
		// Determine the identifier used for this class
		this.classIdentifier = Utils.symbolClassName(__glob,
			this.classFile.thisName());
		this.classInfo = CVariable.of(
			NanoCoatTypes.JCLASS.type(CStructType.class).constType(),
			CIdentifier.of(this.classIdentifier + "__info"));
		this.classFields = CVariable.of(
			NanoCoatTypes.CLASS_FIELDS.type(CStructType.class).constType(),
			CIdentifier.of(this.classIdentifier + "__fields"));
		this.classMethods = CVariable.of(
			NanoCoatTypes.CLASS_METHODS.type(CStructType.class).constType(),
			CIdentifier.of(this.classIdentifier + "__methods"));
		
		// Create processors for each field
		Map<FieldNameAndType, FieldProcessor> fields = this._fields;
		for (Field field : __classFile.fields())
			fields.put(field.nameAndType(), new FieldProcessor(__glob,
				__out, this, field));
		
		// Create processors for each method
		Map<MethodNameAndType, MethodProcessor> methods = this._methods;
		for (Method method : __classFile.methods())
			methods.put(method.nameAndType(), new MethodProcessor(__glob,
				__out, this, method));
	}
	
	/**
	 * Processes the class header output.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	protected void processHeader()
		throws IOException
	{
		CSourceWriter out = this.out;
		ClassFile classFile = this.classFile;
		NanoCoatLinkGlob glob = this.glob;
		
		// Write class identifier, as extern value
		out.define(this.classInfo.extern());
		
		// Process field header details
		for (FieldProcessor field : this._fields.values())
			field.processHeader();
		
		// Process method header details
		for (MethodProcessor method : this._methods.values())
			method.processHeader();
	}
	
	/**
	 * Processes the class source output.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	protected void processSource()
		throws IOException
	{
		CSourceWriter out = this.out;
		ClassFile classFile = this.classFile;
		
		// Process field source details outside the class struct
		try (CStructVariableBlock struct = this.out.declare(
			CStructVariableBlock.class, this.classFields))
		{
			// Field count
			struct.memberSet("count", this._fields.size());
			
			// Then the actual members
			try (CArrayBlock array =
				 struct.memberArraySet("fields"))
			{
				for (FieldProcessor field : this._fields.values())
					field.processInFieldsStruct(array);
			}
		}
		
		// Process method source details outside the class struct
		for (MethodProcessor method : this._methods.values())
			method.processSourceOutside();
		
		// Process method details for method structure
		try (CStructVariableBlock struct = this.out.declare(
			CStructVariableBlock.class, this.classMethods))
		{
			// Method count
			struct.memberSet("count", this._methods.size());
			
			// Then the actual members
			try (CArrayBlock array =
				 struct.memberArraySet("methods"))
			{
				for (MethodProcessor method : this._methods.values())
					method.processInMethodsStruct(array);
			}
		}
		
		// Open class details
		try (CStructVariableBlock struct = this.out.declare(
			CStructVariableBlock.class, this.classInfo))
		{
			// Class details
			struct.memberSet("thisName",
				Utils.quotedString(classFile.thisName()));
			struct.memberSet("superName",
				Utils.quotedString(classFile.superName()));
			struct.memberSet("flags",
				classFile.flags().toJavaBits());
			
			// Fields
			struct.memberSet("fields",
				this.classFields);
			
			// Methods
			struct.memberSet("methods",
				this.classMethods);
		}
	}
}

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
import cc.squirreljme.c.CExpressionBuilder;
import cc.squirreljme.c.CFile;
import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.c.CSourceWriter;
import cc.squirreljme.c.CStructType;
import cc.squirreljme.c.CStructVariableBlock;
import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.Constants;
import cc.squirreljme.jvm.aot.nanocoat.common.JvmTypes;
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
	 * @param __classFile The class file being processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public ClassProcessor(NanoCoatLinkGlob __glob, ClassFile __classFile)
		throws NullPointerException
	{
		if (__glob == null || __classFile == null)
			throw new NullPointerException("NARG");
		
		this.glob = __glob;
		this.classFile = __classFile;
		
		// Determine the identifier used for this class
		this.classIdentifier = Utils.symbolClassName(__glob,
			this.classFile.thisName());
		this.classInfo = CVariable.of(
			JvmTypes.STATIC_CLASS_INFO.type(CStructType.class).constType(),
			CIdentifier.of(this.classIdentifier + "__info"));
		this.classFields = CVariable.of(
			JvmTypes.STATIC_CLASS_FIELDS.type(CStructType.class).constType(),
			CIdentifier.of(this.classIdentifier + "__fields"));
		this.classMethods = CVariable.of(
			JvmTypes.STATIC_CLASS_METHODS.type(CStructType.class).constType(),
			CIdentifier.of(this.classIdentifier + "__methods"));
		
		// Create processors for each field
		Map<FieldNameAndType, FieldProcessor> fields = this._fields;
		for (Field field : __classFile.fields())
			fields.put(field.nameAndType(), new FieldProcessor(__glob,
				this, field));
		
		// Create processors for each method
		Map<MethodNameAndType, MethodProcessor> methods = this._methods;
		for (Method method : __classFile.methods())
			methods.put(method.nameAndType(), new MethodProcessor(__glob,
				this, method));
	}
	
	/**
	 * Processes the class header output.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	protected void processHeader(CSourceWriter __out)
		throws IOException
	{
		ClassFile classFile = this.classFile;
		NanoCoatLinkGlob glob = this.glob;
		
		// Write class identifier, as extern value
		__out.declare(this.classInfo.extern());
		
		// Process field header details
		for (FieldProcessor field : this._fields.values())
			field.processHeader(__out);
		
		// Process method header details
		for (MethodProcessor method : this._methods.values())
			method.processHeader(__out);
	}
	
	/**
	 * Processes the class source output.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/31
	 */
	protected void processSource(CSourceWriter __out)
		throws IOException
	{
		ClassFile classFile = this.classFile;
		
		// Process field source details outside the class struct
		try (CStructVariableBlock struct = __out.define(
			CStructVariableBlock.class, this.classFields))
		{
			// Field count
			struct.memberSet("count",
				CExpressionBuilder.builder()
					.number(Constants.JINT_C, this._fields.size())
					.build());
			
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
			method.processSourceOutside(__out);
		
		// Process method details for method structure
		try (CStructVariableBlock struct = __out.define(
			CStructVariableBlock.class, this.classMethods))
		{
			// Method count
			struct.memberSet("count",
				CExpressionBuilder.builder()
					.number(Constants.JINT_C, this._methods.size())
					.build());
			
			// Then the actual members
			try (CArrayBlock array =
				 struct.memberArraySet("methods"))
			{
				for (MethodProcessor method : this._methods.values())
					method.processInMethodsStruct(array);
			}
		}
		
		// Open class details
		try (CStructVariableBlock struct = __out.define(
			CStructVariableBlock.class, this.classInfo))
		{
			// Class details
			struct.memberSet("thisName",
				CExpressionBuilder.builder()
					.string(classFile.thisName().toString())
					.build());
			if (classFile.superName() != null)
				struct.memberSet("superName",
					CExpressionBuilder.builder()
						.string(classFile.superName().toString())
						.build());
			struct.memberSet("flags",
				CExpressionBuilder.builder()
					.number(Constants.JINT_C, classFile.flags().toJavaBits())
					.build());
			
			// Fields
			struct.memberSet("fields",
				CExpressionBuilder.builder()
					.identifier(this.classFields)
					.build());
			
			// Methods
			struct.memberSet("methods",
				CExpressionBuilder.builder()
					.identifier(this.classMethods)
					.build());
		}
	}
}

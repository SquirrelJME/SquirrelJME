// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
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
		
		// Write class identifier
		out.variableSet(CModifiers.EXTERN_CONST,
			CBasicType.JCLASS, this.classIdentifier);
		
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
		for (FieldProcessor field : this._fields.values())
			field.processSourceOutside();
		
		// Process method source details outside the class struct
		for (MethodProcessor method : this._methods.values())
			method.processSourceOutside();
		
		// Open class details
		try (CStructVariableBlock struct = this.out.structVariableSet(
			CBasicModifier.CONST, CBasicType.JCLASS, this.classIdentifier))
		{
			// Class details
			struct.memberSet("thisName",
				classFile.thisName());
			struct.memberSet("superName",
				classFile.superName());
			struct.memberSet("fields",
				classFile.flags().toJavaBits());
			
			if (true)
				throw Debugging.todo();
			
			// Write fields
			for (FieldProcessor field : this._fields.values())
				field.processSourceInClass(struct);
			
			// Write methods
			for (MethodProcessor method : this._methods.values())
				method.processSourceInClass(struct);
		}
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}

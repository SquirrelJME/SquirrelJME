// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.ConstantValueInteger;
import net.multiphasicapps.classfile.ExceptionHandler;
import net.multiphasicapps.classfile.ExceptionHandlerTable;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;
import net.multiphasicapps.classfile.InstructionJumpTarget;
import net.multiphasicapps.classfile.InstructionJumpTargets;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodHandle;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodReference;
import net.multiphasicapps.classfile.PrimitiveType;
import net.multiphasicapps.classfile.StackMapTable;
import net.multiphasicapps.classfile.StackMapTableState;

/**
 * This is a translator which is designed to run as quick as possible while
 * translating all of the instructions.
 *
 * @since 2019/04/03
 */
public class QuickTranslator
	implements Translator
{
	/** The byte code to translate. */
	protected final ByteCode bytecode;
	
	/** Used to build register codes. */
	protected final RegisterCodeBuilder codebuilder =
		new RegisterCodeBuilder();
	
	/** Exception tracker. */
	protected final __ExceptionTracker__ exceptiontracker;
	
	/** Default field access type, to determine how fields are accessed. */
	protected final FieldAccessTime defaultfieldaccesstime;
	
	/** The stacks which have been recorded. */
	private final Map<Integer, JavaStackState> _stacks =
		new LinkedHashMap<>();
	
	/** The current state of the stack. */
	private JavaStackState _stack;
	
	/**
	 * Converts the input byte code to a register based code.
	 *
	 * @param __bc The byte code to translate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public QuickTranslator(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		this.bytecode = __bc;
		this.exceptiontracker = new __ExceptionTracker__(__bc);
		this.defaultfieldaccesstime = ((__bc.isInstanceInitializer() ||
			__bc.isStaticInitializer()) ? FieldAccessTime.INITIALIZER :
			FieldAccessTime.NORMAL);
			
		// Load initial Java stack state from the initial stack map
		JavaStackState s;
		this._stack = (s = JavaStackState.of(__bc.stackMapTable().get(0),
			__bc.writtenLocals()));
		this._stacks.put(0, s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/03
	 */
	@Override
	public RegisterCode convert()
	{
		throw new todo.TODO();
	}
}


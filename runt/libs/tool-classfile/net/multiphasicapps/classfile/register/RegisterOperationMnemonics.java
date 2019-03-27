// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

/**
 * Mnemonics for register operations.
 *
 * @since 2019/03/17
 */
public final class RegisterOperationMnemonics
{
	/**
	 * Not used.
	 *
	 * @since 2019/03/17
	 */
	private RegisterOperationMnemonics()
	{
	}
	
	/**
	 * Translate the operation to the represented mnemonic.
	 *
	 * @param __op The operation to translate.
	 * @return The string representing the instruction mnemonic.
	 * @since 2019/03/17
	 */
	public static String toString(int __op)
	{
		switch (__op)
		{
			case RegisterOperationType.ARRAY_LOAD_OBJECT:
				return "ARRAY_LOAD_OBJECT";
			case RegisterOperationType.ARRAY_LOAD_X16:
				return "ARRAY_LOAD_X16";
			case RegisterOperationType.ARRAY_LOAD_X32:
				return "ARRAY_LOAD_X32";
			case RegisterOperationType.ARRAY_LOAD_X64:
				return "ARRAY_LOAD_X64";
			case RegisterOperationType.ARRAY_LOAD_X8:
				return "ARRAY_LOAD_X8";
			case RegisterOperationType.ARRAY_STORE_OBJECT:
				return "ARRAY_STORE_OBJECT";
			case RegisterOperationType.ARRAY_STORE_X16:
				return "ARRAY_STORE_X16";
			case RegisterOperationType.ARRAY_STORE_X32:
				return "ARRAY_STORE_X32";
			case RegisterOperationType.ARRAY_STORE_X64:
				return "ARRAY_STORE_X64";
			case RegisterOperationType.ARRAY_STORE_X8:
				return "ARRAY_STORE_X8";
			case RegisterOperationType.CHECKCAST:
				return "CHECKCAST";
			case RegisterOperationType.DCMPG:
				return "DCMPG";
			case RegisterOperationType.DCMPL:
				return "DCMPL";
			case RegisterOperationType.DOUBLE_ADD:
				return "DOUBLE_ADD";
			case RegisterOperationType.DOUBLE_DIV:
				return "DOUBLE_DIV";
			case RegisterOperationType.DOUBLE_MUL:
				return "DOUBLE_MUL";
			case RegisterOperationType.DOUBLE_REM:
				return "DOUBLE_REM";
			case RegisterOperationType.DOUBLE_SUB:
				return "DOUBLE_SUB";
			case RegisterOperationType.DOUBLE_TO_FLOAT:
				return "DOUBLE_TO_FLOAT";
			case RegisterOperationType.DOUBLE_TO_INT:
				return "DOUBLE_TO_INT";
			case RegisterOperationType.DOUBLE_TO_LONG:
				return "DOUBLE_TO_LONG";
			case RegisterOperationType.FCMPG:
				return "FCMPG";
			case RegisterOperationType.FCMPL:
				return "FCMPL";
			case RegisterOperationType.FIELD_IFEQ:
				return "FIELD_IFEQ";
			case RegisterOperationType.FIELD_IFNE:
				return "FIELD_IFNE";
			case RegisterOperationType.FIELD_IFLT:
				return "FIELD_IFLT";
			case RegisterOperationType.FIELD_IFLE:
				return "FIELD_IFLE";
			case RegisterOperationType.FIELD_IFGT:
				return "FIELD_IFGT";
			case RegisterOperationType.FIELD_IFGE:
				return "FIELD_IFGE";
			case RegisterOperationType.FLOAT_ADD:
				return "FLOAT_ADD";
			case RegisterOperationType.FLOAT_DIV:
				return "FLOAT_DIV";
			case RegisterOperationType.FLOAT_MUL:
				return "FLOAT_MUL";
			case RegisterOperationType.FLOAT_REM:
				return "FLOAT_REM";
			case RegisterOperationType.FLOAT_SUB:
				return "FLOAT_SUB";
			case RegisterOperationType.FLOAT_TO_DOUBLE:
				return "FLOAT_TO_DOUBLE";
			case RegisterOperationType.FLOAT_TO_INT:
				return "FLOAT_TO_INT";
			case RegisterOperationType.FLOAT_TO_LONG:
				return "FLOAT_TO_LONG";
			case RegisterOperationType.IFEQ:
				return "IFEQ";
			case RegisterOperationType.IFGE:
				return "IFGE";
			case RegisterOperationType.IFGT:
				return "IFGT";
			case RegisterOperationType.IF_ICMPEQ:
				return "IF_ICMPEQ";
			case RegisterOperationType.IF_ICMPGT:
				return "IF_ICMPGT";
			case RegisterOperationType.IF_ICMPLE:
				return "IF_ICMPLE";
			case RegisterOperationType.IF_ICMPLT:
				return "IF_ICMPLT";
			case RegisterOperationType.IF_ICMPNE:
				return "IF_ICMPNE";
			case RegisterOperationType.IFIELD_LOAD_OBJECT:
				return "IFIELD_LOAD_OBJECT";
			case RegisterOperationType.IFIELD_LOAD_X16:
				return "IFIELD_LOAD_X16";
			case RegisterOperationType.IFIELD_LOAD_X32:
				return "IFIELD_LOAD_X32";
			case RegisterOperationType.IFIELD_LOAD_X64:
				return "IFIELD_LOAD_X64";
			case RegisterOperationType.IFIELD_LOAD_X8:
				return "IFIELD_LOAD_X8";
			case RegisterOperationType.IFIELD_STORE_OBJECT:
				return "IFIELD_STORE_OBJECT";
			case RegisterOperationType.IFIELD_STORE_X16:
				return "IFIELD_STORE_X16";
			case RegisterOperationType.IFIELD_STORE_X32:
				return "IFIELD_STORE_X32";
			case RegisterOperationType.IFIELD_STORE_X64:
				return "IFIELD_STORE_X64";
			case RegisterOperationType.IFIELD_STORE_X8:
				return "IFIELD_STORE_X8";
			case RegisterOperationType.IFLE:
				return "IFLE";
			case RegisterOperationType.IFLT:
				return "IFLT";
			case RegisterOperationType.IFNE:
				return "IFNE";
			case RegisterOperationType.INSTANCEOF:
				return "INSTANCEOF";
			case RegisterOperationType.INT_ADD:
				return "INT_ADD";
			case RegisterOperationType.INT_AND:
				return "INT_AND";
			case RegisterOperationType.INT_DIV:
				return "INT_DIV";
			case RegisterOperationType.INT_MUL:
				return "INT_MUL";
			case RegisterOperationType.INT_OR:
				return "INT_OR";
			case RegisterOperationType.INT_REM:
				return "INT_REM";
			case RegisterOperationType.INT_SHL:
				return "INT_SHL";
			case RegisterOperationType.INT_SHR:
				return "INT_SHR";
			case RegisterOperationType.INT_SUB:
				return "INT_SUB";
			case RegisterOperationType.INT_TO_BYTE:
				return "INT_TO_BYTE";
			case RegisterOperationType.INT_TO_CHAR:
				return "INT_TO_CHAR";
			case RegisterOperationType.INT_TO_DOUBLE:
				return "INT_TO_DOUBLE";
			case RegisterOperationType.INT_TO_FLOAT:
				return "INT_TO_FLOAT";
			case RegisterOperationType.INT_TO_LONG:
				return "INT_TO_LONG";
			case RegisterOperationType.INT_TO_SHORT:
				return "INT_TO_SHORT";
			case RegisterOperationType.INT_USHR:
				return "INT_USHR";
			case RegisterOperationType.INT_XOR:
				return "INT_XOR";
			case RegisterOperationType.INVOKE_METHOD:
				return "INVOKE_METHOD";
			case RegisterOperationType.JUMP:
				return "JUMP";
			case RegisterOperationType.JUMP_IF_EXCEPTION:
				return "JUMP_IF_EXCEPTION";
			case RegisterOperationType.JUMP_IF_INSTANCE:
				return "JUMP_IF_INSTANCE";
			case RegisterOperationType.JUMP_IF_INSTANCE_GET_EXCEPTION:
				return "JUMP_IF_INSTANCE_GET_EXCEPTION";
			case RegisterOperationType.JUMP_IF_RETURN:
				return "JUMP_IF_RETURN";
			case RegisterOperationType.LCMP:
				return "LCMP";
			case RegisterOperationType.LOAD_POOL_VALUE:
				return "LOAD_POOL_VALUE";
			case RegisterOperationType.LONG_ADD:
				return "LONG_ADD";
			case RegisterOperationType.LONG_AND:
				return "LONG_AND";
			case RegisterOperationType.LONG_DIV:
				return "LONG_DIV";
			case RegisterOperationType.LONG_MUL:
				return "LONG_MUL";
			case RegisterOperationType.LONG_OR:
				return "LONG_OR";
			case RegisterOperationType.LONG_REM:
				return "LONG_REM";
			case RegisterOperationType.LONG_SHL:
				return "LONG_SHL";
			case RegisterOperationType.LONG_SHR:
				return "LONG_SHR";
			case RegisterOperationType.LONG_SUB:
				return "LONG_SUB";
			case RegisterOperationType.LONG_TO_DOUBLE:
				return "LONG_TO_DOUBLE";
			case RegisterOperationType.LONG_TO_FLOAT:
				return "LONG_TO_FLOAT";
			case RegisterOperationType.LONG_TO_INT:
				return "LONG_TO_INT";
			case RegisterOperationType.LONG_USHR:
				return "LONG_USHR";
			case RegisterOperationType.LONG_XOR:
				return "LONG_XOR";
			case RegisterOperationType.LOOKUPSWITCH:
				return "LOOKUPSWITCH";
			case RegisterOperationType.NEW:
				return "NEW";
			case RegisterOperationType.NEW_ARRAY:
				return "NEW_ARRAY";
			case RegisterOperationType.NEW_ARRAY_CONST:
				return "NEW_ARRAY_CONST";
			case RegisterOperationType.NOP:
				return "NOP";
			case RegisterOperationType.OBJECT_COPY:
				return "OBJECT_COPY";
			case RegisterOperationType.OBJECT_FIELD_LOAD:
				return "OBJECT_FIELD_LOAD";
			case RegisterOperationType.OBJECT_FIELD_STORE:
				return "OBJECT_FIELD_STORE";
			case RegisterOperationType.RETURN:
				return "RETURN";
			case RegisterOperationType.SFIELD_LOAD_OBJECT:
				return "SFIELD_LOAD_OBJECT";
			case RegisterOperationType.SFIELD_LOAD_X16:
				return "SFIELD_LOAD_X16";
			case RegisterOperationType.SFIELD_LOAD_X32:
				return "SFIELD_LOAD_X32";
			case RegisterOperationType.SFIELD_LOAD_X64:
				return "SFIELD_LOAD_X64";
			case RegisterOperationType.SFIELD_LOAD_X8:
				return "SFIELD_LOAD_X8";
			case RegisterOperationType.SFIELD_STORE_OBJECT:
				return "SFIELD_STORE_OBJECT";
			case RegisterOperationType.SFIELD_STORE_X16:
				return "SFIELD_STORE_X16";
			case RegisterOperationType.SFIELD_STORE_X32:
				return "SFIELD_STORE_X32";
			case RegisterOperationType.SFIELD_STORE_X64:
				return "SFIELD_STORE_X64";
			case RegisterOperationType.SFIELD_STORE_X8:
				return "SFIELD_STORE_X8";
			case RegisterOperationType.TABLESWITCH:
				return "TABLESWITCH";
			case RegisterOperationType.X32_CONST:
				return "X32_CONST";
			case RegisterOperationType.X32_COPY:
				return "X32_COPY";
			case RegisterOperationType.X32_FIELD_LOAD:
				return "X32_FIELD_LOAD";
			case RegisterOperationType.X32_FIELD_STORE:
				return "X32_FIELD_STORE";
			case RegisterOperationType.X64_CONST:
				return "X64_CONST";
			case RegisterOperationType.X64_COPY:
				return "X64_COPY";
			case RegisterOperationType.X64_FIELD_LOAD:
				return "X64_FIELD_LOAD";
			case RegisterOperationType.X64_FIELD_STORE:
				return "X64_FIELD_STORE";
			
				// Unknown
			default:
				return String.format("UNKNOWN_%02X", __op);
		}
	}
}


// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

/**
 * Utilities for Java op-codes.
 *
 * @since 2020/06/13
 */
public final class JavaOpCodeUtils
{
	/**
	 * Not used.
	 *
	 * @since 2020/06/13
	 */
	private JavaOpCodeUtils()
	{
	}
	
	/**
	 * Checks if the instruction is an invoke.
	 * 
	 * @param __code The opcode.
	 * @return If it is a method call.
	 * @since 2020/07/11
	 */
	@SuppressWarnings("MagicNumber")
	public static boolean isInvoke(int __code)
	{
		return (__code >= 182 && __code <= 186);
	}
	
	/**
	 * Converts the op-code to a string.
	 *
	 * @param __code The opcode.
	 * @return The string representation of the code.
	 * @since 2020/06/13
	 */
	@SuppressWarnings("MagicNumber")
	public static String toString(int __code)
	{
		// Wide codes
		switch (__code & 0xFFFF)
		{
			case 50197:	return "WIDE_ILOAD";
			case 50198:	return "WIDE_LLOAD";
			case 50199:	return "WIDE_FLOAD";
			case 50200:	return "WIDE_DLOAD";
			case 50201:	return "WIDE_ALOAD";
			case 50230:	return "WIDE_ISTORE";
			case 50231:	return "WIDE_LSTORE";
			case 50232:	return "WIDE_FSTORE";
			case 50233:	return "WIDE_DSTORE";
			case 50234:	return "WIDE_ASTORE";
			case 50308:	return "WIDE_IINC";
		}
		
		// Narrow codes
		switch (__code & 0xFF)
		{
			case 0:		return "NOP";
			case 1:		return "ACONST_NULL";
			case 2:		return "ICONST_M1";
			case 3:		return "ICONST_0";
			case 4:		return "ICONST_1";
			case 5:		return "ICONST_2";
			case 6:		return "ICONST_3";
			case 7:		return "ICONST_4";
			case 8:		return "ICONST_5";
			case 9:		return "LCONST_0";
			case 10:	return "LCONST_1";
			case 11:	return "FCONST_0";
			case 12:	return "FCONST_1";
			case 13:	return "FCONST_2";
			case 14:	return "DCONST_0";
			case 15:	return "DCONST_1";
			case 16:	return "BIPUSH";
			case 17:	return "SIPUSH";
			case 18:	return "LDC";
			case 19:	return "LDC_W";
			case 20:	return "LDC2_W";
			case 21:	return "ILOAD";
			case 22:	return "LLOAD";
			case 23:	return "FLOAD";
			case 24:	return "DLOAD";
			case 25:	return "ALOAD";
			case 26:	return "ILOAD_0";
			case 27:	return "ILOAD_1";
			case 28:	return "ILOAD_2";
			case 29:	return "ILOAD_3";
			case 30:	return "LLOAD_0";
			case 31:	return "LLOAD_1";
			case 32:	return "LLOAD_2";
			case 33:	return "LLOAD_3";
			case 34:	return "FLOAD_0";
			case 35:	return "FLOAD_1";
			case 36:	return "FLOAD_2";
			case 37:	return "FLOAD_3";
			case 38:	return "DLOAD_0";
			case 39:	return "DLOAD_1";
			case 40:	return "DLOAD_2";
			case 41:	return "DLOAD_3";
			case 42:	return "ALOAD_0";
			case 43:	return "ALOAD_1";
			case 44:	return "ALOAD_2";
			case 45:	return "ALOAD_3";
			case 46:	return "IALOAD";
			case 47:	return "LALOAD";
			case 48:	return "FALOAD";
			case 49:	return "DALOAD";
			case 50:	return "AALOAD";
			case 51:	return "BALOAD";
			case 52:	return "CALOAD";
			case 53:	return "SALOAD";
			case 54:	return "ISTORE";
			case 55:	return "LSTORE";
			case 56:	return "FSTORE";
			case 57:	return "DSTORE";
			case 58:	return "ASTORE";
			case 59:	return "ISTORE_0";
			case 60:	return "ISTORE_1";
			case 61:	return "ISTORE_2";
			case 62:	return "ISTORE_3";
			case 63:	return "LSTORE_0";
			case 64:	return "LSTORE_1";
			case 65:	return "LSTORE_2";
			case 66:	return "LSTORE_3";
			case 67:	return "FSTORE_0";
			case 68:	return "FSTORE_1";
			case 69:	return "FSTORE_2";
			case 70:	return "FSTORE_3";
			case 71:	return "DSTORE_0";
			case 72:	return "DSTORE_1";
			case 73:	return "DSTORE_2";
			case 74:	return "DSTORE_3";
			case 75:	return "ASTORE_0";
			case 76:	return "ASTORE_1";
			case 77:	return "ASTORE_2";
			case 78:	return "ASTORE_3";
			case 79:	return "IASTORE";
			case 80:	return "LASTORE";
			case 81:	return "FASTORE";
			case 82:	return "DASTORE";
			case 83:	return "AASTORE";
			case 84:	return "BASTORE";
			case 85:	return "CASTORE";
			case 86:	return "SASTORE";
			case 87:	return "POP";
			case 88:	return "POP2";
			case 89:	return "DUP";
			case 90:	return "DUP_X1";
			case 91:	return "DUP_X2";
			case 92:	return "DUP2";
			case 93:	return "DUP2_X1";
			case 94:	return "DUP2_X2";
			case 95:	return "SWAP";
			case 96:	return "IADD";
			case 97:	return "LADD";
			case 98:	return "FADD";
			case 99:	return "DADD";
			case 100:	return "ISUB";
			case 101:	return "LSUB";
			case 102:	return "FSUB";
			case 103:	return "DSUB";
			case 104:	return "IMUL";
			case 105:	return "LMUL";
			case 106:	return "FMUL";
			case 107:	return "DMUL";
			case 108:	return "IDIV";
			case 109:	return "LDIV";
			case 110:	return "FDIV";
			case 111:	return "DDIV";
			case 112:	return "IREM";
			case 113:	return "LREM";
			case 114:	return "FREM";
			case 115:	return "DREM";
			case 116:	return "INEG";
			case 117:	return "LNEG";
			case 118:	return "FNEG";
			case 119:	return "DNEG";
			case 120:	return "ISHL";
			case 121:	return "LSHL";
			case 122:	return "ISHR";
			case 123:	return "LSHR";
			case 124:	return "IUSHR";
			case 125:	return "LUSHR";
			case 126:	return "IAND";
			case 127:	return "LAND";
			case 128:	return "IOR";
			case 129:	return "LOR";
			case 130:	return "IXOR";
			case 131:	return "LXOR";
			case 132:	return "IINC";
			case 133:	return "I2L";
			case 134:	return "I2F";
			case 135:	return "I2D";
			case 136:	return "L2I";
			case 137:	return "L2F";
			case 138:	return "L2D";
			case 139:	return "F2I";
			case 140:	return "F2L";
			case 141:	return "F2D";
			case 142:	return "D2I";
			case 143:	return "D2L";
			case 144:	return "D2F";
			case 145:	return "I2B";
			case 146:	return "I2C";
			case 147:	return "I2S";
			case 148:	return "LCMP";
			case 149:	return "FCMPL";
			case 150:	return "FCMPG";
			case 151:	return "DCMPL";
			case 152:	return "DCMPG";
			case 153:	return "IFEQ";
			case 154:	return "IFNE";
			case 155:	return "IFLT";
			case 156:	return "IFGE";
			case 157:	return "IFGT";
			case 158:	return "IFLE";
			case 159:	return "IF_ICMPEQ";
			case 160:	return "IF_ICMPNE";
			case 161:	return "IF_ICMPLT";
			case 162:	return "IF_ICMPGE";
			case 163:	return "IF_ICMPGT";
			case 164:	return "IF_ICMPLE";
			case 165:	return "IF_ACMPEQ";
			case 166:	return "IF_ACMPNE";
			case 167:	return "GOTO";
			case 168:	return "JSR";
			case 169:	return "RET";
			case 170:	return "TABLESWITCH";
			case 171:	return "LOOKUPSWITCH";
			case 172:	return "IRETURN";
			case 173:	return "LRETURN";
			case 174:	return "FRETURN";
			case 175:	return "DRETURN";
			case 176:	return "ARETURN";
			case 177:	return "RETURN";
			case 178:	return "GETSTATIC";
			case 179:	return "PUTSTATIC";
			case 180:	return "GETFIELD";
			case 181:	return "PUTFIELD";
			case 182:	return "INVOKEVIRTUAL";
			case 183:	return "INVOKESPECIAL";
			case 184:	return "INVOKESTATIC";
			case 185:	return "INVOKEINTERFACE";
			case 186:	return "INVOKEDYNAMIC";
			case 187:	return "NEW";
			case 188:	return "NEWARRAY";
			case 189:	return "ANEWARRAY";
			case 190:	return "ARRAYLENGTH";
			case 191:	return "ATHROW";
			case 192:	return "CHECKCAST";
			case 193:	return "INSTANCEOF";
			case 194:	return "MONITORENTER";
			case 195:	return "MONITOREXIT";
			case 196:	return "WIDE";
			case 197:	return "MULTIANEWARRAY";
			case 198:	return "IFNULL";
			case 199:	return "IFNONNULL";
			case 200:	return "GOTO_W";
			case 201:	return "JSR_W";
			case 202:	return "BREAKPOINT";
			case 254:	return "IMPDEP1";
			case 255:	return "IMPDEP2";
		}
		
		return "UNKNOWN";
	}
}

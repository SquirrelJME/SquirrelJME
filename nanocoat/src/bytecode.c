/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/bytecode.h"
#include "sjme/debug.h"
#include "sjme/nvm/bytecodeSlow.h"
#include "sjme/nvm/bytecodeFast.h"

const sjme_jbyte sjme_nvm_byteCode_lengths[SJME_NVM_NUM_JAVA_BYTECODES] =
{
	/* ..0 */ 1, /* Constants. */
	/* ..1 */ 1,
	/* ..2 */ 1,
	/* ..3 */ 1,
	/* ..4 */ 1,
	/* ..5 */ 1,
	/* ..6 */ 1,
	/* ..7 */ 1,
	/* ..8 */ 1,
	/* ..9 */ 1,
	/* .10 */ 1,
	/* .11 */ 1,
	/* .12 */ 1,
	/* .13 */ 1,
	/* .14 */ 1,
	/* .15 */ 1,
	/* .16 */ 2, /* bipush */
	/* .17 */ 3, /* sipush */
	/* .18 */ 2,
	/* .19 */ 3,
	/* .20 */ 3,
	/* .21 */ 2, /* Loads */
	/* .22 */ 2,
	/* .23 */ 2,
	/* .24 */ 2,
	/* .25 */ 2,
	/* .26 */ 1,
	/* .27 */ 1,
	/* .28 */ 1,
	/* .29 */ 1,
	/* .30 */ 1,
	/* .31 */ 1,
	/* .32 */ 1,
	/* .33 */ 1,
	/* .34 */ 1,
	/* .35 */ 1,
	/* .36 */ 1,
	/* .37 */ 1,
	/* .38 */ 1,
	/* .39 */ 1,
	/* .40 */ 1,
	/* .41 */ 1,
	/* .42 */ 1,
	/* .43 */ 1,
	/* .44 */ 1,
	/* .45 */ 1,
	/* .46 */ 1,
	/* .47 */ 1,
	/* .48 */ 1,
	/* .49 */ 1,
	/* .50 */ 1,
	/* .51 */ 1,
	/* .52 */ 1,
	/* .53 */ 1,
	/* .54 */ 2, /* Stores. */
	/* .55 */ 2,
	/* .56 */ 2,
	/* .57 */ 2,
	/* .58 */ 2,
	/* .59 */ 1,
	/* .60 */ 1,
	/* .61 */ 1,
	/* .62 */ 1,
	/* .63 */ 1,
	/* .64 */ 1,
	/* .65 */ 1,
	/* .66 */ 1,
	/* .67 */ 1,
	/* .68 */ 1,
	/* .69 */ 1,
	/* .70 */ 1,
	/* .71 */ 1,
	/* .72 */ 1,
	/* .73 */ 1,
	/* .74 */ 1,
	/* .75 */ 1,
	/* .76 */ 1,
	/* .77 */ 1,
	/* .78 */ 1,
	/* .79 */ 1,
	/* .80 */ 1,
	/* .81 */ 1,
	/* .82 */ 1,
	/* .83 */ 1,
	/* .84 */ 1,
	/* .85 */ 1,
	/* .86 */ 1,
	/* .87 */ 1, /* Stack. */
	/* .88 */ 1,
	/* .89 */ 1,
	/* .90 */ 1,
	/* .91 */ 1,
	/* .92 */ 1,
	/* .93 */ 1,
	/* .94 */ 1,
	/* .95 */ 1,
	/* .96 */ 1, /* Math. */
	/* .97 */ 1,
	/* .98 */ 1,
	/* .99 */ 1,
	/* 100 */ 1,
	/* 101 */ 1,
	/* 102 */ 1,
	/* 103 */ 1,
	/* 104 */ 1,
	/* 105 */ 1,
	/* 106 */ 1,
	/* 107 */ 1,
	/* 108 */ 1,
	/* 109 */ 1,
	/* 110 */ 1,
	/* 111 */ 1,
	/* 112 */ 1,
	/* 113 */ 1,
	/* 114 */ 1,
	/* 115 */ 1,
	/* 116 */ 1,
	/* 117 */ 1,
	/* 118 */ 1,
	/* 119 */ 1,
	/* 120 */ 1,
	/* 121 */ 1,
	/* 122 */ 1,
	/* 123 */ 1,
	/* 124 */ 1,
	/* 125 */ 1,
	/* 126 */ 1,
	/* 127 */ 1,
	/* 128 */ 1,
	/* 129 */ 1,
	/* 130 */ 1,
	/* 131 */ 1,
	/* 132 */ 3,
	/* 133 */ 1, /* Conversions. */
	/* 134 */ 1,
	/* 135 */ 1,
	/* 136 */ 1,
	/* 137 */ 1,
	/* 138 */ 1,
	/* 139 */ 1,
	/* 140 */ 1,
	/* 141 */ 1,
	/* 142 */ 1,
	/* 143 */ 1,
	/* 144 */ 1,
	/* 145 */ 1,
	/* 146 */ 1,
	/* 147 */ 1,
	/* 148 */ 1, /* Comparisons. */
	/* 149 */ 1,
	/* 150 */ 1,
	/* 151 */ 1,
	/* 152 */ 1,
	/* 153 */ 3, /* if.. */
	/* 154 */ 3,
	/* 155 */ 3,
	/* 156 */ 3,
	/* 157 */ 3,
	/* 158 */ 3,
	/* 159 */ 3, /* if_icmp.. */
	/* 160 */ 3,
	/* 161 */ 3,
	/* 162 */ 3,
	/* 163 */ 3,
	/* 164 */ 3,
	/* 165 */ 3,
	/* 166 */ 3,
	/* 167 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_3, /* Control. */
	/* 168 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 169 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 170 */ SJME_NVM_BYTECODE_LENGTH_TABLESWITCH,
	/* 171 */ SJME_NVM_BYTECODE_LENGTH_LOOKUPSWITCH,
	/* 172 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1,
	/* 173 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1,
	/* 174 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1,
	/* 175 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1,
	/* 176 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1,
	/* 177 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1,
	/* 178 */ 3, /* References. */
	/* 179 */ 3,
	/* 180 */ 3,
	/* 181 */ 3,
	/* 182 */ 3,
	/* 183 */ 3,
	/* 184 */ 3,
	/* 185 */ 5, /* @c invokeinterface */
	/* 186 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 187 */ 3,
	/* 188 */ 2,
	/* 189 */ 3,
	/* 190 */ 1,
	/* 191 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1,
	/* 192 */ 3,
	/* 193 */ 3,
	/* 194 */ 1,
	/* 195 */ 1,
	/* 196 */ SJME_NVM_BYTECODE_LENGTH_WIDE, /* Extended. */
	/* 197 */ 4,
	/* 198 */ 3,
	/* 199 */ 3,
	/* 200 */ SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_5,
	/* 201 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 202 */ SJME_NVM_BYTECODE_LENGTH_INVALID, /* Reserved. */
	/* 203 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 204 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 205 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 206 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 207 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 208 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 209 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 210 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 211 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 212 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 213 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 214 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 215 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 216 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 217 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 218 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 219 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 220 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 221 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 222 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 223 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 224 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 225 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 226 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 227 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 228 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 229 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 230 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 231 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 232 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 233 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 234 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 235 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 236 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 237 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 238 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 239 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 240 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 241 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 242 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 243 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 244 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 245 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 246 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 247 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 248 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 249 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 250 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 251 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 252 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 253 */ SJME_NVM_BYTECODE_LENGTH_INVALID,
	/* 254 */ SJME_NVM_BYTECODE_LENGTH_FAST_1,
	/* 255 */ SJME_NVM_BYTECODE_LENGTH_FAST_1,
};

const sjme_lpcstr sjme_nvm_byteCode_names[SJME_NVM_NUM_JAVA_BYTECODES] =
{
	/* ..0 */ "NOP", /* Constants. */
	/* ..1 */ "ACONST_NULL",
	/* ..2 */ "ICONST_M1",
	/* ..3 */ "ICONST_0",
	/* ..4 */ "ICONST_1",
	/* ..5 */ "ICONST_2",
	/* ..6 */ "ICONST_3",
	/* ..7 */ "ICONST_4",
	/* ..8 */ "ICONST_5",
	/* ..9 */ "LCONST_0",
	/* .10 */ "LCONST_1",
	/* .11 */ "FCONST_0",
	/* .12 */ "FCONST_1",
	/* .13 */ "FCONST_2",
	/* .14 */ "DCONST_0",
	/* .15 */ "DCONST_1",
	/* .16 */ "BIPUSH", /* bipush */
	/* .17 */ "SIPUSH", /* sipush */
	/* .18 */ "LDC",
	/* .19 */ "LDC_W",
	/* .20 */ "LDC2_W",
	/* .21 */ "ILOAD", /* Loads */
	/* .22 */ "LLOAD",
	/* .23 */ "FLOAD",
	/* .24 */ "DLOAD",
	/* .25 */ "ALOAD",
	/* .26 */ "ILOAD_0",
	/* .27 */ "ILOAD_1",
	/* .28 */ "ILOAD_2",
	/* .29 */ "ILOAD_3",
	/* .30 */ "LLOAD_0",
	/* .31 */ "LLOAD_1",
	/* .32 */ "LLOAD_2",
	/* .33 */ "LLOAD_3",
	/* .34 */ "FLOAD_0",
	/* .35 */ "FLOAD_1",
	/* .36 */ "FLOAD_2",
	/* .37 */ "FLOAD_3",
	/* .38 */ "DLOAD_0",
	/* .39 */ "DLOAD_1",
	/* .40 */ "DLOAD_2",
	/* .41 */ "DLOAD_3",
	/* .42 */ "ALOAD_0",
	/* .43 */ "ALOAD_1",
	/* .44 */ "ALOAD_2",
	/* .45 */ "ALOAD_3",
	/* .46 */ "IALOAD",
	/* .47 */ "LALOAD",
	/* .48 */ "FALOAD",
	/* .49 */ "DALOAD",
	/* .50 */ "AALOAD",
	/* .51 */ "BALOAD",
	/* .52 */ "CALOAD",
	/* .53 */ "SALOAD",
	/* .54 */ "ISTORE", /* Stores. */
	/* .55 */ "LSTORE",
	/* .56 */ "FSTORE",
	/* .57 */ "DSTORE",
	/* .58 */ "ASTORE",
	/* .59 */ "ISTORE_0",
	/* .60 */ "ISTORE_1",
	/* .61 */ "ISTORE_2",
	/* .62 */ "ISTORE_3",
	/* .63 */ "LSTORE_0",
	/* .64 */ "LSTORE_1",
	/* .65 */ "LSTORE_2",
	/* .66 */ "LSTORE_3",
	/* .67 */ "FSTORE_0",
	/* .68 */ "FSTORE_1",
	/* .69 */ "FSTORE_2",
	/* .70 */ "FSTORE_3",
	/* .71 */ "DSTORE_0",
	/* .72 */ "DSTORE_1",
	/* .73 */ "DSTORE_2",
	/* .74 */ "DSTORE_3",
	/* .75 */ "ASTORE_0",
	/* .76 */ "ASTORE_1",
	/* .77 */ "ASTORE_2",
	/* .78 */ "ASTORE_3",
	/* .79 */ "IASTORE",
	/* .80 */ "LASTORE",
	/* .81 */ "FASTORE",
	/* .82 */ "DASTORE",
	/* .83 */ "AASTORE",
	/* .84 */ "BASTORE",
	/* .85 */ "CASTORE",
	/* .86 */ "SASTORE",
	/* .87 */ "POP", /* Stack. */
	/* .88 */ "POP2",
	/* .89 */ "DUP",
	/* .90 */ "DUP_X1",
	/* .91 */ "DUP_X2",
	/* .92 */ "DUP2",
	/* .93 */ "DUP2_X1",
	/* .94 */ "DUP2_X2",
	/* .95 */ "SWAP",
	/* .96 */ "IADD", /* Math. */
	/* .97 */ "LADD",
	/* .98 */ "FADD",
	/* .99 */ "DADD",
	/* 100 */ "ISUB",
	/* 101 */ "LSUB",
	/* 102 */ "FSUB",
	/* 103 */ "DSUB",
	/* 104 */ "IMUL",
	/* 105 */ "LMUL",
	/* 106 */ "FMUL",
	/* 107 */ "DMUL",
	/* 108 */ "IDIV",
	/* 109 */ "LDIV",
	/* 110 */ "FDIV",
	/* 111 */ "DDIV",
	/* 112 */ "IREM",
	/* 113 */ "LREM",
	/* 114 */ "FREM",
	/* 115 */ "DREM",
	/* 116 */ "INEG",
	/* 117 */ "LNEG",
	/* 118 */ "FNEG",
	/* 119 */ "DNREG",
	/* 120 */ "ISHL",
	/* 121 */ "LSHL",
	/* 122 */ "ISHR",
	/* 123 */ "LSHR",
	/* 124 */ "IUSHR",
	/* 125 */ "LUSHR",
	/* 126 */ "IAND",
	/* 127 */ "LAND",
	/* 128 */ "IOR",
	/* 129 */ "LOR",
	/* 130 */ "IXOR",
	/* 131 */ "LXOR",
	/* 132 */ "IINC",
	/* 133 */ "I2L", /* Conversions. */
	/* 134 */ "I2F",
	/* 135 */ "I2D",
	/* 136 */ "L2I",
	/* 137 */ "L2F",
	/* 138 */ "L2D",
	/* 139 */ "F2I",
	/* 140 */ "F2L",
	/* 141 */ "F2D",
	/* 142 */ "D2I",
	/* 143 */ "D2L",
	/* 144 */ "D2F",
	/* 145 */ "I2B",
	/* 146 */ "I2C",
	/* 147 */ "I2S",
	/* 148 */ "LCMP", /* Comparisons. */
	/* 149 */ "FCMPL",
	/* 150 */ "FCMPG",
	/* 151 */ "DCMPL",
	/* 152 */ "DCMPG",
	/* 153 */ "IFEQ", /* if.. */
	/* 154 */ "IFNE",
	/* 155 */ "IFLT",
	/* 156 */ "IFGE",
	/* 157 */ "IFGT",
	/* 158 */ "IFLE",
	/* 159 */ "IF_ICMPEQ", /* if_icmp.. */
	/* 160 */ "IF_ICMPNE",
	/* 161 */ "IF_ICMPLT",
	/* 162 */ "IF_ICMPGE",
	/* 163 */ "IF_ICMPGT",
	/* 164 */ "IF_ICMPLE",
	/* 165 */ "IF_ACMPEQ",
	/* 166 */ "IF_ACMPNE",
	/* 167 */ "GOTO", /* Control. */
	/* 168 */ "168?",
	/* 169 */ "169?",
	/* 170 */ "TABLESWITCH",
	/* 171 */ "LOOKUPSWITCH",
	/* 172 */ "IRETURN",
	/* 173 */ "LRETURN",
	/* 174 */ "FRETURN",
	/* 175 */ "DRETURN",
	/* 176 */ "ARETURN",
	/* 177 */ "RETURN",
	/* 178 */ "GETSTATIC", /* References. */
	/* 179 */ "PUTSTATIC",
	/* 180 */ "GETFIELD",
	/* 181 */ "PUTFIELD",
	/* 182 */ "INVOKEVIRTUAL",
	/* 183 */ "INVOKESPECIAL",
	/* 184 */ "INVOKESTATIC",
	/* 185 */ "INVOKEINTERFACE", /* @c invokeinterface */
	/* 186 */ "186?",
	/* 187 */ "NEW",
	/* 188 */ "NEWARRAY",
	/* 189 */ "ANEWARRAY",
	/* 190 */ "ARRAYLENGTH",
	/* 191 */ "ATHROW",
	/* 192 */ "CHECKCAST",
	/* 193 */ "INSTANCEOF",
	/* 194 */ "MONITORENTER",
	/* 195 */ "MONITOREXIT",
	/* 196 */ "WIDE", /* Extended. */
	/* 197 */ "MULTIANEWARRAY",
	/* 198 */ "IFNULL",
	/* 199 */ "IFNONNULL",
	/* 200 */ "GOTO_W",
	/* 201 */ "201?",
	/* 202 */ "202?", /* Reserved. */
	/* 203 */ "203?",
	/* 204 */ "204?",
	/* 205 */ "205?",
	/* 206 */ "206?",
	/* 207 */ "207?",
	/* 208 */ "208?",
	/* 209 */ "209?",
	/* 210 */ "210?",
	/* 211 */ "211?",
	/* 212 */ "212?",
	/* 213 */ "213?",
	/* 214 */ "214?",
	/* 215 */ "215?",
	/* 216 */ "216?",
	/* 217 */ "217?",
	/* 218 */ "218?",
	/* 219 */ "219?",
	/* 220 */ "220?",
	/* 221 */ "221?",
	/* 222 */ "222?",
	/* 223 */ "223?",
	/* 224 */ "224?",
	/* 225 */ "225?",
	/* 226 */ "226?",
	/* 227 */ "227?",
	/* 228 */ "228?",
	/* 229 */ "229?",
	/* 230 */ "230?",
	/* 231 */ "231?",
	/* 232 */ "232?",
	/* 233 */ "233?",
	/* 234 */ "234?",
	/* 235 */ "235?",
	/* 236 */ "236?",
	/* 237 */ "237?",
	/* 238 */ "238?",
	/* 239 */ "239?",
	/* 240 */ "240?",
	/* 241 */ "241?",
	/* 242 */ "242?",
	/* 243 */ "243?",
	/* 244 */ "244?",
	/* 245 */ "245?",
	/* 246 */ "246?",
	/* 247 */ "247?",
	/* 248 */ "SJME_DUP2X1NARROW",
	/* 249 */ "SJME_DUP2X1WIDE",
	/* 250 */ "SJME_DUP2NARROW",
	/* 251 */ "SJME_DUPWIDE",
	/* 252 */ "SJME_DUPX2NARROW",
	/* 253 */ "SJME_DUPX1WIDE",
	/* 254 */ "SJME_POP2NARROW",
	/* 255 */ "SJME_POPWIDE",
};

const sjme_nvm_byteCode_lutTableType* sjme_nvm_byteCodeLutTable(
	sjme_attrInRange(0, 256) sjme_byteCode id)
{
	/* Slow variants. */
	if ((/*id >= 0 &&*/ id <= 167) ||
		(id >= 170 && id <= 185) ||
		(id >= 187 && id <= 200))
		return &sjme_nvm_byteCode_slowNarrowFunctions;

	/* Fast variants, inline optimized. */
	else if ((id >= 168 && id <= 169) ||
		id == 186 ||
		(id >= 201 /*&& id <= 255*/))
		return &sjme_nvm_byteCode_fastFunctions;

	/* Invalid. */
	return NULL;
}

sjme_errorCode sjme_nvm_byteCode_calcLength(
	sjme_attrInNullable sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* ev,
	sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew)
{
	sjme_jint hi, lo, count, padding;
	
	if (ev == NULL || pcNew == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	switch (pcNew->adjust)
	{
			/* Fixed size, but no return. */
		case SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1:
		case SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_2:
		case SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_3:
		case SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_4:
		case SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_5:
			pcNew->adjust = ((-pcNew->adjust) -
				(-SJME_NVM_BYTECODE_LENGTH_NO_DEFAULT_1)) + 1;
			break;

			/* Fixed size, but fast. */
		case SJME_NVM_BYTECODE_LENGTH_FAST_1:
		case SJME_NVM_BYTECODE_LENGTH_FAST_2:
		case SJME_NVM_BYTECODE_LENGTH_FAST_3:
		case SJME_NVM_BYTECODE_LENGTH_FAST_4:
		case SJME_NVM_BYTECODE_LENGTH_FAST_5:
			pcNew->adjust = ((-pcNew->adjust) -
				(-SJME_NVM_BYTECODE_LENGTH_FAST_1)) + 1;
			break;
		
		case SJME_NVM_BYTECODE_LENGTH_LOOKUPSWITCH:
			/* Skip padding. */
			padding = ((inFrame->pc + 4) & (~3)) - inFrame->pc;

			/* Read pair count. */
			count = sjme_big_int(*sjme_util_memUnaligned32(&ev[padding + 4]));
			if (count < 0)
				return SJME_ERROR_INVALID_INSTRUCTION;

			/* Calculate offset of default branch. */
			pcNew->adjust = padding + 8 + (count * 8);
			break;
			
		case SJME_NVM_BYTECODE_LENGTH_TABLESWITCH:
			/* Skip padding. */
			padding = ((inFrame->pc + 4) & (~3)) - inFrame->pc;
			
			/* Read high and low values. */
			lo = sjme_big_int(*sjme_util_memUnaligned32(&ev[padding + 4]));
			hi = sjme_big_int(*sjme_util_memUnaligned32(&ev[padding + 8]));
			count = ((hi - lo) + 1);
			if (lo > hi || count <= 0)
				return SJME_ERROR_INVALID_INSTRUCTION;
			
			/* Calculate offset of default branch. */
			pcNew->adjust = padding + 12 + (count * 4);
			break;
		
		case SJME_NVM_BYTECODE_LENGTH_WIDE:
			switch (ev[1])
			{
				case SJME_NVM_BYTECODE_JAVA_ILOAD:
				case SJME_NVM_BYTECODE_JAVA_LLOAD:
				case SJME_NVM_BYTECODE_JAVA_FLOAD:
				case SJME_NVM_BYTECODE_JAVA_DLOAD:
				case SJME_NVM_BYTECODE_JAVA_ALOAD:
				case SJME_NVM_BYTECODE_JAVA_ISTORE:
				case SJME_NVM_BYTECODE_JAVA_LSTORE:
				case SJME_NVM_BYTECODE_JAVA_FSTORE:
				case SJME_NVM_BYTECODE_JAVA_DSTORE:
				case SJME_NVM_BYTECODE_JAVA_ASTORE:
					pcNew->adjust = 4;
					break;
				
				case SJME_NVM_BYTECODE_JAVA_WIDE:
					pcNew->adjust = 6;
					break;

				default:
					return SJME_ERROR_INVALID_ARGUMENT;
			}
			break;
		
			/* Invalid? */
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_jboolean sjme_nvm_byteCode_checkRecycleR(
	sjme_attrInNullable sjme_nvm_frame basisFrame)
{
	sjme_nvm_thread inThread;
	
	if (basisFrame == NULL)
		return SJME_JNI_FALSE;

	/* This occurs if this is no longer the top-most frame, this will */
	/* happen for example if a static constructor was called. */
	inThread = SJME_F_T(basisFrame);
	return basisFrame != inThread->frames->elements[inThread->numFrames - 1];
}

sjme_errorCode sjme_nvm_byteCode_illegalInstruction(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew)
{
	if (inFrame == NULL || relRawCode == NULL || pcNew == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_message("ILLEGAL INSTRUCTION %d at 0x%x",
		id, relRawCode - inFrame->inCode->rawCode);
	sjme_nvm_task_stackTraceThread(SJME_F_T(inFrame));
	sjme_message_hexDump(inFrame->inCode->rawCode,
		inFrame->inCode->rawCodeLen);
	return SJME_ERROR_INVALID_INSTRUCTION;
}

sjme_errorCode sjme_nvm_byteCode_notImplemented(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInRange(0, 256) sjme_byteCode id,
	sjme_attrInNotNull sjme_byteCode* relRawCode,
	sjme_attrInNotNull sjme_nvm_byteCode_pcNew* pcNew)
{
	if (inFrame == NULL || relRawCode == NULL || pcNew == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_nvm_task_stackTraceThread(SJME_F_T(inFrame));
	sjme_todo("Impl? %d", relRawCode[0]);
	return sjme_error_notImplemented(relRawCode[0]);
}

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

const sjme_nvm_byteCode_func (*sjme_nvm_byteCode_lutTable
	[SJME_NVM_NUM_JAVA_BYTECODES])[SJME_NVM_NUM_JAVA_BYTECODES] =
{
	/* ..0 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..1 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..2 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..3 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..4 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..5 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..6 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..7 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..8 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* ..9 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .10 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .11 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .12 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .13 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .14 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .15 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .16 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .17 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .18 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .19 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .20 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .21 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .22 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .23 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .24 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .25 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .26 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .27 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .28 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .29 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .30 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .31 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .32 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .33 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .34 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .35 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .36 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .37 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .38 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .39 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .40 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .41 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .42 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .43 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .44 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .45 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .46 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .47 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .48 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .49 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .50 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .51 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .52 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .53 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .54 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .55 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .56 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .57 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .58 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .59 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .60 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .61 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .62 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .63 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .64 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .65 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .66 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .67 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .68 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .69 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .70 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .71 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .72 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .73 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .74 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .75 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .76 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .77 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .78 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .79 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .80 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .81 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .82 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .83 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .84 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .85 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .86 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .87 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .88 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .89 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .90 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .91 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .92 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .93 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .94 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .95 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .96 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .97 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .98 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* .99 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 100 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 101 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 102 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 103 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 104 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 105 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 106 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 107 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 108 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 109 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 110 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 111 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 112 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 113 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 114 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 115 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 116 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 117 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 118 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 119 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 120 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 121 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 122 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 123 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 124 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 125 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 126 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 127 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 128 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 129 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 130 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 131 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 132 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 133 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 134 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 135 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 136 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 137 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 138 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 139 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 140 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 141 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 142 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 143 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 144 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 145 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 146 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 147 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 148 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 149 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 150 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 151 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 152 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 153 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 154 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 155 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 156 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 157 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 158 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 159 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 160 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 161 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 162 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 163 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 164 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 165 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 166 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 167 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 168 */ &sjme_nvm_byteCode_fastFunctions,
	/* 169 */ &sjme_nvm_byteCode_fastFunctions,
	/* 170 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 171 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 172 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 173 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 174 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 175 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 176 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 177 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 178 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 179 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 180 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 181 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 182 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 183 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 184 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 185 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 186 */ &sjme_nvm_byteCode_fastFunctions,
	/* 187 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 188 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 189 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 190 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 191 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 192 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 193 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 194 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 195 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 196 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 197 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 198 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 199 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 200 */ &sjme_nvm_byteCode_slowNarrowFunctions,
	/* 201 */ &sjme_nvm_byteCode_fastFunctions,
	/* 202 */ &sjme_nvm_byteCode_fastFunctions,
	/* 203 */ &sjme_nvm_byteCode_fastFunctions,
	/* 204 */ &sjme_nvm_byteCode_fastFunctions,
	/* 205 */ &sjme_nvm_byteCode_fastFunctions,
	/* 206 */ &sjme_nvm_byteCode_fastFunctions,
	/* 207 */ &sjme_nvm_byteCode_fastFunctions,
	/* 208 */ &sjme_nvm_byteCode_fastFunctions,
	/* 209 */ &sjme_nvm_byteCode_fastFunctions,
	/* 210 */ &sjme_nvm_byteCode_fastFunctions,
	/* 211 */ &sjme_nvm_byteCode_fastFunctions,
	/* 212 */ &sjme_nvm_byteCode_fastFunctions,
	/* 213 */ &sjme_nvm_byteCode_fastFunctions,
	/* 214 */ &sjme_nvm_byteCode_fastFunctions,
	/* 215 */ &sjme_nvm_byteCode_fastFunctions,
	/* 216 */ &sjme_nvm_byteCode_fastFunctions,
	/* 217 */ &sjme_nvm_byteCode_fastFunctions,
	/* 218 */ &sjme_nvm_byteCode_fastFunctions,
	/* 219 */ &sjme_nvm_byteCode_fastFunctions,
	/* 220 */ &sjme_nvm_byteCode_fastFunctions,
	/* 221 */ &sjme_nvm_byteCode_fastFunctions,
	/* 222 */ &sjme_nvm_byteCode_fastFunctions,
	/* 223 */ &sjme_nvm_byteCode_fastFunctions,
	/* 224 */ &sjme_nvm_byteCode_fastFunctions,
	/* 225 */ &sjme_nvm_byteCode_fastFunctions,
	/* 226 */ &sjme_nvm_byteCode_fastFunctions,
	/* 227 */ &sjme_nvm_byteCode_fastFunctions,
	/* 228 */ &sjme_nvm_byteCode_fastFunctions,
	/* 229 */ &sjme_nvm_byteCode_fastFunctions,
	/* 230 */ &sjme_nvm_byteCode_fastFunctions,
	/* 231 */ &sjme_nvm_byteCode_fastFunctions,
	/* 232 */ &sjme_nvm_byteCode_fastFunctions,
	/* 233 */ &sjme_nvm_byteCode_fastFunctions,
	/* 234 */ &sjme_nvm_byteCode_fastFunctions,
	/* 235 */ &sjme_nvm_byteCode_fastFunctions,
	/* 236 */ &sjme_nvm_byteCode_fastFunctions,
	/* 237 */ &sjme_nvm_byteCode_fastFunctions,
	/* 238 */ &sjme_nvm_byteCode_fastFunctions,
	/* 239 */ &sjme_nvm_byteCode_fastFunctions,
	/* 240 */ &sjme_nvm_byteCode_fastFunctions,
	/* 241 */ &sjme_nvm_byteCode_fastFunctions,
	/* 242 */ &sjme_nvm_byteCode_fastFunctions,
	/* 243 */ &sjme_nvm_byteCode_fastFunctions,
	/* 244 */ &sjme_nvm_byteCode_fastFunctions,
	/* 245 */ &sjme_nvm_byteCode_fastFunctions,
	/* 246 */ &sjme_nvm_byteCode_fastFunctions,
	/* 247 */ &sjme_nvm_byteCode_fastFunctions,
	/* 248 */ &sjme_nvm_byteCode_fastFunctions,
	/* 249 */ &sjme_nvm_byteCode_fastFunctions,
	/* 250 */ &sjme_nvm_byteCode_fastFunctions,
	/* 251 */ &sjme_nvm_byteCode_fastFunctions,
	/* 252 */ &sjme_nvm_byteCode_fastFunctions,
	/* 253 */ &sjme_nvm_byteCode_fastFunctions,
	/* 254 */ &sjme_nvm_byteCode_fastFunctions,
	/* 255 */ &sjme_nvm_byteCode_fastFunctions,
};

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
	sjme_nvm_task_stackTrace(SJME_F_T(inFrame));
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

	sjme_nvm_task_stackTrace(SJME_F_T(inFrame));
	sjme_todo("Impl? %d", relRawCode[0]);
	return sjme_error_notImplemented(relRawCode[0]);
}

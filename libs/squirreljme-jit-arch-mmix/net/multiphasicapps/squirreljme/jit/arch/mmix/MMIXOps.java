// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch.mmix;

/**
 * This interface contains declarations of MMIX operations.
 *
 * @since 2017/08/15
 */
public interface MMIXOps
{
	/** trap (0) rBB,rWW,rXX,rYY,rZZ. */
	public static final int TRAP =
		0;

	/** floating compare (1) rA. */
	public static final int FCMP =
		1;

	/** floating unordered (1). */
	public static final int FUN =
		2;

	/** floating equal to (1) rA. */
	public static final int FEQL =
		3;

	/** floating add (1) rA. */
	public static final int FADD =
		4;

	/** convert floating to fixed (1) rA. */
	public static final int FIX =
		5;

	/** floating subtract (1) rA. */
	public static final int FSUB =
		6;

	/** convert floating to fixed unsigned (1) rA. */
	public static final int FIXU =
		7;

	/** convert fixed to floating (1) rA. */
	public static final int FLOT =
		8;

	/** convert fixed to floating immediate (2) rA. */
	public static final int FLOTI =
		9;

	/** convert fixed to floating unsigned (1) rA. */
	public static final int FLOTU =
		10;

	/** convert fixed to floating unsigned immediate (2) rA. */
	public static final int FLOTUI =
		11;

	/** convert fixed to short float (1) rA. */
	public static final int SFLOT =
		12;

	/** convert fixed to short float immediate (2) rA. */
	public static final int SFLOTI =
		13;

	/** convert fixed to short float unsigned (1) rA. */
	public static final int SFLOTU =
		14;

	/** convert fixed to short float unsigned immediate (2) rA. */
	public static final int SFLOTUI =
		15;

	/** floating multiply (1) rA. */
	public static final int FMUL =
		16;

	/** floating compare with respect to epsilon (1) rA,rE. */
	public static final int FCMPE =
		17;

	/** floating unordered with respect to epsilon (1) rE. */
	public static final int FUNE =
		18;

	/** floating equal with respect to epsilon (1) rA,rE. */
	public static final int FEQLE =
		19;

	/** floating divide (1) rA,rR. */
	public static final int FDIV =
		20;

	/** floating square root (1, Y=0) rA. */
	public static final int FSQRT =
		21;

	/** floating remainder (1) rA. */
	public static final int FREM =
		22;

	/** floating integerize (1, Y=0) rA. */
	public static final int FINT =
		23;

	/** multiply (1) rA. */
	public static final int MUL =
		24;

	/** multiply immediate (2) rA. */
	public static final int MULI =
		25;

	/** multiply unsigned (1) rH. */
	public static final int MULU =
		26;

	/** multiply unsigned immediate (2) rH. */
	public static final int MULUI =
		27;

	/** divide (1) rA,rR. */
	public static final int DIV =
		28;

	/** divide immediate (2) rA,rR. */
	public static final int DIVI =
		29;

	/** divide unsigned (1) rD,rR. */
	public static final int DIVU =
		30;

	/** divide unsigned immediate rD,rR (2). */
	public static final int DIVUI =
		31;

	/** add (1) rA. */
	public static final int ADD =
		32;

	/** add immediate (2) rA. */
	public static final int ADDI =
		33;

	/** add unsigned (1). */
	public static final int ADDU =
		34;

	/** add unsigned immediate (2). */
	public static final int ADDUI =
		35;

	/** subtract (1) rA. */
	public static final int SUB =
		36;

	/** subtract immediate (2) rA. */
	public static final int SUBI =
		37;

	/** subtract unsigned (1). */
	public static final int SUBU =
		38;

	/** subtract unsigned immediate (2). */
	public static final int SUBUI =
		39;

	/** times 2 and add unsigned (1). */
	public static final int T2ADDU =
		40;

	/** times 2 and add unsigned immediate (2). */
	public static final int T2ADDUI =
		41;

	/** times 4 and add unsigned (1). */
	public static final int T4ADDU =
		42;

	/** times 4 and add unsigned immediate (2). */
	public static final int T4ADDUI =
		43;

	/** times 8 and add unsigned (1). */
	public static final int T8ADDU =
		44;

	/** times 8 and add unsigned immediate (2). */
	public static final int T8ADDUI =
		45;

	/** times 16 and add unsigned (1). */
	public static final int T16ADDU =
		46;

	/** times 16 and add unsigned immediate (2). */
	public static final int T16ADDUI =
		47;

	/** compare (1). */
	public static final int CMP =
		48;

	/** compare immediate (2). */
	public static final int CMPI =
		49;

	/** compare unsigned (1). */
	public static final int CMPU =
		50;

	/** compare unsigned immediate (2). */
	public static final int CMPUI =
		51;

	/** negate (1, Y=unsigned immediate) rA. */
	public static final int NEG =
		52;

	/** negate immediate (2, Y=unsigned immediate) rA. */
	public static final int NEGI =
		53;

	/** negate unsigned (1, Y=unsigned immediate). */
	public static final int NEGU =
		54;

	/** negate unsigned immediate (2, Y=unsigned immediate). */
	public static final int NEGUI =
		55;

	/** shift left (1) rA. */
	public static final int SL =
		56;

	/** shift left immediate (2) rA. */
	public static final int SLI =
		57;

	/** shift left unsigned (1). */
	public static final int SLU =
		58;

	/** shift left unsigned immediate (2). */
	public static final int SLUI =
		59;

	/** shift right (1) rA. */
	public static final int SR =
		60;

	/** Stanford Research Institute (2) rA. */
	public static final int SRI =
		61;

	/** shift right unsigned (1). */
	public static final int SRU =
		62;

	/** shift right unsigned immediate (2). */
	public static final int SRUI =
		63;

	/** branch if negative (3). */
	public static final int BN =
		64;

	/** branch if negative, backward (3). */
	public static final int BNB =
		65;

	/** branch if zero (3). */
	public static final int BZ =
		66;

	/** branch if zero, backward (3). */
	public static final int BZB =
		67;

	/** branch if positive (3). */
	public static final int BP =
		68;

	/** branch if positive, backward (3). */
	public static final int BPB =
		69;

	/** branch if odd (3). */
	public static final int BOD =
		70;

	/** branch if odd, backward (3). */
	public static final int BODB =
		71;

	/** branch if nonnegative (3). */
	public static final int BNN =
		72;

	/** branch if nonnegative, backward (3). */
	public static final int BNNB =
		73;

	/** branch if nonzero (3). */
	public static final int BNZ =
		74;

	/** branch if nonzero, backward (3). */
	public static final int BNZB =
		75;

	/** branch if nonpositive (3). */
	public static final int BNP =
		76;

	/** branch if nonpositive, backward (3). */
	public static final int BNPB =
		77;

	/** branch if even (3). */
	public static final int BEV =
		78;

	/** branch if even, backward (3). */
	public static final int BEVB =
		79;

	/** probable branch if negative (3). */
	public static final int PBN =
		80;

	/** probable branch if negative, backward (3). */
	public static final int PBNB =
		81;

	/** probable branch if zero (3). */
	public static final int PBZ =
		82;

	/** probable branch if zero, backward (3). */
	public static final int PBZB =
		83;

	/** probable branch if positive (3). */
	public static final int PBP =
		84;

	/** probable branch if positive, backward (3). */
	public static final int PBPB =
		85;

	/** probable branch if odd (3). */
	public static final int PBOD =
		86;

	/** probable branch if odd, backward (3). */
	public static final int PBODB =
		87;

	/** probable branch if nonnegative (3). */
	public static final int PBNN =
		88;

	/** probable branch if nonnegative, backward (3). */
	public static final int PBNNB =
		89;

	/** probable branch if nonzero (3). */
	public static final int PBNZ =
		90;

	/** probable branch if nonzero, backward (3). */
	public static final int PBNZB =
		91;

	/** probable branch if nonpositive (3). */
	public static final int PBNP =
		92;

	/** probable branch if nonpositive, backward (3). */
	public static final int PBNPB =
		93;

	/** probable branch if even (3). */
	public static final int PBEV =
		94;

	/** probable branch if even, backward (3). */
	public static final int PBEVB =
		95;

	/** conditionally set if negative (1). */
	public static final int CSN =
		96;

	/** conditionally set if negative immediate (2). */
	public static final int CSNI =
		97;

	/** conditionally set if zero (1). */
	public static final int CSZ =
		98;

	/** conditionally set if zero immediate (2). */
	public static final int CSZI =
		99;

	/** conditionally set if positive (1). */
	public static final int CSP =
		100;

	/** conditionally set if positive immediate (2). */
	public static final int CSPI =
		101;

	/** conditionally set if odd (1). */
	public static final int CSOD =
		102;

	/** conditionally set if odd immediate (2). */
	public static final int CSODI =
		103;

	/** conditionally set if nonnegative (2). */
	public static final int CSNN =
		104;

	/** conditionally set if nonnegative immediate (1). */
	public static final int CSNNI =
		105;

	/** conditionally set if nonzero (1). */
	public static final int CSNZ =
		106;

	/** conditionally set if nonzero immediate (2). */
	public static final int CSNZI =
		107;

	/** conditionally set if nonpositive (1). */
	public static final int CSNP =
		108;

	/** conditionally set if nonpositive immediate (2). */
	public static final int CSNPI =
		109;

	/** conditionally set if even (1). */
	public static final int CSEV =
		110;

	/** conditionally set if even immediate (2). */
	public static final int CSEVI =
		111;

	/** zero or set if negative (1). */
	public static final int ZSN =
		112;

	/** zero or set if negative immediate (2). */
	public static final int ZSNI =
		113;

	/** zero or set if zero (1). */
	public static final int ZSZ =
		114;

	/** zero or set if zero immediate (2). */
	public static final int ZSZI =
		115;

	/** zero or set if positive (1). */
	public static final int ZSP =
		116;

	/** zero or set if positive immediate (2). */
	public static final int ZSPI =
		117;

	/** zero or set if odd (1). */
	public static final int ZSOD =
		118;

	/** zero or set if odd immediate (2). */
	public static final int ZSODI =
		119;

	/** zero or set if nonnegative (1). */
	public static final int ZSNN =
		120;

	/** zero or set if nonnegative immediate (2). */
	public static final int ZSNNI =
		121;

	/** zero or set if nonzero (1). */
	public static final int ZSNZ =
		122;

	/** zero or set if nonzero immediate (2). */
	public static final int ZSNZI =
		123;

	/** zero or set if nonpositive (1). */
	public static final int ZSNP =
		124;

	/** zero or set if nonpositive immediate (2). */
	public static final int ZSNPI =
		125;

	/** zero or set if even (1). */
	public static final int ZSEV =
		126;

	/** zero or set if even immediate (2). */
	public static final int ZSEVI =
		127;

	/** load byte (1). */
	public static final int LDB =
		128;

	/** load byte immediate (2). */
	public static final int LDBI =
		129;

	/** load byte unsigned (1). */
	public static final int LDBU =
		130;

	/** load byte unsigned immediate (2). */
	public static final int LDBUI =
		131;

	/** load wyde (1). */
	public static final int LDW =
		132;

	/** load wyde immediate (2). */
	public static final int LDWI =
		133;

	/** load wyde unsigned (1). */
	public static final int LDWU =
		134;

	/** load wyde unsigned immediate (2). */
	public static final int LDWUI =
		135;

	/** load tetrabyte (1). */
	public static final int LDT =
		136;

	/** load tetrabyte immediate (2). */
	public static final int LDTI =
		137;

	/** load tetrabyte unsigned (1). */
	public static final int LDTU =
		138;

	/** load tetrabyte unsigned immediate (2). */
	public static final int LDTUI =
		139;

	/** load octabyte (1). */
	public static final int LDO =
		140;

	/** load octabyte immediate (2). */
	public static final int LDOI =
		141;

	/** load octabyte unsigned (1). */
	public static final int LDOU =
		142;

	/** load octabyte unsigned immediate (2). */
	public static final int LDOUI =
		143;

	/** load short float (1). */
	public static final int LDSF =
		144;

	/** load short float immediate (2). */
	public static final int LDSFI =
		145;

	/** load high tetra (1). */
	public static final int LDHT =
		146;

	/** load high tetra immediate (2). */
	public static final int LDHTI =
		147;

	/** compare and swap octabytes (1) rP. */
	public static final int CSWAP =
		148;

	/** compare and swap octabytes immediate (2) rP. */
	public static final int CSWAPI =
		149;

	/** load octabyte uncached (1). */
	public static final int LDUNC =
		150;

	/** load octabyte uncached immediate (2). */
	public static final int LDUNCI =
		151;

	/** load virtual translation status (1). */
	public static final int LDVTS =
		152;

	/** load virtual translation status immediate. */
	public static final int LDVTSI =
		153;

	/** preload data (1, X=count). */
	public static final int PRELD =
		154;

	/** preload data immediate (2, X=count). */
	public static final int PRELDI =
		155;

	/** prefetch to go (1, X=count). */
	public static final int PREGO =
		156;

	/** prefetch to go immediate (2, X=count). */
	public static final int PREGOI =
		157;

	/** go to location (1). */
	public static final int GO =
		158;

	/** go to location immediate (2). */
	public static final int GOI =
		159;

	/** store byte (1) rA. */
	public static final int STB =
		160;

	/** store byte immediate (2) rA. */
	public static final int STBI =
		161;

	/** store byte unsigned (1). */
	public static final int STBU =
		162;

	/** store byte unsigned immediate (2). */
	public static final int STBUI =
		163;

	/** store wyde (1) rA. */
	public static final int STW =
		164;

	/** store wyde immediate (2) rA. */
	public static final int STWI =
		165;

	/** store wyde unsigned (1). */
	public static final int STWU =
		166;

	/** store wyde unsigned immediate (2). */
	public static final int STWUI =
		167;

	/** store tetrabyte (1) rA. */
	public static final int STT =
		168;

	/** store tetrabyte immediate (2) rA. */
	public static final int STTI =
		169;

	/** store tetrabyte unsigned (1). */
	public static final int STTU =
		170;

	/** store tetrabyte unsigned immediate (2). */
	public static final int STTUI =
		171;

	/** store octabyte (1). */
	public static final int STO =
		172;

	/** store octabyte immediate (2). */
	public static final int STOI =
		173;

	/** store octabyte unsigned (1). */
	public static final int STOU =
		174;

	/** store octabyte unsigned immediate (2). */
	public static final int STOUI =
		175;

	/** store short float (1) rA. */
	public static final int STSF =
		176;

	/** store short float immediate (2) rA. */
	public static final int STSFI =
		177;

	/** store high tetra (1). */
	public static final int STHT =
		178;

	/** store high tetra immediate (2). */
	public static final int STHTI =
		179;

	/** store constant octabyte (X=const, Y=register, Z=register). */
	public static final int STCO =
		180;

	/**
	 * store constant octabyte immediate (X=const, Y=register,
	 * Z=unsigned immediate).
	 */
	public static final int STCOI =
		181;

	/** store octabyte uncached (1). */
	public static final int STUNC =
		182;

	/** store octabyte uncached immediate (2). */
	public static final int STUNCI =
		183;

	/** synchronize data (1, X=count). */
	public static final int SYNCD =
		184;

	/** synchronize data immediate (2, X=count). */
	public static final int SYNCDI =
		185;

	/** prestore data (1, X=count). */
	public static final int PREST =
		186;

	/** prestore data immediate (2, X=count). */
	public static final int PRESTI =
		187;

	/** synchronize instructions and data (1, X=count). */
	public static final int SYNCID =
		188;

	/** synchronize instructions and data immediate (2, X=count). */
	public static final int SYNCIDI =
		189;

	/** push registers and go (1) rJ,rL. */
	public static final int PUSHGO =
		190;

	/** push registers and go immediate (2) rJ,rL. */
	public static final int PUSHGOI =
		191;

	/** bitwise or (1). */
	public static final int OR =
		192;

	/** bitwise or immediate (2). */
	public static final int ORI =
		193;

	/** bitwise or-not (1). */
	public static final int ORN =
		194;

	/** bitwise or-not immediate (2). */
	public static final int ORNI =
		195;

	/** bitwise not-or (1). */
	public static final int NOR =
		196;

	/** bitwise not-or immediate (2). */
	public static final int NORI =
		197;

	/** bitwise exclusive-or (1). */
	public static final int XOR =
		198;

	/** bitwise exclusive-or immediate (2). */
	public static final int XORI =
		199;

	/** bitwise and (1). */
	public static final int AND =
		200;

	/** bitwise and immediate (2). */
	public static final int ANDI =
		201;

	/** bitwise and-not (1). */
	public static final int ANDN =
		202;

	/** bitwise and-not immediate (1). */
	public static final int ANDNI =
		203;

	/** bitwise not and (1). */
	public static final int NAND =
		204;

	/** bitwise not and immediate (2). */
	public static final int NANDI =
		205;

	/** bitwise not-exclusive-or (1). */
	public static final int NXOR =
		206;

	/** bitwise not-exclusive-or immediate (2). */
	public static final int NXORI =
		207;

	/** byte difference (1). */
	public static final int BDIF =
		208;

	/** byte difference immediate (2). */
	public static final int BDIFI =
		209;

	/** wyde difference (1). */
	public static final int WDIF =
		210;

	/** wyde difference immediate (2). */
	public static final int WDIFI =
		211;

	/** tetra difference (1). */
	public static final int TDIF =
		212;

	/** tetra difference immediate (2). */
	public static final int TDIFI =
		213;

	/** octa difference (1). */
	public static final int ODIF =
		214;

	/** octa difference immediate (2). */
	public static final int ODIFI =
		215;

	/** bitwise multiplex (1) rM. */
	public static final int MUX =
		216;

	/** bitwise multiplex immediate (2) rM. */
	public static final int MUXI =
		217;

	/** sideways add (1). */
	public static final int SADD =
		218;

	/** sideways add immediate (2). */
	public static final int SADDI =
		219;

	/** multiple or (1). */
	public static final int MOR =
		220;

	/** multiple or immediate (2). */
	public static final int MORI =
		221;

	/** multiple exclusive-or (1). */
	public static final int MXOR =
		222;

	/** multiple exclusive-or immediate (2). */
	public static final int MXORI =
		223;

	/** set to high wyde (3). */
	public static final int SETH =
		224;

	/** set to medium high wyde (3). */
	public static final int SETMH =
		225;

	/** set to medium low wyde (3). */
	public static final int SETML =
		226;

	/** set to low wyde (3). */
	public static final int SETL =
		227;

	/** increase by high wyde (3). */
	public static final int INCH =
		228;

	/** increase by medium high wyde (3). */
	public static final int INCMH =
		229;

	/** increase by medium low wyde (3). */
	public static final int INCML =
		230;

	/** increase by low wyde (3). */
	public static final int INCL =
		231;

	/** bitwise or with high wyde (3). */
	public static final int ORH =
		232;

	/** bitwise or with medium high wyde (3). */
	public static final int ORMH =
		233;

	/** bitwise or with medium low wyde (3). */
	public static final int ORML =
		234;

	/** bitwise or with low wyde (3). */
	public static final int ORL =
		235;

	/** bitwise and-not high wyde (2). */
	public static final int ANDNH =
		236;

	/** bitwise and-not medium high wyde (3). */
	public static final int ANDNMH =
		237;

	/** bitwise and-not medium low wyde (3). */
	public static final int ANDNML =
		238;

	/** bitwise and-not low wyde (3). */
	public static final int ANDNL =
		239;

	/** jump (4). */
	public static final int JMP =
		240;

	/** jump backward (4). */
	public static final int JMPB =
		241;

	/** push registers and jump (3) rJ,rL. */
	public static final int PUSHJ =
		242;

	/** push registers and jump backward (3) rJ,rL. */
	public static final int PUSHJB =
		243;

	/** get address (3). */
	public static final int GETA =
		244;

	/** get address backward (3). */
	public static final int GETAB =
		245;

	/** put into special register (X=specreg, Y=0, Z=register) rA-rZZ. */
	public static final int PUT =
		246;

	/**
	 * put into special register immediate (X=specreg, Y=0,
	 * Z=unsigned immediate) rA-rZZ.
	 */
	public static final int PUTI =
		247;

	/** pop (3) rJ,rL. */
	public static final int POP =
		248;

	/** resume after interrupt (4) rW,rX,rY,rZ. */
	public static final int RESUME =
		249;

	/**
	 * save context (X=register, Y=Z=0)
	 * rA,rB,rD,rE,rG,rH,rJ,rL,rM,rO,rP,rR,rS,rW,rX,rY,rZ.
	 */
	public static final int SAVE =
		250;

	/**
	 * unsave context (X=Y=0, Z=register)
	 * rA,rB,rD,rE,rG,rH,rJ,rL,rM,rO,rP,rR,rS,rW,rX,rY,rZ.
	 */
	public static final int UNSAVE =
		251;

	/** synchronize (4). */
	public static final int SYNC =
		252;

	/** sympathize with your machinery (0). */
	public static final int SWYM =
		253;

	/** get from special register (X=register, Y=0, Z=specreg) rA-rZZ. */
	public static final int GET =
		254;

	/** trip (0) rB,rW,rX,rY,rZ. */
	public static final int TRIP =
		255;
}


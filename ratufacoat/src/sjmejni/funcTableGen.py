# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#	 Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Script that turns function table CSV to source code

import csv


class FuncArg:
	"""
	Represents a function argument or return type.
	"""
	def __init__(self, in_str: str):
		self.in_str = in_str.strip()

		if in_str.strip() == '...':
			self.mods = ''
			self.type = '...'
			self.name = '...'
			self.star = ''
		else:
			# Extract name first, if any
			last_space = in_str.strip().rfind(" ")
			if last_space >= 0:
				self.name = in_str[last_space + 1:].strip()
			else:
				self.name = ''
				last_space = len(in_str)

			# Determine if there are pointer stars
			type_side = in_str[0:last_space].strip()
			first_pointer_star = type_side.find("*")

			# Found pointer stars
			if first_pointer_star >= 0:
				self.type = type_side[0:first_pointer_star].strip()
				self.star = type_side[first_pointer_star:].strip()

			# There are none
			else:
				self.type = type_side
				self.star = ''

			# Remove const modifier
			if self.type.startswith('const '):
				self.type = self.type[6:].strip()
				self.mods = 'const'
			else:
				self.mods = ''

	def __repr__(self):
		return str('%s|%s|%s|%s' % (self.mods, self.type, self.star, self.name))


class FuncTableEntry:
	"""
	Function table entry, represents a single function to generate.
	"""
	def __init__(self, row_entry):
		self.table = row_entry['table']
		self.index = int(row_entry['index'])
		self.function = row_entry['function']

		if row_entry['returnType'] is not None and row_entry['returnType'] != '':
			self.returnType: FuncArg = FuncArg(row_entry['returnType'])

		# Read in arguments
		self.args: [FuncArg] = []
		try:
			for i in range(0, 99):
				item = row_entry['arg' + str(i)]
				if item is not None and len(item) > 0:
					self.args.append(FuncArg(row_entry['arg' + str(i)]))
		except KeyError:
			pass

	def __repr__(self):
		return str([self.table, str(self.index),
					self.function if self.function is not None else "null",
					self.returnType if self.returnType is not None else "null",
					self.args])


def convert_type(func_type: FuncArg) -> str:
	"""
	Converts the function argument and represents it with a SquirrelJME type
	along with how it would be represented otherwise.

	:param func_type: Function argument.
	:return: The string which represents the function argument mapped to
	SquirrelJME types accordingly.
	"""
	typeish = func_type.type
	nameish = func_type.name
	starish = func_type.star

	if func_type.mods is not None and func_type.mods != '':
		modsish = '%s ' % func_type.mods
	else:
		modsish = ''

	# Remap type to SquirrelJME?
	match typeish:
		case 'jfieldID':
			typeish = 'sjme_vmField'
		case 'jmethodID':
			typeish = 'sjme_vmMethod'
		case 'JavaVM':
			typeish = 'sjme_vmState'
		case 'JNIEnv':
			typeish = 'sjme_vmThread'
		case 'jobjectRefType':
			typeish = 'sjme_jobjectReferenceType'
		case 'JNINativeMethod':
			typeish = 'sjme_vmRegisterNative'

		case 'jvalue':
			typeish = 'sjme_jvalue'
		case 'jboolean':
			typeish = 'sjme_jboolean'
		case 'jbyte':
			typeish = 'sjme_jbyte'
		case 'jchar':
			typeish = 'sjme_jchar'
		case 'jshort':
			typeish = 'sjme_jshort'
		case 'jint':
			typeish = 'sjme_jint'
		case 'jlong':
			typeish = 'sjme_jlong'
		case 'jfloat':
			typeish = 'sjme_jfloat'
		case 'jdouble':
			typeish = 'sjme_jdouble'
		case 'jobject':
			typeish = 'sjme_jobject'
		case 'jclass':
			typeish = 'sjme_jclass'
		case 'jstring':
			typeish = 'sjme_jstring'
		case 'jthrowable':
			typeish = 'sjme_jthrowable'
		case 'jweak':
			typeish = 'sjme_jweakReference'

		case 'jbooleanArray':
			typeish = 'sjme_jbooleanArray'
		case 'jbyteArray':
			typeish = 'sjme_jbyteArray'
		case 'jcharArray':
			typeish = 'sjme_jcharArray'
		case 'jshortArray':
			typeish = 'sjme_jshortArray'
		case 'jintArray':
			typeish = 'sjme_jintArray'
		case 'jlongArray':
			typeish = 'sjme_jlongArray'
		case 'jfloatArray':
			typeish = 'sjme_jfloatArray'
		case 'jdoubleArray':
			typeish = 'sjme_jdoubleArray'

	# Remap name to SquirrelJME?
	match nameish:
		case 'vm':
			nameish = 'vmState'
		case 'p_env':
			nameish = 'vmThread'
		case 'penv':
			nameish = 'vmThread'
		case 'env':
			nameish = 'vmThread'
		case 'thr_args':
			nameish = 'threadArgs'
		case 'cls':
			nameish = 'classy'
		case 'clazz':
			nameish = 'classy'
		case 'clazz1':
			nameish = 'classyA'
		case 'clazz2':
			nameish = 'classyB'
		case 'ref':
			nameish = 'reference'
		case 'ref1':
			nameish = 'referenceA'
		case 'ref2':
			nameish = 'referenceB'
		case 'msg':
			nameish = 'message'
		case 'obj':
			nameish = 'object'
		case 'str':
			nameish = 'string'
		case 'sig':
			nameish = 'signature'
		case 'methodID':
			nameish = 'method'
		case 'fieldID':
			nameish = 'field'
		case 'length':
			nameish = 'len'
		case 'index':
			nameish = 'dx'
		case 'elems':
			nameish = 'elements'
		case 'carray':
			nameish = 'charArray'

	if nameish is not None and nameish != '':
		return '%s%s%s %s' % (modsish, typeish, starish, nameish)
	return '%s%s%s' % (modsish, typeish, starish)


# Header start
header_intro = '/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-\n\
// ---------------------------------------------------------------------------\n\
// SquirrelJME\n\
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>\n\
// ---------------------------------------------------------------------------\n\
// SquirrelJME is under the GNU General Public License v3+, or later.\n\
// See license.mkd for licensing and copyright information.\n\
// -------------------------------------------------------------------------*/\n\
\n'

# Setup file load
csvfile = open('funcTable.csv', newline='')
reader = csv.DictReader(csvfile)

# Read in each row and fill a mapping accordingly
entries = []
entries_by_tab_dex = {}  # dict[str, dict[int, entries]]
for row in reader:
	# Load in entry
	current = FuncTableEntry(row)
	entries.append(current)

	# Setup major table for the group
	if entries_by_tab_dex.get(current.table) is None:
		entries_by_tab_dex[current.table] = {}
	major_table = entries_by_tab_dex.get(current.table)

	# Add entries to sub-table
	major_table[current.index] = current

# Go through each table and generate source code for it
for table_name in entries_by_tab_dex:
	index_tables = entries_by_tab_dex[table_name]

	# Temporary strings for header/source files
	header_function_prototypes = '' + header_intro
	header_struct_fields = '' + header_intro

	# Surround for function prototypes
	header_function_prototypes += '#include "sjmejni/tables/surround/surroundprotoh.h"\n\n'

	# Surround for header struct entries
	header_struct_fields += '#include "sjmejni/tables/surround/surroundstructfield.h"\n\n'

	# Handle each index accordingly
	try:
		for index in range(0, 999):
			# Load in index
			entry: FuncTableEntry = index_tables[index]

			# Nulls are blank specials
			if entry.function == 'NULL':
				header_struct_fields += '/** Do not use, reserved. */\n'
				header_struct_fields += 'void* reserved%d,\n\n' % entry.index

			# Otherwise do normal generation logic
			else:
				# Location of the C include for the prototype
				prototype_file_name = 'sjmejni/tables/%s/def%s.h' % \
					(entry.table, entry.function)

				# Utilize the C include here
				header_struct_fields += '#include "%s"\n' % prototype_file_name

				# Include within prototypes
				header_function_prototypes += '#include "%s"\n' % \
					prototype_file_name

				# Build function arguments
				built_args = ''
				for func_arg in entry.args:
					# Spacer for arguments
					if len(built_args) > 0:
						built_args += ', '

					# Variadic args are a bit special, they just go right on
					if func_arg.type == '...':
						built_args += '...'

					# Otherwise add argument
					else:
						built_args += convert_type(func_arg)

				# if empty, make it void since it accepts no arguments
				if len(built_args) == 0:
					built_args = 'void'

				# Build prototype file
				prototype_file = '' + header_intro
				prototype_file += '%s SJME_FUNC_SURROUND(%s)(%s) ' \
					'SJME_FUNC_SURROUND_SUFFIX\n\n' % \
					(convert_type(entry.returnType),
					entry.function, built_args)

				print(prototype_file)

	except KeyError:
		pass

	print(header_struct_fields)
	print(header_function_prototypes)

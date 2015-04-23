package datapathEngine;

import java.util.ArrayList;

public class Simulator {

	public String[] memory = new String[100];
	public String[] names = { "$0", "$at", "$v0", "$v1", "$a0", "$a1", "$a2",
			"$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7",
			"$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8",
			"$t9", "$gp", "$sp", "$fp", "$ra" };
	public int[] values = new int[30];
	public String[] binaryValues = { "00000", "00001", "00010", "00011",
			"00100", "00101", "00110", "00111", "01000", "01001", "01010",
			"01011", "01100", "01101", "01110", "01111", "10000", "10001",
			"10010", "10011", "10100", "10101", "10110", "10111", "11000",
			"11001", "11010", "11011", "11100", "11101" };
	int pc;
	String op = "";
	String fn = "";
	int index = 0;
	int b1Index = 0;
	int b2Index = 0;
	int b3Index = 0;
	String Format;
	String rs;
	String rt;
	String rd;
	String shamt = "00000";

	String Idex = "";

	boolean r = true;
	int startingAddress;

	public Simulator() {

	}

	public int indexOf(String x) {
		for (int i = 0; i < names.length; i++) {
			if (x.equals(names[i]))
				index = i;
		}
		return index;
	}

	public int getBranchAddress(String branchName) {
		for (int i = startingAddress; i < memory.length; i++) {
			if (memory[i] != null) {
				String[] x = memory[i].split(":");
				if (x.length == 2 && x[0].equals(branchName)) {
					return i;
				}
			}
		}
		return -1;
	}

	public Simulator(ArrayList<String> instructions, ArrayList<String> mem,
			ArrayList<String> regs, int start) {
		pc = start;
		values[1] = start;
		int count = start;
		startingAddress = start;
		for (int i = 0; i < mem.size(); i++) {
			String data = mem.get(i);
			String[] x = data.split(",");
			memory[Integer.parseInt(x[0])] = x[1];
		}
		for (int i = 0; i < regs.size(); i++) {
			String data = regs.get(i);
			String[] x = data.split(",");
			values[Integer.parseInt(x[0])] = Integer.parseInt(x[1]);
		}
		for (int i = 0; i < instructions.size(); i++) {
			String firstInstr = instructions.get(i);
			memory[count] = firstInstr;
			count += 1;
		}

	}

	public void simulate() {

	}

	public void Fetch(String instruction) {
		int offset;

		String immediate = "";
		String[] a;
		if (instruction.split(":").length == 2) {
			a = instruction.split(":")[1].split(" ");
		} else {
			a = instruction.split(" ");
		}
		String[] b = a[1].split(",");
		b1Index = indexOf(b[0]);
		rd = binaryValues[b1Index];
		b2Index = indexOf(b[1]);
		rs = binaryValues[b2Index];
		if (a[0].equals("add")) {
			b3Index = indexOf(b[2]);
			rt = binaryValues[b3Index];
			fn = "100000";
			op = "000000";
			Format = op + rs + rt + rd + shamt + fn;
		}

		else if (a[0].equals("addi")) {
			r = false;
			op = "001000";
			immediate = Binary(Integer.parseInt(b[2]), "", 16);
			Format = op + rs + rd + immediate;

		}
		if (a[0].equals("sub")) {
			b3Index = indexOf(b[2]);
			rt = binaryValues[b3Index];
			fn = "100010";
			op = "000000";
			Format = op + rs + rt + rd + shamt + fn;

		} else if (a[0].equals("lw")) {

			r = false;
			op = "100011";
			int i1 = b[1].indexOf('(');
			offset = Integer.parseInt(b[1].substring(0, i1));
			immediate = Binary(offset, "", 16);
			String reg = b[1].substring(i1 + 1, b[1].length() - 1);
			int index1 = indexOf(reg);
			rs = binaryValues[index1];
			Format = op + rs + rd + immediate;

		} else if (a[0].equals("lb")) {
			r = false;
			op = "100000";
			int i1 = b[1].indexOf('(');
			offset = Integer.parseInt(b[1].substring(0, i1));
			immediate = Binary(offset, "", 16);
			String reg = b[1].substring(i1 + 1, b[1].length() - 1);
			int index1 = indexOf(reg);
			rs = binaryValues[index1];
			Format = op + rs + rd + immediate;

		} else if (a[0].equals("lbu")) {
			r = false;
			op = "100100";
			int i1 = b[1].indexOf('(');
			offset = Integer.parseInt(b[1].substring(0, i1));
			immediate = Binary(offset, "", 16);
			String reg = b[1].substring(i1 + 1, b[1].length() - 1);
			int index1 = indexOf(reg);
			rs = binaryValues[index1];
			Format = op + rs + rd + immediate;

		} else if (a[0].equals("sw")) {
			r = false;
			op = "101011";
			int i1 = b[1].indexOf('(');
			offset = Integer.parseInt(b[1].substring(0, i1));
			immediate = Binary(offset, "", 16);
			String reg = b[1].substring(i1 + 1, b[1].length() - 1);
			int index1 = indexOf(reg);
			rs = binaryValues[index1];
			Format = op + rs + rd + immediate;

		} else if (a[0].equals("sb")) {
			r = false;
			op = "101000";
			int i1 = b[1].indexOf('(');
			offset = Integer.parseInt(b[1].substring(0, i1));
			immediate = Binary(offset, "", 16);
			String reg = b[1].substring(i1 + 1, b[1].length() - 1);
			int index1 = indexOf(reg);
			rs = binaryValues[index1];
			Format = op + rs + rd + immediate;

		} else if (a[0].equals("lui")) {
			r = false;
			op = "001111";
			immediate = Binary(Integer.parseInt(rs), "", 16);
			rs = "00000";
			Format = op + rs + rd + immediate;

		} else if (a[0].equals("sll")) {
			fn = "000000";
			op = "000000";
			rs = "00000";
			shamt = Binary(Integer.parseInt(b[2]), "", 5);
			Format = op + rs + rt + rd + shamt + fn;
		} else if (a[0].equals("srl")) {
			fn = "000010";
			op = "000000";
			rs = "00000";
			shamt = Binary(Integer.parseInt(b[2]), "", 5);
			Format = op + rs + rt + rd + shamt + fn;
		} else if (a[0].equals("and")) {
			fn = "100100";
			op = "000000";
			Format = op + rs + rt + rd + shamt + fn;
		} else if (a[0].equals("nor")) {
			fn = "100111";
			op = "000000";
			Format = op + rs + rt + rd + shamt + fn;
		} else if (a[0].equals("beq")) {
			r = false;
			op = "000100";
			int address = getBranchAddress(b[2]);
			immediate = Binary(address - (pc + 1), "", 16);
			Format = op + rd + rs + immediate;

		} else if (a[0].equals("bne")) {
			r = false;
			op = "000101";
			int address = getBranchAddress(b[2]);
			immediate = Binary(address - (pc + 1), "", 16);
			Format = op + rs + rt + immediate;

		} else if (a[0].equals("j")) {
			op = "000010";
			int address = getBranchAddress(b[0]);
			immediate = Binary(address, "", 26);
			Format = op + immediate;

		} else if (a[0].equals("jal")) {
			op = "000011";
			int address = getBranchAddress(b[0]);
			immediate = Binary(address, "", 26);
			Format = op + immediate;

		} else if (a[0].equals("jr")) {
			fn = "001000";
			op = "000000";
			rs = "00000000000000000000";
			Format = op + rd + rs + fn;
		} else if (a[0].equals("slt")) {
			b3Index = indexOf(b[2]);
			rt = binaryValues[b3Index];
			fn = "101010";
			op = "000000";

			Format = op + rs + rt + rd + shamt + fn;
		} else if (a[0].equals("sltu")) {
			b3Index = indexOf(b[2]);
			rt = binaryValues[b3Index];
			fn = "101001";
			op = "000000";
			Format = op + rs + rt + rd + shamt + fn;
		}
	}

	public void Decode(String format) {
		int indexrs = 0;
		int indexrt = 0;
		int valuers = 0;
		int valuert = 0;
		int indexrd = 0;
		int valuerd = 0;
		int indexShift = 0;
		int Shift = 0;

		if (r) {
			String opR;
			String rsR;
			String rtR;
			String rdR;
			String shamtR;
			String fnR;
			opR = format.substring(0, 6);
			rsR = format.substring(6, 11);
			rdR = format.substring(11, 16);
			rtR = format.substring(16, 21);
			shamtR = format.substring(21, 26);
			fnR = format.substring(26, 32);
			indexrd = indexOf(rdR);
			valuerd = values[indexrd];

			if (fnR.equals("00000") || fnR.equals("00010")) {
				indexrs = indexOf(rdR);
				valuers = values[indexrs];
				indexShift = indexOf(shamtR);
				Shift = values[indexShift];

			} else if (fnR.equals("101001")) {
				indexrs = indexOf(rsR);
				valuers = values[indexrs];
				if (valuers < 0) {
					valuers = valuers * -1;
				}
				indexrt = indexOf(rtR);
				valuert = values[indexrt];
				if (valuert < 0) {
					valuert = valuert * -1;
				}
			} else {
				indexrs = indexOf(rsR);
				valuers = values[indexrs];
				indexrt = indexOf(rtR);
				valuert = values[indexrt];
			}
			Idex = opR + "," + valuers + "," + valuerd + "," + valuert + ","
					+ Shift + "," + fnR;
		} else {
			String opI;
			String rsI;
			String rtI;
			String Constant;
			int offset = 0;
			opI = format.substring(0, 6);
			rsI = format.substring(6, 11);
			rtI = format.substring(11, 16);
			Constant = format.substring(16, 32);

			if (opI.equals("100011") || opI.equals("100000")) {
				offset = decimal(Constant);
				// newOffset = offset / 4;
				indexrs = indexOf(rsI);
				valuers = values[indexrs];

			} else if (opI.equals("101011") || opI.equals("101000")) {
				indexrt = indexOf(rtI);
				valuert = values[indexrt];
				indexrs = indexOf(rsI);
				valuers = values[indexrs];

			} else if (opI.equals("001111")) {
				offset = decimal(Constant);

			} else if (opI.equals("000100") || opI.equals("000101")) {
				indexrt = indexOf(rtI);
				valuert = values[indexrt];
				indexrs = indexOf(rsI);
				valuers = values[indexrs];
			}
			Idex = opI + "," + valuers + "," + valuert + "," + offset;

		}

	}

	public void Execute(String idex) {
		if (r) {

		}

	}

	public static int decimal(String s) {
		int res = 0;
		int j = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			if (s.charAt(i) == '1') {
				res += Math.pow(2, j);
			}
			j += 1;
		}
		return res;
	}
	public static String binaryNeg(int number, int size){
		String s = Integer.toBinaryString(number);
		String x = s;
		for(int i = 0; x.length()>size;i++){
			x = s.substring(i+1, s.length());
		}
		return x;
	}
	public static String Binary(int number, String result, int size) {
		if(number<0)
			return binaryNeg(number, size);
		int rem;
		if (number <= 1) {
			String s = number + result;
			if (size > 0) {
				for (int i = 0; i < size - 1; i++) {
					s = "0" + s;
				}
			}
			return s;
		}
		rem = number % 2;
		return Binary(number >> 1, rem + result, size - 1);

	}

	public static void main(String[] args) {
		ArrayList<String> br = new ArrayList<String>();
		br.add("L1:add $t0,$t1,$t2");
		br.add("add $t0,$t1,$t2");
		br.add("add $t0,$t1,$t2");
		br.add("add $t0,$t1,$t2");
		br.add("add $t6t0,$t1,$t2");
		br.add("beq $t3,$0,L1");
		Simulator sim = new Simulator(br, new ArrayList<String>(),
				new ArrayList<String>(), 5);
		sim.pc = 10;
		sim.Fetch("beq $t3,$0,L1");
		if (sim.Format.equals("00010001011000001111111111111010"))
			System.out.println(true);
		System.out.println(sim.Format);
		//System.out.println(Integer.toBinaryString(-6));
		//System.out.println(Integer.parseInt("-110",2));
	}

}

package com.test.algoram;

/**
 * @ClassName Test
 * @Description TODO
 * @Author liqinchao
 * @Date 2022/2/16 16:04
 * @Version 1.0
 **/
public class Test {
	// 当前固定部分
	private String CurFixPart;
	private String PreGenNum;

	public static void main(String[] args) {
		Test t = new Test();
		t.GenControll("122345");
	}

	// 调整字符串s位置pos字符到最前
	private String shift(String s, int pos) {
		String newStr;
		if (s.length() > pos + 1) {
			newStr = s.substring(pos, pos + 1) + s.substring(0, pos) + s.substring(pos + 1);
		} else {
			newStr = s.substring(pos) + s.substring(0, pos);
		}
		return newStr;
	}

	protected int Validate(String newNum) {
		String newGenNum = CurFixPart + newNum;
		if (Integer.valueOf(newGenNum) <= Integer.valueOf(PreGenNum)) {
			return 0;
		}
		if (newGenNum.substring(2, 3).equals("4") || (newGenNum.indexOf("35") != -1) || (newGenNum.indexOf("53") != -1)) {
			return 0;
		}
		PreGenNum = newGenNum;
		System.out.println(newGenNum);
		return 0;
	}

	public void GenControll(String Base) {
		PreGenNum = "0";
		CurFixPart = "";
		GenNext(Base, 0);
	}

	void GenNext(String varPart, int curPos) {
		if (varPart.length() == 2) {
			Validate(varPart);
			Validate(shift(varPart, 1));
			return;
		}
		// Next Layer
		String newGen = shift(varPart, curPos);
		String SavedFixPart = CurFixPart;
		CurFixPart = CurFixPart + newGen.substring(0, 1);
		GenNext(newGen.substring(1), 0);
		CurFixPart = SavedFixPart;
		// 同层递增
		if (curPos == varPart.length() - 1) {
			return;
		}
		GenNext(varPart, curPos + 1);
	}
}

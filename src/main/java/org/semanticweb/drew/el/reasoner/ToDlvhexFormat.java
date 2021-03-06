package org.semanticweb.drew.el.reasoner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.semanticweb.drew.dlprogram.format.DLProgramStorerImpl;
import org.semanticweb.drew.dlprogram.model.DLProgram;
import org.semanticweb.drew.dlprogram.parser.DLProgramParser;
import org.semanticweb.drew.dlprogram.parser.ParseException;

@Deprecated
public class ToDlvhexFormat {

	public static void main(String[] args) throws ParseException {
		ToDlvhexFormat t = new ToDlvhexFormat();
//		t.toDlvhex("benchmark/lubm/rules/lubm_0.elp");
//		t.toDlvhex("benchmark/lubm/rules/lubm_1.elp");
//		t.toDlvhex("benchmark/lubm/rules/lubm_2.elp");
//		t.toDlvhex("benchmark/lubm/rules/lubm_3.elp");
//		t.toDlvhex("benchmark/lubm/rules/lubm_4.elp");
//		t.toDlvhex("benchmark/lubm/rules/lubm_5.elp");
//		t.toDlvhex("benchmark/lubm/rules/lubm_6.elp");
//		t.toDlvhex("benchmark/lubm/rules/lubm_7.elp");
		t.toDlvhex("benchmark/galen/rules/q1.elp");
	}
	
	void toDlvhex(String dlpFile) throws ParseException{
		String dlvhex = dlpFile.replaceAll("elp", "dlvhex");
		DLProgramStorerImpl helper = new DLProgramStorerImpl();
		try {
			FileReader reader = new FileReader(dlpFile);
			DLProgramParser parser =new DLProgramParser(reader);
			DLProgram program = parser.program();
			helper.setUsingDlvhexFormat(true);
			helper.store(program, new FileWriter(dlvhex));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

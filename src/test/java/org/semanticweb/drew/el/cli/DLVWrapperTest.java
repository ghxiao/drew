package org.semanticweb.drew.el.cli;

import java.io.IOException;
import java.util.List;

import it.unical.mat.dlv.program.Literal;
import it.unical.mat.wrapper.DLVError;
import it.unical.mat.wrapper.DLVInputProgram;
import it.unical.mat.wrapper.DLVInputProgramImpl;
import it.unical.mat.wrapper.DLVInvocation;
import it.unical.mat.wrapper.DLVInvocationException;
import it.unical.mat.wrapper.DLVWrapper;
import it.unical.mat.wrapper.Model;
import it.unical.mat.wrapper.ModelHandler;
import it.unical.mat.wrapper.ModelResult;
import it.unical.mat.wrapper.Predicate;

import org.junit.Test;

public class DLVWrapperTest {
	@Test
	public void testWF() throws DLVInvocationException, IOException {
		DLVInputProgram inputProgram = new DLVInputProgramImpl();
		inputProgram.addText("p :- not q. q:- not p. r.");
		String dlvPath = "/Users/xiao/bin/dlv";
		DLVInvocation invocation = DLVWrapper.getInstance().createInvocation(
				dlvPath);
		invocation.setInputProgram(inputProgram);
		
		invocation.addOption("-wf");
		
		invocation.subscribe(new ModelHandler() {

			@Override
			public void handleResult(DLVInvocation paramDLVInvocation,
					ModelResult modelResult) {
				System.out.print("{ ");
				System.out.print(modelResult);
				System.out.println("}");
				System.out.println();
			}
		});

		invocation.run();

		invocation.waitUntilExecutionFinishes();
		
		List<DLVError> k = invocation.getErrors();
		if (k.size() > 0)
			System.err.println(k);

	}
}
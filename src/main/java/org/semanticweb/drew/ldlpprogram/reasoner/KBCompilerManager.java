package org.semanticweb.drew.ldlpprogram.reasoner;

import org.semanticweb.drew.dlprogram.DLInputSignature;
import org.semanticweb.drew.utils.SymbolEncoder;


public class KBCompilerManager {

	final static KBCompilerManager instance = new KBCompilerManager();

	SymbolEncoder<DLInputSignature> signatureEncoder = new SymbolEncoder<DLInputSignature>();

	public static KBCompilerManager getInstance() {
		return instance;
	}

	private KBCompilerManager() {

	}

	public String getSubscript(DLInputSignature signature) {
		return String.valueOf(signatureEncoder.getValueBySymbol(signature));
	}
}

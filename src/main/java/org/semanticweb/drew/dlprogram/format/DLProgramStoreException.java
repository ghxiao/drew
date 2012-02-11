package org.semanticweb.drew.dlprogram.format;

import java.io.IOException;

public class DLProgramStoreException extends RuntimeException {

	private static final long serialVersionUID = -8299679363683256703L;

	public DLProgramStoreException(IOException e) {
		super(e);
	}

}

package org.semanticweb.drew.dlprogram.model;

public class ProgramComment implements ProgramStatement {

	// private String comment;
	private String str;
	
	
	public ProgramComment(String comment) {
		// this.comment = comment;

		String[] lines = comment.split("\\r?\\n");

		StringBuilder sb = new StringBuilder();
		
		for (String line : lines) {
			sb.append("% ");
			sb.append(line);
			sb.append("\n");
		}
		str = sb.toString();
	}

	@Override
	public String toString() {
		return str;
	}
}

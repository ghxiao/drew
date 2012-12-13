package org.semanticweb.drew.default_logic;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.drew.dlprogram.model.Literal;

import com.google.common.base.Joiner;

public class DefaultRule {
	private List<Literal> prerequisite;

	private List<List<Literal>> justifications;

	private List<Literal> conclusion;

	private List<Literal> typing;

	public List<Literal> getTyping() {
		return typing;
	}

	public void setTyping(List<Literal> typing) {
		this.typing = typing;
	}

	public DefaultRule() {
		this.prerequisite = new ArrayList<>();
		this.justifications = new ArrayList<>();
		this.conclusion = new ArrayList<>();
		this.typing = new ArrayList<>();
	}

	public List<Literal> getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(List<Literal> prerequisite) {
		this.prerequisite = prerequisite;
	}

	public List<List<Literal>> getJustifications() {
		return justifications;
	}

	public void setJustifications(List<List<Literal>> justifications) {
		this.justifications = justifications;
	}

	public List<Literal> getConclusion() {
		return conclusion;
	}

	public void setConclusion(List<Literal> conclusion) {
		this.conclusion = conclusion;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(Joiner.on(" & ").join(prerequisite)); //
		if (justifications.size() > 0) {
			sb.append("; ");

			boolean first = true;
			for (List<Literal> justification : justifications) {
				if (!first) {
					sb.append(", ");
				}
				first = false;
				Joiner.on(" & ").appendTo(sb, justification);
			}
		}
		// sb.append(Joiner.on(" , ").join(justifications)); //
		sb.append("] / [") //
				.append(Joiner.on(" & ").join(conclusion)) //
				.append("]");
		if (typing.size() > 0) {
			sb.append(" <").append(Joiner.on(" & ").join(typing)).append(">");
		}

		return sb.toString();
	}
}

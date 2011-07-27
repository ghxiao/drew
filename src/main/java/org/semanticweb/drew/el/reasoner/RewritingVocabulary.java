package org.semanticweb.drew.el.reasoner;

import org.semanticweb.drew.dlprogram.model.CacheManager;
import org.semanticweb.drew.dlprogram.model.NormalPredicate;

public class RewritingVocabulary {
	public final static NormalPredicate NOM = CacheManager.getInstance().getPredicate("nom", 1);
	public final static NormalPredicate CLASS = CacheManager.getInstance().getPredicate("cls", 1);
	public final static NormalPredicate ROLE = CacheManager.getInstance().getPredicate("rol", 1);
	
	public final static NormalPredicate INST = CacheManager.getInstance().getPredicate("inst", 2);

	public final static NormalPredicate SUB_CLASS = CacheManager.getInstance().getPredicate("subClass", 2);
	public final static NormalPredicate TOP = CacheManager.getInstance().getPredicate("top", 1);
	public final static NormalPredicate BOT = CacheManager.getInstance().getPredicate("bot", 1);
	public final static NormalPredicate SUB_CONJ = CacheManager.getInstance().getPredicate("subConj", 3);

	public final static NormalPredicate SUB_SELF = CacheManager.getInstance().getPredicate("subSelf", 2);
	public final static NormalPredicate SUP_SELF = CacheManager.getInstance().getPredicate("supSelf", 3);

	public final static NormalPredicate SUB_EX = CacheManager.getInstance().getPredicate("subEx", 3);
	public final static NormalPredicate SUP_EX = CacheManager.getInstance().getPredicate("supEx", 4);

	public final static NormalPredicate SUB_ROLE = CacheManager.getInstance().getPredicate("subRole", 4);
	public final static NormalPredicate SUB_R_CHAIN = CacheManager.getInstance().getPredicate("subRChain", 3);

	public final static NormalPredicate SUB_R_CONJ = CacheManager.getInstance().getPredicate("subRConj", 3);
	
	public final static String DL_ATOM = "dl_ATOM";
}

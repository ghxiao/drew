package org.semanticweb.drew.el.profile;

/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.profiles.LastPropertyInChainNotInImposedRange;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.profiles.UseOfAnonymousIndividual;
import org.semanticweb.owlapi.profiles.UseOfDataOneOfWithMultipleLiterals;
import org.semanticweb.owlapi.profiles.UseOfIllegalAxiom;
import org.semanticweb.owlapi.profiles.UseOfIllegalClassExpression;
import org.semanticweb.owlapi.profiles.UseOfIllegalDataRange;
import org.semanticweb.owlapi.profiles.UseOfObjectOneOfWithMultipleIndividuals;
import org.semanticweb.owlapi.profiles.UseOfObjectPropertyInverse;
import org.semanticweb.owlapi.util.OWLObjectPropertyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class SROELProfile implements OWLProfile {

	protected Set<IRI> allowedDatatypes;

	// private OWLOntology ont;

	public SROELProfile() {
		allowedDatatypes = new HashSet<IRI>();
		allowedDatatypes.add(OWLRDFVocabulary.RDF_PLAIN_LITERAL.getIRI());

		allowedDatatypes.add(OWLRDFVocabulary.RDF_XML_LITERAL.getIRI());
		allowedDatatypes.add(OWLRDFVocabulary.RDFS_LITERAL.getIRI());
		allowedDatatypes.add(OWL2Datatype.OWL_RATIONAL.getIRI());
		allowedDatatypes.add(OWL2Datatype.OWL_REAL.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_DECIMAL.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_DECIMAL.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_INTEGER.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_STRING.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_NORMALIZED_STRING.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_TOKEN.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_NAME.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_NCNAME.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_NMTOKEN.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_HEX_BINARY.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_BASE_64_BINARY.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_ANY_URI.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_DATE_TIME.getIRI());
		allowedDatatypes.add(OWL2Datatype.XSD_DATE_TIME_STAMP.getIRI());
	}

	@Override
	public String getName() {
		return "SROEL(D)";
	}

	@Override
	public OWLProfileReport checkOntology(OWLOntology ontology) {
		// this.ont = ontology;
		OWL2DLProfile profile = new OWL2DLProfile();
		OWLProfileReport report = profile.checkOntology(ontology);
		Set<OWLProfileViolation> violations = new HashSet<OWLProfileViolation>();
		violations.addAll(report.getViolations());
		OWLOntologyWalker ontologyWalker = new OWLOntologyWalker(
				ontology.getImportsClosure());
		SROELProfileObjectVisitor visitor = new SROELProfileObjectVisitor(
				ontologyWalker, ontology.getOWLOntologyManager());
		ontologyWalker.walkStructure(visitor);
		violations.addAll(visitor.getProfileViolations());
		return new OWLProfileReport(this, violations);
	}

	public OWLOntology extract(OWLOntology in, OWLProfileReport report)
			throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology out = manager.createOntology();
		Set<OWLAxiom> inAxioms = in.getAxioms();
		Set<OWLAxiom> outAxioms = new HashSet<OWLAxiom>(inAxioms);
		List<OWLProfileViolation> violations = report.getViolations();
		for(OWLProfileViolation v:violations){
			outAxioms.remove(v.getAxiom());
		}
		manager.addAxioms(out, outAxioms);
		
		return out;
	}

	protected class SROELProfileObjectVisitor extends
			OWLOntologyWalkerVisitor<Object> {

		private OWLOntologyManager man;

		private OWLObjectPropertyManager propertyManager;

		private Set<OWLProfileViolation> profileViolations = new HashSet<OWLProfileViolation>();

		public SROELProfileObjectVisitor(OWLOntologyWalker walker,
				OWLOntologyManager man) {
			super(walker);
			this.man = man;
		}

		public Set<OWLProfileViolation> getProfileViolations() {
			return new HashSet<OWLProfileViolation>(profileViolations);
		}

		private OWLObjectPropertyManager getPropertyManager() {
			if (propertyManager == null) {
				propertyManager = new OWLObjectPropertyManager(man,
						getCurrentOntology());
			}
			return propertyManager;
		}

		@Override
		public Object visit(OWLDatatype node) {
			if (!allowedDatatypes.contains(node.getIRI())) {
				profileViolations.add(new UseOfIllegalDataRange(
						getCurrentOntology(), getCurrentAxiom(), node));
			}
			return null;
		}

		@Override
		public Object visit(OWLAnonymousIndividual individual) {
			profileViolations.add(new UseOfAnonymousIndividual(
					getCurrentOntology(), getCurrentAxiom(), individual));
			return null;
		}

		@Override
		public Object visit(OWLObjectInverseOf property) {
			profileViolations.add(new UseOfObjectPropertyInverse(
					getCurrentOntology(), getCurrentAxiom(), property));
			return null;
		}

		@Override
		public Object visit(OWLDataAllValuesFrom desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLDataExactCardinality desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLDataMaxCardinality desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLDataMinCardinality desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLObjectAllValuesFrom desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLObjectComplementOf desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLObjectExactCardinality desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLObjectMaxCardinality desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLObjectMinCardinality desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLObjectOneOf desc) {
			if (desc.getIndividuals().size() != 1) {
				profileViolations
						.add(new UseOfObjectOneOfWithMultipleIndividuals(
								getCurrentOntology(), getCurrentAxiom(), desc));
			}
			return null;
		}

		@Override
		public Object visit(OWLObjectUnionOf desc) {
			profileViolations.add(new UseOfIllegalClassExpression(
					getCurrentOntology(), getCurrentAxiom(), desc));
			return null;
		}

		@Override
		public Object visit(OWLDataComplementOf node) {
			profileViolations.add(new UseOfIllegalDataRange(
					getCurrentOntology(), getCurrentAxiom(), node));
			return null;
		}

		@Override
		public Object visit(OWLDataOneOf node) {
			if (node.getValues().size() != 1) {
				profileViolations.add(new UseOfDataOneOfWithMultipleLiterals(
						getCurrentOntology(), getCurrentAxiom(), node));
			}
			return null;
		}

		@Override
		public Object visit(OWLDatatypeRestriction node) {
			profileViolations.add(new UseOfIllegalDataRange(
					getCurrentOntology(), getCurrentAxiom(), node));
			return null;
		}

		@Override
		public Object visit(OWLDataUnionOf node) {
			profileViolations.add(new UseOfIllegalDataRange(
					getCurrentOntology(), getCurrentAxiom(), node));
			return null;
		}

		@Override
		public Object visit(OWLAsymmetricObjectPropertyAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLDisjointDataPropertiesAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLDisjointObjectPropertiesAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLDisjointUnionAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLFunctionalObjectPropertyAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLHasKeyAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLInverseObjectPropertiesAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(OWLSymmetricObjectPropertyAxiom axiom) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					axiom));
			return null;
		}

		@Override
		public Object visit(SWRLRule rule) {
			profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(),
					rule));
			return super.visit(rule);
		}

		@Override
		public Object visit(OWLSubPropertyChainOfAxiom axiom) {
			Set<OWLObjectPropertyRangeAxiom> rangeAxioms = getCurrentOntology()
					.getAxioms(AxiomType.OBJECT_PROPERTY_RANGE, true);
			if (rangeAxioms.isEmpty()) {
				return false;
			}

			// Do we have a range restriction imposed on our super property?
			for (OWLObjectPropertyRangeAxiom rngAx : rangeAxioms) {
				if (getPropertyManager().isSubPropertyOf(
						axiom.getSuperProperty(), rngAx.getProperty())) {
					// Imposed range restriction!
					OWLClassExpression imposedRange = rngAx.getRange();
					// There must be an axiom that imposes a range on the last
					// prop in the chain
					List<OWLObjectPropertyExpression> chain = axiom
							.getPropertyChain();
					if (!chain.isEmpty()) {
						OWLObjectPropertyExpression lastProperty = chain
								.get(chain.size() - 1);
						boolean rngPresent = false;
						for (OWLOntology ont : getCurrentOntology()
								.getImportsClosure()) {
							for (OWLObjectPropertyRangeAxiom lastPropRngAx : ont
									.getObjectPropertyRangeAxioms(lastProperty)) {
								if (lastPropRngAx.getRange().equals(
										imposedRange)) {
									// We're o.k.
									rngPresent = true;
									break;
								}
							}
						}
						if (!rngPresent) {
							profileViolations
									.add(new LastPropertyInChainNotInImposedRange(
											getCurrentOntology(), axiom, rngAx));
						}
					}
				}
			}

			return null;
		}

		@Override
		public Object visit(OWLOntology ontology) {
			propertyManager = null;
			return null;
		}
	}

}

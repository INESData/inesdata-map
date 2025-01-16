package com.inesdatamap.mapperbackend.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitor;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;

public class LabelExtractor implements OWLAnnotationObjectVisitor {

	@Nullable
	String result = null;

	@Override
	public void visit(OWLAnonymousIndividual individual) {}

	@Override
	public void visit(IRI iri) {}

	@Override
	public void visit(OWLLiteral literal) {}

	@Override
	public void visit(@Nonnull OWLAnnotation node) {
		/*
		 * If it's a label, grab it as the result. Note that if there are multiple labels, the last
		 * one will be used.
		 */
		if (node.getProperty().isLabel()) {
			OWLLiteral c = (OWLLiteral) node.getValue();
			result = c.getLiteral();
		}
	}

	@Override
	public void visit(OWLAnnotationAssertionAxiom axiom) {}

	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom axiom) {}

	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom axiom) {}

	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {}

	@SuppressWarnings("unused")
	public void visit(OWLAnnotationProperty property) {}

	@SuppressWarnings("unused")
	public void visit(OWLAnnotationValue value) {}

	@Nullable
	public String getResult() {
		return result;
	}
}

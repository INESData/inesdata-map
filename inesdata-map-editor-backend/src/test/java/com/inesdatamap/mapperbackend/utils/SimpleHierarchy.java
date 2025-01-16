package com.inesdatamap.mapperbackend.utils;

import java.io.PrintStream;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import static org.semanticweb.owlapi.search.EntitySearcher.getAnnotationObjects;

public class SimpleHierarchy {

	private static final int INDENT = 4;
	@Nonnull
	private final OWLReasonerFactory reasonerFactory;
	@Nonnull
	private final OWLOntology ontology;

	private final PrintStream out;

	public SimpleHierarchy(@Nonnull OWLReasonerFactory reasonerFactory, @Nonnull OWLOntology inputOntology) {
		this.reasonerFactory = reasonerFactory;
		ontology = inputOntology;
		out = System.out;
	}

	/**
	 * Print the class hierarchy for the given ontology from this class down, assuming this class is
	 * at the given level. Makes no attempt to deal sensibly with multiple inheritance.
	 */
	public void printHierarchy(@Nonnull OWLClass clazz) {
		OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
		printHierarchy(reasoner, clazz, 0);
		/* Now print out any unsatisfiable classes */
		for (OWLClass cl : ontology.getClassesInSignature()) {
			assert cl != null;
			if (!reasoner.isSatisfiable(cl)) {
				out.println("XXX: " + labelFor(cl));
			}
		}
		reasoner.dispose();
	}

	private String labelFor(@Nonnull OWLClass clazz) {
		/*
		 * Use a visitor to extract label annotations
		 */
		LabelExtractor le = new LabelExtractor();

		getAnnotationObjects(clazz, ontology).forEach(anno -> anno.accept(le));

		/* Print out the label if there is one. If not, just use the class URI */
		if (le.getResult() != null) {
			return le.getResult();
		} else {
			return clazz.getIRI().toString();
		}
	}

	/**
	 * Print the class hierarchy from this class down, assuming this class is at the given level.
	 * Makes no attempt to deal sensibly with multiple inheritance.
	 */
	private void printHierarchy(@Nonnull OWLReasoner reasoner, @Nonnull OWLClass clazz, int level) {
		/*
		 * Only print satisfiable classes -- otherwise we end up with bottom everywhere
		 */
		if (reasoner.isSatisfiable(clazz)) {
			for (int i = 0; i < level * INDENT; i++) {
				out.print(" ");
			}
			out.println(labelFor(clazz));
			/* Find the children and recurse */
			for (OWLClass child : reasoner.getSubClasses(clazz, true).getFlattened()) {
				if (!child.equals(clazz)) {
					printHierarchy(reasoner, child, level + 1);
				}
			}
		}
	}
}

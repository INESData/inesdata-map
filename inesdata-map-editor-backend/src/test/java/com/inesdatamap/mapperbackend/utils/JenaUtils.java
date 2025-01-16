package com.inesdatamap.mapperbackend.utils;

import java.io.InputStream;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

public class JenaUtils {

	public static void printWithJena(String fileName, Lang format) {

		OntModel m = ModelFactory.createOntologyModel();
		InputStream file = JenaUtils.class.getClassLoader().getResourceAsStream(fileName);
		RDFDataMgr.read(m, file, format);

		System.out.println("***********  OBJECTS  *************");
		m.listObjects().forEach(rdfNode -> System.out.println(rdfNode));

		System.out.println("***********  CLASSES  *************");
		m.listClasses().forEach(rdfNode -> System.out.println(rdfNode.toString()));

		System.out.println("*********** NAMED CLASSES  *********");
		m.listNamedClasses().forEach(rdfNode -> System.out.println(rdfNode.toString()));

		System.out.println("*********** Object Properties  *********");
		m.listObjectProperties().forEach(rdfNode -> System.out.println(rdfNode.toString()));

		System.out.println("*********** Individuals  *********");
		m.listIndividuals().forEach(rdfNode -> System.out.println(rdfNode.toString()));

		System.out.println("*********** Annotation Properties  *********");
		m.listAnnotationProperties().forEach(rdfNode -> System.out.println(rdfNode.toString()));

		System.out.println("*********** SKOS  *********");
		ResIterator iter = m.listResourcesWithProperty(RDF.type, SKOS.Concept);
		while (iter.hasNext()) {
			Resource concept = iter.nextResource();
			System.out.println("Concept: " + concept.getURI());
		}
	}

}

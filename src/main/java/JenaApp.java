import org.apache.jena.graph.Graph;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.VCARD;

import java.io.InputStream;
import java.util.Iterator;

import static org.apache.jena.ontology.OntDocumentManager.NS;
import static org.apache.jena.ontology.OntModelSpec.OWL_MEM;
import static org.apache.jena.ontology.OntModelSpec.OWL_MEM_MICRO_RULE_INF;

public class JenaApp {
    static final String johnSmithUri = "http://somewhere/JohnSmith";

    public static void main(String[] args) {
//        Model model = readOntologyFromFile("ontology.rdf");
//        Model basicModel = createBasicModel();
//        basicOperations(basicModel);
//        printGraph(basicModel);
        createOntology();
    }

    private static void printGraph(Model basicModel) {
        Graph newGraph = basicModel.getGraph();
        System.out.println(newGraph);
    }

    private static void basicOperations(Model model) {
        // retrieve the John Smith vcard resource from the model
        Resource vcard = model.getResource(johnSmithUri);
        String fullName = vcard.getProperty(VCARD.FN).getString();

        System.out.println(fullName);
    }

    private static Model createBasicModel() {
        // some definitions
        String personURI = "http://somewhere/JohnSmith";
        String fullName = "John Smith";

        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        Resource johnSmith = model.createResource(personURI);

        // add the property
        johnSmith.addProperty(VCARD.FN, fullName);

//        System.out.println(johnSmith.toString());
//
//        printPropertiesOfModel(johnSmith);

//        model.write(System.out);
//        model.write(System.out, "RDF/XML-ABBREV");
//        model.write(System.out, "N-TRIPLES");

        return model;
    }

    private static void printPropertiesOfModel(Resource johnSmith) {
        StmtIterator stmtIterator = johnSmith.listProperties();
        while (stmtIterator.hasNext()) {
            Statement next = stmtIterator.next();
            System.out.println(next.getSubject());
            System.out.println(next.getPredicate());
            System.out.println(next.getObject());
            System.out.println("------------");
        }
    }

    private static Model readOntologyFromFile(String inputFileName) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model.read(in, null);

        // write it to standard out
        model.write(System.out);
        return model;
    }

    private static void createOntology() {
        OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
        OntClass programme = m.createClass( NS + "Programme" );
        OntClass orgEvent = m.createClass( NS + "OrganizedEvent" );

        ObjectProperty hasProgramme = m.createObjectProperty( NS + "hasProgramme" );

        hasProgramme.addDomain( orgEvent );
        hasProgramme.addRange( programme );
        hasProgramme.addLabel( "has programme", "en" );

        m.write(System.out);
    }

}

package org.openinfoloka.goosefish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import com.yahoo.labs.samoa.instances.Attribute;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.InstancesHeader;

import moa.classifiers.Classifier;
import moa.classifiers.functions.SPegasos;

public class OnlineClassifier {

  private Classifier learner;
  private Queue<Instance> learningQueue;
  private int dimension;
  private Instances instances;
  private double[] featureValues;
  
  private ArrayList<String> boolAttValues = new ArrayList<String>(
      Arrays.asList("0", "1"));

  private HashMap<String, Integer> attributesIndex;

  private int hashTrickSize;
  private int batchSize;

  private void initInstances() {
    // gather attributes
    List<Attribute> attributes = new ArrayList<Attribute>();
    ArrayList<String> allowedClasses = new ArrayList<String>();
    allowedClasses.add("topic");
    allowedClasses.add("nontopic");
    Attribute classAttribute = new Attribute("class", allowedClasses);
    attributes.add(classAttribute);
    // this looks somehow stupid to me :/
    List<String> vector = null;
    attributes.add(new Attribute("url", vector));
    attributes.add(new Attribute("topicpar"));
    attributes.add(new Attribute("nontopicpar"));
    attributes.add(new Attribute("topicsib"));
    attributes.add(new Attribute("nontopicsib"));
    for (int i = 0; i < hashTrickSize; i++) {
      // the boolAttValues here should not be necessary but based on some
      // runtime experiements they make a (slight) difference as it is not
      // possible to create directly boolean attributes. The time to
      // define a split is reduced by doing this with nominal.
      attributes.add(new Attribute(getAttributeNameOfHash(i), boolAttValues));
    }
    // now we create the Instances
    instances = new Instances("Goosefish", attributes, 1);
    // instances.setClass(classAttribute);
    attributesIndex = new HashMap<String, Integer>();
    for (int i = 0; i < attributes.size(); i++) {
      attributesIndex.put(attributes.get(i).name(), i);
    }
    // set dimension (class + domain + 4xgraph + hashes)
    dimension = 1 + 1 + 4 + hashTrickSize;
    // init replacement array
    featureValues = new double[dimension];
    for (int i = 0; i < dimension; i++) {
      featureValues[i] = 0.0;
    }
  }

  public OnlineClassifier(String classifierName, String classifierOptions,
      int hashTrickSize, int batchSize) {
    this.hashTrickSize = hashTrickSize;
    this.batchSize = batchSize;
    initInstances();
    learner = new SPegasos();
    learner.setModelContext(new InstancesHeader(instances));
    if (classifierOptions != null && !classifierOptions.equals("")) {
      learner.getOptions().setViaCLIString(classifierOptions);
    }
    learner.prepareForUse();
  }
  
  /**
   * Initiates a learning process.
   */
  private void learn() {
    int listSize = learningQueue.size();
    for (int i = 0; i < listSize; i++) {
      Instance inst = learningQueue.poll();
      if (inst != null)
        learner.trainOnInstance(inst);
    }
  }

  /**
   * Returns the name of the hash-attribute for position i
   * 
   * @param i
   *          the position of the hash
   * @return the corresponding attribute name.
   */
  private static String getAttributeNameOfHash(int i) {
    return "hash" + i;
  }
}
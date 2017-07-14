package org.openinfoloka.goosefish.models;

import java.net.URI;

public class URL implements Comparable<URL> {

  private long id;
  private URI uri;
  private String anchorText; 
  private boolean topicFather;
  private boolean nonTopicFather;
  private boolean topicSibling;
  private boolean nonTopicSibling;
  private boolean topic = false;
  private double prediction;
  
  @Override
  public int compareTo(URL o) {
    return 0;
  }

  public long getId() {
    return id;
  }

  public URI getUri() {
    return uri;
  }

  public String getAnchorText() {
    return anchorText;
  }

  public boolean isTopicFather() {
    return topicFather;
  }

  public boolean isNonTopicFather() {
    return nonTopicFather;
  }

  public boolean isTopicSibling() {
    return topicSibling;
  }

  public boolean isNonTopicSibling() {
    return nonTopicSibling;
  }

  public boolean isTopic() {
    return topic;
  }

  public double getPrediction() {
    return prediction;
  }

}
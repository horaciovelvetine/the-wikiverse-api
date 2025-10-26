package edu.velvet.Wikiverse.api.services.wikidata;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WikidataReference {
  private final List<WikidataSnakGroup> snakGroups = new ArrayList<>();

  public List<WikidataSnakGroup> getSnakGroups() {
    return snakGroups;
  }

  public void addSnakGroup(WikidataSnakGroup snakGroup) {
    this.snakGroups.add(snakGroup);
  }
}

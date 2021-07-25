package com.hugomarques.githubasync.lookup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Class used to parse reponses from github user API.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
  private String name;
  private String blog;
}

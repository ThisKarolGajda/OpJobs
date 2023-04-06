package me.opkarol.jobs;

import java.io.Serializable;
import java.util.List;

public record JobEvent(List<String> events, String displayName) implements Serializable {

}

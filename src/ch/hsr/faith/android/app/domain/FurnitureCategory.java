package ch.hsr.faith.android.app.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FurnitureCategory {

	private Long id;
	private String name;
	private FurnitureCategory parent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FurnitureCategory getParent() {
		return parent;
	}

	public void setParent(FurnitureCategory parent) {
		this.parent = parent;
	}

}

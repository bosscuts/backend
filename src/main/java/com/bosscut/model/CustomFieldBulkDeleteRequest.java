package com.bosscut.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldBulkDeleteRequest implements Serializable {

	private List<Long> ids;
	private String language;

	public static class Builder  {

		private List<Long> ids;
		private String language;

		public Builder() {
		}

		public Builder ids(List<Long> ids) {
			this.ids = ids;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public CustomFieldBulkDeleteRequest build() {
			CustomFieldBulkDeleteRequest request = new CustomFieldBulkDeleteRequest();
			request.ids = ids;
			request.language = language;
			return request;
		}
	}
}

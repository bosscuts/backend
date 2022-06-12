package com.bosscut.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDeleteRequest implements Serializable {

	private Long id;
	private String language;

	public static class Builder  {

		private Long id;
		private String language;

		public Builder() {
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public ArticleDeleteRequest build() {
			ArticleDeleteRequest request = new ArticleDeleteRequest();
			request.id = id;
			request.language = language;
			return request;
		}
	}
}

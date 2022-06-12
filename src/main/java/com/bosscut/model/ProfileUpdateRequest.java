package com.bosscut.model;


import com.bosscut.domain.PersonalName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
	private long userId;
	private String email;
	private String loginId;
	private PersonalName name;
}

package io.cooltime.gateway.entity.user;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name= "user_detail")
public class UserDetail {

	@Id
	private String id;

	private String email;

	private String familyName;

	private String givenName;

	private String birthDate;
	private String gender;
	private String locale;
	private String nickname;
	private String picture;
	private String phoneNumber;
	private String phoneNumberVerified;

	@CreatedDate
	@Transient
	private LocalDateTime createdAt;

	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

}
